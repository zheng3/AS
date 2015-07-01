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

import java.util.*;

/**
 * Describes an order of child elements for a parent element,
 * which can be used for sorting children.
 * 
 * @author Eric M. Dashofy (edashofy@ics.uci.edu)
 */
public class SequenceOrder{

	/** Qualified names in the sequence. */
	protected Vector qNames = new Vector();
	
	/**
	 * Creates an empty sequence order.
	 */
	public SequenceOrder(){
	}
	
	/**
	 * Creates a sequence order of size 1 with the given
	 * qualified name.
	 * @param name QName to put in the sequence.
	 */
	public SequenceOrder(QName name){
		qNames.addElement(name);
	}
	
	/**
	 * Creates a sequence order from an array of qualified
	 * names.
	 * @param names QNames, in order, to put in the sequence.
	 */
	public SequenceOrder(QName[] names){
		for(int i = 0; i < names.length; i++){
			qNames.addElement(names[i]);
		}
	}
	
	/**
	 * Creates a sequence order from another sequence order.
	 * @param baseOrder Order to copy into this one.
	 */
	public SequenceOrder(SequenceOrder baseOrder){
		this(baseOrder.getNames());
	}
	
	/**
	 * Creates a sequence order from the concatenation of a base
	 * order and an appendix order.
	 * @param baseOrder Start of sequence
	 * @param appendedOrder Sequence to be appended to baseOrder.
	 */
	public SequenceOrder(SequenceOrder baseOrder, SequenceOrder appendedOrder){
		this(baseOrder, appendedOrder.getNames());
	}
	
	/**
	 * Creates a sequence order from the concatenation of a base
	 * order and a single additional name.
	 * @param baseOrder Start of sequence
	 * @param name Name to append to baseOrder
	 */
	public SequenceOrder(SequenceOrder baseOrder, QName name){
		this(baseOrder.getNames());
		addName(name);
	}
	
	/**
	 * Creates a sequence order from the concatenation of a base
	 * order and an array of additional names.
	 * @param baseOrder Start of sequence
	 * @param names Names to append to baseOrder
	 */
	public SequenceOrder(SequenceOrder baseOrder, QName[] names){
		this(baseOrder.getNames());
		addNames(names);
	}
	
	/**
	 * Returns the size of this sequence order.
	 * @return size of the sequence order.
	 */
	public int size(){
		return qNames.size();
	}

	/** 
	 * Adds a name to the end of the sequence order.
	 * @param name Name to add.
	 */
	public void addName(QName name){
		qNames.addElement(name);
	}
	
	/** 
	 * Adds a set of names to the end of the sequence order
	 * @param names Names to add.
	 */
	public void addNames(QName[] names){
		for(int i = 0; i < names.length; i++){
			qNames.addElement(names[i]);
		}
	}
	
	/**
	 * Gets an element of the sequence order at the
	 * given position in the order.
	 * @param index Index of order at which to retrieve element.
	 * @return Name at position <code>index</code> in order.
	 */
	public QName getName(int index){
		return (QName)qNames.elementAt(index);
	}
	
	/**
	 * Gets this sequence order as an array of names.
	 * @return Names in this sequence order.
	 */
	public QName[] getNames(){
		QName[] names = new QName[size()];
		qNames.copyInto(names);
		return names;
	}
	
	/**
	 * Removes the given name from the sequence order.
	 * @param name Name to remove.
	 */
	public void removeName(QName name){
		qNames.removeElement(name);
	}
	
	/**
	 * Return an enumeration of the elements in this sequence order.
	 * @return Enumeration of elements in this sequence order.
	 */
	public Enumeration elements(){
		return qNames.elements();
	}

}
