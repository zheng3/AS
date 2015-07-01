package edu.uci.isr.xarchflat.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.uci.isr.xarch.IXArch;
import edu.uci.isr.xarch.IXArchElement;
import edu.uci.isr.xarch.IXArchImplementation;
import edu.uci.isr.xarch.instance.IXMLLink;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

/**
 * Utility functions for the XArchFlatProxy mechanism.
 * 
 * @author Eric M. Dashofy <a
 *         href="mailto:edashofy@ics.uci.edu">edashofy@ics.uci.edu</a>
 * @author Scott A. Hendrickson <a
 *         href="mailto:shendric@uci.edu">shendric@uci.edu</a>
 */
public class XArchFlatProxyUtils{

	private static Map proxyClassCache = Collections.synchronizedMap(new HashMap());

	/**
	 * Gets a proxied <code>IXArchImplementation</code> object for the given
	 * <code>XArchFlatInterface</code>.
	 * 
	 * @param xArch
	 *            The <code>XArchFlatInterface</code> to which calls will be
	 *            proxied.
	 * @return <code>IXArchImplementation</code> that can create and manage
	 *         proxies on the given <code>XArchFlatInterface</code>
	 */
	public static IXArchImplementation getXArchImplementation(XArchFlatInterface xArch){
		return new XArchFlatProxyImplementation(xArch);
	}

	/**
	 * Returns a proxy object that wraps the given <code>ObjRef</code>.
	 * 
	 * @param xArch
	 *            interface through which to access the objRef
	 * @param objRef
	 *            <code>ObjRef</code> to proxy
	 * @return a proxy object that wraps the given <code>ObjRef</code>
	 */
	public static <T>T proxy(XArchFlatInterface xArch, ObjRef objRef){
		try{
			// null object refs are valid
			if(objRef == null){
				return null;
			}

			String type = xArch.getType(objRef);
			Class proxyClass = (Class)proxyClassCache.get(type);
			if(proxyClass == null){
				Class clazz = Class.forName(type);
				Class[] classIfaces = clazz.getInterfaces();
				Class[] proxyIfaces = new Class[classIfaces.length + 1];
				System.arraycopy(classIfaces, 0, proxyIfaces, 1, classIfaces.length);
				proxyIfaces[0] = XArchFlatProxyInterface.class;
				proxyClass = Proxy.getProxyClass(XArchFlatProxyUtils.class.getClassLoader(), proxyIfaces);
				proxyClassCache.put(type, proxyClass);
			}
			Object proxy = proxyClass.getConstructor(new Class[]{InvocationHandler.class}).newInstance(new Object[]{new XArchFlatProxy(xArch, objRef)});
			return (T)proxy;
		}
		catch(ClassNotFoundException e){
			throw new RuntimeException(e);
		}
		catch(IllegalAccessException e){
			throw new RuntimeException(e);
		}
		catch(InstantiationException e){
			throw new RuntimeException(e);
		}
		catch(NoSuchMethodException e){
			throw new RuntimeException(e);
		}
		catch(InvocationTargetException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns a collection of proxy objects wrapping each <code>ObjRef</code>.
	 * 
	 * @param xArch
	 *            interface through which to access the objRef
	 * @param objRefs
	 *            <code>ObjRef</code>s to proxy
	 * @return a collection of proxy objects wrapping each <code>ObjRef</code>
	 */
	public static Collection proxy(XArchFlatInterface xArch, ObjRef[] objRefs){
		Object[] objects = new Object[objRefs.length];
		for(int i = 0; i < objRefs.length; i++){
			objects[i] = proxy(xArch, objRefs[i]);
		}
		return Arrays.asList(objects);
	}

	/**
	 * Returns the <code>ObjRef</code> that the proxy object wraps.
	 * 
	 * @param proxy
	 *            the proxy object wrapping an <code>ObjRef</code>
	 * @return the <code>ObjRef</code> that the proxy object wraps
	 * @exception IllegalArgumentException
	 *                if <CODE>proxy</CODE> is not proxied.
	 */
	public static ObjRef getObjRef(Object proxy){
		// null object refs are valid
		if(proxy == null){
			return null;
		}

		if(proxy instanceof XArchFlatProxyInterface){
			return ((XArchFlatProxyInterface)proxy).getProxyHandle().getObjRef();
		}

		throw new IllegalArgumentException("objects must be proxied");
	}

	/**
	 * Returns the <code>XArchFlatInterface</code> used by the proxy object.
	 * 
	 * @param proxy
	 *            the proxy object wrapping an <code>ObjRef</code>
	 * @return the <code>XArchFlatInterface</code> used by the proxy object
	 * @exception IllegalArgumentException
	 *                if <CODE>proxy</CODE> is not proxied.
	 */
	public static XArchFlatInterface getXArchFlatInterface(Object proxy){
		// null object refs are valid
		if(proxy == null){
			return null;
		}

		if(proxy instanceof XArchFlatProxyInterface){
			return ((XArchFlatProxyInterface)proxy).getProxyHandle().getXArch();
		}

		throw new IllegalArgumentException("objects must be proxied");
	}

	/**
	 * Returns the <code>ObjRef</code>s that the proxy objects wrap.
	 * 
	 * @param proxies
	 *            the proxy objects wrapping <code>ObjRef</code>s
	 * @return the <code>ObjRef</code>s that the proxy objects wrap
	 * @exception IllegalArgumentException
	 *                if any of the objects are not proxied.
	 */
	public static ObjRef[] getObjRefs(Collection proxies){
		ObjRef[] objRefs = new ObjRef[proxies.size()];
		int index = 0;
		for(Iterator i = proxies.iterator(); i.hasNext();){
			Object object = i.next();
			objRefs[index++] = getObjRef(object);
		}
		return objRefs;
	}

	/**
	 * Gets an element by its ID within a given xArch tree. If no such element
	 * exists, returns <CODE>null</CODE>.
	 * 
	 * @param xArch
	 *            IXArch object that is the root of the tree to search.
	 * @param id
	 *            The ID to search for.
	 * @return reference the object, or <CODE>null</CODE> if no such object
	 *         exists.
	 * @exception IllegalArgumentException
	 *                if <CODE>xArch</CODE> is not proxied.
	 */
	public static <T extends IXArchElement>T getByID(IXArch xArch, String id){
		if(xArch instanceof XArchFlatProxyInterface){
			XArchFlatInterface xArchFlat = getXArchFlatInterface(xArch);
			ObjRef objRef = xArchFlat.getByID(getObjRef(xArch), id);
			return proxy(xArchFlat, objRef);
		}
		throw new IllegalArgumentException("objects must be proxied");
	}

	/**
	 * Gets the parent (in the XML tree) of a given target object.
	 * 
	 * @param child
	 *            Target object.
	 * @return The target object's parent in the XML tree, or <CODE>null</CODE>
	 *         if it has none.
	 * @exception IllegalArgumentException
	 *                if <CODE>xArch</CODE> is not proxied.
	 */
	public static <T extends IXArchElement>T getParent(IXArchElement child){
		if(child instanceof XArchFlatProxyInterface){
			XArchFlatInterface xArchFlat = getXArchFlatInterface(child);
			ObjRef objRef = getObjRef(child);
			return proxy(xArchFlat, xArchFlat.getParent(objRef));
		}
		throw new IllegalArgumentException("objects must be proxied");
	}

	public static IXArch getXArch(IXArchElement proxy){
		if(proxy instanceof XArchFlatProxyInterface){
			XArchFlatInterface xArchFlat = getXArchFlatInterface(proxy);
			ObjRef objRef = getObjRef(proxy);
			return proxy(xArchFlat, xArchFlat.getXArch(objRef));
		}
		throw new IllegalArgumentException("objects must be proxied");
	}

	public static <T extends IXArchElement>T resolve(IXArch xArch, IXMLLink link){
		if(link == null){
			return null;
		}
		if(link instanceof XArchFlatProxyInterface){
			XArchFlatInterface xArchFlat = getXArchFlatInterface(link);
			ObjRef objRef = xArchFlat.resolveHref(getObjRef(xArch), link.getHref());
			return proxy(xArchFlat, objRef);
		}
		throw new IllegalArgumentException("objects must be proxied");
	}

	@SuppressWarnings("unchecked")
	public static IXArchElement[] getReferencesTo(IXArch xArch, IXArchElement element){
		if(element == null){
			return new IXArchElement[0];
		}
		if(element instanceof XArchFlatProxyInterface){
			XArchFlatInterface xArchFlat = getXArchFlatInterface(element);
			ObjRef[] objRefs = xArchFlat.getReferences(getObjRef(xArch), (String)xArchFlat.get(getObjRef(element), "id"));
			return (IXArchElement[])proxy(xArchFlat, objRefs).toArray(new IXArchElement[objRefs.length]);
		}
		throw new IllegalArgumentException("objects must be proxied");
	}
}
