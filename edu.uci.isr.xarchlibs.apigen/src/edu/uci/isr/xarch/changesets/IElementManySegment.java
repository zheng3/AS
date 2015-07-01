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
 * ElementManySegment <code>xsi:type</code> in the
 * changesets namespace.  Extends and
 * inherits the properties of the
 * ChangeSegment <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IElementManySegment extends IChangeSegment, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"changesets", "ElementManySegment", IChangeSegment.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("changeSegment", "changesets", "ElementSegment", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Add a changeSegment to this ElementManySegment.
	 * @param newChangeSegment changeSegment to add.
	 */
	public void addChangeSegment(IElementSegment newChangeSegment);

	/**
	 * Add a collection of changeSegments to this ElementManySegment.
	 * @param changeSegments changeSegments to add.
	 */
	public void addChangeSegments(Collection changeSegments);

	/**
	 * Remove all changeSegments from this ElementManySegment.
	 */
	public void clearChangeSegments();

	/**
	 * Remove the given changeSegment from this ElementManySegment.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param changeSegmentToRemove changeSegment to remove.
	 */
	public void removeChangeSegment(IElementSegment changeSegmentToRemove);

	/**
	 * Remove all the given changeSegments from this ElementManySegment.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param changeSegments changeSegment to remove.
	 */
	public void removeChangeSegments(Collection changeSegments);

	/**
	 * Get all the changeSegments from this ElementManySegment.
	 * @return all changeSegments in this ElementManySegment.
	 */
	public Collection getAllChangeSegments();

	/**
	 * Determine if this ElementManySegment contains a given changeSegment.
	 * @return <code>true</code> if this ElementManySegment contains the given
	 * changeSegmentToCheck, <code>false</code> otherwise.
	 */
	public boolean hasChangeSegment(IElementSegment changeSegmentToCheck);

	/**
	 * Determine if this ElementManySegment contains the given set of changeSegments.
	 * @param changeSegmentsToCheck changeSegments to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>changeSegments</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasChangeSegments(Collection changeSegmentsToCheck);

	/**
	 * Determine if this ElementManySegment contains each element in the 
	 * given set of changeSegments.
	 * @param changeSegmentsToCheck changeSegments to check for.
	 * @return <code>true</code> if every element in
	 * <code>changeSegments</code> is found in this ElementManySegment,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllChangeSegments(Collection changeSegmentsToCheck);

	/**
	 * Determine if another ElementManySegment is equivalent to this one, ignoring
	 * ID's.
	 * @param ElementManySegmentToCheck ElementManySegment to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * ElementManySegment are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IElementManySegment ElementManySegmentToCheck);

}
