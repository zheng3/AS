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
package edu.uci.isr.xarch.changesets;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * ManualGroup <code>xsi:type</code> in the
 * changesets namespace.  Extends and
 * inherits the properties of the
 * Group <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IManualGroup extends IGroup, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"changesets", "ManualGroup", IGroup.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("item", "instance", "XMLLink", 1, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the item for this ManualGroup.
	 * @param value new item
	 */
	public void setItem(edu.uci.isr.xarch.instance.IXMLLink value);

	/**
	 * Clear the item from this ManualGroup.
	 */
	public void clearItem();

	/**
	 * Get the item from this ManualGroup.
	 * @return item
	 */
	public edu.uci.isr.xarch.instance.IXMLLink getItem();

	/**
	 * Determine if this ManualGroup has the given item
	 * @param itemToCheck item to compare
	 * @return <code>true</code> if the items are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasItem(edu.uci.isr.xarch.instance.IXMLLink itemToCheck);
	/**
	 * Determine if another ManualGroup is equivalent to this one, ignoring
	 * ID's.
	 * @param ManualGroupToCheck ManualGroup to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * ManualGroup are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IManualGroup ManualGroupToCheck);

}
