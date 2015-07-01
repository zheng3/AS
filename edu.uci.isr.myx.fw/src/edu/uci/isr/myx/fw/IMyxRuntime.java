package edu.uci.isr.myx.fw;

import java.util.Properties;

public interface IMyxRuntime{
	public void addBrickLoader(IMyxName loaderName, String className, Properties initParams) throws MyxBrickLoaderException;
	public void removeBrickLoader(IMyxName loaderName);
	public IMyxName[] getBrickLoaderNames();
	
	public void addBrick(IMyxName[] path, IMyxName brickName, IMyxBrickDescription brickDescription) throws MyxBrickLoadException, MyxBrickCreationException;
	public void removeBrick(IMyxName[] path, IMyxName brickName);
	public IMyxName[] getAllBrickNames(IMyxName[] path);
	public IMyxBrickDescription getBrickDescription(IMyxName[] path, IMyxName brickName);
	
	public void addInterface(IMyxName[] path, IMyxName brickName, IMyxName interfaceName, IMyxInterfaceDescription interfaceDescription, EMyxInterfaceDirection interfaceDirection);
	public void addContainerInterface(IMyxName[] path, IMyxName brickName, IMyxName interfaceName, IMyxInterfaceDescription interfaceDescription, EMyxInterfaceDirection interfaceDirection, IMyxName internalBrickName, IMyxName internalBrickInterfaceName);
	public void removeInterface(IMyxName[] path, IMyxName brickName, IMyxName interfaceName);
	public IMyxName[] getAllInterfaceNames(IMyxName[] path, IMyxName brickName);
	public IMyxInterfaceDescription getInterfaceDescription(IMyxName[] path, IMyxName brickName, IMyxName interfaceName);
	public EMyxInterfaceDirection getInterfaceDirection(IMyxName[] path, IMyxName brickName, IMyxName interfaceName);
	
	public void init(IMyxName[] path, IMyxName brickName);
	public void begin(IMyxName[] path, IMyxName brickName);
	public void end(IMyxName[] path, IMyxName brickName);
	public void destroy(IMyxName[] path, IMyxName brickName);
	
	public IMyxWeld createWeld(
		IMyxName[] requiredPath, 
		IMyxName requiredBrickName, 
		IMyxName requiredInterfaceName, 
		IMyxName[] providedPath,
		IMyxName providedBrickName, 
		IMyxName providedInterfaceName);

	public void addWeld(IMyxWeld weld);
	public void removeWeld(IMyxWeld weld);
	public IMyxWeld[] getAllWelds();	
}
