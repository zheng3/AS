package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;
import edu.uci.isr.widgets.swt.constants.Flow;

public interface IHasFlow extends IThing{
	public static final String FLOW_PROPERTY_NAME = "flow";
	
	public Flow getFlow();
}
