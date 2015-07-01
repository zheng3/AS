package edu.uci.isr.archstudio4.comp.selectordriver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IFileEditorInput;

import edu.uci.isr.archstudio4.comp.booleaneval.NoSuchVariableException;
import edu.uci.isr.archstudio4.comp.booleaneval.SymbolTable;
import edu.uci.isr.archstudio4.comp.guardtracker.GuardTrackerImpl;
import edu.uci.isr.archstudio4.comp.pruner.IPruner;
import edu.uci.isr.archstudio4.comp.resources.IResources;
import edu.uci.isr.archstudio4.comp.selector.ISelector;
import edu.uci.isr.archstudio4.comp.versionpruner.IVersionPruner;
import edu.uci.isr.archstudio4.editors.AbstractArchstudioEditor;
import edu.uci.isr.archstudio4.editors.AbstractArchstudioOutlinePage;
import edu.uci.isr.archstudio4.util.EclipseUtils;
import edu.uci.isr.sysutils.SystemUtils;
import edu.uci.isr.sysutils.UIDGenerator;
import edu.uci.isr.widgets.swt.AutoResizeTableLayout;
import edu.uci.isr.widgets.swt.SWTWidgetUtils;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.umkc.archstudio4.processor.export.SourceCodePruner;

public class SelectorDriverEditor
	extends AbstractArchstudioEditor{

	private static final int COLUMN_INDEX_NAME = 0;
	private static final int COLUMN_INDEX_TYPE = 1;
	private static final int COLUMN_INDEX_VALUE = 2;
	private static final int COLUMN_INDEX_DESCRIPTION = 3;
	//varun
	private boolean refreshFlag = true;
	private HashMap<String, String> mapNameToId = new HashMap<String, String>();
	
	private HashMap<String, String> mapIdToName = new HashMap<String, String>();
	private HashMap<String,ArrayList<String>> varientsMap = new HashMap<String, ArrayList<String>>();
	
	
	private static final String[] COLUMN_NAMES = new String[]{"Feature", "Type", "Value","Description"};
	private static final String[] COLUMN_PROPERTY_NAMES = new String[]{"Name", "Type", "Value","Description"};

	protected ISelector selector = null;
	protected IPruner pruner = null;
	protected IVersionPruner versionPruner = null;
	protected SourceCodePruner codeProcessor = null;

	protected SymbolTable symbolTable = new SymbolTable();
	protected boolean architectureEnabled = true;
	protected boolean codeEnabled = false;
	protected boolean versionPruneEnabled = false;

	protected TableViewer symbolTableViewer = null;

	protected int count = 1;

	public SelectorDriverEditor(){
		super(SelectorDriverMyxComponent.class, SelectorDriverMyxComponent.EDITOR_NAME);

		setBannerInfo(((SelectorDriverMyxComponent)comp).getIcon(), "Reduce Product-Line to a Subset");
		setHasBanner(true);

		selector = ((SelectorDriverMyxComponent)comp).getSelector();
		pruner = ((SelectorDriverMyxComponent)comp).getPruner();
		versionPruner = ((SelectorDriverMyxComponent)comp).getVersionPruner();
		
	}
	

	@Override
	public void createEditorContents(Composite parent){
		ObjRef[] selectedRefs = ((SelectorDriverOutlinePage)outlinePage).getSelectedRefs();

		if(selectedRefs.length != 1){
			Label lInvalidSelection = new Label(parent, SWT.LEFT);
			lInvalidSelection.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
			lInvalidSelection.setFont(resources.getPlatformFont(IResources.PLATFORM_DEFAULT_FONT_ID));
			lInvalidSelection.setText("Select a structure or a type in the left as a starting point for selection.");
		}
		else{
			Group cBindings = new Group(parent, SWT.NONE);
			cBindings.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
			cBindings.setText("Variable-to-Value Bindings");
			cBindings.setFont(resources.getBoldPlatformFont(IResources.PLATFORM_DEFAULT_FONT_ID));
			cBindings.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
			cBindings.setLayout(new GridLayout(1, false));
			if(symbolTable.getVariables().length == 0)
			mapIdToText();
			final TableViewer tvBindings = createTableViewer(cBindings);
			symbolTableViewer = tvBindings;
			tvBindings.getTable().setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));

			Composite cBindingsButtons = new Composite(cBindings, SWT.NONE);
			cBindingsButtons.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
			cBindingsButtons.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
			cBindingsButtons.setLayout(new GridLayout(2, false));

/*			Button bNewString = new Button(cBindingsButtons, SWT.PUSH);
			bNewString.setText("New String");
			bNewString.addListener(SWT.Selection, new Listener(){

				public void handleEvent(Event event){
					symbolTable.putString("[New String " + count++ + "]", "[none]");
					doUpdateNow();
				}
			});

			Button bNewNumeric = new Button(cBindingsButtons, SWT.PUSH);
			bNewNumeric.setText("New Numeric");
			bNewNumeric.addListener(SWT.Selection, new Listener(){

				public void handleEvent(Event event){
					symbolTable.putDouble("[New Numeric " + count++ + "]", 0.0d);
					doUpdateNow();
				}
			});

			Button bNewDate = new Button(cBindingsButtons, SWT.PUSH);
			bNewDate.setText("New Date");
			bNewDate.addListener(SWT.Selection, new Listener(){

				public void handleEvent(Event event){
					symbolTable.putDate("[New Date " + count++ + "]", new java.util.Date());
					doUpdateNow();
				}
			});
			
			final Button bRemove = new Button(cBindingsButtons, SWT.PUSH);
			bRemove.setText("Remove");
			bRemove.addListener(SWT.Selection, new Listener(){

				public void handleEvent(Event event){
					Object o = ((IStructuredSelection)tvBindings.getSelection()).getFirstElement();
					if(o != null && o instanceof String){
						symbolTable.remove((String)o);
						updateEditor();
					}
				}
			});
			bRemove.setEnabled(false);*/

			final Button bImport = new Button(cBindingsButtons, SWT.PUSH);
			bImport.setText("Import Feature Settings...");
			bImport.addListener(SWT.Selection, new Listener(){

				public void handleEvent(Event event){
					IResource[] resources = EclipseUtils.selectResourcesToOpen(getSite().getShell(), SWT.SINGLE, "Select Symbols File", new String[]{"sym"});
					if(resources != null){
						IResource res = resources[0];
						IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
						IFile file = root.getFile(res.getFullPath());
						if(!file.exists()){
							MessageDialog.openError(getSite().getShell(), "Error", "Invalid input: file does not exist.");
							return;
						}
						InputStream is = null;
						try{
							is = file.getContents();
							symbolTable.clearTable();
							SymbolTable.parse(is, symbolTable);
						}
						catch(Exception e){
							MessageDialog.openError(getSite().getShell(), "Error", e.getMessage());
						}
						finally{
							try{
								if(is != null){
									is.close();
								}
							}
							catch(IOException ioe){
							}
						}
						updateEditor();
					}
				}
			});
			bImport.setEnabled(true);

			final Button bExport = new Button(cBindingsButtons, SWT.PUSH);
			bExport.setText("Export Feature Settings...");
			bExport.addListener(SWT.Selection, new Listener(){

				public void handleEvent(Event event){
					StringBuffer fileContents = new StringBuffer();
					String[] lines = symbolTable.listTable();
					for(String element: lines){
						fileContents.append(element);
						fileContents.append(System.getProperty("line.separator"));
					}
					InputStream is = new ByteArrayInputStream(fileContents.toString().getBytes());
					EclipseUtils.selectAndSaveFile(getSite().getShell(), "sym", is);
					try{
						is.close();
					}
					catch(java.io.IOException e){
					}
				}
			});
			bExport.setEnabled(!symbolTable.isEmpty());

			/*tvBindings.addSelectionChangedListener(new ISelectionChangedListener(){

				public void selectionChanged(SelectionChangedEvent event){
					bRemove.setEnabled(!tvBindings.getSelection().isEmpty());
				}
			});*/

			//Tasks
			Group cTasks = new Group(parent, SWT.NONE);
			cTasks.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
			cTasks.setText("Artifacts to Process");
			cTasks.setFont(resources.getBoldPlatformFont(IResources.PLATFORM_DEFAULT_FONT_ID));
			cTasks.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
			cTasks.setLayout(new GridLayout(1, false));

			Composite cTaskButtons = new Composite(cTasks, SWT.NONE);
			cTaskButtons.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
			cTaskButtons.setLayout(new GridLayout(2, false));

			final Button cbArchitecture = new Button(cTaskButtons, SWT.CHECK);
			cbArchitecture.setText("Architecture");
			cbArchitecture.setSelection(architectureEnabled);
			cbArchitecture.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));

			final Button cbCode = new Button(cTaskButtons, SWT.CHECK);
			cbCode.setText("Code");
			cbCode.setSelection(codeEnabled);
			cbCode.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));

			/*final Button cbVersionPrune = new Button(cTaskButtons, SWT.CHECK);
			cbVersionPrune.setText("Version Prune");
			cbVersionPrune.setSelection(versionPruneEnabled);
			cbVersionPrune.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));*/

			final Button bRunTasks = new Button(cTasks, SWT.PUSH);
			bRunTasks.setText("Run");
			bRunTasks.setEnabled(architectureEnabled || codeEnabled || versionPruneEnabled);
			bRunTasks.addListener(SWT.Selection, new Listener(){

				public void handleEvent(Event event){
					doTasks(cbArchitecture.getSelection(), cbCode.getSelection());
				}
			});

			/*Listener runTasksEnabler = new Listener(){

				public void handleEvent(Event event){
					selectEnabled = cbSelect.getSelection();
					pruneEnabled = cbPrune.getSelection();
					versionPruneEnabled = cbVersionPrune.getSelection();
					bRunTasks.setEnabled(selectEnabled || pruneEnabled || versionPruneEnabled);
				}
			};
			cbSelect.addListener(SWT.Selection, runTasksEnabler);
			cbPrune.addListener(SWT.Selection, runTasksEnabler);
			cbVersionPrune.addListener(SWT.Selection, runTasksEnabler);*/
			
			
			
			
		}
	}

	

	private void updateTable() throws Exception {
		
		
		GuardTrackerImpl guardImpl = new GuardTrackerImpl();
	
		
		guardImpl.setXArchADT(xarch);
		
		IFile file = ((IFileEditorInput)input).getFile();
		//System.out.println(file.getFullPath());
		ObjRef xarchRef = fileman.openXArch(uniqueEditorID, file);
		
		
		String[] guardStrings = guardImpl.getAllGuards(xarchRef);
		//System.out.println("-------------------------guard String ---------------");
		
		for (int i = 0; i < guardStrings.length; i++) {
			//System.out.println(guardStrings[i]);
		String[] sym = getSymboles(guardStrings[i]);
		mapIdToText();
		
		if(!symbolTable.isPresent(sym[0]) && refreshFlag){
			
			
			if(sym[2].equalsIgnoreCase("string")){
				String symbol = sym[0];
				String value = sym[1].substring(1, sym[1].length()-1);
//				mapNameToId.put(symbol, sym[0]);
//				mapNameToId.put(value, sym[1].substring(1, sym[1].length()-1));
				
				if(mapIdToName.containsKey(value)){
					value = mapIdToName.get(value);
				}
				if(mapIdToName.containsKey(symbol)){
					symbol = mapIdToName.get(symbol);
				}
				
				symbolTable.putString(symbol,value);
			}
		/*if(sym[2].equals("double")){
			
			
			symbolTable.putDouble(sym[0], Double.parseDouble(sym[1]));
		}else{
			symbolTable.putString(sym[0],sym[1].substring(1, sym[1].length()-1));
		}*/
		refreshFlag = true;
		}
		}
		
		
		
		if(refreshFlag){
			doUpdateNow();
			refreshFlag = false;
		}
	}

	private String getValue(String string) {
		
		if(string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false")){
			return string;
		}else{
			String id = getIdFromString(string);
			ObjRef ref = xarch.getByID(id);
			if(ref == null){
				return string;
			}else{
				String description =  XadlUtils.getDescription(xarch, ref);
				return description;
			}
		}
		
	}

	private String getIdFromString(String string) {
		
		String id = "";
		int len = string.length();
		if(len > 24){
			id = string.substring(0,len-24)+"-"+string.substring(len-24,len-16)+"-"+string.substring(len-16,len-8)+"-"+string.substring(len-8,len);
		}
		return id;
	}

	private String getSymbol(String string) {
		String id = getIdFromString(string);
		ObjRef ref = xarch.getByID(id);
		if(ref == null){
			return string;
		}else{
			String description =  XadlUtils.getDescription(xarch, ref);
			return description;
		}
	}
	
	private void mapIdToText(){
		
		//Create the "archFeature" reference if not created yet
		ObjRef featureContextRef = xarch.createContext(xArchRef, "features");
		ObjRef archFeatureRef = xarch.getElement(featureContextRef, "archFeature", xArchRef);
		if(archFeatureRef!=null){
//			archFeatureRef
			ObjRef[] featuresList = xarch.getAll(archFeatureRef, "feature");
			for (ObjRef objRef : featuresList) {
				ObjRef nameRef = (ObjRef)xarch.get(objRef, "featureName");
				ObjRef typeRef = (ObjRef)xarch.get(objRef, "type");
				String type = (String) xarch.get(typeRef, "value");
				String name = (String)xarch.get(nameRef, "value");
				ObjRef defualtRef = (ObjRef)xarch.get(objRef,"defaultValue");
				String defualt = (String)xarch.get(defualtRef, "value");
				
				if(type.equals("optional")){
					
				String id = XadlUtils.getID(xarch, objRef);
				String des = XadlUtils.getDescription(xarch, objRef);
				id = getIdTrim(id);
				
				mapIdToName.put(id,des);
				mapNameToId.put(des,id);
				
				
				symbolTable.putOptional(des,"true",name);
				}else{
					String id = XadlUtils.getID(xarch, objRef);
					String des = XadlUtils.getDescription(xarch, objRef);
					id = getIdTrim(id);
					
					mapIdToName.put(id,des);
					mapNameToId.put(des,id);
					
					
					ObjRef featureVarients = (ObjRef)xarch.get(objRef, "featureVarients");
					ObjRef[] varientsList = xarch.getAll(featureVarients, "varient");
					ArrayList<String> varientList = new ArrayList<String>();
					for (ObjRef varient : varientsList) {
						String vId1 = XadlUtils.getID(xarch, varient);
						String vDes = XadlUtils.getDescription(xarch, varient);
						String vId = getIdTrim(vId1);
						
						varientList.add(vDes);
						mapIdToName.put(vId,vDes);
						mapNameToId.put(vDes,vId);
						
						if(vId1.equalsIgnoreCase(defualt)){
							if(type.equals("optionalAlternative")){
								symbolTable.putOptionalAlternative(des,vDes,name);
							}else if(type.equals("alternative")){
								symbolTable.putAlternative(des,vDes,name);
							}
						}
					}
					if(type.equals("optionalAlternative")){
						varientList.add("false");
					}
					varientsMap.put(des, varientList);
				}
			}
			
		}
		
	}

	private String getIdTrim(String id) {
		
		return id.replaceAll("-", "");
	}

	private String[] getSymboles(String string) {
		String symbol = "";
		String val = "";
		String type = "";
		if(string.contains("==")){
			symbol = string.split("==")[0];
			val = string.split("==")[1];
			if(isNumeric(val)){
			type = "double"; 
			}else{
			type = "string";	
			}
		}
		else if(string.contains("!=")){
			symbol = string.split("!=")[0];
			val = string.split("!=")[1];
			if(isNumeric(val)){
			type = "double"; 
			}else{
			type = "string";	
			}
		}
		else if(string.contains(">=")){
			symbol = string.split(">=")[0];
			val = string.split(">=")[1];
			if(isNumeric(val)){
			type = "double"; 
			}else{
			type = "string";	
			}
		}
		else if(string.contains(">")){
			symbol = string.split(">")[0];
			val = string.split(">")[1];
			if(isNumeric(val)){
			type = "double"; 
			}else{
			type = "string";	
			}
		}
		else if(string.contains("<=")){
			symbol = string.split("<=")[0];
			val = string.split("<=")[1];
			if(isNumeric(val)){
			type = "double"; 
			}else{
			type = "string";	
			}
		}
		else if(string.contains("<")){
			symbol = string.split("<")[0];
			val = string.split("<")[1];
			if(isNumeric(val)){
			type = "double"; 
			}else{
			type = "string";	
			}
		}
		
		String[] result = {symbol.trim(),val.trim(),type.trim()};
		
		return result;
	}

	
	private boolean isNumeric(String str)
	{
		
	  return str.trim().matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	private TableViewer createTableViewer(Composite parent){
		TableViewer tv = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER |SWT.FULL_SELECTION);
		tv.setContentProvider(new PropertyTableContentProvider());
		tv.setLabelProvider(new PropertyTableLabelProvider());

		tv.setInput(symbolTable);

		Table table = tv.getTable();

		int index = 0;
		for(String element: COLUMN_NAMES){
			TableViewerColumn coloumViewer = new TableViewerColumn(tv, SWT.NONE); 
			coloumViewer.setLabelProvider(new ColoumLableProvider(index));
			coloumViewer.getColumn().setText(element);
			if(index == COLUMN_INDEX_VALUE){
			EditingSupport exampleEditingSupport = new DropdownEditingSupport(coloumViewer.getViewer());
			coloumViewer.setEditingSupport(exampleEditingSupport);
			}
//			TableColumn column = new TableColumn(table, SWT.CENTER);
//			column.setText(element);
			index++;
		}

		TableLayout tableLayout = new AutoResizeTableLayout(table);
		tableLayout.addColumnData(new ColumnWeightData(20, true));
		tableLayout.addColumnData(new ColumnWeightData(20, false));
		tableLayout.addColumnData(new ColumnWeightData(30, true));
		tableLayout.addColumnData(new ColumnWeightData(30, false));
		table.setLayout(tableLayout);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		//table.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));

		tv.setColumnProperties(COLUMN_PROPERTY_NAMES);

//		CellEditor nameEditor = new TextCellEditor(table);
//		CellEditor valueEditor =new TextCellEditor(table);
//		
//		
//		tv.setCellEditors(new CellEditor[]{nameEditor, null,  new ComboBoxCellEditor(
//				table, new String[]{"First","Second","Third"}) });
//		tv.setCellModifier(new PropertiesTableCellModifier());

		tv.refresh(true);
		return tv;
	}

	@Override
	protected AbstractArchstudioOutlinePage createOutlinePage(){
		return new SelectorDriverOutlinePage(xarch, xArchRef, resources);
	}

	class PropertyTableContentProvider
		implements IStructuredContentProvider{

		private Object[] EMPTY_ARRAY = new Object[0];

		public Object[] getElements(Object inputElement){
			if(inputElement instanceof SymbolTable){
				SymbolTable st = (SymbolTable)inputElement;
				return st.getVariables();
			}
			return null;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput){
		}

		public void dispose(){
		}
	}
	
	class ColoumLableProvider extends CellLabelProvider{

		private int columnIndex;
		public ColoumLableProvider(int index){
			this.columnIndex = index;
		}
		@Override
		public void update(ViewerCell cell) {
			Object element =  cell.getElement();
			if(element instanceof String){
				if(columnIndex == COLUMN_INDEX_NAME){
					cell.setText((String)element);
				}
				else if(columnIndex == COLUMN_INDEX_TYPE){
					try{
						int type = symbolTable.getType((String)element);
						cell.setText(SymbolTable.typeToString(type));
					}
					catch(NoSuchVariableException nsve){
						cell.setText(null);
					}
				}
				else if(columnIndex == COLUMN_INDEX_VALUE){
					Object value = symbolTable.get((String)element);
					if(value != null){
						cell.setText(value.toString());
					}
				}else if(columnIndex == COLUMN_INDEX_DESCRIPTION){
					try {
						cell.setText( symbolTable.getDescription((String)element));
					} catch (NoSuchVariableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/*Object value = symbolTable.get((String)element);
					if(value != null){
						return value.toString();
					}*/
				}
			}
			
			
		
			
		}
		
	}

	class PropertyTableLabelProvider
		extends LabelProvider
		implements ITableLabelProvider{

		public String getColumnText(Object element, int columnIndex){
			if(element instanceof String){
				if(columnIndex == COLUMN_INDEX_NAME){
					return (String)element;
				}
				else if(columnIndex == COLUMN_INDEX_TYPE){
					try{
						int type = symbolTable.getType((String)element);
						return SymbolTable.typeToString(type);
					}
					catch(NoSuchVariableException nsve){
						return null;
					}
				}
				else if(columnIndex == COLUMN_INDEX_VALUE){
					Object value = symbolTable.get((String)element);
					if(value != null){
						return value.toString();
					}
				}else if(columnIndex == COLUMN_INDEX_DESCRIPTION){
					try {
						return symbolTable.getDescription((String)element);
					} catch (NoSuchVariableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/*Object value = symbolTable.get((String)element);
					if(value != null){
						return value.toString();
					}*/
				}else if(columnIndex == COLUMN_INDEX_VALUE){
					Object value = symbolTable.get((String)element);
					if(value != null){
						return value.toString();
					}
				}else if(columnIndex == COLUMN_INDEX_DESCRIPTION){
					try {
						return symbolTable.getDescription((String)element);
					} catch (NoSuchVariableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/*Object value = symbolTable.get((String)element);
					if(value != null){
						return value.toString();
					}*/
				}
			}
			
			return null;
		}

		public Image getColumnImage(Object element, int columnIndex){
			return null;
		}
	}

	class PropertiesTableCellModifier
		implements ICellModifier{

		public boolean canModify(Object element, String property){
			if(element instanceof String){
				return property.equals(COLUMN_PROPERTY_NAMES[COLUMN_INDEX_NAME]) || property.equals(COLUMN_PROPERTY_NAMES[COLUMN_INDEX_VALUE]);
			}
			return false;
		}

		public Object getValue(Object element, String property){
			if(element instanceof String){
				if(property.equals(COLUMN_PROPERTY_NAMES[COLUMN_INDEX_NAME])){
					return element;
				}
				else if(property.equals(COLUMN_PROPERTY_NAMES[COLUMN_INDEX_VALUE])){
					Object value = symbolTable.get((String)element);
					if(value != null){
						if(value instanceof java.util.Date){
							return DateFormat.getDateTimeInstance().format((java.util.Date)value);
						}
						return value.toString();
					}
				}
			}
			return null;
		}

		public void modify(Object element, String property, Object value){
			//SWT bug workaround
			if(element instanceof Item){
				element = ((Item)element).getData();
			}
			if(element instanceof String){
				SymbolTable.Entry entry =  symbolTable.getEntry((String) element);
				if(property.equals(COLUMN_PROPERTY_NAMES[COLUMN_INDEX_NAME])){
					if(value != null){
						if(value.equals(element)){
							return;
						}
					}
					symbolTable.renameVariable((String)element, value.toString());
					updateTableViewer();
				}
				else if(property.equals(COLUMN_PROPERTY_NAMES[COLUMN_INDEX_VALUE])){
					if(value != null){
						Object oldValue = symbolTable.get((String)element);
						if(value.equals(oldValue)){
							return;
						}
					}
					try{
						int type = symbolTable.getType((String)element);
						if(type == SymbolTable.STRING){
							symbolTable.putString((String)element, value.toString());
							updateTableViewer();
						}
						else if(type == SymbolTable.OPTIONAL){
							symbolTable.putOptional((String)element, value.toString(),entry.getDescription());
							updateTableViewer();
						}
						else if(type == SymbolTable.ALTERNATIVE){
							symbolTable.putAlternative((String)element, value.toString(),entry.getDescription());
							updateTableViewer();
						}
						else if(type == SymbolTable.OPTIONAL_ALTERNATIVE){
							symbolTable.putOptionalAlternative((String)element, value.toString(),entry.getDescription());
							updateTableViewer();
						}
						else if(type == SymbolTable.DOUBLE){
							try{
								Double d = new Double(value.toString());
								symbolTable.putDouble((String)element, d.doubleValue());
								updateTableViewer();
							}
							catch(NumberFormatException nfe){
								showMessage("Input must be a numeric value.");
							}
						}
						else if(type == SymbolTable.DATE){
							java.util.Date date = SystemUtils.parseDate(value.toString());
							if(date != null){
								symbolTable.putDate((String)element, date);
								updateTableViewer();
							}
							else{
								showMessage("Input must be a valid date value.");
							}
						}
					}
					catch(NoSuchVariableException nsve){
					}
				}
			}
		}
	}
	
	public final class DropdownEditingSupport extends EditingSupport {
	     
	    private ComboBoxViewerCellEditor cellEditor = null;
	     private ColumnViewer viewer;
	    private DropdownEditingSupport(ColumnViewer viewer) {
	        super(viewer);
	       this.viewer = viewer;
	    }
	     
	    @Override
	    protected CellEditor getCellEditor(Object element) {
	    	 cellEditor = new ComboBoxViewerCellEditor((Composite) getViewer().getControl(), SWT.READ_ONLY);
		     cellEditor.setLabelProvider(new LabelProvider());
		     cellEditor.setContenProvider(new ArrayContentProvider());
		     int type = -1;
		     try {
				type = symbolTable.getType(element.toString());
			} catch (NoSuchVariableException e) {
				e.printStackTrace();
			} 
		     if(type == SymbolTable.OPTIONAL){
		    	 cellEditor.setInput(new String[]{"true","false"});	 
		     }else if(type == SymbolTable.OPTIONAL_ALTERNATIVE){ 
		    	ArrayList<String> lst = varientsMap.get(element.toString());
		    	String[] arr = new String[lst.size()];
		    	arr = lst.toArray(arr);
		    	cellEditor.setInput(arr);	
		    }else if(type == SymbolTable.ALTERNATIVE){
		    	 ArrayList<String> lst = varientsMap.get(element.toString());
		    	 String[] arr = new String[lst.size()];
			     arr = lst.toArray(arr);
		    	 cellEditor.setInput(arr);	 
		     }
		     
	        return cellEditor;
	    }
	     
	    @Override
	    protected boolean canEdit(Object element) {
	        return true;
	    }
	     
	    @Override
	    protected Object getValue(Object element) {
	        if (element instanceof String) {
//	            ExampleData data = (ExampleData)element;
	        	
	            return symbolTable.get(element.toString());
	        }
	        return null;
	    }
	     
	    @Override
	    protected void setValue(Object element, Object value) {
	        if (element instanceof String && value instanceof String) {
	        	try {
	        		SymbolTable.Entry entry = symbolTable.getEntry((String) element); 
	        		int type = symbolTable.getType(element.toString());
	        		 if(type == SymbolTable.OPTIONAL){
	        			 symbolTable.putOptional(entry.getName(), value.toString(),entry.getDescription());
	    		     }else if(type == SymbolTable.OPTIONAL_ALTERNATIVE){
	    		    	 symbolTable.putOptionalAlternative(entry.getName(), value.toString(),entry.getDescription()); 
	    		     }else if(type == SymbolTable.ALTERNATIVE){
	    		    	 symbolTable.putAlternative(entry.getName(), value.toString(),entry.getDescription());
	    		     }
					viewer.update(element, null);
				} 
	            catch (NoSuchVariableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }
	     
	}
	

	protected void doTasks(boolean generateArch, boolean generateCode){
		String baseURI = xarch.getXArchURI(xArchRef);
		
		ObjRef[] selectedRefs = ((SelectorDriverOutlinePage)outlinePage).getSelectedRefs();
		if(selectedRefs.length != 1){
			showMessage("Invalid selection; can't run tasks.");
			return;
		}

		ObjRef selectedRef = selectedRefs[0];
		boolean isStructural = xarch.isInstanceOf(selectedRef, "types#ArchStructure");

		String startingID = XadlUtils.getID(xarch, selectedRef);
		if(startingID == null){
			showMessage("Selected element has no ID; can't run tasks.");
			return;
		}
		updateEntries(symbolTable.getEntryList());
		java.util.List urisToClose = new java.util.ArrayList();
		try{
			if(generateArch){
				String newURI = UIDGenerator.generateUID("urn:");
				try{
//					symbolTable.
					selector.select(baseURI, newURI, symbolTable, startingID, isStructural);
					urisToClose.add(newURI);
					baseURI = newURI;
				}
				catch(Exception e){
					IStatus status = new Status(IStatus.ERROR, SelectorDriverMyxComponent.ECLIPSE_EDITOR_ID, IStatus.ERROR, "Selection of architecture failed: " + e.getMessage(), e);
					showError(status);
					return;
				}
			}

			ObjRef processedRef = xarch.getOpenXArch(baseURI);
			String targetProject;
			if(processedRef != null){
				String contents = xarch.serialize(processedRef);
				InputStream is = new ByteArrayInputStream(contents.getBytes());
				String targetFile = EclipseUtils.selectAndSaveFile(getEditorSite().getShell(), "xml", is);
				targetProject = targetFile.substring(1, targetFile.indexOf('/', 1));
			}
			else{
				showMessage("Error: after processing, couldn't read output. This shouldn't happen.");
				return;
			}
			
			reUpdateEntries(symbolTable.getEntryList());
			
			if(generateCode){
				try{
					if (codeProcessor == null){
						String xArchURI = xarch.getXArchURI(xArchRef); 
						String prjName = xArchURI.substring(1, xArchURI.indexOf('/', 1));
						codeProcessor = new SourceCodePruner(prjName);
					}
					codeProcessor.pruneFeatures(symbolTable, targetProject);
					//ProcessorUtils.relocate(new File("icon"), new File("newicon"), ProcessorUtils.FileFilter.DEFAULT);
					
				}
				catch(Exception e){
					IStatus status = new Status(IStatus.ERROR, SelectorDriverMyxComponent.ECLIPSE_EDITOR_ID, IStatus.ERROR, "Selection of code failed: " + e.getMessage(), e);
					showError(status);
					return;
				}
			}
			
		}
		finally{
			String[] uris = (String[])urisToClose.toArray(new String[urisToClose.size()]);
			for(String element: uris){
				xarch.close(element);
			}
			
			//reUpdateEntries(symbolTable.getEntryList());
		}
	}

	private void updateEntries(List entryList) {
		
		for (Object object : entryList) {
			SymbolTable.Entry entry = (SymbolTable.Entry)object;
			if(mapNameToId.containsKey(entry.getName())){
				entry.setName(mapNameToId.get(entry.getName()));
			}
			if(mapNameToId.containsKey(entry.getValue())){
				entry.setValue(mapNameToId.get(entry.getValue()));
			}
		}
		
	}
	
	private void reUpdateEntries(List entryList) {
		
		for (Object object : entryList) {
			SymbolTable.Entry entry = (SymbolTable.Entry)object;
			if(mapIdToName.containsKey(entry.getName())){
				entry.setName(mapIdToName.get(entry.getName()));
			}
			if(mapIdToName.containsKey(entry.getValue())){
				entry.setValue(mapIdToName.get(entry.getValue()));
			}
		}
		
	}

	private void updateTableViewer(){
		SWTWidgetUtils.async(symbolTableViewer, new Runnable(){

			public void run(){
				symbolTableViewer.refresh();
				symbolTableViewer.getTable().getParent().layout(true);
			}
		});
	}

}
