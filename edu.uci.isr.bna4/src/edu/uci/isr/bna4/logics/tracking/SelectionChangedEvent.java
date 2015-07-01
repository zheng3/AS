package edu.uci.isr.bna4.logics.tracking;

import edu.uci.isr.bna4.IThing;

public class SelectionChangedEvent{

	public static enum EventType{
		THING_SELECTED, THING_DESELECTED
	}

	protected SelectionTrackingLogic source;
	protected EventType eventType;
	protected IThing targetThing;

	public SelectionChangedEvent(SelectionTrackingLogic source, EventType eventType, IThing targetThing){
		this.source = source;
		this.eventType = eventType;
		this.targetThing = targetThing;
	}

	public SelectionTrackingLogic getSource(){
		return source;
	}

	public EventType getEventType(){
		return eventType;
	}

	public IThing getTargetThing(){
		return targetThing;
	}

}
