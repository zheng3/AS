package edu.uci.isr.archstudio4.comp.archipelago.interactions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.FolderNode;
import edu.uci.isr.archstudio4.comp.archipelago.IArchipelagoTreeContextMenuFiller;
import edu.uci.isr.archstudio4.comp.archipelago.statecharts.StatechartsTreeContentProvider;
import edu.uci.isr.sysutils.UIDGenerator;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;

public class InteractionsNewInteractionContextMenuFiller implements
		IArchipelagoTreeContextMenuFiller {

	protected TreeViewer viewer = null;
	protected ArchipelagoServices AS =  null;
	protected ObjRef xArchRef = null;
	
	public InteractionsNewInteractionContextMenuFiller(TreeViewer viewer, ArchipelagoServices AS, ObjRef xArchRef){
		this.viewer = viewer;
		this.AS = AS;
		this.xArchRef = xArchRef;
	}

	public void fillContextMenu(IMenuManager m, Object[] selectedNodes) {
		// TODO Auto-generated method stub
		if((selectedNodes != null) && (selectedNodes.length == 1)){
			Object selectedNode = selectedNodes[0];
			if(selectedNode instanceof FolderNode){
				FolderNode fn = (FolderNode)selectedNode;
				String fnType = fn.getType();
				if(fnType != null){
					if(fnType.equals(InteractionsTreeContentProvider.FOLDER_NODE_TYPE)){
						IAction newStatechartAction = new Action("New Interaction"){
							public void run(){
								createNewInteraction();
							}
						};
						m.add(newStatechartAction);
					}
				}
			}
		}
	}

	protected void createNewInteraction(){
		ObjRef interactionsContextRef = AS.xarch.createContext(xArchRef, "interactions");
		ObjRef newInteractionRef = AS.xarch.createElement(interactionsContextRef, "interaction");
		String newID = UIDGenerator.generateUID("interaction");
		
		AS.xarch.set(newInteractionRef, "id", newID);
		XadlUtils.setDescription(AS.xarch, newInteractionRef, "[New Interaction]");
		AS.xarch.add(xArchRef, "object", newInteractionRef);

		//Create the "archChange" reference if not created yet
		ObjRef changesContextRef = AS.xarch.createContext(xArchRef, "changes");
		ObjRef archChangeRef = AS.xarch.getElement(changesContextRef, "archChange", xArchRef);
		if (archChangeRef == null){
			//Create ArchChange
			archChangeRef = AS.xarch.createElement(changesContextRef, "archChange");
			String changeID = UIDGenerator.generateUID("archChange");				
			AS.xarch.set(archChangeRef, "id", changeID);
			XadlUtils.setDescription(AS.xarch, archChangeRef, "Architecture Change Model");
			AS.xarch.add(xArchRef, "object", archChangeRef);				
		}
		//Load "unmapped" change session if it exists.
		ObjRef[] changes = AS.xarch.getAll(archChangeRef, "changes");
		ObjRef changesRef = null;
		for (ObjRef chg: changes){
			String status = (String)AS.xarch.get(chg, "status");
			if (status.equals("unmapped")){
				//record changes
				changesRef = chg;
				break;
			}							
		}
		if (changesRef == null){
			//Start a new change session
			changesRef = AS.xarch.create(changesContextRef, "changes");
			AS.xarch.set(changesRef, "id", UIDGenerator.generateUID("changes"));
			AS.xarch.set(changesRef, "status", "unmapped");
			String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm";
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			XadlUtils.setDescription(AS.xarch, changesRef, sdf.format(cal.getTime()));
			AS.xarch.add(archChangeRef, "changes", changesRef);			
		}
		//Record the action of new interaction.
		ObjRef interactionChangeRef = AS.xarch.create(changesContextRef, "interactionChange");
		AS.xarch.set(interactionChangeRef, "id", UIDGenerator.generateUID("interactionChange"));
		AS.xarch.set(interactionChangeRef, "type", "add");
		XadlUtils.setDescription(AS.xarch, interactionChangeRef, "New Interaction");
		XadlUtils.setXLink(AS.xarch, interactionChangeRef, "interaction", newInteractionRef);
		AS.xarch.add(changesRef, "interactionChange", interactionChangeRef);	
	}
	
}
