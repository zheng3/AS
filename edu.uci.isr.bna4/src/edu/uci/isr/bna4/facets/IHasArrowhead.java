package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.constants.ArrowheadShape;

public interface IHasArrowhead extends IThing{
	public static final String ARROWHEAD_SHAPE_PROPERTY_NAME = "arrowheadShape";
	public static final String ARROWHEAD_SIZE_PROPERTY_NAME = "arrowheadSize";
	public static final String ARROWHEAD_FILLED_PROPERTY_NAME = "arrowheadFilled";
	
	public ArrowheadShape getArrowheadShape();
	public int getArrowheadSize();
	public boolean isArrowheadFilled();
}
