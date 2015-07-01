package edu.uci.isr.archstudio4.comp.archipelago.features;

import java.util.ArrayList;

import org.eclipse.swt.graphics.RGB;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoOutlinePage;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoUtils;
import edu.uci.isr.archstudio4.comp.archipelago.IArchipelagoTreeDoubleClickHandler;
import edu.uci.isr.archstudio4.comp.archipelago.types.FeatureEditorSupport;
import edu.uci.isr.archstudio4.comp.archipelago.types.StructureEditorSupport;
import edu.uci.isr.bna4.AbstractThing;
import edu.uci.isr.bna4.BNAComposite;
import edu.uci.isr.bna4.BNAModelEvent;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.things.shapes.BoxThing;
import edu.uci.isr.bna4.things.shapes.SplineThing;
import edu.uci.isr.bna4.things.swt.SWTComboThing;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchPath;

public class FeatureDoubleClickHandler implements IArchipelagoTreeDoubleClickHandler{

	protected ArchipelagoServices AS = null;
	protected ObjRef xArchRef = null;
	public FeatureDoubleClickHandler(ArchipelagoServices archipelagoServices,ObjRef xArchRef){
		this.AS = archipelagoServices;
		this.xArchRef = xArchRef;
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
		
		//System.out.println("double click "+treeNode);
		ObjRef ref = null;
		if(treeNode instanceof ObjRef){
			ref = (ObjRef)treeNode;
			
			ObjRef typesContextRef = AS.xarch.createContext(xArchRef, "types");
			ObjRef archStructureRef = AS.xarch.getElement(typesContextRef, "archStructure", xArchRef);
			if(archStructureRef!=null){
				StructureEditorSupport.setupEditor(AS, archStructureRef);
			}else{
				return;	
			}
			
			if(AS.xarch.isInstanceOf(ref, "features#OptionalFeature")){
				doubleClickOptionalFeature(ref);
			}else if(AS.xarch.isInstanceOf(ref, "features#AlternativeFeature")){
				doubleClickAlternativeFeature(ref);
			}else if(AS.xarch.isInstanceOf(ref, "features#Varient")){
				doubleClickVarientFeature(ref);
			}
			
		}else{
			return;
		}
		
		if(!AS.xarch.isInstanceOf(ref, "features#Feature")){
			return;
		}
		
		//System.out.println(treeNode);
		//ObjRef typesContextRef = AS.xarch.createContext(xArchRef, "types");
		//ObjRef[] archs = AS.xarch.getAllElements(typesContextRef, "archStructure", xArchRef);
		
		
//		ObjRef archStructureRef = AS.xarch.createContext(xArchRef, "types");
	//	ObjRef[] archStructureRefs = AS.xarch.getAllElements(archStructureRef, "archStructure", xArchRef);
		

	}

	private ColorLinkNode getColorForID(ArrayList<ColorLinkNode> optionalIds,
			Object hintContext) {
		for (int i = 0; i < optionalIds.size(); i++) {
			if(optionalIds.get(i).id.equals(hintContext)){
				return optionalIds.get(i);
			}
		}
		return null;
	}

	private void doubleClickVarientFeature(ObjRef ref) {
		
		ArrayList<ColorLinkNode> optionalIds = new ArrayList<ColorLinkNode>();
		
		ObjRef featureElementsRef = (ObjRef)AS.xarch.get(ref, "featureElements");
		ObjRef featureOptions = (ObjRef)AS.xarch.get(ref, "featureOptions");
		ObjRef featureColor = (ObjRef)AS.xarch.get(featureOptions, "featureColor");
		ObjRef colorRef = (ObjRef)AS.xarch.get(featureColor, "value");
		String color = (String)AS.xarch.get(colorRef, "data");
		String colors[] = color.split(",");
		RGB rgb = new RGB(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
		
		if(featureElementsRef == null)
			return ;
		 
		 ObjRef[] linkRefs = AS.xarch.getAll(featureElementsRef, "archElement");
		
		 
		 for (int i = 0; i < linkRefs.length; i++) {
			 ObjRef link = linkRefs[i];
				String href = (String) AS.xarch.get(link, "href");
				String id = href.substring(1);
				if(AS.xarch.getByID(id)!=null){
				optionalIds.add(new ColorLinkNode(id, rgb));
				}else{
					AS.xarch.remove(featureElementsRef, "archElement", link);
				}
		}
		
		
	
		
		
		updateThings(optionalIds, ref);
		
	}

	private void doubleClickAlternativeFeature(ObjRef ref) {
		ArrayList<ColorLinkNode> optionalIds = new ArrayList<ColorLinkNode>();
		ObjRef varients = (ObjRef)AS.xarch.get(ref, "featureVarients");
		ObjRef[] varientList = AS.xarch.getAll(varients, "varient");
		for (ObjRef objRef : varientList) {
			
		
		ObjRef featureElementsRef = (ObjRef)AS.xarch.get(objRef, "featureElements");
		ObjRef featureOptions = (ObjRef)AS.xarch.get(objRef, "featureOptions");
		ObjRef featureColor = (ObjRef)AS.xarch.get(featureOptions, "featureColor");
		ObjRef colorRef = (ObjRef)AS.xarch.get(featureColor, "value");
		String color = (String)AS.xarch.get(colorRef, "data");
		String colors[] = color.split(",");
		RGB rgb = new RGB(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
		
		if(featureElementsRef == null)
			return ;
		 
		 ObjRef[] linkRefs = AS.xarch.getAll(featureElementsRef, "archElement");
		
		 
		 for (int i = 0; i < linkRefs.length; i++) {
			 ObjRef link = linkRefs[i];
				String href = (String) AS.xarch.get(link, "href");
				String id = href.substring(1);
				if(AS.xarch.getByID(id)!=null){
				optionalIds.add(new ColorLinkNode(id, rgb));
				}else{
					AS.xarch.remove(featureElementsRef, "archElement", link);
				}
		}
		 
		}
		

			
		
		ObjRef featureElementsRef = (ObjRef)AS.xarch.get(ref, "featureElements");
		
		
		RGB rgb = new RGB(255, 0, 0);
		
		if(featureElementsRef == null)
		{
			updateThings(optionalIds, ref);
			return;
		}else{
		 ObjRef[] linkRefs = AS.xarch.getAll(featureElementsRef, "archElement");
		
		 
		 for (int i = 0; i < linkRefs.length; i++) {
			 ObjRef link = linkRefs[i];
				String href = (String) AS.xarch.get(link, "href");
				String id = href.substring(1);
				if(AS.xarch.getByID(id)!=null){
				optionalIds.add(new ColorLinkNode(id, rgb));
				}else{
					AS.xarch.remove(featureElementsRef, "archElement", link);
				}
		}
		 
		}
		
		updateThings(optionalIds, ref);
	}

	private void doubleClickOptionalFeature(ObjRef ref) {
		
		ArrayList<ColorLinkNode> optionalIds = new ArrayList<ColorLinkNode>();
		
		ObjRef featureElementsRef = (ObjRef)AS.xarch.get(ref, "featureElements");
		ObjRef featureOptions = (ObjRef)AS.xarch.get(ref, "featureOptions");
		ObjRef featureColor = (ObjRef)AS.xarch.get(featureOptions, "featureColor");
		ObjRef colorRef = (ObjRef)AS.xarch.get(featureColor, "value");
		String color = (String)AS.xarch.get(colorRef, "data");
		String colors[] = color.split(",");
		RGB rgb = new RGB(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
		
		if(featureElementsRef == null)
			return ;
		 
		 ObjRef[] linkRefs = AS.xarch.getAll(featureElementsRef, "archElement");
		
		 
		 for (int i = 0; i < linkRefs.length; i++) {
			 ObjRef link = linkRefs[i];
				String href = (String) AS.xarch.get(link, "href");
				String id = href.substring(1);
				if(AS.xarch.getByID(id)!=null){
				optionalIds.add(new ColorLinkNode(id, rgb));
				}else{
					AS.xarch.remove(featureElementsRef, "archElement", link);
				}
				//System.out.println("varun---->"+href);
		}
		
		
	
		
		updateThings(optionalIds, ref);
		
		
	}
	
	public void updateThings(ArrayList<ColorLinkNode> optionalIds,ObjRef ref){
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
		
		
		IThing[] things = model.getAllThings();

			
			String description = XadlUtils.getDescription(AS.xarch, ref);
			if(description == null){
				
				description = "[No Description]";
			}
			//setting the selected feature
			String featureID = XadlUtils.getID(AS.xarch, ref);
			model.setSelectedFeature(featureID);
			
			//FIXME
			ArchipelagoUtils.showUserNotification(model, "Feature "+description+" selected", 10325 ,10100);
		
	
		
		for (int i = 0; i < things.length; i++) {
		
			Object ob2  = things[i].getProperty("edu.uci.isr.bna4.logics.hints.SynchronizeHintsLogic:hintInformation");
			
			if(ob2!=null){
				edu.uci.isr.bna4.logics.hints.SynchronizeHintsLogic.HintInformation hint = (edu.uci.isr.bna4.logics.hints.SynchronizeHintsLogic.HintInformation)ob2;
//				System.out.println(hint.hintContext);
//				if(hint.hintContext.toString().contains("link")){
//					System.out.println(things[i]);
//				}
				if(things[i] instanceof BoxThing || things[i] instanceof SplineThing){

					AbstractThing thing = (AbstractThing)things[i];
					
					ColorLinkNode featureRGB = getColorForID(optionalIds,hint.hintContext);
					if(featureRGB!=null){
						//things[i].setProperty("color",things[i].getProperty("color"));
						thing.featureSelected = true;
						thing.featureColor = featureRGB.color;
					}else{
						thing.featureSelected = false;
						
					}

				}
			}
			
			
		}
		model.fireStreamNotificationEvent(null);
	}
	
	class ColorLinkNode{
		String id;
		RGB color;
		
		public ColorLinkNode(String id,RGB color){
			this.id = id;
			this.color = color;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj == null){
				return false;
			}else if(obj instanceof String){
			if(this.id.equals(obj)){
				return true;
			}else{
				return false;
			}
			}else if(obj instanceof ColorLinkNode){
				if(this.id.equals(((ColorLinkNode) obj).id) && this.color.equals(((ColorLinkNode) obj).color)){
					return true;
				}else{
					return false;
				}
			}
			return false;
		}
	}
}
