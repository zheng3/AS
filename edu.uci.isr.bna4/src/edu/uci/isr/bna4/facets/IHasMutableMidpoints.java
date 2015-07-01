package edu.uci.isr.bna4.facets;

import org.eclipse.swt.graphics.Point;

public interface IHasMutableMidpoints
	extends IHasMidpoints{

	public static final String USER_MAY_ADD_MIDPOINTS = "userMayAddMidpoints";
	public static final String USER_MAY_REMOVE_MIDPOINTS = "userMayRemoveMidpoints";
	public static final String USER_MAY_MOVE_MIDPOINTS = "userMayMoveMidpoints";

	public void setMidpoints(Point[] midpoints);
}
