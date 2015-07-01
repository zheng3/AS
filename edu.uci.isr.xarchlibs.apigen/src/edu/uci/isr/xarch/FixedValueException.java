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
 * Thrown when a user makes a set, clear, or other call that
 * attempts to change the value of a fixed-value attribute
 * or element.
 * @author Eric M. Dashofy <a href="mailto:edashofy@ics.uci.edu">edashofy@ics.uci.edu</a>
 */
public class FixedValueException extends RuntimeException{

	private String fieldName;
	private String fieldValue;

	/**
	 * Create a new <code>FixedValueException</code> with the name
	 * of the attribute or element given.
	 * @param fieldName The name of the element or attribute with the fixed value.
	 * @param fieldValue The fixed field value.
	 */
	public FixedValueException(String fieldName, String fieldValue){
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public String getFieldName(){
		return fieldName;
	}

	public String getFieldValue(){
		return fieldValue;
	}

	public String toString(){
		return "The field \"" + fieldName + "\" has a fixed value \"" + fieldValue +
			"\" and cannot be set to anything else.";
	}
}
