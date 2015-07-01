package edu.uci.isr.bna4.things.utility;

import org.eclipse.swt.graphics.GC;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.IThingPeer;

public class NoThingPeer extends AbstractThingPeer implements IThingPeer{

	public NoThingPeer(IThing t){
		super(t);
	}
	
	public void draw(IBNAView view, GC g){}

	public boolean isInThing(IBNAView view, int worldX, int worldY){
		return false;
	}
}
