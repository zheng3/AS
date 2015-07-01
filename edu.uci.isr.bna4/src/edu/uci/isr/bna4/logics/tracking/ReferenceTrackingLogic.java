package edu.uci.isr.bna4.logics.tracking;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAModelEvent;
import edu.uci.isr.bna4.IBNASynchronousModelListener;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.sysutils.Tuple;

/**
 * This logic keeps track of things referencing other things by their id.
 * Referencing things have a property name starting with "&" and a value with
 * the referenced thing's id.
 */
public class ReferenceTrackingLogic
	extends AbstractThingLogic
	implements IBNASynchronousModelListener{

	private static final String[] EMPTY_STRING_ARRAY = new String[0];

	private ReadWriteLock lock = new ReentrantReadWriteLock();

	// referenced-id --> byPropertyName --> ids-referencing
	private Map<String, Map<Object, Collection<String>>> references = Collections.synchronizedMap(new HashMap<String, Map<Object, Collection<String>>>());

	public ReferenceTrackingLogic(){
	}

	@Override
	public void init(){
		super.init();
		for(IThing t: getBNAModel().getAllThings()){
			addReferences(t);
		}
	}

	protected void updateReference(IThing sourceThing, Object propertyName, Object oldTargetID, Object newTargetID){
		if((oldTargetID == null || oldTargetID instanceof String) && (newTargetID == null || newTargetID instanceof String)){
			lock.writeLock().lock();
			try{
				if(oldTargetID instanceof String){
					Map<Object, Collection<String>> namedReferences = references.get(oldTargetID);
					if(namedReferences != null){
						Collection<String> thingReferences = namedReferences.get(propertyName);
						if(thingReferences != null){
							if(thingReferences.remove(sourceThing.getID())){
								if(thingReferences.size() == 0){
									namedReferences.remove(propertyName);
									if(namedReferences.size() == 0){
										references.remove(oldTargetID);
									}
								}
							}
						}
					}
				}
				if(newTargetID instanceof String){
					Map<Object, Collection<String>> namedReferences = references.get(newTargetID);
					if(namedReferences == null){
						references.put((String)newTargetID, namedReferences = new HashMap<Object, Collection<String>>());
					}
					Collection<String> thingReferences = namedReferences.get(propertyName);
					if(thingReferences == null){
						namedReferences.put(propertyName, thingReferences = new HashSet<String>());
					}
					thingReferences.add(sourceThing.getID());
				}
			}
			finally{
				lock.writeLock().unlock();
			}
		}
	}

	protected void updateReference(ThingEvent e){
		Object propertyName = e.getPropertyName();
		if(propertyName instanceof String && ((String)propertyName).startsWith("&")){
			updateReference(e.getTargetThing(), propertyName, e.getOldPropertyValue(), e.getNewPropertyValue());
		}
		else if(propertyName instanceof Tuple){
			Tuple tPropertyName = (Tuple)propertyName;
			if(tPropertyName.getElementCount() > 0){
				Object tPropertyNameE0 = tPropertyName.getElement(0);
				if(tPropertyNameE0 instanceof String && ((String)tPropertyNameE0).startsWith("&")){
					updateReference(e.getTargetThing(), propertyName, e.getOldPropertyValue(), e.getNewPropertyValue());
				}
			}
		}
	}

	protected void addReferences(IThing t){
		for(Entry<Object, Object> property: t.getPropertyMap().entrySet()){
			Object propertyName = property.getKey();
			if(propertyName instanceof String && ((String)propertyName).startsWith("&")){
				updateReference(t, propertyName, null, property.getValue());
			}
			else if(propertyName instanceof Tuple){
				Tuple tPropertyName = (Tuple)propertyName;
				if(tPropertyName.getElementCount() > 0){
					Object tPropertyNameE0 = tPropertyName.getElement(0);
					if(tPropertyNameE0 instanceof String && ((String)tPropertyNameE0).startsWith("&")){
						updateReference(t, propertyName, null, property.getValue());
					}
				}
			}
		}
	}

	protected void removeReferences(IThing t){
		for(Entry<Object, Object> property: t.getPropertyMap().entrySet()){
			Object propertyName = property.getKey();
			if(propertyName instanceof String && ((String)propertyName).startsWith("&")){
				updateReference(t, propertyName, property.getValue(), null);
			}
			else if(propertyName instanceof Tuple){
				Tuple tPropertyName = (Tuple)propertyName;
				if(tPropertyName.getElementCount() > 0){
					Object tPropertyNameE0 = tPropertyName.getElement(0);
					if(tPropertyNameE0 instanceof String && ((String)tPropertyNameE0).startsWith("&")){
						updateReference(t, propertyName, null, property.getValue());
					}
				}
			}
		}
	}

	public void bnaModelChangedSync(BNAModelEvent evt){
		switch(evt.getEventType()){
		case THING_ADDED:
			addReferences(evt.getTargetThing());
			break;
		case THING_REMOVING:
			removeReferences(evt.getTargetThing());
			break;
		case THING_CHANGED:
			updateReference(evt.getThingEvent());
			break;
		}
	}

	public boolean hasThingsReferencing(String targetID){
		lock.readLock().lock();
		try{
			return references.get(targetID) != null;
		}
		finally{
			lock.readLock().unlock();
		}
	}

	public boolean hasThingsReferencing(String targetID, Object byPropertyName){
		lock.readLock().lock();
		try{
			Map<Object, Collection<String>> namedReferences = references.get(targetID);
			if(namedReferences != null){
				return namedReferences.containsKey(byPropertyName);
			}
			return false;
		}
		finally{
			lock.readLock().unlock();
		}
	}

	public Map<Object, String[]> getThingsReferencing(String targetID){
		lock.readLock().lock();
		try{
			Map<Object, Collection<String>> namedReferences = references.get(targetID);
			if(namedReferences != null){
				Map<Object, String[]> namedReferencesClone = new HashMap<Object, String[]>();
				for(Entry<Object, Collection<String>> entry: namedReferences.entrySet()){
					Collection<String> namedReference = entry.getValue();
					if(!namedReference.isEmpty()){
						namedReferencesClone.put(entry.getKey(), namedReference.toArray(new String[namedReference.size()]));
					}
				}
				return namedReferencesClone;
			}
			return Collections.emptyMap();
		}
		finally{
			lock.readLock().unlock();
		}
	}

	public Map<Object, String[]> getThingsReferencing(String targetID, Object[] byPropertyNames){
		lock.readLock().lock();
		try{
			Map<Object, Collection<String>> namedReferences = references.get(targetID);
			if(namedReferences != null){
				Map<Object, String[]> namedReferencesClone = new HashMap<Object, String[]>();
				for(Object byPropertyName: byPropertyNames){
					Collection<String> namedReference = namedReferences.get(byPropertyName);
					if(namedReference != null && !namedReference.isEmpty()){
						namedReferencesClone.put(byPropertyName, namedReference.toArray(new String[namedReference.size()]));
					}
				}
				return namedReferencesClone;
			}
			return Collections.emptyMap();
		}
		finally{
			lock.readLock().unlock();
		}
	}

	public Map<Object, String[]> getThingsReferencing(Object[] byPropertyNames){
		lock.readLock().lock();
		try{
			Map<Object, String[]> namedReferencesClone = new HashMap<Object, String[]>();
			for(Object byPropertyName: byPropertyNames){
				String[] references = getThingsReferencing(byPropertyName);
				if(references.length > 0){
					namedReferencesClone.put(byPropertyName, references);
				}
			}
			return namedReferencesClone;
		}
		finally{
			lock.readLock().unlock();
		}
	}

	public String[] getThingsReferencing(String targetID, Object byPropertyName){
		lock.readLock().lock();
		try{
			Map<Object, Collection<String>> namedReferences = references.get(targetID);
			if(namedReferences != null){
				Collection<String> namedReference = namedReferences.get(byPropertyName);
				if(namedReference != null){
					return namedReference.toArray(new String[namedReference.size()]);
				}
			}
			return EMPTY_STRING_ARRAY;
		}
		finally{
			lock.readLock().unlock();
		}
	}

	public String[] getThingsReferencing(Object byPropertyName){
		lock.readLock().lock();
		try{
			Set<String> namedReferencesClone = new HashSet<String>();
			for(Map.Entry<String, Map<Object, Collection<String>>> reference: references.entrySet()){
				Collection<String> namedReferences = reference.getValue().get(byPropertyName);
				if(namedReferences != null && !namedReferences.isEmpty()){
					namedReferencesClone.addAll(namedReferences);
				}
			}
			return namedReferencesClone.toArray(new String[namedReferencesClone.size()]);
		}
		finally{
			lock.readLock().unlock();
		}
	}
}
