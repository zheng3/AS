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
package edu.uci.isr.xarch.features;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * AlternativeFeature <code>xsi:type</code> in the
 * features namespace.  Extends and
 * inherits the properties of the
 * Feature <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IAlternativeFeature extends IFeature, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"features", "AlternativeFeature", IFeature.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("featureElements", "features", "FeatureElements", 1, 1),
			XArchPropertyMetadata.createElement("featureVarients", "features", "FeatureVarients", 1, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the featureElements for this AlternativeFeature.
	 * @param value new featureElements
	 */
	public void setFeatureElements(IFeatureElements value);

	/**
	 * Clear the featureElements from this AlternativeFeature.
	 */
	public void clearFeatureElements();

	/**
	 * Get the featureElements from this AlternativeFeature.
	 * @return featureElements
	 */
	public IFeatureElements getFeatureElements();

	/**
	 * Determine if this AlternativeFeature has the given featureElements
	 * @param featureElementsToCheck featureElements to compare
	 * @return <code>true</code> if the featureElementss are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasFeatureElements(IFeatureElements featureElementsToCheck);

	/**
	 * Set the featureVarients for this AlternativeFeature.
	 * @param value new featureVarients
	 */
	public void setFeatureVarients(IFeatureVarients value);

	/**
	 * Clear the featureVarients from this AlternativeFeature.
	 */
	public void clearFeatureVarients();

	/**
	 * Get the featureVarients from this AlternativeFeature.
	 * @return featureVarients
	 */
	public IFeatureVarients getFeatureVarients();

	/**
	 * Determine if this AlternativeFeature has the given featureVarients
	 * @param featureVarientsToCheck featureVarients to compare
	 * @return <code>true</code> if the featureVarientss are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasFeatureVarients(IFeatureVarients featureVarientsToCheck);
	/**
	 * Determine if another AlternativeFeature is equivalent to this one, ignoring
	 * ID's.
	 * @param AlternativeFeatureToCheck AlternativeFeature to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * AlternativeFeature are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IAlternativeFeature AlternativeFeatureToCheck);

}
