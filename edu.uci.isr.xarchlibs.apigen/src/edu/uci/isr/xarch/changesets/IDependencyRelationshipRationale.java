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
 * DependencyRelationshipRationale <code>xsi:type</code> in the
 * changesets namespace.  Extends and
 * inherits the properties of the
 * RelationshipRationale <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IDependencyRelationshipRationale extends IRelationshipRationale, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"changesets", "DependencyRelationshipRationale", IRelationshipRationale.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("source", "changesets", "PathReference", 0, XArchPropertyMetadata.UNBOUNDED),
			XArchPropertyMetadata.createElement("requires", "changesets", "PathReference", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Add a source to this DependencyRelationshipRationale.
	 * @param newSource source to add.
	 */
	public void addSource(IPathReference newSource);

	/**
	 * Add a collection of sources to this DependencyRelationshipRationale.
	 * @param sources sources to add.
	 */
	public void addSources(Collection sources);

	/**
	 * Remove all sources from this DependencyRelationshipRationale.
	 */
	public void clearSources();

	/**
	 * Remove the given source from this DependencyRelationshipRationale.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param sourceToRemove source to remove.
	 */
	public void removeSource(IPathReference sourceToRemove);

	/**
	 * Remove all the given sources from this DependencyRelationshipRationale.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param sources source to remove.
	 */
	public void removeSources(Collection sources);

	/**
	 * Get all the sources from this DependencyRelationshipRationale.
	 * @return all sources in this DependencyRelationshipRationale.
	 */
	public Collection getAllSources();

	/**
	 * Determine if this DependencyRelationshipRationale contains a given source.
	 * @return <code>true</code> if this DependencyRelationshipRationale contains the given
	 * sourceToCheck, <code>false</code> otherwise.
	 */
	public boolean hasSource(IPathReference sourceToCheck);

	/**
	 * Determine if this DependencyRelationshipRationale contains the given set of sources.
	 * @param sourcesToCheck sources to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>sources</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasSources(Collection sourcesToCheck);

	/**
	 * Determine if this DependencyRelationshipRationale contains each element in the 
	 * given set of sources.
	 * @param sourcesToCheck sources to check for.
	 * @return <code>true</code> if every element in
	 * <code>sources</code> is found in this DependencyRelationshipRationale,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllSources(Collection sourcesToCheck);


	/**
	 * Add a requires to this DependencyRelationshipRationale.
	 * @param newRequires requires to add.
	 */
	public void addRequires(IPathReference newRequires);

	/**
	 * Add a collection of requiress to this DependencyRelationshipRationale.
	 * @param requiress requiress to add.
	 */
	public void addRequiress(Collection requiress);

	/**
	 * Remove all requiress from this DependencyRelationshipRationale.
	 */
	public void clearRequiress();

	/**
	 * Remove the given requires from this DependencyRelationshipRationale.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param requiresToRemove requires to remove.
	 */
	public void removeRequires(IPathReference requiresToRemove);

	/**
	 * Remove all the given requiress from this DependencyRelationshipRationale.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param requiress requires to remove.
	 */
	public void removeRequiress(Collection requiress);

	/**
	 * Get all the requiress from this DependencyRelationshipRationale.
	 * @return all requiress in this DependencyRelationshipRationale.
	 */
	public Collection getAllRequiress();

	/**
	 * Determine if this DependencyRelationshipRationale contains a given requires.
	 * @return <code>true</code> if this DependencyRelationshipRationale contains the given
	 * requiresToCheck, <code>false</code> otherwise.
	 */
	public boolean hasRequires(IPathReference requiresToCheck);

	/**
	 * Determine if this DependencyRelationshipRationale contains the given set of requiress.
	 * @param requiressToCheck requiress to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>requiress</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasRequiress(Collection requiressToCheck);

	/**
	 * Determine if this DependencyRelationshipRationale contains each element in the 
	 * given set of requiress.
	 * @param requiressToCheck requiress to check for.
	 * @return <code>true</code> if every element in
	 * <code>requiress</code> is found in this DependencyRelationshipRationale,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllRequiress(Collection requiressToCheck);

	/**
	 * Determine if another DependencyRelationshipRationale is equivalent to this one, ignoring
	 * ID's.
	 * @param DependencyRelationshipRationaleToCheck DependencyRelationshipRationale to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * DependencyRelationshipRationale are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IDependencyRelationshipRationale DependencyRelationshipRationaleToCheck);

}
