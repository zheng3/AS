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
package edu.uci.isr.xarch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class XArchPropertyMetadata implements java.io.Serializable {

	private static final List EMPTY_LIST = Arrays.asList(new Object[0]);
	
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
	
	private int type;
	private String name;
	private String typePackage;
	private String typeName;
	private int minOccurs;
	private int maxOccurs;
	private String fixedValue;
	private boolean enumerated;
	private List enumeratedValues;
	
	public XArchPropertyMetadata(int type, String name, String typePackage, String typeName, int minOccurs, int maxOccurs, String fixedValue, String[] enumeratedValues) {
		this.type = type;
		this.name = name;
		this.typePackage = typePackage;
		this.typeName = typeName;
		this.minOccurs = minOccurs;
		this.maxOccurs = maxOccurs;
		this.fixedValue = fixedValue;
		this.enumerated = enumeratedValues != null;
		this.enumeratedValues = enumeratedValues != null ? Collections.unmodifiableList(Arrays.asList(enumeratedValues)) : EMPTY_LIST;
	}
	
	public static XArchPropertyMetadata createAttribute(String name, String typePackage, String typeName, String fixedValue, String[] enumeratedValues) {
		XArchPropertyMetadata m = new XArchPropertyMetadata(ATTRIBUTE, name, typePackage, typeName, 0, 1, fixedValue, enumeratedValues);
		XArchTypeMetadata t = XArchMetadataUtils.getTypeMetadata(typePackage, typeName);
		if(t != null){
			XArchPropertyMetadata p = t.getProperty("value");
			if(p != null){
				ArrayList evs = new ArrayList();
				evs.addAll(m.enumeratedValues);
				evs.addAll(p.enumeratedValues);
				m.enumerated |= p.enumerated;
				m.enumeratedValues = Collections.unmodifiableList(evs);
			}
		}
		return m;
	}

	public static XArchPropertyMetadata createElement(String name, String typePackage, String typeName, int minOccurs, int maxOccurs) {
		return new XArchPropertyMetadata(maxOccurs > 1 ? ELEMENT_MANY : ELEMENT, 
				name, typePackage, typeName, minOccurs, maxOccurs, null, null); 
	}

	/**
	 * Returns the type of this property.
	 * @return the type of this property
	 * @see #ATTRIBUTE
	 * @see #ELEMENT
	 * @see #ELEMENT_MANY
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Returns the element property name used to store a value of this type.
	 * @return the element property name used to store a value of this type.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the abbreviated package name (context name) for expected property value types, 
	 * or <code>null</code> in the event that the expected property value type has no package name.
	 * @return the abbreviated package name, or <code>null</code>.
	 */
	public String getTypePackage() {
		return typePackage;
	}

	/**
	 * Returns the base type name for expected property value types.
	 * @return the base type name for expected property value types.
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Returns the minimum cardinality of this property.
	 * @return the minimum cardinality of this property
	 * @see #UNBOUNDED
	 */
	public int getMinOccurs() {
		return minOccurs;
	}
	
	/**
	 * Returns the maximum cardinality of this property, or <code>UNBOUNDED</code> if it is unbounded.
	 * @return the maximum cardinality of this property, or <code>UNBOUNDED</code>
	 * @see #UNBOUNDED
	 */
	public int getMaxOccurs() {
		return maxOccurs;
	}

	/**
	 * Returns the fixed value of this property, or <code>null</code> if it is not fixed.
	 * @return the fixed value of this property, or <code>null</code>
	 */
	public String getFixedValue(){
		return fixedValue;
	}

	/**
	 * Returns a list of values for this property, 
	 * or an empty list if the element is not restricted to a set of values.
	 * @return the list of valid values for this property
	 */
	public List getEnumeratedValues() {
		return enumeratedValues;
	}

	/**
	 * Returns <code>true</code> if this is an id attribute, <code>false</code> otherwise.
	 * @return <code>true</code> if this is an id attribute, <code>false</code> otherwise
	 */
	public boolean isId() {
		return type == ATTRIBUTE && ("Id".equals(name) || "id".equals(name)); // && "instance".equals(typePackage) && "Identifier".equals(typeName);
	}
	
	/**
	 * Returns <code>true</code> if this is an optional property, <code>false</code> otherwise.
	 * @return <code>true</code> if this is an optional property, <code>false</code> otherwise 
	 */
	public boolean isOptional() {
		return minOccurs == 0;
	}
	
	/**
	 * Returns <code>true</code> if a property of this name may have more than one value, <code>false</code> otherwise. 
	 * @return <code>true</code> if a property of this name may have more than one value, <code>false</code> otherwise
	 */
	public boolean isMany() {
		return maxOccurs > 1;
	}

	/**
	 * Returns <code>true</code> if the value of this property is fixed, <code>false</code> otherwise.
	 * @return <code>true</code> if the value of this property is fixed, <code>false</code> otherwise.
	 */
	public boolean isFixed(){
		return fixedValue != null;
	}
	
	/**
	 * Returns <code>true</code> if the element has enumerated values, <code>false</code> otherwise
	 * @return <code>true</code> if the element has enumerated values, <code>false</code> otherwise
	 */
	public boolean isEnumerated(){
		return enumerated;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(getName()).append("(");
		sb.append("type=").append(type).append(", ");
		sb.append("typePackage=").append(getTypePackage()).append(", ");
		sb.append("typeName=").append(getTypeName()).append(", ");
		sb.append("minOccurs=").append(getMinOccurs()).append(", ");
		sb.append("maxOccurs=").append(getMaxOccurs()).append(", ");
		sb.append("fixedValue=").append(getFixedValue()).append(", ");
		sb.append("enumeratedValues=").append(enumeratedValues).append(")");
		return sb.toString();
	}
}