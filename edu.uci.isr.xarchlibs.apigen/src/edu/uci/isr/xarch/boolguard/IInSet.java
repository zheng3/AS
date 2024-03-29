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
 * InSet <code>xsi:type</code> in the
 * boolguard namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface IInSet extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"boolguard", "InSet", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("symbol", "boolguard", "Symbol", 1, 1),
			XArchPropertyMetadata.createElement("value", "boolguard", "Value", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Set the symbol for this InSet.
	 * @param value new symbol
	 */
	public void setSymbol(ISymbol value);

	/**
	 * Clear the symbol from this InSet.
	 */
	public void clearSymbol();

	/**
	 * Get the symbol from this InSet.
	 * @return symbol
	 */
	public ISymbol getSymbol();

	/**
	 * Determine if this InSet has the given symbol
	 * @param symbolToCheck symbol to compare
	 * @return <code>true</code> if the symbols are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasSymbol(ISymbol symbolToCheck);

	/**
	 * Add a value to this InSet.
	 * @param newValue value to add.
	 */
	public void addValue(IValue newValue);

	/**
	 * Add a collection of values to this InSet.
	 * @param values values to add.
	 */
	public void addValues(Collection values);

	/**
	 * Remove all values from this InSet.
	 */
	public void clearValues();

	/**
	 * Remove the given value from this InSet.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param valueToRemove value to remove.
	 */
	public void removeValue(IValue valueToRemove);

	/**
	 * Remove all the given values from this InSet.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param values value to remove.
	 */
	public void removeValues(Collection values);

	/**
	 * Get all the values from this InSet.
	 * @return all values in this InSet.
	 */
	public Collection getAllValues();

	/**
	 * Determine if this InSet contains a given value.
	 * @return <code>true</code> if this InSet contains the given
	 * valueToCheck, <code>false</code> otherwise.
	 */
	public boolean hasValue(IValue valueToCheck);

	/**
	 * Determine if this InSet contains the given set of values.
	 * @param valuesToCheck values to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>values</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasValues(Collection valuesToCheck);

	/**
	 * Determine if this InSet contains each element in the 
	 * given set of values.
	 * @param valuesToCheck values to check for.
	 * @return <code>true</code> if every element in
	 * <code>values</code> is found in this InSet,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllValues(Collection valuesToCheck);

	/**
	 * Determine if another InSet is equivalent to this one, ignoring
	 * ID's.
	 * @param InSetToCheck InSet to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * InSet are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IInSet InSetToCheck);

}
