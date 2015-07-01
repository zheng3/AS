package edu.uci.isr.bna4.things.glass;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;

public class EllipseGlassThingPeer
	extends AbstractThingPeer{

	protected EllipseGlassThing t;

	public EllipseGlassThingPeer(IThing t){
		super(t);
		this.t = (EllipseGlassThing)t;
	}

	@Override
	public void draw(IBNAView view, final GC g){
		if(t.isSelected()){
			final Rectangle lr = BNAUtils.worldToLocal(view.getCoordinateMapper(), BNAUtils.normalizeRectangle(t.getBoundingBox()));
			if(g.getClipping().intersects(lr)){
				int offset = t.getRotatingOffset();
				BNAUtils.drawMarquee(g, offset, false, new Runnable(){

					public void run(){
						g.drawOval(lr.x, lr.y, lr.width, lr.height);
					}
				});
			}
		}
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		Rectangle wr = BNAUtils.normalizeRectangle(t.getBoundingBox());
		return new Ellipse2D.Float(wr.x, wr.y, wr.width, wr.height).contains(new Point2D.Float(worldX, worldY));
	}
}
