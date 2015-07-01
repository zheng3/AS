package edu.uci.isr.bna4.logics.background;

import java.util.concurrent.locks.Lock;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.facets.IHasMutableLife;
import edu.uci.isr.bna4.logics.tracking.TypedThingTrackingLogic;

public class LifeSapperLogic
    extends AbstractThingLogic{

	protected final TypedThingTrackingLogic tttl;
	protected LifeSapper sapper = null;

	public LifeSapperLogic(TypedThingTrackingLogic tttl){
		this.tttl = tttl;
	}

	@Override
	public void init(){
		super.init();
		sapper = new LifeSapper();
		sapper.setName("LifeSapper");
		sapper.setDaemon(true);
		sapper.start();
	}

	@Override
	public void destroy(){
		if(sapper != null){
			sapper.terminate();
			sapper = null;
		}
		super.destroy();
	}

	public class LifeSapper
	    extends Thread{

		protected volatile boolean shouldTerminate = false;

		protected LifeSapper(){
		}

		public synchronized void terminate(){
			shouldTerminate = true;
		}

		@Override
		public void run(){
			while(!shouldTerminate){
				try{
					Thread.sleep(250);
				}
				catch(InterruptedException e){
				}

				IHasMutableLife[] things = tttl.getThings(IHasMutableLife.class);
				if(things.length != 0){
					IBNAModel m = getBNAModel();
					if(m != null){
						m.beginBulkChange();
						try{
							for(IHasMutableLife t: things){
								Lock lock = t.getPropertyLock();
								lock.lock();

								int life;
								try{
									t.setLife(life = t.getLife() - 1);
								}
								finally{
									lock.unlock();
								}
								if(life < 0){
									m.removeThing(t);
								}
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
