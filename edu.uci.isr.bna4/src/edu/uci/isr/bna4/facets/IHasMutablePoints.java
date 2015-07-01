package edu.uci.isr.bna4.facets;

import org.eclipse.swt.graphics.Point;

public interface IHasMutablePoints
	extends IHasPoints{

	public void setPoints(Point[] points);
}
