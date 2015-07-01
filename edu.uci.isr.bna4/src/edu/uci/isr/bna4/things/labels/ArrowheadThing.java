package edu.uci.isr.bna4.things.labels;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;

import edu.uci.isr.bna4.constants.ArrowheadShape;
import edu.uci.isr.bna4.facets.IHasMutableArrowhead;
import edu.uci.isr.bna4.facets.IHasMutableColor;
import edu.uci.isr.bna4.facets.IHasMutableSecondaryAnchorPoint;
import edu.uci.isr.bna4.facets.IHasMutableSecondaryColor;
import edu.uci.isr.bna4.things.essence.AnchorPointEssenceThing;

public class ArrowheadThing
	extends AnchorPointEssenceThing
	implements IHasMutableColor, IHasMutableSecondaryColor,
	IHasMutableArrowhead, IHasMutableSecondaryAnchorPoint{

	public ArrowheadThing(){
		this(null);
	}

	public ArrowheadThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setArrowheadShape(ArrowheadShape.NONE);
		setArrowheadSize(5);
		setArrowheadFilled(false);
		setColor(new RGB(0, 0, 0));
		setSecondaryColor(new RGB(0, 0, 0));
		setSecondaryAnchorPoint(new Point(0, 0));
	}

	public int getArrowheadSize(){
		return getProperty(ARROWHEAD_SIZE_PROPERTY_NAME);
	}

	public void setArrowheadSize(int arrowheadSize){
		setProperty(ARROWHEAD_SIZE_PROPERTY_NAME, arrowheadSize);
	}

	public void setColor(RGB c){
		setProperty(COLOR_PROPERTY_NAME, c);
	}

	public RGB getColor(){
		return getProperty(COLOR_PROPERTY_NAME);
	}

	public void setSecondaryColor(RGB c){
		setProperty(SECONDARY_COLOR_PROPERTY_NAME, c);
	}

	public RGB getSecondaryColor(){
		return getProperty(SECONDARY_COLOR_PROPERTY_NAME);
	}

	public ArrowheadShape getArrowheadShape(){
		return getProperty(ARROWHEAD_SHAPE_PROPERTY_NAME);
	}

	public void setArrowheadShape(ArrowheadShape arrowheadShape){
		setProperty(ARROWHEAD_SHAPE_PROPERTY_NAME, arrowheadShape);
	}

	public boolean isArrowheadFilled(){
		return getProperty(ARROWHEAD_FILLED_PROPERTY_NAME);
	}

	public void setArrowheadFilled(boolean arrowheadFilled){
		setProperty(ARROWHEAD_FILLED_PROPERTY_NAME, arrowheadFilled);
	}

	public Point getSecondaryAnchorPoint(){
		return getProperty(SECONDARY_ANCHOR_POINT_PROPERTY_NAME);
	}

	public void setSecondaryAnchorPoint(Point secondaryAnchorPoint){
		setProperty(SECONDARY_ANCHOR_POINT_PROPERTY_NAME, secondaryAnchorPoint);
	}
}
