package edu.uci.isr.bna4.facets;

import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.IThing;

public interface IHasEndpoints extends IThing{

	public static final String ENDPOINT_1_PROPERTY_NAME = "endpoint1";
	public static final String ENDPOINT_2_PROPERTY_NAME = "endpoint2";
	
	public Point getEndpoint1();
	public Point getEndpoint2();
}
