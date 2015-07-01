package edu.uci.isr.archstudio4.comp.archipelago.statecharts.logics.editing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchActionConstants;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoUtils;
import edu.uci.isr.archstudio4.comp.archipelago.interactions.things.OperationLabel;
import edu.uci.isr.archstudio4.comp.archipelago.interactions.things.SWTOperationSelectorThing;
import edu.uci.isr.archstudio4.util.ArchstudioResources;
import edu.uci.isr.archstudio4.util.XadlSelectorDialog;
import edu.uci.isr.archstudio4.util.XadlTreeUtils;
import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAModelEvent;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAMenuListener;
import edu.uci.isr.bna4.IBNAModelListener;
import edu.uci.isr.bna4.IBNAMouseListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.UserEditableUtils;
import edu.uci.isr.bna4.constants.CompletionStatus;
import edu.uci.isr.bna4.facets.IHasMutableText;
import edu.uci.isr.bna4.facets.IHasText;
import edu.uci.isr.bna4.logics.coordinating.MoveWithLogic;
import edu.uci.isr.bna4.logics.information.ToolTipLogic;
import edu.uci.isr.bna4.things.glass.BoxGlassThing;
import edu.uci.isr.bna4.things.glass.SplineGlassThing;
import edu.uci.isr.bna4.things.swt.SWTTextThing;
import edu.uci.isr.bna4.things.utility.EnvironmentPropertiesThing;
import edu.uci.isr.sysutils.UIDGenerator;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;

public class StatechartsNewElementLogic extends AbstractThingLogic implements IBNAMenuListener, IBNAModelListener, IBNAMouseListener{
	protected ArchipelagoServices AS = null;
	protected ObjRef xArchRef = null;
	
	protected List<SWTOperationSelectorThing> openControlsST = Collections.synchronizedList(new ArrayList<SWTOperationSelectorThing>());
	protected List<SWTTextThing> openControlsTT = Collections.synchronizedList(new ArrayList<SWTTextThing>());

	protected EnvironmentPropertiesThing ept = null;
	
	public StatechartsNewElementLogic(ArchipelagoServices services, ObjRef xArchRef){
		this.AS = services;
		this.xArchRef = xArchRef;
	}
	
	public boolean matches(IBNAView view, IThing t){
		if(t != null && (t instanceof SplineGlassThing)){
			return true;
		}
		return false;
	}
	
	public String getXArchID(IBNAView view, IThing t){
		if(t instanceof SplineGlassThing){
			IThing parentThing = view.getWorld().getBNAModel().getParentThing(t);
			return parentThing.getProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME);
		}
		return null;
	}

	public int getFlags(ObjRef eltRef){
		int flags = 0;
		if(AS.xarch.isInstanceOf(eltRef, "statecharts#Statechart")){
			return flags | XadlTreeUtils.COMPONENT | XadlTreeUtils.COMPONENT_INTERFACE | XadlTreeUtils.STRUCTURE;
		}
		return flags | XadlTreeUtils.ANY_TYPE;
	}

	public void fillMenu(IBNAView view, IMenuManager m, int localX, int localY, IThing t, int worldX, int worldY){
		if( t == null){
			for(IAction action : getActions(view, t, worldX, worldY)){
				m.add(action);
			}
			m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		} else if (matches(view, t)){
			for(IAction action : getTransitionActions(view, t, worldX, worldY)){
				m.add(action);
			}
			m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));			
		}
	}
	
	protected IAction[] getTransitionActions(IBNAView view, IThing t, int worldX, int worldY){
		final IBNAView fview = view;
		final IThing ft = t;
		final int fworldX = worldX;
		final int fworldY = worldY;

		ept = BNAUtils.getEnvironmentPropertiesThing(view.getWorld().getBNAModel());

		final String transitionXArchID = getXArchID(view, t);
		if(transitionXArchID == null){
			//Nothing to set description on
			return new IAction[0];
		}

		final ObjRef transitionRef = AS.xarch.getByID(xArchRef, transitionXArchID);
		if(transitionRef == null){
			//Nothing to set description on
			return new IAction[0];
		}

		String statechartXArchID = ept.getProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME);
		if(statechartXArchID == null){
			//Nothing to set description on
			return new IAction[0];
		}
		
		final ObjRef statechartRef = AS.xarch.getByID(xArchRef, statechartXArchID);
		if(statechartRef == null){
			//Nothing to add elements to
			return new IAction[0];
		}

		ArchipelagoUtils.setNewThingSpot(view.getWorld().getBNAModel(), fworldX, fworldY);
		
		Action addEventAction = new Action("Select Trigger Event"){
			public void run(){
				try {
					ObjRef targetComp = XadlUtils.resolveXLink(AS.xarch, statechartRef, "linkedComp");
					if (targetComp == null){
						//when the linked component is not set
						return;
					}

					Point p = BNAUtils.getCentralPoint(ft);
					if (p == null) {
						p = new Point(fworldX, fworldY);
					}

					SWTOperationSelectorThing st = new SWTOperationSelectorThing();
					st.setResources(AS.resources);
					st.setRepository(AS.xarch);
					st.setProperty("#targetXArchID", transitionXArchID);
					st.setContentProviderRootRef(targetComp);
					st.setContentProviderFlags(getFlags(statechartRef));
					String xArchURI = AS.xarch.getXArchURI(xArchRef);
					String prjName = xArchURI.substring(1, xArchURI.indexOf('/', 1));
					st.setPrjName(prjName);
					st.setTask("Add_Event");

					st.setAnchorPoint(p);
					//MoveWithLogic.moveWith(ft, MoveWithLogic.TRACK_ANCHOR_POINT_FIRST, st);
					st.setEditing(true);
					openControlsST.add(st);
					fview.getWorld().getBNAModel().addThing(st, ft);					
				} catch (Exception e){
					;
				}
			}
		};

		Action addActionAction = new Action("Edit Effect Action"){
			public void run(){
				Point p = BNAUtils.getCentralPoint(ft);
				if(p == null){
					p = new Point(fworldX, fworldY);
				}
				SWTTextThing tt = new SWTTextThing();
				tt.setProperty("#targetXArchID", transitionXArchID);
				ObjRef[] effectRef = AS.xarch.getAll(transitionRef, "effect");
				if ((effectRef!= null)&&(effectRef.length>0)){
					tt.setText((String)AS.xarch.get(effectRef[0], "value"));
				} else{
					tt.setText("");
				}

				tt.setAnchorPoint(p);
				tt.setEditing(true);
				openControlsTT.add(tt);
				fview.getWorld().getBNAModel().addThing(tt, ft);
			}
		};

		return new IAction[]{addEventAction, addActionAction};
		
	}
	
	
	protected IAction[] getActions(IBNAView view, IThing t, int worldX, int worldY){
		final IBNAView fview = view;
		final IThing ft = t;
		final int fworldX = worldX;
		final int fworldY = worldY;
		
		ept = BNAUtils.getEnvironmentPropertiesThing(view.getWorld().getBNAModel());

		String statechartXArchID = ept.getProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME);
		if(statechartXArchID == null){
			//Nothing to set description on
			return new IAction[0];
		}
		
		final ObjRef statechartRef = AS.xarch.getByID(xArchRef, statechartXArchID);
		if(statechartRef == null){
			//Nothing to add elements to
			return new IAction[0];
		}
		
		ArchipelagoUtils.setNewThingSpot(view.getWorld().getBNAModel(), fworldX, fworldY);
		
		Action selectComponentAction = new Action("Select Host Component"){
			public void run(){
				Shell shell = AS.editor.getParentComposite().getShell();
				ObjRef componentRef = XadlSelectorDialog.showSelectorDialog(shell, "Select Existing Component", AS.xarch, AS.resources, xArchRef, XadlTreeUtils.STRUCTURE | XadlTreeUtils.DOCUMENT | XadlTreeUtils.COMPONENT, XadlTreeUtils.STRUCTURE | XadlTreeUtils.COMPONENT);
				if (componentRef == null){
					//User canceled the selection.
					return;
				}
				XadlUtils.setXLink(AS.xarch, statechartRef, "linkedComp", componentRef);
				XadlUtils.setDescription(AS.xarch, statechartRef, XadlUtils.getDescription(AS.xarch, componentRef));
			}
			
			public ImageDescriptor getImageDescriptor(){
				return AS.resources.getImageDescriptor(ArchstudioResources.ICON_COMPONENT);
			}
		};
		
		Action newStateAction = new Action("New Ordinary State"){
			public void run(){
				ObjRef statechartsContextRef = AS.xarch.createContext(xArchRef, "statecharts");
				ObjRef stateRef = AS.xarch.create(statechartsContextRef, "state");
				AS.xarch.set(stateRef, "id", UIDGenerator.generateUID("state"));
				XadlUtils.setDescription(AS.xarch, stateRef, "[New State]");
				AS.xarch.set(stateRef, "stateType", "state");
				AS.xarch.add(statechartRef, "state", stateRef);
				//record change
				recordUpdateStatechartChange(statechartRef, 0);
			}
			
			public ImageDescriptor getImageDescriptor(){
				return AS.resources.getImageDescriptor(ArchstudioResources.ICON_STATE);
			}
		};
		
		Action newInitialStateAction = new Action("New Initial State"){
			public void run(){
				ObjRef statechartsContextRef = AS.xarch.createContext(xArchRef, "statecharts");
				ObjRef stateRef = AS.xarch.create(statechartsContextRef, "state");
				AS.xarch.set(stateRef, "id", UIDGenerator.generateUID("state"));
				XadlUtils.setDescription(AS.xarch, stateRef, "[Initial State]");
				AS.xarch.set(stateRef, "stateType", "initial");
				AS.xarch.add(statechartRef, "state", stateRef);
				//record change
				recordUpdateStatechartChange(statechartRef, 0);
			}
			
			public ImageDescriptor getImageDescriptor(){
				return AS.resources.getImageDescriptor(ArchstudioResources.ICON_INITIAL_STATE);
			}
		};
		
		Action newFinalStateAction = new Action("New Final State"){
			public void run(){
				ObjRef statechartsContextRef = AS.xarch.createContext(xArchRef, "statecharts");
				ObjRef stateRef = AS.xarch.create(statechartsContextRef, "state");
				AS.xarch.set(stateRef, "id", UIDGenerator.generateUID("state"));
				XadlUtils.setDescription(AS.xarch, stateRef, "[Final State]");
				AS.xarch.set(stateRef, "stateType", "final");
				AS.xarch.add(statechartRef, "state", stateRef);
				//record change
				recordUpdateStatechartChange(statechartRef, 0);
			}
			
			public ImageDescriptor getImageDescriptor(){
				return AS.resources.getImageDescriptor(ArchstudioResources.ICON_FINAL_STATE);
			}
		};
		
		Action newTransitionAction = new Action("New Transition"){
			public void run(){
				ObjRef statechartsContextRef = AS.xarch.createContext(xArchRef, "statecharts");
				ObjRef transitionRef = AS.xarch.create(statechartsContextRef, "transition");
				AS.xarch.set(transitionRef, "id", UIDGenerator.generateUID("transition"));
				XadlUtils.setDescription(AS.xarch, transitionRef, "[New Transition]");
				AS.xarch.add(statechartRef, "transition", transitionRef);
				//record change
				recordUpdateStatechartChange(statechartRef, 1);
			}
			
			public ImageDescriptor getImageDescriptor(){
				return AS.resources.getImageDescriptor(ArchstudioResources.ICON_LINK);
			}
		};
		
		return new IAction[]{selectComponentAction, newStateAction, newInitialStateAction, newFinalStateAction, newTransitionAction};
	}

	public void mouseUp(IBNAView view, MouseEvent evt, IThing t, int worldX,
			int worldY) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDown(IBNAView view, MouseEvent evt, IThing t, int worldX,
			int worldY) {
		if(evt.button == 1){
			if(openControlsST.size() > 0){
				SWTOperationSelectorThing[] oc = openControlsST.toArray(new SWTOperationSelectorThing[openControlsST.size()]);
				for(SWTOperationSelectorThing st: oc){
					st.setCompletionStatus(CompletionStatus.CANCEL);
					st.setEditing(false);
				}
			}
			if(openControlsTT.size() > 0){
				SWTTextThing[] oc = openControlsTT.toArray(new SWTTextThing[openControlsTT.size()]);
				for(SWTTextThing tt: oc){
					tt.setCompletionStatus(CompletionStatus.OK);
					tt.setEditing(false);
				}
			}
		}		
	}

	public void mouseClick(IBNAView view, MouseEvent evt, IThing t, int worldX,
			int worldY) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDoubleClick(IBNAView view, MouseEvent evt, IThing t,
			int worldX, int worldY) {
		// TODO Auto-generated method stub
		
	}

	public void bnaModelChanged(BNAModelEvent evt) {
		if(evt.getEventType() == BNAModelEvent.EventType.THING_CHANGED){
			if(evt.getTargetThing() instanceof SWTOperationSelectorThing){
				SWTOperationSelectorThing st = (SWTOperationSelectorThing)evt.getTargetThing();
				if(openControlsST.contains(st)){
					if(st.getCompletionStatus() == CompletionStatus.OK){
						String task = st.getTask();
						if (task == "Add_Event"){
							String targetXArchID = st.getProperty("#targetXArchID");
							if(targetXArchID != null){
								ObjRef eltRef = AS.xarch.getByID(xArchRef, targetXArchID);
								if(eltRef != null){
									OperationLabel oLabel = (OperationLabel)st.getValue();
									//add trigger event
									ObjRef[] eventRefs = AS.xarch.getAll(eltRef, "trigger");
									if ((eventRefs!= null)&&(eventRefs.length>0)){
										AS.xarch.set(eventRefs[0], "value", oLabel.getMthdName());
									} else {
										ObjRef statechartsContextRef = AS.xarch.createContext(xArchRef, "statecharts");
										ObjRef eventRef = AS.xarch.create(statechartsContextRef, "Event");
										AS.xarch.set(eventRef, "value", oLabel.getMthdName());
										AS.xarch.add(eltRef, "trigger", eventRef);										
									}									
									//update transition description
									ObjRef[] effectRef = AS.xarch.getAll(eltRef, "effect");
									if ((effectRef!= null)&&(effectRef.length>0)){
										XadlUtils.setDescription(AS.xarch, eltRef, oLabel.getMthdName()+" / "+AS.xarch.get(effectRef[0], "value"));										
									} else{
										XadlUtils.setDescription(AS.xarch, eltRef, oLabel.getMthdName()+" /");										
									}
								}
							}							
						}
					}
					if(st.getCompletionStatus() != CompletionStatus.INCOMPLETE){
						evt.getSource().removeThing(st);
						openControlsST.remove(st);
					}
				}
			} else if(evt.getTargetThing() instanceof SWTTextThing){
				SWTTextThing tt = (SWTTextThing)evt.getTargetThing();
				if(openControlsTT.contains(tt)){
					if(tt.getCompletionStatus() == CompletionStatus.OK){
						String targetXArchID = tt.getProperty("#targetXArchID");
						if(targetXArchID != null){
							ObjRef eltRef = AS.xarch.getByID(xArchRef, targetXArchID);
							if(eltRef != null){
								//edit effect action
								ObjRef[] actionRefs = AS.xarch.getAll(eltRef, "effect");
								if ((actionRefs!= null)&&(actionRefs.length>0)){
									AS.xarch.set(actionRefs[0], "value", tt.getText());
								} else {
									ObjRef statechartsContextRef = AS.xarch.createContext(xArchRef, "statecharts");
									ObjRef actionRef = AS.xarch.create(statechartsContextRef, "Action");
									AS.xarch.set(actionRef, "value", tt.getText());
									AS.xarch.add(eltRef, "effect", actionRef);										
								}									
								//update transition description
								ObjRef[] eventRef = AS.xarch.getAll(eltRef, "trigger");
								if ((eventRef!= null)&&(eventRef.length>0)){
									XadlUtils.setDescription(AS.xarch, eltRef, AS.xarch.get(eventRef[0], "value")+" / "+tt.getText());										
								} else{
									XadlUtils.setDescription(AS.xarch, eltRef, "/ "+tt.getText());										
								}
							}
						}							
					}
					if(tt.getCompletionStatus() != CompletionStatus.INCOMPLETE){
						evt.getSource().removeThing(tt);
						openControlsTT.remove(tt);
					}
				}
			}
		}
	}

	protected void recordUpdateStatechartChange(ObjRef statechartRef, int changeType){
		//To record changes
		String changesXArchID = ept.getProperty("ChangesID");
		if(changesXArchID == null){
			//Start a new change session
			ObjRef changesContextRef = AS.xarch.createContext(xArchRef, "changes");
			ObjRef archChangeRef = AS.xarch.getElement(changesContextRef, "archChange", xArchRef);			
			ObjRef changesRef = AS.xarch.create(changesContextRef, "changes");
			AS.xarch.set(changesRef, "id", UIDGenerator.generateUID("changes"));
			AS.xarch.set(changesRef, "status", "unmapped");
			String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm";
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			XadlUtils.setDescription(AS.xarch, changesRef, sdf.format(cal.getTime()));
			AS.xarch.add(archChangeRef, "changes", changesRef);
			//Set the environment indicator
			changesXArchID = XadlUtils.getID(AS.xarch, changesRef);
			ept.setProperty("ChangesID", changesXArchID);
		}
		
		final ObjRef changesRef = AS.xarch.getByID(xArchRef, changesXArchID);
		if(changesRef == null){
			//Nothing to add elements to
			return;
		}
		//record changes
		ObjRef changesContextRef = AS.xarch.createContext(xArchRef, "changes");
		ObjRef statechartChangeRef = AS.xarch.create(changesContextRef, "statechartChange");
		AS.xarch.set(statechartChangeRef, "id", UIDGenerator.generateUID("statechartChange"));
		AS.xarch.set(statechartChangeRef, "type", "update");
		if (changeType == 0){
			XadlUtils.setDescription(AS.xarch, statechartChangeRef, "New State");			
		} else if (changeType == 1){
			XadlUtils.setDescription(AS.xarch, statechartChangeRef, "New Transition");			
		}
		XadlUtils.setXLink(AS.xarch, statechartChangeRef, "statechart", statechartRef);
		AS.xarch.add(changesRef, "statechartChange", statechartChangeRef);											
	}	
}
