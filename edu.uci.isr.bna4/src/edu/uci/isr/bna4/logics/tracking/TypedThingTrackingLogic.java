package edu.uci.isr.bna4.logics.tracking;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAModelEvent;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNASynchronousLockModelListener;
import edu.uci.isr.bna4.IThing;

@SuppressWarnings("unchecked")
public class TypedThingTrackingLogic
	extends AbstractThingLogic
	implements IBNASynchronousLockModelListener{

	private Map<Class, Set> allTypedThings = new HashMap<Class, Set>();

	public TypedThingTrackingLogic(){
	}

	@Override
	public void init(){
		super.init();
		synchronized(allTypedThings){
			allTypedThings.clear();
		}
	}

	@Override
	public void destroy(){
		synchronized(allTypedThings){
			allTypedThings.clear();
		}
		super.destroy();
	}

	public void bnaModelChangedSyncLock(BNAModelEvent evt){
		switch(evt.getEventType()){

		case THING_ADDED: {
			synchronized(allTypedThings){
				IThing t = evt.getTargetThing();
				for(Map.Entry<Class, Set> e: allTypedThings.entrySet()){
					if(e.getKey().isInstance(t)){
						e.getValue().add(t);
					}
				}
			}
		}
			break;

		case THING_REMOVED: {
			synchronized(allTypedThings){
				IThing t = evt.getTargetThing();
				for(Map.Entry<Class, Set> e: allTypedThings.entrySet()){
					if(e.getKey().isInstance(t)){
						e.getValue().remove(t);
					}
				}
			}
		}
			break;
		}
	}

	public <T>T[] getThings(Class<T> ofType){
		synchronized(allTypedThings){
			Set<T> things = allTypedThings.get(ofType);
			if(things == null){
				things = new HashSet<T>();
				IBNAModel model = getBNAModel();
				if(model != null){
					allTypedThings.put(ofType, things);
					for(IThing t: model.getAllThings()){
						if(ofType.isInstance(t)){
							things.add((T)t);
						}
					}
				}
			}
			return things.toArray((T[])Array.newInstance(ofType, things.size()));
		}
	}
}
