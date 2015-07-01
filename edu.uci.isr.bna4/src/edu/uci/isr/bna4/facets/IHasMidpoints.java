package edu.uci.isr.bna4.facets;

import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.IThing;

public interface IHasMidpoints extends IThing{
	public static final String MIDPOINTS_PROPERTY_NAME = "midpoints";
	
	public Point[] getMidpoints();
}
