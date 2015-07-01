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
package edu.uci.isr.xarch.hints3;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * HintBundle <code>xsi:type</code> in the
 * hints3 namespace.  Extends and
 * inherits the properties of the
 * HintedElement <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IHintBundle extends IHintedElement, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"hints3", "HintBundle", IHintedElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("maintainer", "http://www.w3.org/2001/XMLSchema", "string", null, null),
			XArchPropertyMetadata.createAttribute("version", "http://www.w3.org/2001/XMLSchema", "string", null, null)},
		new XArchActionMetadata[]{});

	/**
	 * Set the maintainer attribute on this HintBundle.
	 * @param maintainer maintainer
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setMaintainer(String maintainer);

	/**
	 * Remove the maintainer attribute from this HintBundle.
	 */
	public void clearMaintainer();

	/**
	 * Get the maintainer attribute from this HintBundle.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return maintainer on this HintBundle
	 */
	public String getMaintainer();

	/**
	 * Determine if the maintainer attribute on this HintBundle
	 * has the given value.
	 * @param maintainer Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasMaintainer(String maintainer);


	/**
	 * Set the version attribute on this HintBundle.
	 * @param version version
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setVersion(String version);

	/**
	 * Remove the version attribute from this HintBundle.
	 */
	public void clearVersion();

	/**
	 * Get the version attribute from this HintBundle.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return version on this HintBundle
	 */
	public String getVersion();

	/**
	 * Determine if the version attribute on this HintBundle
	 * has the given value.
	 * @param version Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasVersion(String version);

	/**
	 * Determine if another HintBundle is equivalent to this one, ignoring
	 * ID's.
	 * @param HintBundleToCheck HintBundle to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * HintBundle are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IHintBundle HintBundleToCheck);

}
