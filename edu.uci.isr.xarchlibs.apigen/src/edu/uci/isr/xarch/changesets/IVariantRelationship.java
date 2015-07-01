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
 * VariantRelationship <code>xsi:type</code> in the
 * changesets namespace.  Extends and
 * inherits the properties of the
 * Relationship <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IVariantRelationship extends IRelationship, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"changesets", "VariantRelationship", IRelationship.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("atLeast", "http://www.w3.org/2001/XMLSchema", "int", null, null),
			XArchPropertyMetadata.createAttribute("atMost", "http://www.w3.org/2001/XMLSchema", "int", null, null),
			XArchPropertyMetadata.createElement("variantChangeSet", "instance", "XMLLink", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Set the atLeast attribute on this VariantRelationship.
	 * @param atLeast atLeast
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setAtLeast(String atLeast);

	/**
	 * Remove the atLeast attribute from this VariantRelationship.
	 */
	public void clearAtLeast();

	/**
	 * Get the atLeast attribute from this VariantRelationship.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return atLeast on this VariantRelationship
	 */
	public String getAtLeast();

	/**
	 * Determine if the atLeast attribute on this VariantRelationship
	 * has the given value.
	 * @param atLeast Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasAtLeast(String atLeast);


	/**
	 * Set the atMost attribute on this VariantRelationship.
	 * @param atMost atMost
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setAtMost(String atMost);

	/**
	 * Remove the atMost attribute from this VariantRelationship.
	 */
	public void clearAtMost();

	/**
	 * Get the atMost attribute from this VariantRelationship.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return atMost on this VariantRelationship
	 */
	public String getAtMost();

	/**
	 * Determine if the atMost attribute on this VariantRelationship
	 * has the given value.
	 * @param atMost Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasAtMost(String atMost);


	/**
	 * Add a variantChangeSet to this VariantRelationship.
	 * @param newVariantChangeSet variantChangeSet to add.
	 */
	public void addVariantChangeSet(edu.uci.isr.xarch.instance.IXMLLink newVariantChangeSet);

	/**
	 * Add a collection of variantChangeSets to this VariantRelationship.
	 * @param variantChangeSets variantChangeSets to add.
	 */
	public void addVariantChangeSets(Collection variantChangeSets);

	/**
	 * Remove all variantChangeSets from this VariantRelationship.
	 */
	public void clearVariantChangeSets();

	/**
	 * Remove the given variantChangeSet from this VariantRelationship.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param variantChangeSetToRemove variantChangeSet to remove.
	 */
	public void removeVariantChangeSet(edu.uci.isr.xarch.instance.IXMLLink variantChangeSetToRemove);

	/**
	 * Remove all the given variantChangeSets from this VariantRelationship.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param variantChangeSets variantChangeSet to remove.
	 */
	public void removeVariantChangeSets(Collection variantChangeSets);

	/**
	 * Get all the variantChangeSets from this VariantRelationship.
	 * @return all variantChangeSets in this VariantRelationship.
	 */
	public Collection getAllVariantChangeSets();

	/**
	 * Determine if this VariantRelationship contains a given variantChangeSet.
	 * @return <code>true</code> if this VariantRelationship contains the given
	 * variantChangeSetToCheck, <code>false</code> otherwise.
	 */
	public boolean hasVariantChangeSet(edu.uci.isr.xarch.instance.IXMLLink variantChangeSetToCheck);

	/**
	 * Determine if this VariantRelationship contains the given set of variantChangeSets.
	 * @param variantChangeSetsToCheck variantChangeSets to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>variantChangeSets</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasVariantChangeSets(Collection variantChangeSetsToCheck);

	/**
	 * Determine if this VariantRelationship contains each element in the 
	 * given set of variantChangeSets.
	 * @param variantChangeSetsToCheck variantChangeSets to check for.
	 * @return <code>true</code> if every element in
	 * <code>variantChangeSets</code> is found in this VariantRelationship,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllVariantChangeSets(Collection variantChangeSetsToCheck);

	/**
	 * Determine if another VariantRelationship is equivalent to this one, ignoring
	 * ID's.
	 * @param VariantRelationshipToCheck VariantRelationship to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * VariantRelationship are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IVariantRelationship VariantRelationshipToCheck);

}
