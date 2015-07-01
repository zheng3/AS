package edu.uci.isr.bna4;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public abstract class AbstractThingPeer
	implements IThingPeer{

	protected IThing t;

	public AbstractThingPeer(IThing thing){
		this.t = thing;
	}

	public abstract void draw(IBNAView view, GC g);

	public abstract boolean isInThing(IBNAView view, int worldX, int worldY);

	protected Display getDisplay(){
		Display d = Display.getCurrent();
		if(d != null){
			return d;
		}
		return Display.getDefault();
	}
}
