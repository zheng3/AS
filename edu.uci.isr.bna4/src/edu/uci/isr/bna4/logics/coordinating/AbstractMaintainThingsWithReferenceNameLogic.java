package edu.uci.isr.bna4.logics.coordinating;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.logics.tracking.ReferenceTrackingLogic;

public abstract class AbstractMaintainThingsWithReferenceNameLogic<S extends IThing, T extends IThing>
	extends AbstractMaintainThingsLogic<S, T>{

	protected final ReferenceTrackingLogic rtl;
	protected final Object referencePropertyName;

	public AbstractMaintainThingsWithReferenceNameLogic(Class<? extends S> sourceClass, Object[] sourcePropertyNames, Class<? extends T> targetClass, Object[] targetPropertyNames, ReferenceTrackingLogic rtl, Object referencePropertyName){
		super(sourceClass, sourcePropertyNames, targetClass, targetPropertyNames);
		this.rtl = rtl;
		this.referencePropertyName = referencePropertyName;
		this.targetPropertyNames.add(referencePropertyName);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void maintainAll(){
		IBNAModel model = getBNAModel();
		for(String targetThingId: rtl.getThingsReferencing(referencePropertyName)){
			IThing targetThing = model.getThing(targetThingId);
			if(isTargetThing(targetThing, null)){
				updateToTarget(null, (T)targetThing, null);
			}
		}
	}

	abstract protected void maintain(IBNAModel sourceModel, S sourceThing, T targetThing, ThingEvent thingEvent);

	@SuppressWarnings("unchecked")
	@Override
	protected void updateFromSource(IBNAModel sourceModel, S sourceThing, ThingEvent sourceThingEvent){
		IBNAModel targetModel = getBNAModel();
		for(String targetThingId: rtl.getThingsReferencing(sourceThing.getID(), referencePropertyName)){
			IThing targetThing = targetModel.getThing(targetThingId);
			if(targetThing != null && isTargetThing(targetThing, null)){
				maintain(sourceModel, sourceThing, (T)targetThing, sourceThingEvent);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void updateToTarget(IBNAModel sourceModel, T targetThing, ThingEvent targetThingEvent){
		String sourceThingID = targetThing.getProperty(referencePropertyName);
		if(sourceThingID != null){
			IBNAModel targetModel = getBNAModel();
			if(sourceModel == null){
				sourceModel = targetModel;
			}
			IThing sourceThing = sourceModel.getThing(sourceThingID);
			if(sourceThing != null && isSourceThing(sourceModel, sourceThing, null)){
				maintain(sourceModel, (S)sourceThing, targetThing, targetThingEvent);
			}
		}
	}
}
