/*
 * Copyright (c) 2000-2005 Regents of the University of California.
 * All rights reserved.
 *
 * This software was developed at the University of California, Irvine.
 *
 * Redistribution and use in source and binary forms are permitted
 * provided that the above copyright notice and this paragraph are
 * duplicated in all such forms and that any documentation,
 * advertising materials, and other materials related to such
 * distribution and use acknowledge that the software was developed
 * by the University of California, Irvine.  The name of the
 * University may not be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package edu.uci.isr.xarch.uml212superstructure;

import org.w3c.dom.*;

import edu.uci.isr.xarch.*;

import java.util.*;

/**
 * DOM-Based implementation of the IUML212 interface.
 *
 * @author Automatically generated by xArch apigen.
 */
public class UML212Impl implements IUML212, DOMBased{
	
	public static final String XSD_TYPE_NSURI = Uml212superstructureConstants.NS_URI;
	public static final String XSD_TYPE_NAME = "UML212";
	
	protected IXArch xArch;
	
	/** Tag name for ids in this object. */
	public static final String ID_ATTR_NAME = "id";
	/** Tag name for descriptions in this object. */
	public static final String DESCRIPTION_ELT_NAME = "description";
	/** Tag name for useCaseDiagrams in this object. */
	public static final String USE_CASE_DIAGRAM_ELT_NAME = "useCaseDiagram";
	/** Tag name for activityDiagrams in this object. */
	public static final String ACTIVITY_DIAGRAM_ELT_NAME = "activityDiagram";
	/** Tag name for actors in this object. */
	public static final String ACTOR_ELT_NAME = "actor";

	
	protected Element elt;
	
	private static SequenceOrder seqOrd = new SequenceOrder(
		new QName[]{
			new QName(Uml212superstructureConstants.NS_URI, DESCRIPTION_ELT_NAME),
			new QName(Uml212superstructureConstants.NS_URI, USE_CASE_DIAGRAM_ELT_NAME),
			new QName(Uml212superstructureConstants.NS_URI, ACTIVITY_DIAGRAM_ELT_NAME),
			new QName(Uml212superstructureConstants.NS_URI, ACTOR_ELT_NAME)
		}
	);
	
	public UML212Impl(Element elt){
		if(elt == null){
			throw new IllegalArgumentException("Element cannot be null.");
		}
		this.elt = elt;
	}

	public Node getDOMNode(){
		return elt;
	}
	
	public void setDOMNode(Node node){
		if(node.getNodeType() != Node.ELEMENT_NODE){
			throw new IllegalArgumentException("Base DOM node of this type must be an Element.");
		}
		elt = (Element)node;
	}
	
	protected static SequenceOrder getSequenceOrder(){
		return seqOrd;
	}
	
	public void setXArch(IXArch xArch){
		this.xArch = xArch;
	}
	
	public IXArch getXArch(){
		return this.xArch;
	}

	public IXArchElement cloneElement(int depth){
		synchronized(DOMUtils.getDOMLock(elt)){
			Document doc = elt.getOwnerDocument();
			if(depth == 0){
				Element cloneElt = (Element)elt.cloneNode(false);
				cloneElt = (Element)doc.importNode(cloneElt, true);
				UML212Impl cloneImpl = new UML212Impl(cloneElt);
				cloneImpl.setXArch(getXArch());
				return cloneImpl;
			}
			else if(depth == 1){
				Element cloneElt = (Element)elt.cloneNode(false);
				cloneElt = (Element)doc.importNode(cloneElt, true);
				UML212Impl cloneImpl = new UML212Impl(cloneElt);
				cloneImpl.setXArch(getXArch());
				
				NodeList nl = elt.getChildNodes();
				int size = nl.getLength();
				for(int i = 0; i < size; i++){
					Node n = nl.item(i);
					Node cloneNode = (Node)n.cloneNode(false);
					cloneNode = doc.importNode(cloneNode, true);
					cloneElt.appendChild(cloneNode);
				}
				return cloneImpl;
			}
			else /* depth = infinity */{
				Element cloneElt = (Element)elt.cloneNode(true);
				cloneElt = (Element)doc.importNode(cloneElt, true);
				UML212Impl cloneImpl = new UML212Impl(cloneElt);
				cloneImpl.setXArch(getXArch());
				return cloneImpl;
			}
		}
	}

	//Override 'equals' to be DOM-based...	
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		if(!(o instanceof DOMBased)){
			return super.equals(o);
		}
		DOMBased db = (DOMBased)o;
		Node dbNode = db.getDOMNode();
		return dbNode.equals(getDOMNode());
	}

	//Override 'hashCode' to be based on the underlying node
	public int hashCode(){
		return getDOMNode().hashCode();
	}

	/**
	 * For internal use only.
	 */
	private static Object makeDerivedWrapper(Element elt, String baseTypeName){
		synchronized(DOMUtils.getDOMLock(elt)){
			QName typeName = XArchUtils.getXSIType(elt);
			if(typeName == null){
				return null;
			}
			else{
				if(!DOMUtils.hasXSIType(elt, "http://www.ics.uci.edu/pub/arch/xArch/uml212superstructure.xsd", baseTypeName)){
					try{
						String packageTitle = XArchUtils.getPackageTitle(typeName.getNamespaceURI());
						String packageName = XArchUtils.getPackageName(packageTitle);
						String implName = XArchUtils.getImplName(packageName, typeName.getName());
						Class c = Class.forName(implName);
						java.lang.reflect.Constructor con = c.getConstructor(new Class[]{Element.class});
						Object o = con.newInstance(new Object[]{elt});
						return o;
					}
					catch(Exception e){
						//Lots of bad things could happen, but this
						//is OK, because this is best-effort anyway.
					}
				}
				return null;
			}
		}
	}

	public XArchTypeMetadata getTypeMetadata(){
		return IUML212.TYPE_METADATA;
	}

	public XArchInstanceMetadata getInstanceMetadata(){
		return new XArchInstanceMetadata(XArchUtils.getPackageTitle(elt.getNamespaceURI()));
	}
	/**
	 * Set the id attribute on this object.
	 * @param id attribute value.
	 */
	public void setId(String id){
		{
			String oldValue = getId();
			if(oldValue == null ? id == null : oldValue.equals(id))
				return;
			DOMUtils.removeAttribute(elt, Uml212superstructureConstants.NS_URI, ID_ATTR_NAME);
			IXArch _x = getXArch();
			if(_x != null){
				_x.fireXArchEvent(
					new XArchEvent(this, 
					XArchEvent.CLEAR_EVENT,
					XArchEvent.ATTRIBUTE_CHANGED,
					"id", oldValue,
					XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this), true)
				);
			}
		}
		DOMUtils.setAttribute(elt, Uml212superstructureConstants.NS_URI, ID_ATTR_NAME, id);
		IXArch _x = getXArch();
		if(_x != null){
			_x.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.SET_EVENT,
				XArchEvent.ATTRIBUTE_CHANGED,
				"id", id,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
		
	/**
	 * Removes the id attribute from this object.
	 */
	public void clearId(){
		String oldValue = getId();
		if(oldValue == null)
			return;
		DOMUtils.removeAttribute(elt, Uml212superstructureConstants.NS_URI, ID_ATTR_NAME);
		IXArch _x = getXArch();
		if(_x != null){
			_x.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.CLEAR_EVENT,
				XArchEvent.ATTRIBUTE_CHANGED,
				"id", oldValue,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
	
	/**
	 * Gets the value of the id attribute on this object.
	 * @return id attribute's value or <code>null</code> if that
	 * attribute is not set.
	 */
	public String getId(){
		return DOMUtils.getAttributeValue(elt, Uml212superstructureConstants.NS_URI, ID_ATTR_NAME);
	}
	
	/**
	 * Determines if this object's id attribute has the
	 * given value.
	 * @param id value to test.
	 * @return <code>true</code> if the values match, <code>false</code> otherwise.
	 * Matching is done by string-matching.
	 */
	public boolean hasId(String id){
		return DOMUtils.objNullEq(getId(), id);
	}


	public void setDescription(edu.uci.isr.xarch.instance.IDescription value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		{
			edu.uci.isr.xarch.instance.IDescription oldElt = getDescription();
			DOMUtils.removeChildren(elt, Uml212superstructureConstants.NS_URI, DESCRIPTION_ELT_NAME);
			
			IXArch context = getXArch();
			if(context != null){
				context.fireXArchEvent(
					new XArchEvent(this, 
					XArchEvent.CLEAR_EVENT,
					XArchEvent.ELEMENT_CHANGED,
					"description", oldElt,
					XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this), true)
				);
			}
		}
		Element newChildElt = (Element)(((DOMBased)value).getDOMNode());
		newChildElt = DOMUtils.cloneAndRename(newChildElt, Uml212superstructureConstants.NS_URI, DESCRIPTION_ELT_NAME);
		((DOMBased)value).setDOMNode(newChildElt);
		
		synchronized(DOMUtils.getDOMLock(elt)){
			elt.appendChild(newChildElt);
			DOMUtils.order(elt, getSequenceOrder());
		}
		
		IXArch context = getXArch();
		if(context != null){
			context.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.SET_EVENT,
				XArchEvent.ELEMENT_CHANGED,
				"description", value,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
	
	public void clearDescription(){
		edu.uci.isr.xarch.instance.IDescription oldElt = getDescription();
		DOMUtils.removeChildren(elt, Uml212superstructureConstants.NS_URI, DESCRIPTION_ELT_NAME);
		
		IXArch context = getXArch();
		if(context != null){
			context.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.CLEAR_EVENT,
				XArchEvent.ELEMENT_CHANGED,
				"description", oldElt,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
	
	public edu.uci.isr.xarch.instance.IDescription getDescription(){
		NodeList nl = DOMUtils.getChildren(elt, Uml212superstructureConstants.NS_URI, DESCRIPTION_ELT_NAME);
		if(nl.getLength() == 0){
			return null;
		}
		else{
			Element el = (Element)nl.item(0);
			IXArch de = getXArch();
			if(de != null){
				IXArchElement cachedXArchElt = de.getWrapper(el);
				if(cachedXArchElt != null){
					return (edu.uci.isr.xarch.instance.IDescription)cachedXArchElt;
				}
			}
			
			Object o = makeDerivedWrapper(el, "Description");
			if(o != null){
				try{
					((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
					if(de != null){
						de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)o));
					}
					return (edu.uci.isr.xarch.instance.IDescription)o;
				}
				catch(Exception e){}
			}
			edu.uci.isr.xarch.instance.DescriptionImpl eltImpl = new edu.uci.isr.xarch.instance.DescriptionImpl(el);
			eltImpl.setXArch(getXArch());
			if(de != null){
				de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
			}
			return eltImpl;
		}
	}
	
	public boolean hasDescription(edu.uci.isr.xarch.instance.IDescription value){
		edu.uci.isr.xarch.instance.IDescription thisValue = getDescription();
		edu.uci.isr.xarch.instance.IDescription thatValue = value;
		
		if((thisValue == null) && (thatValue == null)){
			return true;
		}
		else if((thisValue == null) && (thatValue != null)){
			return false;
		}
		else if((thisValue != null) && (thatValue == null)){
			return false;
		}
		return thisValue.isEquivalent(thatValue);
	}

	public void addUseCaseDiagram(IUseCase newUseCaseDiagram){
		if(!(newUseCaseDiagram instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		Element newChildElt = (Element)(((DOMBased)newUseCaseDiagram).getDOMNode());
		newChildElt = DOMUtils.cloneAndRename(newChildElt, Uml212superstructureConstants.NS_URI, USE_CASE_DIAGRAM_ELT_NAME);
		((DOMBased)newUseCaseDiagram).setDOMNode(newChildElt);

		synchronized(DOMUtils.getDOMLock(elt)){
			elt.appendChild(newChildElt);
			DOMUtils.order(elt, getSequenceOrder());
		}

		IXArch context = getXArch();
		if(context != null){
			context.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.ADD_EVENT,
				XArchEvent.ELEMENT_CHANGED,
				"useCaseDiagram", newUseCaseDiagram,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
		
	public void addUseCaseDiagrams(Collection useCaseDiagrams){
		for(Iterator en = useCaseDiagrams.iterator(); en.hasNext(); ){
			IUseCase elt = (IUseCase)en.next();
			addUseCaseDiagram(elt);
		}
	}		
		
	public void clearUseCaseDiagrams(){
		//DOMUtils.removeChildren(elt, Uml212superstructureConstants.NS_URI, USE_CASE_DIAGRAM_ELT_NAME);
		Collection coll = getAllUseCaseDiagrams();
		removeUseCaseDiagrams(coll);
	}
	
	public void removeUseCaseDiagram(IUseCase useCaseDiagramToRemove){
		if(!(useCaseDiagramToRemove instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		NodeList nl = DOMUtils.getChildren(elt, Uml212superstructureConstants.NS_URI, USE_CASE_DIAGRAM_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			if(n == ((DOMBased)useCaseDiagramToRemove).getDOMNode()){
				synchronized(DOMUtils.getDOMLock(elt)){
					elt.removeChild(n);
				}

				IXArch context = getXArch();
				if(context != null){
					context.fireXArchEvent(
						new XArchEvent(this, 
						XArchEvent.REMOVE_EVENT,
						XArchEvent.ELEMENT_CHANGED,
						"useCaseDiagram", useCaseDiagramToRemove,
						XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
					);
				}

				return;
			}
		}
	}
	
	public void removeUseCaseDiagrams(Collection useCaseDiagrams){
		for(Iterator en = useCaseDiagrams.iterator(); en.hasNext(); ){
			IUseCase elt = (IUseCase)en.next();
			removeUseCaseDiagram(elt);
		}
	}
	
	public Collection getAllUseCaseDiagrams(){
		NodeList nl = DOMUtils.getChildren(elt, Uml212superstructureConstants.NS_URI, USE_CASE_DIAGRAM_ELT_NAME);
		int nlLength = nl.getLength();
		ArrayList v = new ArrayList(nlLength);
		IXArch de = getXArch();
		for(int i = 0; i < nlLength; i++){
			Element el = (Element)nl.item(i);
			boolean found = false;
			if(de != null){
				IXArchElement cachedXArchElt = de.getWrapper(el);
				if(cachedXArchElt != null){
					v.add((IUseCase)cachedXArchElt);
					found = true;
				}
			}
			if(!found){
				Object o = makeDerivedWrapper(el, "UseCase");	
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)o));
						}
						v.add((IUseCase)o);
					}
					catch(Exception e){
						UseCaseImpl eltImpl = new UseCaseImpl((Element)nl.item(i));
						eltImpl.setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
						}
						v.add(eltImpl);
					}
				}
				else{
					UseCaseImpl eltImpl = new UseCaseImpl((Element)nl.item(i));
					eltImpl.setXArch(getXArch());
					if(de != null){
						de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
					}
					v.add(eltImpl);
				}
			}
		}
		return v;
	}

	public boolean hasUseCaseDiagram(IUseCase useCaseDiagramToCheck){
		Collection c = getAllUseCaseDiagrams();
		
		for(Iterator en = c.iterator(); en.hasNext(); ){
			IUseCase elt = (IUseCase)en.next();
			if(elt.isEquivalent(useCaseDiagramToCheck)){
				return true;
			}
		}
		return false;
	}
	
	public Collection hasUseCaseDiagrams(Collection useCaseDiagramsToCheck){
		Vector v = new Vector();
		for(Iterator en = useCaseDiagramsToCheck.iterator(); en.hasNext(); ){
			IUseCase elt = (IUseCase)en.next();
			v.addElement(new Boolean(hasUseCaseDiagram(elt)));
		}
		return v;
	}
		
	public boolean hasAllUseCaseDiagrams(Collection useCaseDiagramsToCheck){
		for(Iterator en = useCaseDiagramsToCheck.iterator(); en.hasNext(); ){
			IUseCase elt = (IUseCase)en.next();
			if(!hasUseCaseDiagram(elt)){
				return false;
			}
		}
		return true;
	}
	public IUseCase getUseCaseDiagram(String id){
		NodeList nl = DOMUtils.getChildren(elt, Uml212superstructureConstants.NS_URI, USE_CASE_DIAGRAM_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			
			IUseCase el = new UseCaseImpl((Element)nl.item(i));
			if(DOMUtils.objNullEq(id, el.getId())){
				Element domElt = (Element)nl.item(i);
				Object o = makeDerivedWrapper(domElt, "UseCase");
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						return (IUseCase)o;
					}
					catch(Exception e){}
				}
				el.setXArch(getXArch());
				return el;
			}
		}
		return null;
	}
	
	public Collection getUseCaseDiagrams(Collection ids){
		//If there is an ID that does not exist, it is simply ignored.
		//You can tell if this happened if ids.size() != returned collection.size().
		Vector v = new Vector();
		for(Iterator en = ids.iterator(); en.hasNext(); ){
			String elt = (String)en.next();
			IUseCase retElt = getUseCaseDiagram(elt);
			if(retElt != null){
				v.addElement(retElt);
			}
		}
		return v;
	}	
	
	public void addActivityDiagram(IActivityDiagram newActivityDiagram){
		if(!(newActivityDiagram instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		Element newChildElt = (Element)(((DOMBased)newActivityDiagram).getDOMNode());
		newChildElt = DOMUtils.cloneAndRename(newChildElt, Uml212superstructureConstants.NS_URI, ACTIVITY_DIAGRAM_ELT_NAME);
		((DOMBased)newActivityDiagram).setDOMNode(newChildElt);

		synchronized(DOMUtils.getDOMLock(elt)){
			elt.appendChild(newChildElt);
			DOMUtils.order(elt, getSequenceOrder());
		}

		IXArch context = getXArch();
		if(context != null){
			context.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.ADD_EVENT,
				XArchEvent.ELEMENT_CHANGED,
				"activityDiagram", newActivityDiagram,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
		
	public void addActivityDiagrams(Collection activityDiagrams){
		for(Iterator en = activityDiagrams.iterator(); en.hasNext(); ){
			IActivityDiagram elt = (IActivityDiagram)en.next();
			addActivityDiagram(elt);
		}
	}		
		
	public void clearActivityDiagrams(){
		//DOMUtils.removeChildren(elt, Uml212superstructureConstants.NS_URI, ACTIVITY_DIAGRAM_ELT_NAME);
		Collection coll = getAllActivityDiagrams();
		removeActivityDiagrams(coll);
	}
	
	public void removeActivityDiagram(IActivityDiagram activityDiagramToRemove){
		if(!(activityDiagramToRemove instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		NodeList nl = DOMUtils.getChildren(elt, Uml212superstructureConstants.NS_URI, ACTIVITY_DIAGRAM_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			if(n == ((DOMBased)activityDiagramToRemove).getDOMNode()){
				synchronized(DOMUtils.getDOMLock(elt)){
					elt.removeChild(n);
				}

				IXArch context = getXArch();
				if(context != null){
					context.fireXArchEvent(
						new XArchEvent(this, 
						XArchEvent.REMOVE_EVENT,
						XArchEvent.ELEMENT_CHANGED,
						"activityDiagram", activityDiagramToRemove,
						XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
					);
				}

				return;
			}
		}
	}
	
	public void removeActivityDiagrams(Collection activityDiagrams){
		for(Iterator en = activityDiagrams.iterator(); en.hasNext(); ){
			IActivityDiagram elt = (IActivityDiagram)en.next();
			removeActivityDiagram(elt);
		}
	}
	
	public Collection getAllActivityDiagrams(){
		NodeList nl = DOMUtils.getChildren(elt, Uml212superstructureConstants.NS_URI, ACTIVITY_DIAGRAM_ELT_NAME);
		int nlLength = nl.getLength();
		ArrayList v = new ArrayList(nlLength);
		IXArch de = getXArch();
		for(int i = 0; i < nlLength; i++){
			Element el = (Element)nl.item(i);
			boolean found = false;
			if(de != null){
				IXArchElement cachedXArchElt = de.getWrapper(el);
				if(cachedXArchElt != null){
					v.add((IActivityDiagram)cachedXArchElt);
					found = true;
				}
			}
			if(!found){
				Object o = makeDerivedWrapper(el, "ActivityDiagram");	
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)o));
						}
						v.add((IActivityDiagram)o);
					}
					catch(Exception e){
						ActivityDiagramImpl eltImpl = new ActivityDiagramImpl((Element)nl.item(i));
						eltImpl.setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
						}
						v.add(eltImpl);
					}
				}
				else{
					ActivityDiagramImpl eltImpl = new ActivityDiagramImpl((Element)nl.item(i));
					eltImpl.setXArch(getXArch());
					if(de != null){
						de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
					}
					v.add(eltImpl);
				}
			}
		}
		return v;
	}

	public boolean hasActivityDiagram(IActivityDiagram activityDiagramToCheck){
		Collection c = getAllActivityDiagrams();
		
		for(Iterator en = c.iterator(); en.hasNext(); ){
			IActivityDiagram elt = (IActivityDiagram)en.next();
			if(elt.isEquivalent(activityDiagramToCheck)){
				return true;
			}
		}
		return false;
	}
	
	public Collection hasActivityDiagrams(Collection activityDiagramsToCheck){
		Vector v = new Vector();
		for(Iterator en = activityDiagramsToCheck.iterator(); en.hasNext(); ){
			IActivityDiagram elt = (IActivityDiagram)en.next();
			v.addElement(new Boolean(hasActivityDiagram(elt)));
		}
		return v;
	}
		
	public boolean hasAllActivityDiagrams(Collection activityDiagramsToCheck){
		for(Iterator en = activityDiagramsToCheck.iterator(); en.hasNext(); ){
			IActivityDiagram elt = (IActivityDiagram)en.next();
			if(!hasActivityDiagram(elt)){
				return false;
			}
		}
		return true;
	}
	public IActivityDiagram getActivityDiagram(String id){
		NodeList nl = DOMUtils.getChildren(elt, Uml212superstructureConstants.NS_URI, ACTIVITY_DIAGRAM_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			
			IActivityDiagram el = new ActivityDiagramImpl((Element)nl.item(i));
			if(DOMUtils.objNullEq(id, el.getId())){
				Element domElt = (Element)nl.item(i);
				Object o = makeDerivedWrapper(domElt, "ActivityDiagram");
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						return (IActivityDiagram)o;
					}
					catch(Exception e){}
				}
				el.setXArch(getXArch());
				return el;
			}
		}
		return null;
	}
	
	public Collection getActivityDiagrams(Collection ids){
		//If there is an ID that does not exist, it is simply ignored.
		//You can tell if this happened if ids.size() != returned collection.size().
		Vector v = new Vector();
		for(Iterator en = ids.iterator(); en.hasNext(); ){
			String elt = (String)en.next();
			IActivityDiagram retElt = getActivityDiagram(elt);
			if(retElt != null){
				v.addElement(retElt);
			}
		}
		return v;
	}	
	
	public void addActor(IActor newActor){
		if(!(newActor instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		Element newChildElt = (Element)(((DOMBased)newActor).getDOMNode());
		newChildElt = DOMUtils.cloneAndRename(newChildElt, Uml212superstructureConstants.NS_URI, ACTOR_ELT_NAME);
		((DOMBased)newActor).setDOMNode(newChildElt);

		synchronized(DOMUtils.getDOMLock(elt)){
			elt.appendChild(newChildElt);
			DOMUtils.order(elt, getSequenceOrder());
		}

		IXArch context = getXArch();
		if(context != null){
			context.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.ADD_EVENT,
				XArchEvent.ELEMENT_CHANGED,
				"actor", newActor,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
		
	public void addActors(Collection actors){
		for(Iterator en = actors.iterator(); en.hasNext(); ){
			IActor elt = (IActor)en.next();
			addActor(elt);
		}
	}		
		
	public void clearActors(){
		//DOMUtils.removeChildren(elt, Uml212superstructureConstants.NS_URI, ACTOR_ELT_NAME);
		Collection coll = getAllActors();
		removeActors(coll);
	}
	
	public void removeActor(IActor actorToRemove){
		if(!(actorToRemove instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		NodeList nl = DOMUtils.getChildren(elt, Uml212superstructureConstants.NS_URI, ACTOR_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			if(n == ((DOMBased)actorToRemove).getDOMNode()){
				synchronized(DOMUtils.getDOMLock(elt)){
					elt.removeChild(n);
				}

				IXArch context = getXArch();
				if(context != null){
					context.fireXArchEvent(
						new XArchEvent(this, 
						XArchEvent.REMOVE_EVENT,
						XArchEvent.ELEMENT_CHANGED,
						"actor", actorToRemove,
						XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
					);
				}

				return;
			}
		}
	}
	
	public void removeActors(Collection actors){
		for(Iterator en = actors.iterator(); en.hasNext(); ){
			IActor elt = (IActor)en.next();
			removeActor(elt);
		}
	}
	
	public Collection getAllActors(){
		NodeList nl = DOMUtils.getChildren(elt, Uml212superstructureConstants.NS_URI, ACTOR_ELT_NAME);
		int nlLength = nl.getLength();
		ArrayList v = new ArrayList(nlLength);
		IXArch de = getXArch();
		for(int i = 0; i < nlLength; i++){
			Element el = (Element)nl.item(i);
			boolean found = false;
			if(de != null){
				IXArchElement cachedXArchElt = de.getWrapper(el);
				if(cachedXArchElt != null){
					v.add((IActor)cachedXArchElt);
					found = true;
				}
			}
			if(!found){
				Object o = makeDerivedWrapper(el, "Actor");	
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)o));
						}
						v.add((IActor)o);
					}
					catch(Exception e){
						ActorImpl eltImpl = new ActorImpl((Element)nl.item(i));
						eltImpl.setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
						}
						v.add(eltImpl);
					}
				}
				else{
					ActorImpl eltImpl = new ActorImpl((Element)nl.item(i));
					eltImpl.setXArch(getXArch());
					if(de != null){
						de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
					}
					v.add(eltImpl);
				}
			}
		}
		return v;
	}

	public boolean hasActor(IActor actorToCheck){
		Collection c = getAllActors();
		
		for(Iterator en = c.iterator(); en.hasNext(); ){
			IActor elt = (IActor)en.next();
			if(elt.isEquivalent(actorToCheck)){
				return true;
			}
		}
		return false;
	}
	
	public Collection hasActors(Collection actorsToCheck){
		Vector v = new Vector();
		for(Iterator en = actorsToCheck.iterator(); en.hasNext(); ){
			IActor elt = (IActor)en.next();
			v.addElement(new Boolean(hasActor(elt)));
		}
		return v;
	}
		
	public boolean hasAllActors(Collection actorsToCheck){
		for(Iterator en = actorsToCheck.iterator(); en.hasNext(); ){
			IActor elt = (IActor)en.next();
			if(!hasActor(elt)){
				return false;
			}
		}
		return true;
	}
	public IActor getActor(String id){
		NodeList nl = DOMUtils.getChildren(elt, Uml212superstructureConstants.NS_URI, ACTOR_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			
			IActor el = new ActorImpl((Element)nl.item(i));
			if(DOMUtils.objNullEq(id, el.getId())){
				Element domElt = (Element)nl.item(i);
				Object o = makeDerivedWrapper(domElt, "Actor");
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						return (IActor)o;
					}
					catch(Exception e){}
				}
				el.setXArch(getXArch());
				return el;
			}
		}
		return null;
	}
	
	public Collection getActors(Collection ids){
		//If there is an ID that does not exist, it is simply ignored.
		//You can tell if this happened if ids.size() != returned collection.size().
		Vector v = new Vector();
		for(Iterator en = ids.iterator(); en.hasNext(); ){
			String elt = (String)en.next();
			IActor retElt = getActor(elt);
			if(retElt != null){
				v.addElement(retElt);
			}
		}
		return v;
	}	
	
	public boolean isEqual(IUML212 UML212ToCheck){
		String thisId = getId();
		String thatId = UML212ToCheck.getId();
		
		if((thisId == null) || (thatId == null)){
			throw new IllegalArgumentException("One of the arguments is missing an ID.");
		}
		return thisId.equals(thatId);
	}
	
	public boolean isEquivalent(IUML212 c){
		return (getClass().equals(c.getClass())) &&
			hasDescription(c.getDescription()) &&
			hasAllUseCaseDiagrams(c.getAllUseCaseDiagrams()) &&
			c.hasAllUseCaseDiagrams(getAllUseCaseDiagrams()) &&
			hasAllActivityDiagrams(c.getAllActivityDiagrams()) &&
			c.hasAllActivityDiagrams(getAllActivityDiagrams()) &&
			hasAllActors(c.getAllActors()) &&
			c.hasAllActors(getAllActors()) ;
	}

}
