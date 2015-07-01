package edu.uci.isr.archstudio4.comp.aim;

import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxUtils;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public class AIMMyxComponent extends AbstractMyxSimpleBrick{

	public static final IMyxName INTERFACE_NAME_IN_AIM = MyxUtils.createName("aim");
	public static final IMyxName INTERFACE_NAME_OUT_XARCH = MyxUtils.createName("xarch");

	protected AIMImpl aim;

	protected XArchFlatInterface xarch = null;

	public AIMMyxComponent(){
	}

	public void init(){
		xarch = (XArchFlatInterface)MyxUtils.getFirstRequiredServiceObject(this, INTERFACE_NAME_OUT_XARCH);
		aim = new AIMImpl(xarch);
	}

	public Object getServiceObject(IMyxName interfaceName){
		if(interfaceName.equals(INTERFACE_NAME_IN_AIM)){
			return aim;
		}
		return null;
	}

}
