package edu.uci.isr.myx.fw;

public interface IMyxBrick{
	public void setMyxBrickItems(IMyxBrickItems brickItems);
	public IMyxBrickItems getMyxBrickItems();
	
	public IMyxLifecycleProcessor[] getLifecycleProcessors();
	public IMyxProvidedServiceProvider getProvidedServiceProvider();
}
