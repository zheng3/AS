package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;

public interface IHasSelected extends IThing{
	public static final String SELECTED_PROPERTY_NAME = "selected";
	
	public boolean isSelected();
}
