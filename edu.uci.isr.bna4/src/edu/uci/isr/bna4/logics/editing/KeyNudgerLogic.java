package edu.uci.isr.bna4.logics.editing;

import java.util.concurrent.locks.Lock;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.GridUtils;
import edu.uci.isr.bna4.IBNAKeyListener;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.facets.IRelativeMovable;
import edu.uci.isr.widgets.swt.constants.Orientation;

public class KeyNudgerLogic
	extends AbstractThingLogic
	implements IBNAKeyListener{

	public void keyPressed(IBNAView view, KeyEvent e){
	}

	public void keyReleased(IBNAView view, KeyEvent e){
		if(e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_RIGHT){
			IThing[] selectedThings = BNAUtils.getSelectedThings(view.getWorld().getBNAModel());
			Orientation o = orientationForKeyCode(e.keyCode);
			int gridSpacing = GridUtils.getGridSpacing(view.getWorld().getBNAModel());
			int distance = gridSpacing == 0 ? 5 : gridSpacing;

			IBNAModel model = getBNAModel();
			if(model != null && selectedThings.length > 0){
				model.beginBulkChange();
				try{
					for(IThing t: selectedThings){
						if(t instanceof IRelativeMovable){
							nudge(o, distance, (IRelativeMovable)t);
							//if(gridSpacing != 0){
							//	GridUtils.rectifyToGrid(gridSpacing, (IRelativeMovable)t);
							//}
						}
					}
				}
				finally{
					model.endBulkChange();
				}
			}
		}
	}

	private Orientation orientationForKeyCode(int keyCode){
		switch(keyCode){
		case SWT.ARROW_LEFT:
			return Orientation.WEST;
		case SWT.ARROW_UP:
			return Orientation.NORTH;
		case SWT.ARROW_RIGHT:
			return Orientation.EAST;
		case SWT.ARROW_DOWN:
			return Orientation.SOUTH;
		default:
			throw new IllegalArgumentException("Invalid key code");
		}
	}

	private Point nudge(Orientation o, int distance, Point p){
		Point p2 = new Point(p.x, p.y);
		switch(o){
		case NORTH:
			p2.y -= distance;
			break;
		case EAST:
			p2.x += distance;
			break;
		case SOUTH:
			p2.y += distance;
			break;
		case WEST:
			p2.x -= distance;
			break;
		}
		return p2;
	}

	protected void nudge(Orientation o, int distance, IRelativeMovable t){
		Lock lock = t.getPropertyLock();
		lock.lock();
		try{
			Point p = t.getReferencePoint();
			t.setReferencePoint(nudge(o, distance, p));
		}
		finally{
			lock.unlock();
		}
	}
}
