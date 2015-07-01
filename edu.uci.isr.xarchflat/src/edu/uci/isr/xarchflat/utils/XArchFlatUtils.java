package edu.uci.isr.xarchflat.utils;

import edu.uci.isr.sysutils.SystemUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatEvent;
import edu.uci.isr.xarchflat.XArchFlatInterface;
import edu.uci.isr.xarchflat.XArchFlatQueryInterface;
import edu.uci.isr.xarchflat.XArchPath;

public class XArchFlatUtils{

	public static final Object[] EMPTY_ARRAY = new Object[0];

	//	public static boolean isLink(IXArchTypeMetadata type) {
	//		// return XArchMetadataUtils.isAssignable(type, IXMLLink.TYPE_METADATA);
	//		return false;
	//	}

	public static String getTrueName(XArchPath path, int index){
		if(index < 0){
			index += path.getLength();
		}
		switch(index){
		case 0:
			return null;
		case 1:
			return "object";
		default:
			return path.getTagName(index);
		}
	}

	public static String getTrueName(String tagsOnlyPath, int index){
		if(index < 0){
			index += tagsOnlyPath.split("\\/").length;
		}
		switch(index){
		case 0:
			return null;
		case 1:
			return "object";
		default:
			return tagsOnlyPath.split("\\/", index + 2)[index];
		}
	}

	public static String getTrueName(String[] tags, int index){
		if(index < 0){
			index += tags.length;
		}
		switch(index){
		case 0:
			return null;
		case 1:
			return "object";
		default:
			return tags[index];
		}
	}

	public static String getTrueTargetName(XArchFlatEvent evt){
		switch(evt.getSourceType()){
		case XArchFlatEvent.ATTRIBUTE_CHANGED:
			return evt.getTargetName();
		case XArchFlatEvent.SIMPLE_TYPE_VALUE_CHANGED:
			return "value";
		}
		return null;
	}

	@Deprecated
	public static final String capFirstLetter(String s){
		return SystemUtils.capFirst(s);
	}

	public static boolean equals(Object o1, Object o2){
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	public static int bound(int min, int value, int max){
		if(value < min){
			return min;
		}
		if(value > max){
			return max;
		}
		return value;
	}

	public static String getAttribute(XArchFlatQueryInterface xArch, ObjRef objRef, String name, String nullValue){
		String value = null;
		try{
			if(objRef != null){
				value = (String)xArch.get(objRef, name);
			}
		}
		catch(Exception e){
		}
		if(value == null){
			value = nullValue;
		}
		return value;
	}

	public static String getSimpleTypeValue(XArchFlatQueryInterface xArch, ObjRef objRef, String name, String attribute, String nullValue){
		String value = null;
		try{
			ObjRef simpleTypeRef = (ObjRef)xArch.get(objRef, name);
			if(simpleTypeRef != null){
				value = (String)xArch.get(simpleTypeRef, attribute);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		if(value == null){
			value = nullValue;
		}
		return value;
	}

	public static String getSimpleTypeValue(XArchFlatQueryInterface xArch, ObjRef objRef, String simpleTypeName, String nullValue){
		return getSimpleTypeValue(xArch, objRef, simpleTypeName, "value", nullValue);
	}

	public static String getLinkHref(XArchFlatQueryInterface xArch, ObjRef objRef, String linkName, String nullValue){
		String value = null;
		try{
			ObjRef linkRef = (ObjRef)xArch.get(objRef, linkName);
			if(linkRef != null){
				String type = (String)xArch.get(linkRef, "type");
				if(type != null && type.equals("simple")){
					value = (String)xArch.get(linkRef, "href");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		if(value == null){
			value = nullValue;
		}
		return value;
	}

	public static void setLink(XArchFlatInterface xArch, ObjRef objRef, String linkName, String type, String href){
		ObjRef linkRef = (ObjRef)xArch.get(objRef, linkName);
		if(linkRef == null){
			ObjRef xArchRef = xArch.getXArch(objRef);
			ObjRef instanceContextRef = xArch.createContext(xArchRef, "instance");
			linkRef = xArch.create(instanceContextRef, "XMLLink");
			xArch.set(objRef, linkName, linkRef);
		}
		xArch.set(linkRef, "type", type);
		xArch.set(linkRef, "href", href);
	}

	public static String getDescriptionValue(XArchFlatInterface xArch, ObjRef objRef, String nullValue){
		return getSimpleTypeValue(xArch, objRef, "description", nullValue);
	}

	public static void setDescription(XArchFlatInterface xArch, ObjRef objRef, String descriptionName, String description){
		ObjRef descriptionRef = (ObjRef)xArch.get(objRef, "description");
		if(descriptionRef == null){
			ObjRef xArchRef = xArch.getXArch(objRef);
			ObjRef typesContextRef = xArch.createContext(xArchRef, "types");
			descriptionRef = xArch.create(typesContextRef, "description");
			xArch.set(objRef, "description", descriptionRef);
		}
		xArch.set(descriptionRef, "value", description);
	}

	public static String[] getIDs(XArchFlatQueryInterface xArch, ObjRef[] objRefs){
		String[] ids = new String[objRefs.length];
		for(int i = 0; i < ids.length; i++){
			ids[i] = (String)xArch.get(objRefs[i], "id");
		}
		return ids;
	}
}
