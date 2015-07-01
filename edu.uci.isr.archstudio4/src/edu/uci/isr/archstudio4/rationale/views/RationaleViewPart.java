package edu.uci.isr.archstudio4.rationale.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import edu.uci.isr.archstudio4.comp.xarchcs.explicitadt.IExplicitADT;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetInterface;
import edu.uci.isr.archstudio4.rationale.RationaleViewManager;
import edu.uci.isr.archstudio4.rationale.actions.AddRationaleAction;
import edu.uci.isr.archstudio4.rationale.actions.DeleteRationaleAction;
import edu.uci.isr.myx.fw.MyxRegistry;
import edu.uci.isr.xarchflat.IXArchTypeMetadata;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchMetadataUtils;

public class RationaleViewPart extends ViewPart implements ISelectionListener,IPartListener{

	Composite parent;
	RationaleViewMyxComponent rationaleViewMyxComponent;
	XArchChangeSetInterface xArchCS;
	IExplicitADT explicit;
	TableViewer rationaleTableViewer;
	TableViewer associatedItemsTableViewer;
	RationaleViewManager rationaleViewManager;
	AddRationaleAction addRationaleAction;
	DeleteRationaleAction deleteRationaleAction;
	ObjRef xArchRef = null;

	public RationaleViewPart() {
		super();
	}


	static void addCellEditor(TableViewer tableViewer, CellEditor editor) {
		CellEditor[] editors = tableViewer.getCellEditors();
		if (editors == null)
			editors = new CellEditor[0];
		CellEditor[] newEditors = new CellEditor[editors.length + 1];
		System.arraycopy(editors, 0, newEditors, 0, editors.length);
		newEditors[newEditors.length - 1] = editor;
		tableViewer.setCellEditors(newEditors);
	}

	static void addColumnProperty(TableViewer tableViewer, String property) {
		String[] properties = (String[]) tableViewer.getColumnProperties();
		if (properties == null)
			properties = new String[0];
		String[] newProperties = new String[properties.length + 1];
		System.arraycopy(properties, 0, newProperties, 0, properties.length);
		newProperties[newProperties.length - 1] = property;
		tableViewer.setColumnProperties(newProperties);
	}


	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		rationaleViewMyxComponent = MyxRegistry.getSharedInstance().waitForBrick(RationaleViewMyxComponent.class);		
		xArchCS = rationaleViewMyxComponent.xArchCS;
		GridLayout gridLayout = new GridLayout(2,true);
		parent.setLayout(gridLayout);	  
		rationaleTableViewer = new TableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION);
		associatedItemsTableViewer = new TableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION);
		rationaleTableViewer.setUseHashlookup(true);
		associatedItemsTableViewer.setUseHashlookup(true);
		rationaleViewManager = new RationaleViewManager(xArchCS,rationaleTableViewer,associatedItemsTableViewer);
		IActionBars actionBars = getViewSite().getActionBars();
		IToolBarManager toolBarManager = actionBars.getToolBarManager();
		this.addRationaleAction = new AddRationaleAction(xArchCS,rationaleTableViewer,rationaleViewManager);
		this.deleteRationaleAction = new DeleteRationaleAction(xArchCS,rationaleTableViewer,rationaleViewManager);
		toolBarManager.add(addRationaleAction);
		toolBarManager.add(deleteRationaleAction);
		addRationaleAction.setEnabled(false);
		deleteRationaleAction.setEnabled(false);

		rationaleTableViewer.setContentProvider(new RationaleContentProvider(rationaleViewManager));
		rationaleTableViewer.setLabelProvider(new RationaleLabelProvider(xArchCS,rationaleTableViewer,rationaleViewManager));
		associatedItemsTableViewer.setContentProvider(new AssociatedItemsContentProvider(rationaleViewManager));
		associatedItemsTableViewer.setLabelProvider(new AssociatedItemsLabelProvider(xArchCS,rationaleViewManager,associatedItemsTableViewer));

		Table rationaleTable = rationaleTableViewer.getTable();
		TableLayout rationaleTableLayout = new TableLayout();
		rationaleTable.setHeaderVisible(true);
		rationaleTable.setLinesVisible(true);
		rationaleTable.setLayout(rationaleTableLayout);
		rationaleTable.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));

		Table associatedItemsTable = associatedItemsTableViewer.getTable();
		TableLayout associatedItemsTableLayout = new TableLayout();
		associatedItemsTable.setHeaderVisible(true);
		associatedItemsTable.setLinesVisible(true);
		associatedItemsTable.setLayout(associatedItemsTableLayout);
		associatedItemsTable.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));

		TableColumn rationaleTableColumn = new TableColumn(rationaleTable, SWT.LEFT);
		rationaleTableColumn.setText("Sandbox");
		rationaleTableColumn.setImage(null);
		rationaleTableColumn.setResizable(false);
		rationaleTableLayout.addColumnData(new ColumnPixelData(0, rationaleTableColumn.getResizable()));
		CellEditor editor = null;
		addCellEditor(rationaleTableViewer, editor);
		addColumnProperty(rationaleTableViewer, rationaleTableColumn.getText());

		TableColumn associatedItemsTableColumn = new TableColumn(associatedItemsTable, SWT.LEFT);
		associatedItemsTableColumn.setText("Sandbox");
		associatedItemsTableColumn.setImage(null);
		associatedItemsTableColumn.setResizable(false);
		associatedItemsTableLayout.addColumnData(new ColumnPixelData(0, associatedItemsTableColumn.getResizable()));
		CellEditor sandBoxEditor = null;
		addCellEditor(associatedItemsTableViewer, sandBoxEditor);
		addColumnProperty(associatedItemsTableViewer, associatedItemsTableColumn.getText());



		rationaleTableColumn = new TableColumn(rationaleTable, SWT.LEFT);
		rationaleTableColumn.setText("Rationale");
		rationaleTableColumn.setResizable(true);
		rationaleTableLayout.addColumnData(new ColumnWeightData(100, rationaleTableColumn.getResizable()));
		CellEditor rationaleDescriptionEditor = new TextCellEditor(rationaleTable);
		addCellEditor(rationaleTableViewer, rationaleDescriptionEditor);
		addColumnProperty(rationaleTableViewer, rationaleTableColumn.getText());

		associatedItemsTableColumn = new TableColumn(associatedItemsTable, SWT.LEFT);
		associatedItemsTableColumn.setText("Associated Items");
		associatedItemsTableColumn.setResizable(true);
		associatedItemsTableLayout.addColumnData(new ColumnPixelData(100, associatedItemsTableColumn.getResizable()));
		CellEditor itemDetailsEditor = null;
		addCellEditor(associatedItemsTableViewer, itemDetailsEditor);
		addColumnProperty(associatedItemsTableViewer, associatedItemsTableColumn.getText());


		ObjRef xArchRef = null;

		IEditorPart editorPart = getSite().getPage().getActiveEditor();
		if(editorPart != null && editorPart.getEditorInput() instanceof IFileEditorInput){
			IFileEditorInput fileEditorInput = (IFileEditorInput)editorPart.getEditorInput();
			xArchRef = xArchCS.getOpenXArch(fileEditorInput.getFile().getFullPath().makeAbsolute().toString());
		}
		updateXArchRef(xArchRef);
		getViewSite().getPage().addPartListener(this);
		getViewSite().getPage().addSelectionListener(this);
		rationaleTableViewer.setCellModifier(new RationaleCellModifier(rationaleTableViewer,rationaleViewManager,xArchCS));		

		rationaleTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				rationaleViewManager.clearSelectedRationaleRefList();
				Iterator iterator = ((IStructuredSelection) event.getSelection()).iterator();
				if(iterator != null) {
					while(iterator.hasNext()) {
						Object obj = iterator.next();
						if(obj != null && obj instanceof ObjRef) {
							ObjRef rationaleRef = (ObjRef)obj;
							rationaleViewManager.addSelectedRationale(rationaleRef);
						}
					}
					associatedItemsTableViewer.refresh();
				}
			}			
		});
		
		
		associatedItemsTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				rationaleViewManager.clearSelectedAssociatedItemRefList();
				Iterator iterator = ((IStructuredSelection) event.getSelection()).iterator();
				if(iterator != null) {
					while(iterator.hasNext()) {
						Object obj = iterator.next();
						if(obj != null && obj instanceof ObjRef) {
							ObjRef ref = (ObjRef)obj;
							rationaleViewManager.addSelectedAssociatedItemRefList(ref);
						}
					}
					rationaleTableViewer.refresh();
				}
			}			
		});
	}

	@Override
	public void setFocus() {

	}

	public synchronized void selectionChanged(IWorkbenchPart part, ISelection selection) {
		List<ObjRef> currentSelectionList = new ArrayList<ObjRef>();
		if(selection instanceof IStructuredSelection) {
			Object[] selectionArray = ((IStructuredSelection)selection).toArray();
			if(selectionArray != null && selectionArray.length > 0) {
				boolean archChangeSetsRationaleCreated = false;
				for(Object selectedObject : selectionArray) {
					if(selectedObject instanceof ObjRef) {
						ObjRef ref = (ObjRef)selectedObject;
						ObjRef[] ancestors = xArchCS.getAllAncestors(ref);
						if(xArchCS.isInstanceOf(ancestors[ancestors.length - 2],"changesets#ArchChangeSets") && !archChangeSetsRationaleCreated){
							ObjRef changeSetsContextRef = xArchCS.createContext(xArchRef, "changesets");	
							ObjRef archChangeSetsElementRef = xArchCS.getElement(changeSetsContextRef,"ArchChangeSets",xArchRef);
							if(!xArchCS.isInstanceOf(archChangeSetsElementRef,"rationale#ArchChangeSetsRationale")){
								ObjRef rationaleContextRef = xArchCS.createContext(xArchRef,"rationale");
								xArchCS.promoteTo(rationaleContextRef, "ArchChangeSetsRationale", archChangeSetsElementRef);
								ObjRef archRationaleRef = xArchCS.create(rationaleContextRef,"ArchRationale");
								xArchCS.set(archChangeSetsElementRef, "ArchRationale",archRationaleRef);
								archChangeSetsRationaleCreated = true;
							}
						}
						currentSelectionList.add((ObjRef)selectedObject);
					}
				}
			}
		}
		rationaleViewManager.loadRationales(currentSelectionList);
		associatedItemsTableViewer.refresh();

		if(currentSelectionList.size() > 0 && xArchRef != null) {
			addRationaleAction.setEnabled(true);
			deleteRationaleAction.setEnabled(true);
		}
		else {
			addRationaleAction.setEnabled(false);
			deleteRationaleAction.setEnabled(false);
		}

	}

	public void partActivated(IWorkbenchPart part) {
		if(part instanceof IEditorPart){
			IEditorPart editorPart = (IEditorPart)part;
			if(editorPart.getEditorInput() instanceof IFileEditorInput){
				IFileEditorInput fileEditorInput = (IFileEditorInput)editorPart.getEditorInput();
				ObjRef xArchRef = xArchCS.getOpenXArch(fileEditorInput.getFile().getFullPath().makeAbsolute().toString());
				updateXArchRef(xArchRef);
			}
		}		
	}

	public synchronized void updateXArchRef(ObjRef xArchRef) {
		this.xArchRef = xArchRef;
		if(this.xArchRef != null) {
			ObjRef rationaleContextRef = xArchCS.createContext(xArchRef, "rationale");
			ObjRef rationaleElementRef = xArchCS.getElement(rationaleContextRef, "archRationale", xArchRef);
			if(rationaleElementRef == null) {
				rationaleElementRef = xArchCS.createElement(rationaleContextRef,"archRationale");
				xArchCS.add(xArchRef, "object", rationaleElementRef);
			} 
		}
		rationaleTableViewer.setInput(xArchRef);
		associatedItemsTableViewer.setInput(xArchRef);
		rationaleViewManager.setXArchRef(xArchRef);
		addRationaleAction.setXArchRef(xArchRef);
		deleteRationaleAction.setXArchRef(xArchRef);
	}

	public void partBroughtToTop(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	public void partClosed(IWorkbenchPart part) {
		if(part instanceof IEditorPart){
			IEditorPart editorPart = getSite().getPage().getActiveEditor();
			if(editorPart == null){
				updateXArchRef(null);
			}
			else if(editorPart.getEditorInput() instanceof IFileEditorInput){
				IFileEditorInput fileEditorInput = (IFileEditorInput)editorPart.getEditorInput();
				ObjRef xArchRef = xArchCS.getOpenXArch(fileEditorInput.getFile().getFullPath().makeAbsolute().toString());
				updateXArchRef(xArchRef);
			}
		}
	}

	public void partDeactivated(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	public void partOpened(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}


	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
