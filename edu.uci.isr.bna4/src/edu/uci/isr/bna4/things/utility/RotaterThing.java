package edu.uci.isr.bna4.things.utility;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutableAngle;
import edu.uci.isr.bna4.things.essence.AnchorPointEssenceThing;

public class RotaterThing
	extends AnchorPointEssenceThing
	implements IHasBoundingBox, IHasMutableAngle{

	public static final String RADIUS_PROPERTY_NAME = "radius";
	public static final String ROTATED_THING_IDS_PROPERTY_NAME = "rotatedThingIDs";
	public static final String ADJUSTMENT_INCREMENT_PROPERTY_NAME = "adjustmentIncrement";

	public RotaterThing(){
		this(null);
	}

	public RotaterThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setAdjustmentIncrement(15);
		setRadius(50);
		setAngle(0);
	}

	public int getRadius(){
		return getProperty(RADIUS_PROPERTY_NAME);
	}

	public void setRadius(int radius){
		setProperty(RADIUS_PROPERTY_NAME, radius);
	}

	public Rectangle getBoundingBox(){
		Point anchorPoint = getAnchorPoint();
		int radius = getRadius();
		return new Rectangle(anchorPoint.x - radius, anchorPoint.y - radius, radius * 2, radius * 2);
	}

	public void setAngle(int degrees){
		setProperty(ANGLE_PROPERTY_NAME, degrees);
	}

	public int getAngle(){
		return getProperty(ANGLE_PROPERTY_NAME);
	}

	public void setAdjustmentIncrement(int increment){
		setProperty(ADJUSTMENT_INCREMENT_PROPERTY_NAME, increment);
	}

	public int getAdjustmentIncrement(){
		return getProperty(ADJUSTMENT_INCREMENT_PROPERTY_NAME);
	}
}
