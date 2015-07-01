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

import java.util.Arrays;
import java.util.List;

public class XArchActionMetadata {

	/**
	 * <code>CREATE</code> indicates that this describes a create action for a context.
	 */
	public static final int CREATE = 0x01 << 0;
	
	/**
	 * <code>CREATE_ELEMENT</code> indicates that this describes a create element action for a context.
	 */
	public static final int CREATE_ELEMENT = 0x01 << 1;
	
	/**
	 * <code>PROMOTE</code> indicates that this describes a promote action for a context.
	 */
	public static final int PROMOTE = 0x01 << 2;
		
	/**
	 * <code>RECONTEXTUALIZE</code> indicates that this describes a recontextualize action for a context.
	 */
	public static final int RECONTEXTUALIZE = 0x01 << 3;
		
	private int type;
	private XArchTypeMetadata input;
	private XArchTypeMetadata output;

	public XArchActionMetadata(int type, XArchTypeMetadata input, XArchTypeMetadata output) {
		this.type = type;
		this.input = input;
		this.output = output;
	}
	
	/**
	 * Returns the type of this action.
	 * @return the type of this action
	 * @see #CREATE
	 * @see #CREATE_ELEMENT
	 * @see #PROMOTE
	 * @see #RECONTEXTUALIZE
	 */
	public int getType() {
		return type;
	}

	/**
	 * Returns metadata describing the input type used by this action, or <code>null</code> if it does not require one.
	 * @return metadata describing the input type used by this action, or <code>null</code> if it does not require one
	 */
	public XArchTypeMetadata getInputTypeMetadata() {
		return input;
	}
	
	/**
	 * Returns metadata describing the output type resulting from this action.
	 * @return metadata describing the output type resulting from this action
	 */
	public XArchTypeMetadata getOutputTypeMetadata() {
		return output;
	}
}
