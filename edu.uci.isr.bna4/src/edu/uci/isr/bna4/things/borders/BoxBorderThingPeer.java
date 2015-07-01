package edu.uci.isr.bna4.things.borders;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ResourceUtils;

public class BoxBorderThingPeer
	extends AbstractThingPeer{

	protected BoxBorderThing t;

	public BoxBorderThingPeer(IThing t){
		super(t);
		this.t = (BoxBorderThing)t;
	}

	@Override
	public void draw(IBNAView view, GC g){
		Rectangle r = t.getBoundingBox();
		Rectangle lr = BNAUtils.worldToLocal(view.getCoordinateMapper(), r);
		if(g.getClipping().intersects(lr)){

			g.setForeground(ResourceUtils.getColor(getDisplay(), t.getColor()));
			g.setLineStyle(t.getLineStyle());
			int lineWidth = t.getLineWidth();
			g.setLineWidth(lineWidth);
			Point lc = BNAUtils.worldToLocal(view.getCoordinateMapper(), r, t.getCornerWidth(), t.getCornerHeight());

			int count = t.getCount();
			while(count-- > 0 && lr.width >= 0 && lr.height >= 0){
				if(lc.x > 0 && lc.y > 0){
					g.drawRoundRectangle(lr.x, lr.y, lr.width, lr.height, lc.x, lc.y);
				}
				else{
					g.drawRectangle(lr.x, lr.y, lr.width, lr.height);
				}
				if(count > 0){
					lr.x += lineWidth * 2;
					lr.y += lineWidth * 2;
					lr.width -= lineWidth * 4;
					lr.height -= lineWidth * 4;
					lc.x -= lineWidth * 4;
					lc.y -= lineWidth * 4;
				}
			}

			g.setLineStyle(SWT.LINE_SOLID);
			g.setLineWidth(1);
		}
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		return false;
	}
}
