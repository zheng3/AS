package edu.uci.isr.bna4.facets;

import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.IThing;

public interface IHasIndicatorPoint extends IThing{
	public static final String INDICATOR_POINT_PROPERTY_NAME = "indicatorPoint";
	
	public Point getIndicatorPoint();
}
