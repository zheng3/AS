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
 * AndRelationship <code>xsi:type</code> in the
 * changesets namespace.  Extends and
 * inherits the properties of the
 * Relationship <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IAndRelationship extends IRelationship, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"changesets", "AndRelationship", IRelationship.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("andChangeSet", "instance", "XMLLink", 0, XArchPropertyMetadata.UNBOUNDED),
			XArchPropertyMetadata.createElement("andNotChangeSet", "instance", "XMLLink", 0, XArchPropertyMetadata.UNBOUNDED),
			XArchPropertyMetadata.createElement("impliesChangeSet", "instance", "XMLLink", 0, XArchPropertyMetadata.UNBOUNDED),
			XArchPropertyMetadata.createElement("impliesNotChangeSet", "instance", "XMLLink", 0, XArchPropertyMetadata.UNBOUNDED),
			XArchPropertyMetadata.createElement("orChangeSet", "instance", "XMLLink", 0, XArchPropertyMetadata.UNBOUNDED),
			XArchPropertyMetadata.createElement("orNotChangeSet", "instance", "XMLLink", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Add a andChangeSet to this AndRelationship.
	 * @param newAndChangeSet andChangeSet to add.
	 */
	public void addAndChangeSet(edu.uci.isr.xarch.instance.IXMLLink newAndChangeSet);

	/**
	 * Add a collection of andChangeSets to this AndRelationship.
	 * @param andChangeSets andChangeSets to add.
	 */
	public void addAndChangeSets(Collection andChangeSets);

	/**
	 * Remove all andChangeSets from this AndRelationship.
	 */
	public void clearAndChangeSets();

	/**
	 * Remove the given andChangeSet from this AndRelationship.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param andChangeSetToRemove andChangeSet to remove.
	 */
	public void removeAndChangeSet(edu.uci.isr.xarch.instance.IXMLLink andChangeSetToRemove);

	/**
	 * Remove all the given andChangeSets from this AndRelationship.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param andChangeSets andChangeSet to remove.
	 */
	public void removeAndChangeSets(Collection andChangeSets);

	/**
	 * Get all the andChangeSets from this AndRelationship.
	 * @return all andChangeSets in this AndRelationship.
	 */
	public Collection getAllAndChangeSets();

	/**
	 * Determine if this AndRelationship contains a given andChangeSet.
	 * @return <code>true</code> if this AndRelationship contains the given
	 * andChangeSetToCheck, <code>false</code> otherwise.
	 */
	public boolean hasAndChangeSet(edu.uci.isr.xarch.instance.IXMLLink andChangeSetToCheck);

	/**
	 * Determine if this AndRelationship contains the given set of andChangeSets.
	 * @param andChangeSetsToCheck andChangeSets to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>andChangeSets</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasAndChangeSets(Collection andChangeSetsToCheck);

	/**
	 * Determine if this AndRelationship contains each element in the 
	 * given set of andChangeSets.
	 * @param andChangeSetsToCheck andChangeSets to check for.
	 * @return <code>true</code> if every element in
	 * <code>andChangeSets</code> is found in this AndRelationship,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllAndChangeSets(Collection andChangeSetsToCheck);


	/**
	 * Add a andNotChangeSet to this AndRelationship.
	 * @param newAndNotChangeSet andNotChangeSet to add.
	 */
	public void addAndNotChangeSet(edu.uci.isr.xarch.instance.IXMLLink newAndNotChangeSet);

	/**
	 * Add a collection of andNotChangeSets to this AndRelationship.
	 * @param andNotChangeSets andNotChangeSets to add.
	 */
	public void addAndNotChangeSets(Collection andNotChangeSets);

	/**
	 * Remove all andNotChangeSets from this AndRelationship.
	 */
	public void clearAndNotChangeSets();

	/**
	 * Remove the given andNotChangeSet from this AndRelationship.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param andNotChangeSetToRemove andNotChangeSet to remove.
	 */
	public void removeAndNotChangeSet(edu.uci.isr.xarch.instance.IXMLLink andNotChangeSetToRemove);

	/**
	 * Remove all the given andNotChangeSets from this AndRelationship.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param andNotChangeSets andNotChangeSet to remove.
	 */
	public void removeAndNotChangeSets(Collection andNotChangeSets);

	/**
	 * Get all the andNotChangeSets from this AndRelationship.
	 * @return all andNotChangeSets in this AndRelationship.
	 */
	public Collection getAllAndNotChangeSets();

	/**
	 * Determine if this AndRelationship contains a given andNotChangeSet.
	 * @return <code>true</code> if this AndRelationship contains the given
	 * andNotChangeSetToCheck, <code>false</code> otherwise.
	 */
	public boolean hasAndNotChangeSet(edu.uci.isr.xarch.instance.IXMLLink andNotChangeSetToCheck);

	/**
	 * Determine if this AndRelationship contains the given set of andNotChangeSets.
	 * @param andNotChangeSetsToCheck andNotChangeSets to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>andNotChangeSets</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasAndNotChangeSets(Collection andNotChangeSetsToCheck);

	/**
	 * Determine if this AndRelationship contains each element in the 
	 * given set of andNotChangeSets.
	 * @param andNotChangeSetsToCheck andNotChangeSets to check for.
	 * @return <code>true</code> if every element in
	 * <code>andNotChangeSets</code> is found in this AndRelationship,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllAndNotChangeSets(Collection andNotChangeSetsToCheck);


	/**
	 * Add a impliesChangeSet to this AndRelationship.
	 * @param newImpliesChangeSet impliesChangeSet to add.
	 */
	public void addImpliesChangeSet(edu.uci.isr.xarch.instance.IXMLLink newImpliesChangeSet);

	/**
	 * Add a collection of impliesChangeSets to this AndRelationship.
	 * @param impliesChangeSets impliesChangeSets to add.
	 */
	public void addImpliesChangeSets(Collection impliesChangeSets);

	/**
	 * Remove all impliesChangeSets from this AndRelationship.
	 */
	public void clearImpliesChangeSets();

	/**
	 * Remove the given impliesChangeSet from this AndRelationship.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param impliesChangeSetToRemove impliesChangeSet to remove.
	 */
	public void removeImpliesChangeSet(edu.uci.isr.xarch.instance.IXMLLink impliesChangeSetToRemove);

	/**
	 * Remove all the given impliesChangeSets from this AndRelationship.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param impliesChangeSets impliesChangeSet to remove.
	 */
	public void removeImpliesChangeSets(Collection impliesChangeSets);

	/**
	 * Get all the impliesChangeSets from this AndRelationship.
	 * @return all impliesChangeSets in this AndRelationship.
	 */
	public Collection getAllImpliesChangeSets();

	/**
	 * Determine if this AndRelationship contains a given impliesChangeSet.
	 * @return <code>true</code> if this AndRelationship contains the given
	 * impliesChangeSetToCheck, <code>false</code> otherwise.
	 */
	public boolean hasImpliesChangeSet(edu.uci.isr.xarch.instance.IXMLLink impliesChangeSetToCheck);

	/**
	 * Determine if this AndRelationship contains the given set of impliesChangeSets.
	 * @param impliesChangeSetsToCheck impliesChangeSets to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>impliesChangeSets</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasImpliesChangeSets(Collection impliesChangeSetsToCheck);

	/**
	 * Determine if this AndRelationship contains each element in the 
	 * given set of impliesChangeSets.
	 * @param impliesChangeSetsToCheck impliesChangeSets to check for.
	 * @return <code>true</code> if every element in
	 * <code>impliesChangeSets</code> is found in this AndRelationship,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllImpliesChangeSets(Collection impliesChangeSetsToCheck);


	/**
	 * Add a impliesNotChangeSet to this AndRelationship.
	 * @param newImpliesNotChangeSet impliesNotChangeSet to add.
	 */
	public void addImpliesNotChangeSet(edu.uci.isr.xarch.instance.IXMLLink newImpliesNotChangeSet);

	/**
	 * Add a collection of impliesNotChangeSets to this AndRelationship.
	 * @param impliesNotChangeSets impliesNotChangeSets to add.
	 */
	public void addImpliesNotChangeSets(Collection impliesNotChangeSets);

	/**
	 * Remove all impliesNotChangeSets from this AndRelationship.
	 */
	public void clearImpliesNotChangeSets();

	/**
	 * Remove the given impliesNotChangeSet from this AndRelationship.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param impliesNotChangeSetToRemove impliesNotChangeSet to remove.
	 */
	public void removeImpliesNotChangeSet(edu.uci.isr.xarch.instance.IXMLLink impliesNotChangeSetToRemove);

	/**
	 * Remove all the given impliesNotChangeSets from this AndRelationship.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param impliesNotChangeSets impliesNotChangeSet to remove.
	 */
	public void removeImpliesNotChangeSets(Collection impliesNotChangeSets);

	/**
	 * Get all the impliesNotChangeSets from this AndRelationship.
	 * @return all impliesNotChangeSets in this AndRelationship.
	 */
	public Collection getAllImpliesNotChangeSets();

	/**
	 * Determine if this AndRelationship contains a given impliesNotChangeSet.
	 * @return <code>true</code> if this AndRelationship contains the given
	 * impliesNotChangeSetToCheck, <code>false</code> otherwise.
	 */
	public boolean hasImpliesNotChangeSet(edu.uci.isr.xarch.instance.IXMLLink impliesNotChangeSetToCheck);

	/**
	 * Determine if this AndRelationship contains the given set of impliesNotChangeSets.
	 * @param impliesNotChangeSetsToCheck impliesNotChangeSets to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>impliesNotChangeSets</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasImpliesNotChangeSets(Collection impliesNotChangeSetsToCheck);

	/**
	 * Determine if this AndRelationship contains each element in the 
	 * given set of impliesNotChangeSets.
	 * @param impliesNotChangeSetsToCheck impliesNotChangeSets to check for.
	 * @return <code>true</code> if every element in
	 * <code>impliesNotChangeSets</code> is found in this AndRelationship,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllImpliesNotChangeSets(Collection impliesNotChangeSetsToCheck);


	/**
	 * Add a orChangeSet to this AndRelationship.
	 * @param newOrChangeSet orChangeSet to add.
	 */
	public void addOrChangeSet(edu.uci.isr.xarch.instance.IXMLLink newOrChangeSet);

	/**
	 * Add a collection of orChangeSets to this AndRelationship.
	 * @param orChangeSets orChangeSets to add.
	 */
	public void addOrChangeSets(Collection orChangeSets);

	/**
	 * Remove all orChangeSets from this AndRelationship.
	 */
	public void clearOrChangeSets();

	/**
	 * Remove the given orChangeSet from this AndRelationship.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param orChangeSetToRemove orChangeSet to remove.
	 */
	public void removeOrChangeSet(edu.uci.isr.xarch.instance.IXMLLink orChangeSetToRemove);

	/**
	 * Remove all the given orChangeSets from this AndRelationship.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param orChangeSets orChangeSet to remove.
	 */
	public void removeOrChangeSets(Collection orChangeSets);

	/**
	 * Get all the orChangeSets from this AndRelationship.
	 * @return all orChangeSets in this AndRelationship.
	 */
	public Collection getAllOrChangeSets();

	/**
	 * Determine if this AndRelationship contains a given orChangeSet.
	 * @return <code>true</code> if this AndRelationship contains the given
	 * orChangeSetToCheck, <code>false</code> otherwise.
	 */
	public boolean hasOrChangeSet(edu.uci.isr.xarch.instance.IXMLLink orChangeSetToCheck);

	/**
	 * Determine if this AndRelationship contains the given set of orChangeSets.
	 * @param orChangeSetsToCheck orChangeSets to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>orChangeSets</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasOrChangeSets(Collection orChangeSetsToCheck);

	/**
	 * Determine if this AndRelationship contains each element in the 
	 * given set of orChangeSets.
	 * @param orChangeSetsToCheck orChangeSets to check for.
	 * @return <code>true</code> if every element in
	 * <code>orChangeSets</code> is found in this AndRelationship,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllOrChangeSets(Collection orChangeSetsToCheck);


	/**
	 * Add a orNotChangeSet to this AndRelationship.
	 * @param newOrNotChangeSet orNotChangeSet to add.
	 */
	public void addOrNotChangeSet(edu.uci.isr.xarch.instance.IXMLLink newOrNotChangeSet);

	/**
	 * Add a collection of orNotChangeSets to this AndRelationship.
	 * @param orNotChangeSets orNotChangeSets to add.
	 */
	public void addOrNotChangeSets(Collection orNotChangeSets);

	/**
	 * Remove all orNotChangeSets from this AndRelationship.
	 */
	public void clearOrNotChangeSets();

	/**
	 * Remove the given orNotChangeSet from this AndRelationship.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param orNotChangeSetToRemove orNotChangeSet to remove.
	 */
	public void removeOrNotChangeSet(edu.uci.isr.xarch.instance.IXMLLink orNotChangeSetToRemove);

	/**
	 * Remove all the given orNotChangeSets from this AndRelationship.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param orNotChangeSets orNotChangeSet to remove.
	 */
	public void removeOrNotChangeSets(Collection orNotChangeSets);

	/**
	 * Get all the orNotChangeSets from this AndRelationship.
	 * @return all orNotChangeSets in this AndRelationship.
	 */
	public Collection getAllOrNotChangeSets();

	/**
	 * Determine if this AndRelationship contains a given orNotChangeSet.
	 * @return <code>true</code> if this AndRelationship contains the given
	 * orNotChangeSetToCheck, <code>false</code> otherwise.
	 */
	public boolean hasOrNotChangeSet(edu.uci.isr.xarch.instance.IXMLLink orNotChangeSetToCheck);

	/**
	 * Determine if this AndRelationship contains the given set of orNotChangeSets.
	 * @param orNotChangeSetsToCheck orNotChangeSets to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>orNotChangeSets</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasOrNotChangeSets(Collection orNotChangeSetsToCheck);

	/**
	 * Determine if this AndRelationship contains each element in the 
	 * given set of orNotChangeSets.
	 * @param orNotChangeSetsToCheck orNotChangeSets to check for.
	 * @return <code>true</code> if every element in
	 * <code>orNotChangeSets</code> is found in this AndRelationship,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllOrNotChangeSets(Collection orNotChangeSetsToCheck);

	/**
	 * Determine if another AndRelationship is equivalent to this one, ignoring
	 * ID's.
	 * @param AndRelationshipToCheck AndRelationship to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * AndRelationship are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IAndRelationship AndRelationshipToCheck);

}
