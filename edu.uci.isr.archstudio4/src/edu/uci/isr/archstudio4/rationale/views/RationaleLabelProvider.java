package edu.uci.isr.archstudio4.rationale.views;

import java.util.List;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import edu.uci.isr.archstudio4.rationale.RationaleViewManager;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public class RationaleLabelProvider implements ITableLabelProvider,IFontProvider,ITableColorProvider{

	XArchFlatInterface xArch;
	RationaleViewManager rationaleViewManager;
	TableViewer viewer;
	Color generatedColor;

	Font boldFont;
	Font normalFont;
	Font boldItalicFont;
	Font normalItalicFont;	

	public RationaleLabelProvider(XArchFlatInterface xArch,TableViewer viewer,RationaleViewManager rationaleViewManager) {
		this.xArch = xArch;
		this.rationaleViewManager = rationaleViewManager;
		this.viewer = viewer;
		boldFont = new Font(viewer.getControl().getDisplay(),"Arial",8,SWT.BOLD);
		normalFont = new Font(viewer.getControl().getDisplay(),"Arial",8,SWT.NORMAL);
		generatedColor = new Color(viewer.getControl().getDisplay(), 255, 212, 212);
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {

		switch(columnIndex) {
		case 0:
			return null;
		case 1:
			if(element instanceof ObjRef) {
				ObjRef objRef = (ObjRef)element;
				if(xArch.isInstanceOf(objRef, "changesets#RelationshipRationale")) {
					ObjRef[] sourceRefs = xArch.getAll(objRef, "Source");
					String descriptionValue = "";
					for(ObjRef sourceRef : sourceRefs) {
						String path = (String)xArch.get(sourceRef,"xArchPath");
						descriptionValue += path;
						ObjRef sourceChangeSetLinkRef = (ObjRef)xArch.get(sourceRef,"ChangeSet");
						if(sourceChangeSetLinkRef != null) {
							String href = (String)xArch.get(sourceChangeSetLinkRef,"Href");
							String xArchID = href.replaceFirst("#", "");
							ObjRef changeSetRef = xArch.getByID(xArchID);
							ObjRef descriptionRef = (ObjRef)xArch.get(changeSetRef,"Description");
							String value = (String)xArch.get(descriptionRef,"value");
							descriptionValue += " in " + value + ", "; 
						}
					}
					if(descriptionValue.length() > 0) {
						descriptionValue = descriptionValue.substring(0, descriptionValue.length()-2);
					}

					ObjRef[] requiresRefs = xArch.getAll(objRef,"Requires");
					if(requiresRefs != null && requiresRefs.length > 0) {
						descriptionValue += " references ";
						for(ObjRef requiresRef : requiresRefs) {
							String path = (String)xArch.get(requiresRef,"xArchPath");
							descriptionValue += path;
							ObjRef requiresChangeSetLinkRef = (ObjRef)xArch.get(requiresRef,"ChangeSet");
							if(requiresChangeSetLinkRef != null) {
								String href = (String)xArch.get(requiresChangeSetLinkRef,"Href");
								String xArchID = href.replaceFirst("#", "");
								ObjRef changeSetRef = xArch.getByID(xArchID);
								ObjRef descriptionRef = (ObjRef)xArch.get(changeSetRef,"Description");
								String value = (String)xArch.get(descriptionRef,"value");
								descriptionValue += " in " + value + ", "; 
							}

						}
						descriptionValue = descriptionValue.substring(0, descriptionValue.length()-2);
					}

					return descriptionValue;
				}
				else {
					ObjRef descriptionRef = (ObjRef)xArch.get(objRef,"Description");
					String descriptionValue = (String)xArch.get(descriptionRef,"value");
					return descriptionValue;
				}
			}
			break;
		}
		return null;
	}

	public void addListener(ILabelProviderListener listener) {


	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
	}

	public Font getFont(Object element) {
		Font font = null;
		if(element instanceof ObjRef) {
			List<ObjRef> selectedAssociatedItemRefs = rationaleViewManager.getSelectedAssociatedItemRefList();
			ObjRef rationaleRef = (ObjRef)element;
			if(xArch.isInstanceOf(rationaleRef, "changesets#RelationshipRationale")) {
				ObjRef []ancestors = xArch.getAllAncestors(rationaleRef);
				if(ancestors != null && ancestors.length > 1) {
					ObjRef ancestor = ancestors[1];
					for(ObjRef selectedAssociatedItemRef : selectedAssociatedItemRefs) {
						if(selectedAssociatedItemRef.equals(ancestor)) {
							font = boldFont;
							break;
						}
					}				
				}
			}
			else {


				ObjRef[] itemRefArray = xArch.getAll(rationaleRef,"item");
				for(ObjRef itemRef : itemRefArray) {
					String href = (String)xArch.get(itemRef,"Href");
					if(href != null && !"".equals(href.trim())) {
						String xArchID = href.replaceFirst("#", "");
						ObjRef associatedItemRef = xArch.getByID(xArchID);
						if(selectedAssociatedItemRefs.contains(associatedItemRef)) {
							font = boldFont; 
						}
					}
				}
			}
		}
		return font;
	}

	public Color getBackground(Object element, int columnIndex) {		
		if(element instanceof ObjRef) {
			ObjRef elementRef = (ObjRef)element;
			if(xArch.isInstanceOf(elementRef, "changesets#RelationshipRationale")) {
				ObjRef[] ancestors = xArch.getAllAncestors(elementRef);
				if(ancestors != null && ancestors.length > 1) {
					ObjRef ancestor = ancestors[1];
					String generated = (String)xArch.get(ancestor,"generated");
					if("true".equals(generated)) {
						return generatedColor;
					}
				}
			}
		}
		return null;
	}

	public Color getForeground(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}