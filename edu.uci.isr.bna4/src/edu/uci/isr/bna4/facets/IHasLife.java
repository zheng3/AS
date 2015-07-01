package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;

public interface IHasLife extends IThing{
	public static final String LIFE_PROPERTY_NAME = "life";
	
	public int getLife();
}
