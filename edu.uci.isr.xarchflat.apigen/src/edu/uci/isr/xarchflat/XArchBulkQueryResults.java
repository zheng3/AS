package edu.uci.isr.xarchflat;

import edu.uci.isr.xarch.XArchTypeMetadata;

public class XArchBulkQueryResults extends XArchBulkQueryResultNode implements java.io.Serializable{

	protected ObjRef xArchRef;
	
	public XArchBulkQueryResults(String tagName, ObjRef rootResultRef, IXArchTypeMetadata typeMetadata, IXArchInstanceMetadata instanceMetadata, Class rootResultClass, String rootResultRefID, XArchPath xArchPath, ObjRef xArchRef){
		super(tagName, rootResultRef, typeMetadata, instanceMetadata, rootResultClass, rootResultRefID, xArchPath);
		this.xArchRef = xArchRef;
	}

	public ObjRef getXArchRef(){
		return xArchRef;
	}
}
