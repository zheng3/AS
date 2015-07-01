package edu.uci.isr.bna4.logics.tracking;

import java.util.HashSet;
import java.util.Set;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAModelEvent;
import edu.uci.isr.bna4.IBNASynchronousModelListener;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.facets.IHasSelected;
import edu.uci.isr.bna4.logics.tracking.SelectionChangedEvent.EventType;
import edu.uci.isr.sysutils.ListenerList;

public class SelectionTrackingLogic
	extends AbstractThingLogic
	implements IBNASynchronousModelListener{

	protected Set<IThing> selectedThings = new HashSet<IThing>();
	protected IHasSelected[] selectedThingsArray = null;

	@Override
	public void init(){
		for(IThing t: getBNAModel().getAllThings()){
			if(t instanceof IHasSelected){
				IHasSelected st = (IHasSelected)t;
				if(st.isSelected()){
					synchronized(selectedThings){
						selectedThings.add(st);
						selectedThingsArray = null;
					}
					fireSelectionChangedEvent(EventType.THING_SELECTED, st);
				}
			}
		}
	}

	public void bnaModelChangedSync(BNAModelEvent evt){
		switch(evt.getEventType()){
		case THING_ADDED:
		case THING_CHANGED: {
			IThing t = evt.getTargetThing();
			if(t instanceof IHasSelected){
				IHasSelected st = (IHasSelected)t;
				if(st.isSelected()){
					boolean fireEvent = false;
					synchronized(selectedThings){
						if(selectedThings.add(st)){
							fireEvent = true;
							selectedThingsArray = null;
						}
					}
					if(fireEvent){
						fireSelectionChangedEvent(EventType.THING_SELECTED, st);
					}
				}
				else{
					boolean fireEvent = false;
					synchronized(selectedThings){
						if(selectedThings.remove(st)){
							fireEvent = true;
							selectedThingsArray = null;
						}
					}
					if(fireEvent){
						fireSelectionChangedEvent(EventType.THING_DESELECTED, st);
					}
				}
			}
		}
			break;
		case THING_REMOVED:
			boolean fireEvent = false;
			synchronized(selectedThings){
				if(selectedThings.remove(evt.getTargetThing())){
					fireEvent = true;
					selectedThingsArray = null;
				}
			}
			if(fireEvent){
				fireSelectionChangedEvent(EventType.THING_DESELECTED, evt.getTargetThing());
			}
		}
	}

	public IHasSelected[] getSelectedThings(){
		synchronized(selectedThings){
			if(selectedThingsArray == null){
				selectedThingsArray = selectedThings.toArray(new IHasSelected[selectedThings.size()]);
			}
			return selectedThingsArray;
		}
	}

	protected ListenerList<ISelectionTrackingListener> selectionTrackingListeners = new ListenerList<ISelectionTrackingListener>(ISelectionTrackingListener.class);

	public void addSelectionTrackingListener(ISelectionTrackingListener l){
		selectionTrackingListeners.add(l);
	}

	public void removeSelectionTrackingListener(ISelectionTrackingListener l){
		selectionTrackingListeners.remove(l);
	}

	protected void fireSelectionChangedEvent(EventType eventType, IThing targetThing){
		SelectionChangedEvent evt = new SelectionChangedEvent(this, eventType, targetThing);
		for(ISelectionTrackingListener l: selectionTrackingListeners.getListeners()){
			l.selectionChanged(evt);
		}
	}
}
