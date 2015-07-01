package edu.uci.isr.archstudio4.comp.archipelago.features;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.FolderNode;
import edu.uci.isr.archstudio4.comp.archipelago.IArchipelagoTreeContextMenuFiller;
import edu.uci.isr.sysutils.UIDGenerator;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;

public class FeatureAddVarientContextMenuFiller implements IArchipelagoTreeContextMenuFiller{
	protected TreeViewer viewer = null;
	protected ArchipelagoServices AS =  null;
	protected ObjRef xArchRef = null;
	
	public FeatureAddVarientContextMenuFiller(TreeViewer viewer, ArchipelagoServices AS, ObjRef xArchRef){
		this.viewer = viewer;
		this.AS = AS;
		this.xArchRef = xArchRef;
	}
	
	
	public void fillContextMenu(IMenuManager m, Object[] selectedNodes){
		if((selectedNodes != null) && (selectedNodes.length == 1)){
			final Object selectedNode = selectedNodes[0];
			//System.out.println(selectedNode);
			if(selectedNode instanceof ObjRef){
				if(AS.xarch.isInstanceOf((ObjRef) selectedNode, "Features#AlternativeFeature")){
					IAction newVarientAction = new Action("Add Variant"){
						public void run(){
							addNewVarient((ObjRef) selectedNode);
						}
					};
					
					m.add(newVarientAction);
				}
				/*FolderNode fn = (FolderNode)selectedNode;
				String fnType = fn.getType();
				if(fnType != null){
					if(fnType.equals(FeatureTreeContentProvider.FOLDER_NODE_TYPE)){
						
						
					}
				}*/
			}
		}
	}


	protected void addNewVarient(ObjRef selectedNode) {
		
		ObjRef featureContextRef = AS.xarch.createContext(xArchRef, "features");
		
		
		ObjRef featureVarientsRef = (ObjRef) AS.xarch.get(selectedNode,"featureVarients");
				
		if(featureVarientsRef == null){
			featureVarientsRef = AS.xarch.create(featureContextRef, "featureVarients");

			AS.xarch.set(featureVarientsRef, "id", UIDGenerator.generateUID("featureVarients"));
			AS.xarch.add(xArchRef, "featureVarients", featureVarientsRef);
		}
		
		
		ObjRef varientRef = AS.xarch.create(featureContextRef, "varient");
		String varientID = UIDGenerator.generateUID("varient");
		AS.xarch.set(varientRef, "id", varientID);
		
		XadlUtils.setDescription(AS.xarch, varientRef, "New Variant");
		
		//adding Name
		ObjRef varientNameRef = AS.xarch.create(featureContextRef, "FeatureName");
		AS.xarch.set(varientNameRef, "value", "New Variant");
		AS.xarch.set(varientRef,"name",varientNameRef);
		
		
		
		
		//featureOptions
		ObjRef featureOptRef = AS.xarch.create(featureContextRef, "featureOptions");
		AS.xarch.set(featureOptRef, "id", UIDGenerator.generateUID("featureOptions"));
		
		//featureColor
		ObjRef featureColorRef = AS.xarch.create(featureContextRef, "featureColor");
		
		
		//ColorValue
		ObjRef colorValueRef = AS.xarch.create(featureContextRef, "ColorValue");
		AS.xarch.set(colorValueRef, "data","255,0,0");
		AS.xarch.set(colorValueRef, "type", "org.eclipse.swt.graphics.RGB");
		
		AS.xarch.set(featureColorRef, "value", colorValueRef);
		
		//featureSymbole
		ObjRef featureSymboleRef = AS.xarch.create(featureContextRef, "FeatureSymbol");
		AS.xarch.set(featureSymboleRef, "value", varientID);
		
		
		AS.xarch.set(featureOptRef, "featureColor", featureColorRef);
		
		
		AS.xarch.set(featureOptRef,"featureSymbol",featureSymboleRef);
		
		AS.xarch.set(varientRef, "featureOptions", featureOptRef);	
		
		
		
		//featureElements
		ObjRef featureElementsRef = AS.xarch.create(featureContextRef, "featureElements");
		AS.xarch.set(featureElementsRef, "id", UIDGenerator.generateUID("featureElements"));
		
		
	
		
		AS.xarch.set(varientRef, "featureElements", featureElementsRef);
		
		AS.xarch.add(featureVarientsRef, "varient", varientRef);
		
		
	}
}
