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

import org.w3c.dom.*;

/**
 * Describes an xArch interface implementation
 * that is based on a DOM node.
 *
 * @author Eric M. Dashofy (edashofy@ics.uci.edu)
 */
public interface DOMBased{

	/**
	 * Gets the DOM node on which this implementation is based.
	 * @return DOM node
	 */
	public Node getDOMNode();
	
	/**
	 * Sets the DOM node on which this implementation is based.
	 * @param n DOM node
	 */
	public void setDOMNode(Node n);

}
