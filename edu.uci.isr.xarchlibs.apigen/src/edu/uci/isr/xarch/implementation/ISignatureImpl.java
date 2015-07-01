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
 * SignatureImpl <code>xsi:type</code> in the
 * implementation namespace.  Extends and
 * inherits the properties of the
 * Signature <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface ISignatureImpl extends edu.uci.isr.xarch.types.ISignature, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"implementation", "SignatureImpl", edu.uci.isr.xarch.types.ISignature.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("implementation", "implementation", "Implementation", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Add a implementation to this SignatureImpl.
	 * @param newImplementation implementation to add.
	 */
	public void addImplementation(IImplementation newImplementation);

	/**
	 * Add a collection of implementations to this SignatureImpl.
	 * @param implementations implementations to add.
	 */
	public void addImplementations(Collection implementations);

	/**
	 * Remove all implementations from this SignatureImpl.
	 */
	public void clearImplementations();

	/**
	 * Remove the given implementation from this SignatureImpl.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param implementationToRemove implementation to remove.
	 */
	public void removeImplementation(IImplementation implementationToRemove);

	/**
	 * Remove all the given implementations from this SignatureImpl.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param implementations implementation to remove.
	 */
	public void removeImplementations(Collection implementations);

	/**
	 * Get all the implementations from this SignatureImpl.
	 * @return all implementations in this SignatureImpl.
	 */
	public Collection getAllImplementations();

	/**
	 * Determine if this SignatureImpl contains a given implementation.
	 * @return <code>true</code> if this SignatureImpl contains the given
	 * implementationToCheck, <code>false</code> otherwise.
	 */
	public boolean hasImplementation(IImplementation implementationToCheck);

	/**
	 * Determine if this SignatureImpl contains the given set of implementations.
	 * @param implementationsToCheck implementations to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>implementations</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasImplementations(Collection implementationsToCheck);

	/**
	 * Determine if this SignatureImpl contains each element in the 
	 * given set of implementations.
	 * @param implementationsToCheck implementations to check for.
	 * @return <code>true</code> if every element in
	 * <code>implementations</code> is found in this SignatureImpl,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllImplementations(Collection implementationsToCheck);

	/**
	 * Determine if another SignatureImpl is equivalent to this one, ignoring
	 * ID's.
	 * @param SignatureImplToCheck SignatureImpl to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * SignatureImpl are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(ISignatureImpl SignatureImplToCheck);

}
