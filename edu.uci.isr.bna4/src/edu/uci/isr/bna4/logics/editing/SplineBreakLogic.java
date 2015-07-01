package edu.uci.isr.bna4.logics.editing;

import java.awt.geom.Line2D;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAMouseListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.SplineUtils;
import edu.uci.isr.bna4.UserEditableUtils;
import edu.uci.isr.bna4.SplineUtils.SplineData;
import edu.uci.isr.bna4.facets.IHasMutableMidpoints;

public class SplineBreakLogic
	extends AbstractThingLogic
	implements IBNAMouseListener{

	public SplineBreakLogic(){
	}

	public void mouseUp(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
	}

	public void mouseDown(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
	}

	public void mouseClick(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
	}

	public void mouseDoubleClick(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
		if(t instanceof IHasMutableMidpoints && UserEditableUtils.isEditableForAllQualities(t, IHasMutableMidpoints.USER_MAY_ADD_MIDPOINTS)){

			SplineData sd = SplineUtils.getPoints(t);

			// insert the new point
			Point lastPoint = null;
			boolean pointAdded = false;
			for(int i = 0; i < sd.allPoints.size(); i++){
				Point p = sd.allPoints.get(i);
				if(lastPoint != null){
					int dist = BNAUtils.round(Line2D.ptSegDist(p.x, p.y, lastPoint.x, lastPoint.y, worldX, worldY));
					if(dist <= 5){
						pointAdded = true;
						sd.allPoints.add(i, new Point(worldX, worldY));
						break;
					}
				}
				lastPoint = p;
			}

			// if a point wasn't added, do so now
			if(!pointAdded){
				sd.getMidpoints().add(new Point(worldX, worldY));
			}

			SplineUtils.setPoints(t, sd);
		}
	}
}
