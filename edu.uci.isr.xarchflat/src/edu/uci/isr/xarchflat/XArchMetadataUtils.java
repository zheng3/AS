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
package edu.uci.isr.xarchflat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XArchMetadataUtils {

	private static boolean inited = false;

	private static List<String> topLevelElements = Collections.synchronizedList(new ArrayList<String>());

	private static Map<String, Promotion> promotions = Collections.synchronizedMap(new HashMap<String, Promotion>());

	private static class Promotion {
		public final String typeName;

		public List<String> promotions;

		public Set<String> allPromotions;

		public Promotion(String typeName) {
			this.typeName = typeName;
			this.promotions = new ArrayList<String>();
			// this.allPromotions = new HashSet<String>(); // set in the calculateAllPromotions method  
		}

		public boolean equals(Object o) {
			if(o instanceof Promotion)
				return typeName.equals(((Promotion)o).typeName);
			return super.equals(o);
		}

		public int hashCode() {
			return typeName.hashCode();
		}
	}

	/**
	 * Returns a collection of all top level elements.
	 * 
	 * @return a collection of all top level elements
	 */
	public static Collection<String> getTopLevelElements(XArchFlatQueryInterface xArch) {
		init(xArch);
		return topLevelElements;
	}

	private static char contextTypeSeparator = '#';

	/**
	 * Returns the context name for a given compound type
	 * @param type the type from which to extract the context name 
	 * @return the context name for the given type
	 * 
	 * @see #getType(String, String)
	 */
	public static String getTypeContext(String type){
		int i = type.lastIndexOf(contextTypeSeparator);
		assert i >= 0 : "Malformed type <"+type+">";
		return type.substring(0, i); 
	}
	
	/**
	 * Returns the simple type name for a given compound type
	 * @param type the type from which to extract the simple type name
	 * @return the simple type name for the given type
	 * 
	 * @see #getType(String, String)
	 */
	public static String getTypeName(String type){
		int i = type.lastIndexOf(contextTypeSeparator);
		assert i >= 0 : "Malformed type <"+type+">";
		return type.substring(i+1); 
	}
	
	/**
	 * Returns the compound type for a given context and simple type name (e.g. "types#ComponentType" for the "ComponentType" type in the "types" context).
	 * 
	 * Compound types are specified in the following way:
	 * <ul>
	 * <li>"context#" specifies a context type</li>
	 * <li>"context#type" specifies a type within a particular context, </li>
	 * </ul>
	 * 
	 * Because the (root) XArch package has no context, the omission of a context refers to the root XArch package types. For example: 
	 * <ul>
	 * <li>"#" specifies the root xArch context type</li>
	 * <li>"#type" specifies a type within the root xArch context</li>
	 * </ul>
	 *   
	 * @param context the context name for the type
	 * @param type the type name for the type (within the given context)
	 * @return the compound name for the given context and type name
	 */
	public static String getType(String context, String type){
		return (context != null ? context : "") +contextTypeSeparator+ (type != null ? type : "");
	}
	
	/**
	 * Returns the parent type's metadata, or <code>null</code> if non exists.
	 * 
	 * @param type
	 *            the child metadata from which to determine the parent
	 * @return the parent type's metadata, or <code>null</code> if non exists.
	 */
	public static String getParent(XArchFlatQueryInterface xArch, String type) {
		IXArchTypeMetadata typeMetadata = xArch.getTypeMetadata(type);
		if(typeMetadata != null)
			return typeMetadata.getParentType();
		return null;
	}
	
	/**
	 * Returns a set of all possible types to which the given type can be
	 * promotoed.
	 * 
	 * @param type
	 *            the type metadata to start from
	 * @return a set of all possible types to which the given type can be
	 *         promotoed
	 */
	public static Set<String> getAvailablePromotions(XArchFlatQueryInterface xArch, String type) {
		init(xArch);
		Promotion p = promotions.get(type);
		if (p == null)
			return Collections.emptySet();
		return p.allPromotions;

	}

	/**
	 * Returns a list of <code>IXArchTypeMetadata</code> involved in promoting
	 * an instance of <code>fromType</code> to an instance of
	 * <code>toType</code>. An empty list will be returned if
	 * <code>fromType</code> is an instance of <code>toType</code>. 
	 * This method will return <code>null</code> if it is not possible 
	 * to promote <code>fromType</code> to <code>toType</code>.
	 * 
	 * @param fromType
	 *            the initial type
	 * @param toType
	 *            the desired type
	 * @return A list of promotions to promote <code>fromType</code> to
	 *         <code>toType</code>, or <code>null</code> if it is not
	 *         possible.
	 */
	public static List<String> getPromotionPathTypes(XArchFlatQueryInterface xArch, String fromType, String toType) {
		init(xArch);
		if (xArch.isAssignable(toType, fromType))
			return Collections.emptyList();
		Set<String> fromPromotions = getAvailablePromotions(xArch, fromType);
		if (!fromPromotions.contains(toType))
			return null;

		List<String> types = new ArrayList<String>();
		while (!toType.equals(fromType)) {
			types.add(0, toType);
			toType = getParent(xArch, toType);
		}
		return types;
	}

	private static synchronized void init(XArchFlatQueryInterface xArch) {
		if (inited) {
			return;
		}
		try {
			for (String contextTypeName : xArch.getContextTypes()) {
				IXArchTypeMetadata contextType = xArch.getTypeMetadata(getType(contextTypeName, ""));

				for (IXArchActionMetadata action : contextType.getActions()) {
					switch (action.getMetadataType()) {

					case IXArchActionMetadata.CREATE_ELEMENT:
						topLevelElements.add(action.getOutputType());
						break;

					case IXArchActionMetadata.PROMOTE:
						addPromotion(action.getInputType(), action.getOutputType());
						break;
					}
				}
			}
			for (Promotion promotion : promotions.values()) {
				calculateAllPromotions(promotion);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		promotions = Collections.unmodifiableMap(promotions);
		topLevelElements = Collections.unmodifiableList(topLevelElements);

		inited = true;
	}

	private static void addPromotion(String typeName, String promotionTypeName) {
		Promotion promotion = promotions.get(typeName);
		if (promotion == null) {
			promotion = new Promotion(typeName);
			promotions.put(typeName, promotion);
		}
		promotion.promotions.add(promotionTypeName);
	}

	private static void calculateAllPromotions(Promotion promotion) {
		if (promotion.allPromotions != null)
			return;
		promotion.allPromotions = new HashSet<String>();
		for (String toType : promotion.promotions) {
			promotion.allPromotions.add(toType);
			Promotion p = promotions.get(toType);
			if (p != null) {
				calculateAllPromotions(p);
				promotion.allPromotions.addAll(p.allPromotions);
			}
		}
		promotion.allPromotions = Collections.unmodifiableSet(promotion.allPromotions);
	}
}
