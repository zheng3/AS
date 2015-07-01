package edu.uci.isr.bna4.facets;

import org.eclipse.swt.graphics.RGB;

public interface IHasMutableColor
	extends IHasColor{

	public static final String USER_MAY_EDIT_COLOR = "userMayEditColor";

	public void setColor(RGB c);
}
