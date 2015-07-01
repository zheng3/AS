package edu.uci.isr.archstudio4.comp.archipelago.statecharts;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.jface.viewers.TreeViewer;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.util.AbstractRemoveContextMenuFiller;
import edu.uci.isr.sysutils.UIDGenerator;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;

public class StatechartsRemoveContextMenuFiller extends AbstractRemoveContextMenuFiller{

	public StatechartsRemoveContextMenuFiller(TreeViewer viewer, ArchipelagoServices services, ObjRef xArchRef){
		super(viewer, services, xArchRef);
	}
	
	protected boolean matches(Object node){
		if(node != null){
			if(node instanceof ObjRef){
				ObjRef targetRef = (ObjRef)node;
				if(AS.xarch.isInstanceOf(targetRef, "statecharts#Statechart")){
					return true;
				}
			}
		}
		return false;
	}
	
	protected void remove(ObjRef targetRef){
		if(targetRef != null){
			if(AS.xarch.isInstanceOf(targetRef, "statecharts#Statechart")){
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
				ObjRef statechartChangeRef = AS.xarch.create(changesContextRef, "statechartChange");
				AS.xarch.set(statechartChangeRef, "id", UIDGenerator.generateUID("statechartChange"));
				AS.xarch.set(statechartChangeRef, "type", "remove");
				XadlUtils.setDescription(AS.xarch, statechartChangeRef, "Remove Statechart");
				ObjRef clonedElt = AS.xarch.cloneElement(targetRef, 100);
				XadlUtils.setXLink(AS.xarch, statechartChangeRef, "statechart", clonedElt);
				AS.xarch.set(statechartChangeRef, "copyOfRemovedStatechart", clonedElt);
				AS.xarch.add(changesRef, "statechartChange", statechartChangeRef);

				AS.xarch.remove(xArchRef, "Object", targetRef);
				return;
			}
		}
		super.remove(targetRef);
	}
}
