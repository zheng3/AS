package edu.uci.isr.bna4;

public class ThingEvent{

	public enum EventType{
		PROPERTY_SET, PROPERTY_REMOVED
	}

	protected EventType eventType;
	protected IThing targetThing;
	protected Object propertyName;
	protected Object oldPropertyValue;
	protected Object newPropertyValue;

	public ThingEvent(EventType eventType, IThing targetThing, Object propertyName, Object oldPropertyValue, Object newPropertyValue){
		this.eventType = eventType;
		this.targetThing = targetThing;
		this.propertyName = propertyName;
		this.oldPropertyValue = oldPropertyValue;
		this.newPropertyValue = newPropertyValue;
	}

	public EventType getEventType(){
		return eventType;
	}

	public IThing getTargetThing(){
		return targetThing;
	}

	public Object getPropertyName(){
		return propertyName;
	}

	public Object getOldPropertyValue(){
		return oldPropertyValue;
	}

	public Object getNewPropertyValue(){
		return newPropertyValue;
	}

	@Override
	public String toString(){
		return "ThingEvent{thingID=" + targetThing.getID() + "; eventType=" + eventType + "; propertyName=" + propertyName + "; oldPropertyValue=" + oldPropertyValue + "; newPropertyValue=" + newPropertyValue + "; targetThing=" + targetThing + "};";
	}
}
