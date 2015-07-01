package edu.uci.isr.myx.fw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uci.isr.myx.fw.MyxRegistryEvent.EventType;

public class MyxRegistry{

	protected Map<IMyxBrick, List<Object>> brickToObjectMap = Collections.synchronizedMap(new HashMap<IMyxBrick, List<Object>>());

	private MyxRegistry(){
		// to prevent instantiation other than from getSharedInstance()
	}
	
	public synchronized void register(IMyxBrick b){
		Object o = brickToObjectMap.get(b);
		if(o != null){
			throw new IllegalArgumentException("Aleady registered this Myx brick in the Myx Registry");
		}
		brickToObjectMap.put(b, new ArrayList<Object>());
		fireMyxRegistryEvent(MyxRegistryEvent.EventType.BrickRegistered, b, null);
		notifyAll();
	}

	public synchronized void unregister(IMyxBrick b){
		brickToObjectMap.remove(b);
		fireMyxRegistryEvent(MyxRegistryEvent.EventType.BrickUnregistered, b, null);
		notifyAll();
	}

	public synchronized <T extends IMyxBrick>T getBrick(IMyxName name){
		for(IMyxBrick b: brickToObjectMap.keySet())
			if(name.equals(MyxUtils.getName(b)))
				return (T)b;
		return null;
	}

	public synchronized <T extends IMyxBrick>T waitForBrick(IMyxName name){
		IMyxBrick b = null;
		while((b = getBrick(name)) == null){
			try{
				wait();
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		return (T)b;
	}

	public synchronized <T extends IMyxBrick>T getBrick(Class brickClass){
		for(IMyxBrick b: brickToObjectMap.keySet())
			if(brickClass.equals(b.getClass()))
				return (T)b;
		return null;
	}

	public synchronized <T extends IMyxBrick>T waitForBrick(Class brickClass){
		IMyxBrick b = null;
		while((b = getBrick(brickClass)) == null){
			try{
				wait();
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		return (T)b;
	}

	public synchronized void map(IMyxBrick b, Object o){
		List<Object> l = brickToObjectMap.get(b);
		if(l == null){
			throw new IllegalArgumentException("No such brick registered: " + MyxUtils.getName(b));
		}
		if(l.contains(o)){
			throw new IllegalArgumentException("Object " + o + " is already registered with that brick.");
		}
		l.add(o);
		fireMyxRegistryEvent(MyxRegistryEvent.EventType.ObjectUnregistered, b, o);
		notifyAll();
	}

	public synchronized void unmap(IMyxBrick b, Object o){
		List<Object> l = brickToObjectMap.get(b);
		if(l == null){
			throw new IllegalArgumentException("No such brick registered: " + MyxUtils.getName(b));
		}
		if(!l.contains(o)){
			throw new IllegalArgumentException("Object " + o + " is not registered with that brick.");
		}
		if(l.remove(o)){
			fireMyxRegistryEvent(MyxRegistryEvent.EventType.ObjectUnregistered, b, o);
		}
		notifyAll();
	}

	public synchronized Object[] getObjects(IMyxBrick b){
		List<Object> l = brickToObjectMap.get(b);
		if(l == null){
			// throw new IllegalArgumentException("No such brick registered: " +
			// MyxUtils.getName(b));
			return new Object[0];
		}
		return l.toArray(new Object[l.size()]);
	}

	List<MyxRegistryListener> listeners = Collections.synchronizedList(new ArrayList<MyxRegistryListener>());

	public synchronized void addMyxRegistryListener(MyxRegistryListener l){
		listeners.add(l);
	}

	public synchronized void removeMyxRegistryListener(MyxRegistryListener l){
		listeners.remove(l);
	}

	public synchronized void fireMyxRegistryEvent(EventType eventType, IMyxBrick brick, Object object){
		MyxRegistryEvent evt = new MyxRegistryEvent(eventType, brick, object);
		for(MyxRegistryListener l: (MyxRegistryListener[])listeners.toArray(new MyxRegistryListener[listeners.size()])){
			l.handleMyxRegistryEvent(evt);
		}
	}

	protected static MyxRegistry sharedInstance = new MyxRegistry();

	public static MyxRegistry getSharedInstance(){
		return sharedInstance;
	}
}
