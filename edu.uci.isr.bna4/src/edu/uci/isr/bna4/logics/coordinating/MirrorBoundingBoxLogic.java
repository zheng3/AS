package edu.uci.isr.bna4.logics.coordinating;

import java.util.concurrent.locks.Lock;

import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutableBoundingBox;
import edu.uci.isr.bna4.logics.tracking.ReferenceTrackingLogic;

public class MirrorBoundingBoxLogic
	extends
	AbstractMaintainThingsWithReferenceNameLogic<IHasBoundingBox, IHasMutableBoundingBox>{

	private static final String BOUNDING_BOX_MASTER_THING_ID_PROPERTY_NAME = "&boundingBoxMaster";
	private static final String BOUNDING_BOX_MIRROR_OFFSETS_PROPETY_NAME = "boundingBoxMirrorOffsets";

	public static final void mirrorBoundingBox(IHasBoundingBox sourceThing, Rectangle offsets, IHasMutableBoundingBox... targetThings){
		for(IThing targetThing: targetThings){
			targetThing.setProperty(BOUNDING_BOX_MIRROR_OFFSETS_PROPETY_NAME, offsets);
			targetThing.setProperty(BOUNDING_BOX_MASTER_THING_ID_PROPERTY_NAME, sourceThing.getID());
		}
	}

	public MirrorBoundingBoxLogic(ReferenceTrackingLogic rtl){
		super(IHasBoundingBox.class, new String[]{IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME}, IHasMutableBoundingBox.class, new String[]{IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME, BOUNDING_BOX_MIRROR_OFFSETS_PROPETY_NAME}, rtl, BOUNDING_BOX_MASTER_THING_ID_PROPERTY_NAME);
	}

	@Override
	protected void maintain(IBNAModel sourceModel, IHasBoundingBox sourceThing, IHasMutableBoundingBox targetThing, ThingEvent thingEvent){
		Lock lock = targetThing.getPropertyLock();
		lock.lock();
		try{
			Rectangle bb = sourceThing.getBoundingBox();
			Rectangle bbo = targetThing.getProperty(BOUNDING_BOX_MIRROR_OFFSETS_PROPETY_NAME);
			Rectangle nbb = new Rectangle(bb.x, bb.y, bb.width, bb.height);
			if(bbo != null){
				nbb.x += bbo.x;
				nbb.y += bbo.y;
				nbb.width += bbo.width;
				if(nbb.width < 0){
					nbb.width = 0;
				}
				nbb.height += bbo.height;
				if(nbb.height < 0){
					nbb.height = 0;
				}
			}
			targetThing.setBoundingBox(nbb);
		}
		finally{
			lock.unlock();
		}
	}
}
