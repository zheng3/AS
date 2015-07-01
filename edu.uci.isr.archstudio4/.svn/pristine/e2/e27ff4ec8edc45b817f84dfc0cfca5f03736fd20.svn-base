package edu.uci.isr.archstudio4.comp.archipelago.interactions.things;

import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import edu.uci.isr.archstudio4.comp.resources.IResources;
import edu.uci.isr.archstudio4.util.ArchstudioResources;
import edu.uci.isr.archstudio4.util.XadlTreeUtils;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public class OperationLabelProvider extends LabelProvider implements
		ILabelProvider {

	protected XArchFlatInterface xarch = null;
	protected IResources resources = null;

	public OperationLabelProvider(XArchFlatInterface xarch, IResources resources){
		this.xarch = xarch;
		this.resources = resources;
		ArchstudioResources.init(resources);
	}

	public String getText(Object element) {
		if(element instanceof ObjRef){
			ObjRef ref = (ObjRef)element;
			int type = XadlTreeUtils.getType(xarch, ref);
			if(type == XadlTreeUtils.UNKNOWN){
				return "[Unknown Element]";
			}
			else if(type == XadlTreeUtils.DOCUMENT){
				return xarch.getXArchURI(ref);
			}
			else{
				String description = XadlUtils.getDescription(xarch, ref);
				if(description == null){
					String id = XadlUtils.getID(xarch, ref);
					if(id != null){
						description = "[id=" + id + "]";
					}
					description = "[No Identifier]";
				}
				return description;
			}
		}
		else if (element instanceof OperationLabel) {
			OperationLabel methodkLabel = (OperationLabel) element;
			return methodkLabel.toLabelString();
		}
		return super.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		if(element instanceof ObjRef){
			ObjRef ref = (ObjRef)element;
			int type = XadlTreeUtils.getType(xarch, ref);
			Image image = XadlTreeUtils.getIconForType(resources, type);
			if(image != null){
				return image;
			}
		}
		else if (element instanceof OperationLabel) {
			return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_PUBLIC);
			//return XadlTreeUtils.getIconForType(resources, XadlTreeUtils.COMPONENT);
		}
		return super.getImage(element);
	}

}
