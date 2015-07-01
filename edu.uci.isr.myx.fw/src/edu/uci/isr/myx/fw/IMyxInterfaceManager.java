package edu.uci.isr.myx.fw;

public interface IMyxInterfaceManager{
	public void addInterface(IMyxName interfaceName, IMyxInterfaceDescription interfaceDescription, EMyxInterfaceDirection interfaceDirection);
	public void removeInterface(IMyxName interfaceName);
	public IMyxName[] getAllInterfaceNames();
	public IMyxInterfaceDescription getInterfaceDescription(IMyxName interfaceName);
	public EMyxInterfaceDirection getInterfaceDirection(IMyxName interfaceName);
}