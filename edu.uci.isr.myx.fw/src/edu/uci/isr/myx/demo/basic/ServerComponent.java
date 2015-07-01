package edu.uci.isr.myx.demo.basic;

import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxUtils;

public class ServerComponent extends AbstractMyxSimpleBrick{
	public static final IMyxName PROVIDED_INTERFACE_NAME = MyxUtils.createName("math");

	protected IMath math;
	
	public ServerComponent(){
		this.math = new MathImpl();
	}

	public Object getServiceObject(IMyxName interfaceName){
		if(interfaceName.equals(PROVIDED_INTERFACE_NAME)){
			return math;
		}
		return null;
	}
	
	class MathImpl implements IMath{
		public int add(int a, int b){
			return a+b;
		}
		
		public int mul(int a, int b){
			return a*b;
		}

		public int sub(int a, int b){
			return a-b;
		}
	}

}
