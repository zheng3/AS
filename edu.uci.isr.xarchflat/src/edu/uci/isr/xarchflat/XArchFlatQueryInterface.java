package edu.uci.isr.xarchflat;

public interface XArchFlatQueryInterface{

	/**
	 * Creates a context object that can be used for creating, recontextualizing,
	 * and promoting elements in that context (XML namespace).
	 * Roughly equivalent to: new [contextType]Context(iXArch);
	 * 
	 * @param xArchObject Reference to an XArch object to contextualize.
	 * @param contextType The name of the context to create.  If the context object
	 * desired is, say, <CODE>TypesContext</CODE> then the <CODE>contextType</CODE>
	 * parameter will be <CODE>"types"</CODE>.
	 * @return Reference to a new context object of the given type.
	 */
	public ObjRef createContext(ObjRef xArchObject, String contextType);

	/**
	 * Gets all already-opened IXArch URLs.
	 * @deprecated Use getOpenXArchURIs() instead.
	 * @return Array of URL strings that represent open xArches.
	 */
	public String[] getOpenXArchURLs();

	/**
	 * Gets all already-opened IXArch URIs.
	 * @return Array of URI strings that represent open xArches.
	 */
	public String[] getOpenXArchURIs();
	
	/**
	 * Gets the top-level elements for all already-opened documents.
	 * @return array of ObjRefs referring to top-level elements of open xArches.
	 */
	public ObjRef[] getOpenXArches();
	
	/**
	 * Gets a reference to an already-open IXArch given its URI.
	 * @param uri URI of the already-open IXArch to get.
	 * @return <CODE>ObjRef</CODE> referring to the open IXArch, or
	 * <CODE>null</CODE> if the URI does not refer to an open xArch.
	 */
	public ObjRef getOpenXArch(String uri);

	/**
	 * Gets the URL of an open IXArch object.
	 * @param xArchRef An ObjRef to an IXArch object.
	 * @return URL of the xArch document, or <CODE>null</CODE> if one
	 * was not found.
	 * @deprecated Use getXArchURI() instead.
	 */
	public String getXArchURL(ObjRef xArchRef);
	
	/**
	 * Gets the URI of an open IXArch object.
	 * @param xArchRef An ObjRef to an IXArch object.
	 * @return URI of the xArch document, or <CODE>null</CODE> if one
	 * was not found.
	 */
	public String getXArchURI(ObjRef xArchRef);
	
	/**
	 * Determines if a given ObjRef is valid, that is, if there
	 * is an object associated with it.
	 * @param ref ObjRef to check.
	 * @return <CODE>true</CODE> if there is an associated object,
	 * <CODE>false</CODE> otherwise.
	 */
	public boolean isValidObjRef(ObjRef ref);

	/**
	 * Gets a reference to a child or a single value from an xArch element.
	 * Roughly equivalent to: baseObject.get[TypeOfThing]();
	 * So, if the object referred to by baseObjectRef implements a method called
	 * <CODE>IDescription getDescription();</CODE>
	 * then that would be called here as:
	 * <CODE>ObjRef descriptionRef = (ObjRef)get(baseObjectRef, "Description");</CODE>
	 * where baseObjectRef is a reference to the base object, and the typeOfThing
	 * is shown as "Description".
	 * @param baseObjectRef Reference to a base element containing an 
	 * <CODE>get[typeOfThing]</CODE> method.
	 * @param typeOfThing A string containing the type of thing to get.  For instance,
	 * if the object referred to by baseObjectRef contains a method called 
	 * <CODE>getDescription</CODE>,
	 * then typeOfThing would be "Description".
	 * @return An <CODE>ObjRef</CODE> referring to the object gotten if
	 * the object returned is an xArch element; otherwise a String.
	 * For instance, if <CODE>typeOfThing</CODE> refers to an attribute
	 * or is "Value" (when <CODE>baseObjectRef</CODE> refers to a simple type)
	 * then this function will return a string.
	 */
	public Object get(ObjRef baseObjectRef, String typeOfThing);

	/**
	 * Gets a reference to a child from an xArch element, given the child's ID.
	 * Roughly equivalent to: baseObject.get[TypeOfThing](id);
	 * So, if the object referred to by baseObjectRef implements a method called
	 * <CODE>IDescription getDescription(String id);</CODE>
	 * then that would be called here as:
	 * <CODE>ObjRef descriptionRef = get(baseObjectRef, "Description", id);</CODE>
	 * where baseObjectRef is a reference to the base object, and the typeOfThing
	 * is shown as "Description".  ID remains a string.
	 * @param baseObjectRef Reference to a base element containing an 
	 * <CODE>get[typeOfThing]</CODE> method.
	 * @param typeOfThing A string containing the type of thing to get.  For instance,
	 * if the object referred to by baseObjectRef contains a method called 
	 * <CODE>getDescription</CODE>,
	 * then typeOfThing would be "Description".
	 * @param id Identifier of thing to get.
	 * @return An <CODE>ObjRef</CODE> referring to the object gotten.
	 */
	public ObjRef get(ObjRef baseObjectRef, String typeOfThing, String id);

	/**
	 * Gets a set of references to a set of children from an xArch element, 
	 * given the children's IDs.
	 * Roughly equivalent to: baseObject.get[TypeOfThing]s(ids);
	 * So, if the object referred to by baseObjectRef implements a method called
	 * <CODE>Collection getDescriptions(String[] id);</CODE>
	 * then that would be called here as:
	 * <CODE>ObjRef[] descriptionRefs = get(baseObjectRef, "Description", ids);</CODE>
	 * where baseObjectRef is a reference to the base object, and the typeOfThing
	 * is shown as "Description".  IDs remains strings.
	 * @param baseObjectRef Reference to a base element containing an 
	 * <CODE>get[typeOfThing]s</CODE> method.
	 * @param typeOfThing A string containing the type of thing to get.  For instance,
	 * if the object referred to by baseObjectRef contains a method called 
	 * <CODE>getDescriptions</CODE>,
	 * then typeOfThing would be "Description".
	 * @param ids Identifiers of things to get.
	 * @return An array of <CODE>ObjRef</CODE>s referring to the objects gotten.
	 */
	public ObjRef[] get(ObjRef baseObjectRef, String typeOfThing, String[] ids);

	/**
	 * Gets a set of references to a set of children from an xArch element.
	 * Roughly equivalent to: baseObject.getAll[TypeOfThing]s(ids);
	 * So, if the object referred to by baseObjectRef implements a method called
	 * <CODE>Collection getAllDescriptions();</CODE>
	 * then that would be called here as:
	 * <CODE>ObjRef[] descriptionRefs = getAll(baseObjectRef, "Description");</CODE>
	 * where baseObjectRef is a reference to the base object, and the typeOfThing
	 * is shown as "Description".
	 * @param baseObjectRef Reference to a base element containing an 
	 * <CODE>getAll[typeOfThing]s</CODE> method.
	 * @param typeOfThing A string containing the type of thing to get.  For instance,
	 * if the object referred to by baseObjectRef contains a method called 
	 * <CODE>getAllDescriptions</CODE>,
	 * then typeOfThing would be "Description".
	 * @return An array of <CODE>ObjRef</CODE>s referring to the objects gotten.
	 */
	public ObjRef[] getAll(ObjRef baseObjectRef, String typeOfThing);

	/**
	 * Determines if a simple-type element has a value equivalent to a given value.
	 * Roughly equivalent to: baseObject.has[typeOfThing](valueToCheck);
	 * So, if the object referred to by baseObjectRef implements a method called
	 * <CODE>boolean hasValue(String valueToCheck);</CODE>
	 * then that would be called here as:
	 * <CODE>boolean b = has(baseObjectRef, "Value", valueToCheck);</CODE>
	 * where baseObjectRef is a reference to the base object, and the typeOfThing
	 * is shown as "Value".  The value to check against remains a string.
	 * @param baseObjectRef Reference to a base element containing an 
	 * <CODE>has[typeOfThing](String)</CODE> method.
	 * @param typeOfThing A string containing the type of thing where the
	 * value will be checked.  For instance,
	 * if the object referred to by baseObjectRef contains a method called 
	 * <CODE>hasValue</CODE>,
	 * then typeOfThing would be "Value".
	 * @param valueToCheck Value to check against.
	 * @return <CODE>true</CODE> if the values matched, <CODE>false</CODE> otherwise.
	 */
	public boolean has(ObjRef baseObjectRef, String typeOfThing, String valueToCheck);

	/**
	 * Determines if a complex-type element has a child value equivalent to a given element.
	 * Roughly equivalent to: baseObject.has[typeOfThing](valueToCheck);
	 * So, if the object referred to by baseObjectRef implements a method called
	 * <CODE>boolean hasComponent(IComponent comp);</CODE>
	 * then that would be called here as:
	 * <CODE>boolean b = has(baseObjectRef, "Component", compRef);</CODE>
	 * where baseObjectRef is a reference to the base object, and the typeOfThing
	 * is shown as "Value".  The value to check against is an element of the
	 * appropriate type.
	 * @param baseObjectRef Reference to a base element containing an 
	 * <CODE>has[typeOfThing](...)</CODE> method.
	 * @param typeOfThing A string containing the type of thing where the
	 * value will be checked.  For instance,
	 * if the object referred to by baseObjectRef contains a method called 
	 * <CODE>hasComponent</CODE>,
	 * then typeOfThing would be "Component".
	 * @param valueToCheck Value to compare child against.
	 * @return <CODE>true</CODE> if the values matched, <CODE>false</CODE> otherwise.
	 */
	public boolean has(ObjRef baseObjectRef, String typeOfThing, ObjRef valueToCheck);

	/**
	 * Determines if a complex-type element has a set of child values equivalent to a given set.
	 * Roughly equivalent to: baseObject.hasAll[typeOfThing]s(valuesToCheck);
	 * So, if the object referred to by baseObjectRef implements a method called
	 * <CODE>boolean hasAllComponents(Collection values);</CODE>
	 * then that would be called here as:
	 * <CODE>boolean b = has(baseObjectRef, "Component", valuesToCheck);</CODE>
	 * where baseObjectRef is a reference to the base object, and the typeOfThing
	 * is shown as "Component".  The values to check against are object references.
	 * @param baseObjectRef Reference to a base element containing an 
	 * <CODE>hasAll[typeOfThing](Collection)</CODE> method.
	 * @param typeOfThing A string containing the type of thing where the
	 * value will be checked.  For instance,
	 * if the object referred to by baseObjectRef contains a method called 
	 * <CODE>hasAllComponents</CODE>,
	 * then typeOfThing would be "Component".
	 * @param valuesToCheck An array of references to values to check.
	 * @return <CODE>true</CODE> if all the values were present, <CODE>false</CODE> otherwise.
	 */
	public boolean hasAll(ObjRef baseObjectRef, String typeOfThing, ObjRef[] valuesToCheck);

	/**
	 * Determines if a complex-type element has a set of child values equivalent to a given set.
	 * Roughly equivalent to: baseObject.has[typeOfThing]s(valuesToCheck);
	 * So, if the object referred to by baseObjectRef implements a method called
	 * <CODE>boolean[] hasComponents(Collection values);</CODE>
	 * then that would be called here as:
	 * <CODE>boolean[] bArr = has(baseObjectRef, "Component", valuesToCheck);</CODE>
	 * where baseObjectRef is a reference to the base object, and the typeOfThing
	 * is shown as "Component".  The values to check are object references.
	 * @param baseObjectRef Reference to a base element containing an 
	 * <CODE>has[typeOfThing](Collection)</CODE> method.
	 * @param typeOfThing A string containing the type of thing where the
	 * values will be checked.  For instance,
	 * if the object referred to by baseObjectRef contains a method called 
	 * <CODE>hasComponents</CODE>,
	 * then typeOfThing would be "Component".
	 * @param thingsToCheck An array of references to values to check.
	 * @return An array of <CODE>boolean</CODE> corresponding to each <CODE>ObjRef</CODE>
	 * in the <CODE>valuesToCheck</CODE> array.  Each element in the returned array is
	 * <CODE>true</CODE> if the value was present, <CODE>false</CODE> otherwise.
	 */
	public boolean[] has(ObjRef baseObjectRef, String typeOfThing, ObjRef[] thingsToCheck);

	/**
	 * Gets a reference to the IXArch object that contains/is responsible for the given object.
	 * Roughly equivalent to: baseObject.getXArch();
	 * 
	 * @param baseObjectRef Reference to an XArch element.
	 * @return Reference to a the IXArch object responsible for the base object.
	 */
	public ObjRef getXArch(ObjRef baseObjectRef);

	/**
	 * Gets a child of an IXArch element.
	 * Roughly equivalent to: context.get[typeOfThing]Element(xArch);
	 * 
	 * @param contextObjectRef Reference to a context object (created by
	 * <CODE>createContext(...)</CODE> that contains an appropriate 
	 * <CODE>get[typeOfThing](...)</CODE> method.
	 * @param typeOfThing Name of top-level element to get.  If the context object
	 * contains a method called <CODE>getArchStructure(IXArch)</CODE> then the <CODE>typeOfThing</CODE>
	 * parameter will be <CODE>"ArchStructure"</CODE>.	
	 * @param xArchObjectRef Reference to the xArch object from which to retrieve the top-level element.
	 * @return Reference to a the top-level object.
	 */
	public ObjRef getElement(ObjRef contextObjectRef, String typeOfThing, ObjRef xArchObjectRef);

	/**
	 * Gets all children of an IXArch element of a given type.
	 * Roughly equivalent to: context.get[typeOfThing](xArch);
	 * 
	 * @param contextObjectRef Reference to a context object (created by
	 * <CODE>createContext(...)</CODE> that contains an appropriate 
	 * <CODE>get[typeOfThing]Element(...)</CODE> method.
	 * @param typeOfThing Name of top-level element to get.  If the context object
	 * contains a method called <CODE>getAllArchStructures(IXArch)</CODE> then the <CODE>typeOfThing</CODE>
	 * parameter will be <CODE>"ArchStructure"</CODE>.	
	 * @param xArchObjectRef Reference to the xArch object from which to retrieve the top-level element.
	 * @return Array of reference to a the top-level objects.
	 */
	public ObjRef[] getAllElements(ObjRef contextObjectRef, String typeOfThing, ObjRef xArchObjectRef);
	
	/**
	 * Gets metadata describing a compound type (context and simple type name).
	 *  
	 * @param type The compound type (see: {@link XArchMetadataUtils#getType(String, String)})
	 * @return Metadata describing the type of the xArch element.
	 * 
	 * @see XArchMetadataUtils#getType(String, String)
	 */
	public IXArchTypeMetadata getTypeMetadata(String type);
	
	/**
	 * Gets metadata describing the type of an xArch element.
	 * Roughly equivalent to: baseObject.getTypeMetadata();
	 *  
	 * @param baseObjectRef Reference to an xArch element.
	 * @return Metadata describing the type of the xArch element.
	 */
	public IXArchTypeMetadata getTypeMetadata(ObjRef baseObjectRef);
	
	/**
	 * Gets metadata describing the instance of an xArch element.
	 * 
	 * @param baseObjRef Reference to an xArch element.
	 * @return Metadata describing the instance of the xArch element.
	 */
	public IXArchInstanceMetadata getInstanceMetadata(ObjRef baseObjRef);
	
	/**
	 * DEPRICATED: This method reveals the underlying implementing class. Use {@link #isInstanceOf(ObjRef, String)}.
	 * 
	 * Gets the class name of an xArch element.
	 * Roughly equivalent to: baseObject.getClass().getName();
	 * 
	 * @param baseObjectRef Reference to an xArch element.
	 * @return The name of the class of that object.
	 * 
	 * @deprecated Replaced by {@link #isInstanceOf(ObjRef, String)}
	 */
	public String getType(ObjRef baseObjectRef);
	
	/**
	 * NOTE: className reveals implementation detail, replace with compound type described in {@link XArchMetadataUtils#getType(String, String)}.
	 * For example, before you would have called this method with "edu.uci.isr.xarch.types.IComponentType", but now
	 * the format described in {@link XArchMetadataUtils#getType(String, String)}
	 * 
	 * Determines if a given xArch element is an instance of a given class.
	 * Roughly equivalent to: (baseObject instanceof [type])
	 * 
	 * @param baseObjectRef Reference to an xArch element.
	 * @param type The compound type (see: {@link XArchMetadataUtils#getType(String, String)})
	 * @return <CODE>true</CODE> if the base object was an instance of the given type, <CODE>false</CODE> otherwise.
	 * 
	 * @see XArchMetadataUtils#getType(String, String)
	 */
	public boolean isInstanceOf(ObjRef baseObjectRef, String type);

	/**
	 * Determines if toType is either the same as, or a supertype of, the fromType parameter.
	 * Roughly equivalent to: ([toType].isAssignableFrom([fromType])
	 * 
	 * @param fromType The from compound type to check (see: {@link XArchMetadataUtils#getType(String, String)})
	 * @param toType The potential subtype to check (see: {@link XArchMetadataUtils#getType(String, String)})
	 * @return <CODE>true</CODE> if toType is the same as, or a supertype of, fromType, <CODE>false</CODE> otherwise.
	 * 
	 * @see XArchMetadataUtils#getType(String, String)
	 */
	public boolean isAssignable(String toType, String fromType);

	/**
	 * Determines if one node in the XML tree is an ancestor of another.
	 * This method is more efficient than using the output of
	 * <CODE>getAllAncestors(...)</CODE>, since <CODE>getAllAncestors(...)</CODE>
	 * makes wrapper objects for each ancestor, and this one just looks at the
	 * underlying XML nodes.
	 * @param childRef The potential child node.
	 * @param ancestorRef The potential ancestor node.
	 * @return <CODE>true</CODE> if the node referred to by <CODE>ancestorRef</CODE>
	 * is an ancestor of the node referred to by <CODE>childRef</CODE>, <CODE>false</CODE>
	 * otherwise.
	 */
	public boolean hasAncestor(ObjRef childRef, ObjRef ancestorRef);

	/**
	 * Determines if a node in the XML tree is attached to the root.
	 * @param childRef The node to check.
	 * @return <CODE>true</CODE> if the node referred to by <CODE>childRef</CODE>
	 * is attached to the root node of the document; (i.e. the root node is an
	 * ancestor of the node), <CODE>false</CODE> otherwise.
	 */
	public boolean isAttached(ObjRef childRef);
	
	/** 
	 * Gets all the ancestors (in the XML tree) of a given target object.
	 * @param targetObjectRef Reference to target object.
	 * @return Array of references to ancestors, in order starting with
	 * (and including) the target object, then its parent, then its parent's parent, etc.
	 */
	public ObjRef[] getAllAncestors(ObjRef targetObjectRef);
	
	/**
	 * Gets the parent (in the XML tree) of a given target object.
	 * @param targetObjectRef Reference to target object.
	 * @return A reference to its parent in the XML tree, or <CODE>null</CODE> if it has none.
	 */
	public ObjRef getParent(ObjRef targetObjectRef);

	
	/**
	 * Gets the local element (DOM element) name of the element
	 * referred to by the given reference.
	 * @param xArchRef Reference to the element whose element name
	 * you want to get.
	 * @return Element's name.
	 */
	public String getElementName(ObjRef xArchRef);

	/**
	 * Gets the <CODE>XArchPath</CODE> of the given element.
	 * @param ref Reference to the element whose path you
	 * want to get.
	 * @return <CODE>XArchPath</CODE> to that element.
	 */
	public XArchPath getXArchPath(ObjRef ref);

	/**
	 * Gets an element by its ID within a given xArch tree.
	 * If no such element exists, returns <CODE>null</CODE>.
	 * @param xArchRef Reference to the IXArch object that is the root of the tree to search.
	 * @param id The ID to search for.
	 * @return reference to the object, or <CODE>null</CODE> if no such object exists.
	 * @exception IllegalArgumentException if <CODE>xArchRef</CODE> is invalid.
	 */
	public ObjRef getByID(ObjRef xArchRef, String id);
	
	/**
	 * Gets an element by its ID within ANY OPEN xArch tree.
	 * If no such element exists, returns <CODE>null</CODE>.
	 * @param id The ID to search for.
	 * @return reference to the object, or <CODE>null</CODE> if no such object exists.
	 */
	public ObjRef getByID(String id);
	
	/**
	 * Resolves an href, as might be found in an XLink.
	 * @param xArchRef Reference to the IXArch object that provides the context in which the
	 * href exists.
	 * @param href The href to resolve.  May be local or remote.  <I>Note:</I> For this version
	 * of the library, hrefs must be in the form <CODE>#id</CODE> or <CODE>http://....#id</CODE>
	 * @return Reference to the referenced (by the href) object, or <CODE>null</CODE>.
	 */
	public ObjRef resolveHref(ObjRef xArchRef, String href);
	
	
	/**
	 * Resolves an <CODE>XArchPath</CODE> in the context of
	 * the given document (in terms of an <CODE>ObjRef</CODE>
	 * to the top level xArch element of a document).
	 * @param xArchRef Reference to the top-level element of the
	 * document within which to resolve the <CODE>XArchPath</CODE>.
	 * @param xArchPath <CODE>XArchPath</CODE> to resolve.
	 * @return <CODE>ObjRef</CODE> to the referenced element, or
	 * <CODE>null</CODE> if the path cannot be resolved
	 */
	public ObjRef resolveXArchPath(ObjRef xArchRef, XArchPath xArchPath);

	/**
	 * Determines if two objects are equal by matching IDs.
	 * Roughly equivalent to: baseObject.isEqual(thingToCheck);
	 * So, if the object referred to by baseObjectRef implements a method called
	 * <CODE>boolean isEqual(IDescription thingToCheck);</CODE>
	 * then that would be called here as:
	 * <CODE>boolean b = isEqual(baseObjectRef, thingToCheck);</CODE>
	 * where baseObjectRef is a reference to the base object and
	 * thingToCheck is a reference to the potentially-equal value.
	 * @param baseObjectRef Reference to a base element containing an 
	 * <CODE>isEqual(...)</CODE> method.
	 * @param thingToCheck The value to check for equality.
	 * @return <CODE>true</CODE> if the IDs matched, <CODE>false</CODE> otherwise.
	 */
	public boolean isEqual(ObjRef baseObjectRef, ObjRef thingToCheck);

	/**
	 * Determines if two objects are equal by matching content
	 * and ignoring IDs.
	 * Roughly equivalent to: baseObject.isEquivalent(thingToCheck);
	 * So, if the object referred to by baseObjectRef implements a method called
	 * <CODE>boolean isEquivalent(IDescription thingToCheck);</CODE>
	 * then that would be called here as:
	 * <CODE>boolean b = isEquivalent(baseObjectRef, thingToCheck);</CODE>
	 * where baseObjectRef is a reference to the base object and
	 * thingToCheck is a reference to the potentially-equivalent value.
	 * @param baseObjectRef Reference to a base element containing an 
	 * <CODE>isEquivalent(...)</CODE> method.
	 * @param thingToCheck The value to check for equivalence.
	 * @return <CODE>true</CODE> if the IDs matched, <CODE>false</CODE> otherwise.
	 */
	public boolean isEquivalent(ObjRef baseObjectRef, ObjRef thingToCheck);
	
	/**
	 * Attempts to locate links to the element with the given ID.
	 * @param xArchRef Reference to an IXArch that is the root element
	 * of the document containing the given identified element..
	 * @param id Identifier in the document referred to by 
	 * <CODE>xArchRef</CODE>.
	 * @return <CODE>ObjRef[]</CODE> of elements (usually
	 * <CODE>IXMLLink</CODE>s that refer to the element with
	 * the given ID.
	 */
	public ObjRef[] getReferences(ObjRef xArchRef, String id);
	
	
	/**
	 * Determines whether two ObjRefs refer to the same underlying
	 * object.
	 * @param ref1 First ObjRef.
	 * @param ref2 Second ObjRef
	 * @return <code>true</code> if equal, false otherwise.
	 */
	public boolean equals(ObjRef ref1, ObjRef ref2);

	/**
	 * Returns a list of available context names for the given xArchRef.
	 * @return available context names for the given xArchRef.
	 */
	public String[] getContextTypes();
}
