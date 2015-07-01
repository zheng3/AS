package edu.uci.isr.bna4.logics.tracking;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAModelEvent;
import edu.uci.isr.bna4.IBNASynchronousModelListener;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ThingEvent;

public class ThingPropertyTrackingLogic
	extends AbstractThingLogic
	implements IBNASynchronousModelListener{

	private static final IThing[] EMPTY_THINGS_ARRAY = new IThing[0];

	protected final Map<Object, Map<Object, Set<IThing>>> propertyNamesToValuesToThings = new HashMap<Object, Map<Object, Set<IThing>>>();

	public void bnaModelChangedSync(BNAModelEvent evt){
		switch(evt.getEventType()){
		case THING_ADDED: {
			IThing thing = evt.getTargetThing();
			for(Object name: thing.getAllPropertyNames()){
				update(thing, name, null, thing.getProperty(name));
			}
			break;
		}
		case THING_CHANGED: {
			IThing thing = evt.getTargetThing();
			ThingEvent te = evt.getThingEvent();
			switch(te.getEventType()){
			case PROPERTY_SET:
			case PROPERTY_REMOVED:
				update(thing, te.getPropertyName(), te.getOldPropertyValue(), te.getNewPropertyValue());
			}
			break;
		}
		case THING_REMOVED: {
			IThing thing = evt.getTargetThing();
			for(Object name: thing.getAllPropertyNames()){
				update(thing, name, thing.getProperty(name), null);
			}
			break;
		}
		}
	}

	private Map<Object, Set<IThing>> getValuesToThingsMap(Object propertyName, boolean create){
		synchronized(propertyNamesToValuesToThings){
			Map<Object, Set<IThing>> valuesToThings = propertyNamesToValuesToThings.get(propertyName);
			if(valuesToThings != null){
				return valuesToThings;
			}
		}
		if(create){
			// NOTE: this is not within a synchronized block to prevent deadlock
			IThing[] worldThings = bnaWorld.getBNAModel().getAllThings();
			synchronized(propertyNamesToValuesToThings){
				Map<Object, Set<IThing>> valuesToThings = propertyNamesToValuesToThings.get(propertyName);
				if(valuesToThings == null){
					propertyNamesToValuesToThings.put(propertyName, valuesToThings = new HashMap<Object, Set<IThing>>());
				}
				for(IThing thing: worldThings){
					Object value = thing.getProperty(propertyName);
					if(value != null){
						Set<IThing> things = valuesToThings.get(value);
						if(things == null){
							valuesToThings.put(value, things = new HashSet<IThing>());
						}
						things.add(thing);
					}
				}
				return valuesToThings;
			}
		}
		return null;
	}

	private void update(IThing thing, Object propertyName, Object oldValue, Object newValue){
		Map<Object, Set<IThing>> valueThings = getValuesToThingsMap(propertyName, false);
		if(valueThings != null){
			synchronized(propertyNamesToValuesToThings){
				if(oldValue != null){
					Set<IThing> things = valueThings.get(oldValue);
					if(things != null){
						things.remove(thing);
					}
				}
				if(newValue != null){
					Set<IThing> things = valueThings.get(newValue);
					if(things == null){
						valueThings.put(newValue, things = new HashSet<IThing>());
					}
					things.add(thing);
				}
			}
		}
	}

	public boolean hasThings(Object propertyName, Object value){
		Map<Object, Set<IThing>> valueThings = getValuesToThingsMap(propertyName, false);
		if(valueThings != null){
			synchronized(propertyNamesToValuesToThings){
				Set<IThing> things = valueThings.get(value);
				if(things != null && things.size() > 0){
					return true;
				}
			}
		}
		return false;
	}

	public IThing[] getThings(Object propertyName, Object value){
		Map<Object, Set<IThing>> valueThings = getValuesToThingsMap(propertyName, true);
		synchronized(propertyNamesToValuesToThings){
			Set<IThing> things = valueThings.get(value);
			if(things != null){
				return things.toArray(new IThing[things.size()]);
			}
		}
		return EMPTY_THINGS_ARRAY;
	}

	public IThing getThing(Object propertyName, Object value){
		IThing[] things = getThings(propertyName, value);
		if(things.length > 0){
			return things[0];
		}
		return null;
	}

	public IThing[] getThings(Object propertyName){
		Set<IThing> allThings = new HashSet<IThing>();
		Map<Object, Set<IThing>> valueThings = getValuesToThingsMap(propertyName, true);
		synchronized(propertyNamesToValuesToThings){
			for(Set<IThing> things: valueThings.values()){
				if(things != null){
					allThings.addAll(things);
				}
			}
		}
		return allThings.toArray(new IThing[allThings.size()]);
	}
}
