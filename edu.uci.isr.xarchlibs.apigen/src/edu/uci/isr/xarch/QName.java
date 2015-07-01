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
 * Qualified name (NamespaceURI + Local Name) for use in describing
 * a DOM element.
 * 
 * @author Eric M. Dashofy (edashofy@ics.uci.edu)
 */
public class QName{

	/** Namespace URI of this QName */
	protected String namespaceURI;
	/** Local name of this QName */
	protected String name;
	
	/**
	 * Create a qualified name with no namespace URI and the given local name.
	 * @param name Local name.
	 */
	public QName(String name){
		this(null, name);
	}
	
	/**
	 * Create a qualified name with the given namespace URI local name.
	 * @param namespaceURI Namespace URI.
	 * @param name Local name.
	 */
	public QName(String namespaceURI, String name){
		this.namespaceURI = namespaceURI;
		this.name = name;
	}

	/**
	 * Get the namespace URI of this qualified name.
	 * @return Namespace URI of this QName.
	 */
	public String getNamespaceURI(){
		return namespaceURI;
	}
	
	/**
	 * Get the local name of this qualified name.
	 * @return local name of this QName.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Determines if two QNames are equal.
	 * @param o Object to compare with this QName
	 * @return <code>true</code> if they are equal, <code>false</code> otherwise.
	 */
	public boolean equals(Object o){
		if(!(o instanceof QName)){
			return false;
		}
		QName q = (QName)o;
		
		String thisNamespaceURI = getNamespaceURI();
		String thatNamespaceURI = q.getNamespaceURI();
		
		if(!DOMUtils.objNullEq(thisNamespaceURI, thatNamespaceURI)){
			return false;
		}
		
		String thisName = getName();
		String thatName = q.getName();
		if(!DOMUtils.objNullEq(thisName, thatName)){
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the hash code for this QName.
	 * @return hash code.
	 */
	public int hashCode(){
		return namespaceURI.hashCode() ^ name.hashCode();
	}

	/**
	 * Gets the qualified name of a given DOM node.
	 * @param n DOM node to inspect.
	 * @return Qualified name of the given node.
	 */
	public static QName getNodeQName(Node n){
		return new QName(n.getNamespaceURI(), n.getLocalName());
	}
	
	public String toString(){
		return namespaceURI + ":" + name;
	}
}
