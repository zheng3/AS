package edu.uci.isr.bna4.facets;

import org.eclipse.swt.graphics.PathData;

public interface IHasMutablePathData
	extends IHasPathData{

	public void setPathData(PathData pathData);

}