package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;

public interface IHasRadius extends IThing{
	public static final String RADIUS_PROPERTY_NAME = "radius";
	
	public int getRadius();
}
