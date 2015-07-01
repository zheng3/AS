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
package edu.uci.isr.xarch;

/**
 * Describes an xArch context object, capable of creating
 * xArch elements.
 *
 * @author Eric M. Dashofy [edashofy@ics.uci.edu]
 */
public interface IXArchContext{

	final public static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
			XArchTypeMetadata.XARCH_CONTEXT,
			null, null, null, 
			new XArchPropertyMetadata[]{},
			new XArchActionMetadata[]{});
	
	/**
	 * Get the <code>IXArch</code> object that
	 * this context is based upon.
	 *
	 * @return <code>IXArch</code> object upon which
	 * this context is based.
	 */
	public IXArch getXArch();

	/**
	 * Gets metadata describing the <code>xsi:type</code> of this element.
	 * @return metadata describing the <code>xsi:type</code> of this element.
	 */
	public XArchTypeMetadata getTypeMetadata();
}