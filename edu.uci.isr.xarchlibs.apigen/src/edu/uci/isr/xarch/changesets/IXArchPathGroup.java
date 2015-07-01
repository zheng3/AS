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
 * XArchPathGroup <code>xsi:type</code> in the
 * changesets namespace.  Extends and
 * inherits the properties of the
 * Group <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IXArchPathGroup extends IGroup, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"changesets", "XArchPathGroup", IGroup.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("xArchPath", "http://www.w3.org/2001/XMLSchema", "string", null, null)},
		new XArchActionMetadata[]{});

	/**
	 * Set the xArchPath attribute on this XArchPathGroup.
	 * @param xArchPath xArchPath
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setXArchPath(String xArchPath);

	/**
	 * Remove the xArchPath attribute from this XArchPathGroup.
	 */
	public void clearXArchPath();

	/**
	 * Get the xArchPath attribute from this XArchPathGroup.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return xArchPath on this XArchPathGroup
	 */
	public String getXArchPath();

	/**
	 * Determine if the xArchPath attribute on this XArchPathGroup
	 * has the given value.
	 * @param xArchPath Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasXArchPath(String xArchPath);

	/**
	 * Determine if another XArchPathGroup is equivalent to this one, ignoring
	 * ID's.
	 * @param XArchPathGroupToCheck XArchPathGroup to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * XArchPathGroup are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IXArchPathGroup XArchPathGroupToCheck);

}
