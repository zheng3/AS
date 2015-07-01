package edu.uci.isr.bna4.things.borders;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.ICoordinateMapper;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ResourceUtils;

public class EllipseBorderThingPeer
	extends AbstractThingPeer{

	protected EllipseBorderThing t;

	protected Rectangle localBoundingBox = new Rectangle(0, 0, 0, 0);

	public EllipseBorderThingPeer(IThing t){
		super(t);
		if(!(t instanceof EllipseBorderThing)){
			throw new IllegalArgumentException("EllipseBorderThingPeer can only peer for EllipseBorderThing");
		}
		this.t = (EllipseBorderThing)t;
	}

	protected void updateLocalBoundingBox(ICoordinateMapper cm){
		Rectangle worldBoundingBox = t.getBoundingBox();
		worldBoundingBox = BNAUtils.normalizeRectangle(worldBoundingBox);
		localBoundingBox.x = cm.worldXtoLocalX(worldBoundingBox.x);
		localBoundingBox.y = cm.worldYtoLocalY(worldBoundingBox.y);

		int lx2 = cm.worldXtoLocalX(worldBoundingBox.x + worldBoundingBox.width);
		int ly2 = cm.worldYtoLocalY(worldBoundingBox.y + worldBoundingBox.height);

		localBoundingBox.width = lx2 - localBoundingBox.x;
		localBoundingBox.height = ly2 - localBoundingBox.y;
	}

	@Override
	public void draw(IBNAView view, GC g){
		updateLocalBoundingBox(view.getCoordinateMapper());
		if(!g.getClipping().intersects(localBoundingBox)){
			return;
		}

		Color fg = ResourceUtils.getColor(getDisplay(), t.getColor());
		if(fg == null){
			fg = g.getDevice().getSystemColor(SWT.COLOR_BLACK);
		}

		g.setForeground(fg);
		g.setLineStyle(t.getLineStyle());
		int lineWidth = t.getLineWidth();
		g.setLineWidth(lineWidth);
		int count = t.getCount();
		if(count == 1){
			g.drawOval(localBoundingBox.x, localBoundingBox.y, localBoundingBox.width, localBoundingBox.height);
		}
		else{
			for(int i = 0; i < t.getCount(); i++){
				Rectangle r = new Rectangle(localBoundingBox.x, localBoundingBox.y, localBoundingBox.width, localBoundingBox.height);
				if(i > 0){
					r.x += lineWidth * 2 * i;
					r.y += lineWidth * 2 * i;
					r.width -= lineWidth * 4 * i;
					r.height -= lineWidth * 4 * i;
				}
				if(r.width > 0 && r.height > 0){
					g.drawOval(localBoundingBox.x, localBoundingBox.y, localBoundingBox.width, localBoundingBox.height);
				}
			}
		}

		g.setLineStyle(SWT.LINE_SOLID);
		g.setLineWidth(1);
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		return false;
	}
}
