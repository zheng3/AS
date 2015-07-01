package edu.uci.isr.archstudio4.rationale.views;

import java.util.List;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import edu.uci.isr.archstudio4.rationale.RationaleViewManager;
import edu.uci.isr.xarchflat.IXArchTypeMetadata;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;
import edu.uci.isr.xarchflat.XArchMetadataUtils;

public class AssociatedItemsLabelProvider implements ITableLabelProvider,IFontProvider{

	XArchFlatInterface xArch;
	RationaleViewManager rationaleViewManager;
	TableViewer viewer;

	Font boldFont;
	Font normalFont;
	Font boldItalicFont;
	Font normalItalicFont;
	
	public AssociatedItemsLabelProvider(XArchFlatInterface xArch,RationaleViewManager rationaleViewManager,TableViewer viewer) {
		this.xArch = xArch;
		this.rationaleViewManager = rationaleViewManager;
		this.viewer = viewer;
		boldFont = new Font(viewer.getControl().getDisplay(),"Arial",8,SWT.BOLD);
		normalFont = new Font(viewer.getControl().getDisplay(),"Arial",8,SWT.NORMAL);
		boldItalicFont = new Font(viewer.getControl().getDisplay(),"Arial",8,SWT.BOLD|SWT.ITALIC);
		normalItalicFont = new Font(viewer.getControl().getDisplay(),"Arial",8,SWT.NORMAL|SWT.ITALIC);
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
				String descriptionValue = "";
				try {
					ObjRef descriptionRef = (ObjRef)xArch.get(objRef,"Description");
					descriptionValue = (String)xArch.get(descriptionRef,"value");
				}
				catch(Exception e) {}
				IXArchTypeMetadata typeMetadata = xArch.getTypeMetadata(objRef);
				String typeName = XArchMetadataUtils.getTypeName(typeMetadata.getType());
				return typeName + " " + descriptionValue;
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
			ObjRef elementRef = (ObjRef)element;
			List<ObjRef> rationaleRefsList = rationaleViewManager.getSelectedRationales();
			List<ObjRef> selectedRefs = rationaleViewManager.getCurrentSelectedRefs();
			boolean found = false;
			if(rationaleRefsList.size() > 0) {
				for(ObjRef rationaleRef : rationaleRefsList) {
					if(xArch.isInstanceOf(rationaleRef, "changesets#RelationshipRationale")) {
						ObjRef[] ancestors = xArch.getAllAncestors(rationaleRef);
						if(ancestors != null && ancestors.length > 1) {
							ObjRef ancestor = ancestors[1];
							if(elementRef.equals(ancestor)) {
								found = true;
								font = boldFont;
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
								if(associatedItemRef.equals(elementRef)) {
									found = true;
									if(selectedRefs.contains(associatedItemRef)) {
										font = boldFont; 
									}
									else {
										font = boldItalicFont;								
									}
									break;
								}
							}
						}
					}
					if(found) {
						break;
					}
				}
				if(!found) {
					font = normalItalicFont;
				}
			}
			else {
				if(!selectedRefs.contains(elementRef)) {
					font = normalItalicFont;
				}
			}
		}
		return font;
	}
}
