package edu.uci.isr.bna4.facets;

import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.IThing;

public interface IHasBoundingBox
	extends IThing{

	public static final String BOUNDING_BOX_PROPERTY_NAME = "boundingBox";

	//Get bounding box in world coordinates
	public Rectangle getBoundingBox();
}
