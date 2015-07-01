package edu.uci.isr.bna4.logics.coordinating;

import java.util.Map;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.logics.tracking.ReferenceTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.ThingPropertyPrefixTrackingLogic;
import edu.uci.isr.sysutils.Tuple;

public abstract class AbstractMaintainThingsWithReferencePrefixLogic<S extends IThing, T extends IThing>
	extends AbstractMaintainThingsLogic<S, T>{

	protected final ReferenceTrackingLogic rtl;
	protected final ThingPropertyPrefixTrackingLogic tpptl;
	protected final Object referencePropertyNamePrefix;

	public AbstractMaintainThingsWithReferencePrefixLogic(Class<? extends S> sourceClass, Object[] sourcePropertyNames, Class<? extends T> targetClass, Object[] targetPropertyNames, ReferenceTrackingLogic rtl, ThingPropertyPrefixTrackingLogic tpptl, Object referencePropertyNamePrefix){
		super(sourceClass, sourcePropertyNames, targetClass, targetPropertyNames);
		this.rtl = rtl;
		this.tpptl = tpptl;
		this.referencePropertyNamePrefix = referencePropertyNamePrefix;
	}

	abstract protected void maintain(IBNAModel sourceModel, IThing sourceThing, IThing targetThing, Object propertyName, ThingEvent thingEvent);

	protected Map<Object, String[]> updateFromSourceTargetIds(IBNAModel sourceModel, IThing sourceThing, ThingEvent sourceThingEvent){
		return rtl.getThingsReferencing(sourceThing.getID(), tpptl.getPropertyNamesWithPrefix(referencePropertyNamePrefix));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void maintainAll(){
		IBNAModel model = getBNAModel();
		for(Map.Entry<Object, String[]> entry: rtl.getThingsReferencing(tpptl.getPropertyNamesWithPrefix(referencePropertyNamePrefix)).entrySet()){
			Object propertyName = entry.getKey();
			for(String targetThingId: entry.getValue()){
				IThing targetThing = model.getThing(targetThingId);
				if(isTargetThing(targetThing, null)){
					updateToTarget(null, targetThing, propertyName, null);
				}
			}
		}
	}

	@Override
	protected void updateFromSource(IBNAModel sourceModel, IThing sourceThing, ThingEvent sourceThingEvent){
		IBNAModel targetModel = getBNAModel();
		if(sourceThingEvent != null){
			for(Map.Entry<Object, String[]> entry: updateFromSourceTargetIds(sourceModel, sourceThing, sourceThingEvent).entrySet()){
				Object propertyName = ((Tuple)entry.getKey()).getElement(1);
				for(String targetThingId: entry.getValue()){
					IThing targetThing = targetModel.getThing(targetThingId);
					if(targetThing != null && isTargetThing(targetThing, null)){
						maintain(sourceModel, sourceThing, targetThing, propertyName, sourceThingEvent);
					}
				}
			}
		}
		else{
			// the source thing was probably just added, update all target values
			for(Map.Entry<Object, String[]> entry: updateFromSourceTargetIds(sourceModel, sourceThing, sourceThingEvent).entrySet()){
				Object propertyName = ((Tuple)entry.getKey()).getElement(1);
				for(String targetThingId: entry.getValue()){
					IThing targetThing = targetModel.getThing(targetThingId);
					if(targetThing != null && isTargetThing(targetThing, null)){
						maintain(sourceModel, sourceThing, targetThing, propertyName, sourceThingEvent);
					}
				}
			}
		}
	}

	protected void updateToTarget(IBNAModel sourceModel, IThing targetThing, Object propertyName, ThingEvent targetThingEvent){
		IBNAModel targetModel = getBNAModel();
		if(sourceModel == null){
			sourceModel = targetModel;
		}

		if(propertyName instanceof Tuple && ((Tuple)propertyName).startsWith(referencePropertyNamePrefix)){
			// the reference was changed, update the value
			String sourceThingID = targetThing.getProperty(propertyName);
			if(sourceThingID != null){
				IThing sourceThing = sourceModel.getThing(sourceThingID);
				if(sourceThing != null && isSourceThing(sourceModel, sourceThing, null)){
					maintain(sourceModel, sourceThing, targetThing, ((Tuple)propertyName).getElement(1), targetThingEvent);
				}
			}
		}
		else{
			String sourceThingID = targetThing.getProperty(new Tuple(referencePropertyNamePrefix, propertyName));
			if(sourceThingID != null){
				// the value was changed, verify it
				IThing sourceThing = sourceModel.getThing(sourceThingID);
				if(sourceThing != null && isSourceThing(sourceModel, sourceThing, null)){
					maintain(sourceModel, sourceThing, targetThing, propertyName, targetThingEvent);
				}
			}
		}
	}

	@Override
	protected void updateToTarget(IBNAModel sourceModel, IThing targetThing, ThingEvent targetThingEvent){
		if(targetThingEvent != null){
			updateToTarget(sourceModel, targetThing, targetThingEvent.getPropertyName(), targetThingEvent);
		}
		else{
			IBNAModel targetModel = getBNAModel();
			if(sourceModel == null){
				sourceModel = targetModel;
			}

			// the target thing was probably just added, verify all values
			for(Object propertyName: tpptl.getPropertyNamesWithPrefix(referencePropertyNamePrefix)){
				String sourceThingID = targetThing.getProperty(propertyName);
				if(sourceThingID != null){
					IThing sourceThing = sourceModel.getThing(sourceThingID);
					if(sourceThing != null && isSourceThing(sourceModel, sourceThing, null)){
						maintain(sourceModel, sourceThing, targetThing, ((Tuple)propertyName).getElement(1), targetThingEvent);
					}
				}
			}
		}
	}

	@Override
	protected boolean isTargetThing(IThing targetThing, ThingEvent targetThingEvent){
		if(targetThingEvent != null){
			Object propertyName = targetThingEvent.getPropertyName();
			if(propertyName instanceof Tuple && ((Tuple)propertyName).startsWith(referencePropertyNamePrefix)){
				// the reference has changed, update the value
				return super.isTargetThing(targetThing, null);
			}
			if(targetThingEvent.getTargetThing().getProperty(new Tuple(referencePropertyNamePrefix, propertyName)) != null){
				// the value has changed, check it
				return super.isTargetThing(targetThing, null);
			}
			return false;
		}
		return super.isTargetThing(targetThing, targetThingEvent);
	}
}
