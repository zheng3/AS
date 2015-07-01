package edu.uci.isr.bna4.things.glass;

import org.eclipse.swt.graphics.PathData;
import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.AbstractThing;
import edu.uci.isr.bna4.PathDataUtils;
import edu.uci.isr.bna4.facets.IHasMutableAnchorPoint;
import edu.uci.isr.bna4.facets.IHasMutablePathData;
import edu.uci.isr.bna4.facets.IHasMutableRotatingOffset;
import edu.uci.isr.bna4.facets.IHasMutableSelected;
import edu.uci.isr.bna4.facets.IRelativeMovable;

public class PathGlassThing
	extends AbstractThing
	implements IHasMutablePathData, IHasMutableAnchorPoint, IRelativeMovable,
	IHasMutableSelected, IHasMutableRotatingOffset{

	public PathGlassThing(){
		this(null);
	}

	public PathGlassThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setPathData(PathDataUtils.EMPTY_PATH_DATA);
		setAnchorPoint(new Point(0, 0));

		setProperty(ROTATING_OFFSET_PROPERTY_NAME, 0);
		setSelected(false);
	}

	public PathData getPathData(){
		return getProperty(PATH_DATA_PROPERTY_NAME);
	}

	public void setPathData(PathData pathData){
		setProperty(PATH_DATA_PROPERTY_NAME, pathData);
	}

	public Point getAnchorPoint(){
		return getProperty(ANCHOR_POINT_PROPERTY_NAME);
	}

	public void setAnchorPoint(Point newAnchorPoint){
		setProperty(ANCHOR_POINT_PROPERTY_NAME, newAnchorPoint);
	}

	public Point getReferencePoint(){
		return getAnchorPoint();
	}

	public void setReferencePoint(Point p){
		setAnchorPoint(p);
	}

	public void setSelected(boolean selected){
		setProperty(SELECTED_PROPERTY_NAME, selected);
	}

	public boolean isSelected(){
		return getProperty(SELECTED_PROPERTY_NAME);
	}

	public int getRotatingOffset(){
		return getProperty(ROTATING_OFFSET_PROPERTY_NAME);
	}

	public void incrementRotatingOffset(){
		setProperty(ROTATING_OFFSET_PROPERTY_NAME, (getRotatingOffset() + 1) % 6);
	}
}
