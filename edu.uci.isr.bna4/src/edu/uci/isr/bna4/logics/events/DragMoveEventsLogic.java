package edu.uci.isr.bna4.logics.events;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAComposite;
import edu.uci.isr.bna4.IBNAMouseListener;
import edu.uci.isr.bna4.IBNAMouseMoveListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.UserEditableUtils;
import edu.uci.isr.bna4.facets.IHasCursor;
import edu.uci.isr.bna4.facets.IRelativeMovable;

public class DragMoveEventsLogic
	extends AbstractThingLogic
	implements IBNAMouseListener, IBNAMouseMoveListener{

	DragMoveEvent currentEvent = null;

	public void mouseDown(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
		if(evt.button == 1 && (evt.stateMask & SWT.MODIFIER_MASK) == 0){
			if(t instanceof IRelativeMovable && UserEditableUtils.isEditableForAllQualities(t, IRelativeMovable.USER_MAY_MOVE)){
				if(!(t instanceof IHasCursor)){
					Object src = evt.getSource();
					if(src != null && src instanceof BNAComposite){
						BNAComposite bnaComposite = (BNAComposite)src;
						bnaComposite.setCursor(evt.display.getSystemCursor(SWT.CURSOR_SIZEALL));
					}
				}
				fireDragStartedEvent(currentEvent = new DragMoveEvent(t, worldX, worldY, view, evt));
			}
		}
	}

	public void mouseMove(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
		if(currentEvent != null){
			fireDragMoveEvent(currentEvent = new DragMoveEvent(currentEvent, view, evt, worldX, worldY));
		}
	}

	public void mouseUp(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
		if(currentEvent != null){
			if(!(currentEvent.getInitialThing() instanceof IHasCursor)){
				Object src = evt.getSource();
				if(src != null && src instanceof BNAComposite){
					BNAComposite bnaComposite = (BNAComposite)src;
					bnaComposite.setCursor(null);
				}
			}
			fireDragFinishedEvent(currentEvent = new DragMoveEvent(currentEvent, view, evt, worldX, worldY));
			currentEvent = null;
		}
	}

	public void mouseClick(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
	}

	public void mouseDoubleClick(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
	}

	protected void fireDragStartedEvent(DragMoveEvent evt){
		for(IDragMoveListener logic: getBNAWorld().getThingLogicManager().getThingLogics(IDragMoveListener.class)){
			logic.dragStarted(evt);
		}
	}

	protected void fireDragMoveEvent(DragMoveEvent evt){
		for(IDragMoveListener logic: getBNAWorld().getThingLogicManager().getThingLogics(IDragMoveListener.class)){
			logic.dragMoved(evt);
		}
	}

	protected void fireDragFinishedEvent(DragMoveEvent evt){
		for(IDragMoveListener logic: getBNAWorld().getThingLogicManager().getThingLogics(IDragMoveListener.class)){
			logic.dragFinished(evt);
		}
	}
}
