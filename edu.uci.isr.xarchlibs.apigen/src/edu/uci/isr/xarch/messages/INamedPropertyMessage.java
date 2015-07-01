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
package edu.uci.isr.xarch.messages;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * NamedPropertyMessage <code>xsi:type</code> in the
 * messages namespace.  Extends and
 * inherits the properties of the
 * GenericMessage <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface INamedPropertyMessage extends IGenericMessage, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"messages", "NamedPropertyMessage", IGenericMessage.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("name", "messages", "MessageName", 1, 1),
			XArchPropertyMetadata.createElement("namedProperty", "messages", "NamedProperty", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Set the name for this NamedPropertyMessage.
	 * @param value new name
	 */
	public void setName(IMessageName value);

	/**
	 * Clear the name from this NamedPropertyMessage.
	 */
	public void clearName();

	/**
	 * Get the name from this NamedPropertyMessage.
	 * @return name
	 */
	public IMessageName getName();

	/**
	 * Determine if this NamedPropertyMessage has the given name
	 * @param nameToCheck name to compare
	 * @return <code>true</code> if the names are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasName(IMessageName nameToCheck);

	/**
	 * Add a namedProperty to this NamedPropertyMessage.
	 * @param newNamedProperty namedProperty to add.
	 */
	public void addNamedProperty(INamedProperty newNamedProperty);

	/**
	 * Add a collection of namedPropertys to this NamedPropertyMessage.
	 * @param namedPropertys namedPropertys to add.
	 */
	public void addNamedPropertys(Collection namedPropertys);

	/**
	 * Remove all namedPropertys from this NamedPropertyMessage.
	 */
	public void clearNamedPropertys();

	/**
	 * Remove the given namedProperty from this NamedPropertyMessage.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param namedPropertyToRemove namedProperty to remove.
	 */
	public void removeNamedProperty(INamedProperty namedPropertyToRemove);

	/**
	 * Remove all the given namedPropertys from this NamedPropertyMessage.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param namedPropertys namedProperty to remove.
	 */
	public void removeNamedPropertys(Collection namedPropertys);

	/**
	 * Get all the namedPropertys from this NamedPropertyMessage.
	 * @return all namedPropertys in this NamedPropertyMessage.
	 */
	public Collection getAllNamedPropertys();

	/**
	 * Determine if this NamedPropertyMessage contains a given namedProperty.
	 * @return <code>true</code> if this NamedPropertyMessage contains the given
	 * namedPropertyToCheck, <code>false</code> otherwise.
	 */
	public boolean hasNamedProperty(INamedProperty namedPropertyToCheck);

	/**
	 * Determine if this NamedPropertyMessage contains the given set of namedPropertys.
	 * @param namedPropertysToCheck namedPropertys to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>namedPropertys</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasNamedPropertys(Collection namedPropertysToCheck);

	/**
	 * Determine if this NamedPropertyMessage contains each element in the 
	 * given set of namedPropertys.
	 * @param namedPropertysToCheck namedPropertys to check for.
	 * @return <code>true</code> if every element in
	 * <code>namedPropertys</code> is found in this NamedPropertyMessage,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllNamedPropertys(Collection namedPropertysToCheck);

	/**
	 * Determine if another NamedPropertyMessage is equivalent to this one, ignoring
	 * ID's.
	 * @param NamedPropertyMessageToCheck NamedPropertyMessage to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * NamedPropertyMessage are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(INamedPropertyMessage NamedPropertyMessageToCheck);

}
