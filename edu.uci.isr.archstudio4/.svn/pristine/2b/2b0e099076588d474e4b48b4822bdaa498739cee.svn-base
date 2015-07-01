package edu.uci.isr.archstudio4.comp.archipelago;

import org.eclipse.swt.graphics.Image;
import org.w3c.dom.Node;

import edu.uci.isr.archstudio4.util.XadlTreeUtils;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;

public class FeatureLabelProvider implements IArchipelagoLabelProvider{
	protected ArchipelagoServices AS = null;
	
	public FeatureLabelProvider(ArchipelagoServices services){
		this.AS = services;
	}
	
	public String getText(Object element, String textFromPreviousProvider){
		//String name = textFromPreviousProvider;
		if(element instanceof ObjRef){
			ObjRef ref = (ObjRef)element;
			
			if(AS.xarch.isInstanceOf(ref, "features#Feature")){
				
				String description = XadlUtils.getDescription(AS.xarch, ref);
				if(description == null){
					description = "[No Description]";
				}
				return description;
			}else if(AS.xarch.isInstanceOf(ref, "features#Varient")){
				String description = XadlUtils.getDescription(AS.xarch, ref);
				if(description == null){
					description = "[No Description]";
				}
				return description;
			}
		}
		else if(element instanceof String){
			return (String)element;
		}
		return textFromPreviousProvider;
	}
	
	public Image getImage(Object element, Image imageFromPreviousProvider){
		if(element instanceof ObjRef){
			ObjRef ref = (ObjRef)element;
			if(AS.xarch.isInstanceOf(ref, "features#OptionalFeature")){
				return XadlTreeUtils.getIconForType(AS.resources, XadlTreeUtils.ALTERNATIVE);
			}else if(AS.xarch.isInstanceOf(ref, "features#AlternativeFeature")){
				return XadlTreeUtils.getIconForType(AS.resources, XadlTreeUtils.ALTERNATIVE);
			}else if(AS.xarch.isInstanceOf(ref, "features#Varient")){
				return XadlTreeUtils.getIconForType(AS.resources, XadlTreeUtils.VARIENT);
			}
		}
		return imageFromPreviousProvider;
	}
	
}
