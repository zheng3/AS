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
package edu.uci.isr.xarch.javainitparams;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * JavaClassFileParams <code>xsi:type</code> in the
 * javainitparams namespace.  Extends and
 * inherits the properties of the
 * JavaClassFile <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IJavaClassFileParams extends edu.uci.isr.xarch.javaimplementation.IJavaClassFile, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"javainitparams", "JavaClassFileParams", edu.uci.isr.xarch.javaimplementation.IJavaClassFile.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("initializationParameter", "javainitparams", "InitializationParameter", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Add a initializationParameter to this JavaClassFileParams.
	 * @param newInitializationParameter initializationParameter to add.
	 */
	public void addInitializationParameter(IInitializationParameter newInitializationParameter);

	/**
	 * Add a collection of initializationParameters to this JavaClassFileParams.
	 * @param initializationParameters initializationParameters to add.
	 */
	public void addInitializationParameters(Collection initializationParameters);

	/**
	 * Remove all initializationParameters from this JavaClassFileParams.
	 */
	public void clearInitializationParameters();

	/**
	 * Remove the given initializationParameter from this JavaClassFileParams.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param initializationParameterToRemove initializationParameter to remove.
	 */
	public void removeInitializationParameter(IInitializationParameter initializationParameterToRemove);

	/**
	 * Remove all the given initializationParameters from this JavaClassFileParams.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param initializationParameters initializationParameter to remove.
	 */
	public void removeInitializationParameters(Collection initializationParameters);

	/**
	 * Get all the initializationParameters from this JavaClassFileParams.
	 * @return all initializationParameters in this JavaClassFileParams.
	 */
	public Collection getAllInitializationParameters();

	/**
	 * Determine if this JavaClassFileParams contains a given initializationParameter.
	 * @return <code>true</code> if this JavaClassFileParams contains the given
	 * initializationParameterToCheck, <code>false</code> otherwise.
	 */
	public boolean hasInitializationParameter(IInitializationParameter initializationParameterToCheck);

	/**
	 * Determine if this JavaClassFileParams contains the given set of initializationParameters.
	 * @param initializationParametersToCheck initializationParameters to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>initializationParameters</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasInitializationParameters(Collection initializationParametersToCheck);

	/**
	 * Determine if this JavaClassFileParams contains each element in the 
	 * given set of initializationParameters.
	 * @param initializationParametersToCheck initializationParameters to check for.
	 * @return <code>true</code> if every element in
	 * <code>initializationParameters</code> is found in this JavaClassFileParams,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllInitializationParameters(Collection initializationParametersToCheck);

	/**
	 * Determine if another JavaClassFileParams is equivalent to this one, ignoring
	 * ID's.
	 * @param JavaClassFileParamsToCheck JavaClassFileParams to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * JavaClassFileParams are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IJavaClassFileParams JavaClassFileParamsToCheck);

}
