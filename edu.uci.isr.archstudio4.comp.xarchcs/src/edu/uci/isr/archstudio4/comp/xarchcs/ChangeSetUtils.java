package edu.uci.isr.archstudio4.comp.xarchcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import edu.uci.isr.archstudio4.comp.xarchcs.changesetsync.IChangeSetSync.ChangeStatus;
import edu.uci.isr.archstudio4.comp.xarchcs.explicitadt.IExplicitADT;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetInterface;
import edu.uci.isr.widgets.swt.OverlayImageIcon;
import edu.uci.isr.xarchflat.ObjRef;

public class ChangeSetUtils {
	
	static Image removedImage = XArchCSActivator.getDefault().getImageRegistry().get("res/icons/removed_item.gif");
	static Image addedImage =  XArchCSActivator.getDefault().getImageRegistry().get("res/icons/added_item.gif");
	static Image modifiedImage =  XArchCSActivator.getDefault().getImageRegistry().get("res/icons/modified_item.gif");
	
	public static Image getOverlayImageIcon(XArchChangeSetInterface xArchCS,IExplicitADT explicitADT,Image image,Object element) {
		
		Image[] overlayImage = new Image[1];
		overlayImage[0] = null;
		if(element instanceof ObjRef) {
			ObjRef objRef = (ObjRef)element;
			ChangeStatus changeStatus = xArchCS.getChangeStatus(objRef, explicitADT.getExplicitChangeSetRefs(xArchCS.getXArch(objRef)));
			
			switch(changeStatus) {
			case ADDED:
				overlayImage[0] = addedImage;
				break;
			case REMOVED:
				overlayImage[0] = removedImage;
				break;
			case MODIFIED:
				overlayImage[0] = modifiedImage;
				break;
			}
			if(overlayImage[0] != null) {
				OverlayImageIcon overlayImageIcon = new OverlayImageIcon(image,overlayImage,new int[]{0});
				return overlayImageIcon.getImage();
			}
		}
		return image;
	}
	
	public static Object[] filterOutDetatched(XArchChangeSetInterface xArchCS, IExplicitADT explicitADT, Object[] objects) {
		if (objects.length == 0)
			return objects;
		
		ObjRef[] explicitChangeSets = null;
		List<Object> nondetachcedChangeSets = new ArrayList<Object>();
		
		for (Object o : objects) {
			if (o instanceof ObjRef) {
				if (explicitChangeSets == null){
					explicitChangeSets = explicitADT.getExplicitChangeSetRefs(xArchCS.getXArch((ObjRef)o));
				}
				if (ChangeStatus.DETACHED == xArchCS.getChangeStatus((ObjRef)o, explicitChangeSets)) {
					continue;
				}
			}
			nondetachcedChangeSets.add(o);
		}
		
		return nondetachcedChangeSets.toArray(new Object[nondetachcedChangeSets.size()]);
	}
}
