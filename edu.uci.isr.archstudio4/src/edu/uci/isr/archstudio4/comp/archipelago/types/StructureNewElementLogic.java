package edu.uci.isr.archstudio4.comp.archipelago.types;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchActionConstants;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoUtils;
import edu.uci.isr.archstudio4.comp.archipelago.FolderNode;
import edu.uci.isr.archstudio4.comp.booleannotation.ParseException;
import edu.uci.isr.archstudio4.comp.booleannotation.TokenMgrError;
import edu.uci.isr.archstudio4.util.ArchstudioResources;
import edu.uci.isr.archstudio4.util.XadlSelectorDialog;
import edu.uci.isr.archstudio4.util.XadlTreeUtils;
import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAComposite;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAMenuListener;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.things.utility.EnvironmentPropertiesThing;
import edu.uci.isr.sysutils.UIDGenerator;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;

public class StructureNewElementLogic extends AbstractThingLogic implements IBNAMenuListener{
	protected ArchipelagoServices AS = null;
	protected ObjRef xArchRef = null;
	
	public StructureNewElementLogic(ArchipelagoServices services, ObjRef xArchRef){
		this.AS = services;
		this.xArchRef = xArchRef;
	}
	
	public boolean matches(IBNAView view, IThing t){
		return t == null;
	}
	
	public void fillMenu(IBNAView view, IMenuManager m, int localX, int localY, IThing t, int worldX, int worldY){
		if(matches(view, t)){
			for(IAction action : getActions(view, t, worldX, worldY)){
				m.add(action);
			}
			m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		}
	}
	
	protected IAction[] getActions(IBNAView view, IThing t, int worldX, int worldY){
		final IBNAView fview = view;
		final IThing ft = t;
		final int fworldX = worldX;
		final int fworldY = worldY;
		
		final EnvironmentPropertiesThing ept = BNAUtils.getEnvironmentPropertiesThing(view.getWorld().getBNAModel());

		String archStructureXArchID = ept.getProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME);
		if(archStructureXArchID == null){
			//Nothing to set description on
			return new IAction[0];
		}
		
		final ObjRef archStructureRef = AS.xarch.getByID(xArchRef, archStructureXArchID);
		if(archStructureRef == null){
			//Nothing to add elements to
			return new IAction[0];
		}
		
		ArchipelagoUtils.setNewThingSpot(view.getWorld().getBNAModel(), fworldX, fworldY);
		
	
		
		//varun optional component
		Action newOptionalComponentAction = new Action("New Component"){
			public void run(){
				
				
				BNAComposite composit = ArchipelagoUtils.getBNAComposite(AS.editor);
				if(composit == null){
					return;
				}
				if(composit.getView() == null){
					return;
				}
				
				if(composit.getView().getWorld() == null){
					return;
				}
				if(composit.getView().getWorld().getBNAModel() == null){
					return;
				}
				
				IBNAModel model = composit.getView().getWorld().getBNAModel();
				
				
				
				ObjRef typesContextRef = AS.xarch.createContext(xArchRef, "types");
				ObjRef componentRef = AS.xarch.create(typesContextRef, "component");
				String id = UIDGenerator.generateUID("component");
				AS.xarch.set(componentRef, "id",id );
				XadlUtils.setDescription(AS.xarch, componentRef, "[New Opt1 Component]");
				
				boolean alternativeFlag = false;
				if(model.getSelectedFeature()!=null){
				ObjRef featureRef = AS.xarch.getByID(model.getSelectedFeature());
				if( AS.xarch.isInstanceOf(featureRef, "features#AlternativeFeature") && !isOptionalAlternative(featureRef)){
				alternativeFlag = true;
				}
				}
				if(model.getSelectedFeature()==null || alternativeFlag){
					XadlUtils.setDescription(AS.xarch, componentRef, "[New Component]");	
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
				ObjRef compChangeRef = AS.xarch.create(changesContextRef, "componentChange");
				AS.xarch.set(compChangeRef, "id", UIDGenerator.generateUID("componentChange"));
				AS.xarch.set(compChangeRef, "type", "add");
				XadlUtils.setDescription(AS.xarch, compChangeRef, "New Opt1 Component");
				XadlUtils.setXLink(AS.xarch, compChangeRef, "component", componentRef);
				AS.xarch.add(changesRef, "componentChange", compChangeRef);
				
				
				
				}else{

					//promote to optional
					ObjRef optionsContext = AS.xarch.createContext(xArchRef, "options");
					String promotedTypeName = getPromotedTypeName(componentRef);
					if(promotedTypeName != null){
						AS.xarch.promoteTo(optionsContext, promotedTypeName, componentRef);
					}
					//getGuardParentRef
					ObjRef optionsContextRef = AS.xarch.createContext(xArchRef, "options");
					ObjRef optionalRef = AS.xarch.create(optionsContextRef, "optional");
					AS.xarch.set(componentRef, "optional", optionalRef);



					ObjRef guardRef = null;
					try {
						
						String name = model.getSelectedFeature();
						String featureSymbol =name.replaceAll("-", "");
						ObjRef featureRef = AS.xarch.getByID(name);
						if(AS.xarch.isInstanceOf(featureRef, "features#Varient")){
							ObjRef varients = AS.xarch.getParent(featureRef);
							ObjRef feature = AS.xarch.getParent(varients);
							String featureVal = (String)AS.xarch.get(feature, "id");
							featureVal = featureVal.replaceAll("-", "");
							guardRef = AS.booleanNotation.parseBooleanGuard(featureVal + " == \""+featureSymbol+"\"", xArchRef);
						}else if(AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature")){
							
							guardRef = AS.booleanNotation.parseBooleanGuard(featureSymbol + " == \"true\"", xArchRef);
						}else if(AS.xarch.isInstanceOf(featureRef, "features#AlternativeFeature") && isOptionalAlternative(featureRef)){
							guardRef = AS.booleanNotation.parseBooleanGuard(featureSymbol + " != \"false\"", xArchRef);
						}
						if(guardRef!=null)
						AS.xarch.set(optionalRef, "guard", guardRef);
						
						
						
						//adding archElement to the feature
						addArchElementToFeature(model.getSelectedFeature(),id);
						
						
						
					} catch (Exception e) {
						
						e.printStackTrace();
					} catch (TokenMgrError e) {
						
						e.printStackTrace();
					}

				}
				
				AS.xarch.add(archStructureRef, "component", componentRef);
				
			}
			
			public ImageDescriptor getImageDescriptor(){
				return AS.resources.getImageDescriptor(ArchstudioResources.ICON_COMPONENT);
			}
		};
		
		
		
		///varun
		Action newOptionalConnectorAction = new Action("New Connector"){
			public void run(){
				Shell shell = AS.editor.getParentComposite().getShell();
				ObjRef typeRef = XadlSelectorDialog.showSelectorDialog(shell, "Select Connector Type", AS.xarch, AS.resources, xArchRef, XadlTreeUtils.CONNECTOR_TYPE, XadlTreeUtils.CONNECTOR_TYPE);
				if (typeRef == null){
					//User canceled the selection.
					return;
				}
				//XadlUtils.setXLink(AS.xarch, statechartRef, "linkedComp", componentRef);
				//XadlUtils.setDescription(AS.xarch, statechartRef, XadlUtils.getDescription(AS.xarch, componentRef));

				
				ObjRef typesContextRef = AS.xarch.createContext(xArchRef, "types");
				ObjRef connectorRef = AS.xarch.create(typesContextRef, "connector");
				String id = UIDGenerator.generateUID("connector");
				AS.xarch.set(connectorRef, "id", id);
				
				String typeID = XadlUtils.getID(AS.xarch, typeRef);
				if(typeID != null){
					XadlUtils.setXLink(AS.xarch, connectorRef, "type", typeID);
				}
				XadlUtils.setDescription(AS.xarch, connectorRef, XadlUtils.getDescription(AS.xarch, typeRef));
				
				ObjRef[] signatureRefs = AS.xarch.getAll(typeRef, "signature");
				if(!(signatureRefs == null || signatureRefs.length == 0)){
					for(ObjRef element: signatureRefs){
						//For each signature of the selected connector type: 
						//  create a corresponding interface to the new connector;
						//  set the direction, signature, and description of the new interface.
						String signatureID = XadlUtils.getID(AS.xarch, element);
						String dir = XadlUtils.getDirection(AS.xarch, element);
						//String dir = (String)AS.xarch.get(element,"direction");
						String description = XadlUtils.getDescription(AS.xarch, element);
						
						ObjRef interfaceRef = AS.xarch.create(typesContextRef, "interface");
						AS.xarch.set(interfaceRef, "id", UIDGenerator.generateUID("interface"));
						XadlUtils.setDescription(AS.xarch, interfaceRef, description);
						XadlUtils.setDirection(AS.xarch, interfaceRef, dir);
						XadlUtils.setXLink(AS.xarch, interfaceRef, "signature", signatureID);
						AS.xarch.add(connectorRef, "interface", interfaceRef);
					}
				}				
				
				AS.xarch.add(archStructureRef, "connector", connectorRef);
				
				
				BNAComposite composit = ArchipelagoUtils.getBNAComposite(AS.editor);
				if(composit == null){
					return;
				}
				if(composit.getView() == null){
					return;
				}
				
				if(composit.getView().getWorld() == null){
					return;
				}
				if(composit.getView().getWorld().getBNAModel() == null){
					return;
				}
				
				IBNAModel model = composit.getView().getWorld().getBNAModel();
				
				
				
				
				
				
				if(model.getSelectedFeature()!=null){
					
					ObjRef featureReff = AS.xarch.getByID(model.getSelectedFeature());
					if( AS.xarch.isInstanceOf(featureReff, "features#AlternativeFeature") && !isOptionalAlternative(featureReff)){
						return;
					}
					//promote to optional
					ObjRef optionsContext = AS.xarch.createContext(xArchRef, "options");
					String promotedTypeName = getPromotedTypeName(connectorRef);
					if(promotedTypeName != null){
						AS.xarch.promoteTo(optionsContext, promotedTypeName, connectorRef);
					}
					//getGuardParentRef
					ObjRef optionsContextRef = AS.xarch.createContext(xArchRef, "options");
					ObjRef optionalRef = AS.xarch.create(optionsContextRef, "optional");
					AS.xarch.set(connectorRef, "optional", optionalRef);
		
					
					
					ObjRef guardRef = null;
					try {
						
						String name = model.getSelectedFeature();
						String featureSymbol =name.replaceAll("-", "");
						ObjRef featureRef = AS.xarch.getByID(name);
						if(AS.xarch.isInstanceOf(featureRef, "features#Varient")){
							ObjRef varients = AS.xarch.getParent(featureRef);
							ObjRef feature = AS.xarch.getParent(varients);
							String featureVal = (String)AS.xarch.get(feature, "id");
							featureVal = featureVal.replaceAll("-", "");
							guardRef = AS.booleanNotation.parseBooleanGuard(featureVal + " == \""+featureSymbol+"\"", xArchRef);
							
							
						}else if(AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature")){
							
							guardRef = AS.booleanNotation.parseBooleanGuard(featureSymbol + " == \"true\"", xArchRef);
						}else if(AS.xarch.isInstanceOf(featureRef, "features#AlternativeFeature") && isOptionalAlternative(featureRef)){
							guardRef = AS.booleanNotation.parseBooleanGuard(featureSymbol + " != \"false\"", xArchRef);
						}
						if(guardRef!=null){
						
						AS.xarch.set(optionalRef, "guard", guardRef);
						/*String expr = AS.booleanNotation.booleanGuardToString(optionalRef);
						String expr2 = "connector == true";
						guardRef = AS.booleanNotation.parseBooleanGuard("("+expr + ") || ("+expr2+")", xArchRef);
						AS.xarch.set(optionalRef, "guard", guardRef);*/
						}
						
						//adding archElement to the feature
						addArchElementToFeature(model.getSelectedFeature(),id);
						
					} catch (Exception e) {
						
						e.printStackTrace();
					} catch (TokenMgrError e) {
						
						e.printStackTrace();
					}
					
				}


			}

			public ImageDescriptor getImageDescriptor(){
				return AS.resources.getImageDescriptor(ArchstudioResources.ICON_CONNECTOR);
			}
		};
		
		
		
		
		
		//varun
		Action newOptionalLinkAction = new Action("New Link"){
			public void run(){

				
				BNAComposite composit = ArchipelagoUtils.getBNAComposite(AS.editor);
				if(composit == null){
					return;
				}
				if(composit.getView() == null){
					return;
				}
				
				if(composit.getView().getWorld() == null){
					return;
				}
				if(composit.getView().getWorld().getBNAModel() == null){
					return;
				}
				
				IBNAModel model = composit.getView().getWorld().getBNAModel();
				
				
				ObjRef typesContextRef = AS.xarch.createContext(xArchRef, "types");
				ObjRef linkRef = AS.xarch.create(typesContextRef, "link");
				String id = UIDGenerator.generateUID("link");
				AS.xarch.set(linkRef, "id", id);
				XadlUtils.setDescription(AS.xarch, linkRef, "[New Link]");
				AS.xarch.add(archStructureRef, "link", linkRef);

				boolean alternativeFlag = false;
				String selectedFeature = model.getSelectedFeature();
				if(selectedFeature !=null){
					ObjRef featureRef = AS.xarch.getByID(model.getSelectedFeature());
					alternativeFlag =( AS.xarch.isInstanceOf(featureRef, "features#AlternativeFeature") && !isOptionalAlternative(featureRef));
				}
				
				
				if(model.getSelectedFeature() == null || alternativeFlag ){
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
					//Abnormal: no change session is set in the environment
					return;
				}
				//record changes
				ObjRef changesContextRef = AS.xarch.createContext(xArchRef, "changes");
				ObjRef linkChangeRef = AS.xarch.create(changesContextRef, "linkChange");
				AS.xarch.set(linkChangeRef, "id", UIDGenerator.generateUID("linkChange"));
				AS.xarch.set(linkChangeRef, "type", "add");
				XadlUtils.setDescription(AS.xarch, linkChangeRef, "New Link");
				XadlUtils.setXLink(AS.xarch, linkChangeRef, "link", linkRef);
				AS.xarch.add(changesRef, "linkChange", linkChangeRef);
				
				}else{//add options if feature is selected
				

					
					//promote to optional
					ObjRef optionsContext = AS.xarch.createContext(xArchRef, "options");
					String promotedTypeName = getPromotedTypeName(linkRef);
					if(promotedTypeName != null){
						AS.xarch.promoteTo(optionsContext, promotedTypeName, linkRef);
					}
					//getGuardParentRef
					ObjRef optionsContextRef = AS.xarch.createContext(xArchRef, "options");
					ObjRef optionalRef = AS.xarch.create(optionsContextRef, "optional");
					AS.xarch.set(linkRef, "optional", optionalRef);
		
					
					
					ObjRef guardRef = null;
					try {
						
						String name = model.getSelectedFeature();
						String featureSymbol =name.replaceAll("-", "");
						ObjRef featureRef = AS.xarch.getByID(name);
						if(AS.xarch.isInstanceOf(featureRef, "features#Varient")){
							ObjRef varients = AS.xarch.getParent(featureRef);
							ObjRef feature = AS.xarch.getParent(varients);
							String featureVal = (String)AS.xarch.get(feature, "id");
							featureVal = featureVal.replaceAll("-", "");
							guardRef = AS.booleanNotation.parseBooleanGuard(featureVal + " == \""+featureSymbol+"\"", xArchRef);
						}else if(AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature")){
							
							guardRef = AS.booleanNotation.parseBooleanGuard(featureSymbol + " == \"true\"", xArchRef);
						}else if(AS.xarch.isInstanceOf(featureRef, "features#AlternativeFeature") && isOptionalAlternative(featureRef)){
							
							guardRef = AS.booleanNotation.parseBooleanGuard(featureSymbol + " != \"false\"", xArchRef);
						}
						if(guardRef!=null)
						AS.xarch.set(optionalRef, "guard", guardRef);
						
						
						
						//adding archElement to the feature
						addArchElementToFeature(model.getSelectedFeature(),id);
						
						
						
					} catch (Exception e) {
						
						e.printStackTrace();
					} catch (TokenMgrError e) {
						
						e.printStackTrace();
					}
					
				
				}
				
			
			}
			
			public ImageDescriptor getImageDescriptor(){
				return AS.resources.getImageDescriptor(ArchstudioResources.ICON_LINK);
			}
		};
		
		return new IAction[]{/*newComponentAction,*/newOptionalComponentAction, /*newConnectorAction*/newOptionalConnectorAction, /*newLinkAction*/newOptionalLinkAction};
	}

	protected void addArchElementToFeature(String selectedFeature,String id) {

		//New Implementation.
		ObjRef featureRef = AS.xarch.getByID(selectedFeature);
		if(AS.xarch.isInstanceOf(featureRef, "features#Varient")){
			ObjRef featureElementsRef = (ObjRef) AS.xarch.get(featureRef, "featureElements");
			ObjRef instanceContextRef = AS.xarch.createContext(xArchRef, "instance");
			ObjRef XMLLinkRef = AS.xarch.create(instanceContextRef, "XMLLink");
			AS.xarch.set(XMLLinkRef, "href", "#"+id);
			AS.xarch.set(XMLLinkRef, "type", "simple");
			AS.xarch.add(featureElementsRef, "archElement", XMLLinkRef);
		}else if(AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature")){
			ObjRef featureElementsRef = (ObjRef) AS.xarch.get(featureRef, "featureElements");
			ObjRef instanceContextRef = AS.xarch.createContext(xArchRef, "instance");
			ObjRef XMLLinkRef = AS.xarch.create(instanceContextRef, "XMLLink");
			AS.xarch.set(XMLLinkRef, "href", "#"+id);
			AS.xarch.set(XMLLinkRef, "type", "simple");
			AS.xarch.add(featureElementsRef, "archElement", XMLLinkRef);
		}else if(AS.xarch.isInstanceOf(featureRef, "features#AlternativeFeature") && isOptionalAlternative(featureRef)){
			ObjRef featureElementsRef = (ObjRef) AS.xarch.get(featureRef, "featureElements");
			ObjRef instanceContextRef = AS.xarch.createContext(xArchRef, "instance");
			ObjRef XMLLinkRef = AS.xarch.create(instanceContextRef, "XMLLink");
			AS.xarch.set(XMLLinkRef, "href", "#"+id);
			AS.xarch.set(XMLLinkRef, "type", "simple");
			AS.xarch.add(featureElementsRef, "archElement", XMLLinkRef);
		}
		//ObjRef objRef = AS.xarch.getByID(id);
		//XadlUtils.setXLink(AS.xarch, featureRef, "archElement", objRef);
		
		
		
		
	
		
	}
	
	protected boolean isOptionalAlternative(ObjRef featureRef) {

		ObjRef typeRef = (ObjRef) AS.xarch.get(featureRef, "type");
		String value = (String) AS.xarch.get(typeRef, "value");
		if(value.equals("optionalAlternative")){
			return true;
		}else{
			return false;
		}
	}

	protected String getPromotedTypeName(ObjRef ref) {
		if(AS.xarch.isInstanceOf(ref, "types#Component")){
			return "optionalComponent";
		}
		else if(AS.xarch.isInstanceOf(ref, "types#Connector")){
			return "optionalConnector";
		}
		else if(AS.xarch.isInstanceOf(ref, "types#Interface")){
			return "optionalInterface";
		}
		else if(AS.xarch.isInstanceOf(ref, "types#Link")){
			return "optionalLink";
		}
		else if(AS.xarch.isInstanceOf(ref, "types#Signature")){
			return "optionalSignature";
		}
		else if(AS.xarch.isInstanceOf(ref, "types#SignatureInterfaceMapping")){
			return "optionalSignatureInterfaceMapping";
		}
		return null;
	}
	
}
