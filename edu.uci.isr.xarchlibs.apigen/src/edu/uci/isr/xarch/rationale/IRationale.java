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
package edu.uci.isr.xarch.rationale;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * Rationale <code>xsi:type</code> in the
 * rationale namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface IRationale extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"rationale", "Rationale", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("id", "instance", "Identifier", null, null),
			XArchPropertyMetadata.createElement("description", "instance", "Description", 1, 1),
			XArchPropertyMetadata.createElement("item", "instance", "XMLLink", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Set the id attribute on this Rationale.
	 * @param id id
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setId(String id);

	/**
	 * Remove the id attribute from this Rationale.
	 */
	public void clearId();

	/**
	 * Get the id attribute from this Rationale.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return id on this Rationale
	 */
	public String getId();

	/**
	 * Determine if the id attribute on this Rationale
	 * has the given value.
	 * @param id Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasId(String id);


	/**
	 * Set the description for this Rationale.
	 * @param value new description
	 */
	public void setDescription(edu.uci.isr.xarch.instance.IDescription value);

	/**
	 * Clear the description from this Rationale.
	 */
	public void clearDescription();

	/**
	 * Get the description from this Rationale.
	 * @return description
	 */
	public edu.uci.isr.xarch.instance.IDescription getDescription();

	/**
	 * Determine if this Rationale has the given description
	 * @param descriptionToCheck description to compare
	 * @return <code>true</code> if the descriptions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasDescription(edu.uci.isr.xarch.instance.IDescription descriptionToCheck);

	/**
	 * Add a item to this Rationale.
	 * @param newItem item to add.
	 */
	public void addItem(edu.uci.isr.xarch.instance.IXMLLink newItem);

	/**
	 * Add a collection of items to this Rationale.
	 * @param items items to add.
	 */
	public void addItems(Collection items);

	/**
	 * Remove all items from this Rationale.
	 */
	public void clearItems();

	/**
	 * Remove the given item from this Rationale.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param itemToRemove item to remove.
	 */
	public void removeItem(edu.uci.isr.xarch.instance.IXMLLink itemToRemove);

	/**
	 * Remove all the given items from this Rationale.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param items item to remove.
	 */
	public void removeItems(Collection items);

	/**
	 * Get all the items from this Rationale.
	 * @return all items in this Rationale.
	 */
	public Collection getAllItems();

	/**
	 * Determine if this Rationale contains a given item.
	 * @return <code>true</code> if this Rationale contains the given
	 * itemToCheck, <code>false</code> otherwise.
	 */
	public boolean hasItem(edu.uci.isr.xarch.instance.IXMLLink itemToCheck);

	/**
	 * Determine if this Rationale contains the given set of items.
	 * @param itemsToCheck items to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>items</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasItems(Collection itemsToCheck);

	/**
	 * Determine if this Rationale contains each element in the 
	 * given set of items.
	 * @param itemsToCheck items to check for.
	 * @return <code>true</code> if every element in
	 * <code>items</code> is found in this Rationale,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllItems(Collection itemsToCheck);

	/**
	 * Determine if another Rationale has the same
	 * id as this one.
	 * @param RationaleToCheck Rationale to compare with this
	 * one.
	 */
	public boolean isEqual(IRationale RationaleToCheck);
	/**
	 * Determine if another Rationale is equivalent to this one, ignoring
	 * ID's.
	 * @param RationaleToCheck Rationale to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * Rationale are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IRationale RationaleToCheck);

}
