package edu.uci.isr.bna4.logics.editing;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.IBNAMenuListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.facets.IHasAnchorPoint;
import edu.uci.isr.bna4.facets.IHasAngle;
import edu.uci.isr.bna4.logics.coordinating.MirrorValueLogic;
import edu.uci.isr.bna4.things.labels.TagThing;
import edu.uci.isr.bna4.things.utility.RotaterThing;

public class RotateTagsLogic
	extends AbstractThingLogic
	implements IBNAMenuListener{

	public void fillMenu(final IBNAView view, IMenuManager m, int localX, int localY, final IThing t, int worldX, int worldY){
		if(t instanceof TagThing){
			IAction rotateAction = new Action("Rotate Tag"){

				@Override
				public void run(){
					TagThing tt = (TagThing)t;
					RotaterThing rt = new RotaterThing();
					rt.setAngle(tt.getAngle());

					MirrorValueLogic.mirrorValue(tt, IHasAnchorPoint.ANCHOR_POINT_PROPERTY_NAME, rt);
					MirrorValueLogic.mirrorValue(rt, IHasAngle.ANGLE_PROPERTY_NAME, tt);
					view.getWorld().getBNAModel().addThing(rt);
				}
			};
			m.add(rotateAction);
			m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		}
	}
}
