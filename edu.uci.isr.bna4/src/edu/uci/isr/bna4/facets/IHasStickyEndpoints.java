package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.logics.coordinating.MaintainStickyPointLogic;

@Deprecated
public interface IHasStickyEndpoints
	extends IHasMutableEndpoints{

	@Deprecated
	public static final Object ENDPOINT_1_STUCK_TO_THING_ID_PROPERTY_NAME = MaintainStickyPointLogic.getReferenceName(ENDPOINT_1_PROPERTY_NAME);

	@Deprecated
	public static final Object ENDPOINT_2_STUCK_TO_THING_ID_PROPERTY_NAME = MaintainStickyPointLogic.getReferenceName(ENDPOINT_2_PROPERTY_NAME);

	@Deprecated
	public String getEndpoint1StuckToThingID();

	@Deprecated
	public String getEndpoint2StuckToThingID();
}
