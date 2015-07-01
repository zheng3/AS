package edu.uci.isr.bna4.logics.events;

import edu.uci.isr.bna4.IBNAWorld;

public interface IInternalWorldEventListener{

	public void innerWorldAdded(IBNAWorld world);

	public void innerWorldRemoved(IBNAWorld world);
}
