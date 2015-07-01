package edu.uci.isr.bna4.things.glass;

import java.awt.Polygon;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.ICoordinateMapper;
import edu.uci.isr.bna4.IThing;

public class PolygonGlassThingPeer
	extends AbstractThingPeer{

	protected PolygonGlassThing t;

	public PolygonGlassThingPeer(IThing t){
		super(t);
		this.t = (PolygonGlassThing)t;
	}

	@Override
	public void draw(IBNAView view, final GC g){
		if(t.isSelected()){
			ICoordinateMapper cm = view.getCoordinateMapper();
			Rectangle lr = BNAUtils.worldToLocal(cm, BNAUtils.normalizeRectangle(t.getBoundingBox()));
			if(g.getClipping().intersects(lr)){
				int offset = t.getRotatingOffset();
				Point[] lps = BNAUtils.worldToLocal(cm, t.getPoints());
				final int[] coords = new int[lps.length * 2];
				for(int i = 0; i < lps.length; i++){
					Point p = lps[i];
					coords[i * 2 + 0] = p.x;
					coords[i * 2 + 1] = p.y;
				}

				BNAUtils.drawMarquee(g, offset, true, new Runnable(){

					public void run(){
						g.drawPolygon(coords);
					}
				});
			}
		}
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		if(BNAUtils.isWithin(t.getBoundingBox(), worldX, worldY)){
			Point[] ps = t.getPoints();
			int[] xpoints = new int[ps.length];
			int[] ypoints = new int[ps.length];
			for(int i = 0; i < ps.length; i++){
				Point p = ps[i];
				xpoints[i] = p.x;
				ypoints[i] = p.y;
			}

			return new Polygon(xpoints, ypoints, ps.length).contains(worldX, worldY);
		}
		return false;
	}
}
