package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;

public interface IHasRotatingOffset extends IThing{
	public static final String ROTATING_OFFSET_PROPERTY_NAME = "rotatingOffset";
	
	public int getRotatingOffset();
}
