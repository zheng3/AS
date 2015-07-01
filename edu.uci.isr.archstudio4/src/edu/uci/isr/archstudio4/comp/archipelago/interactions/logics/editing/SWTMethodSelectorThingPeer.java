package edu.uci.isr.archstudio4.comp.archipelago.interactions.logics.editing;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.things.swt.AbstractSWTTreeThingPeer;

public class SWTMethodSelectorThingPeer extends AbstractSWTTreeThingPeer {

	protected SWTMethodSelectorThing lt;
	
	public SWTMethodSelectorThingPeer(IThing t){
		super(t);
		System.out.println("DDDD");
		if(!(t instanceof SWTMethodSelectorThing)){
			throw new IllegalArgumentException("SWTMethodSelectorThingPeer can only peer for SWTMethodSelectorThing");
		}
		this.lt = (SWTMethodSelectorThing)t;
	}
	
	protected Object getInput(){
		return lt.getMethodList();
	}
	
	protected ITreeContentProvider getContentProvider(){
		return new MethodContentProvider(lt.getMethodList());
	}
	
	protected ILabelProvider getLabelProvider(){
		return new MethodLabelProvider(lt.getResources());
	}

}
