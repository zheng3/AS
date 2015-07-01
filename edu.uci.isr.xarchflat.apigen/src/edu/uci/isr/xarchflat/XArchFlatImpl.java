package edu.uci.isr.xarchflat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.WeakHashMap;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import edu.uci.isr.sysutils.SystemUtils;
import edu.uci.isr.xarch.DOMBased;
import edu.uci.isr.xarch.IXArch;
import edu.uci.isr.xarch.IXArchContext;
import edu.uci.isr.xarch.IXArchElement;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchEvent;
import edu.uci.isr.xarch.XArchInstanceMetadata;
import edu.uci.isr.xarch.XArchListener;
import edu.uci.isr.xarch.XArchPropertyMetadata;
import edu.uci.isr.xarch.XArchSerializeException;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchUtils;

public class XArchFlatImpl
    implements XArchFlatInterface, XArchFileListener, XArchListener, XArchEventProvider{

	static class WeakValueMap<K, V>
	    implements Map<K, V>{

		final WeakHashMap<K, WeakReference<V>> m;

		public WeakValueMap(){
			m = new WeakHashMap<K, WeakReference<V>>();
		}

		public WeakValueMap(int initialCapacity, float loadFactor){
			m = new WeakHashMap<K, WeakReference<V>>(initialCapacity, loadFactor);
		}

		public WeakValueMap(int initialCapacity){
			m = new WeakHashMap<K, WeakReference<V>>(initialCapacity);
		}

		public V get(Object k){
			WeakReference<V> wr = m.get(k);
			if(wr != null){
				return wr.get();
			}
			return null;
		}

		public V put(K k, V v){
			WeakReference<V> wr = m.put(k, new WeakReference<V>(v));
			if(wr != null){
				return wr.get();
			}
			return null;
		}

		public void clear(){
			m.clear();
		}

		public boolean containsKey(Object key){
			return m.containsKey(key);
		}

		public boolean containsValue(Object value){
			throw new UnsupportedOperationException();
		}

		public Set<Entry<K, V>> entrySet(){
			return new AbstractSet<Entry<K, V>>(){

				final Set<Entry<K, WeakReference<V>>> s = m.entrySet();

				@Override
				public Iterator<Entry<K, V>> iterator(){

					final Iterator<Entry<K, WeakReference<V>>> i = s.iterator();

					return new Iterator<Entry<K, V>>(){

						public boolean hasNext(){
							return i.hasNext();
						}

						public Entry<K, V> next(){

							final Entry<K, WeakReference<V>> n = i.next();

							return new Map.Entry<K, V>(){

								public K getKey(){
									return n.getKey();
								}

								public V getValue(){
									return n.getValue().get();
								}

								public V setValue(V value){
									return n.setValue(new WeakReference<V>(value)).get();
								}
							};
						}

						public void remove(){
							i.remove();
						}
					};
				}

				@Override
				public int size(){
					return s.size();
				}
			};
		}

		@Override
		public boolean equals(Object o){
			throw new UnsupportedOperationException();
		}

		@Override
		public int hashCode(){
			throw new UnsupportedOperationException();
		}

		public boolean isEmpty(){
			return m.isEmpty();
		}

		public Set<K> keySet(){
			return m.keySet();
		}

		public void putAll(Map<? extends K, ? extends V> m){
			throw new UnsupportedOperationException();
		}

		public V remove(Object key){
			WeakReference<V> wr = m.remove(key);
			if(wr != null){
				return wr.get();
			}
			return null;
		}

		public int size(){
			return m.size();
		}

		@Override
		public String toString(){
			return m.toString();
		}

		public Collection<V> values(){
			throw new UnsupportedOperationException();
		}

	}

	// Map URLs to xArches
	private Map<String, ObjRef> xArches = new HashMap<String, ObjRef>();

	private Object tableLock = new Object();

	// Map ObjRef -> Objects
	// private HashMap objects = new HashMap(5000, 0.60f);
	private Map<ObjRef, Object> objects = new HashMap<ObjRef, Object>(5000, 0.60f);

	// Map Objects -> ObjRefs (for reverse-lookup purposes)...
	// private HashMap reverseObjects = new HashMap(5000, 0.60f);
	private Map<Object, ObjRef> reverseObjects = new HashMap<Object, ObjRef>(5000, 0.60f);

	private Vector xArchFileListeners = new Vector();

	private Vector xArchFlatListeners = new Vector();

	private IdTable idTable;

	private XMLLinkResolver resolver;

	protected boolean emitUnattachedEvents = false;

	// Cache contexts so we don't create a zillion of them.
	private ContextCache contextCache;

	// This is a dirty hack that I put here because DOM 2 sucks
	// and it should go away when we standardize on DOM 3/Java 1.5
	// and upgrade the data binding library.
	//
	// When we do an add() or a set(), the data binding library
	// will change the underlying DOM node of the WrapperImpl.
	// This means that the hashCode() changes, so the reverseObjects
	// table stops working right, because we hashed the WrapperImpl
	// with the original hashCode, which has now changed. So, when
	// we go to look up the WrapperImpl's ObjRef, we'll fail unless
	// we rehash the element after the add() or set()
	// with the new hashCode. This seems all well and good--we should
	// just be able to remove it from the reverseObjects table, do the
	// add() or set(), and then re-add it to the table. HOWEVER, the
	// add() or set() will have the side effect of throwing an XArchEvent,
	// and the handler for that uses the reverseObjects table. So, what
	// we have to do is store the wrapper-to-objref mapping temporarily
	// in a special cache (the gutschange cache) so while the wrapperimpl
	// is temporarily missing from the reverseObjects table we can still
	// look up its ObjRef.
	private Vector gutsChangeCache = new Vector();

	private static class GutsChangeMapping{

		public Object o;

		public ObjRef ref;
	}

	public XArchFlatImpl(){
		contextCache = new ContextCache();
		idTable = new IdTable();
		idTable.addXArchFileListener(this);
		resolver = new XMLLinkResolver(idTable);
	}

	public void setEmitUnattachedEvents(boolean emitUnattachedEvents){
		this.emitUnattachedEvents = emitUnattachedEvents;
	}

	public boolean getEmitUnattachedEvents(){
		return this.emitUnattachedEvents;
	}

	private void putGutsChangeMap(Object o, ObjRef ref){
		synchronized(tableLock){
			GutsChangeMapping gcm = new GutsChangeMapping();
			gcm.o = o;
			gcm.ref = ref;
			gutsChangeCache.add(0, gcm);
		}
	}

	private ObjRef getGutsChangeMap(Object o){
		synchronized(tableLock){
			for(int i = 0; i < gutsChangeCache.size(); i++){
				GutsChangeMapping gcm = (GutsChangeMapping)gutsChangeCache.elementAt(i);
				if(gcm.o.equals(o)){
					return gcm.ref;
				}
			}
			return null;
		}
	}

	@Deprecated
	public static final String uncapFirstLetter(String s){
		return SystemUtils.uncapFirst(s);
	}

	@Deprecated
	public static final String capFirstLetter(String s){
		return SystemUtils.capFirst(s);
	}

	@Deprecated
	private static Collection arrayToCollection(Object[] arr){
		return new ArrayList(Arrays.asList(arr));
	}

	private ObjRef put(Object o){
		return put(o, null);
	}

	private ObjRef put(Object o, ObjRef objRef){
		synchronized(tableLock){
			ObjRef alreadyInThere = reverseObjects.get(o);
			if(alreadyInThere != null){
				// System.out.println("That was already in there! : " + o);
				return alreadyInThere;
			}
			alreadyInThere = getGutsChangeMap(o);
			if(alreadyInThere != null){
				// System.out.println("That was already in the gutschange cache!
				// : " + o);
				return alreadyInThere;
			}

			if(objRef == null){
				do{
					objRef = new ObjRef();
				} while(objects.get(objRef) != null);
			}

			// System.out.println("Dude, that wasn't in there: " + o);
			// System.out.println("I'm going to call that: " + objRef);

			objects.put(objRef, o);
			reverseObjects.put(o, objRef); // .duplicate()); // <-- breaks garbage collection
			return objRef;
		}
	}

	private Object get(ObjRef ref){
		synchronized(tableLock){
			Object o = objects.get(ref);
			if(o == null){
				// System.out.println("Table is: " + objects);
				throw new NoSuchObjectException(ref);
			}
			return o;
		}
	}

	public boolean isValidObjRef(ObjRef ref){
		synchronized(tableLock){
			Object o = objects.get(ref);
			if(o != null){
				return true;
			}
			else{
				return false;
			}
		}
	}

	public void cleanup(ObjRef xArchRef){
		synchronized(tableLock){
			ObjRef[] refs = objects.keySet().toArray(new ObjRef[objects.size()]);
			for(int i = 0; i < refs.length; i++){
				ObjRef refRoot = getXArch(refs[i]);
				if(refRoot.equals(xArchRef)){
					Object val = get(refs[i]);
					if(val instanceof IXArchElement){
						if(!isAttached(refs[i])){
							objects.remove(refs[i]);
							reverseObjects.remove(val);
						}
					}
				}
			}
			System.gc();
		}
	}

	static MethodCache methodCache = new MethodCache();

	private static Method getMethod(Class c, String name){
		Method m = methodCache.getMethod(c, name);
		if(m == null){
			throw new InvalidOperationException(c, name);
		}
		return m;
		/*
		 * Method[] methods = c.getMethods(); for(int i = 0; i < methods.length;
		 * i++){ if(methods[i].getName().equals(name)){ return methods[i]; } }
		 * throw new InvalidOperationException(c, name);
		 */
	}

	public ObjRef createXArch(String url){
		ObjRef cachedRef = xArches.get(url);
		if(cachedRef != null){
			throw new IllegalArgumentException("An open xArch with that URL has already been created.");
		}
		// System.out.println("Creating new xArch.");
		IXArch xArch = idTable.createXArch(url);
		// This will, via a callback, make the xArch import in here, so now
		// it'll magically
		// be in the ID table and we just have to get it.
		ObjRef ref = xArches.get(url);
		if(ref == null){
			throw new RuntimeException("Bad mojo!");
		}
		return ref;
	}

	public ObjRef cloneXArch(ObjRef xArchRef, String newURI){
		ObjRef cachedRef = xArches.get(newURI);
		if(cachedRef != null){
			throw new IllegalArgumentException("An open xArch with that URL has already been created.");
		}
		Object to = get(xArchRef);
		if(!(to instanceof edu.uci.isr.xarch.IXArch)){
			throw new IllegalArgumentException("Target of clone must be of type IXArch");
		}
		IXArch xArchToClone = (IXArch)to;

		// System.out.println("Creating new xArch.");
		IXArch clonedXArch = idTable.cloneXArch(xArchToClone, newURI);
		// This will, via a callback, make the xArch import in here, so now
		// it'll magically
		// be in the ID table and we just have to get it.
		ObjRef ref = xArches.get(newURI);
		if(ref == null){
			throw new RuntimeException("Bad mojo!");
		}
		return ref;
	}

	//	public ObjRef cloneXArchElementDepthOne(ObjRef targetRef, String prefix){
	//		Object to = get(targetRef);
	//		if(!(to instanceof IXArchElement)){
	//			throw new IllegalArgumentException("Target of cloneXArchElementDepthOne must be of type IXArchElement");
	//		}
	//		if(!(to instanceof DOMBased)){
	//			throw new IllegalArgumentException("Target of cloneXArchElementDepthOne must be of DOM-based");
	//		}
	//
	//		IXArchElement targetElt = (IXArchElement)to;
	//		Element domElt = (Element)((DOMBased)targetElt).getDOMNode();
	//		NodeList nl = domElt.getChildNodes();
	//
	//		IXArchElement targetEltClone = targetElt.cloneElement(1);
	//		ObjRef cloneRef = put(targetEltClone, prefix + targetRef.getUID());
	//
	//		// System.out.println("Dooped objRef = " + cloneRef);
	//		// Okay, we have cloned the target element and its children, but we need
	//		// to clone
	//		// the associated ObjRefs of its children
	//		Element domEltOfClone = (Element)((DOMBased)targetElt).getDOMNode();
	//		NodeList clonenl = domEltOfClone.getChildNodes();
	//
	//		int size = nl.getLength();
	//		IXArch xArch = targetElt.getXArch();
	//		for(int i = 0; i < size; i++){
	//			Node n = nl.item(i);
	//			Node clonen = clonenl.item(i);
	//			if(n instanceof Element){
	//				// Make a wrapper for the child so we can look it up
	//				Object wrapper = IdTable.makeWrapper(xArch, (Element)n);
	//				// Find the child of the original node
	//				ObjRef originalRef = reverseObjects.get(wrapper);
	//				// If the child of the original node wasn't already ObjReffed,
	//				// ref it now.
	//				if(originalRef == null){
	//					originalRef = put(wrapper);
	//				}
	//				// Now that we have the original object reffed for sure,
	//				// ref its clone.
	//				Object cloneWrapper = IdTable.makeWrapper(xArch, (Element)clonen);
	//				put(cloneWrapper, prefix + originalRef.getUID());
	//			}
	//		}
	//
	//		return cloneRef;
	//	}

	private void importXArch(String url){
		if(xArches.get(url) != null){
			return;
		}

		IXArch xArch = idTable.getXArch(url);
		if(xArch == null){
			throw new IllegalArgumentException("Attempt to import invalid URL.");
		}

		ObjRef objRef = put(xArch);
		xArch.addXArchListener(this);
		xArches.put(url, objRef);
	}

	public void renameXArch(String oldURI, String newURI){
		synchronized(xArches){
			if(oldURI == null){
				throw new IllegalArgumentException("URI to rename was null.");
			}
			ObjRef xArchRef = xArches.get(oldURI);
			if(xArchRef == null){
				throw new IllegalArgumentException("Invalid URI to close.");
			}
			xArches.remove(oldURI);
			xArches.put(newURI, xArchRef);
			XArchFileEvent evt = new XArchFileEvent(XArchFileEvent.XARCH_RENAMED_EVENT, oldURI, newURI, xArchRef);
			fireXArchFileEvent(evt);
		}
	}

	public ObjRef parseFromFile(String fileName) throws FileNotFoundException, IOException, SAXException{
		File f = new File(fileName);
		return parseFromURL(f.toURL().toString());
	}

	public ObjRef parseFromString(String uri, String contents) throws IOException, SAXException{
		ObjRef cachedRef = xArches.get(uri);
		if(cachedRef != null){
			return cachedRef;
		}
		else{
			IXArch xArch = idTable.parseFromReader(uri, new StringReader(contents));

			ObjRef ref = xArches.get(uri);
			if(ref == null){
				throw new RuntimeException("Bad mojo!");
			}
			return ref;
		}
	}

	public ObjRef parseFromURL(String urlString) throws MalformedURLException, IOException, SAXException{
		ObjRef cachedRef = xArches.get(urlString);
		if(cachedRef != null){
			return cachedRef;
		}
		else{
			IXArch xArch = idTable.parseFromURL(urlString);

			// The following now happens by import magic
			// ObjRef ref = put(xArch);
			// xArches.put(urlString, ref);
			// xArch.addXArchListener(this);

			ObjRef ref = xArches.get(urlString);
			if(ref == null){
				throw new RuntimeException("Bad mojo!");
			}
			return ref;
		}

		/*
		 * URL url = new URL(urlString); InputStreamReader isr = new
		 * InputStreamReader(url.openStream()); IXArch xArch =
		 * XArchUtils.parse(isr); ObjRef ref = new ObjRef(); objects.put(ref,
		 * xArch); xArches.put(urlString, ref); xArch.addXArchListener(this);
		 * return ref;
		 */
	}

	public String[] getOpenXArchURLs(){
		return getOpenXArchURIs();
	}

	public String[] getOpenXArchURIs(){
		synchronized(xArches){
			return xArches.keySet().toArray(new String[xArches.size()]);
		}
	}

	public ObjRef[] getOpenXArches(){
		synchronized(xArches){
			ObjRef[] arr = new ObjRef[xArches.size()];
			int i = 0;
			for(Object element: xArches.values()){
				arr[i++] = (ObjRef)element;
			}
			return arr;
		}
	}

	public boolean equals(ObjRef ref1, ObjRef ref2){
		if(ref1 == ref2){
			return true;
		}
		if(ref1.equals(ref2)){
			return true;
		}
		Object to1 = get(ref1);
		Object to2 = get(ref2);
		return to1.equals(to2);
	}

	public String getXArchURL(ObjRef xArchRef){
		return getXArchURI(xArchRef);
	}

	public String getXArchURI(ObjRef xArchRef){
		if(xArchRef == null){
			throw new IllegalArgumentException("Null ObjRef passed to getXArchURL");
		}
		Object to = get(xArchRef);
		if(!(to instanceof edu.uci.isr.xarch.IXArch)){
			throw new IllegalArgumentException("Target of serialization must be of type IXArch");
		}
		for(Map.Entry<String, ObjRef> entry: xArches.entrySet()){
			if(equals(xArchRef, entry.getValue())){
				return entry.getKey();
			}
		}
		return null;
	}

	public ObjRef getOpenXArch(String url){
		if(url == null){
			throw new IllegalArgumentException("Null URL passed to getOpenXArch");
		}
		return xArches.get(url);
	}

	public void close(String url){
		if(url == null){
			throw new IllegalArgumentException("URL to close was null.");
		}
		ObjRef xArchRef = xArches.get(url);
		if(xArchRef == null){
			throw new IllegalArgumentException("Invalid URL to close.");
		}
		close(xArchRef);
	}

	public void close(ObjRef xArchRef){
		Object to = get(xArchRef);
		if(!(to instanceof edu.uci.isr.xarch.IXArch)){
			throw new IllegalArgumentException("Target of close must be of type IXArch");
		}

		String uri = getXArchURI(xArchRef);
		contextCache.removeAll(xArchRef);
		idTable.forgetXArch((IXArch)to);
		forgetXArch(xArchRef);
		XArchFileEvent closeEvent = new XArchFileEvent(XArchFileEvent.XARCH_CLOSED_EVENT, uri, xArchRef);
		fireXArchFileEvent(closeEvent);
	}

	private void forgetXArch(ObjRef xArchRef){
		// ObjRef xArchRef = (ObjRef)xArches.get(url);
		if(xArchRef == null){
			throw new IllegalArgumentException("Invalid document to close.");
		}

		Object to = get(xArchRef);
		if(!(to instanceof edu.uci.isr.xarch.IXArch)){
			throw new IllegalArgumentException("Target of close must be of type IXArch");
		}

		synchronized(tableLock){
			for(Iterator<Map.Entry<Object, ObjRef>> i = reverseObjects.entrySet().iterator(); i.hasNext();){
				Map.Entry<Object, ObjRef> e = i.next();
				Object o = e.getKey();
				if(o instanceof IXArchElement){
					if(to.equals(((IXArchElement)o).getXArch())){
						objects.remove(e.getValue());
						i.remove();
					}
				}
				else if(o instanceof IXArchContext){
					if(to.equals(((IXArchContext)o).getXArch())){
						objects.remove(e.getValue());
						i.remove();
					}
				}
				else{
					System.err.println("Cannot remove element: " + o);
				}
			}
			for(Iterator<Map.Entry<ObjRef, Object>> i = objects.entrySet().iterator(); i.hasNext();){
				Map.Entry<ObjRef, Object> e = i.next();
				Object o = e.getValue();
				if(o instanceof IXArchElement){
					if(to.equals(((IXArchElement)o).getXArch())){
						reverseObjects.remove(e.getValue());
						i.remove();
					}
				}
				else if(o instanceof IXArchContext){
					if(to.equals(((IXArchContext)o).getXArch())){
						reverseObjects.remove(e.getValue());
						i.remove();
					}
				}
				else{
					System.err.println("Cannot remove element: " + o);
				}
			}
			synchronized(xArches){
				while(xArches.values().remove(xArchRef)){
					// remove() only removes one instance
					// the loop ensures that all instances are removed
				}
			}
		}
	}

	public String serialize(ObjRef xArchRef){
		Object to = get(xArchRef);
		if(!(to instanceof edu.uci.isr.xarch.IXArch)){
			throw new IllegalArgumentException("Target of serialization must be of type IXArch");
		}
		try{
			return idTable.getXArchImplementation().serialize((edu.uci.isr.xarch.IXArch)to, null);
		}
		catch(XArchSerializeException e){
			// This shouldn't happen.
			throw new RuntimeException(e);
		}
	}

	public void writeToFile(ObjRef xArchRef, String fileName) throws java.io.IOException{
		String serializedForm = serialize(xArchRef);
		FileWriter fw = new FileWriter(fileName);
		fw.write(serializedForm);
		fw.close();
		fireXArchFileEvent(new XArchFileEvent(XArchFileEvent.XARCH_SAVED_EVENT, getXArchURL(xArchRef), new File(fileName).toURL().toString(), xArchRef));
	}

	public boolean isAttached(ObjRef childRef){
		ObjRef xArchRef = getXArch(childRef);
		return hasAncestor(childRef, xArchRef);
	}

	public String getElementName(ObjRef ref){
		Object elt = get(ref);
		if(!(elt instanceof IXArchElement)){
			throw new IllegalArgumentException("Targets of getElementName(...) must be IXArchElements");
		}
		if(!(elt instanceof DOMBased)){
			throw new IllegalArgumentException("Targets of getElementName(...) must be DOM-based");
		}
		Node node = ((DOMBased)elt).getDOMNode();
		return node.getLocalName();
	}

	public boolean hasAncestor(ObjRef childRef, ObjRef ancestorRef){
		Object child = get(childRef);
		Object ancestor = get(ancestorRef);
		if(!(child instanceof IXArchElement)){
			throw new IllegalArgumentException("Targets of hasAncestor(...) must be IXArchElements");
		}
		if(!(ancestor instanceof IXArchElement)){
			throw new IllegalArgumentException("Targets of hasAncestor(...) must be IXArchElements");
		}

		if(!(child instanceof DOMBased)){
			throw new IllegalArgumentException("Targets of hasAncestor(...) must be DOM-based");
		}
		if(!(ancestor instanceof DOMBased)){
			throw new IllegalArgumentException("Targets of hasAncestor(...) must be DOM-based");
		}

		Node ancestorNode = ((DOMBased)ancestor).getDOMNode();
		Node childNode = ((DOMBased)child).getDOMNode();

		Node n = childNode;
		while(true){
			if(n == null){
				return false;
			}
			if(n.equals(ancestorNode)){
				return true;
			}
			if(childNode.getNodeType() == Node.ATTRIBUTE_NODE){
				n = ((Attr)childNode).getOwnerElement();
			}
			else{
				n = n.getParentNode();
			}
		}
	}

	public ObjRef getParent(ObjRef to){
		// System.out.println("Getting parent of: " + to);
		Object child = get(to);
		// System.out.println("to is a " + child.getClass());
		if(!(child instanceof IXArchElement)){
			throw new IllegalArgumentException("Target of getParent(...) must be an IXArchElement");
		}
		IXArchElement parent = idTable.getParent((IXArchElement)child);
		if(parent == null){
			return null;
		}
		return put(parent);
	}

	public ObjRef[] getAllAncestors(ObjRef to){
		Vector v = new Vector();
		v.addElement(to);
		while(true){
			ObjRef parent = getParent(to);
			if(parent != null){
				v.addElement(parent);
				to = parent;
			}
			else{
				ObjRef[] arr = new ObjRef[v.size()];
				v.copyInto(arr);
				return arr;
			}
		}
	}

	public ObjRef getByID(String id){
		IXArchElement elt = idTable.getEntity(id);
		// System.out.println(idTable.idTableView());
		if(elt == null){
			return null;
		}
		else{

			return put(elt);
		}
	}

	public ObjRef getByID(ObjRef xArchRef, String id){
		Object co = get(xArchRef);
		if(!(co instanceof IXArch)){
			throw new IllegalArgumentException("Context of getByID be of type IXArch");
		}
		IXArch xArch = (IXArch)co;

		IXArchElement elt = idTable.getEntity(xArch, id);
		if(elt == null){
			return null;
		}
		else{
			return put(elt);
		}
	}

	// Simulates contextObject.add[typeOfThing](thingToAdd)
	public void add(ObjRef contextObject, String typeOfThing, ObjRef thingToAdd){
		synchronized(tableLock){
			Object co = get(contextObject);
			typeOfThing = capFirstLetter(typeOfThing);
			String methodName = "add" + typeOfThing;
			Object to = get(thingToAdd);

			Method m = getMethod(co.getClass(), methodName);

			try{
				putGutsChangeMap(to, thingToAdd);
				reverseObjects.remove(to);
				m.invoke(co, new Object[]{to});
			}
			catch(IllegalArgumentException iae){
				throw new InvalidOperationException(co.getClass(), methodName, new Class[]{to.getClass()});
			}
			catch(IllegalAccessException iae){
				throw new RuntimeException(iae.toString());
			}
			catch(InvocationTargetException ite){
				throw new RuntimeException(ite.getTargetException());
			}
			finally{
				reverseObjects.put(to, thingToAdd);
				gutsChangeCache.clear();
			}
			return;
		}
	}

	public void add(ObjRef contextObject, String typeOfThing, ObjRef[] thingsToAdd){
		synchronized(tableLock){
			Object co = get(contextObject);
			typeOfThing = capFirstLetter(typeOfThing);
			String methodName = "add" + typeOfThing + "s";

			Object[] objArr = new Object[thingsToAdd.length];
			for(int i = 0; i < thingsToAdd.length; i++){
				objArr[i] = get(thingsToAdd[i]);
				putGutsChangeMap(objArr[i], thingsToAdd[i]);
				reverseObjects.remove(objArr[i]);
			}

			Method m = getMethod(co.getClass(), methodName);

			try{
				m.invoke(co, new Object[]{arrayToCollection(objArr)});
			}
			catch(IllegalArgumentException iae){
				throw new InvalidOperationException(co.getClass(), methodName, new Class[]{Collection.class});
			}
			catch(IllegalAccessException iae){
				throw new RuntimeException(iae.toString());
			}
			catch(InvocationTargetException ite){
				throw new RuntimeException(ite.getTargetException());
			}
			finally{
				for(int i = 0; i < thingsToAdd.length; i++){
					reverseObjects.put(objArr[i], thingsToAdd[i]);
				}
				gutsChangeCache.clear();
			}
			return;
		}
	}

	public void clear(ObjRef contextObject, String typeOfThing){
		Object co = get(contextObject);
		typeOfThing = capFirstLetter(typeOfThing);
		String methodName1 = "clear" + typeOfThing;
		String methodName2 = "clear" + typeOfThing + "s";

		Method m = null;
		try{
			m = getMethod(co.getClass(), methodName1);
		}
		catch(InvalidOperationException ioe){
			// Hmm. clearFoobar() isn't a method. What about clearFoobars?
			m = getMethod(co.getClass(), methodName2);
		}

		try{
			m.invoke(co, new Object[]{});
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(co.getClass(), methodName1);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
		return;
	}

	// Returns a string for simple values and an ObjRef for complex ones, or
	// null
	// if there wasn't anything there at all.
	public Object get(ObjRef contextObject, String typeOfThing){
		Object co = get(contextObject);
		typeOfThing = capFirstLetter(typeOfThing);
		String methodName = "get" + typeOfThing;

		Method m = getMethod(co.getClass(), methodName);

		try{
			Object o = m.invoke(co, new Object[]{});
			if(o == null){
				return null;
			}
			else if(o instanceof String){
				return o;
			}
			else{
				return put(o);
			}
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(co.getClass(), methodName);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public ObjRef get(ObjRef contextObject, String typeOfThing, String id){
		Object co = get(contextObject);
		typeOfThing = capFirstLetter(typeOfThing);
		String methodName = "get" + typeOfThing;

		Method m = getMethod(co.getClass(), methodName);

		try{
			Object o = m.invoke(co, new Object[]{id});
			if(o == null){
				return null;
			}
			return put(o);
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(co.getClass(), methodName);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public ObjRef[] get(ObjRef contextObject, String typeOfThing, String[] ids){
		Object co = get(contextObject);
		typeOfThing = capFirstLetter(typeOfThing);
		String methodName = "get" + typeOfThing + "s";

		Method m = getMethod(co.getClass(), methodName);

		try{
			Object o = m.invoke(co, new Object[]{ids});
			Object[] arr = (Object[])o;

			ObjRef[] refArr = new ObjRef[arr.length];
			for(int i = 0; i < arr.length; i++){
				refArr[i] = put(arr[i]);
			}
			return refArr;
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(methodName);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public ObjRef[] getAll(ObjRef contextObject, String typeOfThing){
		Object co = get(contextObject);
		typeOfThing = capFirstLetter(typeOfThing);
		String methodName = "getAll" + typeOfThing + "s";

		Method m = getMethod(co.getClass(), methodName);

		try{
			Object o = m.invoke(co, new Object[]{});
			Collection c = (Collection)o;

			ObjRef[] refArr = new ObjRef[c.size()];
			int i = 0;
			for(Iterator it = c.iterator(); it.hasNext();){
				refArr[i++] = put(it.next());
			}
			return refArr;
			/*
			 * Object[] arr = c.toArray(); ObjRef[] refArr = new
			 * ObjRef[arr.length]; for(int i = 0; i < arr.length; i++){
			 * refArr[i] = put(arr[i]); } return refArr;
			 */
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(co.getClass(), methodName);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public boolean has(ObjRef contextObject, String typeOfThing, String valueToCheck){
		Object co = get(contextObject);
		typeOfThing = capFirstLetter(typeOfThing);
		String methodName = "has" + typeOfThing;

		Method m = getMethod(co.getClass(), methodName);

		try{
			Boolean b = (Boolean)m.invoke(co, new Object[]{valueToCheck});
			return b.booleanValue();
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(co.getClass(), methodName, new Class[]{String.class});
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public boolean has(ObjRef contextObject, String typeOfThing, ObjRef thingToCheck){
		Object co = get(contextObject);
		typeOfThing = capFirstLetter(typeOfThing);
		String methodName = "has" + typeOfThing;

		Object to = get(thingToCheck);

		Method m = getMethod(co.getClass(), methodName);

		try{
			Boolean b = (Boolean)m.invoke(co, new Object[]{to});
			return b.booleanValue();
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(co.getClass(), methodName, new Class[]{to.getClass()});
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public boolean hasAll(ObjRef contextObject, String typeOfThing, ObjRef[] thingsToCheck){
		Object co = get(contextObject);
		typeOfThing = capFirstLetter(typeOfThing);
		String methodName = "hasAll" + typeOfThing + "s";

		Vector v = new Vector(thingsToCheck.length);
		for(ObjRef element: thingsToCheck){
			v.addElement(get(element));
		}

		Method m = getMethod(co.getClass(), methodName);

		try{
			Boolean b = (Boolean)m.invoke(co, new Object[]{v});
			return b.booleanValue();
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(co.getClass(), methodName);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public boolean[] has(ObjRef contextObject, String typeOfThing, ObjRef[] thingsToCheck){
		Object co = get(contextObject);
		typeOfThing = capFirstLetter(typeOfThing);
		String methodName = "has" + typeOfThing + "s";

		Vector v = new Vector(thingsToCheck.length);
		for(ObjRef element: thingsToCheck){
			v.addElement(get(element));
		}

		Method m = getMethod(co.getClass(), methodName);

		try{
			Collection c = (Collection)m.invoke(co, new Object[]{v});
			Vector responseVector = new Vector();
			for(Iterator it = c.iterator(); it.hasNext();){
				Boolean b = (Boolean)it.next();
				responseVector.addElement(b);
			}
			boolean[] responseArray = new boolean[responseVector.size()];
			for(int i = 0; i < responseArray.length; i++){
				responseArray[i] = ((Boolean)responseVector.elementAt(i)).booleanValue();
			}
			return responseArray;
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(co.getClass(), methodName, new Class[]{Collection.class});
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public void remove(ObjRef contextObject, String typeOfThing, ObjRef thingToRemove){
		Object co = get(contextObject);
		typeOfThing = capFirstLetter(typeOfThing);
		String methodName = "remove" + typeOfThing;
		Object to = get(thingToRemove);

		Method m = getMethod(co.getClass(), methodName);
		// System.out.println("m = " + m);
		// System.out.println("to.class = " + to.getClass());
		try{
			m.invoke(co, new Object[]{to});
		}
		catch(IllegalArgumentException iae){
			// iae.printStackTrace();
			throw new InvalidOperationException(co.getClass(), methodName, new Class[]{to.getClass()});
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
		return;
	}

	public void remove(ObjRef contextObject, String typeOfThing, ObjRef[] thingsToRemove){
		Object co = get(contextObject);
		typeOfThing = capFirstLetter(typeOfThing);
		String methodName = "remove" + typeOfThing + "s";

		Object[] objArr = new Object[thingsToRemove.length];
		for(int i = 0; i < thingsToRemove.length; i++){
			objArr[i] = get(thingsToRemove[i]);
		}

		Method m = getMethod(co.getClass(), methodName);

		try{
			m.invoke(co, new Object[]{arrayToCollection(objArr)});
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(co.getClass(), methodName, new Class[]{Collection.class});
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
		return;
	}

	public void set(ObjRef contextObject, String typeOfThing, String value){
		Object co = get(contextObject);
		typeOfThing = capFirstLetter(typeOfThing);
		String methodName = "set" + typeOfThing;

		Method m = getMethod(co.getClass(), methodName);

		try{
			m.invoke(co, new Object[]{value});
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(co.getClass(), methodName, new Class[]{String.class});
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public void set(ObjRef contextObject, String typeOfThing, ObjRef value){
		synchronized(tableLock){
			Object co = get(contextObject);
			typeOfThing = capFirstLetter(typeOfThing);
			String methodName = "set" + typeOfThing;

			Object to = get(value);

			Method m = getMethod(co.getClass(), methodName);

			try{
				putGutsChangeMap(to, value);
				reverseObjects.remove(to);
				m.invoke(co, new Object[]{to});
			}
			catch(IllegalArgumentException iae){
				throw new InvalidOperationException(co.getClass(), methodName, new Class[]{to.getClass()});
			}
			catch(IllegalAccessException iae){
				throw new RuntimeException(iae.toString());
			}
			catch(InvocationTargetException ite){
				throw new RuntimeException(ite.getTargetException());
			}
			finally{
				reverseObjects.put(to, value);
				gutsChangeCache.clear();
			}
		}
	}

	private static Method findIsEqualMethod(Class coClass, Class c){
		if(c == null){
			return null;
		}
		try{
			Method m = coClass.getMethod("isEqual", new Class[]{c});
			if(m != null){
				return m;
			}
		}
		catch(NoSuchMethodException nsme1){
		}
		Class[] interfaceClasses = c.getInterfaces();
		if(interfaceClasses != null){
			for(Class element: interfaceClasses){
				try{
					Method m = coClass.getMethod("isEqual", new Class[]{element});
					if(m != null){
						return m;
					}
				}
				catch(NoSuchMethodException nsme2){
				}
			}
		}
		return findIsEqualMethod(coClass, c.getSuperclass());
	}

	public boolean isEqual(ObjRef contextObject, ObjRef thingToCheck){
		Object co = get(contextObject);
		String methodName = "isEqual";

		Object to = get(thingToCheck);

		// Method m = getMethod(co.getClass(), methodName);
		/*
		 * Method m = null; try{ m = co.getClass().getMethod(methodName, new
		 * Class[]{to.getClass()}); } catch(NoSuchMethodException nsme){ throw
		 * new InvalidOperationException(methodName + "(" + to.getClass() +
		 * ")"); }
		 */
		Method m = findIsEqualMethod(co.getClass(), to.getClass());
		if(m == null){
			throw new InvalidOperationException(methodName + "(" + to.getClass() + ")");
		}

		try{
			Boolean b = (Boolean)m.invoke(co, new Object[]{to});
			return b.booleanValue();
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(co.getClass(), methodName, new Class[]{to.getClass()});
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public boolean isEquivalent(ObjRef contextObject, ObjRef thingToCheck){
		Object co = get(contextObject);
		Class coClass = co.getClass();
		String methodName = "isEquivalent";

		Object to = get(thingToCheck);
		Class toClass = to.getClass();

		// This is specially included here to fix a bug that could report
		// no-such-method when the classes are not the same due to the
		// cache-by-name behavior of the method cache, even though
		// an underlying method does actually exist...which would return
		// false in this case.

		if(!coClass.equals(toClass)){
			return false;
		}

		Method m = getMethod(coClass, methodName);

		try{
			Boolean b = (Boolean)m.invoke(co, new Object[]{to});
			return b.booleanValue();
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(methodName);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public ObjRef createContext(ObjRef xArchObject, String contextType){
		Object co = get(xArchObject);
		if(!(co instanceof edu.uci.isr.xarch.IXArch)){
			throw new IllegalArgumentException("xArchObject must be of type IXArch");
		}

		ObjRef existingContext = contextCache.get(xArchObject, contextType);
		if(existingContext != null){
			return existingContext;
		}

		Object context = idTable.getXArchImplementation().createContext((edu.uci.isr.xarch.IXArch)co, contextType);
		if(context == null){
			throw new RuntimeException("Error creating context.");
		}
		ObjRef contextRef = put(context);
		contextCache.put(xArchObject, contextType, contextRef);
		return contextRef;
	}

	public ObjRef create(ObjRef contextObject, String typeOfThing){
		Object co = get(contextObject);
		String methodName = "create" + capFirstLetter(typeOfThing);

		// System.out.println("in create; co=" + co);
		// System.out.println("in create; co.class=" + co.getClass());
		Method m = getMethod(co.getClass(), methodName);

		try{
			Object o = m.invoke(co, new Object[]{});
			return put(o);
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(methodName);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public ObjRef createElement(ObjRef contextObject, String typeOfThing){
		Object co = get(contextObject);
		String methodName = "create" + capFirstLetter(typeOfThing) + "Element";

		Method m = getMethod(co.getClass(), methodName);

		try{
			Object o = m.invoke(co, new Object[]{});
			return put(o);
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(methodName);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public ObjRef promoteTo(ObjRef contextObject, String promoteTo, ObjRef targetObject){
		Object co = get(contextObject);
		String methodName = "promoteTo" + capFirstLetter(promoteTo);

		Object to = get(targetObject);

		Method m = getMethod(co.getClass(), methodName);

		try{
			// System.out.println("Promotion beginning. Class = " +
			// to.getClass());
			// System.out.println("Ref = " + targetObject);

			Object o = m.invoke(co, new Object[]{to});
			// This will always be there, because we just promoted
			// an existing element. Two elements are equal() when
			// their underlying DOM elements are equal, and the
			// underlying DOM element hasn't changed here--just
			// the wrapper.

			// this is now done in the handleXArchEvent method
			// reverseObjects.remove(to);
			// objects.remove(targetObject);

			// but let's still check it
			ObjRef newRef = put(o, targetObject);
			if(!newRef.equals(targetObject)){
				throw new RuntimeException("Bad Mojo!");
			}

			// System.out.println("New class = " + o.getClass());
			// System.out.println("New ref = " + newRef);
			// System.out.println("New class, according to flat = " +
			// getType(newRef));
			return newRef;
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(methodName);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public ObjRef recontextualize(ObjRef contextObject, String typeOfThing, ObjRef targetObject){
		Object co = get(contextObject);
		String methodName = "recontextualize" + capFirstLetter(typeOfThing);

		Object to = get(targetObject);

		Method m = getMethod(co.getClass(), methodName);

		try{
			Object o = m.invoke(co, new Object[]{to});
			objects.remove(targetObject);
			reverseObjects.remove(to);

			return put(o, targetObject);
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(methodName);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public ObjRef getXArch(ObjRef contextObject){
		Object co = get(contextObject);
		String methodName = "getXArch";

		Method m = getMethod(co.getClass(), methodName);

		try{
			Object o = m.invoke(co, new Object[]{});
			return put(o);
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(co.getClass(), methodName);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public ObjRef[] getAllElements(ObjRef contextObject, String typeOfThing, ObjRef xArchObject){
		Object co = get(contextObject);
		String methodName = "getAll" + capFirstLetter(typeOfThing) + "s";
		Object to = get(xArchObject);

		Method m = getMethod(co.getClass(), methodName);

		try{
			Object o = m.invoke(co, new Object[]{to});
			Collection c = (Collection)o;

			Object[] arr = c.toArray();

			ObjRef[] refArr = new ObjRef[arr.length];
			for(int i = 0; i < arr.length; i++){
				refArr[i] = put(arr[i]);
			}
			return refArr;
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(co.getClass(), methodName);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public ObjRef getElement(ObjRef contextObject, String typeOfThing, ObjRef xArchObject){
		Object co = get(contextObject);
		String methodName = "get" + capFirstLetter(typeOfThing);
		Object to = get(xArchObject);

		Method m = getMethod(co.getClass(), methodName);

		try{
			Object o = m.invoke(co, new Object[]{to});
			if(o == null){
				return null;
			}
			return put(o);
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(co.getClass(), methodName);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public IXArchTypeMetadata getTypeMetadata(ObjRef contextObject){
		Object co = get(contextObject);
		String methodName = "getTypeMetadata";
		Method m = getMethod(co.getClass(), methodName);

		try{
			Object o = m.invoke(co, new Object[]{});
			if(o instanceof XArchTypeMetadata){
				return wrap((XArchTypeMetadata)o);
			}
			return null;
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(co.getClass(), methodName);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public IXArchTypeMetadata getTypeMetadata(String type){
		String fieldName = "TYPE_METADATA";
		Class clazz = null;
		try{
			clazz = Class.forName(getInstanceName(type));
			Field f = clazz.getDeclaredField(fieldName);

			Object o = f.get(null);
			if(o instanceof XArchTypeMetadata){
				return wrap((XArchTypeMetadata)o);
			}
			return null;
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(clazz, fieldName);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(NoSuchFieldException nsfe){
			throw new RuntimeException(nsfe);
		}
		catch(ClassNotFoundException e){
			throw new IllegalArgumentException("Invalid type: " + type);
		}
	}

	private IXArchTypeMetadata wrap(final XArchTypeMetadata m){
		if(m == null){
			return null;
		}

		return new IXArchTypeMetadata(){

			@Override
			public String toString(){
				return getType();
			}

			@Override
			public int hashCode(){
				return getType().hashCode();
			}

			public IXArchActionMetadata[] getActions(){
				List<IXArchActionMetadata> actions = new ArrayList<IXArchActionMetadata>();
				for(Iterator it = m.getActions(); it.hasNext();){
					actions.add(wrap((XArchActionMetadata)it.next()));
				}
				return actions.toArray(new IXArchActionMetadata[actions.size()]);
			}

			public String getParentType(){
				return XArchMetadataUtils.getType(m.getParentTypePackage(), m.getParentTypeName());
			}

			public IXArchPropertyMetadata[] getProperties(){
				List<IXArchPropertyMetadata> properties = new ArrayList<IXArchPropertyMetadata>();
				for(Iterator it = m.getProperties(); it.hasNext();){
					properties.add(wrap((XArchPropertyMetadata)it.next()));
				}
				return properties.toArray(new IXArchPropertyMetadata[properties.size()]);
			}

			public IXArchPropertyMetadata getProperty(String name){
				return wrap(m.getProperty(name));
			}

			public int getMetadataType(){
				return m.getType();
			}

			public String getType(){
				return XArchMetadataUtils.getType(m.getTypePackage(), m.getTypeName());
			}
		};
	}

	private IXArchPropertyMetadata wrap(final XArchPropertyMetadata m){
		if(m == null){
			return null;
		}

		return new IXArchPropertyMetadata(){

			@Override
			public String toString(){
				return getName() + ":" + getType();
			}

			@Override
			public int hashCode(){
				return (getName() + ":" + getType()).hashCode();
			}

			public String[] getEnumeratedValues(){
				if(m.isEnumerated()){
					return (String[])m.getEnumeratedValues().toArray(new String[0]);
				}
				return null;
			}

			public String getFixedValue(){
				return m.getFixedValue();
			}

			public int getMaxOccurs(){
				return m.getMaxOccurs();
			}

			public int getMinOccurs(){
				return m.getMinOccurs();
			}

			public String getName(){
				return m.getName();
			}

			public int getMetadataType(){
				return m.getType();
			}

			public String getType(){
				return XArchMetadataUtils.getType(m.getTypePackage(), m.getTypeName());
			}

			public boolean isFixed(){
				return m.isFixed();
			}

			public boolean isOptional(){
				return m.isOptional();
			}
		};
	}

	protected IXArchActionMetadata wrap(final XArchActionMetadata m){
		if(m == null){
			return null;
		}

		return new IXArchActionMetadata(){

			@Override
			public String toString(){
				switch(getMetadataType()){
				case IXArchActionMetadata.CREATE:
					return "create " + getOutputType();
				case IXArchActionMetadata.CREATE_ELEMENT:
					return "create " + getOutputType() + " element";
				case IXArchActionMetadata.PROMOTE:
					return "promote " + getInputType() + " to " + getOutputType();
				case IXArchActionMetadata.RECONTEXTUALIZE:
					return "recontextualize " + getInputType();
				default:
					return "unknown";
				}
			}

			@Override
			public int hashCode(){
				return super.hashCode();
			}

			public String getInputType(){
				return XArchMetadataUtils.getType(m.getInputTypeMetadata().getTypePackage(), m.getInputTypeMetadata().getTypeName());
			}

			public String getOutputType(){
				return XArchMetadataUtils.getType(m.getOutputTypeMetadata().getTypePackage(), m.getOutputTypeMetadata().getTypeName());
			}

			public int getMetadataType(){
				return m.getType();
			}
		};
	}

	private IXArchInstanceMetadata wrap(final XArchInstanceMetadata m){
		if(m == null){
			return null;
		}

		return new IXArchInstanceMetadata(){

			@Override
			public String toString(){
				return getCurrentContext();
			}

			@Override
			public int hashCode(){
				return getCurrentContext().hashCode();
			}

			public String getCurrentContext(){
				return m.getCurrentContext();
			}
		};
	}

	public IXArchInstanceMetadata getInstanceMetadata(ObjRef contextObject){
		Object co = get(contextObject);
		String methodName = "getInstanceMetadata";
		Method m = getMethod(co.getClass(), methodName);

		try{
			Object o = m.invoke(co, new Object[]{});
			if(o instanceof XArchInstanceMetadata){
				return wrap((XArchInstanceMetadata)o);
			}
			return null;
		}
		catch(IllegalArgumentException iae){
			throw new InvalidOperationException(co.getClass(), methodName);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	public String getType(ObjRef contextObject){
		Object co = get(contextObject);
		return co.getClass().getName();
	}

	public boolean isInstanceOf(ObjRef contextObject, String type){
		Object co = get(contextObject);
		try{
			if(type.startsWith("edu.uci.isr.xarch.")){
				String className = type;
				int last = type.lastIndexOf('.');
				if(last > "edu.uci.isr.xarch.".length()){
					String prefix = type.substring("edu.uci.isr.xarch.".length(), last);
					String suffix = type.substring(last + 2); // - the "I" as well
					type = XArchMetadataUtils.getType(prefix, suffix);
				}
				else{
					String prefix = "";
					String suffix = type.substring(last + 2); // - the "I" as well
					type = XArchMetadataUtils.getType(prefix, suffix);
				}
				System.out.println("Warning: class name <" + className + "> used instead of type <" + type + ">");
			}
			Class c = Class.forName(getInstanceName(type));
			return c.isAssignableFrom(co.getClass());
		}
		catch(ClassNotFoundException e){
			throw new IllegalArgumentException("Invalid type name: " + type);
		}
	}

	public ObjRef resolveHref(ObjRef xArchRef, String href){
		Object co = get(xArchRef);
		if(!(co instanceof edu.uci.isr.xarch.IXArch)){
			throw new IllegalArgumentException("Context of resolveHref must be of type IXArch");
		}
		try{
			IXArchElement elt = resolver.resolveHref((IXArch)co, href);
			if(elt == null){
				return null;
			}
			else{
				return put(elt);
			}
		}
		catch(IllegalArgumentException iae){
			throw iae;
		}
		catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}
	}

	public void addXArchFlatListener(XArchFlatListener l){
		xArchFlatListeners.addElement(l);
	}

	public void removeXArchFlatListener(XArchFlatListener l){
		xArchFlatListeners.removeElement(l);
	}

	public void addXArchFileListener(XArchFileListener l){
		xArchFileListeners.addElement(l);
	}

	public void removeXArchFileListener(XArchFileListener l){
		xArchFileListeners.removeElement(l);
	}

	public void handleXArchEvent(XArchEvent evt){

		// Transform the XArchEvent into an XArchFlatEvent
		IXArchElement src = evt.getSource();
		ObjRef srcRef = put(src);

		// ! THIS MUST BE DONE AFTER THE SWITCH STATEMENT BELOW !
		// For promotion events, the new, promoted object replaces
		// the one in the table in the switch statement. We need
		// to do this for unattached events as well.
		
		//if(!emitUnattachedEvents && !isAttached(srcRef)){
		//	return;
		//}

		// System.out.println(srcPath);

		XArchPath allPath = null;
		XArchPath srcPath = null;
		XArchPath targetPath = null;
		Object target = evt.getTarget();

		// update the tables for promote
		int eventType = evt.getEventType();
		switch(eventType){
		case XArchEvent.PROMOTE_EVENT: {
			ObjRef targetRef = put(target, reverseObjects.remove(src));
			target = targetRef;
			allPath = getXArchPath(targetRef);
			srcPath = allPath;
			targetPath = null;
		}
			break;
		case XArchEvent.ADD_EVENT:
		case XArchEvent.SET_EVENT: {
			if(target == null || target instanceof String){
				allPath = getXArchPath(srcRef);
				srcPath = allPath;
				targetPath = null;
			}
			else{
				ObjRef targetRef = put(target);
				target = targetRef;
				allPath = getXArchPath(targetRef);
				srcPath = allPath.subpath(0, allPath.getLength() - 1);
				targetPath = allPath;
			}
		}
			break;
		case XArchEvent.REMOVE_EVENT:
		case XArchEvent.CLEAR_EVENT: {
			if(target == null || target instanceof String){
				allPath = getXArchPath(srcRef);
				srcPath = allPath;
				targetPath = null;
			}
			else{
				ObjRef targetRef = put(target);
				target = targetRef;
				allPath = getXArchPath(srcRef, evt.getTarget());
				srcPath = allPath.subpath(0, allPath.getLength() - 1);
				targetPath = allPath.subpath(allPath.getLength() - 1);
			}
		}
			break;
		}

		// ! THIS MUST BE DONE AFTER THE SWITCH STATEMENT ABOVE !
		// For promotion events, the new, promoted object replaces
		// the one in the table in the switch statement. We need
		// to do this for unattached events as well.
		
		if(!emitUnattachedEvents && !isAttached(srcRef)){
			return;
		}

		ObjRef[] srcAncestors = getAllAncestors(srcRef);

		ObjRef xArchRef = getXArch(srcRef);
		XArchFlatEvent flatEvt = new XArchFlatEvent(xArchRef, srcRef, srcAncestors, srcPath, eventType, evt.getSourceType(), evt.getTargetName(), target, targetPath, evt.getIsAttached(), evt.isExtra(), allPath);

		for(Enumeration en = xArchFlatListeners.elements(); en.hasMoreElements();){
			XArchFlatListener l = (XArchFlatListener)en.nextElement();
			l.handleXArchFlatEvent(flatEvt);
		}
	}

	public void handleXArchFileEvent(XArchFileEvent evt){
		// System.out.println("Got file event.");
		int evtType = evt.getEventType();
		if(evtType == XArchFileEvent.XARCH_CREATED_EVENT || evtType == XArchFileEvent.XARCH_OPENED_EVENT){
			// System.out.println("Importing!");
			importXArch(evt.getURL());
			evt = new XArchFileEvent(evt, getOpenXArch(evt.getURL()));
			fireXArchFileEvent(evt);
		}
		else if(evtType == XArchFileEvent.XARCH_CLOSED_EVENT){
			// System.out.println("Forgetting!");
			// forgetXArch(evt.getXArchRef());
			// The close event will be fired by close() from here...
		}
		else{
			evt = new XArchFileEvent(evt, getOpenXArch(evt.getURL()));
			fireXArchFileEvent(evt);
		}
	}

	protected void fireXArchFileEvent(XArchFileEvent evt){
		for(Enumeration en = xArchFileListeners.elements(); en.hasMoreElements();){
			XArchFileListener l = (XArchFileListener)en.nextElement();
			l.handleXArchFileEvent(evt);
		}
	}

	//	public void forgetAllWithPrefix(ObjRef xArchRef, String prefix){
	//		synchronized(tableLock){
	//			IXArch xArch = (IXArch)get(xArchRef);
	//			ArrayList markedForDeath = new ArrayList();
	//			for(Object element: objects.keySet()){
	//				ObjRef ref = (ObjRef)element;
	//				if(ref.getUID().startsWith(prefix)){
	//					Object o = objects.get(ref);
	//					if(o instanceof IXArchElement){
	//						IXArchElement elt = (IXArchElement)o;
	//						if(elt.getXArch().equals(xArch)){
	//							markedForDeath.add(ref);
	//						}
	//					}
	//				}
	//			}
	//			for(Iterator it = markedForDeath.iterator(); it.hasNext();){
	//				ObjRef ref = (ObjRef)it.next();
	//				Object o = objects.get(ref);
	//				objects.remove(ref);
	//				reverseObjects.remove(o);
	//			}
	//		}
	//	}

	public ObjRef cloneElement(ObjRef targetObjectRef, int depth){
		Object to = get(targetObjectRef);
		if(!(to instanceof IXArchElement)){
			throw new IllegalArgumentException("Target of cloneElement() must be an IXArchElement");
		}
		IXArchElement elt = (IXArchElement)to;
		IXArchElement clonedElt = idTable.cloneElement(elt, depth);
		ObjRef clonedRef = put(clonedElt);
		// System.out.println("Ref of clone is " + clonedRef);
		return clonedRef;
	}

	public ObjRef[] getReferences(ObjRef xArchRef, String id){
		Object co = get(xArchRef);
		if(!(co instanceof IXArch)){
			throw new IllegalArgumentException("Context of getByID be of type IXArch");
		}
		IXArch xArch = (IXArch)co;

		Collection elts = idTable.getReferences(xArch, id);
		ObjRef[] arr = new ObjRef[elts.size()];
		int i = 0;
		for(Iterator it = elts.iterator(); it.hasNext();){
			IXArchElement elt = (IXArchElement)it.next();
			arr[i] = put(elt);
			i++;
		}
		return arr;
	}

	public void dump(ObjRef ref){
		Object co = get(ref);
		System.out.println(co);
	}

	private static String stripPrefix(String s){
		return s.substring(s.indexOf(":") + 1);
	}

	public ObjRef resolveXArchPath(ObjRef xArchRef, XArchPath xArchPath){
		return XArchPath.resolve(this, xArchRef, xArchPath);
	}

	protected XArchPath getXArchPath(ObjRef ref, Object additionalElement){

		List<String> tagNameList = new ArrayList<String>();
		List<Integer> tagIndexList = new ArrayList<Integer>();
		List<String> tagIDList = new ArrayList<String>();

		if(additionalElement != null){
			if(!(additionalElement instanceof DOMBased)){
				throw new IllegalArgumentException("Targets of getXArchPath must be DOM-based");
			}
			Node n = ((DOMBased)additionalElement).getDOMNode();
			if(n != null && n.getNodeType() == Node.ELEMENT_NODE){
				addTag(tagNameList, tagIndexList, tagIDList, (Element)n);
			}
		}

		Object co = get(ref);
		// if(!(co instanceof IXArch)){
		// throw new IllegalArgumentException("Target of getXArchPath must be of
		// type IXArch");
		// }

		if(!(co instanceof DOMBased)){
			throw new IllegalArgumentException("Targets of getXArchPath must be DOM-based");
		}

		Node coNode = ((DOMBased)co).getDOMNode();

		Node n = coNode;
		while(true){
			if(n == null){
				// done
				break;
			}
			if(n.getNodeType() == Node.ATTRIBUTE_NODE){
				n = ((Attr)n).getOwnerElement();
			}
			else if(n.getNodeType() == Node.ELEMENT_NODE){
				addTag(tagNameList, tagIndexList, tagIDList, (Element)n);
			}
			else{
				// done
				break;
			}
			n = n.getParentNode();
		}

		int size = tagNameList.size();
		String[] tagNameArr = new String[size];
		int[] tagIndexArr = new int[size];
		String[] tagIDArr = new String[size];
		for(int i = 0; i < size; i++){
			int ri = size - i - 1;
			tagNameArr[i] = tagNameList.get(ri);
			tagIndexArr[i] = tagIndexList.get(ri).intValue();
			tagIDArr[i] = tagIDList.get(ri);
		}
		XArchPath xp = new XArchPath(tagNameArr, tagIndexArr, tagIDArr);
		return xp;
	}

	public XArchPath getXArchPath(ObjRef ref){
		return getXArchPath(ref, null);
	}

	private final static void addTag(List<String> tagNameList, List<Integer> tagIndexList, List<String> tagIDList, Element elt){
		String tagName = elt.getTagName();
		String tagID = IdTable.getId(elt);
		int tagIndex = -1;
		if(tagID == null){
			tagIndex = 0;
			Node sibNode = elt;
			while(true){
				sibNode = sibNode.getPreviousSibling();
				if(sibNode == null){
					break;
				}
				if(sibNode.getNodeType() == Node.TEXT_NODE){
					continue;
				}
				if(sibNode.getNodeType() != Node.ELEMENT_NODE){
					break;
				}
				Element sibElt = (Element)sibNode;
				String sibTagName = sibElt.getTagName();
				if(!sibTagName.equals(tagName)){
					break;
				}
				tagIndex++;
			}
		}
		tagNameList.add(stripPrefix(tagName));
		tagIndexList.add(Integer.valueOf(tagIndex));
		tagIDList.add(tagID);
	}

	private String bulkQueryGetID(ObjRef ref){
		try{
			String id = (String)get(ref, "id");
			return id;
		}
		catch(Exception e){
			return null;
		}
	}

	public XArchBulkQueryResults bulkQuery(XArchBulkQuery q){
		ObjRef rootRef = q.getQueryRootRef();
		Object root = get(rootRef);
		String rootRefElementName = getElementName(rootRef);
		Class rootClass = root.getClass();
		IXArchTypeMetadata resultTypeMetadata = getTypeMetadata(rootRef);
		IXArchInstanceMetadata resultInstanceMetadata = getInstanceMetadata(rootRef);
		String rootID = bulkQueryGetID(rootRef);
		ObjRef xArchRef = getXArch(rootRef);
		XArchPath xArchPath = getXArchPath(rootRef);

		XArchBulkQueryResults qr = new XArchBulkQueryResults(rootRefElementName, rootRef, resultTypeMetadata, resultInstanceMetadata, rootClass, rootID, xArchPath, xArchRef);

		XArchBulkQueryNode[] children = q.getChildren();
		for(XArchBulkQueryNode element: children){
			processBulkQueryNode(qr, element);
		}
		return qr;
	}

	private XArchBulkQueryResultNode buildBulkQueryResultNode(ObjRef resultRef){
		String resultTagName = getElementName(resultRef);
		Object resultObject = get(resultRef);
		// System.out.println("resultObject = " + resultObject);
		IXArchTypeMetadata resultTypeMetadata = getTypeMetadata(resultRef);
		IXArchInstanceMetadata resultInstanceMetadata = getInstanceMetadata(resultRef);
		Class resultClass = resultObject.getClass();
		String resultID = bulkQueryGetID(resultRef);
		XArchPath xArchPath = getXArchPath(resultRef);
		return new XArchBulkQueryResultNode(resultTagName, resultRef, resultTypeMetadata, resultInstanceMetadata, resultClass, resultID, xArchPath);
	}

	private boolean bqIsXArchRef(ObjRef baseRef){
		Object co = get(baseRef);
		if(co == null){
			return false;
		}
		return co instanceof edu.uci.isr.xarch.IXArch;
	}

	private void processBulkQueryNode(XArchBulkQueryResultNode parentResultNode, XArchBulkQueryNode qn){
		ObjRef baseRef = parentResultNode.getObjRef();
		String tagName = qn.getTagName();
		boolean isPlural = qn.isPlural();

		if(isPlural){
			ObjRef[] resultRefs;

			if(bqIsXArchRef(baseRef)){
				ArrayList resultRefList = new ArrayList();

				try{
					ObjRef[] childRefs = getAll(baseRef, "Object");
					for(ObjRef element: childRefs){
						String childTagName = getElementName(element);
						if(childTagName != null){
							childTagName = capFirstLetter(childTagName);
						}
						if(childTagName != null && childTagName.equals(tagName)){
							resultRefList.add(element);
						}
					}
				}
				catch(InvalidOperationException ioe){
					// Do nothing; there's no children of this kind.
				}
				resultRefs = (ObjRef[])resultRefList.toArray(new ObjRef[0]);
			}
			else{
				try{
					resultRefs = getAll(baseRef, tagName);
				}
				catch(InvalidOperationException ioe){
					resultRefs = new ObjRef[0];
				}
			}
			for(ObjRef element: resultRefs){
				XArchBulkQueryResultNode resultNode = buildBulkQueryResultNode(element);
				parentResultNode.addChild(resultNode);
				XArchBulkQueryNode[] childQueryNodes = qn.getChildren();
				for(XArchBulkQueryNode element2: childQueryNodes){
					processBulkQueryNode(resultNode, element2);
				}
			}
		}
		else{
			Object resultObj = null;
			if(bqIsXArchRef(baseRef)){
				try{
					ObjRef[] childRefs = getAll(baseRef, "Object");
					for(ObjRef element: childRefs){
						String childTagName = getElementName(element);
						if(childTagName != null){
							childTagName = capFirstLetter(childTagName);
						}
						if(childTagName != null && childTagName.equals(tagName)){
							resultObj = element;
							break;
						}
					}
				}
				catch(InvalidOperationException ioe){
					// do nothing
				}
			}
			else{
				try{
					resultObj = get(baseRef, tagName);
				}
				catch(InvalidOperationException ioe){
					// do nothing
				}
			}

			if(resultObj == null){
				// Nothing down this subtree.
				return;
			}
			else if(resultObj instanceof ObjRef){
				ObjRef resultRef = (ObjRef)resultObj;
				XArchBulkQueryResultNode resultNode = buildBulkQueryResultNode(resultRef);
				parentResultNode.addChild(resultNode);
				XArchBulkQueryNode[] childQueryNodes = qn.getChildren();
				for(XArchBulkQueryNode element: childQueryNodes){
					processBulkQueryNode(resultNode, element);
				}
			}
			else if(resultObj instanceof String){
				String resultString = (String)resultObj;
				XArchBulkQueryResultNode resultNode = new XArchBulkQueryResultNode(tagName, resultString);
				parentResultNode.addChild(resultNode);
			}
			else{
				// this shouldn't happen
				throw new RuntimeException("get() call returned object that was not an ObjRef or a String: " + resultObj);
			}
		}
	}

	public String[] getContextTypes(){
		String[] packageNames = XArchUtils.getPackageNames().clone();
		for(int i = 0; i < packageNames.length; i++){
			packageNames[i] = packageNames[i].substring(packageNames[i].lastIndexOf(".") + 1);
		}
		return packageNames;
	}

	public boolean isAssignable(String toType, String fromType){
		Class toClass;
		try{
			toClass = Class.forName(getInstanceName(toType));
		}
		catch(ClassNotFoundException e){
			throw new IllegalArgumentException("Invalid type:" + toType);
		}
		Class fromClass;
		try{
			fromClass = Class.forName(getInstanceName(fromType));
		}
		catch(ClassNotFoundException e){
			throw new IllegalArgumentException("Invalid type:" + fromType);
		}
		return toClass.isAssignableFrom(fromClass);
	}

	private String getInstanceName(String type){
		String context = XArchMetadataUtils.getTypeContext(type);
		String name = XArchMetadataUtils.getTypeName(type);
		String className;
		if("".equals(context)){
			if("".equals(name)){
				// "#", root context class
				return "edu.uci.isr.xarch.IXArchContext";
			}
			// "#XArchElement", no context, root xarch class
			return "edu.uci.isr.xarch.I" + capFirstLetter(name);
		}
		if("".equals(name)){
			// "types#", context class
			return "edu.uci.isr.xarch." + uncapFirstLetter(context) + ".I" + capFirstLetter(context) + "Context";
		}
		// "types#ComponentType", normal type
		return "edu.uci.isr.xarch." + uncapFirstLetter(context) + ".I" + capFirstLetter(name);
	}
}
