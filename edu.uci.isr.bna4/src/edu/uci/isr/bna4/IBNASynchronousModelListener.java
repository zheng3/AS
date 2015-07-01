package edu.uci.isr.bna4;

/**
 * Receives synchronous model events from the thread that produced the event,
 * but after the lock to the model has been released. Most synchronous listeners
 * should implement this interface rather than
 * {@link IBNASynchronousLockModelListener}.
 */
public interface IBNASynchronousModelListener{

	public void bnaModelChangedSync(BNAModelEvent evt);
}
