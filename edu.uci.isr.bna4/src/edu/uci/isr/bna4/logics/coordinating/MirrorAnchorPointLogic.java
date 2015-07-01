package edu.uci.isr.bna4.logics.coordinating;

import java.util.concurrent.locks.Lock;

import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.facets.IHasAnchorPoint;
import edu.uci.isr.bna4.facets.IHasMutableAnchorPoint;
import edu.uci.isr.bna4.logics.tracking.ReferenceTrackingLogic;

public class MirrorAnchorPointLogic
	extends
	AbstractMaintainThingsWithReferenceNameLogic<IHasAnchorPoint, IHasMutableAnchorPoint>{

	private static final String ANCHOR_POINT_MASTER_THING_ID_PROPERTY_NAME = "&anchorPointMaster";
	private static final String ANCHOR_POINT_MIRROR_OFFSETS_PROPETY_NAME = "anchorPointMirrorOffsets";

	public static final void mirrorAnchorPoint(IHasAnchorPoint sourceThing, Point offset, IHasMutableAnchorPoint... targetThings){
		for(IThing targetThing: targetThings){
			targetThing.setProperty(ANCHOR_POINT_MIRROR_OFFSETS_PROPETY_NAME, offset);
			targetThing.setProperty(ANCHOR_POINT_MASTER_THING_ID_PROPERTY_NAME, sourceThing.getID());
		}
	}

	public MirrorAnchorPointLogic(ReferenceTrackingLogic rtl){
		super(IHasAnchorPoint.class, new String[]{IHasAnchorPoint.ANCHOR_POINT_PROPERTY_NAME}, IHasMutableAnchorPoint.class, new String[]{IHasAnchorPoint.ANCHOR_POINT_PROPERTY_NAME, ANCHOR_POINT_MIRROR_OFFSETS_PROPETY_NAME}, rtl, ANCHOR_POINT_MASTER_THING_ID_PROPERTY_NAME);
	}

	@Override
	protected void maintain(IBNAModel sourceModel, IHasAnchorPoint sourceThing, IHasMutableAnchorPoint targetThing, ThingEvent thingEvent){
		Lock lock = targetThing.getPropertyLock();
		lock.lock();
		try{
			Point ap = sourceThing.getAnchorPoint();
			Point apo = targetThing.getProperty(ANCHOR_POINT_MIRROR_OFFSETS_PROPETY_NAME);
			Point nap = new Point(ap.x, ap.y);
			if(apo != null){
				nap.x += apo.x;
				nap.y += apo.y;
			}
			targetThing.setAnchorPoint(nap);
		}
		finally{
			lock.unlock();
		}
	}
}
