package edu.uci.isr.bna4.facets;

import org.eclipse.swt.graphics.Point;

public interface IHasMutableAnchorPoint
	extends IHasAnchorPoint{

	public void setAnchorPoint(Point newAnchorPoint);
}
