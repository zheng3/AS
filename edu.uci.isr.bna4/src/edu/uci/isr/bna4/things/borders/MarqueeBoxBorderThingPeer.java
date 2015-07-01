package edu.uci.isr.bna4.things.borders;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;

public class MarqueeBoxBorderThingPeer
	extends AbstractThingPeer{

	protected MarqueeBoxBorderThing t;

	public MarqueeBoxBorderThingPeer(IThing t){
		super(t);
		this.t = (MarqueeBoxBorderThing)t;
	}

	@Override
	public void draw(IBNAView view, final GC g){
		final Rectangle lr = BNAUtils.worldToLocal(view.getCoordinateMapper(), BNAUtils.normalizeRectangle(t.getBoundingBox()));
		if(g.getClipping().intersects(lr)){
			final int offset = t.getRotatingOffset();

			BNAUtils.drawMarquee(g, offset, true, new Runnable(){

				public void run(){
					g.drawRectangle(lr.x, lr.y, lr.width, lr.height);
				};
			});
		}
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		return false;
	}
}
