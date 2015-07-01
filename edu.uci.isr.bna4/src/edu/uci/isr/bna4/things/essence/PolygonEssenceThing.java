package edu.uci.isr.bna4.things.essence;

import java.util.concurrent.locks.Lock;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IThingListener;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutablePoints;

public abstract class PolygonEssenceThing
	extends RectangleEssenceThing
	implements IHasMutablePoints{

	public static final String POLYGON_GENERATOR_PROPERTY_NAME = "polygonGenerator";

	public static interface IPolygonGenerator{

		Point[] calculatePoints(Rectangle r);
	}

	public PolygonEssenceThing(String id, final IPolygonGenerator polygonGenerator){
		super(id);
		setProperty(POLYGON_GENERATOR_PROPERTY_NAME, polygonGenerator);
		addThingListener(new IThingListener(){

			public void thingChanged(ThingEvent thingEvent){
				IPolygonGenerator polygonGenerator = getProperty(POLYGON_GENERATOR_PROPERTY_NAME);
				if(polygonGenerator != null){
					Object propertyName = thingEvent.getPropertyName();
					if(IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME.equals(propertyName)){
						setPoints(polygonGenerator.calculatePoints((Rectangle)thingEvent.getNewPropertyValue()));
					}
				}
				else{
					Object propertyName = thingEvent.getPropertyName();
					if(!IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME.equals(propertyName)){
						setProperty(IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME, calculateBoundingBox());
					}
				}
			}
		});
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setPoints(new Point[0]);
	}

	protected Rectangle calculateBoundingBox(){

		int x1 = Integer.MAX_VALUE;
		int x2 = Integer.MIN_VALUE;
		int y1 = Integer.MAX_VALUE;
		int y2 = Integer.MIN_VALUE;

		for(Point p: getPoints()){
			if(p.x < x1){
				x1 = p.x;
			}
			if(p.x > x2){
				x2 = p.x;
			}
			if(p.y < y1){
				y1 = p.y;
			}
			if(p.y > y2){
				y2 = p.y;
			}
		}

		return new Rectangle(x1, y1, x2 - x1, y2 - y1);
	}

	public Point[] getPoints(){
		return getProperty(POINTS_PROPERTY_NAME);
	}

	public void setPoints(Point[] points){
		setProperty(POINTS_PROPERTY_NAME, BNAUtils.clone(points));
	}

	@Override
	protected void moveRelative(int dx, int dy){
		if(dx != 0 || dy != 0){
			Lock lock = getPropertyLock();
			lock.lock();
			try{
				IPolygonGenerator polygonGenerator = getProperty(POLYGON_GENERATOR_PROPERTY_NAME);
				if(polygonGenerator != null){
					Rectangle r = getBoundingBox();
					setBoundingBox(new Rectangle(r.x + dx, r.y + dy, r.width, r.height));
				}
				else{
					Point[] points = getPoints();
					Point[] newPoints = new Point[points.length];
					for(int i = 0; i < points.length; i++){
						newPoints[i] = new Point(points[i].x + dx, points[i].y + dy);
					}
					setPoints(newPoints);
				}
			}
			finally{
				lock.unlock();
			}
		}
	}
}
