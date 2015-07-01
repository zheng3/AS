package edu.uci.isr.archstudio4.comp.archipelago.interactions;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.IArchipelagoTreeDoubleClickHandler;
import edu.uci.isr.archstudio4.comp.archipelago.statecharts.StatechartsEditorSupport;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchPath;

public class InteractionsDoubleClickHandler implements
		IArchipelagoTreeDoubleClickHandler {

	protected ArchipelagoServices AS = null;
	
	public InteractionsDoubleClickHandler(ArchipelagoServices archipelagoServices){
		this.AS = archipelagoServices;
	}
	
	protected boolean isTargetNode(Object ref){
		if(ref instanceof ObjRef){
			XArchPath refPath = AS.xarch.getXArchPath((ObjRef)ref);
			if(refPath.toTagsOnlyString().equals("xArch/interaction")){
				return true;
			}
		}
		return false;
	}
	
	public void treeNodeDoubleClicked(Object treeNode){
		if(isTargetNode(treeNode)){		
			ObjRef interactionRef = (ObjRef)treeNode;
			InteractionsEditorSupport.setupEditor(AS, interactionRef);
		}
	}

}
