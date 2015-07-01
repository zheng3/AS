package edu.uci.isr.bna4.logics.editing;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.events.MouseEvent;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAMenuListener;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNAMouseListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.UserEditableUtils;
import edu.uci.isr.bna4.facets.IHasMutableSelected;
import edu.uci.isr.bna4.logics.tracking.TypedThingTrackingLogic;

public class ClickSelectionLogic
	extends AbstractThingLogic
	implements IBNAMouseListener, IBNAMenuListener{

	protected TypedThingTrackingLogic tttl = null;

	public ClickSelectionLogic(TypedThingTrackingLogic ttstlSelected){
		this.tttl = ttstlSelected;
	}

	private void removeAllSelections(){
		IBNAModel model = getBNAModel();
		if(model != null){
			IHasMutableSelected[] allSelectableThings = tttl.getThings(IHasMutableSelected.class);
			if(allSelectableThings.length > 0){
				model.beginBulkChange();
				try{
					for(IHasMutableSelected possiblySelectedThing: allSelectableThings){
						possiblySelectedThing.setSelected(false);
					}
				}
				finally{
					model.endBulkChange();
				}
			}
		}
	}

	public void mouseUp(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
	}

	public void mouseDown(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
		if(evt.button == 1){
			if(t != null && t instanceof IHasMutableSelected){
				if(UserEditableUtils.isEditableForAllQualities(t, IHasMutableSelected.USER_MAY_SELECT)){
					IHasMutableSelected mst = (IHasMutableSelected)t;
					boolean controlPressed = BNAUtils.wasControlPressed(evt);
					boolean shiftPressed = BNAUtils.wasShiftPressed(evt);

					if(!controlPressed && !shiftPressed){
						//Only deselect everything if the thing we're clicking on is not selected
						if(!mst.isSelected()){
							removeAllSelections();
						}
						mst.setSelected(true);
					}
					else if(controlPressed && !shiftPressed){
						//Toggle selection
						mst.setSelected(!mst.isSelected());
					}
					else if(shiftPressed && !controlPressed){
						//Add to selection
						mst.setSelected(true);
					}
					else if(shiftPressed && controlPressed){
						//Subtract from selection
						mst.setSelected(false);
					}
				}
			}
			else if(t == null){
				boolean controlPressed = BNAUtils.wasControlPressed(evt);
				boolean shiftPressed = BNAUtils.wasShiftPressed(evt);
				if(!controlPressed && !shiftPressed){
					removeAllSelections();
				}
			}
		}
	}

	public void mouseClick(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
	}

	public void mouseDoubleClick(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
	}

	public void fillMenu(IBNAView view, IMenuManager m, int localX, int localY, IThing t, int worldX, int worldY){
		//We don't actually want to fill the menu here, but we want to change
		//the selection before the menu really gets filled to reflect the
		//click.  If we clicked on something already selected, we leave
		//the selection alone.  If we click on something not selected, but
		//selectable, we change the selection to be that thing.  If we click
		//on something not selectable, then we clear the selection.
		if(t == null){
			removeAllSelections();
		}
		else if(t instanceof IHasMutableSelected){
			IHasMutableSelected st = (IHasMutableSelected)t;
			if(!st.isSelected()){
				removeAllSelections();
				if(UserEditableUtils.isEditableForAllQualities(t, IHasMutableSelected.USER_MAY_SELECT)){
					st.setSelected(true);
				}
			}
		}
		else{
			removeAllSelections();
		}
	}
}
