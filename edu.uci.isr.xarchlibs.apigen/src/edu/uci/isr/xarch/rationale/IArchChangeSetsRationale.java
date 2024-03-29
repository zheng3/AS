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
package edu.uci.isr.xarch.rationale;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * ArchChangeSetsRationale <code>xsi:type</code> in the
 * rationale namespace.  Extends and
 * inherits the properties of the
 * ArchChangeSets <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IArchChangeSetsRationale extends edu.uci.isr.xarch.changesets.IArchChangeSets, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"rationale", "ArchChangeSetsRationale", edu.uci.isr.xarch.changesets.IArchChangeSets.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("archRationale", "rationale", "ArchRationale", 0, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the archRationale for this ArchChangeSetsRationale.
	 * @param value new archRationale
	 */
	public void setArchRationale(IArchRationale value);

	/**
	 * Clear the archRationale from this ArchChangeSetsRationale.
	 */
	public void clearArchRationale();

	/**
	 * Get the archRationale from this ArchChangeSetsRationale.
	 * @return archRationale
	 */
	public IArchRationale getArchRationale();

	/**
	 * Determine if this ArchChangeSetsRationale has the given archRationale
	 * @param archRationaleToCheck archRationale to compare
	 * @return <code>true</code> if the archRationales are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasArchRationale(IArchRationale archRationaleToCheck);
	/**
	 * Determine if another ArchChangeSetsRationale is equivalent to this one, ignoring
	 * ID's.
	 * @param ArchChangeSetsRationaleToCheck ArchChangeSetsRationale to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * ArchChangeSetsRationale are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IArchChangeSetsRationale ArchChangeSetsRationaleToCheck);

}
