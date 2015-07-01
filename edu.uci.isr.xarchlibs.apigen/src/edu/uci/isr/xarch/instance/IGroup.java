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
package edu.uci.isr.xarch.instance;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * Group <code>xsi:type</code> in the
 * instance namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface IGroup extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"instance", "Group", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("id", "instance", "Identifier", null, null),
			XArchPropertyMetadata.createElement("description", "instance", "Description", 1, 1),
			XArchPropertyMetadata.createElement("member", "instance", "XMLLink", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Set the id attribute on this Group.
	 * @param id id
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setId(String id);

	/**
	 * Remove the id attribute from this Group.
	 */
	public void clearId();

	/**
	 * Get the id attribute from this Group.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return id on this Group
	 */
	public String getId();

	/**
	 * Determine if the id attribute on this Group
	 * has the given value.
	 * @param id Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasId(String id);


	/**
	 * Set the description for this Group.
	 * @param value new description
	 */
	public void setDescription(IDescription value);

	/**
	 * Clear the description from this Group.
	 */
	public void clearDescription();

	/**
	 * Get the description from this Group.
	 * @return description
	 */
	public IDescription getDescription();

	/**
	 * Determine if this Group has the given description
	 * @param descriptionToCheck description to compare
	 * @return <code>true</code> if the descriptions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasDescription(IDescription descriptionToCheck);

	/**
	 * Add a member to this Group.
	 * @param newMember member to add.
	 */
	public void addMember(IXMLLink newMember);

	/**
	 * Add a collection of members to this Group.
	 * @param members members to add.
	 */
	public void addMembers(Collection members);

	/**
	 * Remove all members from this Group.
	 */
	public void clearMembers();

	/**
	 * Remove the given member from this Group.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param memberToRemove member to remove.
	 */
	public void removeMember(IXMLLink memberToRemove);

	/**
	 * Remove all the given members from this Group.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param members member to remove.
	 */
	public void removeMembers(Collection members);

	/**
	 * Get all the members from this Group.
	 * @return all members in this Group.
	 */
	public Collection getAllMembers();

	/**
	 * Determine if this Group contains a given member.
	 * @return <code>true</code> if this Group contains the given
	 * memberToCheck, <code>false</code> otherwise.
	 */
	public boolean hasMember(IXMLLink memberToCheck);

	/**
	 * Determine if this Group contains the given set of members.
	 * @param membersToCheck members to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>members</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasMembers(Collection membersToCheck);

	/**
	 * Determine if this Group contains each element in the 
	 * given set of members.
	 * @param membersToCheck members to check for.
	 * @return <code>true</code> if every element in
	 * <code>members</code> is found in this Group,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllMembers(Collection membersToCheck);

	/**
	 * Determine if another Group has the same
	 * id as this one.
	 * @param GroupToCheck Group to compare with this
	 * one.
	 */
	public boolean isEqual(IGroup GroupToCheck);
	/**
	 * Determine if another Group is equivalent to this one, ignoring
	 * ID's.
	 * @param GroupToCheck Group to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * Group are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IGroup GroupToCheck);

}
