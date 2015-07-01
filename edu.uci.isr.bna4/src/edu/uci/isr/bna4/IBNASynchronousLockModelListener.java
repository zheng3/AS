package edu.uci.isr.bna4;

/**
 * Immediately receives synchronous model events from the thread that produced
 * the event, while the lock is still obtained by that thread. Listeners should
 * take care to avoid deadlock. Listeners that need to be immediately aware of
 * changes, such as caches or indexes of the BNA Model would likely implement
 * this interface.
 */
public interface IBNASynchronousLockModelListener{

	public void bnaModelChangedSyncLock(BNAModelEvent evt);
}
