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
package edu.uci.isr.xarch.activitydiagrams;

import org.w3c.dom.*;

import edu.uci.isr.xarch.*;

import java.util.*;

/**
 * DOM-Based implementation of the INote interface.
 *
 * @author Automatically generated by xArch apigen.
 */
public class NoteImpl implements INote, DOMBased{
	
	public static final String XSD_TYPE_NSURI = ActivitydiagramsConstants.NS_URI;
	public static final String XSD_TYPE_NAME = "Note";
	
	protected IXArch xArch;
	
	/** Tag name for ids in this object. */
	public static final String ID_ATTR_NAME = "id";
	/** Tag name for descriptions in this object. */
	public static final String DESCRIPTION_ELT_NAME = "description";

	
	protected Element elt;
	
	private static SequenceOrder seqOrd = new SequenceOrder(
		new QName[]{
			new QName(ActivitydiagramsConstants.NS_URI, DESCRIPTION_ELT_NAME)
		}
	);
	
	public NoteImpl(Element elt){
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
				NoteImpl cloneImpl = new NoteImpl(cloneElt);
				cloneImpl.setXArch(getXArch());
				return cloneImpl;
			}
			else if(depth == 1){
				Element cloneElt = (Element)elt.cloneNode(false);
				cloneElt = (Element)doc.importNode(cloneElt, true);
				NoteImpl cloneImpl = new NoteImpl(cloneElt);
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
				NoteImpl cloneImpl = new NoteImpl(cloneElt);
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
				if(!DOMUtils.hasXSIType(elt, "http://www.ics.uci.edu/pub/arch/xArch/activitydiagrams.xsd", baseTypeName)){
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
		return INote.TYPE_METADATA;
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
			DOMUtils.removeAttribute(elt, ActivitydiagramsConstants.NS_URI, ID_ATTR_NAME);
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
		DOMUtils.setAttribute(elt, ActivitydiagramsConstants.NS_URI, ID_ATTR_NAME, id);
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
		DOMUtils.removeAttribute(elt, ActivitydiagramsConstants.NS_URI, ID_ATTR_NAME);
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
		return DOMUtils.getAttributeValue(elt, ActivitydiagramsConstants.NS_URI, ID_ATTR_NAME);
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
			DOMUtils.removeChildren(elt, ActivitydiagramsConstants.NS_URI, DESCRIPTION_ELT_NAME);
			
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
		newChildElt = DOMUtils.cloneAndRename(newChildElt, ActivitydiagramsConstants.NS_URI, DESCRIPTION_ELT_NAME);
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
		DOMUtils.removeChildren(elt, ActivitydiagramsConstants.NS_URI, DESCRIPTION_ELT_NAME);
		
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
		NodeList nl = DOMUtils.getChildren(elt, ActivitydiagramsConstants.NS_URI, DESCRIPTION_ELT_NAME);
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

	public boolean isEqual(INote NoteToCheck){
		String thisId = getId();
		String thatId = NoteToCheck.getId();
		
		if((thisId == null) || (thatId == null)){
			throw new IllegalArgumentException("One of the arguments is missing an ID.");
		}
		return thisId.equals(thatId);
	}
	
	public boolean isEquivalent(INote c){
		return (getClass().equals(c.getClass())) &&
			hasDescription(c.getDescription()) ;
	}

}
