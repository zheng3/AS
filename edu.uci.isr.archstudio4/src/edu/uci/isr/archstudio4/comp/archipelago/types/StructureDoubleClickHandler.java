package edu.uci.isr.archstudio4.comp.archipelago.types;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoUtils;
import edu.uci.isr.archstudio4.comp.archipelago.IArchipelagoTreeDoubleClickHandler;
import edu.uci.isr.bna4.AbstractThing;
import edu.uci.isr.bna4.BNAComposite;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.things.shapes.BoxThing;
import edu.uci.isr.bna4.things.shapes.SplineThing;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchPath;

public class StructureDoubleClickHandler implements IArchipelagoTreeDoubleClickHandler{

	protected ArchipelagoServices AS = null;
	
	public StructureDoubleClickHandler(ArchipelagoServices archipelagoServices){
		this.AS = archipelagoServices;
	}
	
	protected boolean isTargetNode(Object ref){
		if(ref instanceof ObjRef){
			XArchPath refPath = AS.xarch.getXArchPath((ObjRef)ref);
			if(refPath.toTagsOnlyString().equals("xArch/archStructure")){
				return true;
			}
		}
		return false;
	}
	
	public void treeNodeDoubleClicked(Object treeNode){
		if(isTargetNode(treeNode)){		
			ObjRef archStructureRef = (ObjRef)treeNode;
			StructureEditorSupport.setupEditor(AS, archStructureRef);
			BNAComposite composit = ArchipelagoUtils.getBNAComposite(AS.editor);
			if(composit == null){
				return;
			}
			if(composit.getView() == null){
				return;
			}
			
			if(composit.getView().getWorld() == null){
				return;
			}
			if(composit.getView().getWorld().getBNAModel() == null){
				return;
			}
			
			IBNAModel model = composit.getView().getWorld().getBNAModel();

			model.setSelectedFeature(null);
			IThing[] things = model.getAllThings();
			for (int i = 0; i < things.length; i++) {
				
				if(things[i] instanceof BoxThing || things[i] instanceof SplineThing){

					AbstractThing thing = (AbstractThing)things[i];

					thing.featureSelected = false;
					

				}
			}
			
		}
	}
	
}
