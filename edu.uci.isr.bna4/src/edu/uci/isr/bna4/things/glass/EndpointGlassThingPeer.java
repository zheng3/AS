package edu.uci.isr.bna4.things.glass;

import org.eclipse.swt.graphics.GC;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;

public class EndpointGlassThingPeer
	extends AbstractThingPeer{

	protected EndpointGlassThing t;

	public EndpointGlassThingPeer(IThing t){
		super(t);
		this.t = (EndpointGlassThing)t;
	}

	@Override
	public void draw(IBNAView view, GC g){
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		return BNAUtils.isWithin(t.getBoundingBox(), worldX, worldY);
	}
}
