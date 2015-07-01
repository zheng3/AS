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
package edu.uci.isr.xarch.activitydiagrams;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * ActivityEdge <code>xsi:type</code> in the
 * activitydiagrams namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface IActivityEdge extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"activitydiagrams", "ActivityEdge", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("id", "instance", "Identifier", null, null),
			XArchPropertyMetadata.createElement("description", "instance", "Description", 1, 1),
			XArchPropertyMetadata.createElement("source", "instance", "XMLLink", 1, 1),
			XArchPropertyMetadata.createElement("target", "instance", "XMLLink", 1, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the id attribute on this ActivityEdge.
	 * @param id id
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setId(String id);

	/**
	 * Remove the id attribute from this ActivityEdge.
	 */
	public void clearId();

	/**
	 * Get the id attribute from this ActivityEdge.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return id on this ActivityEdge
	 */
	public String getId();

	/**
	 * Determine if the id attribute on this ActivityEdge
	 * has the given value.
	 * @param id Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasId(String id);


	/**
	 * Set the description for this ActivityEdge.
	 * @param value new description
	 */
	public void setDescription(edu.uci.isr.xarch.instance.IDescription value);

	/**
	 * Clear the description from this ActivityEdge.
	 */
	public void clearDescription();

	/**
	 * Get the description from this ActivityEdge.
	 * @return description
	 */
	public edu.uci.isr.xarch.instance.IDescription getDescription();

	/**
	 * Determine if this ActivityEdge has the given description
	 * @param descriptionToCheck description to compare
	 * @return <code>true</code> if the descriptions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasDescription(edu.uci.isr.xarch.instance.IDescription descriptionToCheck);

	/**
	 * Set the source for this ActivityEdge.
	 * @param value new source
	 */
	public void setSource(edu.uci.isr.xarch.instance.IXMLLink value);

	/**
	 * Clear the source from this ActivityEdge.
	 */
	public void clearSource();

	/**
	 * Get the source from this ActivityEdge.
	 * @return source
	 */
	public edu.uci.isr.xarch.instance.IXMLLink getSource();

	/**
	 * Determine if this ActivityEdge has the given source
	 * @param sourceToCheck source to compare
	 * @return <code>true</code> if the sources are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasSource(edu.uci.isr.xarch.instance.IXMLLink sourceToCheck);

	/**
	 * Set the target for this ActivityEdge.
	 * @param value new target
	 */
	public void setTarget(edu.uci.isr.xarch.instance.IXMLLink value);

	/**
	 * Clear the target from this ActivityEdge.
	 */
	public void clearTarget();

	/**
	 * Get the target from this ActivityEdge.
	 * @return target
	 */
	public edu.uci.isr.xarch.instance.IXMLLink getTarget();

	/**
	 * Determine if this ActivityEdge has the given target
	 * @param targetToCheck target to compare
	 * @return <code>true</code> if the targets are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasTarget(edu.uci.isr.xarch.instance.IXMLLink targetToCheck);
	/**
	 * Determine if another ActivityEdge has the same
	 * id as this one.
	 * @param ActivityEdgeToCheck ActivityEdge to compare with this
	 * one.
	 */
	public boolean isEqual(IActivityEdge ActivityEdgeToCheck);
	/**
	 * Determine if another ActivityEdge is equivalent to this one, ignoring
	 * ID's.
	 * @param ActivityEdgeToCheck ActivityEdge to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * ActivityEdge are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IActivityEdge ActivityEdgeToCheck);

}
