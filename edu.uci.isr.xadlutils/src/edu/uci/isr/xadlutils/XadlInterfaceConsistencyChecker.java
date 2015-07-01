package edu.uci.isr.xadlutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatQueryInterface;

public class XadlInterfaceConsistencyChecker{

	public static final int OK = 0;
	public static final int INTERFACE_MISSING_DESCRIPTION = (1 << 0);
	public static final int INTERFACE_MISSING_DIRECTION = (1 << 1);
	public static final int INTERFACE_INVALID_DIRECTION = (1 << 2);
	public static final int INTERFACE_MISSING_TYPE = (1 << 3);
	public static final int INTERFACE_INVALID_TYPE = (1 << 4);
	public static final int INTERFACE_MISSING_SIGNATURE = (1 << 5);
	public static final int INTERFACE_INVALID_SIGNATURE = (1 << 6);
	public static final int SIGNATURE_MISSING_DESCRIPTION = (1 << 7);
	public static final int SIGNATURE_MISSING_DIRECTION = (1 << 8);
	public static final int SIGNATURE_INVALID_DIRECTION = (1 << 9);
	public static final int SIGNATURE_MISSING_TYPE = (1 << 10);
	public static final int SIGNATURE_INVALID_TYPE = (1 << 11);
	public static final int DIRECTION_MISMATCH = (1 << 12);
	public static final int TYPE_MISMATCH = (1 << 13);
	public static final int SIGNATURE_ON_WRONG_TYPE = (1 << 14);

	protected static final Map<Integer,String> statusToStringMap = new HashMap<Integer,String>();
	
	static{
		statusToStringMap.put(new Integer(INTERFACE_MISSING_DESCRIPTION), "Interface missing description.");
		statusToStringMap.put(new Integer(INTERFACE_MISSING_DIRECTION), "Interface missing direction.");
		statusToStringMap.put(new Integer(INTERFACE_INVALID_DIRECTION), "Invalid interface direction.");
		statusToStringMap.put(new Integer(INTERFACE_MISSING_TYPE), "Interface missing type.");
		statusToStringMap.put(new Integer(INTERFACE_INVALID_TYPE), "Invalid interface type.");
		statusToStringMap.put(new Integer(INTERFACE_MISSING_SIGNATURE), "Interface missing signature.");
		statusToStringMap.put(new Integer(INTERFACE_INVALID_SIGNATURE), "Invalid signature."); 
		statusToStringMap.put(new Integer(SIGNATURE_MISSING_DESCRIPTION), "Signature missing description");
		statusToStringMap.put(new Integer(SIGNATURE_MISSING_DIRECTION), "Signature missing direction.");
		statusToStringMap.put(new Integer(SIGNATURE_INVALID_DIRECTION), "Invalid signature direction");
		statusToStringMap.put(new Integer(SIGNATURE_MISSING_TYPE), "Signature missing type.");
		statusToStringMap.put(new Integer(SIGNATURE_INVALID_TYPE), "Invalid signature type.");
		statusToStringMap.put(new Integer(DIRECTION_MISMATCH), "Interface and signature directions don't match.");
		statusToStringMap.put(new Integer(TYPE_MISMATCH), "Interface and signature types don't match.");
		statusToStringMap.put(new Integer(SIGNATURE_ON_WRONG_TYPE), "Signature on wrong brick type.");
	}
	
	public static int check(XArchFlatQueryInterface xarch, ObjRef interfaceRef){
		int flags = OK;
		
		String interfaceDescription = XadlUtils.getDescription(xarch, interfaceRef);
		if(interfaceDescription == null){
			flags |= INTERFACE_MISSING_DESCRIPTION;
		}
		
		String interfaceDirection = XadlUtils.getDirection(xarch, interfaceRef);
		if(interfaceDirection == null){
			flags |= INTERFACE_MISSING_DIRECTION;
		}
		else if(!isValidDirection(interfaceDirection)){
			flags |= INTERFACE_INVALID_DIRECTION;
		}
		
		ObjRef interfaceTypeRef = XadlUtils.resolveXLink(xarch, interfaceRef, "type");
		if(interfaceTypeRef == null){
			flags |= INTERFACE_MISSING_TYPE;
		}
		else{
			if(!xarch.isInstanceOf(interfaceTypeRef, "types#InterfaceType")){
				flags |= INTERFACE_INVALID_TYPE;
			}
		}
		
		ObjRef signatureRef = XadlUtils.resolveXLink(xarch, interfaceRef, "signature");
		if(signatureRef == null){
			flags |= INTERFACE_MISSING_SIGNATURE;
		}
		else{
			if(!xarch.isInstanceOf(signatureRef, "types#Signature")){
				flags |= INTERFACE_INVALID_SIGNATURE;
			}
			else{
				String signatureDescription = XadlUtils.getDescription(xarch, signatureRef);
				if(signatureDescription == null){
					flags |= SIGNATURE_MISSING_DESCRIPTION;
				}
				
				String signatureDirection = XadlUtils.getDirection(xarch, signatureRef);
				if(signatureDirection == null){
					flags |= SIGNATURE_MISSING_DIRECTION;
				}
				else if(!isValidDirection(signatureDirection)){
					flags |= SIGNATURE_INVALID_DIRECTION;
				}

				if((interfaceDirection != null) && (signatureDirection != null) && (!interfaceDirection.equals(signatureDirection))){
					flags |= DIRECTION_MISMATCH;
				}
				
				ObjRef signatureTypeRef = XadlUtils.resolveXLink(xarch, signatureRef, "type");
				if(signatureTypeRef == null){
					flags |= SIGNATURE_MISSING_TYPE;
				}
				else{
					if(!xarch.isInstanceOf(signatureTypeRef, "types#InterfaceType")){
						flags |= SIGNATURE_INVALID_TYPE;
					}
					else{
						if((interfaceTypeRef != null) && (!xarch.isEqual(interfaceTypeRef, signatureTypeRef))){
							flags |= TYPE_MISMATCH;
						}
					}
				}
				
				ObjRef interfaceBrickRef = xarch.getParent(interfaceRef);
				ObjRef signatureBrickTypeRef = xarch.getParent(signatureRef);
				
				if((interfaceBrickRef != null) && (signatureBrickTypeRef != null)){
					ObjRef interfaceBrickTypeRef = XadlUtils.resolveXLink(xarch, interfaceBrickRef, "type");
					if(interfaceBrickTypeRef == null){
						flags |= SIGNATURE_ON_WRONG_TYPE;
					}
					else{
						if(!xarch.isEqual(interfaceBrickTypeRef, signatureBrickTypeRef)){
							flags |= SIGNATURE_ON_WRONG_TYPE;
						}
					}
				}
			}
		}
		return flags;
	}
	
	public static String[] statusToStrings(int status){
		List<String> l = new ArrayList<String>();
		for(Iterator it = statusToStringMap.keySet().iterator(); it.hasNext(); ){
			Integer integer = (Integer)it.next();
			int i = integer.intValue();
			if((status & i) != 0){
				l.add(statusToStringMap.get(integer));
			}
		}
		return (String[])l.toArray(new String[l.size()]);
	}
	
	private static boolean isValidDirection(String direction){ 
		return (direction != null) && (direction.equals("none") ||
			direction.equals("in") || direction.equals("inout") || direction.equals("out"));
	}
	
	private XadlInterfaceConsistencyChecker(){}
}
