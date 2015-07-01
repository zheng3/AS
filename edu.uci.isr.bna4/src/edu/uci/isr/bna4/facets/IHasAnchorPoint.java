package edu.uci.isr.bna4.facets;

import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.IThing;

public interface IHasAnchorPoint extends IThing{
	public static final String ANCHOR_POINT_PROPERTY_NAME = "anchorPoint";
	
	public Point getAnchorPoint();
}
