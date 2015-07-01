package edu.uci.isr.myx.fw;

public class MyxBasicImplementation implements IMyxImplementation{
	protected MyxBasicImplementation(){
	}

	public IMyxRuntime createRuntime(){
		return new MyxBasicRuntime();
	}
	
	

}
