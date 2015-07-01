package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;

public interface IHasStandardCursor extends IHasCursor, IThing{
	public static final String STANDARD_CURSOR_PROPERTY_NAME = "standardCursor";
	
	public int getStandardCursor();
}
