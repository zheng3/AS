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
package edu.uci.isr.xarch.variants;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * VariantConnectorType <code>xsi:type</code> in the
 * variants namespace.  Extends and
 * inherits the properties of the
 * ConnectorType <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IVariantConnectorType extends edu.uci.isr.xarch.types.IConnectorType, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"variants", "VariantConnectorType", edu.uci.isr.xarch.types.IConnectorType.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("variant", "variants", "Variant", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Add a variant to this VariantConnectorType.
	 * @param newVariant variant to add.
	 */
	public void addVariant(IVariant newVariant);

	/**
	 * Add a collection of variants to this VariantConnectorType.
	 * @param variants variants to add.
	 */
	public void addVariants(Collection variants);

	/**
	 * Remove all variants from this VariantConnectorType.
	 */
	public void clearVariants();

	/**
	 * Remove the given variant from this VariantConnectorType.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param variantToRemove variant to remove.
	 */
	public void removeVariant(IVariant variantToRemove);

	/**
	 * Remove all the given variants from this VariantConnectorType.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param variants variant to remove.
	 */
	public void removeVariants(Collection variants);

	/**
	 * Get all the variants from this VariantConnectorType.
	 * @return all variants in this VariantConnectorType.
	 */
	public Collection getAllVariants();

	/**
	 * Determine if this VariantConnectorType contains a given variant.
	 * @return <code>true</code> if this VariantConnectorType contains the given
	 * variantToCheck, <code>false</code> otherwise.
	 */
	public boolean hasVariant(IVariant variantToCheck);

	/**
	 * Determine if this VariantConnectorType contains the given set of variants.
	 * @param variantsToCheck variants to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>variants</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasVariants(Collection variantsToCheck);

	/**
	 * Determine if this VariantConnectorType contains each element in the 
	 * given set of variants.
	 * @param variantsToCheck variants to check for.
	 * @return <code>true</code> if every element in
	 * <code>variants</code> is found in this VariantConnectorType,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllVariants(Collection variantsToCheck);

	/**
	 * Determine if another VariantConnectorType is equivalent to this one, ignoring
	 * ID's.
	 * @param VariantConnectorTypeToCheck VariantConnectorType to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * VariantConnectorType are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IVariantConnectorType VariantConnectorTypeToCheck);

}
