package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;
import edu.uci.isr.widgets.swt.constants.Orientation;

public interface IHasOrientation extends IThing{
	public static final String ORIENTATION_PROPERTY_NAME = "orientation";
	
	public Orientation getOrientation();
}
