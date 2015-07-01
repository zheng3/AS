package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;
import edu.uci.isr.widgets.swt.constants.HorizontalAlignment;

public interface IHasHorizontalAlignment extends IThing{
	public static final String HORIZONTAL_ALIGNMENT_PROPERTY_NAME = "horizontalAlignment";
	
	public HorizontalAlignment getHorizontalAlignment();
}
