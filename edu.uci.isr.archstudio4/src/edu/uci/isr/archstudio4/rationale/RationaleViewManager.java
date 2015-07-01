package edu.uci.isr.archstudio4.rationale;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;

import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetInterface;
import edu.uci.isr.xarchflat.IXArchTypeMetadata;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchMetadataUtils;

public class RationaleViewManager {

	List<ObjRef> currentSelectedRefs = new ArrayList<ObjRef>();
	List<ObjRef> selectedRationaleRefs = new ArrayList<ObjRef>();
	List<ObjRef> rationaleItems;
	XArchChangeSetInterface xArchCS;
	ObjRef xArchRef;
	TableViewer rationaleTableViewer;
	TableViewer associatedItemsViewer;

	List<ObjRef> selectedAssociatedItemRefs = new ArrayList<ObjRef>();



	public RationaleViewManager(XArchChangeSetInterface xArchCS,TableViewer rationaleTableViewer,TableViewer associatedItemsViewer) {
		this.rationaleTableViewer = rationaleTableViewer;
		this.associatedItemsViewer = associatedItemsViewer;
		this.xArchCS = xArchCS;
	}

	public List<ObjRef> getCurrentSelectedRefs() {
		return currentSelectedRefs;
	}

	public void refreshTableViewer() {
		rationaleTableViewer.refresh();
	}

	public void setXArchRef(ObjRef xArchRef) {
		this.xArchRef = xArchRef;
	}

	public void loadRationales(List<ObjRef> selectedRefs) {
		currentSelectedRefs.clear();
		currentSelectedRefs.addAll(selectedRefs);
		rationaleTableViewer.refresh();
	}


	public void clearSelectedRationaleRefList() {
		selectedRationaleRefs.clear();
	}

	public void clearSelectedAssociatedItemRefList() {
		selectedAssociatedItemRefs.clear();
	}

	public List<ObjRef> getSelectedAssociatedItemRefList() {
		return this.selectedAssociatedItemRefs;
	}

	public void addSelectedAssociatedItemRefList(ObjRef ref) {
		if(!selectedAssociatedItemRefs.contains(ref)) {
			selectedAssociatedItemRefs.add(ref);
		}
	}

	public void addSelectedRationale(ObjRef rationaleRef) {
		if(!selectedRationaleRefs.contains(rationaleRef)) {
			selectedRationaleRefs.add(rationaleRef);
		}
	}

	public List<ObjRef> getSelectedRationales() {
		return this.selectedRationaleRefs;
	}

	public Object[] getRationales() {
		if(xArchRef == null) {
			return new Object[0];
		}
		else {
			ObjRef rationaleContextRef = xArchCS.createContext(xArchRef, "rationale");
			ObjRef rationaleElementRef = xArchCS.getElement(rationaleContextRef, "ArchRationale", xArchRef);
			List<ObjRef> rationaleRefsForCurrentSelection = new ArrayList<ObjRef>();
			if(rationaleElementRef != null) {
				ObjRef[] rationaleRefs = xArchCS.getAll(rationaleElementRef,"Rationale");
				for(ObjRef rationaleRef: rationaleRefs) {
					ObjRef[] xmlLinkRefs = xArchCS.getAll(rationaleRef, "item");
					for(ObjRef xmlLinkRef : xmlLinkRefs) {
						String href = (String)xArchCS.get(xmlLinkRef,"Href");
						if(href != null && !"".equals(href.trim())) {
							String xArchID = href.replaceFirst("#", "");
							ObjRef ref = xArchCS.getByID(xArchRef, xArchID);
							if(currentSelectedRefs.contains(ref)) {
								rationaleRefsForCurrentSelection.add(rationaleRef);
								break;
							}
						}
					}
				}
			}
			ObjRef changeSetsContextRef = xArchCS.createContext(xArchRef, "changesets");
			ObjRef archChangeSetsElementRef = xArchCS.getElement(changeSetsContextRef, "ArchChangeSets", xArchRef);
			if(archChangeSetsElementRef != null) {
				if(xArchCS.isInstanceOf(archChangeSetsElementRef, "rationale#ArchChangeSetsRationale")) {
					ObjRef archRationaleRef = (ObjRef)xArchCS.get(archChangeSetsElementRef,"ArchRationale");
					ObjRef[] rationaleRefs = xArchCS.getAll(archRationaleRef,"Rationale");
					for(ObjRef rationaleRef: rationaleRefs) {
						ObjRef[] xmlLinkRefs = xArchCS.getAll(rationaleRef, "item");
						for(ObjRef xmlLinkRef : xmlLinkRefs) {
							String href = (String)xArchCS.get(xmlLinkRef,"Href");
							if(href != null && !"".equals(href.trim())) {
								String xArchID = href.replaceFirst("#", "");
								ObjRef ref = xArchCS.getByID(xArchRef, xArchID);
								if(currentSelectedRefs.contains(ref)) {
									rationaleRefsForCurrentSelection.add(rationaleRef);
									break;
								}
							}
						}
					}
				}				
			}

			for(ObjRef selectedRef : currentSelectedRefs) {
				if(xArchCS.isInstanceOf(selectedRef,"changesets#Relationship")) {
					ObjRef[] relationshipRationales = xArchCS.getAll(selectedRef,"rationale");
					for(ObjRef relationshipRationale : relationshipRationales) {
						rationaleRefsForCurrentSelection.add(relationshipRationale);
					}
				}
			}

			if(rationaleRefsForCurrentSelection.size() > 0) {
				return rationaleRefsForCurrentSelection.toArray(new ObjRef[rationaleRefsForCurrentSelection.size()]);
			}
			else {
				return new Object[0];
			}
		}
	}

	public void reloadRationales() {
		this.loadRationales(this.currentSelectedRefs);
	}

	public boolean areObjectsSelected() {
		if(currentSelectedRefs.size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}

	public Object[] getAssociatedItemsForCurrentListOfRationales() {

		List<ObjRef> associatedItemRefs = new ArrayList<ObjRef>();
		associatedItemRefs.addAll(this.currentSelectedRefs);

		Object[] rationaleObjects = this.getRationales();

		for(Object rationaleObject : rationaleObjects) {
			if(rationaleObject instanceof ObjRef) {
				ObjRef rationaleRef = (ObjRef)rationaleObject;
				if(xArchCS.isInstanceOf(rationaleRef, "changesets#RelationshipRationale")) {

				}
				else {
					ObjRef[] itemRefArray = xArchCS.getAll(rationaleRef,"item");
					for(ObjRef itemRef : itemRefArray) {
						String href = (String)xArchCS.get(itemRef,"Href");
						if(href != null && !"".equals(href.trim())) {
							String xArchID = href.replaceFirst("#", "");
							ObjRef associatedItemRef = xArchCS.getByID(xArchID);
							if(!associatedItemRefs.contains(associatedItemRef)) {
								associatedItemRefs.add(associatedItemRef);
							}
						}
					}
				}
			}
		}

		if(this.currentSelectedRefs.size() > 0) {
			return associatedItemRefs.toArray(new ObjRef[associatedItemRefs.size()]);
		}
		else {
			return new Object[0];
		}
	}
}
