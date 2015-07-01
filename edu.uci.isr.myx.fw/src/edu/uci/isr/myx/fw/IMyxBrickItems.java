package edu.uci.isr.myx.fw;

public interface IMyxBrickItems{
	public IMyxName getBrickName();
	public IMyxBrickDescription getBrickDescription();
	public IMyxRequiredServiceProvider getRequiredServiceProvider();
	public IMyxInterfaceManager getInterfaceManager();
	public IMyxClassManager getClassManager();
}
