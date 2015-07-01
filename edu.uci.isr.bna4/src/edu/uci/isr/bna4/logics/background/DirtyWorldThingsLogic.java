package edu.uci.isr.bna4.logics.background;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAModelEvent;
import edu.uci.isr.bna4.IBNASynchronousModelListener;
import edu.uci.isr.bna4.IBNAWorld;
import edu.uci.isr.bna4.facets.IHasWorld;
import edu.uci.isr.bna4.logics.events.IInternalWorldEventListener;
import edu.uci.isr.bna4.logics.events.InternalWorldEventsLogic;

public class DirtyWorldThingsLogic
	extends AbstractThingLogic
	implements IInternalWorldEventListener{

	protected static int InternalModelChangeCount = 0;
	protected final InternalWorldEventsLogic iwel;

	public DirtyWorldThingsLogic(InternalWorldEventsLogic iwel){
		this.iwel = iwel;
	}

	private class BumpOuterViewCounter
		extends AbstractThingLogic
		implements IBNASynchronousModelListener{

		public void bnaModelChangedSync(BNAModelEvent evt){
			for(IHasWorld worldThing: iwel.getWorldThings(getBNAWorld())){
				worldThing.setProperty("InternalModelChangedTicker", ++InternalModelChangeCount);
			}
		}
	}

	public void innerWorldAdded(IBNAWorld world){
		world.getThingLogicManager().addThingLogic(new BumpOuterViewCounter());
	}

	public void innerWorldRemoved(IBNAWorld world){
	}
}
