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
 * OptionalComponentPosition <code>xsi:type</code> in the
 * menage namespace.  Extends and
 * inherits the properties of the
 * OptionalComponent <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IOptionalComponentPosition extends edu.uci.isr.xarch.options.IOptionalComponent, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"menage", "OptionalComponentPosition", edu.uci.isr.xarch.options.IOptionalComponent.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("position", "menage", "Position", 1, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the position for this OptionalComponentPosition.
	 * @param value new position
	 */
	public void setPosition(IPosition value);

	/**
	 * Clear the position from this OptionalComponentPosition.
	 */
	public void clearPosition();

	/**
	 * Get the position from this OptionalComponentPosition.
	 * @return position
	 */
	public IPosition getPosition();

	/**
	 * Determine if this OptionalComponentPosition has the given position
	 * @param positionToCheck position to compare
	 * @return <code>true</code> if the positions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasPosition(IPosition positionToCheck);
	/**
	 * Determine if another OptionalComponentPosition is equivalent to this one, ignoring
	 * ID's.
	 * @param OptionalComponentPositionToCheck OptionalComponentPosition to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * OptionalComponentPosition are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IOptionalComponentPosition OptionalComponentPositionToCheck);

}
