package edu.uci.isr.bna4.logics.coordinating;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAModelEvent;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNASynchronousModelListener;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.things.utility.WorldThing;
import edu.uci.isr.sysutils.Tuple;

/**
 * This class maintains relationships between source and target things. A source
 * thing in a relationship might represent something with a bounded box that a
 * target thing mirrors. The target thing is always present in the world model.
 * However, the source thing may exist outside of this world in order to support
 * sticking to and mirroring values that are in sub-worlds (See
 * {@link WorldThing}, for example).
 */
public abstract class AbstractMaintainThingsLogic<S extends IThing, T extends IThing>
	extends AbstractThingLogic
	implements IBNASynchronousModelListener{

	protected final static boolean equalz(Object o1, Object o2){
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	protected final Class<? extends S> sourceClass;
	protected final Set<Object> sourcePropertyNames;
	protected final Class<? extends T> targetClass;
	protected final Set<Object> targetPropertyNames;
	protected final HashSet<Tuple> thingEventsToIgnore = new HashSet<Tuple>();

	public AbstractMaintainThingsLogic(Class<? extends S> sourceClass, Object[] sourcePropertyNames, Class<? extends T> targetClass, Object[] targetPropertyNames){
		this.sourceClass = sourceClass;
		this.sourcePropertyNames = new HashSet<Object>(Arrays.asList(sourcePropertyNames));
		this.targetClass = targetClass;
		this.targetPropertyNames = new HashSet<Object>(Arrays.asList(targetPropertyNames));
	}

	@Override
	public void init(){
		super.init();
		maintainAll();
	}

	/**
	 * The default implementation of this method queries the world model for
	 * things satisfying {@link #isTargetThing(IThing, ThingEvent)} and calls
	 * the {@link #updateToTarget(IBNAModel, IThing, ThingEvent)} method for
	 * each one.
	 */
	@SuppressWarnings("unchecked")
	protected void maintainAll(){
		IBNAModel model = getBNAModel();
		for(IThing targetThing: model.getAllThings()){
			if(isTargetThing(targetThing, null)){
				updateToTarget(null, (T)targetThing, null);
			}
		}
	}

	protected void setThingProperty(IThing thing, Object propertyName, Object newValue){
		Lock lock = thing.getPropertyLock();
		lock.lock();
		try{
			Object oldValue = thing.setProperty(propertyName, newValue);
			if(!equalz(oldValue, newValue)){
				thingEventsToIgnore.add(new Tuple(thing.getID(), propertyName));
			}
		}
		finally{
			lock.unlock();
		}
	}

	/**
	 * This method queries if an event represents a change in maintenance
	 * relationships. If the event originated from the world model, then it
	 * checks whether the changed thing is a source or target thing. If it
	 * originated from a model other than the world model, then it only checks
	 * whether the event represents a source thing.
	 * 
	 * @see edu.uci.isr.bna4.IBNASynchronousModelListener#bnaModelChangedSync(edu.uci.isr.bna4.BNAModelEvent)
	 */
	@SuppressWarnings("unchecked")
	public void bnaModelChangedSync(BNAModelEvent evt){
		switch(evt.getEventType()){

		case THING_ADDED:
		case THING_CHANGED:
			// for added things, the thingEvent will be null, but that's okay
			IBNAModel model = evt.getSource();
			IThing thing = evt.getTargetThing();
			ThingEvent thingEvent = evt.getThingEvent();

			if(model.equals(getBNAModel())){
				if(isTargetThing(thing, thingEvent)){
					if(thingEvent == null || thingEventsToIgnore.isEmpty() || !thingEventsToIgnore.remove(new Tuple(thing.getID(), thingEvent.getPropertyName()))){
						updateToTarget(null, (T)thing, thingEvent);
					}
				}
			}
			if(isSourceThing(model, thing, thingEvent)){
				updateFromSource(model, (S)thing, thingEvent);
			}
			break;
		}
	}

	/**
	 * Updates target thing(s) given the source thing in the given source model.
	 * 
	 * @param sourceModel
	 *            the {@link IBNAModel} that contains the source thing
	 * @param sourceThing
	 *            the source {@link IThing} for the relationship.
	 * @param sourceThingEvent
	 *            the event that triggered a call to this method. This may be
	 *            <code>null</code> indicating that the logic is being
	 *            initialized or that the source thing has just been added.
	 */
	abstract protected void updateFromSource(IBNAModel sourceModel, S sourceThing, ThingEvent sourceThingEvent);

	/**
	 * Updates source thing(s) given the target thing. The source model may be
	 * <code>null</code> to indicate that it is not specified.
	 * 
	 * @param sourceModel
	 *            the {@link IBNAModel} of the potential source things. This may
	 *            be <code>null</code> indicating that it is not specified.
	 *            Most implementations should assume that such a value indicates
	 *            that the world model should be used.
	 * @param targetThing
	 *            the target {@link IThing} for the relationships
	 * @param targetThingEvent
	 *            the event that triggered a call to this method. This may be
	 *            <code>null</code> indicating that the logic is being
	 *            initialized or that the target thing has just been added.
	 */
	abstract protected void updateToTarget(IBNAModel sourceModel, T targetThing, ThingEvent targetThingEvent);

	/**
	 * Determines if the given thing (and optionally thing event) should be used
	 * in a call to {@link #updateFromSource(IBNAModel, IThing, ThingEvent)}.
	 * This check needs to be relatively fast, further checking can be done in
	 * the {@link #updateFromSource(IBNAModel, IThing, ThingEvent)} method.
	 * 
	 * @param sourceModel
	 *            The model containing the source thing
	 * @param sourceThing
	 *            The source thing of interest
	 * @param sourceThingEvent
	 *            The ThingEvent that triggered this query, or <code>null</code>
	 *            if this query is a result of the logic being initialized or a
	 *            thing being added.
	 * @return <code>true</code> if the thing likely represents a source thing
	 *         in a maintenance relationship and if the thing event represents a
	 *         change related to the maintenance relationship (when present),
	 *         <code>false</code> otherwise.
	 */
	protected boolean isSourceThing(IBNAModel sourceModel, IThing sourceThing, ThingEvent sourceThingEvent){
		if(sourceClass == null || sourceClass.isInstance(sourceThing)){
			if(sourceThingEvent != null){
				return sourcePropertyNames.contains(sourceThingEvent.getPropertyName());
			}
			return true;
		}
		return false;
	}

	/**
	 * Determines if the given thing (and optionally thing event) should be used
	 * in a call to {@link #updateToTarget(IBNAModel, IThing, ThingEvent)} This
	 * check needs to be relatively fast, further checking can be done in the
	 * {@link #updateToTarget(IBNAModel, IThing, ThingEvent)} method.
	 * 
	 * @param targetThing
	 *            The target thing of interest
	 * @param targetThingEvent
	 *            The ThingEvent that triggered this query, or <code>null</code>
	 *            if this query is a result of the logic being initialized or a
	 *            thing being added.
	 * @return <code>true</code> if the thing represents a target thing in a
	 *         maintenance relationship and if the thing event represents a
	 *         change related to the maintenance relationship (when present),
	 *         <code>false</code> otherwise.
	 */
	protected boolean isTargetThing(IThing targetThing, ThingEvent targetThingEvent){
		if(targetClass == null || targetClass.isInstance(targetThing)){
			if(targetThingEvent != null){
				return targetPropertyNames.contains(targetThingEvent.getPropertyName());
			}
			return true;
		}
		return false;
	}
}
