package edu.uci.isr.bna4.logics;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAModelEvent;
import edu.uci.isr.bna4.IBNASynchronousModelListener;

public class DebugLogic
	extends AbstractThingLogic
	implements IBNASynchronousModelListener{

	@Override
	public void init(){
		super.init();
		System.err.println("DL: Initialized");
	}

	@Override
	public void destroy(){
		System.err.println("DL: Destroyed");
		super.destroy();
	}

	int eventCount = 0;

	public void bnaModelChangedSync(BNAModelEvent evt){
		eventCount++;
		System.err.println("DL: BNAModelEvent " + eventCount + " " + evt);
	}
}
