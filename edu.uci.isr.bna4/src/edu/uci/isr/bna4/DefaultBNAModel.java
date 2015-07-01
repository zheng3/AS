package edu.uci.isr.bna4;

import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import edu.uci.isr.bna4.BNAModelEvent.EventType;
import edu.uci.isr.sysutils.IEventListener;
import edu.uci.isr.sysutils.ListenerList;
import edu.uci.isr.sysutils.ThreadEventsLock;

public class DefaultBNAModel
	implements IBNAModel, IThingListener, IEventListener<BNAModelEvent>{

	private static final boolean DEBUG = false;
	private static final String format = "%-75s : %,14d\n";

	static class ThingIndex
		implements IBNASynchronousLockModelListener{

		private final Map<String, IThing> indexMap = new WeakHashMap<String, IThing>();

		public synchronized void bnaModelChangedSyncLock(BNAModelEvent evt){
			if(evt.getEventType().equals(EventType.THING_ADDED)){
				indexMap.put(evt.targetThing.getID(), evt.targetThing);
			}
			else if(evt.getEventType().equals(EventType.THING_REMOVED)){
				indexMap.remove(evt.targetThing.getID());
			}
		}

		public synchronized IThing getThing(String id){
			return indexMap.get(id);
		}
	}

	protected final ThingTree thingTree = new ThingTree();
	protected final ThingIndex thingIndex = new ThingIndex();
	protected final ThreadEventsLock<BNAModelEvent> modelEventLock = new ThreadEventsLock<BNAModelEvent>();
	protected final ListenerList<IBNASynchronousLockModelListener> syncLockListeners = new ListenerList<IBNASynchronousLockModelListener>(IBNASynchronousLockModelListener.class);
	protected final ListenerList<IBNASynchronousModelListener> syncListeners = new ListenerList<IBNASynchronousModelListener>(IBNASynchronousModelListener.class);
	protected final ListenerList<IBNAModelListener> asyncListeners = new ListenerList<IBNAModelListener>(IBNAModelListener.class);
	protected final ExecutorService asyncExecutor;

	//varun
	private String selectedFeature;
	
	public DefaultBNAModel(){
		syncLockListeners.add(thingIndex);
		modelEventLock.addEventListener(this);
		asyncExecutor = Executors.newSingleThreadExecutor();
	}

	public void terminate(boolean t){
		try{
			asyncExecutor.awaitTermination(5, TimeUnit.SECONDS);
		}
		catch(InterruptedException ie){
		}
	}

	public Lock getLock(){
		return modelEventLock;
	}

	public void addBNAModelListener(IBNAModelListener l){
		asyncListeners.add(l);
	}

	public void removeBNAModelListener(IBNAModelListener l){
		asyncListeners.remove(l);
	}

	public void addSynchronousBNAModelListener(IBNASynchronousModelListener l){
		syncListeners.add(l);
	}

	public void removeSynchronousBNAModelListener(IBNASynchronousModelListener l){
		syncListeners.remove(l);
	}

	public void addSynchronousLockedBNAModelListener(IBNASynchronousLockModelListener l){
		syncLockListeners.add(l);
	}

	public void removeSynchronousLockedBNAModelListener(IBNASynchronousLockModelListener l){
		syncLockListeners.remove(l);
	}

	public void handleEvent(final BNAModelEvent evt){
		for(IBNASynchronousModelListener l: syncListeners.getListeners()){
			try{
				long lTime;
				if(DEBUG){
					lTime = System.nanoTime();
				}
				l.bnaModelChangedSync(evt);
				if(DEBUG){
					lTime = System.nanoTime() - lTime;
					System.err.printf(format, "S:" + l, lTime);
				}
			}
			catch(Throwable t){
				t.printStackTrace();
			}
		}

		if(evt.eventType == BNAModelEvent.EventType.THING_REMOVING){
			_removeThing(evt.targetThing);
		}

		final IBNAModelListener[] listenersAtTimeOfEvent = asyncListeners.getListeners();
		asyncExecutor.submit(new Runnable(){

			public void run(){
				for(IBNAModelListener l: listenersAtTimeOfEvent){
					try{
						long lTime;
						if(DEBUG){
							lTime = System.nanoTime();
						}
						l.bnaModelChanged(evt);
						if(DEBUG){
							lTime = System.nanoTime() - lTime;
							System.err.printf(format, "A:" + l, lTime);
						}
					}
					catch(Throwable t){
						t.printStackTrace();
					}
				}
			}
		});
	}

	protected void fireBNAModelEvent(BNAModelEvent evt){
		assert modelEventLock.isHeldByCurrentThread();

		for(IBNASynchronousLockModelListener l: syncLockListeners.getListeners()){
			try{
				long lTime;
				if(DEBUG){
					lTime = System.nanoTime();
				}
				l.bnaModelChangedSyncLock(evt);
				if(DEBUG){
					lTime = System.nanoTime() - lTime;
					System.err.printf(format, "L:" + l, lTime);
				}
			}
			catch(Throwable t){
				t.printStackTrace();
			}
		}
		modelEventLock.enqueueEvent(evt);
	}

	protected void fireBNAModelEvent(BNAModelEvent.EventType eventType, IThing targetThing){
		fireBNAModelEvent(new BNAModelEvent(this, eventType, targetThing));
	}

	protected void fireBNAModelEvent(BNAModelEvent.EventType eventType, IThing targetThing, ThingEvent thingEvent){
		fireBNAModelEvent(new BNAModelEvent(this, eventType, targetThing, thingEvent));
	}

	public void fireStreamNotificationEvent(String streamNotificationEvent){
		modelEventLock.lock();
		try{
			fireBNAModelEvent(new BNAModelEvent(this, streamNotificationEvent));
		}
		finally{
			modelEventLock.unlock();
		}
	}

	public void thingChanged(ThingEvent evt){
		fireBNAModelEvent(BNAModelEvent.EventType.THING_CHANGED, evt.getTargetThing(), evt);
	}

	private int bulkChangeCount = 0;

	public void beginBulkChange(){
		modelEventLock.lock();
		try{
			if(bulkChangeCount++ == 0){
				fireBNAModelEvent(BNAModelEvent.EventType.BULK_CHANGE_BEGIN, null);
			}
		}
		finally{
			modelEventLock.unlock();
		}
	}

	public void endBulkChange(){
		modelEventLock.lock();
		try{
			if(--bulkChangeCount <= 0){
				bulkChangeCount = 0;
				fireBNAModelEvent(BNAModelEvent.EventType.BULK_CHANGE_END, null);
			}
		}
		finally{
			modelEventLock.unlock();
		}
	}

	public void addThing(IThing t){
		modelEventLock.lock();
		try{
			t.addThingListener(this);
			t.setPropertyLock(modelEventLock);
			synchronized(thingTree){
				thingTree.add(t);
			}
			fireBNAModelEvent(BNAModelEvent.EventType.THING_ADDED, t);
		}
		finally{
			modelEventLock.unlock();
		}
	}

	public void addThing(IThing t, IThing parentThing){
		modelEventLock.lock();
		try{
			t.addThingListener(this);
			t.setPropertyLock(modelEventLock);
			synchronized(thingTree){
				thingTree.add(t, parentThing);
			}
			fireBNAModelEvent(BNAModelEvent.EventType.THING_ADDED, t);
		}
		finally{
			modelEventLock.unlock();
		}
	}

	public void removeThing(String id){
		modelEventLock.lock();
		try{
			IThing t = getThing(id);
			if(t != null){
				synchronized(thingTree){
					if(!thingTree.contains(t)){
						return;
					}
				}
				fireBNAModelEvent(BNAModelEvent.EventType.THING_REMOVING, t);
			}
		}
		finally{
			modelEventLock.unlock();
		}
	}

	public void removeThing(IThing t){
		modelEventLock.lock();
		try{
			synchronized(thingTree){
				if(!thingTree.contains(t)){
					return;
				}
			}
			fireBNAModelEvent(BNAModelEvent.EventType.THING_REMOVING, t);
		}
		finally{
			modelEventLock.unlock();
		}
	}

	public void removeThingAndChildren(IThing t){
		modelEventLock.lock();
		try{
			_removeThingAndChildren(t);
		}
		finally{
			modelEventLock.unlock();
		}
	}

	private void _removeThing(IThing t){
		modelEventLock.lock();
		try{
			t.removeThingListener(this);
			t.setPropertyLock(null);
			synchronized(thingTree){
				thingTree.remove(t);
			}
			fireBNAModelEvent(BNAModelEvent.EventType.THING_REMOVED, t);
		}
		finally{
			modelEventLock.unlock();
		}
	}

	private void _removeThingAndChildren(IThing t){
		assert modelEventLock.isHeldByCurrentThread();

		if(thingTree.contains(t)){
			for(IThing c: getChildThings(t)){
				_removeThingAndChildren(c);
			}
			fireBNAModelEvent(BNAModelEvent.EventType.THING_REMOVING, t);
		}
	}

	public IThing getThing(String id){
		return thingIndex.getThing(id);
	}

	public Iterator<IThing> getThingIterator(){
		synchronized(thingTree){
			return Arrays.asList(thingTree.getAllThings()).iterator();
		}
	}

	public ListIterator<IThing> getThingListIterator(int index){
		synchronized(thingTree){
			return Arrays.asList(thingTree.getAllThings()).listIterator(index);
		}
	}

	public int getNumThings(){
		synchronized(thingTree){
			return thingTree.size();
		}
	}

	public IThing[] getAllThings(){
		synchronized(thingTree){
			return thingTree.getAllThings();
		}
	}

	public IThing[] getChildThings(IThing t){
		synchronized(thingTree){
			return thingTree.getChildren(t);
		}
	}

	public IThing getParentThing(IThing t){
		synchronized(thingTree){
			return thingTree.getParent(t);
		}
	}

	public IThing[] getAncestorThings(IThing t){
		synchronized(thingTree){
			return thingTree.getAncestors(t);
		}
	}
	
	public void stackAbove(IThing upperThing, IThing lowerThing){
		modelEventLock.lock();
		try{
			IThing[] movedThings;
			synchronized(thingTree){
				thingTree.moveAfter(lowerThing, upperThing);
				movedThings = thingTree.getAllChildren(lowerThing);
			}
			fireBNAModelEvent(BNAModelEvent.EventType.THING_RESTACKED, lowerThing);
			for(IThing mt: movedThings){
				fireBNAModelEvent(BNAModelEvent.EventType.THING_RESTACKED, mt);
			}
		}
		finally{
			modelEventLock.unlock();
		}
	}

	public void bringToFront(IThing thing){
		modelEventLock.lock();
		try{
			IThing[] movedThings;
			synchronized(thingTree){
				thingTree.bringToFront(thing);
				movedThings = thingTree.getAllChildren(thing);
			}
			fireBNAModelEvent(BNAModelEvent.EventType.THING_RESTACKED, thing);
			for(IThing mt: movedThings){
				fireBNAModelEvent(BNAModelEvent.EventType.THING_RESTACKED, mt);
			}
		}
		finally{
			modelEventLock.unlock();
		}
	}

	public void sendToBack(IThing thing){
		modelEventLock.lock();
		try{
			IThing[] movedThings;
			synchronized(thingTree){
				thingTree.sendToBack(thing);
				movedThings = thingTree.getAllChildren(thing);
			}
			fireBNAModelEvent(BNAModelEvent.EventType.THING_RESTACKED, thing);
			for(IThing mt: movedThings){
				fireBNAModelEvent(BNAModelEvent.EventType.THING_RESTACKED, mt);
			}
		}
		finally{
			modelEventLock.unlock();
		}
	}

	@Override
	public String getSelectedFeature() {
		return selectedFeature;
	}

	@Override
	public void setSelectedFeature(String feature) {
		this.selectedFeature = feature;
		
	}
}
