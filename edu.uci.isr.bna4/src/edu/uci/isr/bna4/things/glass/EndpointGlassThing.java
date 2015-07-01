package edu.uci.isr.bna4.things.glass;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.IThingListener;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.things.essence.AnchorPointEssenceThing;

public class EndpointGlassThing
	extends AnchorPointEssenceThing
	implements IHasBoundingBox{

	public EndpointGlassThing(){
		this(null);
	}

	public EndpointGlassThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setProperty(IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME, calculateBoundingBox());

		addThingListener(new IThingListener(){

			public void thingChanged(ThingEvent thingEvent){
				Object propertyName = thingEvent.getPropertyName();
				if(!IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME.equals(propertyName)){
					setProperty(IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME, calculateBoundingBox());
				}
			}
		});

	}

	protected Rectangle calculateBoundingBox(){
		Point p = getAnchorPoint();
		int w = getWidth();
		int h = getHeight();
		return new Rectangle(p.x - w / 2, p.y - w / 2, w, h);
	}

	protected int getHeight(){
		return 10;
	}

	protected int getWidth(){
		return 10;
	}

	public Rectangle getBoundingBox(){
		return getProperty(BOUNDING_BOX_PROPERTY_NAME);
	}
}
