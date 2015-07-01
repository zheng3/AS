package edu.uci.isr.bna4.logics.background;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.facets.IHasMutableRotatingOffset;
import edu.uci.isr.bna4.logics.tracking.TypedThingTrackingLogic;

public class RotatingOffsetLogic
    extends AbstractThingLogic{

	protected final TypedThingTrackingLogic tttl;
	protected RotatingOffsetIncrementer timer = null;

	public RotatingOffsetLogic(TypedThingTrackingLogic tttl){
		this.tttl = tttl;
	}

	@Override
	public void init(){
		super.init();
		timer = new RotatingOffsetIncrementer();
		timer.setName("RotatingOffsetIncrementer");
		timer.setDaemon(true);
		timer.start();
	}

	@Override
	public void destroy(){
		if(timer != null){
			timer.terminate();
			timer = null;
		}
		super.destroy();
	}

	public class RotatingOffsetIncrementer
	    extends Thread{

		protected volatile boolean shouldTerminate = false;

		protected RotatingOffsetIncrementer(){
		}

		public synchronized void terminate(){
			shouldTerminate = true;
		}

		@Override
		public void run(){
			while(!shouldTerminate){
				try{
					Thread.sleep(800);
				}
				catch(InterruptedException e){
				}

				IHasMutableRotatingOffset[] things = tttl.getThings(IHasMutableRotatingOffset.class);
				if(things.length != 0){
					IBNAModel m = getBNAModel();
					if(m != null){
						m.beginBulkChange();
						try{
							for(IHasMutableRotatingOffset t: things){
								t.incrementRotatingOffset();
							}
						}
						finally{
							m.endBulkChange();
						}
					}
				}
			}
		}
	}
}
