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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class XArchTypeMetadata implements java.io.Serializable {
	
	/**
	 * <code>XARCH_ELEMENT</code> indicates that this type metadata describes an xArch element.
	 */
	public static final int XARCH_ELEMENT = 0x01 << 0;

	/**
	 * <code>XARCH_CONTEXT</code> indicates that this type metadata describes an xArch context.
	 */
	public static final int XARCH_CONTEXT = 0x01 << 1;

	private int type;
	private String typePackage;
	private String typeName;
	private String parentTypePackage;
	private String parentTypeName;
	private String compoundType;
	private String parentCompoundType;
	private List propertyList = new ArrayList();
	private Map propertyMap = new HashMap();
	private List actionList = new ArrayList();

	public XArchTypeMetadata(int type, String packageName, String typeName, XArchTypeMetadata parent, XArchPropertyMetadata[] properties,  XArchActionMetadata[] actions){
		this.type = type;
		this.typePackage = packageName;
		this.typeName = typeName;
		this.compoundType = (typePackage == null ? "" : typePackage)+":"+(typeName == null ? "" : typeName);
		if(parent != null){
			this.parentTypePackage = parent.typePackage;
			this.parentTypeName = parent.typeName;
			this.propertyList.addAll(parent.propertyList);
			this.propertyMap.putAll(parent.propertyMap);
		}
		for (int i = 0; i < properties.length; i++){
			this.propertyList.add(properties[i]);
			this.propertyMap.put(capFirstLetter(properties[i].getName()), properties[i]);
		}
		this.actionList.addAll(Arrays.asList(actions));
		this.propertyList = Collections.unmodifiableList(propertyList);
		this.propertyMap = Collections.unmodifiableMap(propertyMap);
		this.actionList = Collections.unmodifiableList(actionList);
	}
	
	/**
	 * Returns the type of object that this metadata describes.
	 * @return the type of object that this metadata describes.
	 * @see #XARCH_ELEMENT
	 * @see #XARCH_CONTEXT
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Returns the abbreviated package name (context name). 
	 * This name can be used to get an instance of the XARCH_CONTEXT object itself, or 
	 * to get the context object from which an instance of this XARCH_ELEMENT type can be created. 
	 * This may return <code>null</code> in the event that the XARCH_ELEMENT type being described has no abbreviated package name.
	 * @return the abbreviated package name, or <code>null</code>.
	 */
	public String getTypePackage() {
		return typePackage;
	}

	/**
	 * Returns the type name used to create instances of this XARCH_ELEMENT from a context object.
	 * @return the type name used to create instances of this XARCH_ELEMENT from a context object.
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Returns the abbreviated package name (context name) of the parent, or <code>null</code> if non exists. 
	 * @return the abbreviated package name of the parent, or <code>null</code>.
	 * @see #getTypePackage()
	 */
	public String getParentTypePackage() {
		return parentTypePackage;
	}

	/**
	 * Returns the type name of the parent, or <code>null</code> if non exists.
	 * @return the type name of the parent, or <code>null</code> if non exists.
	 * @see #getTypeName()
	 */
	public String getParentTypeName() {
		return parentTypeName;
	}

	/**
	 * Returns metadata describing a given property of this XARCH_ELEMENT object, 
	 * or <code>null</code> if such a property does not exist. 
	 * @param name the name of the property to find
	 * @return metadata describing the property of the given name  
	 */
	public XArchPropertyMetadata getProperty(String name){
		return (XArchPropertyMetadata)propertyMap.get(capFirstLetter(name));
	}
	
	/**
	 * Returns a ordered list of all properties of this XARCH_ELEMENT object.
	 * @return a ordered list of all properties of this XARCH_ELEMENT object
	 */
	public List getPropertyList(){
		return propertyList;
	}
	
	/**
	 * Returns an ordered iterator including all properties of this XARCH_ELEMENT object.
	 * @return an ordered iterator including all properties of this XARCH_ELEMENT object.
	 */
	public Iterator getProperties(){
		return propertyList.iterator();
	}

	/**
	 * Returns an ordered iterator including only properties of the specified type
	 * @param typeMask property types to include
	 * @return an ordered iterator including only properties of the specified type
	 */
	public Iterator getProperties(final int typeMask){
		return new FilterIterator(propertyList.iterator()) {
			public boolean include(Object o) {
				XArchPropertyMetadata t = (XArchPropertyMetadata)o;
				return (t.getType() & typeMask) != 0;
			}
		};
	}
	
	/**
	 * Returns <code>true</code> if this is an XARCH_ELEMENT object and it has an Id attribute, <code>false</code> otherwise.
	 * @return <code>true</code> if this is an XARCH_ELEMENT object and it has an Id attribute, <code>false</code> otherwise
	 */
	public boolean hasId(){
		XArchPropertyMetadata id = getProperty("Id");
		if(id == null)
			return false;
		return id.isId();
	}

	/**
	 * Returns <code>true</code> if this type represents the XArch type, <code>false</code> otherwise. 
	 * @return <code>true</code> if this type represents the XArch type, <code>false</code> otherwise.
	 */
	public boolean isXArch(){
		return typePackage == null && "XArch".equals(typeName);
	}

	/**
	 * Returns an iterator including all actions of this XARCH_CONTEXT object.
	 * @return an iterator including all actions of this XARCH_CONTEXT object.
	 */
	public Iterator getActions(){
		return actionList.iterator();
	}
	
	/**
	 * Returns an iterator including only actions of the specified type 
	 * @param typeMask action types to include
	 * @return an iterator including only actions of the specified type
	 */
	public Iterator getActions(final int typeMask){
		return new FilterIterator(propertyList.iterator()) {
			public boolean include(Object o) {
				XArchActionMetadata t = (XArchActionMetadata)o;
				return (t.getType() & typeMask) != 0;
			}
		};
	}
	
	private static String capFirstLetter(String s){
		if(s.length() == 0){
			return s;
		}
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
	
	public String toString(){
		return compoundType;
	}
	
	public boolean equals(Object o){
		if (o instanceof XArchTypeMetadata)
			return compoundType.equals(((XArchTypeMetadata)o).compoundType);
		return false;
	}
	
	public int hashCode(){
		return compoundType.hashCode();
	}
}