package edu.uci.isr.bna4.facets;

import org.eclipse.swt.widgets.Layout;

import edu.uci.isr.bna4.IThing;

public interface IHasSWTLayout extends IThing {
	public static final String SWT_LAYOUT_PROPERTY_NAME = "swtLayout";

	public Layout getSWTLayout();

	public void setSWTLayout(Layout layout);
}
