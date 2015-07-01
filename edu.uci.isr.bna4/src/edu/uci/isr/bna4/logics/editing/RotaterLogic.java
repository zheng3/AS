package edu.uci.isr.bna4.logics.editing;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAMouseListener;
import edu.uci.isr.bna4.IBNAMouseMoveListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.things.utility.RotaterThing;

public class RotaterLogic
	extends AbstractThingLogic
	implements IBNAMouseListener, IBNAMouseMoveListener{

	protected RotaterThing rt;
	protected boolean pressed = false;

	public void mouseDown(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
		if(evt.button == 1){
			if(t instanceof RotaterThing){
				pressed = true;
				rt = (RotaterThing)t;
			}
		}
	}

	public void mouseUp(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
		pressed = false;
		rt = null;
	}

	public void mouseMove(IBNAView view, MouseEvent e, IThing t, int worldX, int worldY){
		if(pressed){
			Point anchorPointWorld = rt.getAnchorPoint();

			int rwx = anchorPointWorld.x;
			int rwy = anchorPointWorld.y;

			int dx = worldX - rwx;
			int dy = worldY - rwy;

			double angleInRadians = Math.atan((double)dy / (double)dx);
			double angleInDegrees = angleInRadians * 180 / Math.PI;
			if(dx < 0){
				angleInDegrees = (angleInDegrees + 180) % 360;
			}
			//double rvsAngleInDegrees = 360 - angleInDegrees;
			int intAngle = BNAUtils.round(angleInDegrees);
			int increment = rt.getAdjustmentIncrement();
			if(increment > 1){
				while(intAngle % increment != 0){
					intAngle = (intAngle + 1) % 360;
				}
			}
			rt.setAngle(intAngle);
		}
	}

	public void mouseClick(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
	}

	public void mouseDoubleClick(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
		if(evt.button == 1){
			if(t instanceof RotaterThing){
				view.getWorld().getBNAModel().removeThing(t);
			}
		}
	}
}
