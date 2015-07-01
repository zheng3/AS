package edu.uci.isr.bna4.logics.coordinating;

import java.util.concurrent.locks.Lock;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.facets.IHasAnchorPoint;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.facets.IRelativeMovable;
import edu.uci.isr.bna4.logics.tracking.ReferenceTrackingLogic;

public class MoveWithLogic
	extends AbstractMaintainThingsWithReferenceNameLogic<IThing, IRelativeMovable>{

	public static final int TRACK_BOUNDING_BOX_ONLY = 1010;
	public static final int TRACK_ANCHOR_POINT_ONLY = 1025;
	public static final int TRACK_BOUNDING_BOX_FIRST = 1030;
	public static final int TRACK_ANCHOR_POINT_FIRST = 1050;

	public static final String MOVES_WITH_THING_ID_PROPERTY_NAME = "&movesWithThingID";
	public static final String MOVES_WITH_MODE_PROPERTY_NAME = "movesWithMode";

	public static final void moveWith(IThing sourceThing, int movesWithMode, IRelativeMovable... targetThings){
		for(IThing targetThing: targetThings){
			targetThing.setProperty(MOVES_WITH_MODE_PROPERTY_NAME, movesWithMode);
			targetThing.setProperty(MOVES_WITH_THING_ID_PROPERTY_NAME, sourceThing.getID());
		}
	}

	public MoveWithLogic(ReferenceTrackingLogic rtl){
		super(null, new String[]{IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME, IHasAnchorPoint.ANCHOR_POINT_PROPERTY_NAME}, IRelativeMovable.class, new String[]{MOVES_WITH_MODE_PROPERTY_NAME}, rtl, MOVES_WITH_THING_ID_PROPERTY_NAME);
	}

	@Override
	protected void maintain(IBNAModel sourceModel, IThing sourceThing, IRelativeMovable targetThing, ThingEvent thingEvent){
		if(thingEvent != null){
			Integer movesWithMode = targetThing.getProperty(MOVES_WITH_MODE_PROPERTY_NAME);
			if(movesWithMode != null){
				switch(movesWithMode){

				case TRACK_BOUNDING_BOX_FIRST:
					if(sourceThing instanceof IHasBoundingBox){
						if(IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME.equals(thingEvent.getPropertyName())){
							maintainWithBoundingBoxDelta(sourceModel, sourceThing, (Rectangle)thingEvent.getOldPropertyValue(), (Rectangle)thingEvent.getNewPropertyValue(), targetThing);
						}
						break;
					}
					// fall through
				case TRACK_ANCHOR_POINT_ONLY:
					if(sourceThing instanceof IHasAnchorPoint){
						if(IHasAnchorPoint.ANCHOR_POINT_PROPERTY_NAME.equals(thingEvent.getPropertyName())){
							maintainWithAnchorPointDelta(sourceModel, sourceThing, (Point)thingEvent.getOldPropertyValue(), (Point)thingEvent.getNewPropertyValue(), targetThing);
						}
					}
					break;

				case TRACK_ANCHOR_POINT_FIRST:
					if(sourceThing instanceof IHasAnchorPoint){
						if(IHasAnchorPoint.ANCHOR_POINT_PROPERTY_NAME.equals(thingEvent.getPropertyName())){
							maintainWithAnchorPointDelta(sourceModel, sourceThing, (Point)thingEvent.getOldPropertyValue(), (Point)thingEvent.getNewPropertyValue(), targetThing);
						}
						break;
					}
					// fall through
				case TRACK_BOUNDING_BOX_ONLY:
					if(sourceThing instanceof IHasBoundingBox){
						if(IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME.equals(thingEvent.getPropertyName())){
							maintainWithBoundingBoxDelta(sourceModel, sourceThing, (Rectangle)thingEvent.getOldPropertyValue(), (Rectangle)thingEvent.getNewPropertyValue(), targetThing);
						}
					}
					break;
				}
			}
		}
	}

	protected void maintainWithAnchorPointDelta(IBNAModel model, IThing sourceThing, Point oldAnchorPoint, Point newAnchorPoint, IRelativeMovable targetThing){
		if(oldAnchorPoint != null && newAnchorPoint != null && targetThing instanceof IRelativeMovable){
			Lock lock = targetThing.getPropertyLock();
			lock.lock();
			try{
				int dx = newAnchorPoint.x - oldAnchorPoint.x;
				int dy = newAnchorPoint.y - oldAnchorPoint.y;
				Point p = targetThing.getReferencePoint();
				targetThing.setReferencePoint(new Point(p.x + dx, p.y + dy));
			}
			finally{
				lock.unlock();
			}
		}
	}

	protected void maintainWithBoundingBoxDelta(IBNAModel model, IThing sourceThing, Rectangle oldBoundingBox, Rectangle newBoundingBox, IRelativeMovable targetThing){
		Lock lock = targetThing.getPropertyLock();
		lock.lock();
		try{
			Point newPoint = BNAUtils.movePointWith(oldBoundingBox, newBoundingBox, targetThing.getReferencePoint());
			targetThing.setReferencePoint(newPoint);
		}
		finally{
			lock.unlock();
		}
	}

	@Override
	protected boolean isSourceThing(IBNAModel sourceModel, IThing sourceThing, ThingEvent sourceThingEvent){
		return (sourceThing instanceof IHasAnchorPoint || sourceThing instanceof IHasBoundingBox) && super.isSourceThing(sourceModel, sourceThing, sourceThingEvent);
	}

	@Override
	protected boolean isTargetThing(IThing targetThing, ThingEvent targetThingEvent){
		return targetThing instanceof IRelativeMovable && super.isTargetThing(targetThing, targetThingEvent);
	}
}
