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
import edu.uci.isr.bna4.IThingPeer;
import edu.uci.isr.bna4.facets.IHasBoundingBox;

public class MappingGlassThingPeer
	extends AbstractThingPeer{

	protected MappingGlassThing t;

	public MappingGlassThingPeer(IThing t){
		super(t);
		this.t = (MappingGlassThing)t;
	}

	@Override
	public void draw(IBNAView view, GC g){
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		Point ap = t.getAnchorPoint();

		// check that the point is within the vicinity of the anchor point
		// and world thing bounding box
		IThing worldThing = view.getWorld().getBNAModel().getThing(t.getInternalEndpointWorldThingID());
		if(worldThing instanceof IHasBoundingBox){
			Rectangle r = ((IHasBoundingBox)worldThing).getBoundingBox();
			BNAUtils.expandRectangle(r, ap);
			if(!r.contains(worldX, worldY)){
				return false;
			}
		}

		IBNAView iView = BNAUtils.getInternalView(view, worldThing);
		if(iView != null){
			ICoordinateMapper cm = view.getCoordinateMapper();
			ICoordinateMapper iCM = iView.getCoordinateMapper();

			// check that the point is not within the internally referenced thing
			IThing mappedThing = iView.getWorld().getBNAModel().getThing(t.getInternalEndpointStuckToThingID());
			if(mappedThing != null){
				IThingPeer mappedThingPeer = iView.getPeer(mappedThing);
				if(mappedThingPeer != null){
					int iWorldX = BNAUtils.round(iCM.localXtoWorldX(cm.worldXtoLocalX((float)worldX)));
					int iWorldY = BNAUtils.round(iCM.localYtoWorldY(cm.worldYtoLocalY((float)worldY)));
					if(mappedThingPeer.isInThing(iView, iWorldX, iWorldY)){
						return false;
					}
				}
			}

			// check that the point is on the line connecting the anchor point
			// and the internal point
			int localX = cm.worldXtoLocalX(worldX);
			int localY = cm.worldYtoLocalY(worldY);
			Point lp1 = BNAUtils.worldToLocal(cm, t.getAnchorPoint());
			Point lp2 = BNAUtils.worldToLocal(iCM, t.getInternalEndpoint());
			if(Math.abs(BNAUtils.round(Line2D.ptSegDist(lp1.x, lp1.y, lp2.x, lp2.y, localX, localY))) <= 5){
				return true;
			}
		}

		return false;
	}
}
