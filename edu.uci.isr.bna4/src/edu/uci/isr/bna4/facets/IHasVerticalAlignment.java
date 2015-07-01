package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;
import edu.uci.isr.widgets.swt.constants.VerticalAlignment;

public interface IHasVerticalAlignment extends IThing{
	public static final String VERTICAL_ALIGNMENT_PROPERTY_NAME = "verticalAlignment";
	
	public VerticalAlignment getVerticalAlignment();
}
