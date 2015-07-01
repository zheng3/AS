package edu.uci.isr.bna4.logics.editing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAModelEvent;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAMenuListener;
import edu.uci.isr.bna4.IBNAModelListener;
import edu.uci.isr.bna4.IBNAMouseListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.UserEditableUtils;
import edu.uci.isr.bna4.assemblies.AssemblyUtils;
import edu.uci.isr.bna4.assemblies.IAssembly;
import edu.uci.isr.bna4.constants.CompletionStatus;
import edu.uci.isr.bna4.facets.IHasMutableText;
import edu.uci.isr.bna4.facets.IHasText;
import edu.uci.isr.bna4.logics.coordinating.MoveWithLogic;
import edu.uci.isr.bna4.logics.information.ToolTipLogic;
import edu.uci.isr.bna4.things.swt.SWTTextThing;

public class EditTextLogic
	extends AbstractThingLogic
	implements IBNAMenuListener, IBNAModelListener, IBNAMouseListener{

	public static class DescriptionContext{

		public String descriptionName;

		public DescriptionContext(String descriptionName){
			this.descriptionName = descriptionName;
		}
	}

	protected List<SWTTextThing> openControls = Collections.synchronizedList(new ArrayList<SWTTextThing>());

	public EditTextLogic(){
	}

	public void fillMenu(final IBNAView view, IMenuManager m, int localX, int localY, final IThing t, final int worldX, final int worldY){
		IThing editThing = null;
		if(BNAUtils.getSelectedThings(view.getWorld().getBNAModel()).length <= 1){
			IAssembly assembly = AssemblyUtils.getAssemblyWithPart(t);
			if(assembly != null){
				for(IThing partThing: assembly.getParts()){
					if(UserEditableUtils.isEditableForAnyQualities(partThing, IHasMutableText.USER_MAY_EDIT_TEXT, ToolTipLogic.USER_MAY_EDIT_TOOL_TIP)){
						editThing = partThing;
						break;
					}
				}
			}
		}
		final IThing fThing = editThing;
		if(fThing != null){
			m.add(new Action("Edit Description..."){

				@Override
				public void run(){
					Point p = BNAUtils.getCentralPoint(t);
					if(p == null){
						p = new Point(worldX, worldY);
					}
					SWTTextThing tt = new SWTTextThing();
					tt.setProperty("#targetThingID", fThing.getID());
					tt.setText(fThing instanceof IHasText ? ((IHasText)fThing).getText() : ToolTipLogic.getToolTip(fThing));
					tt.setAnchorPoint(p);
					MoveWithLogic.moveWith(t, MoveWithLogic.TRACK_ANCHOR_POINT_FIRST, tt);
					tt.setEditing(true);
					openControls.add(tt);
					view.getWorld().getBNAModel().addThing(tt, t);
				}
			});
		}
	}

	public void bnaModelChanged(BNAModelEvent evt){
		if(evt.getEventType() == BNAModelEvent.EventType.THING_CHANGED){
			if(evt.getTargetThing() instanceof SWTTextThing){
				SWTTextThing tt = (SWTTextThing)evt.getTargetThing();
				if(openControls.contains(tt)){
					if(tt.getCompletionStatus() == CompletionStatus.OK){
						IThing t = evt.getSource().getThing((String)tt.getProperty("#targetThingID"));
						if(t instanceof IHasMutableText && UserEditableUtils.isEditableForAnyQualities(t, IHasMutableText.USER_MAY_EDIT_TEXT)){
							((IHasMutableText)t).setText(tt.getText());
						}
						else if(t != null && UserEditableUtils.isEditableForAnyQualities(t, ToolTipLogic.USER_MAY_EDIT_TOOL_TIP)){
							ToolTipLogic.setToolTip(t, tt.getText());
						}
					}
					if(tt.getCompletionStatus() != CompletionStatus.INCOMPLETE){
						evt.getSource().removeThing(tt);
						openControls.remove(tt);
					}
				}
			}
		}
	}

	public void mouseUp(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
	}

	public void mouseDown(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
		if(openControls.size() > 0){
			SWTTextThing[] oc = openControls.toArray(new SWTTextThing[openControls.size()]);
			for(SWTTextThing tt: oc){
				tt.setCompletionStatus(CompletionStatus.OK);
				tt.setEditing(false);
			}
		}
	}

	public void mouseClick(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
	}

	public void mouseDoubleClick(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
	}
}
