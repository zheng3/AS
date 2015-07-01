package edu.uci.isr.archstudio4.comp.archlight.issueview;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

import edu.uci.isr.archstudio4.archlight.ArchlightElementIdentifier;
import edu.uci.isr.archstudio4.archlight.ArchlightIssue;
import edu.uci.isr.archstudio4.comp.archlight.ArchlightConstants;
import edu.uci.isr.archstudio4.comp.archlight.DoubleClickAction;
import edu.uci.isr.archstudio4.comp.archlight.issueadt.ArchlightIssueADTEvent;
import edu.uci.isr.archstudio4.comp.archlight.issueadt.ArchlightIssueADTListener;
import edu.uci.isr.archstudio4.comp.archlight.issueadt.IArchlightIssueADT;
import edu.uci.isr.archstudio4.comp.resources.IResources;
import edu.uci.isr.archstudio4.editors.IEditorManager;
import edu.uci.isr.myx.fw.MyxRegistry;
import edu.uci.isr.widgets.swt.AutoResizeTableLayout;
import edu.uci.isr.widgets.swt.IMenuFiller;
import edu.uci.isr.widgets.swt.SWTWidgetUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public class ArchlightIssueView extends ViewPart implements ArchlightIssueADTListener, IMenuFiller{
	private ArchlightIssueViewMyxComponent comp = null;
	private MyxRegistry er = MyxRegistry.getSharedInstance();
	
	private static final int COLUMN_INDEX_SEVERITY = 0;
	private static final int COLUMN_INDEX_SUMMARY = 1;
	private static final int COLUMN_INDEX_TOOL = 2;
	
	private static final String[] COLUMN_NAMES = new String[]{
		" " /* severity */, "Summary", "Tool"
	};
	
	private TableViewer viewer;

	protected XArchFlatInterface xarch = null;
	protected IArchlightIssueADT issueadt = null;
	protected IResources resources = null;
	protected IEditorManager editorManager = null;
	protected IPreferenceStore prefs = null;
	
	public ArchlightIssueView(){
		comp = (ArchlightIssueViewMyxComponent)er.waitForBrick(ArchlightIssueViewMyxComponent.class);
		er.map(comp, this);
		xarch = comp.xarch;
		issueadt = comp.issueadt;
		resources = comp.resources;
		editorManager = comp.editorManager;
		prefs = comp.prefs;
	}

	public void issueADTChanged(ArchlightIssueADTEvent evt){
		refreshView();
	}
	
	public void refreshView(){
		SWTWidgetUtils.async(viewer, new Runnable(){
			public void run(){
				if(!viewer.getControl().isDisposed()) viewer.refresh();
			}
		});
	}
	
	public void createPartControl(Composite parent){
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(getViewSite());
		
		Table table = viewer.getTable();

		TableColumn column = new TableColumn(table, SWT.CENTER);
		column.setText(COLUMN_NAMES[0]);

		TableColumn column2 = new TableColumn(table, SWT.LEFT);
		column2.setText(COLUMN_NAMES[1]);
		
		TableColumn column3 = new TableColumn(table, SWT.LEFT);
		column3.setText(COLUMN_NAMES[2]);
		
		TableLayout tableLayout = new AutoResizeTableLayout(table);
		tableLayout.addColumnData(new ColumnWeightData(5, false));
		tableLayout.addColumnData(new ColumnWeightData(80, true));
		tableLayout.addColumnData(new ColumnWeightData(15, true));
		table.setLayout(tableLayout);
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		//table.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));

		viewer.setColumnProperties(COLUMN_NAMES);
		//CellEditor tce = new TextCellEditor(table);
		//tv.setCellEditors(new CellEditor[]{null, tce});
		//tv.setCellModifier(new ViewCellModifier(selectedRefs[i]));
		
		viewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event){
				String doubleClickActionString = prefs.getString(ArchlightConstants.PREF_DOUBLE_CLICK_ACTION);
				if((doubleClickActionString == null) || (doubleClickActionString.length() == 0)){
					doubleClickActionString = DoubleClickAction.OPEN_DETAIL_WINDOW.name();
				}
				
				DoubleClickAction dca = DoubleClickAction.OPEN_DETAIL_WINDOW;
				try{
					dca = DoubleClickAction.valueOf(doubleClickActionString);
				}
				catch(Exception e){
					dca = DoubleClickAction.OPEN_DETAIL_WINDOW;
				}

				ArchlightIssue[] selectedIssues = getSelectedIssues();
				if(selectedIssues.length == 1){
					if(dca.equals(DoubleClickAction.OPEN_DETAIL_WINDOW)){
						showDetailDialog(selectedIssues[0]);
					}
					else if(dca.equals(DoubleClickAction.FOCUS_IN_DEFAULT_EDITOR)){
						String defaultEditor = editorManager.getDefaultEditor();
						ArchlightElementIdentifier[] elementIdentifiers = selectedIssues[0].getElementIdentifiers();
						if(elementIdentifiers.length > 0){
							focusEditor(defaultEditor, selectedIssues[0], elementIdentifiers[0]);
						}
					}
				}
			}
		});
		
		SWTWidgetUtils.setupContextMenu("#PopupMenu", viewer.getControl(), getSite(), this);
		
		viewer.refresh();
	}
	
	protected Image getSeverityIcon(ArchlightIssue issue){
		switch(issue.getSeverity()){
		case ArchlightIssue.SEVERITY_ERROR:
			return resources.getPlatformImage(ISharedImages.IMG_OBJS_ERROR_TSK);
		case ArchlightIssue.SEVERITY_WARNING:
			return resources.getPlatformImage(ISharedImages.IMG_OBJS_WARN_TSK);
		case ArchlightIssue.SEVERITY_INFO:
		default:
			return resources.getPlatformImage(ISharedImages.IMG_OBJS_INFO_TSK);
		}
	}
	
	class ViewContentProvider implements IStructuredContentProvider{
		//private Object[] EMPTY_ARRAY = new Object[0];
		
		public Object[] getElements(Object inputElement){
			return issueadt.getAllIssues();
		}
		
		public void dispose(){
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput){
		}
	}
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider{
		public String getColumnText(Object obj, int index){
			if(obj instanceof ArchlightIssue){
				if(index == COLUMN_INDEX_SUMMARY){
					return ((ArchlightIssue)obj).getHeadline();
				}
				else if(index == COLUMN_INDEX_TOOL){
					return ((ArchlightIssue)obj).getToolID();
				}
			}
			return null;
		}
		
		public Image getColumnImage(Object obj, int index){
			if(obj instanceof ArchlightIssue){
				if(index == COLUMN_INDEX_SEVERITY){
					return getSeverityIcon((ArchlightIssue)obj);
				}
			}
			return null;
		}
		
		public Image getImage(Object obj){
			return null;
		}
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	public ArchlightIssue[] getSelectedIssues(){
		ISelection selection = viewer.getSelection();
		if(selection instanceof IStructuredSelection){
			IStructuredSelection ss = (IStructuredSelection)selection;
			Object[] issueObjects = ss.toArray();
			ArchlightIssue[] issues = new ArchlightIssue[issueObjects.length];
			System.arraycopy(issueObjects, 0, issues, 0, issueObjects.length);
			return issues;
		}
		else{
			return new ArchlightIssue[0];
		}
	}
	
	protected void focusEditor(String editorName, ArchlightIssue issue, ArchlightElementIdentifier eltId){
		ObjRef ref = getRef(issue, eltId);
		if(ref != null){
			editorManager.focusEditor(editorName, new ObjRef[]{ref});
		}
	}
	
	protected ObjRef getRef(ArchlightIssue issue, ArchlightElementIdentifier eltId){
		ObjRef ref = eltId.getElementRef();
		if(ref == null){
			String id = eltId.getElementID();
			if(id != null){
				ref = xarch.getByID(issue.getDocumentRef(), eltId.getElementID());
			}
		}
		return ref;
	}
	
	public void showDetailDialog(ArchlightIssue issue){
		final Shell dialog = new Shell(getViewSite().getShell(), SWT.RESIZE | SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		dialog.setText("Archlight Issue Detail");

		GridLayout dialogLayout = new GridLayout(1, true);
		dialogLayout.marginTop = 5;
		dialogLayout.marginBottom = 5;
		dialogLayout.marginLeft = 5;
		dialogLayout.marginRight = 5;

		dialog.setLayout(dialogLayout);
		
		Label lHeadline = new Label(dialog, SWT.WRAP);
		lHeadline.setText(issue.getHeadline());
		lHeadline.setFont(resources.getPlatformFont(IResources.PLATFORM_HEADER_FONT_ID));
		lHeadline.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1,	1));
		
		Label lSep1 = new Label(dialog, SWT.SEPARATOR | SWT.HORIZONTAL);
		lSep1.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));		
		
		Label lSeverity = new Label(dialog, SWT.NONE);
		lSeverity.setFont(resources.getPlatformFont(IResources.PLATFORM_BANNER_FONT_ID));
		lSeverity.setText("Severity:");
		
		Composite cSeverity = new Composite(dialog, SWT.NONE);
		cSeverity.setLayout(new GridLayout(2, false));
		GridData cSeverityData = new GridData();
		cSeverityData.horizontalIndent = 5;
		cSeverity.setLayoutData(cSeverityData);

		Label lSeverityIcon = new Label(cSeverity, SWT.NONE);
		lSeverityIcon.setImage(getSeverityIcon(issue));
		
		Label lSeverityText = new Label(cSeverity, SWT.NONE);
		switch(issue.getSeverity()){
		case ArchlightIssue.SEVERITY_ERROR:
			lSeverityText.setText("Error");
			break;
		case ArchlightIssue.SEVERITY_WARNING:
			lSeverityText.setText("Warning");
			break;
		case ArchlightIssue.SEVERITY_INFO:
		default:
			lSeverityText.setText("Info");
			break;
		}
		
		Label lSep2 = new Label(dialog, SWT.SEPARATOR | SWT.HORIZONTAL);
		lSep2.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));		
		
		Label lDetail = new Label(dialog, SWT.NONE);
		lDetail.setFont(resources.getPlatformFont(IResources.PLATFORM_BANNER_FONT_ID));
		lDetail.setText("Detail:");
		
		Label lDetailContent = new Label(dialog, SWT.WRAP);
		lDetailContent.setText(issue.getDetailedDescription());
		lDetailContent.setFont(resources.getPlatformFont(IResources.PLATFORM_DEFAULT_FONT_ID));
		GridData lDetailContentData = new GridData(SWT.FILL, SWT.TOP, true, false, 1,	1);
		lDetailContentData.horizontalIndent = 5;
		lDetailContent.setLayoutData(lDetailContentData);
		
		Label lSep3 = new Label(dialog, SWT.SEPARATOR | SWT.HORIZONTAL);
		lSep3.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));		
		
		Composite cButtons = new Composite(dialog, SWT.NONE);
		GridLayout cButtonsLayout = new GridLayout(1, false);
		cButtonsLayout.marginRight = 5;
		cButtons.setLayout(cButtonsLayout);
		cButtons.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		
		final Button buttonOK = new Button(cButtons, SWT.PUSH);
		buttonOK.setText("OK");
 		GridData gridData = new GridData();
 		gridData.horizontalAlignment = GridData.FILL;
 		gridData.grabExcessHorizontalSpace = true;
 		gridData.widthHint = 100;
 		buttonOK.setLayoutData(gridData);

		Listener listener = new Listener(){
			public void handleEvent(Event event){
				dialog.close();
			}
		};
		buttonOK.addListener(SWT.Selection, listener);

		dialog.setSize(dialog.computeSize(500, SWT.DEFAULT));
		dialog.open();
	}
	
	public void fillMenu(IMenuManager m){
		ArchlightIssue[] issues = getSelectedIssues();
		if(issues.length == 0){
			m.add(SWTWidgetUtils.createNoAction("[Select an issue for menu]"));
		}
		else if(issues.length > 1){
			m.add(SWTWidgetUtils.createNoAction("[Select one issue for menu]"));
		}
		else{
			ArchlightElementIdentifier[] elementIdentifiers = issues[0].getElementIdentifiers();
			String[] editors = editorManager.getEditors();
			final ArchlightIssue fissue = issues[0];
			for(int i = 0; i < elementIdentifiers.length; i++){
				String defaultEditor = editorManager.getDefaultEditor();
				if((defaultEditor != null) && (editorManager.isEditorRegistered(defaultEditor))){
					final String fdefaultEditor = defaultEditor;
					final ArchlightElementIdentifier felementIdentifier = elementIdentifiers[i];
					IAction focusDefaultEditor = new Action("Focus on " + elementIdentifiers[i].getElementDescription() + " in " + defaultEditor){
						public void run(){
							focusEditor(fdefaultEditor, fissue, felementIdentifier);
						}
					};
					m.add(focusDefaultEditor);
				}
				if(editors.length > 0){
					MenuManager submenu = new MenuManager("Focus on " + elementIdentifiers[i].getElementDescription() + " in...");
					if(editors.length > 0){
						for(int j = 0; j < editors.length; j++){
							final String feditor = editors[j];
							final ArchlightElementIdentifier felementIdentifier = elementIdentifiers[i];
							IAction focusEditor = new Action(editors[j]){
								public void run(){
									focusEditor(feditor, fissue, felementIdentifier);
								}
							};
							submenu.add(focusEditor);
						}
					}
					m.add(submenu);
					m.add(new Separator());
				}
			}
			IAction detailAction = new Action("Detail..."){
				public void run(){
					showDetailDialog(fissue);
				}
			};
			m.add(detailAction);
			//m.add(SWTWidgetUtils.createNoAction("[Issue Menu Here]"));
		}
		m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

}