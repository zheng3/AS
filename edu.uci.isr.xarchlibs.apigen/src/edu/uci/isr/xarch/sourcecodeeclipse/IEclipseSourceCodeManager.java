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
package edu.uci.isr.xarch.sourcecodeeclipse;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * EclipseSourceCodeManager <code>xsi:type</code> in the
 * sourcecodeeclipse namespace.  Extends and
 * inherits the properties of the
 * JavaSourceCodeManager <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IEclipseSourceCodeManager extends edu.uci.isr.xarch.javasourcecode.IJavaSourceCodeManager, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"sourcecodeeclipse", "EclipseSourceCodeManager", edu.uci.isr.xarch.javasourcecode.IJavaSourceCodeManager.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("eclipseProjectName", "sourcecodeeclipse", "EclipseProjectName", 0, 1),
			XArchPropertyMetadata.createElement("eclipseProjectId", "sourcecodeeclipse", "EclipseProjectId", 1, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the eclipseProjectName for this EclipseSourceCodeManager.
	 * @param value new eclipseProjectName
	 */
	public void setEclipseProjectName(IEclipseProjectName value);

	/**
	 * Clear the eclipseProjectName from this EclipseSourceCodeManager.
	 */
	public void clearEclipseProjectName();

	/**
	 * Get the eclipseProjectName from this EclipseSourceCodeManager.
	 * @return eclipseProjectName
	 */
	public IEclipseProjectName getEclipseProjectName();

	/**
	 * Determine if this EclipseSourceCodeManager has the given eclipseProjectName
	 * @param eclipseProjectNameToCheck eclipseProjectName to compare
	 * @return <code>true</code> if the eclipseProjectNames are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasEclipseProjectName(IEclipseProjectName eclipseProjectNameToCheck);

	/**
	 * Set the eclipseProjectId for this EclipseSourceCodeManager.
	 * @param value new eclipseProjectId
	 */
	public void setEclipseProjectId(IEclipseProjectId value);

	/**
	 * Clear the eclipseProjectId from this EclipseSourceCodeManager.
	 */
	public void clearEclipseProjectId();

	/**
	 * Get the eclipseProjectId from this EclipseSourceCodeManager.
	 * @return eclipseProjectId
	 */
	public IEclipseProjectId getEclipseProjectId();

	/**
	 * Determine if this EclipseSourceCodeManager has the given eclipseProjectId
	 * @param eclipseProjectIdToCheck eclipseProjectId to compare
	 * @return <code>true</code> if the eclipseProjectIds are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasEclipseProjectId(IEclipseProjectId eclipseProjectIdToCheck);
	/**
	 * Determine if another EclipseSourceCodeManager is equivalent to this one, ignoring
	 * ID's.
	 * @param EclipseSourceCodeManagerToCheck EclipseSourceCodeManager to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * EclipseSourceCodeManager are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IEclipseSourceCodeManager EclipseSourceCodeManagerToCheck);

}
