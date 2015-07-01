package edu.uci.isr.bna4.things.glass;

import edu.uci.isr.bna4.facets.IHasEndpoints;
import edu.uci.isr.bna4.facets.IHasMutableStickyEndpoints;
import edu.uci.isr.bna4.logics.coordinating.MaintainStickyPointLogic;
import edu.uci.isr.bna4.logics.coordinating.MaintainStickyPointLogic.StickyMode;

@Deprecated
public class StickySplineGlassThing
	extends SplineGlassThing
	implements IHasMutableStickyEndpoints{

	@Deprecated
	public StickySplineGlassThing(){
		this(null);
	}

	@Deprecated
	public StickySplineGlassThing(String id){
		super(id);
	}

	@Deprecated
	final public String getEndpoint1StuckToThingID(){
		return MaintainStickyPointLogic.getStuckToThingId(IHasEndpoints.ENDPOINT_1_PROPERTY_NAME, this);
	}

	@Deprecated
	public void setEndpoint1StuckToThingID(String endpoint1StuckToThingID){
		MaintainStickyPointLogic.stickPoint(endpoint1StuckToThingID, IHasEndpoints.ENDPOINT_1_PROPERTY_NAME, StickyMode.EDGE, this);
	}

	@Deprecated
	public void clearEndpoint1StuckToThingID(){
		MaintainStickyPointLogic.unstickPoint(IHasEndpoints.ENDPOINT_1_PROPERTY_NAME, this);
	}

	@Deprecated
	public String getEndpoint2StuckToThingID(){
		return MaintainStickyPointLogic.getStuckToThingId(IHasEndpoints.ENDPOINT_2_PROPERTY_NAME, this);
	}

	@Deprecated
	public void setEndpoint2StuckToThingID(String endpoint2StuckToThingID){
		MaintainStickyPointLogic.stickPoint(endpoint2StuckToThingID, IHasEndpoints.ENDPOINT_2_PROPERTY_NAME, StickyMode.EDGE, this);
	}

	@Deprecated
	public void clearEndpoint2StuckToThingID(){
		MaintainStickyPointLogic.unstickPoint(IHasEndpoints.ENDPOINT_2_PROPERTY_NAME, this);
	}
}
