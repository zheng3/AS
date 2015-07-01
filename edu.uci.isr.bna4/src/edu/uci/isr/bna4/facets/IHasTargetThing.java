package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;

public interface IHasTargetThing extends IThing{
	public static final String TARGET_THING_ID_PROPERTY_NAME = "&targetThingID";
	
	public String getTargetThingID();
}
