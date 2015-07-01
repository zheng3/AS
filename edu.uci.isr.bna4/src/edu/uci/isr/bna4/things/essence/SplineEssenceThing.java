package edu.uci.isr.bna4.things.essence;

import java.util.concurrent.locks.Lock;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IThingListener;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.facets.IHasEndpoints;
import edu.uci.isr.bna4.facets.IHasMutableEndpoints;
import edu.uci.isr.bna4.facets.IHasMutableMidpoints;
import edu.uci.isr.bna4.logics.coordinating.MaintainStickyPointLogic;

public abstract class SplineEssenceThing
	extends RelativeMovableEssenceThing
	implements IHasMutableEndpoints, IHasMutableMidpoints, IHasBoundingBox{

	public SplineEssenceThing(String id){
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
		setEndpoint1(new Point(0, 0));
		setEndpoint2(new Point(0, 0));
		setMidpoints(new Point[0]);
		setProperty(IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME, calculateBoundingBox());
	}

	final protected Rectangle calculateBoundingBox(){

		int x1 = Integer.MAX_VALUE;
		int x2 = Integer.MIN_VALUE;
		int y1 = Integer.MAX_VALUE;
		int y2 = Integer.MIN_VALUE;

		Point p = getEndpoint1();
		if(p != null){
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

		p = getEndpoint2();
		if(p != null){
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

		Point[] midpoints = getMidpoints();
		if(midpoints != null){
			for(Point m: midpoints){
				if(m.x < x1){
					x1 = m.x;
				}
				if(m.x > x2){
					x2 = m.x;
				}
				if(m.y < y1){
					y1 = m.y;
				}
				if(m.y > y2){
					y2 = m.y;
				}
			}
		}

		return new Rectangle(x1, y1, x2 - x1, y2 - y1);
	}

	public Rectangle getBoundingBox(){
		Rectangle bb = (Rectangle)getProperty(BOUNDING_BOX_PROPERTY_NAME);
		return new Rectangle(bb.x, bb.y, bb.width, bb.height);
	}

	public Point getEndpoint1(){
		return getProperty(ENDPOINT_1_PROPERTY_NAME);
	}

	public void setEndpoint1(Point endpoint1){
		setProperty(ENDPOINT_1_PROPERTY_NAME, BNAUtils.clone(endpoint1));
	}

	public Point getEndpoint2(){
		return getProperty(ENDPOINT_2_PROPERTY_NAME);
	}

	public void setEndpoint2(Point endpoint2){
		setProperty(ENDPOINT_2_PROPERTY_NAME, BNAUtils.clone(endpoint2));
	}

	public void setEndpoints(Point endpoint1, Point endpoint2){
		Lock lock = getPropertyLock();
		lock.lock();
		try{
			setEndpoint1(endpoint1);
			setEndpoint2(endpoint2);
		}
		finally{
			lock.unlock();
		}
	}

	public Point[] getMidpoints(){
		return getProperty(MIDPOINTS_PROPERTY_NAME);
	}

	public void setMidpoints(Point[] midpoints){
		assert midpoints != null;

		setProperty(MIDPOINTS_PROPERTY_NAME, BNAUtils.clone(midpoints));
	}

	@Override
	public Point getReferencePoint(){
		Lock lock = getPropertyLock();
		lock.lock();
		try{
			Point ep1 = MaintainStickyPointLogic.getStuckToThingId(IHasEndpoints.ENDPOINT_1_PROPERTY_NAME, this) == null ? getEndpoint1() : null;
			if(ep1 != null){
				return ep1;
			}

			Point ep2 = MaintainStickyPointLogic.getStuckToThingId(IHasEndpoints.ENDPOINT_2_PROPERTY_NAME, this) == null ? getEndpoint2() : null;
			if(ep2 != null){
				return ep2;
			}

			Point[] mps = getMidpoints();
			if(mps != null && mps.length > 0){
				return mps[0];
			}

			return new Point(0, 0);
		}
		finally{
			lock.unlock();
		}
	}

	@Override
	protected void moveRelative(int dx, int dy){
		if(dx != 0 || dy != 0){
			Lock lock = getPropertyLock();
			lock.lock();
			try{
				if(MaintainStickyPointLogic.getStuckToThingId(IHasEndpoints.ENDPOINT_1_PROPERTY_NAME, this) == null){
					Point ep1 = getEndpoint1();
					if(ep1 != null){
						setEndpoint1(new Point(ep1.x + dx, ep1.y + dy));
					}
				}

				if(MaintainStickyPointLogic.getStuckToThingId(IHasEndpoints.ENDPOINT_2_PROPERTY_NAME, this) == null){
					Point ep2 = getEndpoint2();
					if(ep2 != null){
						setEndpoint2(new Point(ep2.x + dx, ep2.y + dy));
					}
				}

				Point[] midpoints = getMidpoints();
				if(midpoints != null && midpoints.length > 0){
					Point[] newMidpoints = new Point[midpoints.length];
					for(int i = 0; i < midpoints.length; i++){
						newMidpoints[i] = new Point(midpoints[i].x + dx, midpoints[i].y + dy);
					}
					setMidpoints(newMidpoints);
				}
			}
			finally{
				lock.unlock();
			}
		}
	}
}
