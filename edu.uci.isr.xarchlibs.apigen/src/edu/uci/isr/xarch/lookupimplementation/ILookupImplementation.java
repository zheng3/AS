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
package edu.uci.isr.xarch.lookupimplementation;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * LookupImplementation <code>xsi:type</code> in the
 * lookupimplementation namespace.  Extends and
 * inherits the properties of the
 * Implementation <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface ILookupImplementation extends edu.uci.isr.xarch.implementation.IImplementation, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"lookupimplementation", "LookupImplementation", edu.uci.isr.xarch.implementation.IImplementation.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("name", "lookupimplementation", "LookupName", 1, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the name for this LookupImplementation.
	 * @param value new name
	 */
	public void setName(ILookupName value);

	/**
	 * Clear the name from this LookupImplementation.
	 */
	public void clearName();

	/**
	 * Get the name from this LookupImplementation.
	 * @return name
	 */
	public ILookupName getName();

	/**
	 * Determine if this LookupImplementation has the given name
	 * @param nameToCheck name to compare
	 * @return <code>true</code> if the names are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasName(ILookupName nameToCheck);
	/**
	 * Determine if another LookupImplementation is equivalent to this one, ignoring
	 * ID's.
	 * @param LookupImplementationToCheck LookupImplementation to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * LookupImplementation are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(ILookupImplementation LookupImplementationToCheck);

}
