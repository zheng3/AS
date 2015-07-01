package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;

public interface IHasLineWidth extends IThing{

	public static final String LINE_WIDTH_PROPERTY_NAME = "lineWidth";
	
	public int getLineWidth();
}
