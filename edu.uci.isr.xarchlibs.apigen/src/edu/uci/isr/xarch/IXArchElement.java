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

public interface IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
			XArchTypeMetadata.XARCH_ELEMENT,
			null, "XArchElement", null,
			new XArchPropertyMetadata[]{},
			new XArchActionMetadata[]{});
	
	public static final int DEPTH_ZERO = 0;
	public static final int DEPTH_ONE = 1;
	public static final int DEPTH_INFINITY = 100;

	public void setXArch(IXArch thisXArch);
	public IXArch getXArch();
	public IXArchElement cloneElement(int depth);
	
	/**
	 * Gets metadata describing the <code>xsi:type</code> of this element.
	 * @return metadata describing the <code>xsi:type</code> of this element.
	 */
	public XArchTypeMetadata getTypeMetadata();
	
	/**
	 * Gets metadata describing the instance of this element.
	 * @return metadata describing the instance of this element.
	 */
	public XArchInstanceMetadata getInstanceMetadata();
}