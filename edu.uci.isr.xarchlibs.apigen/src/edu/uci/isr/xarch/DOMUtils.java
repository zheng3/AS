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
package edu.uci.isr.xarch;

import org.w3c.dom.*;

/**
 * Some utility functions for working with the DOM API.
 *
 * @author Eric M. Dashofy (edashofy@ics.uci.edu)
 */
public class DOMUtils{

	/**
	 * Gets the DOM lock for a particular node.  Since DOM
	 * is not thread-safe (and this can be a real problem),
	 * methods that access the DOM directly should synchronize
	 * on the object returned by this method.
	 * @param n The node
	 * @return An object suitable for synchronization.
	 */
	public static final Object getDOMLock(Node n){
		return DOMUtils.class;
	}
	
	/** 
	 * This variable is set to TRUE if we're using JDK 1.4.2 or
	 * below; this fixes an obscure DOM bug that involves parsed
	 * documents' Attrs not having the proper ownerDocument and
	 * sometimes causing WRONG_DOCUMENT_ERR 
	 */
	protected static boolean WRONG_DOCUMENT_ERR_WORKAROUND;
	
	static{
		String jvmVersion = System.getProperty("java.version");
		if(jvmVersion.startsWith("1.4")){
			WRONG_DOCUMENT_ERR_WORKAROUND = true;
		}
		else if(jvmVersion.startsWith("1.3")){
			WRONG_DOCUMENT_ERR_WORKAROUND = true;
		}
		else if(jvmVersion.startsWith("1.2")){
			WRONG_DOCUMENT_ERR_WORKAROUND = true;
		}
		else{
			WRONG_DOCUMENT_ERR_WORKAROUND = false;
		}
	}
	
	/**
	 * Adds the xsi:type attribute to an element.
	 * @param elt Element to annotate
	 * @param namespaceURI Namespace URI for the xsi:type
	 * @param typeName Type name for the xsi:type
	 */
	public static void addXSIType(Element elt, String namespaceURI, String typeName){
		synchronized(getDOMLock(elt)){
			elt.setAttributeNS(XArchConstants.XSI_NS_URI, "type", "unfixed$" + namespaceURI + ":" + typeName);
		}
	}
	
	/**
	 * Determines if the element has the given xsi:type.  If the type
	 * has already been fixed with a prefix instead of a namespace
	 * URI, this will walk up the DOM tree to find the namespace URI.
	 
	 * @param elt Element to check
	 * @param namespaceURI Namespace URI for the xsi:type
	 * @param typeName Type name for the xsi:type
	 * @return <code>true</code> if the element has the given xsi:type,
	 * <code>false</code> otherwise or if the namespace URI cannot be determined.
	 */
	public static boolean hasXSIType(Element elt, String namespaceURI, String typeName){
		synchronized(getDOMLock(elt)){
			QName qName = DOMUtils.getXSIType(elt);
			if(qName == null){
				return false;
			}
			
			String thisNamespaceURI = qName.getNamespaceURI();
			String thisTypeName = qName.getName();
			
			if((thisNamespaceURI == null) && (namespaceURI == null)){
				return typeName.equals(thisTypeName);
			}
			if((thisNamespaceURI == null) || (namespaceURI == null)){
				return false;
			}
			if(!thisNamespaceURI.equals(namespaceURI)){
				return false;
			}
			return typeName.equals(thisTypeName);
		}
	}

	/**
	 * Gets the xsi:type of an element in the form of a qualified name. If the type
	 * has already been fixed with a prefix instead of a namespace
	 * URI, this will walk up the DOM tree to find the namespace URI.
	 * @param elt Element to check
	 * @return Qualified name representing the xsi:type
	 */
	public static QName getXSIType(Element elt){
		synchronized(getDOMLock(elt)){
			String xsiTypeString = getAttributeValue(elt, XArchConstants.XSI_NS_URI, "type");
			if(xsiTypeString == null){
				return null;
			}
			if(xsiTypeString.startsWith("unfixed$")){
				//it's unfixed, so we don't have to crawl around for the URI...it hasn't
				//been turned into a prefix yet.
				xsiTypeString = xsiTypeString.substring(xsiTypeString.indexOf("$") + 1);
				
				int colonIndex = xsiTypeString.lastIndexOf(":");
				if(colonIndex != -1){
					String internalNSURI = xsiTypeString.substring(0, colonIndex);
					String internalTypeName = xsiTypeString.substring(colonIndex + 1);
					return new QName(internalNSURI, internalTypeName);
				}
				return null;
			}
			
			//Okay, the value has a prefix and a name.  The name is easy enough to
			//divine, but we need to figure out what the URI is that maps to
			//that prefix
			
			int colonIndex = xsiTypeString.indexOf(":");
			if(colonIndex == -1){
				//Hmph.  Must be unqualified.
				return new QName(null, xsiTypeString);
			}
			
			String internalPrefix = xsiTypeString.substring(0, colonIndex);
			String internalTypeName = xsiTypeString.substring(colonIndex + 1);
			
			Element curElt = elt;
			while(curElt != null){
				String nsURI = getAttributeValue(curElt, XArchConstants.XMLNS_NS_URI, internalPrefix);
				//if((nsURI != null) && (!nsURI.equals(""))){
				if(nsURI != null){
					return new QName(nsURI, internalTypeName);
				}
				Node parent = curElt.getParentNode();
				if(!(parent instanceof Element)){
					break;
				}
				curElt = (Element)parent;
			}
			
			//This may be an unattached element.  The type prefixes don't mean anything unless
			//the element is attached, but we can make a good guess--that the prefix mapping
			//we're looking for is in the document element.
			curElt = elt.getOwnerDocument().getDocumentElement();
			if(curElt != null){
				String nsURI = getAttributeValue(curElt, XArchConstants.XMLNS_NS_URI, internalPrefix);
				//if((nsURI != null) && (!nsURI.equals(""))){
				if(nsURI != null){
					return new QName(nsURI, internalTypeName);
				}
			}			
			return null;
		}
	}
	
	/**
	 * Strips the namespace prefix from a tag name.
	 * @param name Name to strip the prefix from.
	 * @return Name stripped of prefix.
	 */
	public static String stripPrefix(String name){
		int colonIndex = name.indexOf(":");
		if(colonIndex == -1){
			return name;
		}
		return name.substring(colonIndex+1);
	}
	
	/**
	 * Gets all the children of a given DOM node with a specific namespace URI/name 
	 * combination.
	 * @param parentNode DOM node to check for children.
	 * @param namespaceURI Namespace URI to look for in the children.
	 * @param childName Name to look for in the children.
	 * @return <code>NodeList</code> containing all children with the given
	 * namespace URI and child name.
	 */
	public static NodeList getChildren(Node parentNode, String namespaceURI, String childName){
		synchronized(getDOMLock(parentNode)){
			SimpleNodeList snl = new SimpleNodeList();
			NodeList nl = parentNode.getChildNodes();
			int size = nl.getLength();
			for(int i = 0; i < size; i++){
				Node n = nl.item(i);
				if(stripPrefix(n.getNodeName()).equals(childName)){
					if(n.getNamespaceURI().equals(namespaceURI)){
						snl.addNode(n);
					}
				}
			}
			return snl;
		}
	}

	/**
	 * Removes all the children of a given DOM node with a specific namespace URI/name 
	 * combination.
	 * @param parentNode DOM node to check for children.
	 * @param namespaceURI Namespace URI to look for in the children.
	 * @param childName Name to look for in the children.
	 */
	public static void removeChildren(Node parentNode, String namespaceURI, String childName){
		synchronized(getDOMLock(parentNode)){
			NodeList nl = getChildren(parentNode, namespaceURI, childName);
			int size = nl.getLength();
			for(int i = (size - 1); i >= 0; i--){
				parentNode.removeChild(nl.item(i));
			}
		}
	}
	
	/**
	 * Given a node, a namespace URI and attribute name for an attribute,
	 * gets that attribute value, or <code>null</code> if the attribute
	 * does not exist.
	 * @param node Node to check for attribute
	 * @param namespaceURI Namespace URI of attribute to check for
	 * @param attrName Name of attribute to check for
	 * @return Attribute value, or <code>null</code> if attribute
	 * does not exist.
	 */
	public static String getAttributeValue(Node node, String namespaceURI, String attrName){
		synchronized(getDOMLock(node)){
			if(node.getNodeType() != Node.ELEMENT_NODE){
				throw new IllegalArgumentException("That type of node does not have attributes.");
			}
			
			Element elt = (Element)node;
			//Xerces 1.2.1 and previous returned null when there was no attribute value.
			//Xerces 1.2.3 returns "", which is pretty weak.  We'll simulate the 1.2.1 behavior
			//here, because about 12,000 lines of code depend on it working that way.  
			//Good thing we wrapped this function.
			if(elt.hasAttributeNS(namespaceURI, attrName)){
				return elt.getAttributeNS(namespaceURI, attrName);
			}
			else{
				return null;
			}
		}
	}

	/**
	 * Given a node, a namespace URI and attribute name for an attribute,
	 * remoevs that attribute.
	 * @param node Node to check for attribute
	 * @param namespaceURI Namespace URI of attribute to remove
	 * @param attrName Name of attribute to remove
	 */
	public static void removeAttribute(Node node, String namespaceURI, String attrName){
		synchronized(getDOMLock(node)){
			if(node.getNodeType() != Node.ELEMENT_NODE){
				throw new IllegalArgumentException("That type of node does not have attributes.");
			}
			
			Element elt = (Element)node;
			elt.removeAttributeNS(namespaceURI, attrName);
		}
	}

	/**
	 * Given a node, a namespace URI and attribute name for an attribute,
	 * creates that attribute.
	 * @param node Node to add attribute to
	 * @param namespaceURI Namespace URI of attribute to add
	 * @param attrName Name of attribute to add
	 */
	public static void setAttribute(Node node, String namespaceURI, String attrName, String attrVal){
		synchronized(getDOMLock(node)){
			if(node.getNodeType() != Node.ELEMENT_NODE){
				throw new IllegalArgumentException("That type of node does not have attributes.");
			}
			
			Element elt = (Element)node;
			elt.setAttributeNS(namespaceURI, attrName, attrVal);
		}
	}
	
	/**
	 * Sets the xsi:type attribute on a node.
	 * @param node Node to set type on
	 * @param typeName Value for xsi:type attribute.
	 */
	public static void setXSIType(Node node, String typeName){
		synchronized(getDOMLock(node)){
			if(node.getNodeType() != Node.ELEMENT_NODE){
				throw new IllegalArgumentException("That type of node does not have attributes.");
			}
			
			Element elt = (Element)node;
			elt.setAttributeNS(XArchConstants.XSI_NS_URI, "type", typeName);
		}
	}
	
	/**
	 * Given an element with children and a sequence order of
	 * tag names, does a stable sort on the children of the
	 * element.  Children with tag names not specified in the
	 * sequence order are shifted to the end, and kept in
	 * their original order.
	 * @param elt Element on which to sort children.
	 * @param order Sequence order defining sort order for children.
	 */
	public static void order(Element elt, SequenceOrder order){
		synchronized(getDOMLock(elt)){
			NodeList nl = elt.getChildNodes();
			Node[] children = new Node[nl.getLength()];
			
			Node[] newChildren = new Node[nl.getLength()];
			
			//Move all of elt's children into the children[] array
			for(int i = 0; i < children.length; i++){
				Node n = elt.getFirstChild();
				children[i] = elt.removeChild(n);
			}
			
			int newChildrenPtr = 0;
			
			for(java.util.Enumeration en = order.elements(); en.hasMoreElements(); ){
				QName orderName = (QName)en.nextElement();
				for(int i = 0; i < children.length; i++){
					if(children[i] != null){
						QName thisName = QName.getNodeQName(children[i]);
						if(orderName.equals(thisName)){
							newChildren[newChildrenPtr++] = children[i];
							//Mark this so we don't pick it up in the final sweep
							children[i] = null;
						}
					}
				}
			}
			//Final sweep: go through all the remaining original children
			//and tack them onto the end of the newChildren[] array,
			//preserving the original order
			for(int i = 0; i < children.length; i++){
				if(children[i] != null){
					newChildren[newChildrenPtr++] = children[i];
				}
			}
			//Now add all the 'new' children back into the element.
			for(int i = 0; i < newChildren.length; i++){
				elt.appendChild(newChildren[i]);
			}
		}
	}
	
	/**
	 * Determines if two objects are equal.  Two <code>null</code>
	 * objects are considered to be equal.  If only one of the
	 * objects is non-<code>null</code>, they are not equal.
	 * If both are non-null, then this returns object1.equals(object2);
	 * @param object1 First object to compare.
	 * @param object2 Second object to compare.
	 * @return <code>true</code> or <code>false</code> according
	 * to the above description.
	 */
	public static boolean objNullEq(Object object1, Object object2){
		if((object1 == null) && (object2 == null)){
			return true;
		}
		if((object1 == null) && (object2 != null)){
			return false;
		}
		if((object1 != null) && (object2 == null)){
			return false;
		}
		return object1.equals(object2);
	}

	/**
	 * Clones a DOM element and gives the clone a new tag name.
	 * @param elt Element to clone.
	 * @param newTagName New tag name for clone.
	 * @return cloned and renamed element.
	 */
	public static Element cloneAndRename(Element elt, String newTagName){
		return cloneAndRename(elt, elt.getNamespaceURI(), newTagName);
	}
	
	/**
	 * Clones a DOM element and gives the clone a new tag name and namespace URI.
	 * @param elt Element to clone.
	 * @param newNamespaceURI New namespace URI for clone.
	 * @param newTagName New tag name for clone.
	 * @return cloned and renamed element.
	 */
	public static Element cloneAndRename(Element elt, String newNamespaceURI, String newTagName){
		return cloneAndRename(elt, elt.getOwnerDocument(), newNamespaceURI, newTagName);
	}

	/**
	 * Clones a DOM element and gives the clone a new tag name and namespace URI.
	 * Also moves the element and all its children into a new document, if necessary.
	 * @param elt Element to clone.
	 * @param newNamespaceURI New namespace URI for clone.
	 * @param newTagName New tag name for clone.
	 * @return cloned and renamed element.
	 */
	public static Element cloneAndRename(Element elt, Document doc, String newNamespaceURI, String newTagName){
		synchronized(getDOMLock(elt)){
			//Document doc = elt.getOwnerDocument();
			
			//String prefix = elt.getPrefix();
			//String name;
			//if(prefix != null){
			//	name = prefix + ":" + elt.getLocalName();
			//}
			//else{
			//	name = elt.getNodeName();
			//}
			
			//Determine whether we have to import (change the parent document of)
			//the nodes.
			boolean importNodes = (elt.getOwnerDocument() != doc);
	
			Element newElt = doc.createElementNS(newNamespaceURI, newTagName);
			
			//Copy the children.  We have to do this via a Vector
			//because if we screw with the child nodes of elt,
			//it will mess up the node list while we're iterating
			//on it.
			java.util.Vector v = new java.util.Vector();
			
			NodeList nl = elt.getChildNodes();
			for(int i = 0; i < nl.getLength(); i++){
				v.addElement(nl.item(i));
			}		
			for(int i = 0; i < v.size(); i++){
				Node n = (Node)v.elementAt(i);
				if(importNodes){
					n = doc.importNode(n, true);
				}
				newElt.appendChild(n);
			}
			
			//Copy the attributes
			NamedNodeMap nnm = elt.getAttributes();
			if(nnm != null){
				for(int i = 0; i < nnm.getLength(); i++){
					Node n = nnm.item(i);
					if(importNodes || WRONG_DOCUMENT_ERR_WORKAROUND){
						n = doc.importNode(n, true);
					}
					Attr attr = (Attr)(n.cloneNode(true));
					if(!newElt.hasAttributeNS(attr.getNamespaceURI(), attr.getLocalName())){
						newElt.setAttributeNodeNS(attr);
					}
				}
			}
			return newElt;
		}
	}
	
	public static String normalizeString(String s){
		if(s == null){
			return null;
		}

		StringBuffer sb = new StringBuffer();
		int len = s.length();
		boolean lastWasWhitespace = false;
		for(int i = 0; i < len; i++){
			char ch = s.charAt(i);
			if(!Character.isWhitespace(ch)){
				sb.append(ch);
				lastWasWhitespace = false;
			}
			else{
				if(!lastWasWhitespace){
					lastWasWhitespace = true;
					sb.append(" ");
				}
			}
		}
		return sb.toString();
	}
}
