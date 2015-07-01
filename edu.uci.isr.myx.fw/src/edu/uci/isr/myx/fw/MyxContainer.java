package edu.uci.isr.myx.fw;

import java.util.*;

public class MyxContainer implements IMyxContainer{

	protected Set<IMyxBrick> internalBricks = 
		Collections.synchronizedSet(new HashSet<IMyxBrick>());
	protected IMyxBrickItems brickItems = null;
	protected IMyxProvidedServiceProvider providedServiceProvider;
	
	protected IMyxLifecycleProcessor[] lifecycleProcessors = 
		new IMyxLifecycleProcessor[]{new MyxContainerLifecycleProcessor()};
	
	public MyxContainer(){
		providedServiceProvider = new MyxBasicProvidedServiceProvider();
	}
	
	public void setMyxBrickItems(IMyxBrickItems brickItems){
		this.brickItems = brickItems;
	}
	
	public IMyxBrickItems getMyxBrickItems(){
		return this.brickItems;
	}
	
	public void addInternalBrick(IMyxBrick brick){
		internalBricks.add(brick);
	}
	
	public void removeInternalBrick(IMyxBrick brick){
		internalBricks.remove(brick);
	}
	
	public IMyxBrick[] getInternalBricks(){
		return internalBricks.toArray(new IMyxBrick[internalBricks.size()]);
	}
	
	public IMyxLifecycleProcessor[] getLifecycleProcessors(){
		return lifecycleProcessors;
	}
	
	public IMyxProvidedServiceProvider getProvidedServiceProvider(){
		return providedServiceProvider;
	}
	
	public IMyxBrick getInternalBrick(IMyxName brickName){
		IMyxBrick[] bricks = getInternalBricks();
		for(int i = 0; i < bricks.length; i++){
			IMyxBrickItems brickItems = bricks[i].getMyxBrickItems();
			if(brickItems != null){
				IMyxName brickName2 = brickItems.getBrickName();
				if(brickName2 != null){
					if(brickName2.equals(brickName)){
						return bricks[i];
					}
				}
			}
		}
		return null;
	}
	
	class MyxContainerLifecycleProcessor implements IMyxLifecycleProcessor{
		public void init(){
			IMyxBrick[] bricks = getInternalBricks();
			for(int i = 0; i < bricks.length; i++){
				IMyxLifecycleProcessor[] lps = bricks[i].getLifecycleProcessors();
				if(lps != null){
					for(int j = 0; j < lps.length; j++){
						lps[j].init();
					}
				}
			}
		}

		public void begin(){
			IMyxBrick[] bricks = getInternalBricks();
			for(int i = 0; i < bricks.length; i++){
				IMyxLifecycleProcessor[] lps = bricks[i].getLifecycleProcessors();
				if(lps != null){
					for(int j = 0; j < lps.length; j++){
						lps[j].begin();
					}
				}
			}
		}
		
		public void end(){
			IMyxBrick[] bricks = getInternalBricks();
			for(int i = 0; i < bricks.length; i++){
				IMyxLifecycleProcessor[] lps = bricks[i].getLifecycleProcessors();
				if(lps != null){
					for(int j = 0; j < lps.length; j++){
						lps[j].end();
					}
				}
			}
		}

		public void destroy(){
			IMyxBrick[] bricks = getInternalBricks();
			for(int i = 0; i < bricks.length; i++){
				IMyxLifecycleProcessor[] lps = bricks[i].getLifecycleProcessors();
				if(lps != null){
					for(int j = 0; j < lps.length; j++){
						lps[j].destroy();
					}
				}
			}
		}
	}

}
