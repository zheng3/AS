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
package edu.uci.isr.xarch.boolguard;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * BooleanGuard <code>xsi:type</code> in the
 * boolguard namespace.  Extends and
 * inherits the properties of the
 * Guard <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IBooleanGuard extends edu.uci.isr.xarch.options.IGuard, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"boolguard", "BooleanGuard", edu.uci.isr.xarch.options.IGuard.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("booleanExp", "boolguard", "BooleanExp", 1, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the booleanExp for this BooleanGuard.
	 * @param value new booleanExp
	 */
	public void setBooleanExp(IBooleanExp value);

	/**
	 * Clear the booleanExp from this BooleanGuard.
	 */
	public void clearBooleanExp();

	/**
	 * Get the booleanExp from this BooleanGuard.
	 * @return booleanExp
	 */
	public IBooleanExp getBooleanExp();

	/**
	 * Determine if this BooleanGuard has the given booleanExp
	 * @param booleanExpToCheck booleanExp to compare
	 * @return <code>true</code> if the booleanExps are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasBooleanExp(IBooleanExp booleanExpToCheck);
	/**
	 * Determine if another BooleanGuard is equivalent to this one, ignoring
	 * ID's.
	 * @param BooleanGuardToCheck BooleanGuard to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * BooleanGuard are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IBooleanGuard BooleanGuardToCheck);

}
