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
 * AttributeSegment <code>xsi:type</code> in the
 * changesets namespace.  Extends and
 * inherits the properties of the
 * ChangeSegment <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IAttributeSegment extends IChangeSegment, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"changesets", "AttributeSegment", IChangeSegment.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("value", "http://www.w3.org/2001/XMLSchema", "string", null, null)},
		new XArchActionMetadata[]{});

	/**
	 * Set the value attribute on this AttributeSegment.
	 * @param value value
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setValue(String value);

	/**
	 * Remove the value attribute from this AttributeSegment.
	 */
	public void clearValue();

	/**
	 * Get the value attribute from this AttributeSegment.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return value on this AttributeSegment
	 */
	public String getValue();

	/**
	 * Determine if the value attribute on this AttributeSegment
	 * has the given value.
	 * @param value Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasValue(String value);

	/**
	 * Determine if another AttributeSegment is equivalent to this one, ignoring
	 * ID's.
	 * @param AttributeSegmentToCheck AttributeSegment to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * AttributeSegment are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IAttributeSegment AttributeSegmentToCheck);

}
