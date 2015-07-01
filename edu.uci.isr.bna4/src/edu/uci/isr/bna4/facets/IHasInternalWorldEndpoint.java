package edu.uci.isr.bna4.facets;

import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.IThing;

public interface IHasInternalWorldEndpoint
	extends IThing{

	public static final String INTERNAL_ENDPOINT_WORLD_THING_ID_PROPERTY_NAME = "&internalEndpointWorldThingID";
	public static final String INTERNAL_ENDPOINT_PROPERTY_NAME = "internalEndpoint";

	public String getInternalEndpointWorldThingID();

	public Point getInternalEndpoint();
}
