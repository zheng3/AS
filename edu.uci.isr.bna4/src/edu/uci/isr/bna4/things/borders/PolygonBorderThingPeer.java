package edu.uci.isr.bna4.things.borders;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.ICoordinateMapper;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ResourceUtils;

public class PolygonBorderThingPeer
	extends AbstractThingPeer{

	protected PolygonBorderThing t;

	public PolygonBorderThingPeer(IThing t){
		super(t);
		this.t = (PolygonBorderThing)t;
	}

	@Override
	public void draw(IBNAView view, GC g){
		ICoordinateMapper cm = view.getCoordinateMapper();
		Rectangle localBoundingBox = t.getBoundingBox();
		BNAUtils.convertWorldToLocal(cm, localBoundingBox);
		if(!g.getClipping().intersects(localBoundingBox)){
			return;
		}

		Point[] points = t.getPoints();
		int[] localXYArray = new int[2 * points.length];
		for(int i = 0; i < points.length; i++){
			localXYArray[2 * i] = points[i].x;
			localXYArray[2 * i + 1] = points[i].y;
		}
		BNAUtils.convertWorldToLocal(cm, localXYArray);

		Color fg = ResourceUtils.getColor(getDisplay(), t.getColor());
		if(fg == null){
			fg = g.getDevice().getSystemColor(SWT.COLOR_GRAY);
		}

		g.setForeground(fg);
		g.drawPolyline(localXYArray);

		g.setLineStyle(SWT.LINE_SOLID);
		g.setLineWidth(1);
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		// TODO Auto-generated method stub
		return false;
	}
}
