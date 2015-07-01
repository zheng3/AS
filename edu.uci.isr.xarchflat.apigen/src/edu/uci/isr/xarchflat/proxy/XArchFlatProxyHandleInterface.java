package edu.uci.isr.xarchflat.proxy;

import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public interface XArchFlatProxyHandleInterface {

	public ObjRef getObjRef();

	public void setObjRef(ObjRef objRef);

	public XArchFlatInterface getXArch();

	public void setXArch(XArchFlatInterface xArch);
}
