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
 * GreaterThanOrEquals <code>xsi:type</code> in the
 * boolguard namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface IGreaterThanOrEquals extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"boolguard", "GreaterThanOrEquals", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("symbol", "boolguard", "Symbol", 1, 1),
			XArchPropertyMetadata.createElement("symbol2", "boolguard", "Symbol", 1, 1),
			XArchPropertyMetadata.createElement("value", "boolguard", "Value", 1, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the symbol for this GreaterThanOrEquals.
	 * @param value new symbol
	 */
	public void setSymbol(ISymbol value);

	/**
	 * Clear the symbol from this GreaterThanOrEquals.
	 */
	public void clearSymbol();

	/**
	 * Get the symbol from this GreaterThanOrEquals.
	 * @return symbol
	 */
	public ISymbol getSymbol();

	/**
	 * Determine if this GreaterThanOrEquals has the given symbol
	 * @param symbolToCheck symbol to compare
	 * @return <code>true</code> if the symbols are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasSymbol(ISymbol symbolToCheck);

	/**
	 * Set the symbol2 for this GreaterThanOrEquals.
	 * @param value new symbol2
	 */
	public void setSymbol2(ISymbol value);

	/**
	 * Clear the symbol2 from this GreaterThanOrEquals.
	 */
	public void clearSymbol2();

	/**
	 * Get the symbol2 from this GreaterThanOrEquals.
	 * @return symbol2
	 */
	public ISymbol getSymbol2();

	/**
	 * Determine if this GreaterThanOrEquals has the given symbol2
	 * @param symbol2ToCheck symbol2 to compare
	 * @return <code>true</code> if the symbol2s are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasSymbol2(ISymbol symbol2ToCheck);

	/**
	 * Set the value for this GreaterThanOrEquals.
	 * @param value new value
	 */
	public void setValue(IValue value);

	/**
	 * Clear the value from this GreaterThanOrEquals.
	 */
	public void clearValue();

	/**
	 * Get the value from this GreaterThanOrEquals.
	 * @return value
	 */
	public IValue getValue();

	/**
	 * Determine if this GreaterThanOrEquals has the given value
	 * @param valueToCheck value to compare
	 * @return <code>true</code> if the values are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasValue(IValue valueToCheck);
	/**
	 * Determine if another GreaterThanOrEquals is equivalent to this one, ignoring
	 * ID's.
	 * @param GreaterThanOrEqualsToCheck GreaterThanOrEquals to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * GreaterThanOrEquals are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IGreaterThanOrEquals GreaterThanOrEqualsToCheck);

}
