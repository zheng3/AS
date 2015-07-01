package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;

public interface IHasText extends IThing{

	public static final String TEXT_PROPERTY_NAME = "text";
	
	public String getText();
}
