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
package edu.uci.isr.xarch.instance;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * SubArchitecture <code>xsi:type</code> in the
 * instance namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface ISubArchitecture extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"instance", "SubArchitecture", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("archInstance", "instance", "ArchInstance", 1, 1),
			XArchPropertyMetadata.createElement("interfaceInstanceMapping", "instance", "InterfaceInstanceMapping", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Set the archInstance for this SubArchitecture.
	 * @param value new archInstance
	 */
	public void setArchInstance(IArchInstance value);

	/**
	 * Clear the archInstance from this SubArchitecture.
	 */
	public void clearArchInstance();

	/**
	 * Get the archInstance from this SubArchitecture.
	 * @return archInstance
	 */
	public IArchInstance getArchInstance();

	/**
	 * Determine if this SubArchitecture has the given archInstance
	 * @param archInstanceToCheck archInstance to compare
	 * @return <code>true</code> if the archInstances are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasArchInstance(IArchInstance archInstanceToCheck);

	/**
	 * Add a interfaceInstanceMapping to this SubArchitecture.
	 * @param newInterfaceInstanceMapping interfaceInstanceMapping to add.
	 */
	public void addInterfaceInstanceMapping(IInterfaceInstanceMapping newInterfaceInstanceMapping);

	/**
	 * Add a collection of interfaceInstanceMappings to this SubArchitecture.
	 * @param interfaceInstanceMappings interfaceInstanceMappings to add.
	 */
	public void addInterfaceInstanceMappings(Collection interfaceInstanceMappings);

	/**
	 * Remove all interfaceInstanceMappings from this SubArchitecture.
	 */
	public void clearInterfaceInstanceMappings();

	/**
	 * Remove the given interfaceInstanceMapping from this SubArchitecture.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param interfaceInstanceMappingToRemove interfaceInstanceMapping to remove.
	 */
	public void removeInterfaceInstanceMapping(IInterfaceInstanceMapping interfaceInstanceMappingToRemove);

	/**
	 * Remove all the given interfaceInstanceMappings from this SubArchitecture.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param interfaceInstanceMappings interfaceInstanceMapping to remove.
	 */
	public void removeInterfaceInstanceMappings(Collection interfaceInstanceMappings);

	/**
	 * Get all the interfaceInstanceMappings from this SubArchitecture.
	 * @return all interfaceInstanceMappings in this SubArchitecture.
	 */
	public Collection getAllInterfaceInstanceMappings();

	/**
	 * Determine if this SubArchitecture contains a given interfaceInstanceMapping.
	 * @return <code>true</code> if this SubArchitecture contains the given
	 * interfaceInstanceMappingToCheck, <code>false</code> otherwise.
	 */
	public boolean hasInterfaceInstanceMapping(IInterfaceInstanceMapping interfaceInstanceMappingToCheck);

	/**
	 * Determine if this SubArchitecture contains the given set of interfaceInstanceMappings.
	 * @param interfaceInstanceMappingsToCheck interfaceInstanceMappings to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>interfaceInstanceMappings</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasInterfaceInstanceMappings(Collection interfaceInstanceMappingsToCheck);

	/**
	 * Determine if this SubArchitecture contains each element in the 
	 * given set of interfaceInstanceMappings.
	 * @param interfaceInstanceMappingsToCheck interfaceInstanceMappings to check for.
	 * @return <code>true</code> if every element in
	 * <code>interfaceInstanceMappings</code> is found in this SubArchitecture,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllInterfaceInstanceMappings(Collection interfaceInstanceMappingsToCheck);

	/**
	 * Gets the interfaceInstanceMapping from this SubArchitecture with the given
	 * id.
	 * @param id ID to look for.
	 * @return interfaceInstanceMapping with the given ID, or <code>null</code> if not found.
	 */
	public IInterfaceInstanceMapping getInterfaceInstanceMapping(String id);

	/**
	 * Gets the interfaceInstanceMappings from this SubArchitecture with the given
	 * ids.
	 * @param ids ID to look for.
	 * @return interfaceInstanceMappings with the given IDs.  If an element with a given
	 * ID was not found, that ID is ignored.
	 */
	public Collection getInterfaceInstanceMappings(Collection ids);

	/**
	 * Determine if another SubArchitecture is equivalent to this one, ignoring
	 * ID's.
	 * @param SubArchitectureToCheck SubArchitecture to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * SubArchitecture are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(ISubArchitecture SubArchitectureToCheck);

}