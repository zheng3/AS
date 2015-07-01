package edu.uci.isr.myx.fw;

public abstract class AbstractMyxSimpleBrick implements IMyxBrick, IMyxLifecycleProcessor, IMyxProvidedServiceProvider{

	private IMyxBrickItems brickItems = null;

	public IMyxLifecycleProcessor[] getLifecycleProcessors(){
		return new IMyxLifecycleProcessor[]{this};
	}
	
	public void setMyxBrickItems(IMyxBrickItems brickItems){
		this.brickItems = brickItems;
	}
	
	public IMyxBrickItems getMyxBrickItems(){
		return brickItems;
	}
	
	public IMyxProvidedServiceProvider getProvidedServiceProvider(){
		return this;
	}
	
	public void init(){}
	public void begin(){}
	public void end(){}
	public void destroy(){}
}
