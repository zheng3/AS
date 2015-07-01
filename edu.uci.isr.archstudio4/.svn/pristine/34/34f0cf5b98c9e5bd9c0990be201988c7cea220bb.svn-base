package edu.uci.isr.archstudio4.comp.archipelago.types;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoUtils;
import edu.uci.isr.archstudio4.comp.archipelago.util.AbstractXadlSelectorLogic;
import edu.uci.isr.archstudio4.util.ArchstudioResources;
import edu.uci.isr.archstudio4.util.XadlTreeUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.things.glass.EndpointGlassThing;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;

public class StructureAssignInterfaceTypeLogic extends
		AbstractXadlSelectorLogic {

	public StructureAssignInterfaceTypeLogic(ArchipelagoServices services,
			ObjRef xArchRef) {
		super(services, xArchRef);
	}

	public boolean matches(IBNAView view, IThing t) {
		if(t instanceof EndpointGlassThing){
			IThing pt = view.getWorld().getBNAModel().getParentThing(t);
			if(pt != null){
				return StructureMapper.isInterfaceAssemblyRootThing(pt);
			}
		}
		return false;
	}

	public String getXArchID(IBNAView view, IThing t) {
		if(t instanceof EndpointGlassThing){
			IThing parentThing = view.getWorld().getBNAModel().getParentThing(t);
			return parentThing.getProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME);
		}
		return null;
	}

	public String getMenuItemString() {
		return "Assign Interface Type...";
	}

	public ImageDescriptor getMenuItemIcon(ObjRef eltRef) {
		return AS.resources.getImageDescriptor(ArchstudioResources.ICON_INTERFACE_TYPE);
	}

	public Object getInitialValue(ObjRef eltRef) {
		if(eltRef != null){
			ObjRef typeRef = XadlUtils.resolveXLink(AS.xarch, eltRef, "type");
			return typeRef;
		}
		return null;
	}

	public ObjRef getRootRef(ObjRef eltRef) {
		if(eltRef != null){
			return AS.xarch.getXArch(eltRef);
		}
		return null;
	}

	public int getFlags(ObjRef eltRef) {
		int flags = 0;
		if(AS.xarch.isInstanceOf(eltRef, "types#Interface")){
			return flags | XadlTreeUtils.INTERFACE_TYPE;
		}
		return flags | XadlTreeUtils.ANY_TYPE;
	}

	public void setValue(ObjRef eltRef, Object newValue) {
		if(newValue instanceof ObjRef){
			ObjRef newRef = (ObjRef)newValue;
			if(AS.xarch.isInstanceOf(newRef, "types#InterfaceType")){
				String typeID = XadlUtils.getID(AS.xarch, newRef);
				if(typeID != null){
					XadlUtils.setXLink(AS.xarch, eltRef, "type", typeID);
				}
			}
		}
	}	

}
