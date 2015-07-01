package edu.uci.isr.bna4;

public class DefaultBNAWorld
	implements IBNAWorld, IBNAModelListener, IBNASynchronousModelListener, IBNASynchronousLockModelListener{

	private static final boolean DEBUG = false;
	private static final String format = "%-75s : %,14d\n";

	protected String id = null;
	protected IBNAModel model = null;
	protected boolean isDestroyed = false;

	protected transient IThingLogicManager logicManager = null;

	public DefaultBNAWorld(String id, IBNAModel model){
		this.id = id;
		this.model = model;
		this.logicManager = new DefaultThingLogicManager(this);

		getBNAModel().addSynchronousLockedBNAModelListener(this);
		getBNAModel().addSynchronousBNAModelListener(this);
		getBNAModel().addBNAModelListener(this);
	}

	public void bnaModelChanged(BNAModelEvent evt){
		for(IBNAModelListener logic: logicManager.getThingLogics(IBNAModelListener.class)){
			long lTime;
			if(DEBUG){
				lTime = System.nanoTime();
			}
			logic.bnaModelChanged(evt);
			if(DEBUG){
				lTime = System.nanoTime() - lTime;
				System.err.printf(format, "A:" + logic, lTime);
			}
		}
	}

	public void bnaModelChangedSync(BNAModelEvent evt){
		for(IBNASynchronousModelListener logic: logicManager.getThingLogics(IBNASynchronousModelListener.class)){
			long lTime;
			if(DEBUG){
				lTime = System.nanoTime();
			}
			logic.bnaModelChangedSync(evt);
			if(DEBUG){
				lTime = System.nanoTime() - lTime;
				System.err.printf(format, "S:" + logic, lTime);
			}
		}
	}

	public void bnaModelChangedSyncLock(BNAModelEvent evt){
		for(IBNASynchronousLockModelListener logic: logicManager.getThingLogics(IBNASynchronousLockModelListener.class)){
			long lTime;
			if(DEBUG){
				lTime = System.nanoTime();
			}
			logic.bnaModelChangedSyncLock(evt);
			if(DEBUG){
				lTime = System.nanoTime() - lTime;
				System.err.printf(format, "L:" + logic, lTime);
			}
		}
	}

	public IThingLogicManager getThingLogicManager(){
		return logicManager;
	}

	public void destroy(){
		logicManager.destroy();

		model.removeBNAModelListener(this);
		model.removeSynchronousBNAModelListener(this);
		model.removeSynchronousLockedBNAModelListener(this);

		isDestroyed = true;
	}

	public boolean isDestroyed(){
		return isDestroyed;
	}

	public String getID(){
		return id;
	}

	public IBNAModel getBNAModel(){
		return model;
	}

}
