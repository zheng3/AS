package edu.uci.isr.archstudio4.comp.archlight;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;

import edu.uci.isr.archstudio4.archlight.ArchlightTest;
import edu.uci.isr.archstudio4.archlight.IArchlightTool;
import edu.uci.isr.archstudio4.comp.archlight.testadt.IArchlightTestADT;
import edu.uci.isr.archstudio4.comp.resources.IResources;
import edu.uci.isr.archstudio4.editors.AbstractArchstudioEditor;
import edu.uci.isr.archstudio4.editors.AbstractArchstudioOutlinePage;
import edu.uci.isr.widgets.swt.DropdownSelectionListener;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatEvent;

public class ArchlightEditor extends AbstractArchstudioEditor{
	protected IArchlightTestADT testadt = null;
	protected IArchlightTool tools = null;

	public ArchlightEditor(){
		super(ArchlightMyxComponent.class, ArchlightMyxComponent.EDITOR_NAME);
		testadt = ((ArchlightMyxComponent)comp).getTestADT();
		tools = ((ArchlightMyxComponent)comp).getTools();

		ArchlightUtils.initResources(resources);
		
		setBannerInfo(((ArchlightMyxComponent)comp).getIcon(), 
			"Architecture Analysis Framework");
		setHasBanner(true);
	}

	public void init(IEditorSite site, IEditorInput input)
		throws PartInitException{
		super.init(site, input);

		//setSite(site);
		//setInput(input);

		setupToolbar(site);
	}
	
	protected AbstractArchstudioOutlinePage createOutlinePage(){
		return new ArchlightOutlinePage(testadt, xarch, getXArchRef(), resources);
	}

	protected void setupToolbar(IEditorSite site){
		IActionBars bars = site.getActionBars();
		IToolBarManager manager = bars.getToolBarManager();
		IAction[] actions = getToolbarActions();
		for(int i = 0; i < actions.length; i++){
			manager.add(actions[i]);
		}
	}

	public void createEditorContents(Composite c){
		Object[] selectedNodes = null;
		if(outlinePage != null){
			selectedNodes = outlinePage.getSelectedObjects();
		}
		
		Label sep1 = new Label(c, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep1.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
			| GridData.GRAB_HORIZONTAL));

		Composite cButtons = new Composite(c, SWT.NONE);
		cButtons.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridLayout cButtonsGridLayout = new GridLayout(4, false);
		cButtonsGridLayout.marginTop = 5;
		cButtonsGridLayout.marginBottom = 5;
		cButtonsGridLayout.marginLeft = 5;
		cButtonsGridLayout.marginRight = 5;
		cButtons.setLayout(cButtonsGridLayout);

		Button bRunTests = new Button(cButtons, SWT.NONE);
		bRunTests.setText("Run Tests");
		bRunTests.setImage(resources.getImage(ArchlightUtils.IMAGE_RUN_TESTS));
		bRunTests.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent event){
				runTests();
			}

			public void widgetDefaultSelected(SelectionEvent event){
				runTests();
			}
		});

		Label lRunTests = new Label(cButtons, SWT.LEFT);
		lRunTests.setBackground(c.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		lRunTests.setFont(resources
			.getPlatformFont(IResources.PLATFORM_DEFAULT_FONT_ID));
		lRunTests.setText("Click this button to run all enabled tests.");

		Button bReloadTests = new Button(cButtons, SWT.NONE);
		bReloadTests.setText("Reload Tests");
		bReloadTests
			.setImage(resources.getImage(ArchlightUtils.IMAGE_RELOAD_TESTS));
		bReloadTests.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent event){
				reloadTests();
			}

			public void widgetDefaultSelected(SelectionEvent event){
				reloadTests();
			}
		});

		GridData separatorData = new GridData();
		separatorData.horizontalIndent = 15;
		bReloadTests.setLayoutData(separatorData);

		Label lReloadTests = new Label(cButtons, SWT.LEFT);
		lReloadTests.setBackground(c.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		lReloadTests.setFont(resources
			.getPlatformFont(IResources.PLATFORM_DEFAULT_FONT_ID));
		lReloadTests.setText("Click this button to reload all tests.");

		Label sep2 = new Label(c, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep2.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
			| GridData.GRAB_HORIZONTAL));

		if((selectedNodes == null) || (selectedNodes.length == 0)){
			Label lNothingSelected = new Label(c, SWT.LEFT);
			lNothingSelected.setBackground(c.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));
			lNothingSelected.setFont(resources
				.getPlatformFont(IResources.PLATFORM_DEFAULT_FONT_ID));
			lNothingSelected
				.setText("Select one or more elements in the outline view.");
		}
		else{
			for(int i = 0; i < selectedNodes.length; i++){
				if(selectedNodes[i] instanceof ObjRef){
					Label lElement = new Label(c, SWT.LEFT);
					lElement
						.setBackground(c.getDisplay().getSystemColor(SWT.COLOR_WHITE));
					lElement.setFont(resources
						.getPlatformFont(IResources.PLATFORM_DEFAULT_FONT_ID));
					lElement
						.setText("Select a sub-node of the document for more detail.");
				}
				else if((selectedNodes[i] instanceof FolderNode)
					|| (selectedNodes[i] instanceof ArchlightTest)){
					final Object node = selectedNodes[i];
					boolean isFolder = node instanceof FolderNode;

					Label lElement = new Label(c, SWT.LEFT);
					lElement
						.setBackground(c.getDisplay().getSystemColor(SWT.COLOR_WHITE));
					lElement.setFont(resources
						.getPlatformFont(IResources.PLATFORM_HEADER_FONT_ID));
					if(isFolder){
						lElement.setText("Test Folder: "
							+ ((FolderNode)node).getLastSegment());
					}
					else{
						lElement.setText("Test: "
							+ ArchlightTest
								.getLastCategoryPathComponent(((ArchlightTest)node)
									.getCategory()));
					}

					Group g = new Group(c, SWT.NONE);
					g.setBackground(c.getDisplay().getSystemColor(SWT.COLOR_WHITE));
					GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL
						| GridData.GRAB_HORIZONTAL);
					gd.verticalIndent = 5;
					gd.horizontalIndent = 10;
					g.setLayoutData(gd);

					GridLayout groupGridLayout = new GridLayout(1, true);
					groupGridLayout.marginTop = 5;
					groupGridLayout.marginBottom = 5;
					groupGridLayout.marginLeft = 5;
					groupGridLayout.marginLeft = 5;
					groupGridLayout.verticalSpacing = 5;
					g.setLayout(groupGridLayout);

					g.setText("Manage Test Folder");

					Label lElementDesc = new Label(g, SWT.LEFT);
					lElementDesc.setBackground(g.getDisplay().getSystemColor(
						SWT.COLOR_WHITE));
					lElementDesc.setFont(resources
						.getPlatformFont(IResources.PLATFORM_DEFAULT_FONT_ID));
					if(isFolder){
						lElementDesc
							.setText("This is a test folder.  It is a container for other tests.  You can use it to enable or disable tests as a group.");
					}
					else{
						lElementDesc.setText(((ArchlightTest)node).getLongDescription());
					}

					ToolBar toolBar = new ToolBar(g, SWT.HORIZONTAL | SWT.RIGHT);
					toolBar.setBackground(g.getDisplay().getSystemColor(SWT.COLOR_WHITE));

					String dropDownImageID = isFolder ? ISharedImages.IMG_OBJ_FOLDER
						: ISharedImages.IMG_OBJ_FILE;
					String dropDownText = isFolder ? "Change Test States"
						: "Change Test State";
					ToolItem dropDownButton = createToolItem(toolBar, SWT.DROP_DOWN,
						dropDownText, resources.getPlatformImage(dropDownImageID), null,
						dropDownText);
					dropDownButton.addSelectionListener(new DropdownSelectionListener(
						dropDownButton){
						public void fillDropdownMenu(IMenuManager menuMgr){
							IAction[] actions = ArchlightUtils.createTestMenuActions(xarch,
								xArchRef, testadt.getAllTests(), resources, node);
							if(actions.length == 0){
								Action noAction = new Action("[No Actions]"){
									public void run(){
									}
								};
								noAction.setEnabled(false);
								menuMgr.add(noAction);
							}
							for(int i = 0; i < actions.length; i++){
								menuMgr.add(actions[i]);
							}
						}
					});
				}

				Label sep = new Label(c, SWT.SEPARATOR | SWT.HORIZONTAL);
				sep.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
					| GridData.GRAB_HORIZONTAL));
			}
		}
	}
	
	private ToolItem createToolItem(ToolBar parent, int type, String text,
		Image image, Image hotImage, String toolTipText){
		ToolItem item = new ToolItem(parent, type);
		item.setText(text);
		item.setImage(image);
		item.setHotImage(hotImage);
		item.setToolTipText(toolTipText);
		return item;
	}

	public void setFocus(){
		// parent.getChildren()[0].setFocus();
	}

	public IAction[] getToolbarActions(){
		Action runTests = new Action("Run Tests", Action.AS_PUSH_BUTTON){
			public void run(){
				runTests();
			};
		};
		runTests.setImageDescriptor(resources
			.getImageDescriptor(ArchlightUtils.IMAGE_RUN_TESTS));
		runTests.setToolTipText("Run Tests");

		Action reloadTests = new Action("Reload Tests", Action.AS_PUSH_BUTTON){
			public void run(){
				reloadTests();
			};
		};
		reloadTests.setImageDescriptor(resources
			.getImageDescriptor(ArchlightUtils.IMAGE_RELOAD_TESTS));
		reloadTests.setToolTipText("Reload Tests");

		return new IAction[]{ runTests, reloadTests };
	}

	protected void reloadTests(){
		tools.reloadTests();
	}

	protected void runTests(){
		ArchlightDocTest[] docTests = ArchlightUtils.loadDocTests(xarch, xArchRef);
		java.util.List<String> testUIDlist = new ArrayList<String>(docTests.length);
		for(int i = 0; i < docTests.length; i++){
			if(docTests[i].isEnabled()){
				testUIDlist.add(docTests[i].getTestUID());
			}
		}
		String[] testUIDs = (String[])testUIDlist.toArray(new String[testUIDlist
			.size()]);
		tools.runTests(xArchRef, testUIDs);
		
		try{
			getEditorSite().getPage().showView(ArchlightUtils.ARCHLIGHT_ISSUE_VIEW_ECLIPSE_ID);
		}
		catch(PartInitException pe){}
	}
}
