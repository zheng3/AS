package edu.uci.isr.bna4.logics.editing;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAModelEvent;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNASynchronousModelListener;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.UserEditableUtils;
import edu.uci.isr.bna4.assemblies.BoxReshapeHandlesAssembly;
import edu.uci.isr.bna4.assemblies.ReshapeHandleAssembly;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutableBoundingBox;
import edu.uci.isr.bna4.facets.IHasSelected;
import edu.uci.isr.bna4.logics.events.DragMoveEvent;
import edu.uci.isr.bna4.logics.events.DragMoveEventsLogic;
import edu.uci.isr.bna4.logics.events.IDragMoveListener;
import edu.uci.isr.bna4.logics.tracking.ISelectionTrackingListener;
import edu.uci.isr.bna4.logics.tracking.SelectionChangedEvent;
import edu.uci.isr.bna4.logics.tracking.SelectionTrackingLogic;
import edu.uci.isr.bna4.things.glass.ReshapeHandleGlassThing;
import edu.uci.isr.widgets.swt.constants.Orientation;

public class BoxReshapeHandleLogic
	extends AbstractThingLogic
	implements IBNASynchronousModelListener, ISelectionTrackingListener,
	IDragMoveListener{

	protected SelectionTrackingLogic stl = null;

	protected IHasMutableBoundingBox targetThing = null;

	protected BoxReshapeHandlesAssembly reshapeHandles = null;

	public BoxReshapeHandleLogic(SelectionTrackingLogic stl, DragMoveEventsLogic dml){
		this.stl = stl;
		// dml: this logic listens to dml events, this ensures that the designer remembers to include it
	}

	@Override
	public void init(){
		stl.addSelectionTrackingListener(this);
		checkHandles();
	}

	@Override
	public void destroy(){
		removeHandles();
		stl.removeSelectionTrackingListener(this);
	}

	public void bnaModelChangedSync(BNAModelEvent evt){
		switch(evt.getEventType()){
		case THING_REMOVING:
			if(evt.getTargetThing().equals(targetThing)){
				removeHandles();
			}
			break;
		case THING_CHANGED:
			if(evt.getTargetThing().equals(targetThing) && IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME.equals(evt.getThingEvent().getPropertyName())){
				updateHandlePositions();
			}
			break;
		}
	}

	protected synchronized void removeHandles(){
		if(reshapeHandles == null){
			return;
		}

		IBNAModel m = getBNAModel();
		if(m != null){
			IThing handleAssemblyRoot = reshapeHandles.getRootThing();
			m.removeThingAndChildren(handleAssemblyRoot);
			targetThing = null;
			reshapeHandles = null;
		}
	}

	protected synchronized void updateHandlePositions(){
		IBNAModel m = getBNAModel();
		if(m != null){
			m.beginBulkChange();
			try{
				if(reshapeHandles != null && targetThing != null){
					Rectangle bb = targetThing.getBoundingBox();
					if(bb != null){
						ReshapeHandleAssembly rh;
						rh = reshapeHandles.getReshapeHandleAssembly(Orientation.NORTHWEST);
						rh.getReshapeHandleGlassThing().setAnchorPoint(new Point(bb.x, bb.y));

						rh = reshapeHandles.getReshapeHandleAssembly(Orientation.NORTH);
						rh.getReshapeHandleGlassThing().setAnchorPoint(new Point(bb.x + bb.width / 2, bb.y));

						rh = reshapeHandles.getReshapeHandleAssembly(Orientation.NORTHEAST);
						rh.getReshapeHandleGlassThing().setAnchorPoint(new Point(bb.x + bb.width, bb.y));

						rh = reshapeHandles.getReshapeHandleAssembly(Orientation.EAST);
						rh.getReshapeHandleGlassThing().setAnchorPoint(new Point(bb.x + bb.width, bb.y + bb.height / 2));

						rh = reshapeHandles.getReshapeHandleAssembly(Orientation.SOUTHEAST);
						rh.getReshapeHandleGlassThing().setAnchorPoint(new Point(bb.x + bb.width, bb.y + bb.height));

						rh = reshapeHandles.getReshapeHandleAssembly(Orientation.SOUTH);
						rh.getReshapeHandleGlassThing().setAnchorPoint(new Point(bb.x + bb.width / 2, bb.y + bb.height));

						rh = reshapeHandles.getReshapeHandleAssembly(Orientation.SOUTHWEST);
						rh.getReshapeHandleGlassThing().setAnchorPoint(new Point(bb.x, bb.y + bb.height));

						rh = reshapeHandles.getReshapeHandleAssembly(Orientation.WEST);
						rh.getReshapeHandleGlassThing().setAnchorPoint(new Point(bb.x, bb.y + bb.height / 2));
					}
				}
			}
			finally{
				m.endBulkChange();
			}
		}
	}

	protected synchronized void addHandles(IHasMutableBoundingBox t){
		IBNAModel m = getBNAModel();
		if(m != null){
			Rectangle bb = t.getBoundingBox();
			if(bb != null){
				targetThing = t;
				reshapeHandles = new BoxReshapeHandlesAssembly(m, null, null);
				for(ReshapeHandleAssembly rh: reshapeHandles.getAllReshapeHandleAssemblies()){
					rh.getReshapeHandleThing().setColor(new RGB(0, 0, 255));
					rh.getReshapeHandleGlassThing().setTargetThingID(targetThing.getID());
				}
				updateHandlePositions();
			}
		}
	}

	protected synchronized void checkHandles(){
		IHasSelected[] selectedThings = stl.getSelectedThings();
		// Show reshape handles whenever one thing is selected.
		if(selectedThings.length != 1){
			removeHandles();
			return;
		}
		else{
			IHasSelected selectedThing = selectedThings[0];
			if(selectedThing.equals(targetThing)){
				// It's fine, do nothing.
				return;
			}
			if(targetThing != null && !targetThing.equals(selectedThing)){
				// The selection has changed to a different thing
				removeHandles();
			}
			if(selectedThing instanceof IHasMutableBoundingBox){
				if(UserEditableUtils.isEditableForAllQualities(selectedThing, IHasMutableBoundingBox.USER_MAY_RESIZE)){
					addHandles((IHasMutableBoundingBox)selectedThing);
				}
			}
		}
	}

	public synchronized void selectionChanged(SelectionChangedEvent evt){
		checkHandles();
	}

	public void dragStarted(DragMoveEvent evt){
	}

	public void dragMoved(DragMoveEvent evt){
		if(targetThing != null){
			IThing movedThing = evt.getInitialThing();
			if(movedThing instanceof ReshapeHandleGlassThing){
				ReshapeHandleGlassThing rht = (ReshapeHandleGlassThing)movedThing;
				String rhtTargetThingID = rht.getTargetThingID();
				if(rhtTargetThingID != null && rhtTargetThingID.equals(targetThing.getID())){
					Orientation o = rht.getOrientation();
					Rectangle bb = targetThing.getBoundingBox();

					int x1 = bb.x;
					int y1 = bb.y;
					int x2 = bb.x + bb.width;
					int y2 = bb.y + bb.height;

					int nx1 = x1;
					int ny1 = y1;
					int nx2 = x2;
					int ny2 = y2;

					Point ap = evt.getAdjustedWorldPoint();

					switch(o){
					case NORTHWEST:
						nx1 = ap.x;
						ny1 = ap.y;
						break;
					case NORTH:
						ny1 = ap.y;
						break;
					case NORTHEAST:
						nx2 = ap.x;
						ny1 = ap.y;
						break;
					case EAST:
						nx2 = ap.x;
						break;
					case SOUTHEAST:
						nx2 = ap.x;
						ny2 = ap.y;
						break;
					case SOUTH:
						ny2 = ap.y;
						break;
					case SOUTHWEST:
						nx1 = ap.x;
						ny2 = ap.y;
						break;
					case WEST:
						nx1 = ap.x;
						break;
					}
					bb.x = nx1;
					bb.y = ny1;
					bb.width = nx2 - nx1;
					bb.height = ny2 - ny1;
					targetThing.setBoundingBox(bb);

					// This code is here to keep the anchor
					// points on the sides of the box.
					updateHandlePositions();
				}
			}
		}
	}

	public void dragFinished(DragMoveEvent evt){
	}
}
