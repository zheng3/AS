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

import java.util.Iterator;

public abstract class FilterIterator implements Iterator {
	
	private static final Object UNINIT = new Object();
	
	private Iterator i;
	private Object next;
	
	public FilterIterator(Iterator i) {
		this.i = i;
		this.next = UNINIT;
	}
	
	public boolean hasNext() {
		if(next == UNINIT)
			advance();
		return next != null;
	}
	
	public Object next() {
		if(next == UNINIT)
			advance();
		Object o = next;
		advance();
		return o;
	}
	
	public void remove() {
		throw new UnsupportedOperationException(); 
	}
	
	private void advance(){
		while(i.hasNext()){
			next = i.next();
			if(include(next))
				return;
		}
		next = null;
	}
	
	public abstract boolean include(Object o);
}
