package edu.uci.isr.myx.fw;

import java.util.Arrays;

class MyxBasicWeld implements IMyxWeld{

	protected IMyxName[] requiredPath;
	protected IMyxName requiredBrickName;
	protected IMyxName requiredInterfaceName;
	
	protected IMyxName[] providedPath;
	protected IMyxName providedBrickName;
	protected IMyxName providedInterfaceName;

	public MyxBasicWeld(
		IMyxName[] requiredPath, 
		IMyxName requiredBrickName, 
		IMyxName requiredInterfaceName, 
		IMyxName[] providedPath,
		IMyxName providedBrickName, 
		IMyxName providedInterfaceName){
		
		this.requiredPath = requiredPath;
		this.requiredBrickName = requiredBrickName;
		this.requiredInterfaceName = requiredInterfaceName;
		this.providedPath = providedPath;
		this.providedBrickName = providedBrickName;
		this.providedInterfaceName = providedInterfaceName;
	}

	public IMyxName getProvidedBrickName(){
		return providedBrickName;
	}

	public IMyxName getProvidedInterfaceName(){
		return providedInterfaceName;
	}

	public IMyxName[] getProvidedPath(){
		return providedPath;
	}

	public IMyxName getRequiredBrickName(){
		return requiredBrickName;
	}

	public IMyxName getRequiredInterfaceName(){
		return requiredInterfaceName;
	}

	public IMyxName[] getRequiredPath(){
		return requiredPath;
	}
	
	public int hashCode(){
		return 
			MyxUtils.hc(requiredPath) ^
			MyxUtils.hc(requiredBrickName) ^
			MyxUtils.hc(requiredInterfaceName) ^
			MyxUtils.hc(providedPath) ^
			MyxUtils.hc(providedBrickName) ^
			MyxUtils.hc(providedInterfaceName);
	}
	
	public boolean equals(Object o){
		return (o == this) ||
			MyxUtils.classeq(this, o) &&
			Arrays.equals(this.requiredPath, ((MyxBasicWeld)o).requiredPath) &&
			MyxUtils.nulleq(this.requiredBrickName, ((MyxBasicWeld)o).requiredBrickName) &&
			MyxUtils.nulleq(this.requiredInterfaceName, ((MyxBasicWeld)o).requiredInterfaceName) &&
			Arrays.equals(this.providedPath, ((MyxBasicWeld)o).providedPath) &&
			MyxUtils.nulleq(this.providedBrickName, ((MyxBasicWeld)o).providedBrickName) &&
			MyxUtils.nulleq(this.providedInterfaceName, ((MyxBasicWeld)o).providedInterfaceName);
			
	}
	
	public String toString(){
		return "MyxBasicWeld{requiredPath=" + MyxUtils.pathToString(requiredPath) + 
			"; requiredBrickName=" + requiredBrickName + 
			"; requiredInterfaceName=" + requiredInterfaceName +
			"; providedPath=" + MyxUtils.pathToString(providedPath) +
			"; providedBrickName=" + providedBrickName + 
			"; providedInterfaceName=" + providedInterfaceName +
			"}";
			
	}
	
	
}
