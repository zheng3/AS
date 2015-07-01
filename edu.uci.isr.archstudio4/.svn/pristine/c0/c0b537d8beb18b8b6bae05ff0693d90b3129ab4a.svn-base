package edu.uci.isr.archstudio4.comp.archipelago.interactions;

import org.eclipse.jface.viewers.TreeViewer;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.util.AbstractEditDescriptionContextMenuFiller;
import edu.uci.isr.xarchflat.ObjRef;

public class InteractionsEditDescriptionContextMenuFiller extends
		AbstractEditDescriptionContextMenuFiller {

	public InteractionsEditDescriptionContextMenuFiller(TreeViewer viewer, ArchipelagoServices services, ObjRef xArchRef){
		super(viewer, services, xArchRef);
	}
	
	protected boolean matches(Object node){
		if(node != null){
			if(node instanceof ObjRef){
				ObjRef targetRef = (ObjRef)node;
				if(AS.xarch.isInstanceOf(targetRef, "interactions#Interaction")){
					return true;
				}
			}
		}
		return false;
	}

}
