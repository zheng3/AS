package edu.uci.isr.bna4;

import java.lang.ref.WeakReference;

public class WeakThingListener implements IThingListener{

	protected IThing t = null;
	protected WeakReference<IThingListener> listenerRef = null;
	
	public WeakThingListener(IThing t, IThingListener realListener){
		this.t = t;
		this.listenerRef = new WeakReference<IThingListener>(realListener);
	}
	
	public void thingChanged(ThingEvent thingEvent){
		IThingListener realListener = listenerRef.get();
		if(realListener == null){
			t.removeThingListener(this);
		}
		else{
			realListener.thingChanged(thingEvent);
		}
	}
}
