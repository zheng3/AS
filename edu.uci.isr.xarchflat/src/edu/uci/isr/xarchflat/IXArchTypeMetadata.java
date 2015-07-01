package edu.uci.isr.xarchflat;

public interface IXArchTypeMetadata extends java.io.Serializable{

	/**
	 * <code>XARCH_ELEMENT</code> indicates that this type metadata describes an xArch element.
	 */
	public static final int XARCH_ELEMENT = 0x01 << 0;

	/**
	 * <code>XARCH_CONTEXT</code> indicates that this type metadata describes an xArch context.
	 */
	public static final int XARCH_CONTEXT = 0x01 << 1;

	/**
	 * Returns the type of object that this metadata describes.
	 * @return the type of object that this metadata describes.
	 * @see #XARCH_ELEMENT
	 * @see #XARCH_CONTEXT
	 */
	public int getMetadataType();

	/**
	 * Returns the type of this element or context.
	 * @return the type of this element or context.
	 * @see XArchMetadataUtils#getType(String, String)
	 */
	public String getType();
	
//	/**
//	 * Returns the abbreviated package name (context name). 
//	 * This name can be used to get an instance of the XARCH_CONTEXT object itself, or 
//	 * to get the context object from which an instance of this XARCH_ELEMENT type can be created. 
//	 * This may return <code>null</code> in the event that the XARCH_ELEMENT type being described has no abbreviated package name.
//	 * @return the abbreviated package name, or <code>null</code>.
//	 */
//	public String getTypePackage();

//	/**
//	 * Returns the type name used to create instances of this XARCH_ELEMENT from a context object.
//	 * @return the type name used to create instances of this XARCH_ELEMENT from a context object.
//	 */
//	public String getTypeName();

	/**
	 * Returns the parent type for this element, or <code>null</code> if one does not exist.
	 * @return the parent type for this element, or <code>null</code> if one does not exist.
	 * @see XArchMetadataUtils#getType(String, String)
	 */
	public String getParentType();
	
//	/**
//	 * Returns the abbreviated package name (context name) of the parent, or <code>null</code> if non exists. 
//	 * @return the abbreviated package name of the parent, or <code>null</code>.
//	 * @see #getTypePackage()
//	 */
//	public String getParentTypePackage();

//	/**
//	 * Returns the type name of the parent, or <code>null</code> if non exists.
//	 * @return the type name of the parent, or <code>null</code> if non exists.
//	 * @see #getTypeName()
//	 */
//	public String getParentTypeName();

	/**
	 * Returns metadata describing a given property of this XARCH_ELEMENT object, 
	 * or <code>null</code> if such a property does not exist. 
	 * @param name the name of the property to find
	 * @return metadata describing the property of the given name  
	 */
	public IXArchPropertyMetadata getProperty(String name);

//	/**
//	 * Returns a ordered list of all properties of this XARCH_ELEMENT object.
//	 * @return a ordered list of all properties of this XARCH_ELEMENT object
//	 */
//	public List<IXArchPropertyMetadata> getPropertyList();

	/**
	 * Returns an ordered iterator including all properties of this XARCH_ELEMENT object.
	 * @return an ordered iterator including all properties of this XARCH_ELEMENT object.
	 */
	public IXArchPropertyMetadata[] getProperties();

//	/**
//	 * Returns an ordered iterator including only properties of the specified type
//	 * @param typeMask property types to include
//	 * @return an ordered iterator including only properties of the specified type
//	 */
//	public Iterator<IXArchPropertyMetadata> getProperties(final int typeMask);
//
//	/**
//	 * Returns <code>true</code> if this is an XARCH_ELEMENT object and it has an Id attribute, <code>false</code> otherwise.
//	 * @return <code>true</code> if this is an XARCH_ELEMENT object and it has an Id attribute, <code>false</code> otherwise
//	 */
//	public boolean hasId();
//
//	/**
//	 * Returns <code>true</code> if this type represents the XArch type, <code>false</code> otherwise. 
//	 * @return <code>true</code> if this type represents the XArch type, <code>false</code> otherwise.
//	 */
//	public boolean isXArch();

	/**
	 * Returns an iterator including all actions of this XARCH_CONTEXT object.
	 * @return an iterator including all actions of this XARCH_CONTEXT object.
	 */
	public IXArchActionMetadata[] getActions();

//	/**
//	 * Returns an iterator including only actions of the specified type 
//	 * @param typeMask action types to include
//	 * @return an iterator including only actions of the specified type
//	 */
//	public Iterator<IXArchActionMetadata> getActions(final int typeMask);
}