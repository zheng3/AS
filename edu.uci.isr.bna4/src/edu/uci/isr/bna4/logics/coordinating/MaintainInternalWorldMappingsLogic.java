package edu.uci.isr.bna4.logics.coordinating;

import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAModelEvent;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNASynchronousModelListener;
import edu.uci.isr.bna4.IBNAWorld;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.facets.IHasInternalWorldEndpoint;
import edu.uci.isr.bna4.facets.IHasInternalWorldMapping;
import edu.uci.isr.bna4.facets.IHasWorld;
import edu.uci.isr.bna4.logics.coordinating.MaintainStickyPointLogic.StickyMode;
import edu.uci.isr.bna4.logics.events.IInternalWorldEventListener;
import edu.uci.isr.bna4.logics.events.InternalWorldEventsLogic;
import edu.uci.isr.bna4.logics.tracking.ReferenceTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.ThingPropertyTrackingLogic;

public class MaintainInternalWorldMappingsLogic
	extends
	AbstractMaintainThingsWithReferenceNameLogic<IHasWorld, IHasInternalWorldMapping>
	implements IInternalWorldEventListener{

	protected final ThingPropertyTrackingLogic tptl;
	protected final InternalWorldEventsLogic iwel;

	public MaintainInternalWorldMappingsLogic(ReferenceTrackingLogic rtl, ThingPropertyTrackingLogic tptl, InternalWorldEventsLogic iwel){
		super(IHasWorld.class, new String[]{}, IHasInternalWorldMapping.class, new String[]{IHasInternalWorldEndpoint.INTERNAL_ENDPOINT_PROPERTY_NAME, IHasInternalWorldMapping.INTERNAL_ENDPOINT_STUCK_TO_THING_ID_PROPERTY_NAME}, rtl, IHasInternalWorldEndpoint.INTERNAL_ENDPOINT_WORLD_THING_ID_PROPERTY_NAME);
		this.tptl = tptl;
		this.iwel = iwel;
	}

	@Override
	protected void maintain(IBNAModel sourceModel, IHasWorld sourceThing, IHasInternalWorldMapping targetThing, ThingEvent thingEvent){
		IBNAWorld world = sourceThing.getWorld();
		if(world != null){
			IThing internalThing = world.getBNAModel().getThing(targetThing.getInternalEndpointStuckToThingID());
			if(internalThing != null){
				targetThing.setInternalEndpoint(MaintainStickyPointLogic.getStuckPoint(internalThing, StickyMode.CENTER, new Point(0, 0)));
			}
		}
	}

	private class UpdateExternalWorldMappings
		extends AbstractThingLogic
		implements IBNASynchronousModelListener{

		public void bnaModelChangedSync(BNAModelEvent evt){
			switch(evt.getEventType()){
			case THING_ADDED:
			case THING_CHANGED: {
				for(IThing t: tptl.getThings(IHasInternalWorldMapping.INTERNAL_ENDPOINT_STUCK_TO_THING_ID_PROPERTY_NAME, evt.getTargetThing().getID())){
					if(t instanceof IHasInternalWorldMapping){
						IThing worldThing = MaintainInternalWorldMappingsLogic.this.getBNAModel().getThing(((IHasInternalWorldMapping)t).getInternalEndpointWorldThingID());
						if(worldThing instanceof IHasWorld){
							maintain(getBNAModel(), (IHasWorld)worldThing, (IHasInternalWorldMapping)t, null);
						}
					}
				}
			}
				break;
			}
		}
	}

	public void innerWorldAdded(IBNAWorld world){
		world.getThingLogicManager().addThingLogic(new UpdateExternalWorldMappings());
	}

	public void innerWorldRemoved(IBNAWorld world){
	}
}
