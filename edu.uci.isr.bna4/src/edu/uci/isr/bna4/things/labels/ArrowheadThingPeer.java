package edu.uci.isr.bna4.things.labels;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.ArrowheadUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.ICoordinateMapper;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ResourceUtils;
import edu.uci.isr.bna4.constants.ArrowheadShape;

public class ArrowheadThingPeer
	extends AbstractThingPeer{

	protected ArrowheadThing t;

	public ArrowheadThingPeer(IThing t){
		super(t);
		this.t = (ArrowheadThing)t;
	}

	@Override
	public void draw(IBNAView view, GC g){
		ICoordinateMapper cm = view.getCoordinateMapper();

		Point wp1 = t.getAnchorPoint();
		int lx = cm.worldXtoLocalX(wp1.x);
		int ly = cm.worldYtoLocalY(wp1.y);

		Point wp2 = t.getSecondaryAnchorPoint();
		int lx2 = cm.worldXtoLocalX(wp2.x);
		int ly2 = cm.worldYtoLocalY(wp2.y);

		if(!(g.getClipping().contains(lx, ly) || g.getClipping().contains(lx2, ly2))){
			return;
		}

		ArrowheadShape arrowheadShape = t.getArrowheadShape();
		if(arrowheadShape == null || arrowheadShape == ArrowheadShape.NONE){
			return;
		}

		int arrowheadSize = t.getArrowheadSize();
		boolean arrowheadFilled = t.isArrowheadFilled();

		Color fg = ResourceUtils.getColor(getDisplay(), t.getColor());
		if(fg == null){
			fg = g.getDevice().getSystemColor(SWT.COLOR_BLACK);
		}

		Color bg = ResourceUtils.getColor(getDisplay(), t.getSecondaryColor());
		if(bg == null){
			bg = g.getDevice().getSystemColor(SWT.COLOR_BLACK);
		}

		if(arrowheadShape == ArrowheadShape.WEDGE){
			int[] points = ArrowheadUtils.calculateTriangularArrowhead(wp2.x, wp2.y, wp1.x, wp1.y, arrowheadSize);
			if(points == null){
				return;
			}
			for(int i = 0; i < points.length; i += 2){
				points[i] = cm.worldXtoLocalX(points[i]);
				points[i + 1] = cm.worldYtoLocalY(points[i + 1]);
			}
			if(arrowheadFilled){
				g.setBackground(bg);
				g.fillPolygon(points);
			}
			g.setForeground(fg);
			g.drawPolyline(points);
		}
		else if(arrowheadShape == ArrowheadShape.TRIANGLE){
			int[] points = ArrowheadUtils.calculateTriangularArrowhead(wp2.x, wp2.y, wp1.x, wp1.y, arrowheadSize);
			if(points == null){
				return;
			}
			for(int i = 0; i < points.length; i += 2){
				points[i] = cm.worldXtoLocalX(points[i]);
				points[i + 1] = cm.worldYtoLocalY(points[i + 1]);
			}
			if(arrowheadFilled){
				g.setBackground(bg);
				g.fillPolygon(points);
			}
			g.setForeground(fg);
			g.drawPolygon(points);
		}
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		return false;
	}

}
