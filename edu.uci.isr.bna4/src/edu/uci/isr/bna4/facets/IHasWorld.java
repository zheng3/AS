package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IBNAWorld;
import edu.uci.isr.bna4.IThing;

public interface IHasWorld extends IThing{
	public static final String WORLD_PROPERTY_NAME = "$world";
	
	public IBNAWorld getWorld();
}
