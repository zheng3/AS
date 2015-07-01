package edu.uci.isr.bna4.logics.editing;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.UserEditableUtils;
import edu.uci.isr.bna4.facets.IHasSelected;
import edu.uci.isr.bna4.facets.IRelativeMovable;
import edu.uci.isr.bna4.logics.events.DragMoveEvent;
import edu.uci.isr.bna4.logics.events.DragMoveEventsLogic;
import edu.uci.isr.bna4.logics.events.IDragMoveListener;
import edu.uci.isr.bna4.logics.tracking.SelectionTrackingLogic;

public class DragMovableLogic
	extends AbstractThingLogic
	implements IDragMoveListener{

	protected SelectionTrackingLogic stl = null;

	public DragMovableLogic(DragMoveEventsLogic dml, SelectionTrackingLogic stl){
		// dml: this logic listens to dml events, this ensures that the designer remembers to include it
		this.stl = stl;
	}

	Map<IRelativeMovable, Point> initialReferencePoints = new HashMap<IRelativeMovable, Point>();
	int initialAdjustedWorldX = 0;
	int initialAdjustedWorldY = 0;

	public void dragStarted(DragMoveEvent evt){
		initialReferencePoints.clear();
		initialAdjustedWorldX = evt.getAdjustedWorldX();
		initialAdjustedWorldY = evt.getAdjustedWorldY();

		IThing movedThing = evt.getInitialThing();
		if(movedThing instanceof IHasSelected){
			if(((IHasSelected)movedThing).isSelected()){
				for(IHasSelected t: stl.getSelectedThings()){
					if(t instanceof IRelativeMovable && UserEditableUtils.isEditableForAllQualities(t, IRelativeMovable.USER_MAY_MOVE)){
						Lock lock = t.getPropertyLock();
						lock.lock();
						try{
							IRelativeMovable rmt = (IRelativeMovable)t;
							initialReferencePoints.put(rmt, rmt.getReferencePoint());
						}
						finally{
							lock.unlock();
						}
					}
				}
			}
		}
		if(movedThing instanceof IRelativeMovable && UserEditableUtils.isEditableForAllQualities(movedThing, IRelativeMovable.USER_MAY_MOVE)){
			Lock lock = movedThing.getPropertyLock();
			lock.lock();
			try{
				IRelativeMovable rmt = (IRelativeMovable)movedThing;
				initialReferencePoints.put(rmt, rmt.getReferencePoint());
			}
			finally{
				lock.unlock();
			}
		}
	}

	public void dragMoved(DragMoveEvent evt){
		IBNAModel model = getBNAModel();
		if(model != null){
			model.beginBulkChange();
			try{
				for(Map.Entry<IRelativeMovable, Point> entry: initialReferencePoints.entrySet()){
					IRelativeMovable rmt = entry.getKey();
					Point initialReferencePoint = entry.getValue();
					Lock lock = rmt.getPropertyLock();
					lock.lock();
					try{
						rmt.setReferencePoint(new Point(initialReferencePoint.x + evt.getAdjustedWorldX() - initialAdjustedWorldX, initialReferencePoint.y + evt.getAdjustedWorldY() - initialAdjustedWorldY));
					}
					finally{
						lock.unlock();
					}
				}
			}
			finally{
				model.endBulkChange();
			}
		}
	}

	public void dragFinished(DragMoveEvent evt){
	}
}
