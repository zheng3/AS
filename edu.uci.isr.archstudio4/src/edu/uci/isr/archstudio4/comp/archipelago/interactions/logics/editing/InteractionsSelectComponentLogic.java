package edu.uci.isr.archstudio4.comp.archipelago.interactions.logics.editing;

import org.eclipse.jface.resource.ImageDescriptor;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoUtils;
import edu.uci.isr.archstudio4.comp.archipelago.util.AbstractXadlSelectorLogic;
import edu.uci.isr.archstudio4.util.ArchstudioResources;
import edu.uci.isr.archstudio4.util.XadlTreeUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.things.glass.BoxGlassThing;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;

public class InteractionsSelectComponentLogic extends AbstractXadlSelectorLogic {

	public InteractionsSelectComponentLogic(ArchipelagoServices services, ObjRef xArchRef){
		super(services, xArchRef);
	}

	public boolean matches(IBNAView view, IThing t) {
		if(t != null && (t instanceof BoxGlassThing)){
			return true;
		}
		return false;
	}

	public String getXArchID(IBNAView view, IThing t) {
		if(t instanceof BoxGlassThing){
			IThing parentThing = view.getWorld().getBNAModel().getParentThing(t);
			return parentThing.getProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME);
		}
		return null;
	}

	public String getMenuItemString() {
		return "Select Existing Component";
	}

	public ImageDescriptor getMenuItemIcon(ObjRef eltRef) {
		if (AS.xarch.isInstanceOf(eltRef, "interactions#LifeLine")) {
			return AS.resources.getImageDescriptor(ArchstudioResources.ICON_COMPONENT);
		}
		return null;
	}

	public Object getInitialValue(ObjRef eltRef) {
		if(eltRef != null){
			ObjRef typeRef = XadlUtils.resolveXLink(AS.xarch, eltRef, "represents");
			return typeRef;
		}
		return null;
	}

	public ObjRef getRootRef(ObjRef eltRef) {
		if(eltRef != null){
			ObjRef typesContextRef = AS.xarch.createContext(xArchRef, "types");
			ObjRef archTypesRef = AS.xarch.getElement(typesContextRef, "archStructure", xArchRef);
			return archTypesRef;
			//return AS.xarch.getXArch(eltRef);
		}
		return null;
	}

	public int getFlags(ObjRef eltRef) {
		int flags = 0;
		if(AS.xarch.isInstanceOf(eltRef, "interactions#LifeLine")){
			return flags | XadlTreeUtils.COMPONENT;
		}
		return flags | XadlTreeUtils.ANY_TYPE;
	}

	public void setValue(ObjRef eltRef, Object newValue) {
		if(newValue instanceof ObjRef){
			ObjRef newRef = (ObjRef)newValue;
			if(AS.xarch.isInstanceOf(newRef, "types#Component")){
				String typeID = XadlUtils.getID(AS.xarch, newRef);
				if(typeID != null){
					XadlUtils.setXLink(AS.xarch, eltRef, "represents", typeID);
					XadlUtils.setDescription(AS.xarch, eltRef, XadlUtils.getDescription(AS.xarch, newRef).toLowerCase());
				}
			}
		}
	}

}
