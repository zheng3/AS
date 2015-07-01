package edu.uci.isr.bna4.things.essence;

import edu.uci.isr.bna4.facets.IHasMutableRoundedCorners;

public abstract class RoundedRectangleEssenceThing
	extends RectangleEssenceThing
	implements IHasMutableRoundedCorners{

	public RoundedRectangleEssenceThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setCornerHeight(0);
		setCornerWidth(0);
	}

	public int getCornerHeight(){
		return getProperty(CORNER_HEIGHT_PROPERTY_NAME);
	}

	public void setCornerHeight(int cornerHeight){
		setProperty(CORNER_HEIGHT_PROPERTY_NAME, cornerHeight);
	}

	public int getCornerWidth(){
		return getProperty(CORNER_WIDTH_PROPERTY_NAME);
	}

	public void setCornerWidth(int cornerWidth){
		setProperty(CORNER_WIDTH_PROPERTY_NAME, cornerWidth);
	}
}
