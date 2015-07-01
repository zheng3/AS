package edu.uci.isr.bna4.logics.coordinating;

import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.facets.IHasAnchorPoint;
import edu.uci.isr.bna4.facets.IHasEndpoints;
import edu.uci.isr.bna4.facets.IHasMidpoints;
import edu.uci.isr.bna4.facets.IHasMutableAnchorPoint;
import edu.uci.isr.bna4.facets.IHasMutableSecondaryAnchorPoint;
import edu.uci.isr.bna4.logics.tracking.ReferenceTrackingLogic;

public class MirrorEndpointLogic
	extends
	AbstractMaintainThingsWithReferenceNameLogic<IHasEndpoints, IHasMutableAnchorPoint>{

	public static final String ENDPOINT_MASTER_THING_ID_PROPERTY_NAME = "&endpointMasterThingID";
	public static final String ENDPOINT_NUMBER_PROPERTY_NAME = "endpointNumber";

	public static final void mirrorEndpoint(IHasEndpoints sourceThing, int endpointNumber, IHasMutableAnchorPoint... targetThings){
		for(IThing targetThing: targetThings){
			targetThing.setProperty(ENDPOINT_NUMBER_PROPERTY_NAME, endpointNumber);
			targetThing.setProperty(ENDPOINT_MASTER_THING_ID_PROPERTY_NAME, sourceThing.getID());
		}
	}

	public MirrorEndpointLogic(ReferenceTrackingLogic rtl){
		super(IHasEndpoints.class, new String[]{IHasEndpoints.ENDPOINT_1_PROPERTY_NAME, IHasEndpoints.ENDPOINT_2_PROPERTY_NAME, IHasMidpoints.MIDPOINTS_PROPERTY_NAME}, IHasMutableAnchorPoint.class, new String[]{IHasAnchorPoint.ANCHOR_POINT_PROPERTY_NAME, ENDPOINT_NUMBER_PROPERTY_NAME}, rtl, ENDPOINT_MASTER_THING_ID_PROPERTY_NAME);
	}

	@Override
	protected void maintain(IBNAModel sourceModel, IHasEndpoints sourceThing, IHasMutableAnchorPoint targetThing, ThingEvent thingEvent){
		Integer endpointNumber = targetThing.getProperty(ENDPOINT_NUMBER_PROPERTY_NAME);
		if(endpointNumber != null){
			Point p1 = null;
			Point p2 = null;
			if(endpointNumber == 1){
				p1 = sourceThing.getEndpoint1();
				if(sourceThing instanceof IHasMidpoints){
					Point[] midpoints = ((IHasMidpoints)sourceThing).getMidpoints();
					if(midpoints != null && midpoints.length > 0){
						p2 = midpoints[0];
					}
				}
				if(p2 == null){
					p2 = sourceThing.getEndpoint2();
				}
			}
			else if(endpointNumber == 2){
				p1 = sourceThing.getEndpoint2();
				if(sourceThing instanceof IHasMidpoints){
					Point[] midpoints = ((IHasMidpoints)sourceThing).getMidpoints();
					if(midpoints != null && midpoints.length > 0){
						p2 = midpoints[midpoints.length - 1];
					}
				}
				if(p2 == null){
					p2 = sourceThing.getEndpoint1();
				}
			}
			targetThing.setAnchorPoint(p1);
			if(targetThing instanceof IHasMutableSecondaryAnchorPoint){
				((IHasMutableSecondaryAnchorPoint)targetThing).setSecondaryAnchorPoint(p2);
			}
		}
	}
}
