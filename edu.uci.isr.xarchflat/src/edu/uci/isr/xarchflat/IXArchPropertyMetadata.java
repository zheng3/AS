package edu.uci.isr.xarchflat;

public interface IXArchPropertyMetadata{

	/**
	 * <code>ATTRIBUTE</code> indicates that this property is an attribute.
	 */
	public static final int ATTRIBUTE = 0x01 << 0;

	/**
	 * <code>ELEMENT</code> indicates that this property is an element that can contain only one value.
	 */
	public static final int ELEMENT = 0x01 << 1;

	/**
	 * <code>ELEMENT_MANY</code> indicates that this property is an element that can contain multiple values.
	 */
	public static final int ELEMENT_MANY = 0x01 << 2;

	/**
	 * Comment for <code>UNBOUNDED</code>
	 */
	public static final int UNBOUNDED = Integer.MAX_VALUE;

	/**
	 * Returns the type of metadata stored for this property.
	 * @return the type of metadata stored for this property
	 * @see #ATTRIBUTE
	 * @see #ELEMENT
	 * @see #ELEMENT_MANY
	 */
	public int getMetadataType();

	/**
	 * Returns the element property name used to store a value of this type.
	 * @return the element property name used to store a value of this type.
	 */
	public String getName();

	/**
	 * Returns the expected type for the property value.
	 * @return the value type.
	 * @see XArchMetadataUtils#getType(String, String)
	 */
	public String getType();	
	
//	/**
//	 * Returns the context name for expected property value types, 
//	 * or <code>null</code> in the event that the expected property value type has no package name.
//	 * @return the abbreviated package name, or <code>null</code>.
//	 */
//	public String getTypePackage();

//	/**
//	 * Returns the base type name for expected property value types.
//	 * @return the base type name for expected property value types.
//	 */
//	public String getTypeName();

	/**
	 * Returns the minimum cardinality of this property.
	 * @return the minimum cardinality of this property
	 * @see #UNBOUNDED
	 */
	public int getMinOccurs();

	/**
	 * Returns the maximum cardinality of this property, or <code>UNBOUNDED</code> if it is unbounded.
	 * @return the maximum cardinality of this property, or <code>UNBOUNDED</code>
	 * @see #UNBOUNDED
	 */
	public int getMaxOccurs();

	/**
	 * Returns the fixed value of this property, or <code>null</code> if it is not fixed.
	 * @return the fixed value of this property, or <code>null</code>
	 */
	public String getFixedValue();

	/**
	 * Returns a list of values for this property, 
	 * or an empty list if the element is not restricted to a set of values.
	 * @return the list of valid values for this property, or <code>null<code> if this attribute is not enumerated
	 */
	public String[] getEnumeratedValues();

//	/**
//	 * Returns <code>true</code> if this is an id attribute, <code>false</code> otherwise.
//	 * @return <code>true</code> if this is an id attribute, <code>false</code> otherwise
//	 */
//	public boolean isId();

	/**
	 * Returns <code>true</code> if this is an optional property, <code>false</code> otherwise.
	 * @return <code>true</code> if this is an optional property, <code>false</code> otherwise 
	 */
	public boolean isOptional();

//	/**
//	 * Returns <code>true</code> if a property of this name may have more than one value, <code>false</code> otherwise. 
//	 * @return <code>true</code> if a property of this name may have more than one value, <code>false</code> otherwise
//	 */
//	public boolean isMany();

	/**
	 * Returns <code>true</code> if the value of this property is fixed, <code>false</code> otherwise.
	 * @return <code>true</code> if the value of this property is fixed, <code>false</code> otherwise.
	 */
	public boolean isFixed();

//	/**
//	 * Returns <code>true</code> if the element has enumerated values, <code>false</code> otherwise
//	 * @return <code>true</code> if the element has enumerated values, <code>false</code> otherwise
//	 */
//	public boolean isEnumerated();
}