package edu.uci.isr.archstudio4.comp.archipelago.util;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.IArchipelagoTreeContextMenuFiller;
import edu.uci.isr.xarchflat.ObjRef;

public abstract class AbstractRemoveContextMenuFiller implements IArchipelagoTreeContextMenuFiller{
	protected TreeViewer viewer = null;
	protected ArchipelagoServices AS =  null;
	protected ObjRef xArchRef = null;
	
	public AbstractRemoveContextMenuFiller(TreeViewer viewer, ArchipelagoServices AS, ObjRef xArchRef){
		this.viewer = viewer;
		this.AS = AS;
		this.xArchRef = xArchRef;
	}
	
	protected abstract boolean matches(Object node);
	
	public void fillContextMenu(IMenuManager m, Object[] selectedNodes){
		if((selectedNodes != null) && (selectedNodes.length == 1)){
			Object selectedNode = selectedNodes[0];
			if((selectedNode instanceof ObjRef) && matches(selectedNodes[0])){
				final ObjRef targetRef = (ObjRef)selectedNode;
				IAction removeAction = new Action("Remove"){
					public void run(){
						remove(targetRef);
					}
				};
				m.add(removeAction);
			}
		}
	}
	
	protected void remove(ObjRef targetRef){
		String tagName = AS.xarch.getElementName(targetRef);
		if(tagName != null){
			ObjRef parentRef = AS.xarch.getParent(targetRef);
			if(parentRef != null){
				AS.xarch.remove(parentRef, tagName, targetRef);
			}
		}
	}
}
