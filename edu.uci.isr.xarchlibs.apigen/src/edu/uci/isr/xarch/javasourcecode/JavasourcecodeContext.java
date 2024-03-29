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
package edu.uci.isr.xarch.javasourcecode;

import java.util.*;

import edu.uci.isr.xarch.*;

import org.w3c.dom.*;

import edu.uci.isr.xarch.IXArch;
import edu.uci.isr.xarch.IXArchContext;

/**
 * The context object for the javasourcecode package.
 * This object is used to create objects that are used
 * in the javasourcecode namespace.
 *
 * @author Automatically Generated by xArch apigen
 */
public class JavasourcecodeContext implements IJavasourcecodeContext {

	protected static final String DEFAULT_ELT_NAME = "anonymousInstanceTag";
	protected Document doc;
	protected IXArch xArch;

	/**
	 * Create a new JavasourcecodeContext for the given
	 * IXArch object.
	 * @param xArch XArch object to contextualize in this namespace.
	 */
	public JavasourcecodeContext(IXArch xArch){
		if(!(xArch instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Node docRootNode = ((DOMBased)xArch).getDOMNode();
		synchronized(DOMUtils.getDOMLock(docRootNode)){
			this.doc = docRootNode.getOwnerDocument();
			xArch.addSchemaLocation("http://www.ics.uci.edu/pub/arch/xArch/javasourcecode.xsd", "http://www.isr.uci.edu/projects/xarchuci/ext/javasourcecode.xsd");
			this.xArch = xArch;
		}
	}

	public IXArch getXArch(){
		return xArch;
	}
	
	protected Element createElement(String name){
		synchronized(DOMUtils.getDOMLock(doc)){
			return doc.createElementNS(JavasourcecodeConstants.NS_URI, name);
		}
	}

	public XArchTypeMetadata getTypeMetadata(){
		return IJavasourcecodeContext.TYPE_METADATA;
	}
	/**
	 * Create an IJavaSourceCodeManager object in this namespace.
	 * @return New IJavaSourceCodeManager object.
	 */
	public IJavaSourceCodeManager createJavaSourceCodeManager(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, JavaSourceCodeManagerImpl.XSD_TYPE_NSURI, JavaSourceCodeManagerImpl.XSD_TYPE_NAME);
		JavaSourceCodeManagerImpl newElt = new JavaSourceCodeManagerImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IJavaSourceCodeManager object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IJavaSourceCodeManager recontextualizeJavaSourceCodeManager(IJavaSourceCodeManager value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, JavasourcecodeConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, JavasourcecodeConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IRepositoryLocation object in this namespace.
	 * @return New IRepositoryLocation object.
	 */
	public IRepositoryLocation createRepositoryLocation(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, RepositoryLocationImpl.XSD_TYPE_NSURI, RepositoryLocationImpl.XSD_TYPE_NAME);
		RepositoryLocationImpl newElt = new RepositoryLocationImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IRepositoryLocation object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IRepositoryLocation recontextualizeRepositoryLocation(IRepositoryLocation value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, JavasourcecodeConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, JavasourcecodeConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an edu.uci.isr.xarch.instance.IXMLLink object in this namespace.
	 * @return New edu.uci.isr.xarch.instance.IXMLLink object.
	 */
	public edu.uci.isr.xarch.instance.IXMLLink createXMLLink(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, edu.uci.isr.xarch.instance.XMLLinkImpl.XSD_TYPE_NSURI, edu.uci.isr.xarch.instance.XMLLinkImpl.XSD_TYPE_NAME);
		edu.uci.isr.xarch.instance.XMLLinkImpl newElt = new edu.uci.isr.xarch.instance.XMLLinkImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an edu.uci.isr.xarch.instance.IXMLLink object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public edu.uci.isr.xarch.instance.IXMLLink recontextualizeXMLLink(edu.uci.isr.xarch.instance.IXMLLink value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, JavasourcecodeConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, JavasourcecodeConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IJavaSourceFile object in this namespace.
	 * @return New IJavaSourceFile object.
	 */
	public IJavaSourceFile createJavaSourceFile(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, JavaSourceFileImpl.XSD_TYPE_NSURI, JavaSourceFileImpl.XSD_TYPE_NAME);
		JavaSourceFileImpl newElt = new JavaSourceFileImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IJavaSourceFile object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IJavaSourceFile recontextualizeJavaSourceFile(IJavaSourceFile value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, JavasourcecodeConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, JavasourcecodeConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IJavaSourceCodeImplementation object in this namespace.
	 * @return New IJavaSourceCodeImplementation object.
	 */
	public IJavaSourceCodeImplementation createJavaSourceCodeImplementation(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, JavaSourceCodeImplementationImpl.XSD_TYPE_NSURI, JavaSourceCodeImplementationImpl.XSD_TYPE_NAME);
		JavaSourceCodeImplementationImpl newElt = new JavaSourceCodeImplementationImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IJavaSourceCodeImplementation object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IJavaSourceCodeImplementation recontextualizeJavaSourceCodeImplementation(IJavaSourceCodeImplementation value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, JavasourcecodeConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, JavasourcecodeConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.implementation.IImplementation</code>
	 * to one of type <code>IJavaSourceCodeImplementation</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.implementation.IImplementation</code>
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
	public IJavaSourceCodeImplementation promoteToJavaSourceCodeImplementation(
	edu.uci.isr.xarch.implementation.IImplementation value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		if(DOMUtils.hasXSIType(elt, 
			edu.uci.isr.xarch.implementation.ImplementationImpl.XSD_TYPE_NSURI,
			edu.uci.isr.xarch.implementation.ImplementationImpl.XSD_TYPE_NAME)){

				DOMUtils.addXSIType(elt, JavaSourceCodeImplementationImpl.XSD_TYPE_NSURI, 
					JavaSourceCodeImplementationImpl.XSD_TYPE_NAME);
		}
		JavaSourceCodeImplementationImpl newElt = new JavaSourceCodeImplementationImpl(elt);
		newElt.setXArch(this.getXArch());

		xArch.fireXArchEvent(
			new XArchEvent(value, 
			XArchEvent.PROMOTE_EVENT,
			XArchEvent.ELEMENT_CHANGED,
			IJavaSourceCodeImplementation.class.getName(), newElt,
			XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, newElt))
		);

		return newElt;
	}


}

