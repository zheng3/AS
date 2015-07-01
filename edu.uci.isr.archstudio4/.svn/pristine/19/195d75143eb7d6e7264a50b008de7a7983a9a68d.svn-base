package edu.uci.isr.archstudio4.comp.archipelago.features;

import org.eclipse.jface.viewers.Viewer;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoUtils;
import edu.uci.isr.archstudio4.comp.archipelago.FolderNode;
import edu.uci.isr.archstudio4.comp.archipelago.IArchipelagoTreeContentProvider;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchPath;

public class FeatureTreeContentProvider implements IArchipelagoTreeContentProvider{
	protected static final String FOLDER_NODE_TYPE = "FEATURES";
	protected FolderNode featureFolderNode = null;
	
	protected ArchipelagoServices AS = null;
	protected ObjRef xArchRef = null;
	
	public FeatureTreeContentProvider(ArchipelagoServices services, ObjRef xArchRef){
		this.AS = services;
		this.xArchRef = xArchRef;
	}
	
	protected boolean isRootElement(Object ref){
		if(ref instanceof ObjRef){
			return AS.xarch.isInstanceOf((ObjRef)ref, "#XArch");
		}
		return false;
	}
	
	protected boolean isFolderNode(Object fn){
		if(fn instanceof FolderNode){
			return ((FolderNode)fn).getType().equals(FOLDER_NODE_TYPE);
		}
		return false;
	}
	
	protected boolean isTargetNode(Object ref){
		if(ref instanceof ObjRef){
			XArchPath refPath = AS.xarch.getXArchPath((ObjRef)ref);
			//System.out.println(refPath.toTagsOnlyString());
			if(refPath.toTagsOnlyString().equals("xArch/archFeature/feature")){
			//	ObjRef featureContextRef = AS.xarch.createContext(xArchRef, "features");
				ObjRef type =(ObjRef)AS.xarch.get((ObjRef)ref, "type");
				if(type != null){
				String val = (String) AS.xarch.get(type,"value");
				//System.out.println(val);
				
				}
				return true;
			}
		}
		return false;
	}
	
	protected boolean isAlternativeNode(Object ref){
		if(ref instanceof ObjRef){
			XArchPath refPath = AS.xarch.getXArchPath((ObjRef)ref);
			//System.out.println(refPath.toTagsOnlyString());
			if(refPath.toTagsOnlyString().equals("xArch/archFeature/feature")){
			//	ObjRef featureContextRef = AS.xarch.createContext(xArchRef, "features");
				ObjRef type =(ObjRef)AS.xarch.get((ObjRef)ref, "type");
				if(type != null){
				String val = (String) AS.xarch.get(type,"value");
				
					if(val.equalsIgnoreCase("alternative") || val.equalsIgnoreCase("optionalAlternative")){
						return true;
					}
				}
				
			}
		}
		return false;
	}
	
	public Object[] getChildren(Object parentElement, Object[] childrenFromPreviousProvider){
		if(isRootElement(parentElement)){
			if(featureFolderNode == null){
				featureFolderNode = new FolderNode((ObjRef)parentElement, FOLDER_NODE_TYPE, "Features");
			}
			return ArchipelagoUtils.combine(childrenFromPreviousProvider, new Object[]{featureFolderNode});
		}
		else if(isFolderNode(parentElement)){
			
			FolderNode fn = (FolderNode)parentElement;
			ObjRef xArchRef = (ObjRef)((FolderNode)fn).getParent();
			ObjRef featureContextRef = AS.xarch.createContext(xArchRef, "features");
			
			ObjRef archFeature = AS.xarch.getElement(featureContextRef, "archFeature", xArchRef);
			
			if(archFeature == null){
				return childrenFromPreviousProvider;
			}
			Object[] featuresRef = AS.xarch.getAll(archFeature, "feature");
			
			return ArchipelagoUtils.combine(childrenFromPreviousProvider, featuresRef);
		}else if(isAlternativeNode(parentElement)){
			ObjRef featureVarients = (ObjRef) AS.xarch.get((ObjRef) parentElement,"featureVarients");
			Object[] varients = AS.xarch.getAll(featureVarients,"varient");
			//String[] child = {"Child"};
			return ArchipelagoUtils.combine(childrenFromPreviousProvider, varients);
		}
		return childrenFromPreviousProvider;
	}
	
	public boolean hasChildren(Object element, boolean hasChildrenFromPreviousProvider){
		if(isRootElement(element)){
			return true;
		}
		else if(isFolderNode(element)){
			Object[] list = getChildren(element, null);
			if(list == null ){
				return false;
			}else{
				int len = list.length;
				return len> 0;	
			}
			
		}else if(isAlternativeNode(element)){
			Object[] list = getChildren(element, null);
			if(list == null ){
				return false;
			}else{
				int len = list.length;
				return len> 0;	
			}
		}
		return hasChildrenFromPreviousProvider;
	}
	
	public Object getParent(Object element, Object parentFromPreviousProvider){
		if(isTargetNode(element)){
			return featureFolderNode;
		}
		if(isFolderNode(element)){
			return ((FolderNode)element).getParent();
		}
		return parentFromPreviousProvider;
	}
	
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput){
	}

	public void dispose(){
	}
	
}
