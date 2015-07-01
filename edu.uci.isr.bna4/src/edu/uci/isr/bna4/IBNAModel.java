package edu.uci.isr.bna4;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;

public interface IBNAModel{
	
	public String getSelectedFeature();
	public void setSelectedFeature(String feature);

	public IThing[] getAllThings();

	public IThing[] getChildThings(IThing parent);

	public void removeThing(String id);

	public IThing getThing(String id);

	public void removeThing(IThing t);

	public void removeThingAndChildren(IThing t);

	public void addThing(IThing t);

	public void addThing(IThing t, IThing parentThing);

	public int getNumThings();

	public IThing getParentThing(IThing t);

	public IThing[] getAncestorThings(IThing t);

	public Iterator<IThing> getThingIterator();

	public ListIterator<IThing> getThingListIterator(int index);

	public void stackAbove(IThing upperThing, IThing lowerThing);

	public void bringToFront(IThing thing);

	public void sendToBack(IThing thing);

	public void fireStreamNotificationEvent(String streamNotification);

	/**
	 * Callers should invoke this when they are about to start a bulk change to
	 * the model, that is, many changes to the model in quick succession. This
	 * generally results in a <code>BULK_CHANGE_BEGIN</code> event being
	 * emitted from the model and can provide listeners with a hint to hold
	 * normally per-event processing (like repaints) until the end of the bulk
	 * change. This method is optional; it need not be implemented by models.
	 * Note that each call to beginBulkChange MUST be accompanied by a paired
	 * <code>endBulkChange()</code> call.
	 */
	public void beginBulkChange();

	/**
	 * Callers should invoke this when they have completed a bulk change to the
	 * model, that is, many changes to the model in quick succession. This
	 * generally results in a <code>BULK_CHANGE_END</code> event being emitted
	 * from the model and can provide listeners with a hint to hold normally
	 * per-event processing (like repaints) until the end of the bulk change.
	 * This method is optional; it need not be implemented by models.
	 */
	public void endBulkChange();

	//public Object getLock();

	public void addBNAModelListener(IBNAModelListener l);

	public void removeBNAModelListener(IBNAModelListener l);

	public void addSynchronousBNAModelListener(IBNASynchronousModelListener l);

	public void removeSynchronousBNAModelListener(IBNASynchronousModelListener l);

	public void addSynchronousLockedBNAModelListener(IBNASynchronousLockModelListener l);

	public void removeSynchronousLockedBNAModelListener(IBNASynchronousLockModelListener l);

	public Lock getLock();
}
