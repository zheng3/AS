package edu.uci.isr.bna4.logics.editing;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.GridUtils;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.facets.IRelativeMovable;
import edu.uci.isr.bna4.logics.events.DragMoveEvent;
import edu.uci.isr.bna4.logics.events.DragMoveEventsLogic;
import edu.uci.isr.bna4.logics.events.IDragMoveListener;

public class SnapToGridLogic
	extends AbstractThingLogic
	implements IDragMoveListener{

	public SnapToGridLogic(DragMoveEventsLogic dml){
		// dml: this logic listens to dml events, this ensures that the designer remembers to include it
	}

	int initialAdjustedOffsetX = 0;
	int initialAdjustedOffsetY = 0;

	public void dragStarted(DragMoveEvent evt){
		initialAdjustedOffsetX = 0;
		initialAdjustedOffsetY = 0;
		IBNAModel model = getBNAModel();
		if(model != null){
			int gridSpacing = GridUtils.getGridSpacing(model);
			if(gridSpacing != 0){
				Point initialAdjustedPoint = evt.getAdjustedWorldPoint();
				Point snappedAdjustedPoint = GridUtils.snapToGrid(gridSpacing, initialAdjustedPoint);
				initialAdjustedOffsetX = snappedAdjustedPoint.x - initialAdjustedPoint.x;
				initialAdjustedOffsetY = snappedAdjustedPoint.y - initialAdjustedPoint.y;

				// adjust the point so that it is on the grid
				evt.setAdjustedWorldPoint(GridUtils.snapToGrid(gridSpacing, evt.getAdjustedWorldPoint()));

				// we also include the delta of the snapped reference point
				// the first time only in order to initially move it to the 
				// snapped postiion
				IThing initialThing = evt.getInitialThing();
				if(initialThing instanceof IRelativeMovable){
					Point referencePoint = ((IRelativeMovable)initialThing).getReferencePoint();
					Point snappedReferencePoint = GridUtils.snapToGrid(gridSpacing, referencePoint);
					evt.setAdjustedWorldX(evt.getAdjustedWorldX() - (snappedReferencePoint.x - referencePoint.x));
					evt.setAdjustedWorldY(evt.getAdjustedWorldY() - (snappedReferencePoint.y - referencePoint.y));
				}
			}
		}
	}

	public void dragMoved(DragMoveEvent evt){
		Point adjustedWorldPoint = evt.getAdjustedWorldPoint();
		adjustedWorldPoint = new Point(adjustedWorldPoint.x + initialAdjustedOffsetX, adjustedWorldPoint.y + initialAdjustedOffsetY);
		if((evt.getEvt().stateMask & SWT.MOD3) == 0){
			IBNAModel model = getBNAModel();
			if(model != null){
				int gridSpacing = GridUtils.getGridSpacing(model);
				if(gridSpacing != 0){
					// adjust the point so that it is on the grid
					adjustedWorldPoint = GridUtils.snapToGrid(gridSpacing, adjustedWorldPoint);
				}
			}
		}
		evt.setAdjustedWorldPoint(adjustedWorldPoint);
	}

	public void dragFinished(DragMoveEvent evt){
	}
}
