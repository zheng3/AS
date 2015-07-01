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

import java.util.Collection;

/**
 * Interface for accessing objects of the
 * xArch <code>xsi:type</code> in the
 * instance namespace.
 * 
 * @author Andr&eacute; van der Hoek (andre@ics.uci.edu)
 * @author Eric M. Dashofy (edashofy@ics.uci.edu)
 */
public interface IXArch extends IXArchElement
{
	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
			XArchTypeMetadata.XARCH_ELEMENT,
			null, "XArch", IXArchElement.TYPE_METADATA,
			new XArchPropertyMetadata[]{
				XArchPropertyMetadata.createElement("object", null, "XArchElement", 0, XArchPropertyMetadata.UNBOUNDED)},
			new XArchActionMetadata[]{});
			
	/**
	 * Add an object to this xArch object.
	 * @param object Object to add.
	 */
	public void addObject(Object object);

	/**
	 * Add a set of objects to this xArch object.
	 * @param objects Objects to add.
	 */
	public void addObjects(Collection objects);

	/**
	 * Remove all objects from this xArch object.
	 */
	public void clearObjects();

	/**
	 * Remove a given object from this xArch object.
	 * @param object Object to remove.
	 */
	public void removeObject(Object object);

	/**
	 * Remove a set of objects from this xArch object.
	 * @param objects Objects to remove.
	 */
	public void removeObjects(Collection objects);

	/**
	 * Get all the objects from this xArch object.
	 * @return All objects in this xArch object.
	 */
	public Collection getAllObjects();

	/**
	 * Determine if this xArch object contains a given
	 * object.
	 * @param object Object to check for.
	 * @return <code>true</code> if the object was found,
	 * <code>false</code> otherwise.
	 */
	public boolean hasObject(Object object);

	/**
	 * Determine if this xArch object contains a given
	 * set of objects.
	 * @param objects Objects to check for.
	 * @return Collection of <code>java.lang.Boolean</code>
	 * that are <code>true</code> if the object was found,
	 * <code>false</code> otherwise.
	 */
	public Collection hasObjects(Collection objects);

	/**
	 * Determine if this xArch object contains a given
	 * set of objects.
	 * @param objects Objects to check for.
	 * @return <code>true</code> if all the objects were found,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllObjects(Collection objects);

	/**
	 * Determine if this xArch object is equivalent to another
	 * one.
	 * @param xArch Other xArch object to compare to this one.
	 * @return <code>true</code> if both contain the same
	 * objects, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IXArch xArch);

	/**
	 * Adds an object that will listen to <code>XArchEvent</code>s
	 * to this xArch object.
	 * @param l Listener to add.
	 */
	public void addXArchListener(XArchListener l);

	/**
	 * Removes an object that will listen to <code>XArchEvent</code>s
	 * from this xArch object.
	 * @param l Listener to add.
	 */
	public void removeXArchListener(XArchListener l);

	/**
	 * Broadcasts a given event to all the XArchListeners on this
	 * xArch object.
	 * @param evt Event to broadcast
	 */
	public void fireXArchEvent(XArchEvent evt);

	/**
	 * Add a schemaLocation mapping to the xArch element.
	 * @param uri Schema URI to add.
	 * @param location Location of the schema.  May be local or a URL.
	 */
	public void addSchemaLocation(String uri, String location);

	/**
	 * Removes a schemaLocation mapping from the xArch element.
	 * @param uri Schema URI to remove.
	 */
	public void removeSchemaLocation(String uri);

	public void cacheWrapper(Node domNode, IXArchElement wrapper);

	public IXArchElement getWrapper(Node domNode);
}

