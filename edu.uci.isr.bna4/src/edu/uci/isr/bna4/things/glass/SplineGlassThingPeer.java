package edu.uci.isr.bna4.things.glass;

import java.awt.geom.Line2D;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.ICoordinateMapper;
import edu.uci.isr.bna4.IThing;

public class SplineGlassThingPeer
	extends AbstractThingPeer{

	protected SplineGlassThing t;

	protected static final Point[] EMPTY_POINT_ARRAY = new Point[0];

	protected Rectangle localBoundingBox = new Rectangle(0, 0, 0, 0);
	protected int[] localPoints = new int[4];

	public SplineGlassThingPeer(IThing t){
		super(t);
		this.t = (SplineGlassThing)t;
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
	public void draw(IBNAView view, final GC g){
		if(t.isSelected()){
			updateLocalBoundingBox(view, view.getCoordinateMapper());
			if(g.getClipping().intersects(localBoundingBox)){
				updateLocalPoints(view, view.getCoordinateMapper());
				int offset = t.getRotatingOffset();

				BNAUtils.drawMarquee(g, offset, true, new Runnable(){

					public void run(){
						g.drawPolyline(localPoints);
					};
				});
			}
		}
	}

	/*
	 * public boolean isInThing(IBNAView view, int worldX, int worldY){
	 * ICoordinateMapper cm = view.getCoordinateMapper();
	 * updateLocalBoundingBox(view, cm); int localX = cm.worldXtoLocalX(worldX);
	 * int localY = cm.worldYtoLocalY(worldY); Rectangle
	 * expandedLocalBoundingBox = new Rectangle(localBoundingBox.x - 5,
	 * localBoundingBox.y - 5, localBoundingBox.width + 10,
	 * localBoundingBox.height + 10);
	 * if(!expandedLocalBoundingBox.contains(localX, localY)) return false;
	 * Point[] midpoints = lt.getMidpoints(); if(midpoints == null) midpoints =
	 * EMPTY_POINT_ARRAY; Point endpoint1 = lt.getEndpoint1(); Point endpoint2 =
	 * lt.getEndpoint2(); int lastX = cm.worldXtoLocalX(endpoint1.x); int lastY =
	 * cm.worldYtoLocalY(endpoint1.y); for(int i = 0; i < midpoints.length;
	 * i++){ int x = cm.worldXtoLocalX(midpoints[i].x); int y =
	 * cm.worldYtoLocalY(midpoints[i].y); int dist =
	 * Math.abs(BNAUtils.round(Line2D.ptSegDist(x, y, lastX, lastY, localX,
	 * localY))); if(dist <= 5) return true; lastX = x; lastY = y; } int x =
	 * cm.worldXtoLocalX(endpoint2.x); int y = cm.worldYtoLocalY(endpoint2.y);
	 * int dist = Math.abs(BNAUtils.round(Line2D.ptSegDist(x, y, lastX, lastY,
	 * localX, localY))); if(dist <= 5) return true; return false; }
	 */

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		ICoordinateMapper cm = view.getCoordinateMapper();
		updateLocalBoundingBox(view, cm);
		int localX = cm.worldXtoLocalX(worldX);
		int localY = cm.worldYtoLocalY(worldY);

		Rectangle expandedLocalBoundingBox = new Rectangle(localBoundingBox.x - 5, localBoundingBox.y - 5, localBoundingBox.width + 10, localBoundingBox.height + 10);
		if(!expandedLocalBoundingBox.contains(localX, localY)){
			return false;
		}

		updateLocalPoints(view, view.getCoordinateMapper());
		int[] lps = localPoints;
		for(int i = 2; i < lps.length; i += 2){
			int dist = Math.abs(BNAUtils.round(Line2D.ptSegDist(lps[i - 2], lps[i - 1], lps[i], lps[i + 1], localX, localY)));
			if(dist <= 5){
				return true;
			}
		}
		return false;
	}

}
