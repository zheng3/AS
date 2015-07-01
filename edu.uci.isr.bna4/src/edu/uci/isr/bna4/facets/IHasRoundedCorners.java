package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;

public interface IHasRoundedCorners extends IThing{
	public static final String CORNER_WIDTH_PROPERTY_NAME = "cornerWidth";
	public static final String CORNER_HEIGHT_PROPERTY_NAME = "cornerHeight";
	
	public int getCornerWidth();
	public int getCornerHeight();
}
