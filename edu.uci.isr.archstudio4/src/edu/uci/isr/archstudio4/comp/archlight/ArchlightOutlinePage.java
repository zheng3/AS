package edu.uci.isr.archstudio4.comp.archlight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchSite;

import edu.uci.isr.archstudio4.archlight.ArchlightTest;
import edu.uci.isr.archstudio4.comp.archlight.testadt.IArchlightTestADT;
import edu.uci.isr.archstudio4.comp.resources.IResources;
import edu.uci.isr.archstudio4.editors.AbstractArchstudioOutlinePage;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public class ArchlightOutlinePage extends AbstractArchstudioOutlinePage{
	protected IArchlightTestADT testadt = null;
	
	protected ArchlightTest[] tests = null;
	protected ArchlightDocTest[] doctests = null;
	
	public ArchlightOutlinePage(IArchlightTestADT testadt, XArchFlatInterface xarch, ObjRef xArchRef, IResources resources){
		super(xarch, xArchRef, resources, false, true);
		this.testadt = testadt;
		
		updateTests();
		updateDocTests();
	}
	
	public void createControl(Composite parent){
		super.createControl(parent);
		getTreeViewer().expandToLevel(4);
	}
	
	protected ITreeContentProvider createViewContentProvider(){
		return new ViewContentProvider();
	}
	
	protected ILabelProvider createViewLabelProvider(){
		return new ViewLabelProvider();
	}
	
	public void updateTests(){
		tests = testadt.getAllTests();
	}
	
	public void updateDocTests(){
		doctests = ArchlightUtils.loadDocTests(xarch, xArchRef);
	}
	
	public void updateOutlinePage(){
		updateTests();
		updateDocTests();
		super.updateOutlinePage();
	}
	
	protected int getTestState(ArchlightTest test){
		String uid = test.getUID();
		for(int i = 0; i < doctests.length; i++){
			if(doctests[i].getTestUID().equals(uid)){
				if(doctests[i].isEnabled()){
					return ArchlightUtils.APPLIED;
				}
				else{
					return ArchlightUtils.DISABLED;
				}
			}
		}
		return ArchlightUtils.NOT_APPLIED;
	}
	
	/* We do an interesting thing here; namely that the tree itself
	 * is fake and not structured like a tree.  Instead, we just have
	 * the list of current tests.  The view content provider iterates
	 * through those and responds to getChildren based on the contents
	 * of that list.  Intermediate FolderNodes are created as necessary
	 * as placeholders.
	 */
	class ViewContentProvider implements ITreeContentProvider{
		private Object[] EMPTY_ARRAY = new Object[0];
		
		public Object[] getElements(Object inputElement){
			return getChildren(inputElement);
		}
		
		public Object[] getChildren(Object parentElement){
			if(parentElement instanceof IWorkbenchSite){
				return new Object[]{xArchRef};
			}
			else if((parentElement instanceof ObjRef) || (parentElement instanceof FolderNode)){
				//Figure out where we are
				String[] currentLocation = null;
				if(parentElement instanceof ObjRef){
					currentLocation = new String[0];
				}
				else if(parentElement instanceof FolderNode){
					currentLocation = ((FolderNode)parentElement).getPathSegments();
				}
				else{
					throw new RuntimeException("Unknown node in tree: " + parentElement);
				}
				
				//Find all the children of where we are
				List children = new ArrayList();
				ArchlightTest[] ts = tests;
				for(int i = 0; i < ts.length; i++){
					String[] testLocation = ArchlightTest.getCategoryPathComponents(ts[i].getCategory());
					if(testLocation.length <= currentLocation.length) continue;
					boolean matches = true;
					for(int j = 0; j < currentLocation.length; j++){
						if(!currentLocation[j].equals(testLocation[j])){
							matches = false;
							break;
						}
					}
					if(matches){
						//Okay, this is at least a descendant of our current location
						if(testLocation.length == (currentLocation.length + 1)){
							//It's a direct child
							children.add(ts[i]);
						}
						else{
							//It's a descendant and we need to put in a foldernode
							String[] folderNodeLocation = new String[currentLocation.length + 1];
							System.arraycopy(currentLocation, 0, folderNodeLocation, 0, currentLocation.length);
							folderNodeLocation[currentLocation.length] = testLocation[currentLocation.length];
							FolderNode fn = new FolderNode(folderNodeLocation);
							if(!children.contains(fn)){
								children.add(fn);
							}
						}
					}
				}
				//Sort the list of children
				Collections.sort(children, new Comparator(){
					public int compare(Object o1, Object o2){
						if((o1 instanceof FolderNode) && (o2 instanceof ArchlightTest)){
							return -1; //folders always before tests
						}
						else if((o1 instanceof ArchlightTest) && (o2 instanceof FolderNode)){
							return 1; //Tests always after folders
						}
						else if((o1 instanceof FolderNode) && (o2 instanceof FolderNode)){
							return ((FolderNode)o1).getLastSegment().compareToIgnoreCase(((FolderNode)o2).getLastSegment());
						}
						else if((o1 instanceof ArchlightTest) && (o2 instanceof ArchlightTest)){
							return ArchlightTest.getLastCategoryPathComponent(((ArchlightTest)o1).getCategory()).compareTo(
								ArchlightTest.getLastCategoryPathComponent(((ArchlightTest)o2).getCategory()));
						}
						return 0;
					};
				});
				return children.toArray();
			}
			return EMPTY_ARRAY;
		}
		
		public Object getParent(Object element){
			return null;
		}
		
		public boolean hasChildren(Object element){
			if(element instanceof IWorkbenchSite){
				return true;
			}
			else if(element instanceof ObjRef){
				return true;
			}
			else if(element instanceof FolderNode){
				return true;
			}
			else if(element instanceof ArchlightTest){
				return false;
			}
			return false;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput){
		}
		
		public void dispose(){
		}
	}
	
	class ViewLabelProvider extends LabelProvider implements ILabelProvider{
		public Image getImage(Object element){
			if(element instanceof ObjRef){
				return resources.getPlatformImage(ISharedImages.IMG_OBJ_FOLDER);
			}
			else if(element instanceof FolderNode){
				return resources.getPlatformImage(ISharedImages.IMG_OBJ_FOLDER);
			}
			else if(element instanceof ArchlightTest){
				switch(getTestState((ArchlightTest)element)){
				case ArchlightUtils.APPLIED:
					return resources.getImage(ArchlightUtils.IMAGE_TEST_APPLIED);
				case ArchlightUtils.DISABLED:
					return resources.getImage(ArchlightUtils.IMAGE_TEST_DISABLED);
				case ArchlightUtils.NOT_APPLIED:
					return resources.getImage(ArchlightUtils.IMAGE_TEST_NOTAPPLIED);
				}
				return resources.getPlatformImage(ISharedImages.IMG_OBJ_FILE);
			}
			return null;
		}
		
		public String getText(Object element){
			if(element instanceof ObjRef){
				String uri = xarch.getXArchURI((ObjRef)element);
				if(uri == null){
					return "Document";
				}
				else{
					return uri;
				}
			}
			else if(element instanceof FolderNode){
				return ((FolderNode)element).getLastSegment();
			}
			else if(element instanceof ArchlightTest){
				return ArchlightTest.getLastCategoryPathComponent(((ArchlightTest)element).getCategory());
			}
			return super.getText(element);
		}
	}
	
	public Object[] getSelectedNodes(){
		ISelection selection = getSelection();
		if(selection instanceof IStructuredSelection){
			IStructuredSelection ss = (IStructuredSelection)selection;
			Object[] nodes = ss.toArray();
			return nodes;
		}
		else{
			return new Object[0];
		}
	}
	
	protected void fillContextMenu(IMenuManager menuMgr){
		Object[] selectedNodes = getSelectedNodes();
		if(selectedNodes.length == 0){
			Action noAction = new Action("[No Selection]"){
				public void run(){}
			};
			noAction.setEnabled(false);
			menuMgr.add(noAction);
		}
		else if(selectedNodes.length > 1){
			Action noAction = new Action("[Select One Node for Menu]"){
				public void run(){}
			};
			noAction.setEnabled(false);
			menuMgr.add(noAction);
		}
		else{
			Object node = selectedNodes[0];
			IAction[] actions = ArchlightUtils.createTestMenuActions(xarch, xArchRef, tests, resources, node);
			if(actions.length == 0){
				Action noAction = new Action("[No Actions]"){
					public void run(){}
				};
				noAction.setEnabled(false);
				menuMgr.add(noAction);
			}
			for(int i = 0; i < actions.length; i++){
				menuMgr.add(actions[i]);
			}
		}
		menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	public void focusEditor(String editorName, ObjRef[] refs){
	}
}
