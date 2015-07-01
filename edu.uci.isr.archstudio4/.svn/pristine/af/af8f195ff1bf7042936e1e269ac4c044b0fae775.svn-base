package edu.uci.isr.archstudio4.comp.archipelago.generic.logics.editing;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoUtils;
import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAMenuListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.assemblies.AssemblyUtils;
import edu.uci.isr.bna4.assemblies.IAssembly;
import edu.uci.isr.bna4.things.utility.EnvironmentPropertiesThing;
import edu.uci.isr.sysutils.UIDGenerator;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public class XadlRemoveElementLogic
	extends AbstractThingLogic
	implements IBNAMenuListener{

	final protected XArchFlatInterface xarch;

	final protected ObjRef xArchRef;

	public XadlRemoveElementLogic(XArchFlatInterface xarch, ObjRef xArchRef){
		this.xarch = xarch;
		this.xArchRef = xArchRef;
	}

	public void fillMenu(IBNAView view, IMenuManager m, int localX, int localY, IThing t, int worldX, int worldY){

		if(t != null){
			final Set<String> selectedThingIDs = new HashSet<String>();
			for(IThing selectedThing: BNAUtils.getSelectedThings(view.getWorld().getBNAModel())){
				IAssembly assembly = AssemblyUtils.getAssemblyWithPart(selectedThing);
				if(assembly != null){
					String xArchID = assembly.getRootThing().getProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME);
					if(xArchID != null){
						selectedThingIDs.add(xArchID);
					}
				}
			}
			if(t != null){
				IAssembly assembly = AssemblyUtils.getAssemblyWithPart(t);
				if(assembly != null){
					String xArchID = assembly.getRootThing().getProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME);
					if(xArchID != null){
						selectedThingIDs.add(xArchID);
					}
				}
			}
			if(selectedThingIDs.size() > 0){
				//To record changes
				final EnvironmentPropertiesThing ept = BNAUtils.getEnvironmentPropertiesThing(view.getWorld().getBNAModel());

				m.add(new Action(selectedThingIDs.size() == 1 ? "Remove" : "Remove " + selectedThingIDs.size() + " Elements"){

					@Override
					public void run(){
						for(String xArchID: selectedThingIDs){
							ObjRef objRef = xarch.getByID(xArchRef, xArchID);
							if(objRef != null){
								String changesXArchID = ept.getProperty("ChangesID");
								if(changesXArchID == null){
									//Start a new change session
									ObjRef changesContextRef = xarch.createContext(xArchRef, "changes");
									ObjRef archChangeRef = xarch.getElement(changesContextRef, "archChange", xArchRef);			
									ObjRef changesRef = xarch.create(changesContextRef, "changes");
									xarch.set(changesRef, "id", UIDGenerator.generateUID("changes"));
									xarch.set(changesRef, "status", "unmapped");
									String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm";
									Calendar cal = Calendar.getInstance();
									SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
									XadlUtils.setDescription(xarch, changesRef, sdf.format(cal.getTime()));
									xarch.add(archChangeRef, "changes", changesRef);
									//Set the environment indicator
									changesXArchID = XadlUtils.getID(xarch, changesRef);
									ept.setProperty("ChangesID", changesXArchID);
								}								
								final ObjRef changesRef = xarch.getByID(xArchRef, changesXArchID);
								if(changesRef == null){
									//Abnormal: no change session is set in the environment
									return;
								}
								//recording changes
								String eleType = xarch.getElementName(objRef);
								if (eleType.equalsIgnoreCase("component")){
									ObjRef changesContextRef = xarch.createContext(xArchRef, "changes");
									ObjRef compChangeRef = xarch.create(changesContextRef, "componentChange");
									xarch.set(compChangeRef, "id", UIDGenerator.generateUID("componentChange"));
									xarch.set(compChangeRef, "type", "remove");
									XadlUtils.setDescription(xarch, compChangeRef, "Remove Component");
									ObjRef clonedElt = xarch.cloneElement(objRef, 100);
									XadlUtils.setXLink(xarch, compChangeRef, "component", clonedElt);
									xarch.set(compChangeRef, "copyOfRemovedComponent", clonedElt);
									xarch.add(changesRef, "componentChange", compChangeRef);									
								} else if (eleType.equalsIgnoreCase("link")){
									ObjRef changesContextRef = xarch.createContext(xArchRef, "changes");
									ObjRef linkChangeRef = xarch.create(changesContextRef, "linkChange");
									xarch.set(linkChangeRef, "id", UIDGenerator.generateUID("linkChange"));
									xarch.set(linkChangeRef, "type", "remove");
									XadlUtils.setDescription(xarch, linkChangeRef, "Remove Link");
									ObjRef clonedElt = xarch.cloneElement(objRef, 100);
									XadlUtils.setXLink(xarch, linkChangeRef, "link", clonedElt);
									xarch.set(linkChangeRef, "copyOfRemovedLink", clonedElt);
									xarch.add(changesRef, "linkChange", linkChangeRef);									
								} else if (eleType.equalsIgnoreCase("interface")){
									ObjRef parentRef = xarch.getParent(objRef);
									ObjRef changesContextRef = xarch.createContext(xArchRef, "changes");
									ObjRef compChangeRef = xarch.create(changesContextRef, "componentChange");
									xarch.set(compChangeRef, "id", UIDGenerator.generateUID("componentChange"));
									xarch.set(compChangeRef, "type", "update");
									XadlUtils.setDescription(xarch, compChangeRef, "Remove Interface");
									XadlUtils.setXLink(xarch, compChangeRef, "component", parentRef);
									//Add interface change - START
									ObjRef intfChangeRef = xarch.create(changesContextRef, "interfaceChange");
									xarch.set(intfChangeRef, "id", UIDGenerator.generateUID("interfaceChange"));
									xarch.set(intfChangeRef, "type", "remove");
									ObjRef clonedElt = xarch.cloneElement(objRef, 100);
									xarch.set(intfChangeRef, "copyOfRemovedInterface", clonedElt);
									XadlUtils.setXLink(xarch, intfChangeRef, "interface", clonedElt);
									xarch.set(compChangeRef, "interfaceChange", intfChangeRef);
									//Add interface change - END
									xarch.add(changesRef, "componentChange", compChangeRef);																		
								} else if (eleType.equalsIgnoreCase("lifeline")){
									ObjRef parentRef = xarch.getParent(objRef);
									ObjRef changesContextRef = xarch.createContext(xArchRef, "changes");
									ObjRef interactionChangeRef = xarch.create(changesContextRef, "interactionChange");
									xarch.set(interactionChangeRef, "id", UIDGenerator.generateUID("interactionChange"));
									xarch.set(interactionChangeRef, "type", "update");
									XadlUtils.setDescription(xarch, interactionChangeRef, "Remove Participant");
									XadlUtils.setXLink(xarch, interactionChangeRef, "interaction", parentRef);
									xarch.add(changesRef, "interactionChange", interactionChangeRef);																		
								} else if (eleType.equalsIgnoreCase("message")){
									ObjRef parentRef = xarch.getParent(objRef);
									ObjRef changesContextRef = xarch.createContext(xArchRef, "changes");
									ObjRef interactionChangeRef = xarch.create(changesContextRef, "interactionChange");
									xarch.set(interactionChangeRef, "id", UIDGenerator.generateUID("interactionChange"));
									xarch.set(interactionChangeRef, "type", "update");
									XadlUtils.setDescription(xarch, interactionChangeRef, "Remove Message");
									XadlUtils.setXLink(xarch, interactionChangeRef, "interaction", parentRef);
									xarch.add(changesRef, "interactionChange", interactionChangeRef);																		
								} else if (eleType.equalsIgnoreCase("state")){
									ObjRef parentRef = xarch.getParent(objRef);
									ObjRef changesContextRef = xarch.createContext(xArchRef, "changes");
									ObjRef statechartChangeRef = xarch.create(changesContextRef, "statechartChange");
									xarch.set(statechartChangeRef, "id", UIDGenerator.generateUID("statechartChange"));
									xarch.set(statechartChangeRef, "type", "update");
									XadlUtils.setDescription(xarch, statechartChangeRef, "Remove State");
									XadlUtils.setXLink(xarch, statechartChangeRef, "statechart", parentRef);
									xarch.add(changesRef, "statechartChange", statechartChangeRef);																		
								} else if (eleType.equalsIgnoreCase("transition")){
									ObjRef parentRef = xarch.getParent(objRef);
									ObjRef changesContextRef = xarch.createContext(xArchRef, "changes");
									ObjRef statechartChangeRef = xarch.create(changesContextRef, "statechartChange");
									xarch.set(statechartChangeRef, "id", UIDGenerator.generateUID("statechartChange"));
									xarch.set(statechartChangeRef, "type", "update");
									XadlUtils.setDescription(xarch, statechartChangeRef, "Remove Transition");
									XadlUtils.setXLink(xarch, statechartChangeRef, "statechart", parentRef);
									xarch.add(changesRef, "statechartChange", statechartChangeRef);																		
								}
								
								XadlUtils.remove(xarch, objRef);								
							}
						}
					}
				});
			}
		}
	}
}
