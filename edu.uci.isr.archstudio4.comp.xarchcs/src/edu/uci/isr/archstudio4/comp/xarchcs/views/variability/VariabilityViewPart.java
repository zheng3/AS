package edu.uci.isr.archstudio4.comp.xarchcs.views.variability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeColumn;

import edu.uci.isr.archstudio4.comp.xarchcs.XArchCSActivator;
import edu.uci.isr.archstudio4.comp.xarchcs.actions.AddAndRelationshipAction;
import edu.uci.isr.archstudio4.comp.xarchcs.actions.AddOrRelationshipAction;
import edu.uci.isr.archstudio4.comp.xarchcs.actions.AddVariantRelationshipAction;
import edu.uci.isr.archstudio4.comp.xarchcs.views.changesets.ChangeSetViewPart;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetEvent;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetListener;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetEvent.ChangeSetEventType;
import edu.uci.isr.widgets.swt.SWTWidgetUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatEvent;
import edu.uci.isr.xarchflat.XArchFlatListener;
import edu.uci.isr.xarchflat.XArchPath;
import edu.uci.isr.xarchflat.utils.IXArchRelativePathTrackerListener;
import edu.uci.isr.xarchflat.utils.XArchRelativePathTracker;

public class VariabilityViewPart
    extends ChangeSetViewPart
    implements XArchFlatListener, XArchChangeSetListener, SelectionListener, IXArchRelativePathTrackerListener{

	XArchRelativePathTracker relationshipTracker = null;

	public VariabilityViewPart(){
	}

	@Override
	protected void createMainMyxPartControl(Composite parent){
		super.createMainMyxPartControl(parent);

		getViewSite().getActionBars().getToolBarManager().add(new AddOrRelationshipAction(xarch));
		getViewSite().getActionBars().getToolBarManager().add(new AddAndRelationshipAction(xarch));
		getViewSite().getActionBars().getToolBarManager().add(new AddVariantRelationshipAction(xarch));
		getViewSite().getActionBars().updateActionBars();

		if(relationshipTracker == null){
			relationshipTracker = new XArchRelativePathTracker(xarch);
			relationshipTracker.addTrackerListener(this);
		}
	}

	@Override
	protected void updateStatus(){
		super.updateStatus();
		if(relationshipTracker != null && changeSetViewer != null){
			SWTWidgetUtils.async(changeSetViewer, new Runnable(){

				public void run(){
					changeSetViewer.getControl().setRedraw(false);
				}
			});
			relationshipTracker.setTrackInfo((ObjRef)changeSetViewer.getInput(), "archChangeSets/relationship");
			relationshipTracker.startScanning();
			SWTWidgetUtils.async(changeSetViewer, new Runnable(){

				public void run(){
					changeSetViewer.getControl().setRedraw(true);
				}
			});
		}
	}

	public void handleXArchFlatEvent(XArchFlatEvent evt){
		if(relationshipTracker != null){
			relationshipTracker.handleXArchFlatEvent(evt);
		}
		super.handleXArchFlatEvent(evt);
	}

	List<ObjRef> sortedRelationships = new ArrayList<ObjRef>();

	Map<ObjRef, TreeViewerColumn> tableViewerColumns = Collections.synchronizedMap(new HashMap<ObjRef, TreeViewerColumn>());
	Map<ObjRef, RelationshipColumnLabelProvider> relationshipColumnLabelProviders = Collections.synchronizedMap(new HashMap<ObjRef, RelationshipColumnLabelProvider>());

	boolean updateRelationships = false;

	@Override
	public void handleXArchChangeSetEvent(final XArchChangeSetEvent evt){
		super.handleXArchChangeSetEvent(evt);
		if(evt.getEventType() == ChangeSetEventType.UPDATED_APPLIED_CHANGE_SETS){
			updateRelationships = true;
			SWTWidgetUtils.async(changeSetViewer, new Runnable(){

				public void run(){
					if(updateRelationships){
						updateRelationships = false;
						Set<ObjRef> appliedChangeSetRefs = new HashSet<ObjRef>(Arrays.asList(evt.getAppliedChangeSets()));
						for(RelationshipColumnLabelProvider rclp: relationshipColumnLabelProviders.values().toArray(new RelationshipColumnLabelProvider[0])){
							rclp.updateSatisfiedStatus(appliedChangeSetRefs);
						}
					}
				}
			});
		}
	}

	protected ObjRef[] sort(ObjRef[] elements, List<ObjRef> elementList){
		Set<ObjRef> elementSet = new HashSet<ObjRef>(Arrays.asList(elements));
		for(Iterator<ObjRef> i = elementList.iterator(); i.hasNext();){
			Object element = i.next();
			if(elementSet.remove(element)){
				continue;
			}
			i.remove();
		}
		if(elementSet.size() > 1){
			List<ObjRef> remainingElementList = new ArrayList<ObjRef>(Arrays.asList(elements));
			remainingElementList.retainAll(elementSet);
			elementList.addAll(0, remainingElementList);
		}
		else{
			elementList.addAll(0, elementSet);
		}
		return elementList.toArray(new ObjRef[elementList.size()]);
	}

	public void widgetDefaultSelected(SelectionEvent e){
		//Null Implementation.

	}

	public void widgetSelected(SelectionEvent e){
		Object source = e.getSource();
		if(source instanceof TreeColumn){
			TreeColumn treeColumn = (TreeColumn)source;
			Object data = treeColumn.getData();
			if(data instanceof ObjRef){
				ObjRef relationshipRef = (ObjRef)data;
				StructuredSelection selection = new StructuredSelection(relationshipRef);
				this.fireSelectionChangedEvent(selection);
			}
		}
	}

	public void processAdd(final ObjRef relationshipRef, ObjRef[] relativeAncestorRefs){
		SWTWidgetUtils.async(changeSetViewer, new Runnable(){

			public void run(){
				TreeViewerColumn tvColumn = tableViewerColumns.get(relationshipRef);
				if(tvColumn == null){
					TreeColumn column = new TreeColumn(changeSetViewer.getTree(), SWT.CENTER);
					tvColumn = new TreeViewerColumn(changeSetViewer, column);
					RelationshipColumnLabelProvider rclp = new RelationshipColumnLabelProvider(changeSetViewer, xarch, relationshipRef);

					tableViewerColumns.put(relationshipRef, tvColumn);
					relationshipColumnLabelProviders.put(relationshipRef, rclp);

					column.setResizable(false);
					column.setAlignment(SWT.CENTER);
					column.setMoveable(true);
					column.setData(relationshipRef);
					column.setWidth(20);
					//column.addSelectionListener(variabilityViewPart);

					tvColumn.setEditingSupport(new RelationshipEditingSupport(changeSetViewer, xarch, relationshipRef));
					tvColumn.setLabelProvider(rclp);

					if(xarch.isInstanceOf(relationshipRef, "changesets#AndRelationship")){
						tvColumn.getColumn().setImage(XArchCSActivator.getDefault().getImageRegistry().get("res/icons/and_relationship_reference.gif"));
					}
					else if(xarch.isInstanceOf(relationshipRef, "changesets#OrRelationship")){
						tvColumn.getColumn().setImage(XArchCSActivator.getDefault().getImageRegistry().get("res/icons/or_relationship_reference.gif"));
					}
					else if(xarch.isInstanceOf(relationshipRef, "changesets#VariantRelationship")){
						tvColumn.getColumn().setImage(XArchCSActivator.getDefault().getImageRegistry().get("res/icons/variant_relationship_reference.gif"));
					}
					else{
						tvColumn.getColumn().setImage(XArchCSActivator.getDefault().getImageRegistry().get("res/icons/relationships_view.gif"));
					}

					rclp.updateRelationship();
				}
			}
		});
	}

	public void processUpdate(final ObjRef relationshipRef, ObjRef[] relativeAncestorRefs, XArchFlatEvent evt, final XArchPath relativeSourceTargetPath){
		SWTWidgetUtils.async(changeSetViewer, new Runnable(){

			public void run(){
				if(relativeSourceTargetPath.getLength() == 0){
					TreeViewerColumn tvColumn = tableViewerColumns.get(relationshipRef);
					if(xarch.isInstanceOf(relationshipRef, "changesets#AndRelationship")){
						tvColumn.getColumn().setImage(XArchCSActivator.getDefault().getImageRegistry().get("res/icons/and_relationship_reference.gif"));
					}
					else if(xarch.isInstanceOf(relationshipRef, "changesets#OrRelationship")){
						tvColumn.getColumn().setImage(XArchCSActivator.getDefault().getImageRegistry().get("res/icons/or_relationship_reference.gif"));
					}
					else if(xarch.isInstanceOf(relationshipRef, "changesets#VariantRelationship")){
						tvColumn.getColumn().setImage(XArchCSActivator.getDefault().getImageRegistry().get("res/icons/variant_relationship_reference.gif"));
					}
					else{
						tvColumn.getColumn().setImage(XArchCSActivator.getDefault().getImageRegistry().get("res/icons/relationships_view.gif"));
					}
				}

				RelationshipColumnLabelProvider rclp = relationshipColumnLabelProviders.get(relationshipRef);
				rclp.updateRelationship();
			}
		});
	}

	public void processRemove(final ObjRef relationshipRef, ObjRef[] relativeAncestorRefs){
		SWTWidgetUtils.async(changeSetViewer, new Runnable(){

			public void run(){
				TreeViewerColumn tvColumn = tableViewerColumns.remove(relationshipRef);
				if(tvColumn != null){
					tvColumn.getColumn().dispose();
				}
				relationshipColumnLabelProviders.remove(relationshipRef);
			}
		});
	}
}
