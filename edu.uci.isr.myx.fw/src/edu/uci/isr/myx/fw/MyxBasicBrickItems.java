package edu.uci.isr.myx.fw;

public class MyxBasicBrickItems implements IMyxBrickItems{

	protected IMyxName brickName;
	protected IMyxRequiredServiceProvider requiredServiceProvider;
	protected IMyxInterfaceManager interfaceManager;
	protected IMyxClassManager classManager;
	protected IMyxBrickDescription brickDescription;

	public MyxBasicBrickItems(IMyxName brickName, IMyxRequiredServiceProvider requiredServiceProvider, 
	IMyxInterfaceManager interfaceManager, IMyxClassManager classManager, IMyxBrickDescription brickDescription){
		this.brickName = brickName;
		this.requiredServiceProvider = requiredServiceProvider;
		this.interfaceManager = interfaceManager;
		this.classManager = classManager;
		this.brickDescription = brickDescription; 
	}

	public IMyxInterfaceManager getInterfaceManager(){
		return interfaceManager;
	}

	public void setInterfaceManager(IMyxInterfaceManager interfaceManager){
		this.interfaceManager = interfaceManager;
	}

	public IMyxRequiredServiceProvider getRequiredServiceProvider(){
		return requiredServiceProvider;
	}

	public void setRequiredServiceProvider(IMyxRequiredServiceProvider requiredServiceProvider){
		this.requiredServiceProvider = requiredServiceProvider;
	}

	public IMyxName getBrickName(){
		return brickName;
	}

	public void setBrickName(IMyxName brickName){
		this.brickName = brickName;
	}

	public IMyxClassManager getClassManager(){
		return classManager;
	}

	public void setClassManager(IMyxClassManager classManager){
		this.classManager = classManager;
	}

	public IMyxBrickDescription getBrickDescription() {
		return brickDescription;
	}

	public void setBrickDescription(IMyxBrickDescription brickDescription) {
		this.brickDescription = brickDescription;
	}
}
