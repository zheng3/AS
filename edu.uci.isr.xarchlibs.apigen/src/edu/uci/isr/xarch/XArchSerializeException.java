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
 * This exception is thrown when serializing an xArch-based document to
 * a persistent representation fails.  In general, the chained exception
 * (e.g., from the real serializer) contains the actual failure data.
 * 
 * @author Eric M. Dashofy <a href="mailto:edashofy@ics.uci.edu">edashofy@ics.uci.edu</a>
 */
public class XArchSerializeException extends Exception{

	/**
	 * Create a new xArch serialization exception with no explanation and no
	 * chained exception.
	 */
	public XArchSerializeException() {
		super();
	}
	
	/**
	 * Create a new xArch serialization exception with the given explanation
	 * @param description Explanation for the serialization failure.
	 */
	public XArchSerializeException(String description){
		super(description);
	}

	/**
	 * Create a new xArch serialization exception based on the given error
	 * that caused this exception.
	 * @param t The underlying error that caused this exception.
	 */
	public XArchSerializeException(Throwable t){
		super(t);
	}

	/**
	 * Create a new xArch serialization exception based on an explanation and
	 * the given error that caused this exception.
	 * @param description Explanation for the serialization failure.
	 * @param t The underlying error that caused this exception.
	 */
	public XArchSerializeException(String description, Throwable t){
		super(description, t);
	}

}
