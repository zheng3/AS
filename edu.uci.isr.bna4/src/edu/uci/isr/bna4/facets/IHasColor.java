package edu.uci.isr.bna4.facets;

import org.eclipse.swt.graphics.RGB;

import edu.uci.isr.bna4.IThing;

public interface IHasColor extends IThing{
	public static final String COLOR_PROPERTY_NAME = "color";
	
	public RGB getColor();
}
