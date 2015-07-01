package edu.uci.isr.myx.fw;

import java.util.*;

public abstract class AbstractMyxFlexibleBrick implements IMyxBrick{

	private IMyxBrickItems brickItems = null;
	protected List<IMyxLifecycleProcessor> lifecycleProcessors = 
		Collections.synchronizedList(new ArrayList<IMyxLifecycleProcessor>());
	protected MyxBasicProvidedServiceProvider providedServiceProvider = new MyxBasicProvidedServiceProvider();
	
	public void addLifecycleProcessor(IMyxLifecycleProcessor lp){
		lifecycleProcessors.add(lp);
	}
	
	public void removeLifecycleProcessor(IMyxLifecycleProcessor lp){
		lifecycleProcessors.remove(lp);
	}
	
	public IMyxLifecycleProcessor[] getLifecycleProcessors(){
		return lifecycleProcessors.toArray(new IMyxLifecycleProcessor[lifecycleProcessors.size()]);
	}
	
	public void setMyxBrickItems(IMyxBrickItems brickItems){
		this.brickItems = brickItems;
	}
	
	public IMyxBrickItems getMyxBrickItems(){
		return brickItems;
	}

	public IMyxProvidedServiceProvider getProvidedServiceProvider(){
		return providedServiceProvider;
	}
}
