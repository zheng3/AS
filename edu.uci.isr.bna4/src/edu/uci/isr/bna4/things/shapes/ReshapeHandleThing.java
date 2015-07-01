package edu.uci.isr.bna4.things.shapes;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;

import edu.uci.isr.bna4.AbstractThing;
import edu.uci.isr.bna4.facets.IHasMutableAnchorPoint;
import edu.uci.isr.bna4.facets.IHasMutableColor;
import edu.uci.isr.bna4.facets.IHasMutableOrientation;
import edu.uci.isr.widgets.swt.constants.Orientation;

public class ReshapeHandleThing
	extends AbstractThing
	implements IHasMutableColor, IHasMutableAnchorPoint, IHasMutableOrientation{

	protected static final String HANDLE_BOUNDING_BOX_PROPERTY_NAME = "#boundingBox";

	public ReshapeHandleThing(){
		this(null);
	}

	public ReshapeHandleThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setAnchorPoint(new Point(0, 0));
		setOrientation(Orientation.NONE);
	}

	public void moveRelative(int dx, int dy){
		Point p = getAnchorPoint();
		p.x += dx;
		p.y += dy;
		setAnchorPoint(p);
	}

	public void setColor(RGB c){
		setProperty(COLOR_PROPERTY_NAME, c);
	}

	public RGB getColor(){
		return getProperty(COLOR_PROPERTY_NAME);
	}

	public Point getAnchorPoint(){
		Point p = getProperty(ANCHOR_POINT_PROPERTY_NAME);
		return new Point(p.x, p.y);
	}

	public void setAnchorPoint(Point p){
		setProperty(ANCHOR_POINT_PROPERTY_NAME, p);
	}

	public Orientation getOrientation(){
		return getProperty(ORIENTATION_PROPERTY_NAME);
	}

	public void setOrientation(Orientation orientation){
		setProperty(ORIENTATION_PROPERTY_NAME, orientation);
	}

}
