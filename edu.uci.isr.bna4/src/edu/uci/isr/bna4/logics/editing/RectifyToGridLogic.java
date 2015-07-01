package edu.uci.isr.bna4.logics.editing;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.GridUtils;
import edu.uci.isr.bna4.IBNAMenuListener;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;

public class RectifyToGridLogic extends AbstractThingLogic implements IBNAMenuListener{
	
	public void fillMenu(IBNAView view, IMenuManager m, int localX, int localY, IThing t, int worldX, int worldY){
		if(t == null){
			final IBNAModel model = view.getWorld().getBNAModel();
			if(GridUtils.getGridSpacing(model) != 0){
				IAction rectifyAction = new Action("Rectify Diagram to Grid"){
					public void run(){
						GridUtils.rectifyToGrid(model);
					}
				};
				m.add(rectifyAction);
				m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
			}
		}
	}
}
