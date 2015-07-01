package edu.uci.isr.bna4.logics.coordinating;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.assemblies.AssemblyUtils;
import edu.uci.isr.bna4.assemblies.IAssembly;
import edu.uci.isr.bna4.facets.IHasAnchorPoint;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutableOrientation;
import edu.uci.isr.bna4.facets.IHasOrientation;
import edu.uci.isr.bna4.logics.tracking.ReferenceTrackingLogic;
import edu.uci.isr.widgets.swt.constants.Orientation;

public class MaintainAnchoredAssemblyOrientationLogic
	extends
	AbstractMaintainThingsWithReferenceNameLogic<IHasBoundingBox, IHasAnchorPoint>{

	private static Orientation getOnEdgeOrientation(int x1, int x2, int y1, int y2, int x, int y){
		if(y == y1){
			return getOnLineSegmentOrientation(x1, x, x2, Orientation.NORTHWEST, Orientation.NORTH, Orientation.NORTHEAST);
		}
		else if(y == y2){
			return getOnLineSegmentOrientation(x1, x, x2, Orientation.SOUTHWEST, Orientation.SOUTH, Orientation.SOUTHEAST);
		}
		else if(x == x1){
			return getOnLineSegmentOrientation(y1, y, y2, Orientation.NORTHWEST, Orientation.WEST, Orientation.SOUTHWEST);
		}
		else if(x == x2){
			return getOnLineSegmentOrientation(y1, y, y2, Orientation.NORTHEAST, Orientation.EAST, Orientation.SOUTHEAST);
		}
		return Orientation.NONE;
	}

	private static Orientation getOnLineSegmentOrientation(int p1, int p, int p2, Orientation o1, Orientation o, Orientation o2){
		if(p == p1){
			return o1;
		}
		if(p == p2){
			return o2;
		}
		if(p >= p1 && p <= p2){
			return o;
		}
		return Orientation.NONE;
	}

	public MaintainAnchoredAssemblyOrientationLogic(ReferenceTrackingLogic rtl){
		super(IHasBoundingBox.class, new String[]{IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME}, IHasAnchorPoint.class, new String[]{IHasOrientation.ORIENTATION_PROPERTY_NAME, IHasAnchorPoint.ANCHOR_POINT_PROPERTY_NAME}, rtl, MaintainStickyPointLogic.getReferenceName(IHasAnchorPoint.ANCHOR_POINT_PROPERTY_NAME));
	}

	@Override
	protected void maintain(IBNAModel sourceModel, IHasBoundingBox sourceThing, IHasAnchorPoint targetThing, ThingEvent thingEvent){
		Rectangle r = sourceThing.getBoundingBox();
		Point p = targetThing.getAnchorPoint();
		if(r != null && p != null){
			Orientation orientation = getOnEdgeOrientation(r.x, r.x + r.width, r.y, r.y + r.height, p.x, p.y);
			IAssembly assembly = AssemblyUtils.getAssemblyWithPart(targetThing);
			if(assembly != null){
				for(IThing partThing: assembly.getParts()){
					if(partThing instanceof IHasMutableOrientation){
						((IHasMutableOrientation)partThing).setOrientation(orientation);
					}
				}
			}
		}
	}
}
