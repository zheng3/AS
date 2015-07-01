package edu.uci.isr.bna4.things.borders;

import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.facets.IHasMutableRotatingOffset;
import edu.uci.isr.bna4.things.essence.RectangleEssenceThing;

public class MarqueeBoxBorderThing
	extends RectangleEssenceThing
	implements IHasMutableRotatingOffset{

	public MarqueeBoxBorderThing(){
		this(null);
	}

	public MarqueeBoxBorderThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setProperty(ROTATING_OFFSET_PROPERTY_NAME, 0);
		setMinBoundingBoxSize(new Point(0, 0));
	}

	public int getRotatingOffset(){
		return getProperty(ROTATING_OFFSET_PROPERTY_NAME);
	}

	public void incrementRotatingOffset(){
		setProperty(ROTATING_OFFSET_PROPERTY_NAME, (getRotatingOffset() + 1) % 6);
	}
}
