/*
 * Copyright (c) 2000-2005 Regents of the University of California.
 * All rights reserved.
 *
 * This software was developed at the University of California, Irvine.
 *
 * Redistribution and use in source and binary forms are permitted
 * provided that the above copyright notice and this paragraph are
 * duplicated in all such forms and that any documentation,
 * advertising materials, and other materials related to such
 * distribution and use acknowledge that the software was developed
 * by the University of California, Irvine.  The name of the
 * University may not be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package edu.uci.isr.xarch;

/**
 * An event object that is broadcast to <code>XArchListener</code>s
 * when an xArch element changes.
 * 
 * @author Eric M. Dashofy [edashofy@ics.uci.edu]
 */
public class XArchEvent{

	/** Event type used when an element is set */
	public static final int SET_EVENT = 100;
	/** Event type used when an element or set of elements is cleared. */
	public static final int CLEAR_EVENT = 200;
	/** Event type used when an element is added. */
	public static final int ADD_EVENT = 300;
	/** Event type used when an element is removed. */
	public static final int REMOVE_EVENT = 400;
	/** Event type used when an element is promoted */
	public static final int PROMOTE_EVENT = 500;

	/** Source type used when an attribute changes. */
	public static final int ATTRIBUTE_CHANGED = 1100;
	/** Source type used when an element changes. */
	public static final int ELEMENT_CHANGED = 1200;
	/** Source type used when a simple type's value changes. */
	public static final int SIMPLE_TYPE_VALUE_CHANGED = 1300;
	
	protected IXArchElement src;
	protected int eventType;
	protected int srcType;
	protected String targetName;
	protected boolean isExtraEvent;
	
	/**
	 * This is a string with the value if srcType = ATTRIBUTE_CHANGED,
	 * a string with the value srcType = SIMPLE_TYPE_VALUE_CHANGED,
	 * otherwise it's an IXArchElement if srcType = ELEMENT_CHANGED.
	 */
	protected Object target;
	
	/**
	 * <code>true</code> if the source of the event is attached to the
	 * xArch element in the context, <code>false</code> otherwise.
	 */
	protected boolean isAttached;
	
	/**
	 * Create a new xArch event.
	 *
	 * @param src xArch element that is the source of this event (i.e. that
	 * changed.)
	 * @param eventType One of the event types (above) that indicates what
	 * happened.
	 * @param srcType One of the event types (above) that indicates what
	 * changed.
	 * @param targetName Name of the element or attribute that was 
	 * set/added/cleared/removed.
	 * @param target The attribute/element/value that was 
	 * set/added/cleared/removed.
	 * @param isAttached <code>true</code> if the element that was changed is
	 * actually connected to the xArch element emitting this event or not.
	 */
	public XArchEvent(IXArchElement src, int eventType, int srcType, String targetName,
	Object target, boolean isAttached){
		this.src = src;
		this.eventType = eventType;
		this.srcType = srcType;
		this.targetName = targetName;
		this.target = target;
		this.isAttached = isAttached;
	}

	/**
	 * Create a new xArch event.
	 *
	 * @param src xArch element that is the source of this event (i.e. that
	 * changed.)
	 * @param eventType One of the event types (above) that indicates what
	 * happened.
	 * @param srcType One of the event types (above) that indicates what
	 * changed.
	 * @param targetName Name of the element or attribute that was 
	 * set/added/cleared/removed.
	 * @param target The attribute/element/value that was 
	 * set/added/cleared/removed.
	 * @param isAttached <code>true</code> if the element that was changed is
	 * actually connected to the xArch element emitting this event or not.
	 * @param isExtra <code>true</code> if the value of the element that was 
	 * changed will immediately be changed to a different value.
	 */
	public XArchEvent(IXArchElement src, int eventType, int srcType, String targetName,
	Object target, boolean isAttached, boolean isExtra){
		this.src = src;
		this.eventType = eventType;
		this.srcType = srcType;
		this.targetName = targetName;
		this.target = target;
		this.isAttached = isAttached;
		this.isExtraEvent = isExtra;
	}

	/**
	 * Set the source of this event.
	 * @param src Source of this event.
	 */
	public void setSource(IXArchElement src){
		this.src = src;
	}
	
	/**
	 * Get the source xArch element of this event.
	 * @return source of this event.
	 */
	public IXArchElement getSource(){
		return src;
	}
	
	/**
	 * Set the event type of this event.
	 * @param eventType Event type.
	 */
	public void setEventType(int eventType){
		this.eventType = eventType;
	}
	
	/**
	 * Get the event type of this event.
	 * @return Event type.
	 */
	public int getEventType(){
		return eventType;
	}
	
	/**
	 * Set the source type of this event.
	 * @param srcType Source type.
	 */
	public void setSourceType(int srcType){
		this.srcType = srcType;
	}
	
	/**
	 * Get the source type of this event.
	 * @return Source type.
	 */
	public int getSourceType(){
		return srcType;
	}
	
	/**
	 * Set the target name of this event.
	 * @param targetName Target name.
	 */
	public void setTargetName(String targetName){
		this.targetName = targetName;
	}
	
	/**
	 * Get the target name of this event.
	 * Returns the tag name if the target is an element,
	 * the attribute name if the target is an attribute,
	 * or an undefined value if the target is a simple type.
	 * @return Target name.
	 */
	public String getTargetName(){
		return this.targetName;
	}
	
	/**
	 * Set the target of this event.
	 * @param target Event target.
	 */
	public void setTarget(Object target){
		this.target = target;
	}
	
	/**
	 * Get the target of this event.
	 * @return Event target.
	 */
	public Object getTarget(){
		return target;
	}
	
	/**
	 * Set whether the changed xArch element is currently
	 * attached to (has as an ancestor) the xArch element
	 * that emitted this event.
	 * @param isAttached Whether the changed element is attached or not.
	 */
	public void setIsAttached(boolean isAttached){
		this.isAttached = isAttached;
	}
	
	/**
	 * Determine whether the changed xArch element is currently
	 * attached to (has as an ancestor) the xArch element
	 * that emits this event.
	 * @return <code>true</code> if it is attached, <code>false</code>
	 * otherwise.
	 */
	public boolean getIsAttached(){
		return isAttached;
	}
	
	/**
	 * Determines if this is an extra (unnecessary) event, such as
	 * an event indicating the removal of an element just before it is
	 * replaced. It is extra because this event will be immediately 
	 * followed by the set event for the new value.
	 * @return
	 */
	public boolean isExtra(){
		return isExtraEvent;
	}
}
