package edu.uci.isr.myx.fw;

public interface IMyxWeld extends java.io.Serializable{

	public IMyxName[] getRequiredPath();
	public IMyxName getRequiredBrickName();
	public IMyxName getRequiredInterfaceName();
	
	public IMyxName[] getProvidedPath();
	public IMyxName getProvidedBrickName();
	public IMyxName getProvidedInterfaceName();
}
