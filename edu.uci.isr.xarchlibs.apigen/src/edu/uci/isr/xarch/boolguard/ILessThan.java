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
package edu.uci.isr.xarch.boolguard;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * LessThan <code>xsi:type</code> in the
 * boolguard namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface ILessThan extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"boolguard", "LessThan", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("symbol", "boolguard", "Symbol", 1, 1),
			XArchPropertyMetadata.createElement("symbol2", "boolguard", "Symbol", 1, 1),
			XArchPropertyMetadata.createElement("value", "boolguard", "Value", 1, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the symbol for this LessThan.
	 * @param value new symbol
	 */
	public void setSymbol(ISymbol value);

	/**
	 * Clear the symbol from this LessThan.
	 */
	public void clearSymbol();

	/**
	 * Get the symbol from this LessThan.
	 * @return symbol
	 */
	public ISymbol getSymbol();

	/**
	 * Determine if this LessThan has the given symbol
	 * @param symbolToCheck symbol to compare
	 * @return <code>true</code> if the symbols are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasSymbol(ISymbol symbolToCheck);

	/**
	 * Set the symbol2 for this LessThan.
	 * @param value new symbol2
	 */
	public void setSymbol2(ISymbol value);

	/**
	 * Clear the symbol2 from this LessThan.
	 */
	public void clearSymbol2();

	/**
	 * Get the symbol2 from this LessThan.
	 * @return symbol2
	 */
	public ISymbol getSymbol2();

	/**
	 * Determine if this LessThan has the given symbol2
	 * @param symbol2ToCheck symbol2 to compare
	 * @return <code>true</code> if the symbol2s are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasSymbol2(ISymbol symbol2ToCheck);

	/**
	 * Set the value for this LessThan.
	 * @param value new value
	 */
	public void setValue(IValue value);

	/**
	 * Clear the value from this LessThan.
	 */
	public void clearValue();

	/**
	 * Get the value from this LessThan.
	 * @return value
	 */
	public IValue getValue();

	/**
	 * Determine if this LessThan has the given value
	 * @param valueToCheck value to compare
	 * @return <code>true</code> if the values are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasValue(IValue valueToCheck);
	/**
	 * Determine if another LessThan is equivalent to this one, ignoring
	 * ID's.
	 * @param LessThanToCheck LessThan to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * LessThan are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(ILessThan LessThanToCheck);

}
