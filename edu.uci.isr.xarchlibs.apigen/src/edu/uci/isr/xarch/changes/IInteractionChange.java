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
 * InteractionChange <code>xsi:type</code> in the
 * changes namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface IInteractionChange extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"changes", "InteractionChange", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("id", "instance", "Identifier", null, null),
			XArchPropertyMetadata.createAttribute("type", "changes", "ChangeType", null, null),
			XArchPropertyMetadata.createElement("description", "instance", "Description", 1, 1),
			XArchPropertyMetadata.createElement("interaction", "instance", "XMLLink", 1, 1),
			XArchPropertyMetadata.createElement("copyOfRemovedInteraction", "interactions", "Interaction", 0, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the id attribute on this InteractionChange.
	 * @param id id
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setId(String id);

	/**
	 * Remove the id attribute from this InteractionChange.
	 */
	public void clearId();

	/**
	 * Get the id attribute from this InteractionChange.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return id on this InteractionChange
	 */
	public String getId();

	/**
	 * Determine if the id attribute on this InteractionChange
	 * has the given value.
	 * @param id Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasId(String id);


	/**
	 * Set the type attribute on this InteractionChange.
	 * @param type type
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setType(String type);

	/**
	 * Remove the type attribute from this InteractionChange.
	 */
	public void clearType();

	/**
	 * Get the type attribute from this InteractionChange.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return type on this InteractionChange
	 */
	public String getType();

	/**
	 * Determine if the type attribute on this InteractionChange
	 * has the given value.
	 * @param type Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasType(String type);


	/**
	 * Set the description for this InteractionChange.
	 * @param value new description
	 */
	public void setDescription(edu.uci.isr.xarch.instance.IDescription value);

	/**
	 * Clear the description from this InteractionChange.
	 */
	public void clearDescription();

	/**
	 * Get the description from this InteractionChange.
	 * @return description
	 */
	public edu.uci.isr.xarch.instance.IDescription getDescription();

	/**
	 * Determine if this InteractionChange has the given description
	 * @param descriptionToCheck description to compare
	 * @return <code>true</code> if the descriptions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasDescription(edu.uci.isr.xarch.instance.IDescription descriptionToCheck);

	/**
	 * Set the interaction for this InteractionChange.
	 * @param value new interaction
	 */
	public void setInteraction(edu.uci.isr.xarch.instance.IXMLLink value);

	/**
	 * Clear the interaction from this InteractionChange.
	 */
	public void clearInteraction();

	/**
	 * Get the interaction from this InteractionChange.
	 * @return interaction
	 */
	public edu.uci.isr.xarch.instance.IXMLLink getInteraction();

	/**
	 * Determine if this InteractionChange has the given interaction
	 * @param interactionToCheck interaction to compare
	 * @return <code>true</code> if the interactions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasInteraction(edu.uci.isr.xarch.instance.IXMLLink interactionToCheck);

	/**
	 * Set the copyOfRemovedInteraction for this InteractionChange.
	 * @param value new copyOfRemovedInteraction
	 */
	public void setCopyOfRemovedInteraction(edu.uci.isr.xarch.interactions.IInteraction value);

	/**
	 * Clear the copyOfRemovedInteraction from this InteractionChange.
	 */
	public void clearCopyOfRemovedInteraction();

	/**
	 * Get the copyOfRemovedInteraction from this InteractionChange.
	 * @return copyOfRemovedInteraction
	 */
	public edu.uci.isr.xarch.interactions.IInteraction getCopyOfRemovedInteraction();

	/**
	 * Determine if this InteractionChange has the given copyOfRemovedInteraction
	 * @param copyOfRemovedInteractionToCheck copyOfRemovedInteraction to compare
	 * @return <code>true</code> if the copyOfRemovedInteractions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasCopyOfRemovedInteraction(edu.uci.isr.xarch.interactions.IInteraction copyOfRemovedInteractionToCheck);
	/**
	 * Determine if another InteractionChange has the same
	 * id as this one.
	 * @param InteractionChangeToCheck InteractionChange to compare with this
	 * one.
	 */
	public boolean isEqual(IInteractionChange InteractionChangeToCheck);
	/**
	 * Determine if another InteractionChange is equivalent to this one, ignoring
	 * ID's.
	 * @param InteractionChangeToCheck InteractionChange to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * InteractionChange are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IInteractionChange InteractionChangeToCheck);

}
