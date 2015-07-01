package edu.uci.isr.myx.fw;

public class MyxRegistryEvent{

	public enum EventType{
		BrickRegistered, BrickUnregistered, ObjectRegistered, ObjectUnregistered
	}

	EventType eventType;

	IMyxBrick brick;

	Object object;

	public MyxRegistryEvent(EventType eventType, IMyxBrick brick, Object object){
		this.eventType = eventType;
		this.brick = brick;
		this.object = object;
	}

	public EventType getEventType(){
		return eventType;
	}

	public IMyxBrick getBrick(){
		return brick;
	}

	public Object getObject(){
		return object;
	}

	@Override
	public String toString(){
		return "" + eventType + ": Brick:" + brick.getMyxBrickItems().getBrickName() + " Object:" + object;
	}
}
