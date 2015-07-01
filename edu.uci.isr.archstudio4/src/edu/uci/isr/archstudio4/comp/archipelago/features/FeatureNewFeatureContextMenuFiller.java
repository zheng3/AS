package edu.uci.isr.archstudio4.comp.archipelago.features;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

public class FeatureNewFeatureContextMenuFiller implements IArchipelagoTreeContextMenuFiller{
	protected TreeViewer viewer = null;
	protected ArchipelagoServices AS =  null;
	protected ObjRef xArchRef = null;
	
	public FeatureNewFeatureContextMenuFiller(TreeViewer viewer, ArchipelagoServices AS, ObjRef xArchRef){
		this.viewer = viewer;
		this.AS = AS;
		this.xArchRef = xArchRef;
	}
	
	
	public void fillContextMenu(IMenuManager m, Object[] selectedNodes){
		if((selectedNodes != null) && (selectedNodes.length == 1)){
			Object selectedNode = selectedNodes[0];
			if(selectedNode instanceof FolderNode){
				FolderNode fn = (FolderNode)selectedNode;
				String fnType = fn.getType();
				if(fnType != null){
					if(fnType.equals(FeatureTreeContentProvider.FOLDER_NODE_TYPE)){
						IAction newOptionalFeatureAction = new Action("New Optional Feature"){
							public void run(){
								createNewOptionalFeature();
							}
						};
						IAction newAlternativeFeatureAction = new Action("New Alternative Feature"){
							public void run(){
								createNewAlternativeFeature();
							}
						};
						IAction newOptAltFeatureAction = new Action("New Optional Alternative Feature"){
							public void run(){
								//createNewAlternativeFeature();
								createOptAlternativeFeature();
							}
						};
						m.add(newOptionalFeatureAction);
						m.add(newAlternativeFeatureAction);
						m.add(newOptAltFeatureAction);
						
					}
				}
			}
		}
	}

	protected void createOptAlternativeFeature(){


		
		
		//Create the "archFeature" reference if not created yet
			ObjRef featureContextRef = AS.xarch.createContext(xArchRef, "features");
			ObjRef archFeatureRef = AS.xarch.getElement(featureContextRef, "archFeature", xArchRef);
			if (archFeatureRef == null){
				//Create ArchFeature
				archFeatureRef = AS.xarch.createElement(featureContextRef, "archFeature");
				String changeID = UIDGenerator.generateUID("archFeature");				
				AS.xarch.set(archFeatureRef, "id", changeID);
				XadlUtils.setDescription(AS.xarch, archFeatureRef, "Architecture Feature Model");
				AS.xarch.add(xArchRef, "object", archFeatureRef);				
			}
			
			//Start a new feature session
			ObjRef featuresRef = AS.xarch.create(featureContextRef, "feature");
			String featureID = UIDGenerator.generateUID("feature");
			AS.xarch.set(featuresRef, "id", featureID);
			
			
			XadlUtils.setDescription(AS.xarch, featuresRef, "New Feature");
			
			//adding Name
			ObjRef featureNameRef = AS.xarch.create(featureContextRef, "FeatureName");
			AS.xarch.set(featureNameRef, "value", "New Feature");
			AS.xarch.set(featuresRef,"featureName",featureNameRef);
			
			//Adding type
			ObjRef featureTypeRef = AS.xarch.create(featureContextRef, "FeatureType");
			AS.xarch.set(featureTypeRef, "value", "optionalAlternative");
			AS.xarch.set(featuresRef,"type",featureTypeRef);
			
			//Adding Binding time
			ObjRef bindingTimeRef = AS.xarch.create(featureContextRef, "BindingTime");
			AS.xarch.set(bindingTimeRef, "value", "development");
			AS.xarch.set(featuresRef,"bindingTime",bindingTimeRef);
			
			//Adding Default value
			/*ObjRef defaultValueRef = AS.xarch.create(featureContextRef, "DefaultValue");
			AS.xarch.set(defaultValueRef, "value", "True");
			AS.xarch.set(featuresRef,"defaultValue",defaultValueRef);*/
			
			
			
			//promote to optionalFeature
			AS.xarch.promoteTo(featureContextRef, "alternativeFeature", featuresRef);
			
			//featureElements
			ObjRef featureElementsRef1 = AS.xarch.create(featureContextRef, "featureElements");
			AS.xarch.set(featureElementsRef1, "id", UIDGenerator.generateUID("featureElements"));
			
			AS.xarch.set(featuresRef, "featureElements", featureElementsRef1);	
			
			
			//FeatureVarients
			ObjRef featureVarientsRef = AS.xarch.create(featureContextRef, "featureVarients");
			
			AS.xarch.set(featureVarientsRef, "id", UIDGenerator.generateUID("featureVarients"));
			
			//varient
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
			
			
			//instance:XMLLink
			/*ObjRef instanceContextRef = AS.xarch.createContext(xArchRef, "instance");
			ObjRef XMLLinkRef = AS.xarch.create(instanceContextRef, "XMLLink");
			AS.xarch.set(XMLLinkRef, "href", "#componentffa80015-357eb752-c9fa637d-937f0051");
			AS.xarch.set(XMLLinkRef, "type", "simple");
			
			AS.xarch.add(featureElementsRef, "archElement", XMLLinkRef);	*/
			
			//adding default value
			ObjRef defaultValueRef = AS.xarch.create(featureContextRef, "DefaultValue");
			AS.xarch.set(defaultValueRef, "value", varientID);
			AS.xarch.set(featuresRef,"defaultValue",defaultValueRef);
			
			
			AS.xarch.set(varientRef, "featureElements", featureElementsRef);
			
			AS.xarch.add(featureVarientsRef, "varient", varientRef);
			
			AS.xarch.set(featuresRef, "featureVarients", featureVarientsRef);
			
			AS.xarch.add(archFeatureRef, "feature", featuresRef);	
			
			

	}
	protected void createNewAlternativeFeature() {

		
		
		//Create the "archFeature" reference if not created yet
			ObjRef featureContextRef = AS.xarch.createContext(xArchRef, "features");
			ObjRef archFeatureRef = AS.xarch.getElement(featureContextRef, "archFeature", xArchRef);
			if (archFeatureRef == null){
				//Create ArchFeature
				archFeatureRef = AS.xarch.createElement(featureContextRef, "archFeature");
				String changeID = UIDGenerator.generateUID("archFeature");				
				AS.xarch.set(archFeatureRef, "id", changeID);
				XadlUtils.setDescription(AS.xarch, archFeatureRef, "Architecture Feature Model");
				AS.xarch.add(xArchRef, "object", archFeatureRef);				
			}
			
			//Start a new feature session
			ObjRef featuresRef = AS.xarch.create(featureContextRef, "feature");
			String featureID = UIDGenerator.generateUID("feature");
			AS.xarch.set(featuresRef, "id", featureID);
			
			
			XadlUtils.setDescription(AS.xarch, featuresRef, "New Feature");
			
			//adding Name
			ObjRef featureNameRef = AS.xarch.create(featureContextRef, "FeatureName");
			AS.xarch.set(featureNameRef, "value", "New Feature");
			AS.xarch.set(featuresRef,"featureName",featureNameRef);
			
			//Adding type
			ObjRef featureTypeRef = AS.xarch.create(featureContextRef, "FeatureType");
			AS.xarch.set(featureTypeRef, "value", "alternative");
			AS.xarch.set(featuresRef,"type",featureTypeRef);
			
			//Adding Binding time
			ObjRef bindingTimeRef = AS.xarch.create(featureContextRef, "BindingTime");
			AS.xarch.set(bindingTimeRef, "value", "development");
			AS.xarch.set(featuresRef,"bindingTime",bindingTimeRef);
			
			//Adding Default value
			/*ObjRef defaultValueRef = AS.xarch.create(featureContextRef, "DefaultValue");
			AS.xarch.set(defaultValueRef, "value", "True");
			AS.xarch.set(featuresRef,"defaultValue",defaultValueRef);*/
			
			
			
			//promote to optionalFeature
			AS.xarch.promoteTo(featureContextRef, "alternativeFeature", featuresRef);
			
			//featureElements
			ObjRef featureElementsRef1 = AS.xarch.create(featureContextRef, "featureElements");
			AS.xarch.set(featureElementsRef1, "id", UIDGenerator.generateUID("featureElements"));
			
			AS.xarch.set(featuresRef, "featureElements", featureElementsRef1);	
			
			
			//FeatureVarients
			ObjRef featureVarientsRef = AS.xarch.create(featureContextRef, "featureVarients");
			
			AS.xarch.set(featureVarientsRef, "id", UIDGenerator.generateUID("featureVarients"));
			
			//varient
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
			
			
			//instance:XMLLink
			/*ObjRef instanceContextRef = AS.xarch.createContext(xArchRef, "instance");
			ObjRef XMLLinkRef = AS.xarch.create(instanceContextRef, "XMLLink");
			AS.xarch.set(XMLLinkRef, "href", "#componentffa80015-357eb752-c9fa637d-937f0051");
			AS.xarch.set(XMLLinkRef, "type", "simple");
			
			AS.xarch.add(featureElementsRef, "archElement", XMLLinkRef);	*/
			
			//adding default value
			ObjRef defaultValueRef = AS.xarch.create(featureContextRef, "DefaultValue");
			AS.xarch.set(defaultValueRef, "value", varientID);
			AS.xarch.set(featuresRef,"defaultValue",defaultValueRef);
			
			
			AS.xarch.set(varientRef, "featureElements", featureElementsRef);
			
			AS.xarch.add(featureVarientsRef, "varient", varientRef);
			
			AS.xarch.set(featuresRef, "featureVarients", featureVarientsRef);
			
			AS.xarch.add(archFeatureRef, "feature", featuresRef);	
			
			
}


	protected void createNewOptionalFeature(){
	
		
			//Create the "archFeature" reference if not created yet
				ObjRef featureContextRef = AS.xarch.createContext(xArchRef, "features");
				ObjRef archFeatureRef = AS.xarch.getElement(featureContextRef, "archFeature", xArchRef);
				if (archFeatureRef == null){
					//Create ArchFeature
					archFeatureRef = AS.xarch.createElement(featureContextRef, "archFeature");
					String changeID = UIDGenerator.generateUID("archFeature");				
					AS.xarch.set(archFeatureRef, "id", changeID);
					XadlUtils.setDescription(AS.xarch, archFeatureRef, "Architecture Feature Model");
					AS.xarch.add(xArchRef, "object", archFeatureRef);				
				}
				
				//Start a new feature session
				ObjRef featuresRef = AS.xarch.create(featureContextRef, "feature");
				String featureID = UIDGenerator.generateUID("feature");
				AS.xarch.set(featuresRef, "id", featureID);
				
				
				XadlUtils.setDescription(AS.xarch, featuresRef, "New Feature");
				
				//adding Name
				ObjRef featureNameRef = AS.xarch.create(featureContextRef, "FeatureName");
				AS.xarch.set(featureNameRef, "value", "New Feature");
				AS.xarch.set(featuresRef,"featureName",featureNameRef);
				
				//Adding type
				ObjRef featureTypeRef = AS.xarch.create(featureContextRef, "FeatureType");
				AS.xarch.set(featureTypeRef, "value", "optional");
				AS.xarch.set(featuresRef,"type",featureTypeRef);
				
				//Adding Binding time
				ObjRef bindingTimeRef = AS.xarch.create(featureContextRef, "BindingTime");
				AS.xarch.set(bindingTimeRef, "value", "development");
				AS.xarch.set(featuresRef,"bindingTime",bindingTimeRef);
				
				//Adding Default value
				ObjRef defaultValueRef = AS.xarch.create(featureContextRef, "DefaultValue");
				AS.xarch.set(defaultValueRef, "value", "True");
				AS.xarch.set(featuresRef,"defaultValue",defaultValueRef);
				
				
				//promote to optionalFeature
				AS.xarch.promoteTo(featureContextRef, "optionalFeature", featuresRef);
				
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
				AS.xarch.set(featureSymboleRef, "value", featureID);
				
				
				AS.xarch.set(featureOptRef, "featureColor", featureColorRef);
				
				
				AS.xarch.set(featureOptRef,"featureSymbol",featureSymboleRef);
				
				AS.xarch.set(featuresRef, "featureOptions", featureOptRef);	
				
				
				
				//featureElements
				ObjRef featureElementsRef = AS.xarch.create(featureContextRef, "featureElements");
				AS.xarch.set(featureElementsRef, "id", UIDGenerator.generateUID("featureElements"));
				
				
				
				
				AS.xarch.set(featuresRef, "featureElements", featureElementsRef);	
				
				AS.xarch.add(archFeatureRef, "feature", featuresRef);	
				
				

	
		
	

		
	}

}
