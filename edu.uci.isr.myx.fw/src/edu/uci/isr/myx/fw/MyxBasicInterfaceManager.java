package edu.uci.isr.myx.fw;

public class MyxBasicInterfaceManager implements IMyxInterfaceManager{

	protected IMyxRuntime runtime;
	protected IMyxName[] path;
	protected IMyxName brickName;
	
	public MyxBasicInterfaceManager(IMyxRuntime runtime, IMyxName[] path, IMyxName brickName){
		this.runtime = runtime;
		this.path = path;
		this.brickName = brickName;
	}
	
	public void addInterface(IMyxName interfaceName, IMyxInterfaceDescription interfaceDescription, EMyxInterfaceDirection interfaceDirection){
		runtime.addInterface(path, brickName, interfaceName, interfaceDescription, interfaceDirection);
	}
	
	public void removeInterface(IMyxName interfaceName){
		runtime.removeInterface(path, brickName, interfaceName);
	}

	public IMyxName[] getAllInterfaceNames() {
		return runtime.getAllInterfaceNames(path, brickName);
	}
	
	public IMyxInterfaceDescription getInterfaceDescription(IMyxName interfaceName) {
		return runtime.getInterfaceDescription(path, brickName, interfaceName);
	}

	public EMyxInterfaceDirection getInterfaceDirection(IMyxName interfaceName) {
		return runtime.getInterfaceDirection(path, brickName, interfaceName);
	}
}
