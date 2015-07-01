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
package edu.uci.isr.xarch.versions;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * InterfaceTypeImplVers <code>xsi:type</code> in the
 * versions namespace.  Extends and
 * inherits the properties of the
 * InterfaceTypeImpl <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IInterfaceTypeImplVers extends edu.uci.isr.xarch.implementation.IInterfaceTypeImpl, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"versions", "InterfaceTypeImplVers", edu.uci.isr.xarch.implementation.IInterfaceTypeImpl.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("versionGraphNode", "instance", "XMLLink", 1, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the versionGraphNode for this InterfaceTypeImplVers.
	 * @param value new versionGraphNode
	 */
	public void setVersionGraphNode(edu.uci.isr.xarch.instance.IXMLLink value);

	/**
	 * Clear the versionGraphNode from this InterfaceTypeImplVers.
	 */
	public void clearVersionGraphNode();

	/**
	 * Get the versionGraphNode from this InterfaceTypeImplVers.
	 * @return versionGraphNode
	 */
	public edu.uci.isr.xarch.instance.IXMLLink getVersionGraphNode();

	/**
	 * Determine if this InterfaceTypeImplVers has the given versionGraphNode
	 * @param versionGraphNodeToCheck versionGraphNode to compare
	 * @return <code>true</code> if the versionGraphNodes are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasVersionGraphNode(edu.uci.isr.xarch.instance.IXMLLink versionGraphNodeToCheck);
	/**
	 * Determine if another InterfaceTypeImplVers is equivalent to this one, ignoring
	 * ID's.
	 * @param InterfaceTypeImplVersToCheck InterfaceTypeImplVers to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * InterfaceTypeImplVers are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IInterfaceTypeImplVers InterfaceTypeImplVersToCheck);

}
