package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;

public interface IHasAngle extends IThing{
	public static final String ANGLE_PROPERTY_NAME = "angle";
	
	public int getAngle();
}
