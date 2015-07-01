package edu.uci.isr.bna4.facets;

import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.IThing;

public interface IRelativeMovable
	extends IThing{

	public static final String USER_MAY_MOVE = "userMayMove";

	public Point getReferencePoint();

	public void setReferencePoint(Point p);
}
