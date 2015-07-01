package edu.uci.isr.bna4.facets;

@Deprecated
public interface IHasMutableStickyEndpoints
	extends IHasStickyEndpoints{

	@Deprecated
	public void setEndpoint1StuckToThingID(String endpoint1StuckToThingID);

	@Deprecated
	public void setEndpoint2StuckToThingID(String endpoint2StuckToThingID);
}
