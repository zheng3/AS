package edu.uci.isr.bna4.things.shapes;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ResourceUtils;

public class MappingThingPeer
	extends AbstractThingPeer{

	protected MappingThing t;

	public MappingThingPeer(IThing t){
		super(t);
		this.t = (MappingThing)t;
	}

	@Override
	public void draw(IBNAView view, GC g){
		IBNAView iView = BNAUtils.getInternalView(view, t.getInternalEndpointWorldThingID());
		if(iView != null){
			Point lp1 = BNAUtils.worldToLocal(view.getCoordinateMapper(), t.getAnchorPoint());
			Point lp2 = BNAUtils.worldToLocal(iView.getCoordinateMapper(), t.getInternalEndpoint());

			if(g.getClipping().intersects(BNAUtils.normalizeRectangle(new Rectangle(lp1.x, lp1.y, lp2.x - lp1.x, lp2.y - lp1.y)))){
				LineAttributes oldAttributes = g.getLineAttributes();

				g.setForeground(ResourceUtils.getColor(getDisplay(), t.getColor()));
				g.setLineWidth(t.getLineWidth());
				g.setLineStyle(t.getLineStyle());

				g.drawLine(lp1.x, lp1.y, lp2.x, lp2.y);

				g.setLineAttributes(oldAttributes);
			}
		}
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		return false;
	}
}
