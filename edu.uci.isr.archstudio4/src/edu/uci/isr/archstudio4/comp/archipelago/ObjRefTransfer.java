package edu.uci.isr.archstudio4.comp.archipelago;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import edu.uci.isr.xarchflat.ObjRef;

public class ObjRefTransfer extends ByteArrayTransfer{

	private static final String OBJREF_TYPE_NAME = "ObjRef";
	private static final int OBJREF_TYPE_ID = registerType(OBJREF_TYPE_NAME);

	private static ObjRefTransfer _instance = new ObjRefTransfer();

	private ObjRefTransfer(){
	}

	public static ObjRefTransfer getInstance(){
		return _instance;
	}

	public void javaToNative(Object object, TransferData transferData){
		if(object == null || !(object instanceof ObjRef[])){
			return;
		}
			
		if(isSupportedType(transferData)){
			ObjRef[] refs = (ObjRef[])object;

			StringBuffer sb = new StringBuffer(refs.length * 10);
			for(int i = 0; i < refs.length; i++){
				sb.append(refs[i].getUID());
				if(i < (refs.length - 1)){
					sb.append("$");
				}
			}
			super.javaToNative(sb.toString().getBytes(), transferData);
		}
	}

	public Object nativeToJava(TransferData transferData){
		if(isSupportedType(transferData)){
			byte[] buffer = (byte[])super.nativeToJava(transferData);
			if(buffer == null){
				return null;
			}
			
			String s = new String(buffer);
			String[] uids = s.split("\\$");
			ObjRef[] refs = new ObjRef[uids.length];
			for(int i = 0; i < uids.length; i++){
				refs[i] = new ObjRef(uids[i]);
			}
			return refs;
		}
		return null;
	}

	protected String[] getTypeNames(){
		return new String[]{ OBJREF_TYPE_NAME };
	}

	protected int[] getTypeIds(){
		return new int[]{ OBJREF_TYPE_ID };
	}
}
