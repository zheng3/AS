package edu.uci.isr.bna4.things.essence;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.PathData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.IThingListener;
import edu.uci.isr.bna4.PathDataUtils;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutablePathData;

public class PathEssenceThing
	extends AnchorPointEssenceThing
	implements IHasMutablePathData, IHasBoundingBox{

	public PathEssenceThing(String id){
		super(id);

		addThingListener(new IThingListener(){

			public void thingChanged(ThingEvent thingEvent){
				Object propertyName = thingEvent.getPropertyName();
				if(!IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME.equals(propertyName)){
					setProperty(IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME, calculateBoundingBox());
				}
			}
		});
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setPathData(PathDataUtils.EMPTY_PATH_DATA);
		setProperty(IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME, calculateBoundingBox());
	}

	final protected Rectangle calculateBoundingBox(){

		PathData p = getPathData();
		Point o = getAnchorPoint();

		float x1 = Float.MAX_VALUE;
		float y1 = Float.MAX_VALUE;
		float x2 = Float.MIN_VALUE;
		float y2 = Float.MIN_VALUE;

		int i = 0;
		float[] points = p.points;

		for(byte type: p.types){
			switch(type){
			case SWT.PATH_MOVE_TO: {
				float x = points[i++] + o.x;
				float y = points[i++] + o.y;
				x1 = Math.min(x1, x);
				x2 = Math.max(x2, x);
				y1 = Math.min(y1, y);
				y2 = Math.max(y2, y);
				break;
			}
			case SWT.PATH_LINE_TO: {
				float x = points[i++] + o.x;
				float y = points[i++] + o.y;
				x1 = Math.min(x1, x);
				x2 = Math.max(x2, x);
				y1 = Math.min(y1, y);
				y2 = Math.max(y2, y);
				break;
			}
			case SWT.PATH_CLOSE: {
				// do nothing, the "close" point was already included
				break;
			}
			default:
				throw new UnsupportedOperationException("Unimplemented");
			}
		}

		int ix1 = (int)Math.round(Math.floor(x1));
		int iy1 = (int)Math.round(Math.floor(y1));
		int ix2 = (int)Math.round(Math.ceil(x2));
		int iy2 = (int)Math.round(Math.ceil(y2));

		return new Rectangle(ix1, iy1, ix2 - ix1, iy2 - iy1);
	}

	public Rectangle getBoundingBox(){
		Rectangle bb = (Rectangle)getProperty(BOUNDING_BOX_PROPERTY_NAME);
		return new Rectangle(bb.x, bb.y, bb.width, bb.height);
	}

	public PathData getPathData(){
		return getProperty(PATH_DATA_PROPERTY_NAME);
	}

	public void setPathData(PathData pathData){
		setProperty(PATH_DATA_PROPERTY_NAME, pathData);
	}

}
