package edu.uci.isr.bna4.logics.editing;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.IBNAMenuListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.UserEditableUtils;
import edu.uci.isr.bna4.assemblies.AssemblyUtils;
import edu.uci.isr.bna4.assemblies.IAssembly;
import edu.uci.isr.bna4.logics.coordinating.MaintainTagsLogic;

public class ShowHideTagsLogic
	extends AbstractThingLogic
	implements IBNAMenuListener{

	public void fillMenu(IBNAView view, IMenuManager m, int localX, int localY, IThing t, int worldX, int worldY){
		IAssembly assembly = AssemblyUtils.getAssemblyWithPart(t);
		if(assembly != null){
			List<IThing> taggableThings = new ArrayList<IThing>();
			for(IThing pt: assembly.getParts()){
				if(UserEditableUtils.hasAnyEditableQualities(pt, MaintainTagsLogic.USER_MAY_SHOW_TAG)){
					taggableThings.add(pt);
				}
			}

			if(!taggableThings.isEmpty()){
				for(final IThing tt: taggableThings){
					final boolean isShown = Boolean.TRUE.equals(tt.getProperty(MaintainTagsLogic.SHOW_TAG_PROPERTY_NAME));
					IAction tagAction = new Action("Show Tag"){

						@Override
						public void run(){
							tt.setProperty(MaintainTagsLogic.SHOW_TAG_PROPERTY_NAME, !isShown);
						}
					};
					tagAction.setChecked(isShown);
					m.add(tagAction);
				}
				m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
			}
		}
	}
}
