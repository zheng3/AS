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
package edu.uci.isr.xarch.implementation;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * InterfaceTypeImpl <code>xsi:type</code> in the
 * implementation namespace.  Extends and
 * inherits the properties of the
 * InterfaceType <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IInterfaceTypeImpl extends edu.uci.isr.xarch.types.IInterfaceType, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"implementation", "InterfaceTypeImpl", edu.uci.isr.xarch.types.IInterfaceType.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("implementation", "implementation", "Implementation", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Add a implementation to this InterfaceTypeImpl.
	 * @param newImplementation implementation to add.
	 */
	public void addImplementation(IImplementation newImplementation);

	/**
	 * Add a collection of implementations to this InterfaceTypeImpl.
	 * @param implementations implementations to add.
	 */
	public void addImplementations(Collection implementations);

	/**
	 * Remove all implementations from this InterfaceTypeImpl.
	 */
	public void clearImplementations();

	/**
	 * Remove the given implementation from this InterfaceTypeImpl.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param implementationToRemove implementation to remove.
	 */
	public void removeImplementation(IImplementation implementationToRemove);

	/**
	 * Remove all the given implementations from this InterfaceTypeImpl.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param implementations implementation to remove.
	 */
	public void removeImplementations(Collection implementations);

	/**
	 * Get all the implementations from this InterfaceTypeImpl.
	 * @return all implementations in this InterfaceTypeImpl.
	 */
	public Collection getAllImplementations();

	/**
	 * Determine if this InterfaceTypeImpl contains a given implementation.
	 * @return <code>true</code> if this InterfaceTypeImpl contains the given
	 * implementationToCheck, <code>false</code> otherwise.
	 */
	public boolean hasImplementation(IImplementation implementationToCheck);

	/**
	 * Determine if this InterfaceTypeImpl contains the given set of implementations.
	 * @param implementationsToCheck implementations to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>implementations</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasImplementations(Collection implementationsToCheck);

	/**
	 * Determine if this InterfaceTypeImpl contains each element in the 
	 * given set of implementations.
	 * @param implementationsToCheck implementations to check for.
	 * @return <code>true</code> if every element in
	 * <code>implementations</code> is found in this InterfaceTypeImpl,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllImplementations(Collection implementationsToCheck);

	/**
	 * Determine if another InterfaceTypeImpl is equivalent to this one, ignoring
	 * ID's.
	 * @param InterfaceTypeImplToCheck InterfaceTypeImpl to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * InterfaceTypeImpl are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IInterfaceTypeImpl InterfaceTypeImplToCheck);

}
