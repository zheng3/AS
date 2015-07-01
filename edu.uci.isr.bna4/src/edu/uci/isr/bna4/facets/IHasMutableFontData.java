package edu.uci.isr.bna4.facets;

import edu.uci.isr.widgets.swt.constants.FontStyle;

public interface IHasMutableFontData
	extends IHasFontData{

	public void setFontName(String fontName);

	public void setFontSize(int fontSize);

	public void setFontStyle(FontStyle fontStyle);
}
