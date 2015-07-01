package edu.uci.isr.archstudio4.comp.archipelago.options;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import edu.uci.isr.archstudio4.action.CodeSynchronizingAction;
import edu.uci.isr.archstudio4.action.CodeTracingAction;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoUtils;
import edu.uci.isr.archstudio4.comp.archipelago.types.StructureMapper;
import edu.uci.isr.archstudio4.comp.archipelago.types.TypesMapper;
import edu.uci.isr.archstudio4.comp.archipelago.util.AbstractEditGuardLogic;
import edu.uci.isr.archstudio4.comp.archipelago.util.Relationship;
import edu.uci.isr.archstudio4.comp.booleannotation.ParseException;
import edu.uci.isr.archstudio4.comp.booleannotation.TokenMgrError;
import edu.uci.isr.archstudio4.comp.xmapper.XMapper;
import edu.uci.isr.bna4.BNAComposite;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.things.glass.BoxGlassThing;
import edu.uci.isr.bna4.things.glass.EndpointGlassThing;
import edu.uci.isr.bna4.things.glass.MappingGlassThing;
import edu.uci.isr.bna4.things.glass.StickySplineGlassThing;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.InvalidOperationException;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;
import edu.umkc.archstudio4.processor.export.ProcessorUtils;

public class EditOptionalLogic extends AbstractEditGuardLogic{
	private final XMapper xMpr;

	public EditOptionalLogic(ArchipelagoServices services, ObjRef xArchRef){
		super(services, xArchRef);
		xMpr = new XMapper(services, xArchRef);
	}
	
	public boolean matches(IBNAView view, IThing t){
		if(t instanceof BoxGlassThing){
			IThing pt = view.getWorld().getBNAModel().getParentThing(t);
			if(pt != null){
				return StructureMapper.isBrickAssemblyRootThing(pt);
			}
		}
		else if(t instanceof EndpointGlassThing){
			IThing pt = view.getWorld().getBNAModel().getParentThing(t);
			if(pt != null){
				return StructureMapper.isInterfaceAssemblyRootThing(pt) ||
					TypesMapper.isSignatureAssemblyRootThing(pt);
			}
		}
		else if(t instanceof StickySplineGlassThing){
			IThing pt = view.getWorld().getBNAModel().getParentThing(t);
			if(pt != null){
				return StructureMapper.isLinkAssemblyRootThing(pt);
			}
		}
		else if(t instanceof MappingGlassThing){
			IThing pt = view.getWorld().getBNAModel().getParentThing(t);
			if(pt != null){
				return TypesMapper.isSignatureInterfaceMappingAssemblyRootThing(pt);
			}
		}
		return false;
	}

	public String getXArchID(IBNAView view, IThing t){
		if(t instanceof BoxGlassThing){
			IThing parentThing = view.getWorld().getBNAModel().getParentThing(t);
			return parentThing.getProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME);
		}
		else if(t instanceof EndpointGlassThing){
			IThing parentThing = view.getWorld().getBNAModel().getParentThing(t);
			return parentThing.getProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME);
		}
		else if(t instanceof StickySplineGlassThing){
			IThing parentThing = view.getWorld().getBNAModel().getParentThing(t);
			return parentThing.getProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME);
		}
		else if(t instanceof MappingGlassThing){
			IThing parentThing = view.getWorld().getBNAModel().getParentThing(t);
			return parentThing.getProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME);
		}
		return null;
	}
	
	protected String getPromotedTypeName(ObjRef ref){
		if(AS.xarch.isInstanceOf(ref, "types#Component")){
			return "optionalComponent";
		}
		else if(AS.xarch.isInstanceOf(ref, "types#Connector")){
			return "optionalConnector";
		}
		else if(AS.xarch.isInstanceOf(ref, "types#Interface")){
			return "optionalInterface";
		}
		else if(AS.xarch.isInstanceOf(ref, "types#Link")){
			return "optionalLink";
		}
		else if(AS.xarch.isInstanceOf(ref, "types#Signature")){
			return "optionalSignature";
		}
		else if(AS.xarch.isInstanceOf(ref, "types#SignatureInterfaceMapping")){
			return "optionalSignatureInterfaceMapping";
		}
		return null;
	}
	
	public ObjRef getGuardParentRef(IBNAModel m, ObjRef eltRef){
		try{
			ObjRef optionalRef = (ObjRef)AS.xarch.get(eltRef, "optional");
			if(optionalRef == null){
				ObjRef optionsContextRef = AS.xarch.createContext(xArchRef, "options");
				optionalRef = AS.xarch.create(optionsContextRef, "optional");
				AS.xarch.set(eltRef, "optional", optionalRef);
			}
			return optionalRef;
		}
		catch(InvalidOperationException ioe){
			return null;
		}
	}
	
	protected IAction[] getActions(final IBNAView view, final IThing t, final int worldX, final int worldY){
		IAction[] localActions = getLocalActions(view, t, worldX, worldY);
		IAction[] inheritedActions = super.getActions(view, t, worldX, worldY);
		
		IAction[] allActions = new IAction[localActions.length + inheritedActions.length];
		System.arraycopy(localActions, 0, allActions, 0, localActions.length);
		System.arraycopy(inheritedActions, 0, allActions, localActions.length, inheritedActions.length);
		return allActions;
	}
	
	protected IAction[] getLocalActions(final IBNAView view, final IThing t, final int worldX, final int worldY){
		final String eltXArchID = getXArchID(view, t);
		if(eltXArchID == null){
			return new IAction[0];
		}
		
		final ObjRef eltRef = AS.xarch.getByID(xArchRef, eltXArchID);
		if(eltRef == null){
			return new IAction[0];
		}
		
		Action addToFeatureAction = new Action("Add to Current Feature"){

			@Override
			public void run(){
				
				
				
				if(isPossiblyOptional(eltRef)){
					//optional
					
					//add OR guard with existing feature 
					ObjRef optionalRef = (ObjRef)AS.xarch.get(eltRef, "optional");
					String expr = AS.booleanNotation.booleanGuardToString(optionalRef);
					String selectedFeature = getCurrentFeature();
					String expr2 = selectedFeature.replaceAll("-", "")+" == \"true\"";
					ObjRef guardRef;
					
					String name = getCurrentFeature();
					String featureSymbol =name.replaceAll("-", "");
					ObjRef featureRef = AS.xarch.getByID(name);
					
					try {
						//System.out.println("("+expr + ") || ("+expr2+")");
						
						if(AS.xarch.isInstanceOf(featureRef, "features#Varient")){
							ObjRef varients = AS.xarch.getParent(featureRef);
							ObjRef feature = AS.xarch.getParent(varients);
							String featureVal = (String)AS.xarch.get(feature, "id");
							featureVal = featureVal.replaceAll("-", "");
							expr2 = featureVal + " == \""+featureSymbol+"\"";
						}else if(AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature")){
							expr2  = featureSymbol + " == \"true\"";
						}else if(AS.xarch.isInstanceOf(featureRef, "features#AlternativeFeature") && isOptionalAlternative(featureRef)){
							
							expr2 = featureSymbol + " != \"false\"";
						}
						
						
						
						guardRef = AS.booleanNotation.parseBooleanGuard("("+expr + ") || ("+expr2+")", xArchRef);
						AS.xarch.set(optionalRef, "guard", guardRef);
					} catch (ParseException e) {
						
						e.printStackTrace();
					} catch (TokenMgrError e) {
						
						e.printStackTrace();
					}
					
					//add to existing feature list
					addArchElementToFeature(selectedFeature,XadlUtils.getID(AS.xarch, eltRef) );
					
					
					ObjRef parent = AS.xarch.getParent(eltRef);
					
					if(isPossiblyOptional(parent)){
						//add OR guard with existing feature
						ObjRef parentOptionalRef = (ObjRef)AS.xarch.get(parent, "optional");
						String parentExpr = AS.booleanNotation.booleanGuardToString(parentOptionalRef);
						
						try {
								
							if(parentNotHasGuard(featureRef, XadlUtils.getID(AS.xarch,parent))){ //check wether parent already has this guard
									if(AS.xarch.isInstanceOf(featureRef, "features#Varient")){
										ObjRef varients = AS.xarch.getParent(featureRef);
										ObjRef feature = AS.xarch.getParent(varients);
										String featureVal = (String)AS.xarch.get(feature, "id");
										featureVal = featureVal.replaceAll("-", "");
										expr2 = featureVal + " == \""+featureSymbol+"\"";
									}else if(AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature")){
										expr2  = featureSymbol + " == \"true\"";
									}else if(AS.xarch.isInstanceOf(featureRef, "features#AlternativeFeature") && isOptionalAlternative(featureRef)){
										
										expr2 = featureSymbol + " != \"false\"";
									}
									
									guardRef = AS.booleanNotation.parseBooleanGuard("("+parentExpr + ") || ("+expr2+")", xArchRef);
									AS.xarch.set(parentOptionalRef, "guard", guardRef);
							}
						} catch (ParseException e) {
							
							e.printStackTrace();
						} catch (TokenMgrError e) {
							
							e.printStackTrace();
						}
						
						//add to existing feature list
						addArchElementToFeature(selectedFeature,XadlUtils.getID(AS.xarch, parent) );
					}
					
				}else{
					//not optional
					ObjRef parent = AS.xarch.getParent(eltRef);
					
					if(isPossiblyOptional(parent)){
						
						//add this parent as OR with current feature
						ObjRef optionalRef = (ObjRef)AS.xarch.get(parent, "optional");
						String expr = AS.booleanNotation.booleanGuardToString(optionalRef);
						String selectedFeature = getCurrentFeature();
						String expr2 = selectedFeature.replaceAll("-", "")+" == \"true\"";
						ObjRef guardRef = null;
						ObjRef elementGuardRef = null;
						try {
							String name = getCurrentFeature();
							String featureSymbol =name.replaceAll("-", "");
							ObjRef featureRef = AS.xarch.getByID(name);
							
							if(parentNotHasGuard(featureRef,XadlUtils.getID(AS.xarch, parent))){//check wether parent already has this guard
							
								if(AS.xarch.isInstanceOf(featureRef, "features#Varient")){
									ObjRef varients = AS.xarch.getParent(featureRef);
									ObjRef feature = AS.xarch.getParent(varients);
									String featureVal = (String)AS.xarch.get(feature, "id");
									featureVal = featureVal.replaceAll("-", "");
									expr2 = featureVal + " == \""+featureSymbol+"\"";
								}else if(AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature")){
									expr2  = featureSymbol + " == \"true\"";
								}else if(AS.xarch.isInstanceOf(featureRef, "features#AlternativeFeature") && isOptionalAlternative(featureRef)){
									
									expr2 = featureSymbol + " != \"false\"";
								}
								
								guardRef = AS.booleanNotation.parseBooleanGuard("("+expr + ") || ("+expr2+")", xArchRef);
								elementGuardRef = AS.booleanNotation.parseBooleanGuard("("+expr + ") || ("+expr2+")", xArchRef);
								AS.xarch.set(optionalRef, "guard", guardRef);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
							
						//add parent to the existing feature list
						addArchElementToFeature(selectedFeature,XadlUtils.getID(AS.xarch, parent) );
						
						//promot Eltref as Optional
						ObjRef optionsContext = AS.xarch.createContext(xArchRef, "options");
						String promotedTypeName = getPromotedTypeName(eltRef);
						if(promotedTypeName != null){
							AS.xarch.promoteTo(optionsContext, promotedTypeName, eltRef);
						}
						//getGuardParentRef
						ObjRef optionsContextRef = AS.xarch.createContext(xArchRef, "options");
						ObjRef optionalRef1 = AS.xarch.create(optionsContextRef, "optional");
						AS.xarch.set(eltRef, "optional", optionalRef1);


						//add this eltRef as optionalguard
						if(elementGuardRef!=null)
						AS.xarch.set(optionalRef1, "guard", elementGuardRef);
							
						addArchElementToFeature(selectedFeature,XadlUtils.getID(AS.xarch, eltRef));
					
					
						
					}
					//parent not optional and currentElement not optional
					else 
					{
						
						//promot Eltref as Optional
						ObjRef optionsContext = AS.xarch.createContext(xArchRef, "options");
						String promotedTypeName = getPromotedTypeName(eltRef);
						if(promotedTypeName != null){
							AS.xarch.promoteTo(optionsContext, promotedTypeName, eltRef);
						}
						//getGuardParentRef
						ObjRef optionsContextRef = AS.xarch.createContext(xArchRef, "options");
						ObjRef optionalRef = AS.xarch.create(optionsContextRef, "optional");
						AS.xarch.set(eltRef, "optional", optionalRef);


						//add this eltRef as optionalguard
						ObjRef guardRef = null;
						try {
							
							String name = getCurrentFeature();
							String featureSymbol =name.replaceAll("-", "");
							ObjRef featureRef = AS.xarch.getByID(name);
							if(AS.xarch.isInstanceOf(featureRef, "features#Varient")){
								ObjRef varients = AS.xarch.getParent(featureRef);
								ObjRef feature = AS.xarch.getParent(varients);
								String featureVal = (String)AS.xarch.get(feature, "id");
								featureVal = featureVal.replaceAll("-", "");
								guardRef = AS.booleanNotation.parseBooleanGuard(featureVal + " == \""+featureSymbol+"\"", xArchRef);
							}else if(AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature")){
								
								guardRef = AS.booleanNotation.parseBooleanGuard(featureSymbol + " == \"true\"", xArchRef);
							}else if(AS.xarch.isInstanceOf(featureRef, "features#AlternativeFeature") && isOptionalAlternative(featureRef)){
								guardRef = AS.booleanNotation.parseBooleanGuard(featureSymbol + " != \"false\"", xArchRef);
							}
							if(guardRef!=null)
							AS.xarch.set(optionalRef, "guard", guardRef);
							
						//add eltRef to the existing feature list
						addArchElementToFeature(name,XadlUtils.getID(AS.xarch, eltRef));
					}catch(Exception e){
						e.printStackTrace();
					}
					
				}
				
			}
		}
			
			@Override
			public boolean isEnabled() {
				String selectedFeature = getCurrentFeature();
				String compID = XadlUtils.getID(AS.xarch, eltRef);
				ObjRef featureRef = AS.xarch.getByID(selectedFeature);
				
				if(featureRef !=null && (AS.xarch.isInstanceOf(featureRef, "features#Varient") || AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature") || (AS.xarch.isInstanceOf(featureRef, "features#AlternativeFeature") && isOptionalAlternative(featureRef)))){
					ObjRef featureElementsRef = (ObjRef) AS.xarch.get(featureRef, "featureElements");
					
					if(featureElementsRef != null){


						ObjRef[] linkRefs = AS.xarch.getAll(featureElementsRef, "archElement");


						for (int i = 0; i < linkRefs.length; i++) {
							ObjRef link = linkRefs[i];
							String href = (String) AS.xarch.get(link, "href");
							if(compID.equals(href.substring(1))){ //exit if the parent already has same guard condition
								return false; 	
							}

						}
						return true;
					}else{
						return false;
					}
				}else{
					return false;
				}
				
				//return super.isEnabled();
			}
		};
		

		if(isPossiblyOptional(eltRef)){
			IAction removeGuardAction = new Action("Make Mandatory/Remove Guard"){
				public void run(){
					AS.xarch.clear(eltRef, "optional");
					
					//TODO remove link from all the feature elements.
					removeElement(XadlUtils.getID(AS.xarch, eltRef));
					
				}
				
				
			};
			IAction removeFromCurrentFeature = new Action("Remove from Current Feature"){
				
				ObjRef linkId = null;
				public void run() {
					String selectedFeature = getCurrentFeature();
					String compID = XadlUtils.getID(AS.xarch, eltRef);
					ObjRef featureRef = AS.xarch.getByID(selectedFeature);
					ObjRef featureElementsRef = (ObjRef) AS.xarch.get(featureRef, "featureElements");
					
					if(AS.xarch.isInstanceOf(featureRef, "features#Varient") || AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature") || isOptionalAlternative(featureRef) ){
						
						
						if(featureElementsRef != null){


							ObjRef[] linkRefs = AS.xarch.getAll(featureElementsRef, "archElement");


							for (int i = 0; i < linkRefs.length; i++) {
								ObjRef link = linkRefs[i];
								String href = (String) AS.xarch.get(link, "href");
								if(compID.equals(href.substring(1))){ 
									linkId = link;
									break; 	
								}

							}
							if(linkId == null){
							return ;
							}
						}else{
							return ;
						}
					}
					
					
					
					try {
						adjustGuardCondition(selectedFeature,eltRef);
					} catch (ParseException e) {
						
						e.printStackTrace();
					} catch (TokenMgrError e) {
						
						e.printStackTrace();
					}
					
					
					
					
				
					
					
					
					//remove link form the feature lists
					if(linkId != null) {
						AS.xarch.remove(featureElementsRef, "archElement", linkId);
					}
				};
				
				@Override
				public boolean isEnabled() {
				

					String selectedFeature = getCurrentFeature();
					String compID = XadlUtils.getID(AS.xarch, eltRef);
					ObjRef featureRef = AS.xarch.getByID(selectedFeature);
					
					if(featureRef !=null && (AS.xarch.isInstanceOf(featureRef, "features#Varient") || AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature") || (AS.xarch.isInstanceOf(featureRef, "features#AlternativeFeature") && isOptionalAlternative(featureRef)))){
						ObjRef featureElementsRef = (ObjRef) AS.xarch.get(featureRef, "featureElements");
						
						if(featureElementsRef != null){


							ObjRef[] linkRefs = AS.xarch.getAll(featureElementsRef, "archElement");


							for (int i = 0; i < linkRefs.length; i++) {
								ObjRef link = linkRefs[i];
								String href = (String) AS.xarch.get(link, "href");
								if(compID.equals(href.substring(1))){ //exit if the parent already has same guard condition
									linkId = link;
									return true; 	
								}

							}
							return false;
						}else{
							return false;
						}
					}else{
						return false;
					}
					
					//return super.isEnabled();
				
				}
				
			};
			if(getCurrentFeature()!=null){
				return new IAction[]{removeGuardAction,addToFeatureAction,removeFromCurrentFeature};
			}else{
				return new IAction[]{removeGuardAction,removeFromCurrentFeature};
			}
		}
		else{
			IAction promoteToOptionalAction = new Action("Promote to Optional"){
				public void run(){
					ObjRef optionsContext = AS.xarch.createContext(xArchRef, "options");
					String promotedTypeName = getPromotedTypeName(eltRef);
					if(promotedTypeName != null){
						AS.xarch.promoteTo(optionsContext, promotedTypeName, eltRef);
					}
				}
			};
			
			if(getCurrentFeature() != null) {
				return new IAction[]{promoteToOptionalAction,addToFeatureAction};
			} else {
				return new IAction[]{promoteToOptionalAction};
			}
		}
	}
	
	protected void removeElement(String id) {
	
		//ObjRef gobalFeatures = AS.xarch.get
		
		ObjRef featureContextRef =  AS.xarch.createContext(xArchRef, "features");
		ObjRef archFeatureRef =  AS.xarch.getElement(featureContextRef, "archFeature", xArchRef);
		if(archFeatureRef!=null){

			ObjRef[] featuresList =  AS.xarch.getAll(archFeatureRef, "feature");
			for (ObjRef objRef : featuresList) {
			//	ObjRef nameRef = (ObjRef) AS.xarch.get(objRef, "featureName");
				ObjRef typeRef = (ObjRef) AS.xarch.get(objRef, "type");
				String type = (String)  AS.xarch.get(typeRef, "value");
				
				
				if(type.equals("optional") || type.equals("alternative")){
					ObjRef featureElementsRef = (ObjRef) AS.xarch.get(objRef, "featureElements");
					if(featureElementsRef != null){


						ObjRef[] linkRefs = AS.xarch.getAll(featureElementsRef, "archElement");


						for (int i = 0; i < linkRefs.length; i++) {
							ObjRef link = linkRefs[i];
							String href = (String) AS.xarch.get(link, "href");
							if(id.equals(href.substring(1))){ 
								AS.xarch.remove(featureElementsRef, "archElement", link);
								
							}

						}
					}
				
				}
				if(type.equals("optionalAlternative") || type.equals("alternative") ){
					
					
					
					ObjRef featureVarients = (ObjRef)AS.xarch.get(objRef, "featureVarients");
					ObjRef[] varientsList = AS.xarch.getAll(featureVarients, "varient");
					for (ObjRef varient : varientsList) {
						ObjRef featureElementsRef = (ObjRef) AS.xarch.get(varient, "featureElements");
						if(featureElementsRef != null){


							ObjRef[] linkRefs = AS.xarch.getAll(featureElementsRef, "archElement");


							for (int i = 0; i < linkRefs.length; i++) {
								ObjRef link = linkRefs[i];
								String href = (String) AS.xarch.get(link, "href");
								if(id.equals(href.substring(1))){ 
									AS.xarch.remove(featureElementsRef, "archElement", link);
									
								}

							}
						}
					}
					
				}
				
			}//end of for

		} //end of null check
		
	}

	protected boolean parentNotHasGuard(ObjRef featureRef,String parentId) {
		ObjRef featureElementsRef = (ObjRef) AS.xarch.get(featureRef, "featureElements");
		
		if(featureElementsRef != null){


			ObjRef[] linkRefs = AS.xarch.getAll(featureElementsRef, "archElement");


			for (int i = 0; i < linkRefs.length; i++) {
				ObjRef link = linkRefs[i];
				String href = (String) AS.xarch.get(link, "href");
				if(parentId.equals(href.substring(1))){ //exit if the parent already has same guard condition
					return false; 	
				}

			}
			return true;
		}else{
			return true;
		}
		
	}

	protected boolean adjustGuardCondition(String selectedFeature, ObjRef eltRef) throws ParseException, TokenMgrError {
		ObjRef featureRef = AS.xarch.getByID(selectedFeature);
		selectedFeature = selectedFeature.replaceAll("-", "");
		ObjRef optionalRef = (ObjRef)AS.xarch.get(eltRef, "optional");
		String expr = AS.booleanNotation.booleanGuardToString(optionalRef);
		if(expr.indexOf("||") == -1){
			
			AS.xarch.clear(eltRef, "optional");
			//remove link form the feature lists
			String tagName = AS.xarch.getElementName(eltRef);
			if(tagName != null){
				ObjRef parentRef = AS.xarch.getParent(eltRef);
				if(parentRef != null){
					AS.xarch.remove(parentRef, tagName, eltRef);
				}
			}
			
			return true;
		}
		
		if(AS.xarch.isInstanceOf(featureRef, "features#Varient")) {
			

			ObjRef varients = AS.xarch.getParent(featureRef);
			ObjRef Altrfeature = AS.xarch.getParent(varients);
			selectedFeature = XadlUtils.getID(AS.xarch, Altrfeature);
			selectedFeature = selectedFeature.replaceAll("-", "");
			
			int index = expr.indexOf(selectedFeature);
			
			if(index!=-1){
				int  orIndex = expr.indexOf("||", index);
				int parathIndex = expr.indexOf(")", index);
				if(orIndex == -1){
					StringBuilder exprBuilder = new StringBuilder(expr);
					orIndex = expr.lastIndexOf("||", index);
					exprBuilder.replace(orIndex, parathIndex, "");
					expr = exprBuilder.toString();
				}else{
					if(orIndex < parathIndex){
						StringBuilder exprBuilder = new StringBuilder(expr);
						exprBuilder.replace(index, orIndex+2, "");
						expr = exprBuilder.toString();
					}else{
						StringBuilder exprBuilder = new StringBuilder(expr);
						orIndex = expr.lastIndexOf("||", index);
						exprBuilder.replace(orIndex, parathIndex, "");
						expr = exprBuilder.toString();
					}
				}
				
				ObjRef guardRef = AS.booleanNotation.parseBooleanGuard(expr, xArchRef);
				AS.xarch.set(optionalRef, "guard", guardRef);
			}else{
				//unexpected no selected feature is there;
				return false;
			}
			
		
			
			
		}else if(AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature") || isOptionalAlternative(featureRef) ){
			
			int index = expr.indexOf(selectedFeature);
			
			if(index!=-1){
				int  orIndex = expr.indexOf("||", index);
				int parathIndex = expr.indexOf(")", index);
				if(orIndex == -1){
					StringBuilder exprBuilder = new StringBuilder(expr);
					orIndex = expr.lastIndexOf("||", index);
					exprBuilder.replace(orIndex, parathIndex, "");
					expr = exprBuilder.toString();
				}else{
					if(orIndex < parathIndex){
						StringBuilder exprBuilder = new StringBuilder(expr);
						exprBuilder.replace(index, orIndex+2, "");
						expr = exprBuilder.toString();
					}else{
						StringBuilder exprBuilder = new StringBuilder(expr);
						orIndex = expr.lastIndexOf("||", index);
						exprBuilder.replace(orIndex, parathIndex, "");
						expr = exprBuilder.toString();
					}
				}
				
				ObjRef guardRef = AS.booleanNotation.parseBooleanGuard(expr, xArchRef);
				AS.xarch.set(optionalRef, "guard", guardRef);
				
			}else{
				//unexpected no selected feature is there;
				return false;
			}
			
		}
		
		return false;
		
	}

	public String getCurrentFeature(){
		BNAComposite composit = ArchipelagoUtils.getBNAComposite(AS.editor);
		if(composit == null){
			return null;
		}
		if(composit.getView() == null){
			return null;
		}
		
		if(composit.getView().getWorld() == null){
			return null;
		}
		if(composit.getView().getWorld().getBNAModel() == null){
			return null;
		}
		
		IBNAModel model = composit.getView().getWorld().getBNAModel();
		return model.getSelectedFeature();
	}
	
	public boolean isPossiblyOptional(ObjRef ref){
		try{
			ObjRef optionalRef = (ObjRef)AS.xarch.get(ref, "optional");
			if(optionalRef == null){
				return false;
			}
			String expr = AS.booleanNotation.booleanGuardToString(optionalRef);
			if(expr !=null)
			return true;
			else
				return false;
		}
		catch(InvalidOperationException ioe){
			return false;
		}
	}
	
	protected boolean isOptionalAlternative(ObjRef featureRef) {

		ObjRef typeRef = (ObjRef) AS.xarch.get(featureRef, "type");
		String value = (String) AS.xarch.get(typeRef, "value");
		if(value.equals("optionalAlternative")){
			return true;
		}else{
			return false;
		}
	}
	
	protected void addArchElementToFeature(String selectedFeature,String id) {

		//New Implementation.
		ObjRef featureRef = AS.xarch.getByID(selectedFeature);
		if(AS.xarch.isInstanceOf(featureRef, "features#Varient")){
			ObjRef featureElementsRef = (ObjRef) AS.xarch.get(featureRef, "featureElements");
			ObjRef instanceContextRef = AS.xarch.createContext(xArchRef, "instance");
			ObjRef XMLLinkRef = AS.xarch.create(instanceContextRef, "XMLLink");
			AS.xarch.set(XMLLinkRef, "href", "#"+id);
			AS.xarch.set(XMLLinkRef, "type", "simple");
			AS.xarch.add(featureElementsRef, "archElement", XMLLinkRef);
		}else if(AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature")){
			ObjRef featureElementsRef = (ObjRef) AS.xarch.get(featureRef, "featureElements");
			ObjRef instanceContextRef = AS.xarch.createContext(xArchRef, "instance");
			ObjRef XMLLinkRef = AS.xarch.create(instanceContextRef, "XMLLink");
			AS.xarch.set(XMLLinkRef, "href", "#"+id);
			AS.xarch.set(XMLLinkRef, "type", "simple");
			AS.xarch.add(featureElementsRef, "archElement", XMLLinkRef);
		}else if(AS.xarch.isInstanceOf(featureRef, "features#AlternativeFeature") && isOptionalAlternative(featureRef)){
			ObjRef featureElementsRef = (ObjRef) AS.xarch.get(featureRef, "featureElements");
			ObjRef instanceContextRef = AS.xarch.createContext(xArchRef, "instance");
			ObjRef XMLLinkRef = AS.xarch.create(instanceContextRef, "XMLLink");
			AS.xarch.set(XMLLinkRef, "href", "#"+id);
			AS.xarch.set(XMLLinkRef, "type", "simple");
			AS.xarch.add(featureElementsRef, "archElement", XMLLinkRef);
		}
		//ObjRef objRef = AS.xarch.getByID(id);
		//XadlUtils.setXLink(AS.xarch, featureRef, "archElement", objRef);
		
	}
}
