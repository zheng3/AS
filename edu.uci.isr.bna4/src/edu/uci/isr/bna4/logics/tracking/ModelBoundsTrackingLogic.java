package edu.uci.isr.bna4.logics.tracking;

import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAModelEvent;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNASynchronousLockModelListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IBNAWorld;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.IThingLogicManager;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.facets.IHasBoundingBox;

public class ModelBoundsTrackingLogic
    extends AbstractThingLogic
    implements IBNASynchronousLockModelListener{

	public static Rectangle getModelBounds(IBNAWorld world){
		IThingLogicManager tlm = world.getThingLogicManager();
		ModelBoundsTrackingLogic mbtl = null;
		if(tlm != null){
			for(ModelBoundsTrackingLogic tl: tlm.getThingLogics(ModelBoundsTrackingLogic.class)){
				mbtl = tl;
			}
			if(mbtl == null){
				TypedThingTrackingLogic tttl = null;
				for(TypedThingTrackingLogic tl: tlm.getThingLogics(TypedThingTrackingLogic.class)){
					tttl = tl;
				}
				if(tttl == null){
					tlm.addThingLogic(tttl = new TypedThingTrackingLogic());
				}
				tlm.addThingLogic(mbtl = new ModelBoundsTrackingLogic(tttl));
			}
		}
		return mbtl.getModelBounds();
	}

	protected final TypedThingTrackingLogic tttl;

	private Rectangle modelBounds = null;

	public ModelBoundsTrackingLogic(TypedThingTrackingLogic tttl){
		this.tttl = tttl;
	}

	public void bnaModelChangedSyncLock(BNAModelEvent evt){
		switch(evt.getEventType()){
		case THING_ADDED: {
			IThing thing = evt.getTargetThing();
			if(thing instanceof IHasBoundingBox){
				synchronized(this){
					updateModelBounds(null, ((IHasBoundingBox)thing).getBoundingBox());
				}
			}
		}
			break;
		case THING_REMOVED: {
			IThing thing = evt.getTargetThing();
			if(thing instanceof IHasBoundingBox){
				synchronized(this){
					updateModelBounds(((IHasBoundingBox)thing).getBoundingBox(), null);
				}
			}
		}
			break;
		case THING_CHANGED: {
			ThingEvent te = evt.getThingEvent();
			if(IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME.equals(te.getPropertyName())){
				synchronized(this){
					updateModelBounds((Rectangle)te.getOldPropertyValue(), (Rectangle)te.getNewPropertyValue());
				}
			}
		}
			break;
		}
	}

	private void updateModelBounds(Rectangle obb, Rectangle nbb){
		assert Thread.holdsLock(this);

		if(modelBounds != null){
			if(obb != null){
				//See if it was on the edge of the old model
				if(obb.x == modelBounds.x || obb.y == modelBounds.y || obb.x + obb.width == modelBounds.x + modelBounds.width || obb.y + obb.height == modelBounds.y + modelBounds.height){
					//It was, let's recalc the whole thing since it may have moved inward by some dimension
					modelBounds = null;
					return;
				}
			}
			if(nbb != null){
				Rectangle newModelBounds = BNAUtils.clone(modelBounds);
				if(nbb.x <= 0 || nbb.y <= 0){
					return;
				}
				if(nbb.x < modelBounds.x){
					newModelBounds.x = nbb.x;
				}
				if(nbb.y < modelBounds.y){
					newModelBounds.y = nbb.y;
				}
				if(nbb.x + nbb.width > modelBounds.x + modelBounds.width){
					newModelBounds.width = nbb.x + nbb.width - newModelBounds.x;
				}
				if(nbb.y + nbb.height > modelBounds.y + modelBounds.height){
					newModelBounds.height = nbb.y + nbb.height - newModelBounds.y;
				}
				modelBounds = newModelBounds;
			}
		}
	}

	private void updateModelBounds(){
		assert Thread.holdsLock(this);

		if(modelBounds == null){
			IBNAModel m = getBNAModel();
			if(m != null){
				int x1 = Integer.MAX_VALUE;
				int y1 = Integer.MAX_VALUE;
				int x2 = Integer.MIN_VALUE;
				int y2 = Integer.MIN_VALUE;

				for(IHasBoundingBox t: tttl.getThings(IHasBoundingBox.class)){
					if(!Boolean.TRUE.equals(t.getProperty(IBNAView.HIDE_THING_PROPERTY_NAME))){
						Rectangle bb = t.getBoundingBox();
						if(bb != null){
							if(bb.x <= 0 || bb.y <= 0){
								continue;
							}
							if(bb.x < x1){
								x1 = bb.x;
							}
							if(bb.y < y1){
								y1 = bb.y;
							}
							if(bb.x + bb.width > x2){
								x2 = bb.x + bb.width;
							}
							if(bb.y + bb.height > y2){
								y2 = bb.y + bb.height;
							}
						}
					}
				}
				modelBounds = new Rectangle(x1, y1, x2 - x1, y2 - y1);
			}
		}
	}

	public synchronized Rectangle getModelBounds(){
		synchronized(this){
			updateModelBounds();
			if(modelBounds != null){
				return BNAUtils.clone(modelBounds);
			}
			return BNAUtils.NONEXISTENT_RECTANGLE;
		}
	}
}
