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

import java.util.*;

public class XArchMetadataUtils {

	private static final Object[] EMPTY_ARRAY = new Object[0];

	private static final List EMPTY_LIST = Arrays.asList(EMPTY_ARRAY);

	private static final Set EMPTY_SET = Collections.unmodifiableSet(new HashSet(EMPTY_LIST));

	private static boolean inited = false;

	private static List topLevelElements;

	private static Hashtable promotionsTable;

	private static HashMap typesMap = new HashMap();
	static {
		typesMap.put("#", IXArchContext.TYPE_METADATA);
		typesMap.put(IXArchContext.TYPE_METADATA, IXArchContext.class);
		typesMap.put("#XArch", IXArch.TYPE_METADATA);
		typesMap.put(IXArch.TYPE_METADATA, IXArch.class);
		typesMap.put("#XArchElement", IXArchElement.TYPE_METADATA);
		typesMap.put(IXArchElement.TYPE_METADATA, IXArchElement.class);
	}

	private static class Promotion {
		public final XArchTypeMetadata type;

		public List promotions;

		public Set allPromotions;

		public Promotion(XArchTypeMetadata type) {
			this.type = type;
			this.promotions = new ArrayList();
		}

		public boolean equals(Object o) {
			return type.equals(o);
		}

		public int hashCode() {
			return type.hashCode();
		}
	}

	private static Class getTypeClass(XArchTypeMetadata type){
		Class clazz = (Class)typesMap.get(type);
		try{
			if(clazz == null){
				String packageName = XArchUtils.getPackageName(type.getTypePackage());
				String className = XArchUtils.getInterfaceName(packageName, type.getTypeName());
				clazz = Class.forName(className);
				typesMap.put(type, clazz);
			}
		}catch(Exception e){}
		return clazz;
	}
	
	/**
	 * Returns the type metadata describing the specified type.
	 * 
	 * @param packageTitle
	 *            package containing the type, or <code>null</code> if the
	 *            type has no package
	 * @param typeName
	 *            the type to return
	 * @return type metadata describing the specified type
	 */
	public static XArchTypeMetadata getTypeMetadata(String packageTitle, String typeName) {
		try {
			String compoundType = (packageTitle == null ? "" : packageTitle) + ":" + (typeName == null ? "" : typeName);
			XArchTypeMetadata type = (XArchTypeMetadata) typesMap.get(compoundType);
			if (type == null) {
				String className;
				if (typeName != null)
					className = XArchUtils.getInterfaceName(XArchUtils.getPackageName(packageTitle), typeName);
				else
					className = XArchUtils.getInterfaceName(XArchUtils.getPackageName(packageTitle), capFirstLetter(packageTitle) + "Context");
				Class typeClass = Class.forName(className);
				type = (XArchTypeMetadata) typeClass.getDeclaredField("TYPE_METADATA").get(null);
			}
			return type;
		} catch (Exception e) {
		}
		return null;
	}

    /**
     * Determines if the type represented by <code>fromType</code> is 
     * either the same as, or is a supertype of, the type represented by 
     * <code>toType</code>. It returns <code>true</code> if so;
     * otherwise it returns <code>false</code>.
     *  
     * @return <code>true</code> if <code>fromType</code> is either
     * the same as, or is a supertype of, <code>toType</code>.
     */
	
	public static boolean isAssignable(XArchTypeMetadata fromType, XArchTypeMetadata toType) {
		Class fromClass = getTypeClass(fromType);
		Class toClass = getTypeClass(toType);
		return fromClass.isAssignableFrom(toClass);
	}

	/**
	 * Returns a collection of all top level elements.
	 * 
	 * @return a collection of all top level elements
	 */
	public static Collection getTopLevelElements() {
		init();
		return topLevelElements;
	}

	/**
	 * Returns the parent type's metadata, or <code>null</code> if non exists.
	 * 
	 * @param type
	 *            the child metadata from which to determine the parent
	 * @return the parent type's metadata, or <code>null</code> if non exists.
	 */
	public static XArchTypeMetadata getParent(XArchTypeMetadata type) {
		return getTypeMetadata(type.getParentTypePackage(), type.getParentTypeName());
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
	public static Set getAvailablePromotions(XArchTypeMetadata type) {
		init();
		Promotion p = (Promotion) promotionsTable.get(type);
		if (p == null)
			return EMPTY_SET;
		return p.allPromotions;

	}

	/**
	 * Returns a list of <code>XArchTypeMetadata</code> involved in promoting
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
	public static List getPromotionPathTypes(XArchTypeMetadata fromType, XArchTypeMetadata toType) {
		if (isAssignable(toType, fromType))
			return EMPTY_LIST;
		Set fromPromotions = getAvailablePromotions(fromType);
		if (!fromPromotions.contains(toType))
			return null;

		ArrayList types = new ArrayList();
		while (!toType.equals(fromType)) {
			types.add(0, toType);
			toType = getParent(toType);
		}
		return types;
	}

	private static String capFirstLetter(String s) {
		if (s == null) {
			return null;
		} else if (s.length() == 0) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		sb.append(Character.toUpperCase(s.charAt(0)));
		sb.append(s.substring(1));
		return sb.toString();
	}

	private static void calculateAllPromotions(Promotion b) {
		if (b.allPromotions != null)
			return;
		b.allPromotions = new HashSet();
		for (Iterator i = b.promotions.iterator(); i.hasNext();) {
			XArchTypeMetadata pt = (XArchTypeMetadata) i.next();
			b.allPromotions.add(pt);

			Promotion p = (Promotion) promotionsTable.get(pt);
			if (p != null) {
				calculateAllPromotions(p);
				b.allPromotions.addAll(p.allPromotions);
			}
		}
		b.allPromotions = Collections.unmodifiableSet(b.allPromotions);
	}

	private static synchronized void init() {
		if (inited) {
			return;
		}
		try {
			promotionsTable = new Hashtable();
			topLevelElements = new Vector();

			String[] packageNames = XArchUtils.getPackageNames();
			for (int p = 0; p < packageNames.length; p++) {
				String packageTitle = packageNames[p].substring(packageNames[p].lastIndexOf(".") + 1);
				XArchTypeMetadata contextType = getTypeMetadata(packageTitle, null);

				for (Iterator i = contextType.getActions(); i.hasNext();) {
					XArchActionMetadata action = (XArchActionMetadata) i.next();
					XArchTypeMetadata input = action.getInputTypeMetadata();
					XArchTypeMetadata output = action.getOutputTypeMetadata();
					switch (action.getType()) {

					case XArchActionMetadata.CREATE_ELEMENT:
						topLevelElements.add(output);
						break;

					case XArchActionMetadata.PROMOTE:
						addPromotion(input, output);
						break;
					}
				}
			}
			for (Iterator i = promotionsTable.values().iterator(); i.hasNext();) {
				Promotion p = (Promotion) i.next();
				calculateAllPromotions(p);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		topLevelElements = Collections.unmodifiableList(topLevelElements);

		inited = true;
	}

	private static void addPromotion(XArchTypeMetadata type, XArchTypeMetadata promotionType) {
		Promotion promotion = (Promotion) promotionsTable.get(type);
		if (promotion == null) {
			promotion = new Promotion(type);
			// System.err.println(" <- " + type);
			promotionsTable.put(type, promotion);
		}
		promotion.promotions.add(promotionType);
	}

//	public static void main(String[] args) {
//		try {
//			init();
//			ArrayList list = new ArrayList();
//			Object[] list2;
//
//			System.err.println();
//			list.clear();
//			for (Iterator i = topLevelElements.iterator(); i.hasNext();) {
//				XArchTypeMetadata t = (XArchTypeMetadata) i.next();
//				list.add("" + t);
//			}
//			list2 = list.toArray();
//			Arrays.sort(list2);
//			for (int i = 0; i < list2.length; i++)
//				System.err.println(list2[i]);
//			
//			System.err.println();
//			list.clear();
//			for (Iterator i = promotionsTable.values().iterator(); i.hasNext();) {
//				Promotion p = (Promotion) i.next();
//				list.add("" + p.type + " -> " + p.allPromotions);
//			}
//			list2 = list.toArray();
//			Arrays.sort(list2);
//			for (int i = 0; i < list2.length; i++)
//				System.err.println(list2[i]);
//			
//			System.err.println();
//			System.err.println(getPromotionPathTypes(IComponentType.TYPE_METADATA, IVariantComponentTypeImplVersSpec.TYPE_METADATA));
//			System.err.println(getPromotionPathTypes(IComponentType.TYPE_METADATA, IVariantComponentTypeImplVers.TYPE_METADATA));
//			System.err.println(getPromotionPathTypes(IComponentType.TYPE_METADATA, IVariantComponentTypeImpl.TYPE_METADATA));
//			System.err.println(getPromotionPathTypes(IComponentType.TYPE_METADATA, IVariantComponentType.TYPE_METADATA));
//			System.err.println(getPromotionPathTypes(IComponentType.TYPE_METADATA, IComponentType.TYPE_METADATA));
//			System.err.println(getPromotionPathTypes(IComponentType.TYPE_METADATA, IXArchElement.TYPE_METADATA));
//			System.err.println(getPromotionPathTypes(IComponentType.TYPE_METADATA, IConnectorType.TYPE_METADATA));
//			System.err.println();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
