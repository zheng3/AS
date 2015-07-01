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
package edu.uci.isr.xarch.changes;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * ComponentChange <code>xsi:type</code> in the
 * changes namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface IComponentChange extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"changes", "ComponentChange", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("id", "instance", "Identifier", null, null),
			XArchPropertyMetadata.createAttribute("type", "changes", "ChangeType", null, null),
			XArchPropertyMetadata.createElement("description", "instance", "Description", 1, 1),
			XArchPropertyMetadata.createElement("component", "instance", "XMLLink", 1, 1),
			XArchPropertyMetadata.createElement("copyOfRemovedComponent", "types", "Component", 0, 1),
			XArchPropertyMetadata.createElement("interfaceChange", "changes", "InterfaceChange", 0, 1),
			XArchPropertyMetadata.createElement("propertyChange", "changes", "PropertyChange", 0, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the id attribute on this ComponentChange.
	 * @param id id
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setId(String id);

	/**
	 * Remove the id attribute from this ComponentChange.
	 */
	public void clearId();

	/**
	 * Get the id attribute from this ComponentChange.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return id on this ComponentChange
	 */
	public String getId();

	/**
	 * Determine if the id attribute on this ComponentChange
	 * has the given value.
	 * @param id Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasId(String id);


	/**
	 * Set the type attribute on this ComponentChange.
	 * @param type type
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setType(String type);

	/**
	 * Remove the type attribute from this ComponentChange.
	 */
	public void clearType();

	/**
	 * Get the type attribute from this ComponentChange.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return type on this ComponentChange
	 */
	public String getType();

	/**
	 * Determine if the type attribute on this ComponentChange
	 * has the given value.
	 * @param type Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasType(String type);


	/**
	 * Set the description for this ComponentChange.
	 * @param value new description
	 */
	public void setDescription(edu.uci.isr.xarch.instance.IDescription value);

	/**
	 * Clear the description from this ComponentChange.
	 */
	public void clearDescription();

	/**
	 * Get the description from this ComponentChange.
	 * @return description
	 */
	public edu.uci.isr.xarch.instance.IDescription getDescription();

	/**
	 * Determine if this ComponentChange has the given description
	 * @param descriptionToCheck description to compare
	 * @return <code>true</code> if the descriptions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasDescription(edu.uci.isr.xarch.instance.IDescription descriptionToCheck);

	/**
	 * Set the component for this ComponentChange.
	 * @param value new component
	 */
	public void setComponent(edu.uci.isr.xarch.instance.IXMLLink value);

	/**
	 * Clear the component from this ComponentChange.
	 */
	public void clearComponent();

	/**
	 * Get the component from this ComponentChange.
	 * @return component
	 */
	public edu.uci.isr.xarch.instance.IXMLLink getComponent();

	/**
	 * Determine if this ComponentChange has the given component
	 * @param componentToCheck component to compare
	 * @return <code>true</code> if the components are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasComponent(edu.uci.isr.xarch.instance.IXMLLink componentToCheck);

	/**
	 * Set the copyOfRemovedComponent for this ComponentChange.
	 * @param value new copyOfRemovedComponent
	 */
	public void setCopyOfRemovedComponent(edu.uci.isr.xarch.types.IComponent value);

	/**
	 * Clear the copyOfRemovedComponent from this ComponentChange.
	 */
	public void clearCopyOfRemovedComponent();

	/**
	 * Get the copyOfRemovedComponent from this ComponentChange.
	 * @return copyOfRemovedComponent
	 */
	public edu.uci.isr.xarch.types.IComponent getCopyOfRemovedComponent();

	/**
	 * Determine if this ComponentChange has the given copyOfRemovedComponent
	 * @param copyOfRemovedComponentToCheck copyOfRemovedComponent to compare
	 * @return <code>true</code> if the copyOfRemovedComponents are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasCopyOfRemovedComponent(edu.uci.isr.xarch.types.IComponent copyOfRemovedComponentToCheck);

	/**
	 * Set the interfaceChange for this ComponentChange.
	 * @param value new interfaceChange
	 */
	public void setInterfaceChange(IInterfaceChange value);

	/**
	 * Clear the interfaceChange from this ComponentChange.
	 */
	public void clearInterfaceChange();

	/**
	 * Get the interfaceChange from this ComponentChange.
	 * @return interfaceChange
	 */
	public IInterfaceChange getInterfaceChange();

	/**
	 * Determine if this ComponentChange has the given interfaceChange
	 * @param interfaceChangeToCheck interfaceChange to compare
	 * @return <code>true</code> if the interfaceChanges are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasInterfaceChange(IInterfaceChange interfaceChangeToCheck);

	/**
	 * Set the propertyChange for this ComponentChange.
	 * @param value new propertyChange
	 */
	public void setPropertyChange(IPropertyChange value);

	/**
	 * Clear the propertyChange from this ComponentChange.
	 */
	public void clearPropertyChange();

	/**
	 * Get the propertyChange from this ComponentChange.
	 * @return propertyChange
	 */
	public IPropertyChange getPropertyChange();

	/**
	 * Determine if this ComponentChange has the given propertyChange
	 * @param propertyChangeToCheck propertyChange to compare
	 * @return <code>true</code> if the propertyChanges are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasPropertyChange(IPropertyChange propertyChangeToCheck);
	/**
	 * Determine if another ComponentChange has the same
	 * id as this one.
	 * @param ComponentChangeToCheck ComponentChange to compare with this
	 * one.
	 */
	public boolean isEqual(IComponentChange ComponentChangeToCheck);
	/**
	 * Determine if another ComponentChange is equivalent to this one, ignoring
	 * ID's.
	 * @param ComponentChangeToCheck ComponentChange to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * ComponentChange are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IComponentChange ComponentChangeToCheck);

}
