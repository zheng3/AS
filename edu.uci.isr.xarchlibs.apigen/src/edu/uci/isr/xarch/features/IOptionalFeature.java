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
 * OptionalFeature <code>xsi:type</code> in the
 * features namespace.  Extends and
 * inherits the properties of the
 * Feature <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IOptionalFeature extends IFeature, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"features", "OptionalFeature", IFeature.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("featureOptions", "features", "FeatureOptions", 1, 1),
			XArchPropertyMetadata.createElement("featureElements", "features", "FeatureElements", 1, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the featureOptions for this OptionalFeature.
	 * @param value new featureOptions
	 */
	public void setFeatureOptions(IFeatureOptions value);

	/**
	 * Clear the featureOptions from this OptionalFeature.
	 */
	public void clearFeatureOptions();

	/**
	 * Get the featureOptions from this OptionalFeature.
	 * @return featureOptions
	 */
	public IFeatureOptions getFeatureOptions();

	/**
	 * Determine if this OptionalFeature has the given featureOptions
	 * @param featureOptionsToCheck featureOptions to compare
	 * @return <code>true</code> if the featureOptionss are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasFeatureOptions(IFeatureOptions featureOptionsToCheck);

	/**
	 * Set the featureElements for this OptionalFeature.
	 * @param value new featureElements
	 */
	public void setFeatureElements(IFeatureElements value);

	/**
	 * Clear the featureElements from this OptionalFeature.
	 */
	public void clearFeatureElements();

	/**
	 * Get the featureElements from this OptionalFeature.
	 * @return featureElements
	 */
	public IFeatureElements getFeatureElements();

	/**
	 * Determine if this OptionalFeature has the given featureElements
	 * @param featureElementsToCheck featureElements to compare
	 * @return <code>true</code> if the featureElementss are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasFeatureElements(IFeatureElements featureElementsToCheck);
	/**
	 * Determine if another OptionalFeature is equivalent to this one, ignoring
	 * ID's.
	 * @param OptionalFeatureToCheck OptionalFeature to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * OptionalFeature are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IOptionalFeature OptionalFeatureToCheck);

}
