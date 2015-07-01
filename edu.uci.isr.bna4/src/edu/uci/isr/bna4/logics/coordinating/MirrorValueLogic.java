package edu.uci.isr.bna4.logics.coordinating;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.logics.tracking.ReferenceTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.ThingPropertyPrefixTrackingLogic;
import edu.uci.isr.sysutils.Tuple;

public class MirrorValueLogic
	extends AbstractMaintainThingsWithReferencePrefixLogic<IThing, IThing>{

	private static final String REFERENCE_PROPERTY_NAME_PREFIX = "&mirrorValue";

	public static final Object getReferenceName(Object propertyName){
		return new Tuple(REFERENCE_PROPERTY_NAME_PREFIX, propertyName);
	}

	public static final boolean isReferenceName(Object propertyName){
		return propertyName instanceof Tuple && ((Tuple)propertyName).startsWith(REFERENCE_PROPERTY_NAME_PREFIX);
	}

	public static final void mirrorValue(IThing sourceThing, Object propertyName, IThing... targetThings){
		mirrorValue(sourceThing.getID(), propertyName, targetThings);
	}

	public static final void mirrorValue(String sourceThingId, Object propertyName, IThing... targetThings){
		for(IThing targetThing: targetThings){
			targetThing.setProperty(getReferenceName(propertyName), sourceThingId);
		}
	}

	public static final void unmirrorValue(Object propertyName, IThing... targetThings){
		for(IThing targetThing: targetThings){
			targetThing.removeProperty(getReferenceName(propertyName));
		}
	}

	public MirrorValueLogic(ReferenceTrackingLogic rtl, ThingPropertyPrefixTrackingLogic tpptl){
		super(null, new String[]{}, null, new String[]{}, rtl, tpptl, REFERENCE_PROPERTY_NAME_PREFIX);
	}

	@Override
	protected void maintain(IBNAModel sourceModel, IThing sourceThing, IThing targetThing, Object propertyName, ThingEvent thingEvent){
		targetThing.setProperty(propertyName, sourceThing.getProperty(propertyName));
	}

	@Override
	protected boolean isSourceThing(IBNAModel sourceModel, IThing sourceThing, ThingEvent sourceThingEvent){
		return true;
	}
}
