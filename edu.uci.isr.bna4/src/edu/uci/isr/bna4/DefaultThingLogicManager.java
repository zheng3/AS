package edu.uci.isr.bna4;

import edu.uci.isr.bna4.ThingLogicManagerEvent.EventType;
import edu.uci.isr.sysutils.ListenerList;
import edu.uci.isr.sysutils.ListenerListsOfType;

class DefaultThingLogicManager
	implements IThingLogicManager{

	private static final boolean DEBUG = false;
	String format = "%-75.75s : %,14d\n";

	protected final IBNAWorld bnaWorld;

	public DefaultThingLogicManager(IBNAWorld bnaWorld){
		this.bnaWorld = bnaWorld;
	}

	protected final ListenerList<IThingLogicManagerListener> listeners = new ListenerList<IThingLogicManagerListener>(ListenerList.IDENTITY, IThingLogicManagerListener.class);

	public void addThingLogicManagerListener(IThingLogicManagerListener l){
		listeners.add(l);
	}

	public void removeThingLogicManagerListener(IThingLogicManagerListener l){
		listeners.remove(l);
	}

	protected void fireThingLogicManagerEvent(EventType eventType, IThingLogic tl){
		if(!listeners.isEmpty()){
			ThingLogicManagerEvent evt = new ThingLogicManagerEvent(eventType, tl);
			for(IThingLogicManagerListener l: listeners.getListeners()){
				l.handleThingLogicManagerEvent(evt);
			}
		}
	}

	protected final ListenerListsOfType<IThingLogic> logics = new ListenerListsOfType<IThingLogic>(ListenerList.IDENTITY, IThingLogic.class);

	public void addThingLogic(IThingLogic tl){
		long time;
		if(DEBUG){
			time = System.nanoTime();
		}
		tl.setBNAWorld(bnaWorld);
		if(!logics.add(tl)){
			throw new IllegalArgumentException("Logic is already added <" + tl + ">");
		}
		if(DEBUG){
			time = System.nanoTime() - time;
			System.err.printf(format, tl, time);
		}
		fireThingLogicManagerEvent(EventType.LOGIC_ADDED, tl);
	}

	public void removeThingLogic(IThingLogic tl){
		fireThingLogicManagerEvent(EventType.LOGIC_REMOVING, tl);
		logics.remove(tl);
		tl.setBNAWorld(null);
		fireThingLogicManagerEvent(EventType.LOGIC_REMOVED, tl);
	}

	public IThingLogic[] getAllThingLogics(){
		return logics.getListeners();
	}

	public <T>T[] getThingLogics(Class<T> implementingInterface){
		return logics.getListeners(implementingInterface);
	}

	public void destroy(){
		// perform remove in reverse order since latter logics often depend on the former logics
		IThingLogic[] logics = this.logics.getListeners();
		for(int i = logics.length - 1; i >= 0; i--){
			removeThingLogic(logics[i]);
		}
	}
}
