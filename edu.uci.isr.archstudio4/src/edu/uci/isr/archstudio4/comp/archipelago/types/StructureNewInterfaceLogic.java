package edu.uci.isr.archstudio4.comp.archipelago.types;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchActionConstants;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoUtils;
import edu.uci.isr.archstudio4.comp.booleannotation.ParseException;
import edu.uci.isr.archstudio4.comp.booleannotation.TokenMgrError;
import edu.uci.isr.archstudio4.util.ArchstudioResources;
import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAComposite;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAMenuListener;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.things.glass.BoxGlassThing;
import edu.uci.isr.bna4.things.swt.SWTTextThing;
import edu.uci.isr.bna4.things.utility.EnvironmentPropertiesThing;
import edu.uci.isr.sysutils.UIDGenerator;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.InvalidOperationException;
import edu.uci.isr.xarchflat.ObjRef;

public class StructureNewInterfaceLogic extends AbstractThingLogic implements IBNAMenuListener{
	protected ArchipelagoServices AS = null;
	protected ObjRef xArchRef = null;
	private String[] result;
	protected List<SWTTextThing> openControls = Collections.synchronizedList(new ArrayList<SWTTextThing>());
	
	public StructureNewInterfaceLogic(ArchipelagoServices services, ObjRef xArchRef){
		this.AS = services;
		this.xArchRef = xArchRef;
	}
	
	public boolean matches(IBNAView view, IThing t){
		if(t instanceof BoxGlassThing){
			IThing pt = view.getWorld().getBNAModel().getParentThing(t);
			if(pt != null){
				return StructureMapper.isBrickAssemblyRootThing(pt);
			}
		}
		return false;
	}
	
	public String getXArchID(IBNAView view, IThing t){
		if(t instanceof BoxGlassThing){
			IThing parentThing = view.getWorld().getBNAModel().getParentThing(t);
			return parentThing.getProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME);
		}
		return null;
	}
	
	public void fillMenu(IBNAView view, IMenuManager m, int localX, int localY, IThing t, int worldX, int worldY){
		IThing[] selectedThings = BNAUtils.getSelectedThings(view.getWorld().getBNAModel());
		if(selectedThings.length > 1) return;

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
		
		final String eltXArchID = getXArchID(view, t);
		if(eltXArchID == null){
			//Nothing to set description on
			return new IAction[0];
		}
		
		final ObjRef eltRef = AS.xarch.getByID(xArchRef, eltXArchID);
		if(eltRef == null){
			//Nothing to set description on
			return new IAction[0];
		}
		
		//To record changes
		final EnvironmentPropertiesThing ept = BNAUtils.getEnvironmentPropertiesThing(view.getWorld().getBNAModel());		
		
		Action newInterfaceAction = new Action("New Interface", AS.resources.getImageDescriptor(ArchstudioResources.ICON_INTERFACE)){
			public void run(){
				ObjRef typesContextRef = AS.xarch.createContext(xArchRef, "types");
				ObjRef interfaceRef = AS.xarch.create(typesContextRef, "interface");
				AS.xarch.set(interfaceRef, "id", UIDGenerator.generateUID("interface"));
				XadlUtils.setDescription(AS.xarch, interfaceRef, "[New Interface]");
				XadlUtils.setDirection(AS.xarch, interfaceRef, "none");
				AS.xarch.add(eltRef, "interface", interfaceRef);
				
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
				//recording changes
				ObjRef changesContextRef = AS.xarch.createContext(xArchRef, "changes");
				ObjRef compChangeRef = AS.xarch.create(changesContextRef, "componentChange");
				AS.xarch.set(compChangeRef, "id", UIDGenerator.generateUID("componentChange"));
				AS.xarch.set(compChangeRef, "type", "update");
				XadlUtils.setDescription(AS.xarch, compChangeRef, "New interface");
				XadlUtils.setXLink(AS.xarch, compChangeRef, "component", eltRef);
				//Add interface change - START
				ObjRef intfChangeRef = AS.xarch.create(changesContextRef, "interfaceChange");
				AS.xarch.set(intfChangeRef, "id", UIDGenerator.generateUID("interfaceChange"));
				AS.xarch.set(intfChangeRef, "type", "add");
				XadlUtils.setXLink(AS.xarch, intfChangeRef, "interface", interfaceRef);
				AS.xarch.set(compChangeRef, "interfaceChange", intfChangeRef);
				//Add interface change - END
				AS.xarch.add(changesRef, "componentChange", compChangeRef);				
			}
		};
		
		Action propertyAction = new Action("Property",AS.resources.getImageDescriptor(ArchstudioResources.ICON_TYPES)){
			
			@Override
			public void run() {
//				super.run();
				
				Shell shell = AS.editor.getParentComposite().getShell();
				ObjRef typesContextRef = AS.xarch.createContext(xArchRef, "types"); 
				ObjRef list = (ObjRef)AS.xarch.get(eltRef,"propertyGroup");
				if(list==null){
					list = AS.xarch.create(typesContextRef, "propertyGroup");
					String changeID = UIDGenerator.generateUID("propertyGroup");				
					AS.xarch.set(list, "id", changeID);
					
					AS.xarch.set(eltRef, "propertyGroup", list);
					//System.out.println("success");
				}
				displayTable(shell,eltRef,list);
				
				
			}
		};
		
		
		
		
		
		//Action 
		Action newOptionalInterfaceAction = new Action("New Interface", AS.resources.getImageDescriptor(ArchstudioResources.ICON_INTERFACE)){
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
				ObjRef interfaceRef = AS.xarch.create(typesContextRef, "interface");
				String id = UIDGenerator.generateUID("interface");
				AS.xarch.set(interfaceRef, "id", id);
				XadlUtils.setDescription(AS.xarch, interfaceRef, "[New Interface]");
				XadlUtils.setDirection(AS.xarch, interfaceRef, "none");
				AS.xarch.add(eltRef, "interface", interfaceRef);
				String selectedFeature = model.getSelectedFeature();
				boolean alternativeFlag = false;
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
					//recording changes
					ObjRef changesContextRef = AS.xarch.createContext(xArchRef, "changes");
					ObjRef compChangeRef = AS.xarch.create(changesContextRef, "componentChange");
					AS.xarch.set(compChangeRef, "id", UIDGenerator.generateUID("componentChange"));
					AS.xarch.set(compChangeRef, "type", "update");
					XadlUtils.setDescription(AS.xarch, compChangeRef, "New interface");
					XadlUtils.setXLink(AS.xarch, compChangeRef, "component", eltRef);
					//Add interface change - START
					ObjRef intfChangeRef = AS.xarch.create(changesContextRef, "interfaceChange");
					AS.xarch.set(intfChangeRef, "id", UIDGenerator.generateUID("interfaceChange"));
					AS.xarch.set(intfChangeRef, "type", "add");
					XadlUtils.setXLink(AS.xarch, intfChangeRef, "interface", interfaceRef);
					AS.xarch.set(compChangeRef, "interfaceChange", intfChangeRef);
					//Add interface change - END
					AS.xarch.add(changesRef, "componentChange", compChangeRef);	
				}else{
					//add options if feature is selected	
					String compID = XadlUtils.getID(AS.xarch, eltRef);
					ObjRef featureRef = AS.xarch.getByID(model.getSelectedFeature());
					
					if(AS.xarch.isInstanceOf(featureRef, "features#Varient") || AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature") ){
						ObjRef featureElementsRef = (ObjRef) AS.xarch.get(featureRef, "featureElements");
						
						if(featureElementsRef != null){


							ObjRef[] linkRefs = AS.xarch.getAll(featureElementsRef, "archElement");


							for (int i = 0; i < linkRefs.length; i++) {
								ObjRef link = linkRefs[i];
								String href = (String) AS.xarch.get(link, "href");
								if(compID.equals(href.substring(1))){ //exit if the parent already has same guard condition
									return; 	
								}

							}
						}
					}else if(AS.xarch.isInstanceOf(featureRef, "features#AlternativeFeature")  && isOptionalAlternative(featureRef)){ 

						ObjRef featureElementsRef = (ObjRef) AS.xarch.get(featureRef, "featureElements");
						
						if(featureElementsRef != null){


							ObjRef[] linkRefs = AS.xarch.getAll(featureElementsRef, "archElement");


							for (int i = 0; i < linkRefs.length; i++) {
								ObjRef link = linkRefs[i];
								String href = (String) AS.xarch.get(link, "href");
								if(compID.equals(href.substring(1))){ //exit if the parent already has same guard condition
									return; 	
								}

							}
						}
					
					}
					else{
						return;
					}
					
					
					
					//promote to optional
					ObjRef optionsContext = AS.xarch.createContext(xArchRef, "options");
					String promotedTypeName = "optionalInterface";
					if(promotedTypeName != null){
						AS.xarch.promoteTo(optionsContext, promotedTypeName, interfaceRef);
					}
					//getGuardParentRef
					ObjRef optionsContextRef = AS.xarch.createContext(xArchRef, "options");
					ObjRef optionalRef = AS.xarch.create(optionsContextRef, "optional");
					AS.xarch.set(interfaceRef, "optional", optionalRef);
		
					
					
					ObjRef guardRef = null;
					try {
						
						String name = model.getSelectedFeature();
						String featureValue =name.replaceAll("-", "");
						String expr2 = selectedFeature.replaceAll("-", "")+" == \"true\"";
						
						if(AS.xarch.isInstanceOf(featureRef, "features#Varient")){
							ObjRef varients = AS.xarch.getParent(featureRef);
							ObjRef feature = AS.xarch.getParent(varients);
							String featureSymbol = (String)AS.xarch.get(feature, "id");
							featureSymbol = featureSymbol.replaceAll("-", "");
							expr2 = featureSymbol + " == \""+featureValue+"\"";
							guardRef = AS.booleanNotation.parseBooleanGuard(expr2, xArchRef);
						}else if(AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature")){
							expr2 = featureValue + " == \"true\"";
							guardRef = AS.booleanNotation.parseBooleanGuard(expr2, xArchRef);
						}else if(AS.xarch.isInstanceOf(featureRef, "features#AlternativeFeature") && isOptionalAlternative(featureRef)){
							expr2 = featureValue + " != \"false\"";
							guardRef = AS.booleanNotation.parseBooleanGuard(expr2, xArchRef);
							//guardRef = AS.booleanNotation.parseBooleanGuard(featureSymbol + " != \"false\"", xArchRef);
						}
						if(guardRef!=null)
						AS.xarch.set(optionalRef, "guard", guardRef);
						
						
						//adding archElement to the feature
						addArchElementToFeature(model.getSelectedFeature(),id);
						
						
						ObjRef parent = eltRef;
						
						if(isPossiblyOptional(parent)){
							//FIXME need to add OR guard with parent 
							ObjRef parentOptionalRef = (ObjRef)AS.xarch.get(parent, "optional");
							String parentExpr = AS.booleanNotation.booleanGuardToString(parentOptionalRef);
							//String selectedFeature = getCurrentFeature();
							//String expr2 = selectedFeature.replaceAll("-", "")+" == \"true\"";
							//ObjRef guardRef;
							try {
								guardRef = AS.booleanNotation.parseBooleanGuard("("+parentExpr + ") || ("+expr2+")", xArchRef);
								AS.xarch.set(parentOptionalRef, "guard", guardRef);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (TokenMgrError e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
							
							//FIXME add parent to the current feature List
							//add to existing feature list
							addArchElementToFeature(selectedFeature,XadlUtils.getID(AS.xarch, parent) );
						}
						
						
					} catch (Exception e) {
						
						e.printStackTrace();
					} catch (TokenMgrError e) {
						
						e.printStackTrace();
					}
					
				
				}
			}
		};
		
		
		
		return new IAction[]{/*newInterfaceAction*/newOptionalInterfaceAction,/*newProperty,*/propertyAction};
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

	public boolean isPossiblyOptional(ObjRef ref){
		try{
			ObjRef optionalRef = (ObjRef)AS.xarch.get(ref, "optional");
			return true;
		}
		catch(InvalidOperationException ioe){
			//ioe.printStackTrace();
			return false;
		}
	}

	


	
	protected void displayTable(Shell parentShell, final ObjRef compRef, final ObjRef list) {
		
		final Shell shell = new Shell(parentShell, SWT.RESIZE | SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		shell.setLayout(new GridLayout(1,true));
		final Table table = new Table (shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		
		
		
		
		ObjRef[] props = AS.xarch.getAll(list, "property");
		final ArrayList<ObjRef> propertyList = new ArrayList<ObjRef>();
		
		
		
		Composite cButtons = new Composite(shell, SWT.NONE);
		GridLayout cButtonsLayout = new GridLayout(3, false);
		cButtonsLayout.horizontalSpacing = 5;
		cButtonsLayout.marginTop = 5;
		cButtonsLayout.marginBottom = 5;
		cButtonsLayout.marginLeft = 5;
		cButtonsLayout.marginRight = 5;
		cButtons.setLayout(cButtonsLayout);

		cButtons.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));

		Button add = new Button(cButtons, SWT.PUSH);
		
		Button delete = new Button(cButtons, SWT.PUSH);
		
		Button edit = new Button(cButtons,SWT.PUSH);
		
		
		
		//Button add = new Button(shell, SWT.PUSH);
		add.setText("Add");
		delete.setText("Delete");
		edit.setText("Edit");
		
		
		
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);
		String[] titles = {"S.No ", " Name ", " Type ", " Value "};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}
		Menu menu = new Menu (shell, SWT.POP_UP);
		table.setMenu (menu);
		MenuItem menuitem = new MenuItem (menu, SWT.PUSH);
		menuitem.setText ("Delete Selection");
		menuitem.addListener (SWT.Selection, new Listener () {
			
			public void handleEvent (Event event) {
				
				//System.out.println(table.getSelectionIndex());
				int id  = table.getSelectionIndex();
				if(id == -1){
					return;
				}
				
				ObjRef prop = propertyList.get(id);
				AS.xarch.remove(list,"property",prop);
				
				propertyList.remove(id);
				table.remove (id);
				
			}
		});
		
		delete.addListener(SWT.Selection, new Listener() {
			
			
			public void handleEvent(Event event) {
			
				//System.out.println(table.getSelectionIndex());
				int id  = table.getSelectionIndex();
				if(id == -1){
					return;
				}
				
				ObjRef prop = propertyList.get(id);
				AS.xarch.remove(list,"property",prop);
				propertyList.remove(id);
				table.remove (id);
				
			}
		});
		
		edit.addListener(SWT.Selection, new Listener(){

			
			public void handleEvent(Event event) {
				
				int id  = table.getSelectionIndex();
				if(id == -1){
					return;
				}
				
				ObjRef prop = propertyList.get(id);
				
				String type = AS.xarch.get(prop, "type").toString();
				String[] resl = null;
				if(type.equals("varient")){
					
					resl  = showVarientDialog(shell,prop,compRef,false);
				}else{
					resl  = showDialog(shell,prop,false);	
				}
				
				
				if(resl == null){
					return;
				}
			
				
				
				
				AS.xarch.set(prop, "name", resl[0]);
				AS.xarch.set(prop, "type", resl[1]);
				AS.xarch.set(prop, "value", resl[2]);
				
				TableItem item = table.getItem(id);
//				item.setText (1, AS.xarch.get(prop, "name").toString());
				item.setText (1, resl[0]);
				item.setText (2, resl[1]);
				item.setText (3, resl[2]);
			}
			
			
		});
		add.addListener(SWT.Selection, new Listener() {
			
			
			public void handleEvent(Event event) {
				
				
				String[] resl  = showDialog(shell,null,false);
				
				if(resl == null){
					return;
				}
				//System.out.println("name ->"+resl[0]);
				//System.out.println("type ->"+resl[1]);
				//System.out.println("value ->"+resl[2]);
				
				
				ObjRef typesContextRef = AS.xarch.createContext(xArchRef, "types");
				
				ObjRef property = AS.xarch.create(typesContextRef, "property");
				
				AS.xarch.set(property, "name", resl[0]);
				AS.xarch.set(property, "type", resl[1]);
				AS.xarch.set(property, "value", resl[2]);
				
				AS.xarch.add(list,"property",property);
				
				
				propertyList.add(property);
				
				TableItem item = new TableItem (table, SWT.NONE);
				item.setText (0, propertyList.size()+"");
				item.setText (1, resl[0]);
				item.setText (2, resl[1]);
				item.setText (3, resl[2]);
				
				
			}
		});
		
		boolean varient = false;
		for (int i = 0; i < props.length; i++) {
			//System.out.println(AS.xarch.get(props[i%props.length], "name"));	
			propertyList.add(props[i]);
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (0, i+"");
			item.setText (1, AS.xarch.get(props[i%props.length], "name").toString());
			item.setText (2, AS.xarch.get(props[i%props.length], "type").toString());
			item.setText (3, AS.xarch.get(props[i%props.length], "value").toString());
			
			if(varient || AS.xarch.get(props[i%props.length], "type").toString().equals("varient")){
				
				varient = true;
			}
			
		}
		
		if(!varient){
			ObjRef typesContextRef = AS.xarch.createContext(xArchRef, "types"); 
			ObjRef varientGroupRef = (ObjRef)AS.xarch.get(compRef,"varientGroup");
			if(varientGroupRef==null){
				varientGroupRef = AS.xarch.create(typesContextRef, "varientGroup");
				String changeID = UIDGenerator.generateUID("varientGroup");				
				AS.xarch.set(varientGroupRef, "id", changeID);
				
				AS.xarch.set(compRef, "varientGroup", varientGroupRef);
				
				//System.out.println("success");
			}
			ObjRef[] varients = AS.xarch.getAll(varientGroupRef, "varientItem");
			if(varients.length !=0){
			String val =  AS.xarch.get(varients[0], "name").toString();//;
			ObjRef property = AS.xarch.create(typesContextRef, "property");
			
			AS.xarch.set(property, "name", "Name");
			AS.xarch.set(property, "type", "varient");
			AS.xarch.set(property, "value",val );
			
			AS.xarch.add(list,"property",property);
			
			
			propertyList.add(property);
			
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (0, propertyList.size()+"");
			item.setText (1, "Name");
			item.setText (2, "varient");
			item.setText (3, val);
			}
			
		}
		
		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}	
		shell.pack ();
		shell.open ();
		while(!shell.isDisposed()){
			if(!shell.getDisplay().readAndDispatch()){
				shell.getDisplay().sleep();
			}
		}
		
		
		
	}
	
	protected String[] showVarientDialog(Shell parentShell,ObjRef property,ObjRef eltRef,boolean isVarient){

		
		final Shell dialog = new Shell(parentShell, SWT.RESIZE | SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		
		dialog.setText("New Property");
		
		 GridLayout gridLayout = new GridLayout(3, false);
		 gridLayout.horizontalSpacing = 5;
		 gridLayout.marginTop = 5;
		 gridLayout.marginBottom = 5;
		 gridLayout.marginLeft = 5;
		 gridLayout.marginRight = 5;
		 dialog.setLayout(gridLayout);

		  Label  labelUser = new Label(dialog, SWT.NULL);

		    GridData gridData = new GridData(GridData.CENTER);
		    
//		    gridData.grabExcessHorizontalSpace = true;
		    Label textUser = new Label(dialog, SWT.NULL);
		    
		    textUser.setLayoutData(gridData);
		    
		    Label val =  new Label(dialog, SWT.NULL);

		    labelUser.setText("Name");
		    textUser.setText("Type");
		    val.setText("value");
		    
		  

		    // 2nd row.
		    final Text  nameText = new Text(dialog, SWT.NULL);
		    
		    
		   /* final Combo typeCombo = new Combo(dialog, SWT.DROP_DOWN | SWT.READ_ONLY);
		    String[] ITEMS = {"String","Number","Boolean"};
		    typeCombo.setItems(ITEMS);*/
		    final Text typeText = new Text(dialog, SWT.NULL);
		    
		    typeText.setText("varient");
		    
		    typeText.setEditable(false);
		    
		    
		    
		   /* gridData = new GridData(GridData.CENTER);
//		    gridData.grabExcessHorizontalSpace = true;
		    
		    final Text ValueText = new Text(dialog, SWT.SINGLE | SWT.BORDER);
		    ValueText.setLayoutData(gridData);*/
		    
		    final Combo ValueText = new Combo(dialog, SWT.DROP_DOWN | SWT.READ_ONLY);
		    ObjRef varientGroupRef = (ObjRef)AS.xarch.get(eltRef,"varientGroup");
			if(varientGroupRef==null){
				ObjRef typesContextRef = AS.xarch.createContext(xArchRef, "types");
				varientGroupRef = AS.xarch.create(typesContextRef, "varientGroup");
				String changeID = UIDGenerator.generateUID("varientGroup");				
				AS.xarch.set(varientGroupRef, "id", changeID);
				
				AS.xarch.set(eltRef, "varientGroup", varientGroupRef);
				//System.out.println("success");
			}
			
			ObjRef[] varients = AS.xarch.getAll(varientGroupRef, "varientItem");
			String[] varietsType  = new String[varients.length];
			for (int i = 0 ; i < varients.length; i++) {
				varietsType[i] = AS.xarch.get(varients[i], "name").toString();	
			}
			
//		    String[] ITEMS = {"String","Number","Boolean"};
		    ValueText.setItems(varietsType);
		    
		    //final row
		    
		    new Label(dialog, SWT.NULL);
		    
		    Button bOK = new Button(dialog, SWT.PUSH);
			bOK.setText("OK");
			
			Button bCancel = new Button(dialog, SWT.PUSH);
			bCancel.setText("Cancel");
			Listener okListener = new Listener(){
				public void handleEvent(Event event) {
					
					result = new String[]{nameText.getText(),typeText.getText(),ValueText.getText()};
					String validate = validate(result);  
					if(validate.equals("ok")){
						dialog.close();	
					}else{
						//System.out.println(validate);
						result = null;
						MessageDialog msgBox = new MessageDialog(dialog, "Error", null,
								validate, MessageDialog.ERROR, new String[] { "OK" }, 0);
							int result = msgBox.open();
							//System.out.println(result); 
						//dialog.close();
					}
					
					
					
					
					
				}

				private String validate(String[] result) {
				
					if(result[0] == null || result[1] == null || result[2] == null){
						return "All the fields should by filled";
					}
					
					if(result[0].trim().equals("") || result[1].trim().equals("") || result[2].trim().equals("")){
						return "fields cannot have blank strings";
					}
					
					if(result[1].equals("String")){
						
					}else if (result[1].equals("Number")){
						//"-?\\d+(\\.\\d+)?" 
						if( result[2].matches("\\d") ){
							
						}else{
							return "value of number type should be a Number";
						}
					}else if (result[1].equals("Boolean")){
						if(result[2].equals("true") || result[2].equals("false")){
							
						}else{
							return "Value of boolean type should be either true or false";
						}
					}
					
					
					return "ok";
				};
			};
			Listener cancelListener = new Listener(){

				public void handleEvent(Event event){
					result = null;
					dialog.close();
				}
			};
			bOK.addListener(SWT.Selection, okListener);
			bCancel.addListener(SWT.Selection, cancelListener);
			
			
			if(property != null){
				nameText.setText(AS.xarch.get(property, "name").toString());
				ValueText.setText(AS.xarch.get(property, "value").toString());
				typeText.setText(AS.xarch.get(property, "type").toString());
			}
		   
		
		    
		    dialog.pack();
			dialog.open();

			while(!dialog.isDisposed()){
				if(!dialog.getDisplay().readAndDispatch()){
					dialog.getDisplay().sleep();
				}
			}
		
			return result;
	
	}

	protected String[] showDialog(Shell parentShell,ObjRef property,boolean isVarient) {
		
		final Shell dialog = new Shell(parentShell, SWT.RESIZE | SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		
		dialog.setText("New Property");
		
		 GridLayout gridLayout = new GridLayout(3, false);
		 gridLayout.horizontalSpacing = 5;
		 gridLayout.marginTop = 5;
		 gridLayout.marginBottom = 5;
		 gridLayout.marginLeft = 5;
		 gridLayout.marginRight = 5;
		 dialog.setLayout(gridLayout);

		  Label  labelUser = new Label(dialog, SWT.NULL);

		    GridData gridData = new GridData(GridData.CENTER);
		    
//		    gridData.grabExcessHorizontalSpace = true;
		    Label textUser = new Label(dialog, SWT.NULL);
		    
		    textUser.setLayoutData(gridData);
		    
		    Label val =  new Label(dialog, SWT.NULL);

		    labelUser.setText("Name");
		    textUser.setText("Type");
		    val.setText("value");
		    
		  

		    // 2nd row.
		    final Text  nameText = new Text(dialog, SWT.NULL);
		    
		    
		    final Combo typeCombo = new Combo(dialog, SWT.DROP_DOWN | SWT.READ_ONLY);
		    String[] ITEMS = {"String","Number","Boolean"};
		    typeCombo.setItems(ITEMS);
		    
		    
		    
		    gridData = new GridData(GridData.CENTER);
//		    gridData.grabExcessHorizontalSpace = true;
		    
		    final Text ValueText = new Text(dialog, SWT.SINGLE | SWT.BORDER);
		    ValueText.setLayoutData(gridData);
		    
		    //final row
		    
		    new Label(dialog, SWT.NULL);
		    
		    Button bOK = new Button(dialog, SWT.PUSH);
			bOK.setText("OK");
			
			Button bCancel = new Button(dialog, SWT.PUSH);
			bCancel.setText("Cancel");
			Listener okListener = new Listener(){
				public void handleEvent(Event event) {
					
					result = new String[]{nameText.getText(),typeCombo.getText(),ValueText.getText()};
					String validate = validate(result);  
					if(validate.equals("ok")){
						dialog.close();	
					}else{
						//System.out.println(validate);
						result = null;
						MessageDialog msgBox = new MessageDialog(dialog, "Error", null,
								validate, MessageDialog.ERROR, new String[] { "OK" }, 0);
							int result = msgBox.open();
							//System.out.println(result); 
						//dialog.close();
					}
					
					
					
					
					
				}

				private String validate(String[] result) {
				
					if(result[0] == null || result[1] == null || result[2] == null){
						return "All the fields should by filled";
					}
					
					if(result[0].trim().equals("") || result[1].trim().equals("") || result[2].trim().equals("")){
						return "fields cannot have blank strings";
					}
					
					if(result[1].equals("String")){
						
					}else if (result[1].equals("Number")){
						//"-?\\d+(\\.\\d+)?" 
						if( result[2].matches("\\d") ){
							
						}else{
							return "value of number type should be a Number";
						}
					}else if (result[1].equals("Boolean")){
						if(result[2].equals("true") || result[2].equals("false")){
							
						}else{
							return "Value of boolean type should be either true or false";
						}
					}
					
					
					return "ok";
				};
			};
			Listener cancelListener = new Listener(){

				public void handleEvent(Event event){
					result = null;
					dialog.close();
				}
			};
			bOK.addListener(SWT.Selection, okListener);
			bCancel.addListener(SWT.Selection, cancelListener);
			
			if(property != null){
				nameText.setText(AS.xarch.get(property, "name").toString());
				ValueText.setText(AS.xarch.get(property, "value").toString());
				typeCombo.setText(AS.xarch.get(property, "type").toString());
			}
		   
		
		    
		    dialog.pack();
			dialog.open();

			while(!dialog.isDisposed()){
				if(!dialog.getDisplay().readAndDispatch()){
					dialog.getDisplay().sleep();
				}
			}
		
			return result;
	}

	//varun
	protected void addArchElementToFeature(String selectedFeature, String id) {

		
		
		//New Implementation.
		ObjRef featureRef = AS.xarch.getByID(selectedFeature);
		ObjRef featureElementsRef = (ObjRef) AS.xarch.get(featureRef, "featureElements");
		if(AS.xarch.isInstanceOf(featureRef, "features#Varient")){
			
			ObjRef instanceContextRef = AS.xarch.createContext(xArchRef, "instance");
			ObjRef XMLLinkRef = AS.xarch.create(instanceContextRef, "XMLLink");
			AS.xarch.set(XMLLinkRef, "href", "#"+id);
			AS.xarch.set(XMLLinkRef, "type", "simple");
			AS.xarch.add(featureElementsRef, "archElement", XMLLinkRef);
		}else if(AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature")){
//			ObjRef featureElementsRef = (ObjRef) AS.xarch.get(featureRef, "featureElements");
			ObjRef instanceContextRef = AS.xarch.createContext(xArchRef, "instance");
			ObjRef XMLLinkRef = AS.xarch.create(instanceContextRef, "XMLLink");
			AS.xarch.set(XMLLinkRef, "href", "#"+id);
			AS.xarch.set(XMLLinkRef, "type", "simple");
			AS.xarch.add(featureElementsRef, "archElement", XMLLinkRef);
		}else if(AS.xarch.isInstanceOf(featureRef, "features#AlternativeFeature") && isOptionalAlternative(featureRef)){
//			ObjRef featureElementsRef = (ObjRef) AS.xarch.get(featureRef, "featureElements");
			ObjRef instanceContextRef = AS.xarch.createContext(xArchRef, "instance");
			ObjRef XMLLinkRef = AS.xarch.create(instanceContextRef, "XMLLink");
			AS.xarch.set(XMLLinkRef, "href", "#"+id);
			AS.xarch.set(XMLLinkRef, "type", "simple");
			AS.xarch.add(featureElementsRef, "archElement", XMLLinkRef);
		}
		//ObjRef objRef = AS.xarch.getByID(id);
		//XadlUtils.setXLink(AS.xarch, featureRef, "archElement", objRef);
		
	}
	
	
	
	
}
