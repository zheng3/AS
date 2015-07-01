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

import java.util.Vector;
import org.w3c.dom.*;

/**
 * Simple implementation of the DOM <code>NodeList</code>
 * interface.
 *
 * @author Eric M. Dashofy (edashofy@ics.uci.edu)
 */
class SimpleNodeList implements org.w3c.dom.NodeList{

	protected Vector listContents = new Vector();
	
	public SimpleNodeList(){
	}
	
	public void addNode(Node n){
		listContents.addElement(n);
	}
	
	public void removeNode(Node n){
		listContents.removeElement(n);
	}
	
	public int getLength(){
		return listContents.size();
	}
	
	public Node item(int n){
		return (Node)listContents.elementAt(n);
	}

}
