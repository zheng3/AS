package edu.uci.isr.archstudio4.comp.archipelago.interactions.logics.editing;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.jdt.core.dom.*;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoUtils;
import edu.uci.isr.archstudio4.comp.archipelago.interactions.things.OperationLabel;
import edu.uci.isr.archstudio4.comp.archipelago.interactions.things.SWTOperationSelectorThing;
import edu.uci.isr.archstudio4.comp.archipelago.things.SWTXadlSelectorThing;
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
import edu.uci.isr.bna4.constants.CompletionStatus;
import edu.uci.isr.bna4.logics.coordinating.MoveWithLogic;
import edu.uci.isr.bna4.things.glass.BoxGlassThing;
import edu.uci.isr.bna4.things.utility.EnvironmentPropertiesThing;
import edu.uci.isr.sysutils.UIDGenerator;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;

public class InteractionsNewElementLogic extends AbstractThingLogic implements
		IBNAMenuListener, IBNAModelListener, IBNAMouseListener {

	protected ArchipelagoServices AS = null;
	protected ObjRef xArchRef = null;
	
	protected List<SWTOperationSelectorThing> openControls = Collections.synchronizedList(new ArrayList<SWTOperationSelectorThing>());
	
	protected EnvironmentPropertiesThing ept = null;

	public InteractionsNewElementLogic(ArchipelagoServices services, ObjRef xArchRef){
		this.AS = services;
		this.xArchRef = xArchRef;
	}
	
	public boolean matches(IBNAView view, IThing t){
		return t == null;
	}

	public ObjRef getRootRef(ObjRef eltRef){
		return xArchRef;
	}

	public int getFlags(ObjRef eltRef){
		int flags = 0;
		if(AS.xarch.isInstanceOf(eltRef, "interactions#Interaction")){
			return flags | XadlTreeUtils.COMPONENT | XadlTreeUtils.COMPONENT_INTERFACE | XadlTreeUtils.STRUCTURE;
		}
		return flags | XadlTreeUtils.ANY_TYPE;
	}
	
	public void fillMenu(IBNAView view, IMenuManager m, int localX, int localY,
			IThing t, int worldX, int worldY) {
		if(matches(view, t)){
			//Select XXX menu items
			for(IAction action : getSelectActions(view, t, worldX, worldY)){
				m.add(action);
			}
			//New XXX menu terms
			m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
			for(IAction action : getNewActions(view, t, worldX, worldY)){
				m.add(action);
			}
			m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		}
	}

	protected IAction[] getSelectActions(IBNAView view, IThing t, int worldX, int worldY){
		final ArchipelagoServices fAS = AS;
		final IBNAView fview = view;
		final IThing ft = t;
		final int fworldX = worldX;
		final int fworldY = worldY;
		
		ept = BNAUtils.getEnvironmentPropertiesThing(view.getWorld().getBNAModel());

		final String interactionXArchID = ept.getProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME);
		if(interactionXArchID == null){
			//Nothing to set description on
			return new IAction[0];
		}
		
		final ObjRef interactionRef = AS.xarch.getByID(xArchRef, interactionXArchID);
		if(interactionRef == null){
			//Nothing to add elements to
			return new IAction[0];
		}
		
		ArchipelagoUtils.setNewThingSpot(view.getWorld().getBNAModel(), fworldX, fworldY);

		Action selectAction = new Action("Select Starting Point Operation"){
			public void run(){
				try{
					Point p = BNAUtils.getCentralPoint(ft);
					if (p == null) {
						p = new Point(fworldX, fworldY);
					}

					SWTOperationSelectorThing st = new SWTOperationSelectorThing();
					st.setResources(fAS.resources);
					st.setRepository(fAS.xarch);
					st.setProperty("#targetXArchID", interactionXArchID);
					st.setContentProviderRootRef(getRootRef(interactionRef));
					st.setContentProviderFlags(getFlags(interactionRef));
					String xArchURI = AS.xarch.getXArchURI(xArchRef);
					String prjName = xArchURI.substring(1, xArchURI.indexOf('/', 1));
					st.setPrjName(prjName);
					st.setTask("Select_Starting_Point");

					st.setAnchorPoint(p);
					//MoveWithLogic.moveWith(ft, MoveWithLogic.TRACK_ANCHOR_POINT_FIRST, st);
					st.setEditing(true);
					openControls.add(st);
					fview.getWorld().getBNAModel().addThing(st, ft);

				}catch (Exception e){
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}			
		};
		
		return new IAction[]{selectAction};
	}	

	protected IAction[] getNewActions(IBNAView view, IThing t, int worldX, int worldY){
		final IBNAView fview = view;
		final IThing ft = t;
		final int fworldX = worldX;
		final int fworldY = worldY;
		
		ept = BNAUtils.getEnvironmentPropertiesThing(view.getWorld().getBNAModel());

		final String interactionXArchID = ept.getProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME);
		if(interactionXArchID == null){
			//Nothing to set description on
			return new IAction[0];
		}
		
		final ObjRef interactionRef = AS.xarch.getByID(xArchRef, interactionXArchID);
		if(interactionRef == null){
			//Nothing to add elements to
			return new IAction[0];
		}
		
		ArchipelagoUtils.setNewThingSpot(view.getWorld().getBNAModel(), fworldX, fworldY);
		
		Action newParticiplantAction = new Action("New Participant"){
			public void run(){
				Shell shell = AS.editor.getParentComposite().getShell();
				ObjRef componentRef = XadlSelectorDialog.showSelectorDialog(shell, "Select Existing Component", AS.xarch, AS.resources, xArchRef, XadlTreeUtils.STRUCTURE | XadlTreeUtils.DOCUMENT | XadlTreeUtils.COMPONENT, XadlTreeUtils.STRUCTURE | XadlTreeUtils.COMPONENT);
				if (componentRef == null){
					//User canceled the selection.
					return;
				}
				ObjRef interactionsContextRef = AS.xarch.createContext(xArchRef, "interactions");
				ObjRef lifelineRef = AS.xarch.create(interactionsContextRef, "LifeLine");
				AS.xarch.set(lifelineRef, "id", UIDGenerator.generateUID("lifeline"));
				//XadlUtils.setDescription(AS.xarch, lifelineRef, "[New LifeLine]");
				XadlUtils.setDescription(AS.xarch, lifelineRef, XadlUtils.getDescription(AS.xarch, componentRef));
				XadlUtils.setXLink(AS.xarch, lifelineRef, "represents", componentRef);
				AS.xarch.add(interactionRef, "lifeline", lifelineRef);
				//record change
				recordUpdateInteractionChange(interactionRef, 0);			
			}
			
			public ImageDescriptor getImageDescriptor(){
				return AS.resources.getImageDescriptor(ArchstudioResources.ICON_STATE);
			}
		};
		
		Action newMessageAction = new Action("New Interaction Message"){
			public void run(){
				try{
					ObjRef targetComp = XadlUtils.resolveXLink(AS.xarch, interactionRef, "targetComp");
					if (targetComp == null){
						//when the starting point is not set
						ObjRef interactionsContextRef = AS.xarch.createContext(xArchRef, "interactions");
						ObjRef messageRef = AS.xarch.create(interactionsContextRef, "Message");
						AS.xarch.set(messageRef, "id", UIDGenerator.generateUID("message"));
						XadlUtils.setDescription(AS.xarch, messageRef, "[New Message]");
						AS.xarch.add(interactionRef, "message", messageRef);
						//record change
						recordUpdateInteractionChange(interactionRef, 1);						
						return;
					}

					Point p = BNAUtils.getCentralPoint(ft);
					if (p == null) {
						p = new Point(fworldX, fworldY);
					}

					SWTOperationSelectorThing st = new SWTOperationSelectorThing();
					st.setResources(AS.resources);
					st.setRepository(AS.xarch);
					st.setProperty("#targetXArchID", interactionXArchID);
					st.setContentProviderRootRef(targetComp);
					st.setContentProviderFlags(getFlags(interactionRef));
					String xArchURI = AS.xarch.getXArchURI(xArchRef);
					String prjName = xArchURI.substring(1, xArchURI.indexOf('/', 1));
					st.setPrjName(prjName);
					st.setTask("New_Message");

					st.setAnchorPoint(p);
					//MoveWithLogic.moveWith(ft, MoveWithLogic.TRACK_ANCHOR_POINT_FIRST, st);
					st.setEditing(true);
					openControls.add(st);
					fview.getWorld().getBNAModel().addThing(st, ft);

				}catch (Exception e){
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}			
			
			public ImageDescriptor getImageDescriptor(){
				return AS.resources.getImageDescriptor(ArchstudioResources.ICON_LINK);
			}
		};

		Action selfCallMessageAction = new Action("New Self-Call Message"){
			public void run(){
				ObjRef interactionsContextRef = AS.xarch.createContext(xArchRef, "interactions");
				ObjRef messageRef = AS.xarch.create(interactionsContextRef, "Message");
				AS.xarch.set(messageRef, "id", UIDGenerator.generateUID("message"));
				XadlUtils.setDescription(AS.xarch, messageRef, "");
				AS.xarch.add(interactionRef, "message", messageRef);
				//record change
				recordUpdateInteractionChange(interactionRef, 1);						
				return;				
			}
		};		
		
		return new IAction[]{newParticiplantAction, newMessageAction, selfCallMessageAction};
	}

	public void mouseUp(IBNAView view, MouseEvent evt, IThing t, int worldX,
			int worldY) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDown(IBNAView view, MouseEvent evt, IThing t, int worldX,
			int worldY) {
		if(evt.button == 1){
			if(openControls.size() > 0){
				SWTOperationSelectorThing[] oc = openControls.toArray(new SWTOperationSelectorThing[openControls.size()]);
				for(SWTOperationSelectorThing st: oc){
					st.setCompletionStatus(CompletionStatus.CANCEL);
					st.setEditing(false);
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
	
	protected void addJavadoc(String prjName, String className, String mthdName, String interactionID){
		try{
			IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(prjName);
			IJavaProject jProj = JavaCore.create(proj);
			IType t = jProj.findType(className);
			ICompilationUnit icu = t.getCompilationUnit();
			
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setSource(icu);
			CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			cu.recordModifications();
			
			List cuTypes = cu.types();
			TypeDeclaration type = (TypeDeclaration)cuTypes.get(0);
			MethodDeclaration[] mthdDeclarations = type.getMethods();
			for (MethodDeclaration mthd: mthdDeclarations){
				String mName = mthd.getName().getIdentifier();
				if (mName.equals(mthdName)){
					//Obtain an IDocument
					ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
					IPath path = cu.getJavaElement().getPath();
					bufferManager.connect(path, LocationKind.IFILE,null);
					ITextFileBuffer textFileBuffer = bufferManager.getTextFileBuffer(path, LocationKind.IFILE);
					IDocument document = textFileBuffer.getDocument();
					//Create a new Javadoc node
					AST ast = cu.getAST();
					Javadoc jc = ast.newJavadoc();
					TagElement tag = ast.newTagElement();
					tag.setTagName(TagElement.TAG_SEE);
					TextElement txt = ast.newTextElement();
					txt.setText(interactionID);
					tag.fragments().add(txt);
					jc.tags().add(tag);
					//add the created Javadoc to corresponding method
					mthd.setJavadoc(jc);
					//Apply the change
					TextEdit text = cu.rewrite(document, null);
					text.apply(document);
					textFileBuffer.commit(null, false);
					bufferManager.disconnect(path, LocationKind.IFILE, null);
					return;
				}
			}			
			
		} catch (Exception e){
			;
		}
	}

	public void bnaModelChanged(BNAModelEvent evt) {
		if(evt.getEventType() == BNAModelEvent.EventType.THING_CHANGED){
			if(evt.getTargetThing() instanceof SWTOperationSelectorThing){
				SWTOperationSelectorThing st = (SWTOperationSelectorThing)evt.getTargetThing();
				if(openControls.contains(st)){
					if(st.getCompletionStatus() == CompletionStatus.OK){
						String task = st.getTask();
						if (task == "Select_Starting_Point"){
							String targetXArchID = st.getProperty("#targetXArchID");
							if(targetXArchID != null){
								ObjRef eltRef = AS.xarch.getByID(xArchRef, targetXArchID);
								if(eltRef != null){
									OperationLabel oLabel = (OperationLabel)st.getValue();
									ObjRef intf = oLabel.getInterface();
									ObjRef comp = AS.xarch.getParent(intf);
									XadlUtils.setXLink(AS.xarch, eltRef, "targetComp", comp);
									XadlUtils.setXLink(AS.xarch, eltRef, "targetIntf", intf);
									XadlUtils.setDescription(AS.xarch, eltRef, XadlUtils.getDescription(AS.xarch, comp)+"#"+XadlUtils.getDescription(AS.xarch, intf));
									
									ObjRef interactionsContextRef = AS.xarch.createContext(xArchRef, "interactions");
									ObjRef noteRef = AS.xarch.create(interactionsContextRef, "Note");
									AS.xarch.set(noteRef, "id", UIDGenerator.generateUID("note"));
									XadlUtils.setDescription(AS.xarch, noteRef, oLabel.getSource());
									AS.xarch.add(eltRef, "note", noteRef);
									
									addJavadoc(st.getPrjName(),oLabel.getClassName(), oLabel.getMthdName(), targetXArchID);
								}
							}							
						} else if (task == "New_Message"){
							String targetXArchID = st.getProperty("#targetXArchID");
							if(targetXArchID != null){
								ObjRef eltRef = AS.xarch.getByID(xArchRef, targetXArchID);
								if(eltRef != null){
									OperationLabel oLabel = (OperationLabel)st.getValue();
									ObjRef interactionsContextRef = AS.xarch.createContext(xArchRef, "interactions");
									ObjRef messageRef = AS.xarch.create(interactionsContextRef, "Message");
									AS.xarch.set(messageRef, "id", UIDGenerator.generateUID("message"));
									XadlUtils.setDescription(AS.xarch, messageRef, oLabel.toLabelString());
									//The following three lines are to create a child element of string type, not a good design for XADL XARCH.
									ObjRef classNameRef = AS.xarch.create(interactionsContextRef, "StringType");
									AS.xarch.set(messageRef, "interfaceClassName", classNameRef);
									AS.xarch.set(classNameRef, "value", oLabel.getClassName());
									AS.xarch.add(eltRef, "message", messageRef);
									//record change
									recordUpdateInteractionChange(eltRef, 1);
								}
							}							
						}
					}
					if(st.getCompletionStatus() != CompletionStatus.INCOMPLETE){
						evt.getSource().removeThing(st);
						openControls.remove(st);
					}
				}
			}
		}
	}
	
	protected void recordUpdateInteractionChange(ObjRef interactionRef, int changeType){
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
		ObjRef interactionChangeRef = AS.xarch.create(changesContextRef, "interactionChange");
		AS.xarch.set(interactionChangeRef, "id", UIDGenerator.generateUID("interactionChange"));
		AS.xarch.set(interactionChangeRef, "type", "update");
		if (changeType == 0){
			XadlUtils.setDescription(AS.xarch, interactionChangeRef, "New Participant");			
		} else if (changeType == 1){
			XadlUtils.setDescription(AS.xarch, interactionChangeRef, "New Message");			
		}
		XadlUtils.setXLink(AS.xarch, interactionChangeRef, "interaction", interactionRef);
		AS.xarch.add(changesRef, "interactionChange", interactionChangeRef);											
	}
}
