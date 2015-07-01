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
package edu.uci.isr.xarch.javasourcecode;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * JavaSourceCodeImplementation <code>xsi:type</code> in the
 * javasourcecode namespace.  Extends and
 * inherits the properties of the
 * Implementation <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IJavaSourceCodeImplementation extends edu.uci.isr.xarch.implementation.IImplementation, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"javasourcecode", "JavaSourceCodeImplementation", edu.uci.isr.xarch.implementation.IImplementation.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("sourceCodeFile", "javasourcecode", "JavaSourceFile", 0, XArchPropertyMetadata.UNBOUNDED),
			XArchPropertyMetadata.createElement("repositoryLocation", "javasourcecode", "RepositoryLocation", 0, XArchPropertyMetadata.UNBOUNDED),
			XArchPropertyMetadata.createElement("sourceCodeManager", "javasourcecode", "JavaSourceCodeManager", 0, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Add a sourceCodeFile to this JavaSourceCodeImplementation.
	 * @param newSourceCodeFile sourceCodeFile to add.
	 */
	public void addSourceCodeFile(IJavaSourceFile newSourceCodeFile);

	/**
	 * Add a collection of sourceCodeFiles to this JavaSourceCodeImplementation.
	 * @param sourceCodeFiles sourceCodeFiles to add.
	 */
	public void addSourceCodeFiles(Collection sourceCodeFiles);

	/**
	 * Remove all sourceCodeFiles from this JavaSourceCodeImplementation.
	 */
	public void clearSourceCodeFiles();

	/**
	 * Remove the given sourceCodeFile from this JavaSourceCodeImplementation.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param sourceCodeFileToRemove sourceCodeFile to remove.
	 */
	public void removeSourceCodeFile(IJavaSourceFile sourceCodeFileToRemove);

	/**
	 * Remove all the given sourceCodeFiles from this JavaSourceCodeImplementation.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param sourceCodeFiles sourceCodeFile to remove.
	 */
	public void removeSourceCodeFiles(Collection sourceCodeFiles);

	/**
	 * Get all the sourceCodeFiles from this JavaSourceCodeImplementation.
	 * @return all sourceCodeFiles in this JavaSourceCodeImplementation.
	 */
	public Collection getAllSourceCodeFiles();

	/**
	 * Determine if this JavaSourceCodeImplementation contains a given sourceCodeFile.
	 * @return <code>true</code> if this JavaSourceCodeImplementation contains the given
	 * sourceCodeFileToCheck, <code>false</code> otherwise.
	 */
	public boolean hasSourceCodeFile(IJavaSourceFile sourceCodeFileToCheck);

	/**
	 * Determine if this JavaSourceCodeImplementation contains the given set of sourceCodeFiles.
	 * @param sourceCodeFilesToCheck sourceCodeFiles to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>sourceCodeFiles</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasSourceCodeFiles(Collection sourceCodeFilesToCheck);

	/**
	 * Determine if this JavaSourceCodeImplementation contains each element in the 
	 * given set of sourceCodeFiles.
	 * @param sourceCodeFilesToCheck sourceCodeFiles to check for.
	 * @return <code>true</code> if every element in
	 * <code>sourceCodeFiles</code> is found in this JavaSourceCodeImplementation,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllSourceCodeFiles(Collection sourceCodeFilesToCheck);


	/**
	 * Add a repositoryLocation to this JavaSourceCodeImplementation.
	 * @param newRepositoryLocation repositoryLocation to add.
	 */
	public void addRepositoryLocation(IRepositoryLocation newRepositoryLocation);

	/**
	 * Add a collection of repositoryLocations to this JavaSourceCodeImplementation.
	 * @param repositoryLocations repositoryLocations to add.
	 */
	public void addRepositoryLocations(Collection repositoryLocations);

	/**
	 * Remove all repositoryLocations from this JavaSourceCodeImplementation.
	 */
	public void clearRepositoryLocations();

	/**
	 * Remove the given repositoryLocation from this JavaSourceCodeImplementation.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param repositoryLocationToRemove repositoryLocation to remove.
	 */
	public void removeRepositoryLocation(IRepositoryLocation repositoryLocationToRemove);

	/**
	 * Remove all the given repositoryLocations from this JavaSourceCodeImplementation.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param repositoryLocations repositoryLocation to remove.
	 */
	public void removeRepositoryLocations(Collection repositoryLocations);

	/**
	 * Get all the repositoryLocations from this JavaSourceCodeImplementation.
	 * @return all repositoryLocations in this JavaSourceCodeImplementation.
	 */
	public Collection getAllRepositoryLocations();

	/**
	 * Determine if this JavaSourceCodeImplementation contains a given repositoryLocation.
	 * @return <code>true</code> if this JavaSourceCodeImplementation contains the given
	 * repositoryLocationToCheck, <code>false</code> otherwise.
	 */
	public boolean hasRepositoryLocation(IRepositoryLocation repositoryLocationToCheck);

	/**
	 * Determine if this JavaSourceCodeImplementation contains the given set of repositoryLocations.
	 * @param repositoryLocationsToCheck repositoryLocations to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>repositoryLocations</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasRepositoryLocations(Collection repositoryLocationsToCheck);

	/**
	 * Determine if this JavaSourceCodeImplementation contains each element in the 
	 * given set of repositoryLocations.
	 * @param repositoryLocationsToCheck repositoryLocations to check for.
	 * @return <code>true</code> if every element in
	 * <code>repositoryLocations</code> is found in this JavaSourceCodeImplementation,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllRepositoryLocations(Collection repositoryLocationsToCheck);


	/**
	 * Set the sourceCodeManager for this JavaSourceCodeImplementation.
	 * @param value new sourceCodeManager
	 */
	public void setSourceCodeManager(IJavaSourceCodeManager value);

	/**
	 * Clear the sourceCodeManager from this JavaSourceCodeImplementation.
	 */
	public void clearSourceCodeManager();

	/**
	 * Get the sourceCodeManager from this JavaSourceCodeImplementation.
	 * @return sourceCodeManager
	 */
	public IJavaSourceCodeManager getSourceCodeManager();

	/**
	 * Determine if this JavaSourceCodeImplementation has the given sourceCodeManager
	 * @param sourceCodeManagerToCheck sourceCodeManager to compare
	 * @return <code>true</code> if the sourceCodeManagers are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasSourceCodeManager(IJavaSourceCodeManager sourceCodeManagerToCheck);
	/**
	 * Determine if another JavaSourceCodeImplementation is equivalent to this one, ignoring
	 * ID's.
	 * @param JavaSourceCodeImplementationToCheck JavaSourceCodeImplementation to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * JavaSourceCodeImplementation are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IJavaSourceCodeImplementation JavaSourceCodeImplementationToCheck);

}
