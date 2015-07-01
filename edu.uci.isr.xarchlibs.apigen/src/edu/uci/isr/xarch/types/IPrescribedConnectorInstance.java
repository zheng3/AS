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
package edu.uci.isr.xarch.types;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * PrescribedConnectorInstance <code>xsi:type</code> in the
 * types namespace.  Extends and
 * inherits the properties of the
 * ConnectorInstance <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IPrescribedConnectorInstance extends edu.uci.isr.xarch.instance.IConnectorInstance, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"types", "PrescribedConnectorInstance", edu.uci.isr.xarch.instance.IConnectorInstance.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("structure", "instance", "XMLLink", 0, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the structure for this PrescribedConnectorInstance.
	 * @param value new structure
	 */
	public void setStructure(edu.uci.isr.xarch.instance.IXMLLink value);

	/**
	 * Clear the structure from this PrescribedConnectorInstance.
	 */
	public void clearStructure();

	/**
	 * Get the structure from this PrescribedConnectorInstance.
	 * @return structure
	 */
	public edu.uci.isr.xarch.instance.IXMLLink getStructure();

	/**
	 * Determine if this PrescribedConnectorInstance has the given structure
	 * @param structureToCheck structure to compare
	 * @return <code>true</code> if the structures are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasStructure(edu.uci.isr.xarch.instance.IXMLLink structureToCheck);
	/**
	 * Determine if another PrescribedConnectorInstance is equivalent to this one, ignoring
	 * ID's.
	 * @param PrescribedConnectorInstanceToCheck PrescribedConnectorInstance to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * PrescribedConnectorInstance are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IPrescribedConnectorInstance PrescribedConnectorInstanceToCheck);

}
