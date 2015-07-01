package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;

public interface IHasEditing extends IThing{
	public static final String EDITING_PROPERTY_NAME = "editing";
	
	public boolean isEditing();
}
