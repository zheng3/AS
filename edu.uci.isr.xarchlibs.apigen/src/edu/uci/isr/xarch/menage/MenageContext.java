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
package edu.uci.isr.xarch.menage;

import java.util.*;

import edu.uci.isr.xarch.*;

import org.w3c.dom.*;

import edu.uci.isr.xarch.IXArch;
import edu.uci.isr.xarch.IXArchContext;

/**
 * The context object for the menage package.
 * This object is used to create objects that are used
 * in the menage namespace.
 *
 * @author Automatically Generated by xArch apigen
 */
public class MenageContext implements IMenageContext {

	protected static final String DEFAULT_ELT_NAME = "anonymousInstanceTag";
	protected Document doc;
	protected IXArch xArch;

	/**
	 * Create a new MenageContext for the given
	 * IXArch object.
	 * @param xArch XArch object to contextualize in this namespace.
	 */
	public MenageContext(IXArch xArch){
		if(!(xArch instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Node docRootNode = ((DOMBased)xArch).getDOMNode();
		synchronized(DOMUtils.getDOMLock(docRootNode)){
			this.doc = docRootNode.getOwnerDocument();
			xArch.addSchemaLocation("http://www.ics.uci.edu/pub/arch/xArch/menage.xsd", "http://www.isr.uci.edu/projects/xarchuci/ext/menage.xsd");
			this.xArch = xArch;
		}
	}

	public IXArch getXArch(){
		return xArch;
	}
	
	protected Element createElement(String name){
		synchronized(DOMUtils.getDOMLock(doc)){
			return doc.createElementNS(MenageConstants.NS_URI, name);
		}
	}

	public XArchTypeMetadata getTypeMetadata(){
		return IMenageContext.TYPE_METADATA;
	}
	/**
	 * Create an IPosition object in this namespace.
	 * @return New IPosition object.
	 */
	public IPosition createPosition(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, PositionImpl.XSD_TYPE_NSURI, PositionImpl.XSD_TYPE_NAME);
		PositionImpl newElt = new PositionImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IPosition object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IPosition recontextualizePosition(IPosition value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, MenageConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, MenageConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IOptionalComponentPosition object in this namespace.
	 * @return New IOptionalComponentPosition object.
	 */
	public IOptionalComponentPosition createOptionalComponentPosition(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, OptionalComponentPositionImpl.XSD_TYPE_NSURI, OptionalComponentPositionImpl.XSD_TYPE_NAME);
		OptionalComponentPositionImpl newElt = new OptionalComponentPositionImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IOptionalComponentPosition object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IOptionalComponentPosition recontextualizeOptionalComponentPosition(IOptionalComponentPosition value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, MenageConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, MenageConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.options.IOptionalComponent</code>
	 * to one of type <code>IOptionalComponentPosition</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.options.IOptionalComponent</code>
	 * object wraps a DOM element that is the base type, then the 
	 * <code>xsi:type</code> of the base element is promoted.  Otherwise, 
	 * it is left unchanged.
	 *
	 * This function also emits an <CODE>XArchEvent</CODE> with type
	 * PROMOTE_TYPE.  The source for this events is the pre-promoted
	 * IXArchElement object (should no longer be used), and the
	 * target is the post-promotion object.  The target name is
	 * the name of the interface class that was the target of the
	 * promotion.
	 * 
	 * @param value Object to promote.
	 * @return Promoted object.
	 */
	public IOptionalComponentPosition promoteToOptionalComponentPosition(
	edu.uci.isr.xarch.options.IOptionalComponent value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		if(DOMUtils.hasXSIType(elt, 
			edu.uci.isr.xarch.options.OptionalComponentImpl.XSD_TYPE_NSURI,
			edu.uci.isr.xarch.options.OptionalComponentImpl.XSD_TYPE_NAME)){

				DOMUtils.addXSIType(elt, OptionalComponentPositionImpl.XSD_TYPE_NSURI, 
					OptionalComponentPositionImpl.XSD_TYPE_NAME);
		}
		OptionalComponentPositionImpl newElt = new OptionalComponentPositionImpl(elt);
		newElt.setXArch(this.getXArch());

		xArch.fireXArchEvent(
			new XArchEvent(value, 
			XArchEvent.PROMOTE_EVENT,
			XArchEvent.ELEMENT_CHANGED,
			IOptionalComponentPosition.class.getName(), newElt,
			XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, newElt))
		);

		return newElt;
	}

	/**
	 * Create an IOptionalConnectorPosition object in this namespace.
	 * @return New IOptionalConnectorPosition object.
	 */
	public IOptionalConnectorPosition createOptionalConnectorPosition(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, OptionalConnectorPositionImpl.XSD_TYPE_NSURI, OptionalConnectorPositionImpl.XSD_TYPE_NAME);
		OptionalConnectorPositionImpl newElt = new OptionalConnectorPositionImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IOptionalConnectorPosition object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IOptionalConnectorPosition recontextualizeOptionalConnectorPosition(IOptionalConnectorPosition value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, MenageConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, MenageConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.options.IOptionalConnector</code>
	 * to one of type <code>IOptionalConnectorPosition</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.options.IOptionalConnector</code>
	 * object wraps a DOM element that is the base type, then the 
	 * <code>xsi:type</code> of the base element is promoted.  Otherwise, 
	 * it is left unchanged.
	 *
	 * This function also emits an <CODE>XArchEvent</CODE> with type
	 * PROMOTE_TYPE.  The source for this events is the pre-promoted
	 * IXArchElement object (should no longer be used), and the
	 * target is the post-promotion object.  The target name is
	 * the name of the interface class that was the target of the
	 * promotion.
	 * 
	 * @param value Object to promote.
	 * @return Promoted object.
	 */
	public IOptionalConnectorPosition promoteToOptionalConnectorPosition(
	edu.uci.isr.xarch.options.IOptionalConnector value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		if(DOMUtils.hasXSIType(elt, 
			edu.uci.isr.xarch.options.OptionalConnectorImpl.XSD_TYPE_NSURI,
			edu.uci.isr.xarch.options.OptionalConnectorImpl.XSD_TYPE_NAME)){

				DOMUtils.addXSIType(elt, OptionalConnectorPositionImpl.XSD_TYPE_NSURI, 
					OptionalConnectorPositionImpl.XSD_TYPE_NAME);
		}
		OptionalConnectorPositionImpl newElt = new OptionalConnectorPositionImpl(elt);
		newElt.setXArch(this.getXArch());

		xArch.fireXArchEvent(
			new XArchEvent(value, 
			XArchEvent.PROMOTE_EVENT,
			XArchEvent.ELEMENT_CHANGED,
			IOptionalConnectorPosition.class.getName(), newElt,
			XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, newElt))
		);

		return newElt;
	}

	/**
	 * Create an IOptionalInterfacePosition object in this namespace.
	 * @return New IOptionalInterfacePosition object.
	 */
	public IOptionalInterfacePosition createOptionalInterfacePosition(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, OptionalInterfacePositionImpl.XSD_TYPE_NSURI, OptionalInterfacePositionImpl.XSD_TYPE_NAME);
		OptionalInterfacePositionImpl newElt = new OptionalInterfacePositionImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IOptionalInterfacePosition object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IOptionalInterfacePosition recontextualizeOptionalInterfacePosition(IOptionalInterfacePosition value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, MenageConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, MenageConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.options.IOptionalInterface</code>
	 * to one of type <code>IOptionalInterfacePosition</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.options.IOptionalInterface</code>
	 * object wraps a DOM element that is the base type, then the 
	 * <code>xsi:type</code> of the base element is promoted.  Otherwise, 
	 * it is left unchanged.
	 *
	 * This function also emits an <CODE>XArchEvent</CODE> with type
	 * PROMOTE_TYPE.  The source for this events is the pre-promoted
	 * IXArchElement object (should no longer be used), and the
	 * target is the post-promotion object.  The target name is
	 * the name of the interface class that was the target of the
	 * promotion.
	 * 
	 * @param value Object to promote.
	 * @return Promoted object.
	 */
	public IOptionalInterfacePosition promoteToOptionalInterfacePosition(
	edu.uci.isr.xarch.options.IOptionalInterface value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		if(DOMUtils.hasXSIType(elt, 
			edu.uci.isr.xarch.options.OptionalInterfaceImpl.XSD_TYPE_NSURI,
			edu.uci.isr.xarch.options.OptionalInterfaceImpl.XSD_TYPE_NAME)){

				DOMUtils.addXSIType(elt, OptionalInterfacePositionImpl.XSD_TYPE_NSURI, 
					OptionalInterfacePositionImpl.XSD_TYPE_NAME);
		}
		OptionalInterfacePositionImpl newElt = new OptionalInterfacePositionImpl(elt);
		newElt.setXArch(this.getXArch());

		xArch.fireXArchEvent(
			new XArchEvent(value, 
			XArchEvent.PROMOTE_EVENT,
			XArchEvent.ELEMENT_CHANGED,
			IOptionalInterfacePosition.class.getName(), newElt,
			XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, newElt))
		);

		return newElt;
	}

	/**
	 * Create an IOptionalSignaturePosition object in this namespace.
	 * @return New IOptionalSignaturePosition object.
	 */
	public IOptionalSignaturePosition createOptionalSignaturePosition(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, OptionalSignaturePositionImpl.XSD_TYPE_NSURI, OptionalSignaturePositionImpl.XSD_TYPE_NAME);
		OptionalSignaturePositionImpl newElt = new OptionalSignaturePositionImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IOptionalSignaturePosition object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IOptionalSignaturePosition recontextualizeOptionalSignaturePosition(IOptionalSignaturePosition value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, MenageConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, MenageConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.options.IOptionalSignature</code>
	 * to one of type <code>IOptionalSignaturePosition</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.options.IOptionalSignature</code>
	 * object wraps a DOM element that is the base type, then the 
	 * <code>xsi:type</code> of the base element is promoted.  Otherwise, 
	 * it is left unchanged.
	 *
	 * This function also emits an <CODE>XArchEvent</CODE> with type
	 * PROMOTE_TYPE.  The source for this events is the pre-promoted
	 * IXArchElement object (should no longer be used), and the
	 * target is the post-promotion object.  The target name is
	 * the name of the interface class that was the target of the
	 * promotion.
	 * 
	 * @param value Object to promote.
	 * @return Promoted object.
	 */
	public IOptionalSignaturePosition promoteToOptionalSignaturePosition(
	edu.uci.isr.xarch.options.IOptionalSignature value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		if(DOMUtils.hasXSIType(elt, 
			edu.uci.isr.xarch.options.OptionalSignatureImpl.XSD_TYPE_NSURI,
			edu.uci.isr.xarch.options.OptionalSignatureImpl.XSD_TYPE_NAME)){

				DOMUtils.addXSIType(elt, OptionalSignaturePositionImpl.XSD_TYPE_NSURI, 
					OptionalSignaturePositionImpl.XSD_TYPE_NAME);
		}
		OptionalSignaturePositionImpl newElt = new OptionalSignaturePositionImpl(elt);
		newElt.setXArch(this.getXArch());

		xArch.fireXArchEvent(
			new XArchEvent(value, 
			XArchEvent.PROMOTE_EVENT,
			XArchEvent.ELEMENT_CHANGED,
			IOptionalSignaturePosition.class.getName(), newElt,
			XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, newElt))
		);

		return newElt;
	}


}

