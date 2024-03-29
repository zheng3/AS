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
package edu.uci.isr.xarch.uml212superstructure;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * UML212 <code>xsi:type</code> in the
 * uml212superstructure namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface IUML212 extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"uml212superstructure", "UML212", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("id", "instance", "Identifier", null, null),
			XArchPropertyMetadata.createElement("description", "instance", "Description", 1, 1),
			XArchPropertyMetadata.createElement("useCaseDiagram", "uml212superstructure", "UseCase", 0, XArchPropertyMetadata.UNBOUNDED),
			XArchPropertyMetadata.createElement("activityDiagram", "uml212superstructure", "ActivityDiagram", 0, XArchPropertyMetadata.UNBOUNDED),
			XArchPropertyMetadata.createElement("actor", "uml212superstructure", "Actor", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Set the id attribute on this UML212.
	 * @param id id
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setId(String id);

	/**
	 * Remove the id attribute from this UML212.
	 */
	public void clearId();

	/**
	 * Get the id attribute from this UML212.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return id on this UML212
	 */
	public String getId();

	/**
	 * Determine if the id attribute on this UML212
	 * has the given value.
	 * @param id Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasId(String id);


	/**
	 * Set the description for this UML212.
	 * @param value new description
	 */
	public void setDescription(edu.uci.isr.xarch.instance.IDescription value);

	/**
	 * Clear the description from this UML212.
	 */
	public void clearDescription();

	/**
	 * Get the description from this UML212.
	 * @return description
	 */
	public edu.uci.isr.xarch.instance.IDescription getDescription();

	/**
	 * Determine if this UML212 has the given description
	 * @param descriptionToCheck description to compare
	 * @return <code>true</code> if the descriptions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasDescription(edu.uci.isr.xarch.instance.IDescription descriptionToCheck);

	/**
	 * Add a useCaseDiagram to this UML212.
	 * @param newUseCaseDiagram useCaseDiagram to add.
	 */
	public void addUseCaseDiagram(IUseCase newUseCaseDiagram);

	/**
	 * Add a collection of useCaseDiagrams to this UML212.
	 * @param useCaseDiagrams useCaseDiagrams to add.
	 */
	public void addUseCaseDiagrams(Collection useCaseDiagrams);

	/**
	 * Remove all useCaseDiagrams from this UML212.
	 */
	public void clearUseCaseDiagrams();

	/**
	 * Remove the given useCaseDiagram from this UML212.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param useCaseDiagramToRemove useCaseDiagram to remove.
	 */
	public void removeUseCaseDiagram(IUseCase useCaseDiagramToRemove);

	/**
	 * Remove all the given useCaseDiagrams from this UML212.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param useCaseDiagrams useCaseDiagram to remove.
	 */
	public void removeUseCaseDiagrams(Collection useCaseDiagrams);

	/**
	 * Get all the useCaseDiagrams from this UML212.
	 * @return all useCaseDiagrams in this UML212.
	 */
	public Collection getAllUseCaseDiagrams();

	/**
	 * Determine if this UML212 contains a given useCaseDiagram.
	 * @return <code>true</code> if this UML212 contains the given
	 * useCaseDiagramToCheck, <code>false</code> otherwise.
	 */
	public boolean hasUseCaseDiagram(IUseCase useCaseDiagramToCheck);

	/**
	 * Determine if this UML212 contains the given set of useCaseDiagrams.
	 * @param useCaseDiagramsToCheck useCaseDiagrams to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>useCaseDiagrams</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasUseCaseDiagrams(Collection useCaseDiagramsToCheck);

	/**
	 * Determine if this UML212 contains each element in the 
	 * given set of useCaseDiagrams.
	 * @param useCaseDiagramsToCheck useCaseDiagrams to check for.
	 * @return <code>true</code> if every element in
	 * <code>useCaseDiagrams</code> is found in this UML212,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllUseCaseDiagrams(Collection useCaseDiagramsToCheck);

	/**
	 * Gets the useCaseDiagram from this UML212 with the given
	 * id.
	 * @param id ID to look for.
	 * @return useCaseDiagram with the given ID, or <code>null</code> if not found.
	 */
	public IUseCase getUseCaseDiagram(String id);

	/**
	 * Gets the useCaseDiagrams from this UML212 with the given
	 * ids.
	 * @param ids ID to look for.
	 * @return useCaseDiagrams with the given IDs.  If an element with a given
	 * ID was not found, that ID is ignored.
	 */
	public Collection getUseCaseDiagrams(Collection ids);


	/**
	 * Add a activityDiagram to this UML212.
	 * @param newActivityDiagram activityDiagram to add.
	 */
	public void addActivityDiagram(IActivityDiagram newActivityDiagram);

	/**
	 * Add a collection of activityDiagrams to this UML212.
	 * @param activityDiagrams activityDiagrams to add.
	 */
	public void addActivityDiagrams(Collection activityDiagrams);

	/**
	 * Remove all activityDiagrams from this UML212.
	 */
	public void clearActivityDiagrams();

	/**
	 * Remove the given activityDiagram from this UML212.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param activityDiagramToRemove activityDiagram to remove.
	 */
	public void removeActivityDiagram(IActivityDiagram activityDiagramToRemove);

	/**
	 * Remove all the given activityDiagrams from this UML212.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param activityDiagrams activityDiagram to remove.
	 */
	public void removeActivityDiagrams(Collection activityDiagrams);

	/**
	 * Get all the activityDiagrams from this UML212.
	 * @return all activityDiagrams in this UML212.
	 */
	public Collection getAllActivityDiagrams();

	/**
	 * Determine if this UML212 contains a given activityDiagram.
	 * @return <code>true</code> if this UML212 contains the given
	 * activityDiagramToCheck, <code>false</code> otherwise.
	 */
	public boolean hasActivityDiagram(IActivityDiagram activityDiagramToCheck);

	/**
	 * Determine if this UML212 contains the given set of activityDiagrams.
	 * @param activityDiagramsToCheck activityDiagrams to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>activityDiagrams</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasActivityDiagrams(Collection activityDiagramsToCheck);

	/**
	 * Determine if this UML212 contains each element in the 
	 * given set of activityDiagrams.
	 * @param activityDiagramsToCheck activityDiagrams to check for.
	 * @return <code>true</code> if every element in
	 * <code>activityDiagrams</code> is found in this UML212,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllActivityDiagrams(Collection activityDiagramsToCheck);

	/**
	 * Gets the activityDiagram from this UML212 with the given
	 * id.
	 * @param id ID to look for.
	 * @return activityDiagram with the given ID, or <code>null</code> if not found.
	 */
	public IActivityDiagram getActivityDiagram(String id);

	/**
	 * Gets the activityDiagrams from this UML212 with the given
	 * ids.
	 * @param ids ID to look for.
	 * @return activityDiagrams with the given IDs.  If an element with a given
	 * ID was not found, that ID is ignored.
	 */
	public Collection getActivityDiagrams(Collection ids);


	/**
	 * Add a actor to this UML212.
	 * @param newActor actor to add.
	 */
	public void addActor(IActor newActor);

	/**
	 * Add a collection of actors to this UML212.
	 * @param actors actors to add.
	 */
	public void addActors(Collection actors);

	/**
	 * Remove all actors from this UML212.
	 */
	public void clearActors();

	/**
	 * Remove the given actor from this UML212.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param actorToRemove actor to remove.
	 */
	public void removeActor(IActor actorToRemove);

	/**
	 * Remove all the given actors from this UML212.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param actors actor to remove.
	 */
	public void removeActors(Collection actors);

	/**
	 * Get all the actors from this UML212.
	 * @return all actors in this UML212.
	 */
	public Collection getAllActors();

	/**
	 * Determine if this UML212 contains a given actor.
	 * @return <code>true</code> if this UML212 contains the given
	 * actorToCheck, <code>false</code> otherwise.
	 */
	public boolean hasActor(IActor actorToCheck);

	/**
	 * Determine if this UML212 contains the given set of actors.
	 * @param actorsToCheck actors to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>actors</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasActors(Collection actorsToCheck);

	/**
	 * Determine if this UML212 contains each element in the 
	 * given set of actors.
	 * @param actorsToCheck actors to check for.
	 * @return <code>true</code> if every element in
	 * <code>actors</code> is found in this UML212,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllActors(Collection actorsToCheck);

	/**
	 * Gets the actor from this UML212 with the given
	 * id.
	 * @param id ID to look for.
	 * @return actor with the given ID, or <code>null</code> if not found.
	 */
	public IActor getActor(String id);

	/**
	 * Gets the actors from this UML212 with the given
	 * ids.
	 * @param ids ID to look for.
	 * @return actors with the given IDs.  If an element with a given
	 * ID was not found, that ID is ignored.
	 */
	public Collection getActors(Collection ids);

	/**
	 * Determine if another UML212 has the same
	 * id as this one.
	 * @param UML212ToCheck UML212 to compare with this
	 * one.
	 */
	public boolean isEqual(IUML212 UML212ToCheck);
	/**
	 * Determine if another UML212 is equivalent to this one, ignoring
	 * ID's.
	 * @param UML212ToCheck UML212 to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * UML212 are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IUML212 UML212ToCheck);

}
