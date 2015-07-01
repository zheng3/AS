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
package edu.uci.isr.xarch.menage;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * OptionalInterfacePosition <code>xsi:type</code> in the
 * menage namespace.  Extends and
 * inherits the properties of the
 * OptionalInterface <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IOptionalInterfacePosition extends edu.uci.isr.xarch.options.IOptionalInterface, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"menage", "OptionalInterfacePosition", edu.uci.isr.xarch.options.IOptionalInterface.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("position", "menage", "Position", 1, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the position for this OptionalInterfacePosition.
	 * @param value new position
	 */
	public void setPosition(IPosition value);

	/**
	 * Clear the position from this OptionalInterfacePosition.
	 */
	public void clearPosition();

	/**
	 * Get the position from this OptionalInterfacePosition.
	 * @return position
	 */
	public IPosition getPosition();

	/**
	 * Determine if this OptionalInterfacePosition has the given position
	 * @param positionToCheck position to compare
	 * @return <code>true</code> if the positions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasPosition(IPosition positionToCheck);
	/**
	 * Determine if another OptionalInterfacePosition is equivalent to this one, ignoring
	 * ID's.
	 * @param OptionalInterfacePositionToCheck OptionalInterfacePosition to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * OptionalInterfacePosition are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IOptionalInterfacePosition OptionalInterfacePositionToCheck);

}
