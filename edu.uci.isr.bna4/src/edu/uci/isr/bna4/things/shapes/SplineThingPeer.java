package edu.uci.isr.bna4.things.shapes;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.ICoordinateMapper;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ResourceUtils;

public class SplineThingPeer
	extends AbstractThingPeer{

	protected SplineThing t;

	protected Rectangle localBoundingBox = new Rectangle(0, 0, 0, 0);
	protected int[] localPoints = new int[4];

	public SplineThingPeer(IThing t){
		super(t);
		if(!(t instanceof SplineThing)){
			throw new IllegalArgumentException("SplineThingPeer can only peer for SplineThing");
		}
		this.t = (SplineThing)t;
	}

	protected void updateLocalBoundingBox(IBNAView view, ICoordinateMapper cm){
		localBoundingBox = BNAUtils.worldToLocal(cm, BNAUtils.normalizeRectangle(t.getBoundingBox()));
	}

	protected void updateLocalPoints(IBNAView view, ICoordinateMapper cm){
		Point wp1 = t.getEndpoint1();
		if(wp1 == null){
			wp1 = new Point(0, 0);
		}

		Point[] midpoints = t.getMidpoints();
		int numMidpoints = 0;
		if(midpoints != null){
			numMidpoints = midpoints.length;
		}

		Point wp2 = t.getEndpoint2();
		if(wp2 == null){
			wp2 = new Point(0, 0);
		}

		int lpsize = 4 + numMidpoints + numMidpoints;
		if(localPoints.length != lpsize){
			localPoints = new int[lpsize];
		}
		int i = 0;
		localPoints[i++] = cm.worldXtoLocalX(wp1.x);
		localPoints[i++] = cm.worldYtoLocalY(wp1.y);
		for(int p = 0; p < numMidpoints; p++){
			Point mp = midpoints[p];
			localPoints[i++] = cm.worldXtoLocalX(mp.x);
			localPoints[i++] = cm.worldYtoLocalY(mp.y);
		}
		localPoints[i++] = cm.worldXtoLocalX(wp2.x);
		localPoints[i++] = cm.worldYtoLocalY(wp2.y);
	}

	@Override
	public void draw(IBNAView view, GC g){
		updateLocalBoundingBox(view, view.getCoordinateMapper());
		if(!g.getClipping().intersects(localBoundingBox)){
			return;
		}

		updateLocalPoints(view, view.getCoordinateMapper());
		Color fg;
		if(t.featureSelected){
		fg = 	ResourceUtils.getColor(getDisplay(), t.featureColor);
		}else{
		fg	 =  ResourceUtils.getColor(getDisplay(), t.getColor());
		}
		
		
		if(fg == null){
			fg = g.getDevice().getSystemColor(SWT.COLOR_BLACK);
		}
		
		g.setForeground(fg);
		g.setLineWidth(t.getLineWidth());
		g.setLineStyle(t.getLineStyle());

		g.drawPolyline(localPoints);

		g.setLineStyle(SWT.LINE_SOLID);
		g.setLineWidth(1);
	}

	/*
	 * public Rectangle getLocalBoundingBox(IBNAContext context, GC g,
	 * ICoordinateMapper cm){ updateLocalBoundingBox(cm); return
	 * localBoundingBox; }
	 */

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		return false;
	}

}
