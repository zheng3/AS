package edu.uci.isr.bna4.things.glass;

import java.awt.geom.RoundRectangle2D;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;

public class BoxGlassThingPeer
	extends AbstractThingPeer{

	protected BoxGlassThing t;

	public BoxGlassThingPeer(IThing t){
		super(t);
		this.t = (BoxGlassThing)t;
	}

	@Override
	public void draw(IBNAView view, final GC g){
		if(t.isSelected()){
			Rectangle r = t.getBoundingBox();
			final Rectangle lr = BNAUtils.worldToLocal(view.getCoordinateMapper(), r);
			if(g.getClipping().intersects(lr)){

				final Point lc = BNAUtils.worldToLocal(view.getCoordinateMapper(), r, t.getCornerWidth(), t.getCornerHeight());
				BNAUtils.drawMarquee(g, t.getRotatingOffset(), true, new Runnable(){

					public void run(){
						if(lc.x > 0 && lc.y > 0){
							g.drawRoundRectangle(lr.x, lr.y, lr.width, lr.height, lc.x, lc.y);
						}
						else{
							g.drawRectangle(lr.x, lr.y, lr.width, lr.height);
						}
					};
				});
			}
		}
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		Rectangle r = t.getBoundingBox();
		if(BNAUtils.isWithin(r, worldX, worldY)){
			int cornerWidth = t.getCornerWidth();
			int cornerHeight = t.getCornerHeight();
			return new RoundRectangle2D.Float(r.x, r.y, r.width, r.height, cornerWidth, cornerHeight).contains(worldX, worldY);
		}
		return false;
	}
}
