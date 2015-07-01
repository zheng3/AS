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
package edu.uci.isr.xarch.options;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * OptionalConnector <code>xsi:type</code> in the
 * options namespace.  Extends and
 * inherits the properties of the
 * Connector <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IOptionalConnector extends edu.uci.isr.xarch.types.IConnector, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"options", "OptionalConnector", edu.uci.isr.xarch.types.IConnector.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("optional", "options", "Optional", 0, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the optional for this OptionalConnector.
	 * @param value new optional
	 */
	public void setOptional(IOptional value);

	/**
	 * Clear the optional from this OptionalConnector.
	 */
	public void clearOptional();

	/**
	 * Get the optional from this OptionalConnector.
	 * @return optional
	 */
	public IOptional getOptional();

	/**
	 * Determine if this OptionalConnector has the given optional
	 * @param optionalToCheck optional to compare
	 * @return <code>true</code> if the optionals are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasOptional(IOptional optionalToCheck);
	/**
	 * Determine if another OptionalConnector is equivalent to this one, ignoring
	 * ID's.
	 * @param OptionalConnectorToCheck OptionalConnector to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * OptionalConnector are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IOptionalConnector OptionalConnectorToCheck);

}
