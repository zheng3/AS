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
package edu.uci.isr.xarch.javaimplementation;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * JavaImplementation <code>xsi:type</code> in the
 * javaimplementation namespace.  Extends and
 * inherits the properties of the
 * Implementation <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IJavaImplementation extends edu.uci.isr.xarch.implementation.IImplementation, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"javaimplementation", "JavaImplementation", edu.uci.isr.xarch.implementation.IImplementation.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("mainClass", "javaimplementation", "JavaClassFile", 1, 1),
			XArchPropertyMetadata.createElement("auxClass", "javaimplementation", "JavaClassFile", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Set the mainClass for this JavaImplementation.
	 * @param value new mainClass
	 */
	public void setMainClass(IJavaClassFile value);

	/**
	 * Clear the mainClass from this JavaImplementation.
	 */
	public void clearMainClass();

	/**
	 * Get the mainClass from this JavaImplementation.
	 * @return mainClass
	 */
	public IJavaClassFile getMainClass();

	/**
	 * Determine if this JavaImplementation has the given mainClass
	 * @param mainClassToCheck mainClass to compare
	 * @return <code>true</code> if the mainClasss are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasMainClass(IJavaClassFile mainClassToCheck);

	/**
	 * Add a auxClass to this JavaImplementation.
	 * @param newAuxClass auxClass to add.
	 */
	public void addAuxClass(IJavaClassFile newAuxClass);

	/**
	 * Add a collection of auxClasss to this JavaImplementation.
	 * @param auxClasss auxClasss to add.
	 */
	public void addAuxClasss(Collection auxClasss);

	/**
	 * Remove all auxClasss from this JavaImplementation.
	 */
	public void clearAuxClasss();

	/**
	 * Remove the given auxClass from this JavaImplementation.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param auxClassToRemove auxClass to remove.
	 */
	public void removeAuxClass(IJavaClassFile auxClassToRemove);

	/**
	 * Remove all the given auxClasss from this JavaImplementation.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param auxClasss auxClass to remove.
	 */
	public void removeAuxClasss(Collection auxClasss);

	/**
	 * Get all the auxClasss from this JavaImplementation.
	 * @return all auxClasss in this JavaImplementation.
	 */
	public Collection getAllAuxClasss();

	/**
	 * Determine if this JavaImplementation contains a given auxClass.
	 * @return <code>true</code> if this JavaImplementation contains the given
	 * auxClassToCheck, <code>false</code> otherwise.
	 */
	public boolean hasAuxClass(IJavaClassFile auxClassToCheck);

	/**
	 * Determine if this JavaImplementation contains the given set of auxClasss.
	 * @param auxClasssToCheck auxClasss to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>auxClasss</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasAuxClasss(Collection auxClasssToCheck);

	/**
	 * Determine if this JavaImplementation contains each element in the 
	 * given set of auxClasss.
	 * @param auxClasssToCheck auxClasss to check for.
	 * @return <code>true</code> if every element in
	 * <code>auxClasss</code> is found in this JavaImplementation,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllAuxClasss(Collection auxClasssToCheck);

	/**
	 * Determine if another JavaImplementation is equivalent to this one, ignoring
	 * ID's.
	 * @param JavaImplementationToCheck JavaImplementation to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * JavaImplementation are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IJavaImplementation JavaImplementationToCheck);

}
