package edu.uci.isr.bna4.facets;

public interface IHasInternalWorldMapping
	extends IHasMutableInternalWorldEndpoint{

	public static final String INTERNAL_ENDPOINT_STUCK_TO_THING_ID_PROPERTY_NAME = "internalEndpointStuckToThingID";

	public String getInternalEndpointStuckToThingID();
}
