package edu.uci.isr.bna4.facets;

import org.eclipse.swt.graphics.PathData;

import edu.uci.isr.bna4.IThing;

public interface IHasPathData extends IThing {

	public static final String PATH_DATA_PROPERTY_NAME = "pathData";

	public abstract PathData getPathData();

}