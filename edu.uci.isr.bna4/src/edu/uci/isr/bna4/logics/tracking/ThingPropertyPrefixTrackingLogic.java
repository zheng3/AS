package edu.uci.isr.bna4.logics.tracking;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAModelEvent;
import edu.uci.isr.bna4.IBNASynchronousModelListener;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.sysutils.Tuple;

public class ThingPropertyPrefixTrackingLogic
	extends AbstractThingLogic
	implements IBNASynchronousModelListener{

	protected final ThingPropertyTrackingLogic tptl;
	protected final Set<Object> allPropertyNames = Collections.synchronizedSet(new HashSet<Object>());
	protected final Map<Object, Set<Object>> prefixToPropertyNames = new HashMap<Object, Set<Object>>();

	public ThingPropertyPrefixTrackingLogic(ThingPropertyTrackingLogic tptl){
		this.tptl = tptl;
	}

	@Override
	public void init(){
		super.init();
		for(IThing t: getBNAModel().getAllThings()){
			for(Object propertyName: t.getAllPropertyNames()){
				addPropertyName(propertyName);
			}
		}
	}

	public void bnaModelChangedSync(BNAModelEvent evt){
		switch(evt.getEventType()){
		case THING_ADDED: {
			for(Object propertyName: evt.getTargetThing().getAllPropertyNames()){
				addPropertyName(propertyName);
			}
			break;
		}
		case THING_CHANGED: {
			ThingEvent te = evt.getThingEvent();
			switch(te.getEventType()){
			case PROPERTY_SET:
				addPropertyName(te.getPropertyName());
			}
			break;
		}
		}
	}

	private void addPropertyName(Object propertyName){
		if(allPropertyNames.add(propertyName)){
			if(propertyName instanceof Tuple){
				synchronized(prefixToPropertyNames){
					for(Map.Entry<Object, Set<Object>> entry: prefixToPropertyNames.entrySet()){
						if(((Tuple)propertyName).startsWith(entry.getKey())){
							entry.getValue().add(propertyName);
						}
					}
				}
			}
			else if(propertyName instanceof String){
				synchronized(prefixToPropertyNames){
					for(Map.Entry<Object, Set<Object>> entry: prefixToPropertyNames.entrySet()){
						Object prefix = entry.getKey();
						if(prefix instanceof String){
							if(((String)propertyName).startsWith((String)prefix)){
								entry.getValue().add(propertyName);
							}
						}
					}
				}
			}
		}
	}

	private Set<Object> getPrefixCollection(Object prefix){
		Set<Object> propertyNames = prefixToPropertyNames.get(prefix);
		if(propertyNames == null){
			prefixToPropertyNames.put(prefix, propertyNames = new HashSet<Object>());
			for(Object propertyName: allPropertyNames.toArray()){
				if(propertyName instanceof String && prefix instanceof String){
					if(((String)propertyName).startsWith((String)prefix)){
						propertyNames.add(propertyName);
					}
				}
				else if(propertyName instanceof Tuple){
					if(((Tuple)propertyName).startsWith(prefix)){
						propertyNames.add(propertyName);
					}
				}
			}
		}
		return propertyNames;
	}

	public Object[] getPropertyNamesWithPrefix(Object prefix){
		synchronized(prefixToPropertyNames){
			return getPrefixCollection(prefix).toArray();
		}
	}
}
