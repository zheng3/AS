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
package edu.uci.isr.xarch.tronanalysis;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * TronAnalysis <code>xsi:type</code> in the
 * tronanalysis namespace.  Extends and
 * inherits the properties of the
 * Analysis <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface ITronAnalysis extends edu.uci.isr.xarch.analysis.IAnalysis, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"tronanalysis", "TronAnalysis", edu.uci.isr.xarch.analysis.IAnalysis.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("id", "instance", "Identifier", null, null),
			XArchPropertyMetadata.createElement("description", "instance", "Description", 1, 1),
			XArchPropertyMetadata.createElement("test", "tronanalysis", "Test", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Set the id attribute on this TronAnalysis.
	 * @param id id
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setId(String id);

	/**
	 * Remove the id attribute from this TronAnalysis.
	 */
	public void clearId();

	/**
	 * Get the id attribute from this TronAnalysis.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return id on this TronAnalysis
	 */
	public String getId();

	/**
	 * Determine if the id attribute on this TronAnalysis
	 * has the given value.
	 * @param id Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasId(String id);


	/**
	 * Set the description for this TronAnalysis.
	 * @param value new description
	 */
	public void setDescription(edu.uci.isr.xarch.instance.IDescription value);

	/**
	 * Clear the description from this TronAnalysis.
	 */
	public void clearDescription();

	/**
	 * Get the description from this TronAnalysis.
	 * @return description
	 */
	public edu.uci.isr.xarch.instance.IDescription getDescription();

	/**
	 * Determine if this TronAnalysis has the given description
	 * @param descriptionToCheck description to compare
	 * @return <code>true</code> if the descriptions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasDescription(edu.uci.isr.xarch.instance.IDescription descriptionToCheck);

	/**
	 * Add a test to this TronAnalysis.
	 * @param newTest test to add.
	 */
	public void addTest(ITest newTest);

	/**
	 * Add a collection of tests to this TronAnalysis.
	 * @param tests tests to add.
	 */
	public void addTests(Collection tests);

	/**
	 * Remove all tests from this TronAnalysis.
	 */
	public void clearTests();

	/**
	 * Remove the given test from this TronAnalysis.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param testToRemove test to remove.
	 */
	public void removeTest(ITest testToRemove);

	/**
	 * Remove all the given tests from this TronAnalysis.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param tests test to remove.
	 */
	public void removeTests(Collection tests);

	/**
	 * Get all the tests from this TronAnalysis.
	 * @return all tests in this TronAnalysis.
	 */
	public Collection getAllTests();

	/**
	 * Determine if this TronAnalysis contains a given test.
	 * @return <code>true</code> if this TronAnalysis contains the given
	 * testToCheck, <code>false</code> otherwise.
	 */
	public boolean hasTest(ITest testToCheck);

	/**
	 * Determine if this TronAnalysis contains the given set of tests.
	 * @param testsToCheck tests to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>tests</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasTests(Collection testsToCheck);

	/**
	 * Determine if this TronAnalysis contains each element in the 
	 * given set of tests.
	 * @param testsToCheck tests to check for.
	 * @return <code>true</code> if every element in
	 * <code>tests</code> is found in this TronAnalysis,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllTests(Collection testsToCheck);

	/**
	 * Gets the test from this TronAnalysis with the given
	 * id.
	 * @param id ID to look for.
	 * @return test with the given ID, or <code>null</code> if not found.
	 */
	public ITest getTest(String id);

	/**
	 * Gets the tests from this TronAnalysis with the given
	 * ids.
	 * @param ids ID to look for.
	 * @return tests with the given IDs.  If an element with a given
	 * ID was not found, that ID is ignored.
	 */
	public Collection getTests(Collection ids);

	/**
	 * Determine if another TronAnalysis is equivalent to this one, ignoring
	 * ID's.
	 * @param TronAnalysisToCheck TronAnalysis to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * TronAnalysis are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(ITronAnalysis TronAnalysisToCheck);

}
