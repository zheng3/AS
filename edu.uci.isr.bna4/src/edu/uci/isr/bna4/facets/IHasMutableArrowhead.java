package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.constants.ArrowheadShape;

public interface IHasMutableArrowhead
	extends IHasArrowhead{

	public void setArrowheadShape(ArrowheadShape arrowheadShape);

	public void setArrowheadSize(int arrowheadSize);

	public void setArrowheadFilled(boolean arrowheadFilled);
}
