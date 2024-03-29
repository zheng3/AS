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
package edu.uci.isr.xarch.instance;

import java.util.*;

import edu.uci.isr.xarch.*;

import org.w3c.dom.*;

import edu.uci.isr.xarch.IXArch;
import edu.uci.isr.xarch.IXArchContext;

/**
 * The context object for the instance package.
 * This object is used to create objects that are used
 * in the instance namespace.
 *
 * @author Automatically Generated by xArch apigen
 */
public class InstanceContext implements IInstanceContext {

	protected static final String DEFAULT_ELT_NAME = "anonymousInstanceTag";
	protected Document doc;
	protected IXArch xArch;

	/**
	 * Create a new InstanceContext for the given
	 * IXArch object.
	 * @param xArch XArch object to contextualize in this namespace.
	 */
	public InstanceContext(IXArch xArch){
		if(!(xArch instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Node docRootNode = ((DOMBased)xArch).getDOMNode();
		synchronized(DOMUtils.getDOMLock(docRootNode)){
			this.doc = docRootNode.getOwnerDocument();
			xArch.addSchemaLocation("http://www.ics.uci.edu/pub/arch/xArch/instance.xsd", "http://www.isr.uci.edu/projects/xarchuci/core/instance.xsd");
			this.xArch = xArch;
		}
	}

	public IXArch getXArch(){
		return xArch;
	}
	
	protected Element createElement(String name){
		synchronized(DOMUtils.getDOMLock(doc)){
			return doc.createElementNS(InstanceConstants.NS_URI, name);
		}
	}

	public XArchTypeMetadata getTypeMetadata(){
		return IInstanceContext.TYPE_METADATA;
	}
	/**
	 * Create an IIdentifier object in this namespace.
	 * @return New IIdentifier object.
	 */
	public IIdentifier createIdentifier(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, IdentifierImpl.XSD_TYPE_NSURI, IdentifierImpl.XSD_TYPE_NAME);
		IdentifierImpl newElt = new IdentifierImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IIdentifier object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IIdentifier recontextualizeIdentifier(IIdentifier value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, InstanceConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, InstanceConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IDirectionSimpleType object in this namespace.
	 * @return New IDirectionSimpleType object.
	 */
	public IDirectionSimpleType createDirectionSimpleType(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, DirectionSimpleTypeImpl.XSD_TYPE_NSURI, DirectionSimpleTypeImpl.XSD_TYPE_NAME);
		DirectionSimpleTypeImpl newElt = new DirectionSimpleTypeImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IDirectionSimpleType object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IDirectionSimpleType recontextualizeDirectionSimpleType(IDirectionSimpleType value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, InstanceConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, InstanceConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IDescription object in this namespace.
	 * @return New IDescription object.
	 */
	public IDescription createDescription(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, DescriptionImpl.XSD_TYPE_NSURI, DescriptionImpl.XSD_TYPE_NAME);
		DescriptionImpl newElt = new DescriptionImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IDescription object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IDescription recontextualizeDescription(IDescription value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, InstanceConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, InstanceConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IDirection object in this namespace.
	 * @return New IDirection object.
	 */
	public IDirection createDirection(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, DirectionImpl.XSD_TYPE_NSURI, DirectionImpl.XSD_TYPE_NAME);
		DirectionImpl newElt = new DirectionImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IDirection object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IDirection recontextualizeDirection(IDirection value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, InstanceConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, InstanceConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IXMLLink object in this namespace.
	 * @return New IXMLLink object.
	 */
	public IXMLLink createXMLLink(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, XMLLinkImpl.XSD_TYPE_NSURI, XMLLinkImpl.XSD_TYPE_NAME);
		XMLLinkImpl newElt = new XMLLinkImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IXMLLink object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IXMLLink recontextualizeXMLLink(IXMLLink value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, InstanceConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, InstanceConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IInterfaceInstance object in this namespace.
	 * @return New IInterfaceInstance object.
	 */
	public IInterfaceInstance createInterfaceInstance(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, InterfaceInstanceImpl.XSD_TYPE_NSURI, InterfaceInstanceImpl.XSD_TYPE_NAME);
		InterfaceInstanceImpl newElt = new InterfaceInstanceImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IInterfaceInstance object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IInterfaceInstance recontextualizeInterfaceInstance(IInterfaceInstance value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, InstanceConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, InstanceConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IComponentInstance object in this namespace.
	 * @return New IComponentInstance object.
	 */
	public IComponentInstance createComponentInstance(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, ComponentInstanceImpl.XSD_TYPE_NSURI, ComponentInstanceImpl.XSD_TYPE_NAME);
		ComponentInstanceImpl newElt = new ComponentInstanceImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IComponentInstance object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IComponentInstance recontextualizeComponentInstance(IComponentInstance value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, InstanceConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, InstanceConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IConnectorInstance object in this namespace.
	 * @return New IConnectorInstance object.
	 */
	public IConnectorInstance createConnectorInstance(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, ConnectorInstanceImpl.XSD_TYPE_NSURI, ConnectorInstanceImpl.XSD_TYPE_NAME);
		ConnectorInstanceImpl newElt = new ConnectorInstanceImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IConnectorInstance object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IConnectorInstance recontextualizeConnectorInstance(IConnectorInstance value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, InstanceConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, InstanceConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IPoint object in this namespace.
	 * @return New IPoint object.
	 */
	public IPoint createPoint(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, PointImpl.XSD_TYPE_NSURI, PointImpl.XSD_TYPE_NAME);
		PointImpl newElt = new PointImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IPoint object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IPoint recontextualizePoint(IPoint value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, InstanceConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, InstanceConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an ILinkInstance object in this namespace.
	 * @return New ILinkInstance object.
	 */
	public ILinkInstance createLinkInstance(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, LinkInstanceImpl.XSD_TYPE_NSURI, LinkInstanceImpl.XSD_TYPE_NAME);
		LinkInstanceImpl newElt = new LinkInstanceImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an ILinkInstance object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public ILinkInstance recontextualizeLinkInstance(ILinkInstance value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, InstanceConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, InstanceConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IGroup object in this namespace.
	 * @return New IGroup object.
	 */
	public IGroup createGroup(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, GroupImpl.XSD_TYPE_NSURI, GroupImpl.XSD_TYPE_NAME);
		GroupImpl newElt = new GroupImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IGroup object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IGroup recontextualizeGroup(IGroup value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, InstanceConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, InstanceConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IInterfaceInstanceMapping object in this namespace.
	 * @return New IInterfaceInstanceMapping object.
	 */
	public IInterfaceInstanceMapping createInterfaceInstanceMapping(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, InterfaceInstanceMappingImpl.XSD_TYPE_NSURI, InterfaceInstanceMappingImpl.XSD_TYPE_NAME);
		InterfaceInstanceMappingImpl newElt = new InterfaceInstanceMappingImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IInterfaceInstanceMapping object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IInterfaceInstanceMapping recontextualizeInterfaceInstanceMapping(IInterfaceInstanceMapping value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, InstanceConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, InstanceConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an ISubArchitecture object in this namespace.
	 * @return New ISubArchitecture object.
	 */
	public ISubArchitecture createSubArchitecture(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, SubArchitectureImpl.XSD_TYPE_NSURI, SubArchitectureImpl.XSD_TYPE_NAME);
		SubArchitectureImpl newElt = new SubArchitectureImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an ISubArchitecture object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public ISubArchitecture recontextualizeSubArchitecture(ISubArchitecture value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, InstanceConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, InstanceConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IArchInstance object in this namespace.
	 * @return New IArchInstance object.
	 */
	public IArchInstance createArchInstance(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, ArchInstanceImpl.XSD_TYPE_NSURI, ArchInstanceImpl.XSD_TYPE_NAME);
		ArchInstanceImpl newElt = new ArchInstanceImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IArchInstance object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IArchInstance recontextualizeArchInstance(IArchInstance value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, InstanceConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, InstanceConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create a top-level element of type <code>IArchInstance</code>.
	 * This function should be used in lieu of <code>createArchInstance</code>
	 * if the element is to be added as a sub-object of <code>IXArch</code>.
	 * @return new IArchInstance suitable for adding
	 * as a child of xArch.
	 */
	public IArchInstance createArchInstanceElement(){
		Element elt = createElement("archInstance");
		DOMUtils.addXSIType(elt, ArchInstanceImpl.XSD_TYPE_NSURI, 
			ArchInstanceImpl.XSD_TYPE_NAME);
		ArchInstanceImpl newElt = new ArchInstanceImpl(elt);

		IXArch de = getXArch();
		newElt.setXArch(de);
		return newElt;
	}

	/**
	 * Gets the IArchInstance child from the given <code>IXArch</code>
	 * element.  If there are multiple matching children, this returns the first one.
	 * @param xArch <code>IXArch</code> object from which to get the child.
	 * @return <code>IArchInstance</code> that is the child
	 * of <code>xArch</code> or <code>null</code> if no such object exists.
	 */
	public IArchInstance getArchInstance(IXArch xArch){
		if(!(xArch instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Collection elementCollection = xArch.getAllObjects();
		for(Iterator en = elementCollection.iterator(); en.hasNext(); ){
			Object o = en.next();
			if(o instanceof IArchInstance){
				return (IArchInstance)o;
			}
			else if(o instanceof Element){
				Element elt = (Element)o;
				synchronized(DOMUtils.getDOMLock(elt)){
					String nsURI = elt.getNamespaceURI();
					String localName = elt.getLocalName();
					if((nsURI != null) && (nsURI.equals(InstanceConstants.NS_URI))){
						if((localName != null) && (localName.equals("archInstance"))){
							ArchInstanceImpl newElt = new ArchInstanceImpl(elt);
							newElt.setXArch(this.getXArch());
							return newElt;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets all the IArchInstance children from the given 
	 * <code>IXArch</code> element.
	 * @param xArch <code>IXArch</code> object from which to get the children.
	 * @return Collection of <code>IArchInstance</code> that are 	
	 * the children of <code>xArch</code> or an empty collection if no such object exists.
	 */
	public Collection getAllArchInstances(IXArch xArch){
		if(!(xArch instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Collection elementCollection = xArch.getAllObjects();
		Vector v = new Vector();

		for(Iterator en = elementCollection.iterator(); en.hasNext(); ){
			Object o = en.next();
			if(o instanceof IArchInstance){
				v.addElement(o);
			}
			else if(o instanceof Element){
				Element elt = (Element)o;
				synchronized(DOMUtils.getDOMLock(elt)){
					String nsURI = elt.getNamespaceURI();
					String localName = elt.getLocalName();
					if((nsURI != null) && (nsURI.equals(InstanceConstants.NS_URI))){
						if((localName != null) && (localName.equals("archInstance"))){
							ArchInstanceImpl newElt = new ArchInstanceImpl(elt);
							newElt.setXArch(this.getXArch());
							v.addElement(newElt);
						}
					}
				}
			}
		}
		return v;
	}

}

