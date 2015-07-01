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
 * Useful constants that persist across the xArch API implementation.
 *
 * @author Eric M. Dashofy (edashofy@ics.uci.edu)
 */
public class XArchConstants{

	/** Namespace URI for XLink */
	public static final String XLINK_NS_URI = "http://www.w3.org/1999/xlink";
	
	/** Namespace URI for XML Namespace */
	public static final String XMLNS_NS_URI = "http://www.w3.org/2000/xmlns/";

	/** Namespace URI for XML Schema (XSD) */
	public static final String XSD_NS_URI = "http://www.w3.org/2001/XMLSchema";
	
	/** Namespace URI for XML Schema Instance (XSI) */
	public static final String XSI_NS_URI = "http://www.w3.org/2001/XMLSchema-instance";
	
	/** Old (2000) Namespace URI for XML Schema (XSD) */
	public static final String OLD_XSD_NS_URI = "http://www.w3.org/2000/10/XMLSchema";
	
	/** Old (2000) Namespace URI for XML Schema Instance (XSI) */
	public static final String OLD_XSI_NS_URI = "http://www.w3.org/2000/10/XMLSchema-instance";
	
	/** Name of xsi:schemaLocation attribute */
	public static final String SCHEMA_LOCATION_ATTR_NAME = "schemaLocation";

	/** Namespace URI for XSD */
	public static final String NS_URI = XSD_NS_URI;

}

