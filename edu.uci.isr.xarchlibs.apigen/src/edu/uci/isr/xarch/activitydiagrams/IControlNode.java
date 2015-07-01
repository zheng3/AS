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
package edu.uci.isr.xarch.activitydiagrams;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * ControlNode <code>xsi:type</code> in the
 * activitydiagrams namespace.  Extends and
 * inherits the properties of the
 * ActivityNode <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IControlNode extends IActivityNode, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"activitydiagrams", "ControlNode", IActivityNode.TYPE_METADATA,
		new XArchPropertyMetadata[]{},
		new XArchActionMetadata[]{});
	/**
	 * Determine if another ControlNode is equivalent to this one, ignoring
	 * ID's.
	 * @param ControlNodeToCheck ControlNode to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * ControlNode are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IControlNode ControlNodeToCheck);

}
