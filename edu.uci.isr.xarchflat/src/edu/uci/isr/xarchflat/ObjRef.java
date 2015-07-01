package edu.uci.isr.xarchflat;

public final class ObjRef
	implements java.io.Serializable{

	private static long nextUidNum = 1000;

	private int hc;
	private String uid;

	public ObjRef(){
		uid = "obj" + nextUidNum++;
		hc = uid.hashCode();
	}

	public ObjRef(String uid){
		this.uid = uid;
		hc = uid.hashCode();
	}

	public String getUID(){
		return uid;
	}

	//	public ObjRef duplicate(){
	//		return new ObjRef(uid);
	//	}

	@Override
	public int hashCode(){
		return hc;
	}

	@Override
	public boolean equals(Object otherObjRef){
		if(this == otherObjRef){
			return true;
		}
		if(!(otherObjRef instanceof ObjRef)){
			return false;
		}

		return ((ObjRef)otherObjRef).uid.equals(uid);
	}

	@Override
	public String toString(){
		return "ObjRef[" + uid + "]";
	}
}
