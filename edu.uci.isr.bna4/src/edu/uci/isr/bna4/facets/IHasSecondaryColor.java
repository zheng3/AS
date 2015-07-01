package edu.uci.isr.bna4.facets;

import org.eclipse.swt.graphics.RGB;

import edu.uci.isr.bna4.IThing;

public interface IHasSecondaryColor extends IThing{
	public static final String SECONDARY_COLOR_PROPERTY_NAME = "secondaryColor";

	public RGB getSecondaryColor();
}
