package edu.uci.isr.bna4;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.eclipse.swt.graphics.RGB;

import edu.uci.isr.sysutils.ListenerList;
import edu.uci.isr.sysutils.SystemUtils;
import edu.uci.isr.sysutils.UIDGenerator;

public class AbstractThing
    implements IThing{
	
	//varun
	public boolean featureSelected = false;
	public RGB featureColor = null;

	private static final Condition fakeCondition = new Condition(){

		public void await() throws InterruptedException{
		}

		public boolean await(long time, TimeUnit unit) throws InterruptedException{
			return true;
		}

		public long awaitNanos(long nanosTimeout) throws InterruptedException{
			return 0;
		}

		public void awaitUninterruptibly(){
		}

		public boolean awaitUntil(Date deadline) throws InterruptedException{
			return true;
		}

		public void signal(){
		}

		public void signalAll(){
		}
	};

	private static final Lock fakeLock = new Lock(){

		public void lock(){
		}

		public void lockInterruptibly() throws InterruptedException{
		}

		public Condition newCondition(){
			return fakeCondition;
		}

		public boolean tryLock(){
			return true;
		}

		public boolean tryLock(long time, TimeUnit unit) throws InterruptedException{
			return true;
		}

		public void unlock(){
		}
	};

	private Lock lock = fakeLock;

	public Lock getPropertyLock(){
		return lock;
	}

	public void setPropertyLock(Lock lock){
		this.lock = lock != null ? lock : fakeLock;
	}

	private static final boolean equalz(Object o1, Object o2){
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	private static final String simpleName(String className){
		int index = className.lastIndexOf('.');
		if(index >= 0){
			className = className.substring(index + 1);
		}
		return className;
	}

	private final String id;

	private final Map<Object, Object> props = Collections.synchronizedMap(new HashMap<Object, Object>());

	private boolean initedProperties = false;

	public AbstractThing(String id){
		this.id = id == null ? UIDGenerator.generateUID(simpleName(getClass().getName()) + "-") : id;

		initProperties();
		if(!initedProperties){
			throw new RuntimeException("Thing " + this.getClass().getName() + " must call super.initPropeties().");
		}
	}

	protected void initProperties(){
		if(initedProperties){
			throw new RuntimeException("Thing " + this.getClass().getName() + " must only call super.initPropeties() once.");
		}
		initedProperties = true;
	}

	private static final Map<Class<?>, Class<? extends IThingPeer>> peerClassCache = Collections.synchronizedMap(new HashMap<Class<?>, Class<? extends IThingPeer>>());

	@SuppressWarnings({"unchecked"})
	public Class<? extends IThingPeer> getPeerClass(){
		synchronized(peerClassCache){
			Class<?> thingClass = this.getClass();
			Class<? extends IThingPeer> peerClass = peerClassCache.get(thingClass);
			if(peerClass != null){
				return peerClass;
			}

			Class<?> c = thingClass;
			while(c != null){
				try{
					String peerClassName = c.getName() + "Peer";
					peerClass = (Class<? extends IThingPeer>)Class.forName(peerClassName, false, thingClass.getClassLoader());
					peerClassCache.put(thingClass, peerClass);
					return peerClass;
				}
				catch(ClassNotFoundException e){
				}
				c = c.getSuperclass();
			}
			throw new RuntimeException(new ClassNotFoundException("Peer not found for " + this.getClass().getName()));
		}
	}

	public String getID(){
		return id;
	}

	private ListenerList<IThingListener> thingListeners = new ListenerList<IThingListener>(IThingListener.class);

	public void addThingListener(IThingListener thingListener){
		thingListeners.add(thingListener);
	}

	public void removeThingListener(IThingListener thingListener){
		thingListeners.remove(thingListener);
	}

	protected void fireThingEvent(ThingEvent evt){
		if(evt.propertyName instanceof String && ((String)evt.propertyName).startsWith("#")){
			return;
		}
		if(equalz(evt.oldPropertyValue, evt.newPropertyValue)){
			return;
		}
		for(IThingListener listener: thingListeners.getListeners()){
			try{
				listener.thingChanged(evt);
			}
			catch(Throwable t){
				t.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T>T setProperty(Object name, Object value){
		Lock lock = getPropertyLock();
		Object oldValue;

		lock.lock();
		try{
			oldValue = props.put(name, value);
			fireThingEvent(new ThingEvent(ThingEvent.EventType.PROPERTY_SET, this, name, oldValue, value));
			return (T)oldValue;
		}
		finally{
			lock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public <T>T removeProperty(Object name){
		Lock lock = getPropertyLock();
		lock.lock();
		try{
			boolean containedValue = props.containsKey(name);
			Object oldValue = props.remove(name);
			if(containedValue){
				fireThingEvent(new ThingEvent(ThingEvent.EventType.PROPERTY_REMOVED, this, name, oldValue, null));
			}
			return (T)oldValue;
		}
		finally{
			lock.unlock();
		}
	}

	@SuppressWarnings({"unchecked"})
	public <T>T getProperty(Object name){
		return (T)props.get(name);
	}

	public boolean hasProperty(Object name){
		return props.containsKey(name);
	}

	public Map<Object, Object> getPropertyMap(){
		synchronized(props){
			return new HashMap<Object, Object>(props);
		}
	}

	public Collection<Object> getAllPropertyNames(){
		synchronized(props){
			return new HashSet<Object>(props.keySet());
		}
	}

	@Override
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		if(o instanceof IThing){
			IThing ot = (IThing)o;
			if(equalz(id, ot.getID())){
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode(){
		return id.hashCode();
	}

	@Override
	public String toString(){
		synchronized(props){
			StringBuffer sb = new StringBuffer();
			Object[] propertyNames = props.keySet().toArray();
			Arrays.sort(propertyNames, new Comparator<Object>(){

				@SuppressWarnings("unchecked")
				public int compare(Object o1, Object o2){
					if(o1 instanceof Comparable && o1.getClass().equals(o2.getClass())){
						return ((Comparable)o1).compareTo(o2);
					}
					return o1.toString().compareTo(o2.toString());
				}

				@Override
				public boolean equals(Object obj){
					return false;
				}
			});
			for(Object propertyName: propertyNames){
				if(sb.length() > 0){
					sb.append(", ");
				}
				sb.append(propertyName).append('=');
				SystemUtils.toString(props.get(propertyName), sb);
			}
			return "Thing={" + "id=" + id + ", " + sb.toString() + "}";
		}
	}
}
