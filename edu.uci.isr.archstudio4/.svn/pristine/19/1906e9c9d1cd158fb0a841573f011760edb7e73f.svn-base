package edu.uci.isr.archstudio4.comp.archipelago.interactions;

import org.eclipse.swt.graphics.Image;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.IArchipelagoLabelProvider;
import edu.uci.isr.archstudio4.util.ArchstudioResources;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;

public class InteractionsTreeLabelProvider implements IArchipelagoLabelProvider{
	protected ArchipelagoServices AS = null;
	
	public InteractionsTreeLabelProvider(ArchipelagoServices services){
		this.AS = services;
	}
	
	public String getText(Object element, String textFromPreviousProvider){
		if(element instanceof ObjRef){
			ObjRef ref = (ObjRef)element;
			if(AS.xarch.isInstanceOf(ref, "interactions#Interaction")){
				String description = XadlUtils.getDescription(AS.xarch, ref);
				if(description == null){
					description = "[No Description]";
				}
				return description;
			}
		}
		return textFromPreviousProvider;
	}
	
	public Image getImage(Object element, Image imageFromPreviousProvider){
		if(element instanceof ObjRef){
			ObjRef ref = (ObjRef)element;
			if(AS.xarch.isInstanceOf(ref, "interactions#Interaction")){
				return AS.resources.getImage(ArchstudioResources.ICON_STATECHART);
			}
		}
		return imageFromPreviousProvider;
	}
	
}
