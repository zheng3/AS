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
package edu.uci.isr.xarch.options;

import java.util.*;

import edu.uci.isr.xarch.*;

import org.w3c.dom.*;

import edu.uci.isr.xarch.IXArch;
import edu.uci.isr.xarch.IXArchContext;

/**
 * The context object for the options package.
 * This object is used to create objects that are used
 * in the options namespace.
 *
 * @author Automatically Generated by xArch apigen
 */
public class OptionsContext implements IOptionsContext {

	protected static final String DEFAULT_ELT_NAME = "anonymousInstanceTag";
	protected Document doc;
	protected IXArch xArch;

	/**
	 * Create a new OptionsContext for the given
	 * IXArch object.
	 * @param xArch XArch object to contextualize in this namespace.
	 */
	public OptionsContext(IXArch xArch){
		if(!(xArch instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Node docRootNode = ((DOMBased)xArch).getDOMNode();
		synchronized(DOMUtils.getDOMLock(docRootNode)){
			this.doc = docRootNode.getOwnerDocument();
			xArch.addSchemaLocation("http://www.ics.uci.edu/pub/arch/xArch/options.xsd", "http://www.isr.uci.edu/projects/xarchuci/ext/options.xsd");
			this.xArch = xArch;
		}
	}

	public IXArch getXArch(){
		return xArch;
	}
	
	protected Element createElement(String name){
		synchronized(DOMUtils.getDOMLock(doc)){
			return doc.createElementNS(OptionsConstants.NS_URI, name);
		}
	}

	public XArchTypeMetadata getTypeMetadata(){
		return IOptionsContext.TYPE_METADATA;
	}
	/**
	 * Create an IGuard object in this namespace.
	 * @return New IGuard object.
	 */
	public IGuard createGuard(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, GuardImpl.XSD_TYPE_NSURI, GuardImpl.XSD_TYPE_NAME);
		GuardImpl newElt = new GuardImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IGuard object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IGuard recontextualizeGuard(IGuard value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, OptionsConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, OptionsConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IOptional object in this namespace.
	 * @return New IOptional object.
	 */
	public IOptional createOptional(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, OptionalImpl.XSD_TYPE_NSURI, OptionalImpl.XSD_TYPE_NAME);
		OptionalImpl newElt = new OptionalImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IOptional object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IOptional recontextualizeOptional(IOptional value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, OptionsConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, OptionsConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IOptionalComponent object in this namespace.
	 * @return New IOptionalComponent object.
	 */
	public IOptionalComponent createOptionalComponent(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, OptionalComponentImpl.XSD_TYPE_NSURI, OptionalComponentImpl.XSD_TYPE_NAME);
		OptionalComponentImpl newElt = new OptionalComponentImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IOptionalComponent object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IOptionalComponent recontextualizeOptionalComponent(IOptionalComponent value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, OptionsConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, OptionsConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.types.IComponent</code>
	 * to one of type <code>IOptionalComponent</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.types.IComponent</code>
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
	public IOptionalComponent promoteToOptionalComponent(
	edu.uci.isr.xarch.types.IComponent value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		if(DOMUtils.hasXSIType(elt, 
			edu.uci.isr.xarch.types.ComponentImpl.XSD_TYPE_NSURI,
			edu.uci.isr.xarch.types.ComponentImpl.XSD_TYPE_NAME)){

				DOMUtils.addXSIType(elt, OptionalComponentImpl.XSD_TYPE_NSURI, 
					OptionalComponentImpl.XSD_TYPE_NAME);
		}
		OptionalComponentImpl newElt = new OptionalComponentImpl(elt);
		newElt.setXArch(this.getXArch());

		xArch.fireXArchEvent(
			new XArchEvent(value, 
			XArchEvent.PROMOTE_EVENT,
			XArchEvent.ELEMENT_CHANGED,
			IOptionalComponent.class.getName(), newElt,
			XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, newElt))
		);

		return newElt;
	}

	/**
	 * Create an IOptionalConnector object in this namespace.
	 * @return New IOptionalConnector object.
	 */
	public IOptionalConnector createOptionalConnector(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, OptionalConnectorImpl.XSD_TYPE_NSURI, OptionalConnectorImpl.XSD_TYPE_NAME);
		OptionalConnectorImpl newElt = new OptionalConnectorImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IOptionalConnector object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IOptionalConnector recontextualizeOptionalConnector(IOptionalConnector value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, OptionsConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, OptionsConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.types.IConnector</code>
	 * to one of type <code>IOptionalConnector</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.types.IConnector</code>
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
	public IOptionalConnector promoteToOptionalConnector(
	edu.uci.isr.xarch.types.IConnector value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		if(DOMUtils.hasXSIType(elt, 
			edu.uci.isr.xarch.types.ConnectorImpl.XSD_TYPE_NSURI,
			edu.uci.isr.xarch.types.ConnectorImpl.XSD_TYPE_NAME)){

				DOMUtils.addXSIType(elt, OptionalConnectorImpl.XSD_TYPE_NSURI, 
					OptionalConnectorImpl.XSD_TYPE_NAME);
		}
		OptionalConnectorImpl newElt = new OptionalConnectorImpl(elt);
		newElt.setXArch(this.getXArch());

		xArch.fireXArchEvent(
			new XArchEvent(value, 
			XArchEvent.PROMOTE_EVENT,
			XArchEvent.ELEMENT_CHANGED,
			IOptionalConnector.class.getName(), newElt,
			XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, newElt))
		);

		return newElt;
	}

	/**
	 * Create an IOptionalLink object in this namespace.
	 * @return New IOptionalLink object.
	 */
	public IOptionalLink createOptionalLink(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, OptionalLinkImpl.XSD_TYPE_NSURI, OptionalLinkImpl.XSD_TYPE_NAME);
		OptionalLinkImpl newElt = new OptionalLinkImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IOptionalLink object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IOptionalLink recontextualizeOptionalLink(IOptionalLink value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, OptionsConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, OptionsConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.types.ILink</code>
	 * to one of type <code>IOptionalLink</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.types.ILink</code>
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
	public IOptionalLink promoteToOptionalLink(
	edu.uci.isr.xarch.types.ILink value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		if(DOMUtils.hasXSIType(elt, 
			edu.uci.isr.xarch.types.LinkImpl.XSD_TYPE_NSURI,
			edu.uci.isr.xarch.types.LinkImpl.XSD_TYPE_NAME)){

				DOMUtils.addXSIType(elt, OptionalLinkImpl.XSD_TYPE_NSURI, 
					OptionalLinkImpl.XSD_TYPE_NAME);
		}
		OptionalLinkImpl newElt = new OptionalLinkImpl(elt);
		newElt.setXArch(this.getXArch());

		xArch.fireXArchEvent(
			new XArchEvent(value, 
			XArchEvent.PROMOTE_EVENT,
			XArchEvent.ELEMENT_CHANGED,
			IOptionalLink.class.getName(), newElt,
			XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, newElt))
		);

		return newElt;
	}

	/**
	 * Create an IOptionalInterface object in this namespace.
	 * @return New IOptionalInterface object.
	 */
	public IOptionalInterface createOptionalInterface(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, OptionalInterfaceImpl.XSD_TYPE_NSURI, OptionalInterfaceImpl.XSD_TYPE_NAME);
		OptionalInterfaceImpl newElt = new OptionalInterfaceImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IOptionalInterface object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IOptionalInterface recontextualizeOptionalInterface(IOptionalInterface value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, OptionsConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, OptionsConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.types.IInterface</code>
	 * to one of type <code>IOptionalInterface</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.types.IInterface</code>
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
	public IOptionalInterface promoteToOptionalInterface(
	edu.uci.isr.xarch.types.IInterface value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		if(DOMUtils.hasXSIType(elt, 
			edu.uci.isr.xarch.types.InterfaceImpl.XSD_TYPE_NSURI,
			edu.uci.isr.xarch.types.InterfaceImpl.XSD_TYPE_NAME)){

				DOMUtils.addXSIType(elt, OptionalInterfaceImpl.XSD_TYPE_NSURI, 
					OptionalInterfaceImpl.XSD_TYPE_NAME);
		}
		OptionalInterfaceImpl newElt = new OptionalInterfaceImpl(elt);
		newElt.setXArch(this.getXArch());

		xArch.fireXArchEvent(
			new XArchEvent(value, 
			XArchEvent.PROMOTE_EVENT,
			XArchEvent.ELEMENT_CHANGED,
			IOptionalInterface.class.getName(), newElt,
			XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, newElt))
		);

		return newElt;
	}

	/**
	 * Create an IOptionalSignature object in this namespace.
	 * @return New IOptionalSignature object.
	 */
	public IOptionalSignature createOptionalSignature(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, OptionalSignatureImpl.XSD_TYPE_NSURI, OptionalSignatureImpl.XSD_TYPE_NAME);
		OptionalSignatureImpl newElt = new OptionalSignatureImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IOptionalSignature object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IOptionalSignature recontextualizeOptionalSignature(IOptionalSignature value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, OptionsConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, OptionsConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.types.ISignature</code>
	 * to one of type <code>IOptionalSignature</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.types.ISignature</code>
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
	public IOptionalSignature promoteToOptionalSignature(
	edu.uci.isr.xarch.types.ISignature value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		if(DOMUtils.hasXSIType(elt, 
			edu.uci.isr.xarch.types.SignatureImpl.XSD_TYPE_NSURI,
			edu.uci.isr.xarch.types.SignatureImpl.XSD_TYPE_NAME)){

				DOMUtils.addXSIType(elt, OptionalSignatureImpl.XSD_TYPE_NSURI, 
					OptionalSignatureImpl.XSD_TYPE_NAME);
		}
		OptionalSignatureImpl newElt = new OptionalSignatureImpl(elt);
		newElt.setXArch(this.getXArch());

		xArch.fireXArchEvent(
			new XArchEvent(value, 
			XArchEvent.PROMOTE_EVENT,
			XArchEvent.ELEMENT_CHANGED,
			IOptionalSignature.class.getName(), newElt,
			XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, newElt))
		);

		return newElt;
	}

	/**
	 * Create an IOptionalSignatureInterfaceMapping object in this namespace.
	 * @return New IOptionalSignatureInterfaceMapping object.
	 */
	public IOptionalSignatureInterfaceMapping createOptionalSignatureInterfaceMapping(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, OptionalSignatureInterfaceMappingImpl.XSD_TYPE_NSURI, OptionalSignatureInterfaceMappingImpl.XSD_TYPE_NAME);
		OptionalSignatureInterfaceMappingImpl newElt = new OptionalSignatureInterfaceMappingImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IOptionalSignatureInterfaceMapping object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IOptionalSignatureInterfaceMapping recontextualizeOptionalSignatureInterfaceMapping(IOptionalSignatureInterfaceMapping value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, OptionsConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, OptionsConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.types.ISignatureInterfaceMapping</code>
	 * to one of type <code>IOptionalSignatureInterfaceMapping</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.types.ISignatureInterfaceMapping</code>
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
	public IOptionalSignatureInterfaceMapping promoteToOptionalSignatureInterfaceMapping(
	edu.uci.isr.xarch.types.ISignatureInterfaceMapping value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		if(DOMUtils.hasXSIType(elt, 
			edu.uci.isr.xarch.types.SignatureInterfaceMappingImpl.XSD_TYPE_NSURI,
			edu.uci.isr.xarch.types.SignatureInterfaceMappingImpl.XSD_TYPE_NAME)){

				DOMUtils.addXSIType(elt, OptionalSignatureInterfaceMappingImpl.XSD_TYPE_NSURI, 
					OptionalSignatureInterfaceMappingImpl.XSD_TYPE_NAME);
		}
		OptionalSignatureInterfaceMappingImpl newElt = new OptionalSignatureInterfaceMappingImpl(elt);
		newElt.setXArch(this.getXArch());

		xArch.fireXArchEvent(
			new XArchEvent(value, 
			XArchEvent.PROMOTE_EVENT,
			XArchEvent.ELEMENT_CHANGED,
			IOptionalSignatureInterfaceMapping.class.getName(), newElt,
			XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, newElt))
		);

		return newElt;
	}


}

