package edu.uci.isr.archstudio4.comp.xarchcs.views.changesets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.ContainerGenerator;
import org.eclipse.ui.dialogs.SaveAsDialog;

import edu.uci.isr.archstudio4.comp.xarchcs.XArchCSActivator;
import edu.uci.isr.archstudio4.comp.xarchcs.actions.AddChangeSetAction;
import edu.uci.isr.archstudio4.comp.xarchcs.actions.IHasXArchRef;
import edu.uci.isr.archstudio4.comp.xarchcs.changesetsync.IChangeSetSync.IChangeSetSyncMonitor;
import edu.uci.isr.archstudio4.comp.xarchcs.explicitadt.IExplicitADT;
import edu.uci.isr.archstudio4.comp.xarchcs.views.changesets.conversion.CSConverter;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetEvent;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetInterface;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetListener;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetUtils;
import edu.uci.isr.myx.fw.MyxRegistry;
import edu.uci.isr.myx.fw.MyxRegistryListener;
import edu.uci.isr.sysutils.DelayedExecuteOnceThread;
import edu.uci.isr.sysutils.ListenerList;
import edu.uci.isr.widgets.swt.SWTWidgetUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatEvent;
import edu.uci.isr.xarchflat.XArchFlatListener;

public class ChangeSetViewPart
    extends MyxViewPart
    implements ISelectionProvider, MyxRegistryListener, XArchFlatListener, XArchChangeSetListener, IPartListener, ISelectionChangedListener{

	protected static final boolean equalz(Object o1, Object o2){
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	protected static GridData excludeGridData(){
		GridData d = new GridData();
		d.exclude = true;
		return d;
	}

	ListenerList<ISelectionChangedListener> selectionChangedListeners = new ListenerList<ISelectionChangedListener>(ISelectionChangedListener.class);
	List<ObjRef> selectedRefs = new ArrayList<ObjRef>();

	protected static void addCellEditor(TreeViewer viewer, CellEditor editor){
		CellEditor[] editors = viewer.getCellEditors();
		if(editors == null){
			editors = new CellEditor[0];
		}
		CellEditor[] newEditors = new CellEditor[editors.length + 1];
		System.arraycopy(editors, 0, newEditors, 0, editors.length);
		newEditors[newEditors.length - 1] = editor;
		viewer.setCellEditors(newEditors);
	}

	protected static void addColumnProperty(TreeViewer viewer, String property){
		String[] properties = (String[])viewer.getColumnProperties();
		if(properties == null){
			properties = new String[0];
		}
		String[] newProperties = new String[properties.length + 1];
		System.arraycopy(properties, 0, newProperties, 0, properties.length);
		newProperties[newProperties.length - 1] = property;
		viewer.setColumnProperties(newProperties);
	}

	protected static int indexOf(Item[] items, Object element){
		for(int i = 0; i < items.length; i++){
			if(equalz(items[i].getData(), element)){
				return i;
			}
		}
		return -1;
	}

	protected ChangeSetViewMyxComponent comp = null;
	protected XArchChangeSetInterface xarch;
	protected IExplicitADT explicit;
	protected TreeViewer changeSetViewer = null;
	protected ChangeSetSorter changeSetSorter = null;
	protected Composite notificationComposite = null;
	protected Object ignoreEventsLock = new Object();
	protected int ignoreChangeSetSelectionEvents = 0;

	public ChangeSetViewPart(){
		super(ChangeSetViewMyxComponent.class);
	}

	Collection<Object> myxMapped = new ArrayList<Object>();

	protected void myxMap(Object o){
		if(o != null){
			myxMapped.add(o);
			MyxRegistry.getSharedInstance().map(comp, o);
		}
	}

	protected void createMainMyxPartControl(Composite parent){
		changeSetViewer = new TreeViewer(parent, SWT.MULTI | SWT.FULL_SELECTION){

			boolean needsLabelUpdate = false;
			DelayedExecuteOnceThread labelUpdater = null;

			@Override
			protected synchronized void handleLabelProviderChanged(LabelProviderChangedEvent event){
				/*
				 * We catch calls to this method and then perform a single call
				 * at a later time.
				 */
				if(labelUpdater == null){
					labelUpdater = new DelayedExecuteOnceThread(250, new Runnable(){

						public void run(){
							SWTWidgetUtils.async(changeSetViewer, new Runnable(){

								public void run(){
									superHandleLabelProviderChanged(new LabelProviderChangedEvent(changeSetViewer.getLabelProvider()));
								}
							});
						}
					});
					labelUpdater.start();
				}
				needsLabelUpdate = true;
				labelUpdater.execute();
			}

			private void superHandleLabelProviderChanged(LabelProviderChangedEvent event){
				if(needsLabelUpdate){
					needsLabelUpdate = false;
					super.handleLabelProviderChanged(event);
				}
			}
		};

		changeSetViewer.getControl().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		changeSetViewer.setUseHashlookup(true);
		changeSetViewer.setContentProvider(new ChangeSetContentProvider(xarch));
		changeSetViewer.setComparator(changeSetSorter = new ChangeSetSorter(xarch));
		changeSetViewer.setLabelProvider(new ChangeSetLabelProvider(changeSetViewer, xarch, explicit));
		changeSetViewer.setCellModifier(new ChangeSetCellModifier(changeSetViewer, xarch, explicit));
		changeSetViewer.addPostSelectionChangedListener(new ISelectionChangedListener(){

			public void selectionChanged(SelectionChangedEvent event){
				synchronized(ignoreEventsLock){
					if(ignoreChangeSetSelectionEvents > 0){
						ignoreChangeSetSelectionEvents--;
						return;
					}
				}
				ISelection selection = event.getSelection();
				if(selection instanceof IStructuredSelection){
					ObjRef csRef = null;
					IStructuredSelection ss = (IStructuredSelection)selection;
					if(ss.size() == 1){
						csRef = (ObjRef)ss.getFirstElement();
					}
					ObjRef xArchRef = (ObjRef)changeSetViewer.getInput();
					if(xArchRef != null){
						xarch.setActiveChangeSetRef(xArchRef, csRef);
					}
				}
			}
		});
		changeSetViewer.addOpenListener(new IOpenListener(){

			public void open(OpenEvent event){
				for(Object element: ((IStructuredSelection)event.getSelection()).toArray()){
					changeSetViewer.getCellModifier().modify(element, "Apply", !Arrays.asList(xarch.getAppliedChangeSetRefs((ObjRef)changeSetViewer.getInput())).contains(element));
				}
			}
		});
		changeSetViewer.addSelectionChangedListener(this);
		changeSetViewer.addDragSupport(DND.DROP_MOVE, new Transfer[]{ObjRefTransfer.getInstance()}, new DragSourceAdapter(){

			ObjRef[] data = null;

			@SuppressWarnings("unchecked")
			@Override
			public void dragStart(DragSourceEvent event){
				ISelection selection = changeSetViewer.getSelection();
				if(selection instanceof IStructuredSelection){
					data = (ObjRef[])((IStructuredSelection)selection).toList().toArray(new ObjRef[0]);
				}
				event.doit &= data != null;

				// it seems like this should be done automatically!?
				if(changeSetViewer.isCellEditorActive()){
					for(CellEditor e: changeSetViewer.getCellEditors()){
						if(e != null){
							e.deactivate();
						}
					}
				}
			}

			@Override
			public void dragSetData(DragSourceEvent event){
				if(ObjRefTransfer.getInstance().isSupportedType(event.dataType) && data != null){
					event.data = data.clone();
				}
			}

			@Override
			public void dragFinished(DragSourceEvent event){
				if(!event.doit){
					return;
				}
			}
		});
		changeSetViewer.addDropSupport(DND.DROP_MOVE, new Transfer[]{ObjRefTransfer.getInstance()}, new ViewerDropAdapter(changeSetViewer){

			@Override
			protected int determineLocation(DropTargetEvent event){
				if(!(event.item instanceof Item)){
					return LOCATION_NONE;
				}
				Item item = (Item)event.item;
				Point coordinates = new Point(event.x, event.y);
				coordinates = changeSetViewer.getControl().toControl(coordinates);
				if(item != null){
					Rectangle bounds = getBounds(item);
					if(bounds == null){
						return LOCATION_NONE;
					}
					if(coordinates.y - (bounds.y + bounds.height / 2) < 0){
						return LOCATION_BEFORE;
					}
					else{
						return LOCATION_AFTER;
					}
				}
				return LOCATION_ON;
			}

			@Override
			public boolean validateDrop(Object target, int operation, TransferData transferType){
				return ObjRefTransfer.getInstance().isSupportedType(transferType);
			}

			@Override
			public boolean performDrop(Object data){

				if(data instanceof ObjRef[] && ((ObjRef[])data).length > 0){
					if(xarch.isInstanceOf(((ObjRef[])data)[0], "changesets#ChangeSet")){
						int newIndex = indexOf(changeSetViewer.getTree().getItems(), getCurrentTarget());
						if(newIndex >= 0){
							if(getCurrentLocation() == ViewerDropAdapter.LOCATION_AFTER){
								newIndex++;
							}
							XArchChangeSetUtils.move(xarch, (ObjRef)changeSetViewer.getInput(), (ObjRef[])data, -newIndex - 1, null);
							return true;
						}
					}
				}
				return false;
			}
		});

		//		Tree tree = changeSetViewer.getTree();
		//		tree.setHeaderVisible(true);
		//		tree.setLinesVisible(true);
		//		//tree.setLayout(new TableLayout());
		//
		//		TreeViewerColumn vColumn;
		//		TreeColumn tColumn;
		//
		//		vColumn = new TreeViewerColumn(changeSetViewer, SWT.LEFT);
		//		vColumn.setEditingSupport(new ChangeSetColumnEditorSupport(changeSetViewer, xarch));
		//		vColumn.setLabelProvider(new ChangeSetColumnLabelProvider(xarch));
		//		tColumn = vColumn.getColumn();
		//		tColumn.setText("Change Set");
		//		tColumn.setAlignment(SWT.LEFT);
		//		tColumn.setMoveable(false);
		//		tColumn.setResizable(true);
		//		tColumn.setWidth(20);

		Tree tree = changeSetViewer.getTree();
		TreeColumn column;
		CellEditor editor;
		TableLayout layout = new TableLayout();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		tree.setLayout(layout);

		/*
		 * Feature in Windows. The first column in a windows table reserves room
		 * for a check box. The fix is to have this column with a width of zero
		 * to hide it.
		 */
		column = new TreeColumn(tree, SWT.CENTER);
		column.setText("Windows first column fix");
		column.setImage(null);
		column.setResizable(false);
		column.setAlignment(SWT.CENTER);
		layout.addColumnData(new ColumnPixelData(1, column.getResizable()));
		editor = null;
		addCellEditor(changeSetViewer, editor);
		addColumnProperty(changeSetViewer, column.getText());

		column = new TreeColumn(tree, SWT.CENTER);
		column.setText("");
		column.setImage(XArchCSActivator.getDefault().getImageRegistry().get("res/icons/applied.gif"));
		column.setResizable(false);
		column.setAlignment(SWT.CENTER);
		layout.addColumnData(new ColumnPixelData(20, column.getResizable()));
		editor = new CheckboxCellEditor(tree);
		addCellEditor(changeSetViewer, editor);
		addColumnProperty(changeSetViewer, "Apply");
		column = new TreeColumn(tree, SWT.CENTER);
		column.setText("");
		column.setImage(XArchCSActivator.getDefault().getImageRegistry().get("res/icons/explicit.gif"));
		column.setResizable(false);
		column.setAlignment(SWT.CENTER);
		layout.addColumnData(new ColumnPixelData(20, column.getResizable()));
		editor = new CheckboxCellEditor(tree);
		addCellEditor(changeSetViewer, editor);
		addColumnProperty(changeSetViewer, "View");

		column = new TreeColumn(tree, SWT.LEFT);
		column.setText("Change Set");
		column.setResizable(true);
		column.setAlignment(SWT.LEFT);
		layout.addColumnData(new ColumnWeightData(1, column.getResizable()));
		editor = new TextCellEditor(tree);
		addCellEditor(changeSetViewer, editor);
		addColumnProperty(changeSetViewer, column.getText());

		ObjRef xArchRef = null;

		IEditorPart editorPart = getSite().getPage().getActiveEditor();
		if(editorPart != null && editorPart.getEditorInput() instanceof IFileEditorInput){
			IFileEditorInput fileEditorInput = (IFileEditorInput)editorPart.getEditorInput();
			xArchRef = xarch.getOpenXArch(fileEditorInput.getFile().getFullPath().makeAbsolute().toString());
		}

		changeSetViewer.setInput(xArchRef);

		getViewSite().getActionBars().getToolBarManager().add(new AddChangeSetAction(xarch));
		getViewSite().getActionBars().updateActionBars();

		/*
		 * This needs to be here since we always want to update the change sets
		 * viewer before the relationships viewer.
		 */
		myxMap(this);
		myxMap(changeSetViewer.getContentProvider());
		myxMap(changeSetViewer.getLabelProvider());
		myxMap(changeSetViewer.getSorter());
		getViewSite().setSelectionProvider(this);

		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();

		IAction diffToExternalFile = new Action("Create Diff to External File"){

			@Override
			public void run(){
				FileDialog fileDialog = new FileDialog(changeSetViewer.getControl().getShell(), SWT.OPEN);
				fileDialog.setFilterExtensions(new String[]{"*.xml"});
				fileDialog.setFilterNames(new String[]{"XML Files (*.xml)"});
				fileDialog.setText("Select File");
				fileDialog.open();
				String fileName = fileDialog.getFileName();
				String filePath = fileDialog.getFilterPath();
				if(fileName != null && !"".equals(fileName.trim())){
					try{
						ObjRef targetXArchRef = xarch.parseFromFile(filePath + java.io.File.separator + fileName);
						ObjRef sourceXArchRef = (ObjRef)changeSetViewer.getInput();
						xarch.diffToExternalFile(sourceXArchRef, targetXArchRef);
					}
					catch(Exception e){
						String[] labels = {"Ok"};
						MessageDialog dialog = new MessageDialog(changeSetViewer.getControl().getShell(), "Error", null, e.getMessage(), MessageDialog.ERROR, labels, 0);
						dialog.open();
					}
				}
			}
		};
		menuManager.add(diffToExternalFile);

		// not yet enabled
		//IAction diffFromExternalFile = new Action("Create Diff from External File"){
		//
		//	@Override
		//	public void run(){
		//		FileDialog fileDialog = new FileDialog(changeSetViewer.getControl().getShell(), SWT.OPEN);
		//		fileDialog.setFilterExtensions(new String[]{"*.xml"});
		//		fileDialog.setFilterNames(new String[]{"XML Files (*.xml)"});
		//		fileDialog.open();
		//		String fileName = fileDialog.getFileName();
		//		String filePath = fileDialog.getFilterPath();
		//		if(fileName != null && !"".equals(fileName.trim())){
		//			try{
		//				ObjRef targetXArchRef = xarch.parseFromFile(filePath + java.io.File.separator + fileName);
		//				ObjRef sourceXArchRef = (ObjRef)changeSetViewer.getInput();
		//				xarch.diffFromExternalFile(sourceXArchRef, targetXArchRef);
		//			}
		//			catch(Exception e){
		//				String[] labels = {"Ok"};
		//				MessageDialog dialog = new MessageDialog(changeSetViewer.getControl().getShell(), "Error", null, e.getMessage(), MessageDialog.ERROR, labels, 0);
		//				dialog.open();
		//			}
		//		}
		//	}
		//};
		//menuManager.add(diffFromExternalFile);

		IAction convertPLAtoCS = new Action("Convert PLA Guards to Change Sets"){

			private String rename(String s){
				if(s.endsWith(".xml")){
					s = s.substring(0, s.length() - 4) + "-cs.xml";
				}
				return s;
			}

			@Override
			public void run(){

				final ObjRef oldXArchRef = (ObjRef)changeSetViewer.getInput();
				final IWorkspace workspace = ResourcesPlugin.getWorkspace();
				final IPath originalPath = new Path(rename(xarch.getXArchURI(oldXArchRef)));
				final IFile originalFile = workspace.getRoot().getFile(originalPath);

				SaveAsDialog sad = new SaveAsDialog(changeSetViewer.getControl().getShell());
				sad.setOriginalFile(originalFile);
				sad.open();

				final IPath path = sad.getResult();

				if(path != null){
					new Thread(new Runnable(){

						public void run(){
							try{
								xarch.close(path.toString());
							}
							catch(Throwable t){
							}
							final ObjRef newXArchRef = xarch.createXArch(path.toString());
							try{
								CSConverter.convertPLAtoCS(xarch, oldXArchRef, newXArchRef);
								SWTWidgetUtils.sync(changeSetViewer, new Runnable(){

									public void run(){
										String contents = xarch.serialize(newXArchRef);
										InputStream is = new ByteArrayInputStream(contents.getBytes());
										saveFile(changeSetViewer.getControl().getShell(), is, path);
									}
								});
							}
							catch(Throwable t){
								t.printStackTrace();
							}
							finally{
								if(newXArchRef != null){
									xarch.close(newXArchRef);
								}
							}
						}
					}).start();
				}
			}
		};
		menuManager.add(convertPLAtoCS);
	}

	@Override
	public void createMyxPartControl(Composite parent){
		comp = MyxRegistry.getSharedInstance().waitForBrick(ChangeSetViewMyxComponent.class);
		xarch = comp.xarch;
		explicit = comp.explicit;

		parent.setLayout(new GridLayout());

		notificationComposite = new Composite(parent, SWT.BORDER);

		notificationComposite.setLayoutData(excludeGridData());
		notificationComposite.setVisible(false);

		createMainMyxPartControl(parent);

		getSite().getPage().addPartListener(this);

		updateStatus();
	}

	@Override
	public void dispose(){
		for(Object o: myxMapped){
			MyxRegistry.getSharedInstance().unmap(comp, o);
		}

		getSite().getPage().removePartListener(this);

		super.dispose();
	}

	@Override
	public void setMyxFocus(){
		changeSetViewer.getControl().setFocus();
	}

	public void handleXArchChangeSetEvent(final XArchChangeSetEvent evt){
		if(evt.getEventType() == XArchChangeSetEvent.ChangeSetEventType.UPDATED_ACTIVE_CHANGE_SET){
			SWTWidgetUtils.async(changeSetViewer, new Runnable(){

				public void run(){
					if(equalz(evt.getXArchRef(), changeSetViewer.getInput())){
						ObjRef activeChangeSetRef = evt.getActiveChangeSet();
						StructuredSelection selection = StructuredSelection.EMPTY;
						if(activeChangeSetRef != null){
							selection = new StructuredSelection(activeChangeSetRef);
						}
						ignoreChangeSetSelectionEvents++;
						changeSetViewer.setSelection(selection);
					}
				}
			});
		}
		if(evt.getEventType() == XArchChangeSetEvent.ChangeSetEventType.UPDATED_ENABLED){
			SWTWidgetUtils.async(changeSetViewer, new Runnable(){

				public void run(){
					if(equalz(evt.getXArchRef(), changeSetViewer.getInput())){
						updateStatus();
					}
				}
			});
		}
	}

	public void handleXArchFlatEvent(XArchFlatEvent evt){
		if(changeSetSorter != null){
			changeSetSorter.handleXArchFlatEvent(evt);
		}
	}

	protected void setInput(ObjRef xArchRef){
		if(!equalz(changeSetViewer.getInput(), xArchRef)){
			changeSetViewer.setInput(xArchRef);
			updateStatus();
		}
	}

	public void partActivated(final IWorkbenchPart part){
		if(part instanceof IEditorPart){
			IEditorPart editorPart = (IEditorPart)part;
			if(editorPart.getEditorInput() instanceof IFileEditorInput){
				IFileEditorInput fileEditorInput = (IFileEditorInput)editorPart.getEditorInput();
				setInput(xarch.getOpenXArch(fileEditorInput.getFile().getFullPath().makeAbsolute().toString()));
			}
		}
	}

	public void partDeactivated(IWorkbenchPart part){
	}

	public void partClosed(final IWorkbenchPart part){
		if(part instanceof IEditorPart){
			IEditorPart editorPart = getSite().getPage().getActiveEditor();
			if(editorPart == null){
				setInput(null);
			}
			else if(editorPart.getEditorInput() instanceof IFileEditorInput){
				IFileEditorInput fileEditorInput = (IFileEditorInput)editorPart.getEditorInput();
				setInput(xarch.getOpenXArch(fileEditorInput.getFile().getFullPath().makeAbsolute().toString()));
			}
		}
	}

	public void partBroughtToTop(IWorkbenchPart part){
	}

	public void partOpened(IWorkbenchPart part){
	}

	protected void updateStatus(){
		boolean enabled = false;

		for(Control c: notificationComposite.getChildren()){
			c.dispose();
		}
		ObjRef xArchRef = (ObjRef)changeSetViewer.getInput();
		if(xArchRef == null){
			notificationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			notificationComposite.setVisible(true);

			notificationComposite.setLayout(new GridLayout());
			notificationComposite.setBackground(notificationComposite.getDisplay().getSystemColor(SWT.COLOR_WHITE));

			Label message = new Label(notificationComposite, SWT.CENTER | SWT.WRAP);
			message.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
			message.setText("Open a xADL document to use change sets.");
			message.setBackground(notificationComposite.getBackground());
			message.setForeground(message.getDisplay().getSystemColor(SWT.COLOR_BLACK));

			// notificationComposite.setLayoutData(excludeGridData());
			// notificationComposite.setVisible(false);

		}
		else if(!xarch.getChangeSetsEnabled(xArchRef)){
			notificationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			notificationComposite.setVisible(true);

			notificationComposite.setLayout(new GridLayout());
			notificationComposite.setBackground(notificationComposite.getDisplay().getSystemColor(SWT.COLOR_RED));

			Label message = new Label(notificationComposite, SWT.CENTER | SWT.WRAP);
			message.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
			message.setText("Change Sets are currently disabled for this document.");
			message.setBackground(message.getDisplay().getSystemColor(SWT.COLOR_RED));
			message.setForeground(message.getDisplay().getSystemColor(SWT.COLOR_WHITE));

			Button enableButton = new Button(notificationComposite, SWT.PUSH);
			enableButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
			enableButton.setText("Enable");
			enableButton.addSelectionListener(new SelectionAdapter(){

				@Override
				public void widgetSelected(SelectionEvent event){
					final ObjRef xArchRef = (ObjRef)changeSetViewer.getInput();
					if(xArchRef != null){
						try{
							// PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress(){
							// see: https://bugs.eclipse.org/bugs/show_bug.cgi?id=156687
							ProgressMonitorDialog pd = new ProgressMonitorDialog(changeSetViewer.getControl().getShell());
							pd.run(true, true, new IRunnableWithProgress(){

								public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException{
									monitor.beginTask("Creating a baseline change set for the current document...", 1);
									xarch.enableChangeSets(xArchRef, new IChangeSetSyncMonitor(){

										IProgressMonitor m = monitor;

										public void beginTask(int totalWork){
											m = new SubProgressMonitor(m, 1);
											m.beginTask("", totalWork);
										}

										public void worked(int work){
											m.worked(work);
										}

										public void done(){
											m.done();
											if(m instanceof SubProgressMonitor){
												m = ((SubProgressMonitor)m).getWrappedProgressMonitor();
											}
										}

										public boolean isCanceled(){
											return m.isCanceled();
										}

										public void setCanceled(boolean canceled){
											m.setCanceled(canceled);
										}
									});
									monitor.done();
								}
							});
						}
						catch(InvocationTargetException e){
							e.printStackTrace();
						}
						catch(InterruptedException e){
							e.printStackTrace();
						}
					}
				}
			});
		}
		else{
			enabled = true;
			notificationComposite.setLayoutData(excludeGridData());
			notificationComposite.setVisible(false);
			ObjRef activeChangeSetRef = xarch.getActiveChangeSetRef(xArchRef);
			++ignoreChangeSetSelectionEvents;
			changeSetViewer.setSelection(activeChangeSetRef != null ? new StructuredSelection(activeChangeSetRef) : StructuredSelection.EMPTY);
		}

		changeSetViewer.getControl().setEnabled(enabled);
		for(IContributionItem item: getViewSite().getActionBars().getToolBarManager().getItems()){
			if(item instanceof ActionContributionItem){
				IAction action = ((ActionContributionItem)item).getAction();
				if(action instanceof IHasXArchRef){
					((IHasXArchRef)action).setXArchRef(enabled ? xArchRef : null);
				}
			}
		}

		notificationComposite.layout();
		notificationComposite.getParent().layout();
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener){
		selectionChangedListeners.add(listener);
	}

	public ISelection getSelection(){
		return new StructuredSelection(selectedRefs);
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener){
		selectionChangedListeners.remove(listener);
	}

	public void setSelection(ISelection selection){
		// TODO Auto-generated method stub

	}

	protected void fireSelectionChangedEvent(ISelection selection){
		SelectionChangedEvent evt = new SelectionChangedEvent(this, selection);
		for(ISelectionChangedListener l: selectionChangedListeners.getListeners()){
			l.selectionChanged(evt);
		}
	}

	public void selectionChanged(SelectionChangedEvent event){
		Iterator iterator = ((IStructuredSelection)event.getSelection()).iterator();
		if(iterator != null){
			selectedRefs.clear();
			while(iterator.hasNext()){
				Object obj = iterator.next();
				if(obj != null && obj instanceof ObjRef){
					ObjRef ref = (ObjRef)obj;
					selectedRefs.add(ref);
				}
			}
			fireSelectionChangedEvent(new StructuredSelection(selectedRefs));
		}
	}

	private static void saveFile(Shell shell, InputStream contents, IPath initialTargetPath){
		final Shell fshell = shell;
		final InputStream fcontents = contents;
		final IPath targetPath = initialTargetPath;

		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IFile targetFile = workspace.getRoot().getFile(targetPath);

		WorkspaceModifyOperation operation = new WorkspaceModifyOperation(){

			@Override
			protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException{
				IPath targetContainerPath = targetPath.removeLastSegments(1);
				boolean createContainer = true;
				if(workspace.getRoot().getContainerForLocation(targetContainerPath) != null){
					createContainer = false;
				}
				ContainerGenerator gen = new ContainerGenerator(targetContainerPath);
				IContainer res = null;
				try{
					if(createContainer){
						res = gen.generateContainer(monitor); // creates project A and folder B if required
					}
					if(targetFile.exists()){
						targetFile.delete(false, monitor);
					}
					targetFile.create(fcontents, false, monitor);
					try{
						fcontents.close();
					}
					catch(IOException ioe){
					}
				}
				catch(CoreException e){
					MessageDialog.openError(fshell, "Error", "Could not save file: " + e.getMessage());
					return;
				}
				catch(OperationCanceledException e){
					return;
				}
			}
		};
		try{
			operation.run(null);
		}
		catch(InterruptedException e){
		}
		catch(InvocationTargetException ite){
		}
	}

}
