package edu.uci.isr.bna4.facets;

import org.eclipse.swt.SWT;

import edu.uci.isr.bna4.IThing;

public interface IHasLineStyle extends IThing{

	public static final String LINE_STYLE_PROPERTY_NAME = "lineStyle";
	
	public static final int LINE_STYLE_SOLID = SWT.LINE_SOLID;
	public static final int LINE_STYLE_DASH = SWT.LINE_DASH;
	public static final int LINE_STYLE_DOT = SWT.LINE_DOT;
	public static final int LINE_STYLE_DASHDOT = SWT.LINE_DASHDOT;
	public static final int LINE_STYLE_DASHDOTDOT = SWT.LINE_DASHDOTDOT;

	public int getLineStyle();
}
