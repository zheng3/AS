package edu.uci.isr.archstudio4.comp.archipelago.features;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.IArchipelagoTreeContextMenuFiller;
import edu.uci.isr.widgets.swt.ColorSelectorDialog;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.umkc.archstudio4.processor.export.CodeFragmentHighlighter;

public class FeatureAddColorContextMenuFiller  implements IArchipelagoTreeContextMenuFiller{
	protected TreeViewer viewer = null;
	protected ArchipelagoServices AS =  null;
	protected ObjRef xArchRef = null;
	
	public FeatureAddColorContextMenuFiller(TreeViewer viewer, ArchipelagoServices AS, ObjRef xArchRef){
		this.viewer = viewer;
		this.AS = AS;
		this.xArchRef = xArchRef;
	}
	
	
	public void fillContextMenu(IMenuManager m, Object[] selectedNodes){
		if((selectedNodes != null) && (selectedNodes.length == 1)){
			final Object selectedNode = selectedNodes[0];
			//System.out.println(selectedNode);
			if(selectedNode instanceof ObjRef){
				if(AS.xarch.isInstanceOf((ObjRef) selectedNode, "Features#Varient") || AS.xarch.isInstanceOf((ObjRef) selectedNode, "Features#OptionalFeature")  ){
					IAction addColorAction = new Action("Change Color"){
						public void run(){
							addColor((ObjRef) selectedNode);
						}
					};
				
					IAction markDefaultAction = new Action("Mark as Default"){
						public void run(){
							markAsDefault((ObjRef) selectedNode);
						}
					};
					
					m.add(addColorAction);
					
					if(AS.xarch.isInstanceOf((ObjRef) selectedNode, "Features#Varient"))
					m.add(markDefaultAction);
				}
				
			}
		}
	}


	protected void markAsDefault(ObjRef selectedNode) {
		
		String id = (String) AS.xarch.get(selectedNode,"id");
		ObjRef varients = AS.xarch.getParent(selectedNode);
		ObjRef feature = AS.xarch.getParent(varients);
		
		ObjRef defaultRef  = (ObjRef) AS.xarch.get(feature, "defaultValue");
		if(defaultRef == null){
			ObjRef featureContextRef = AS.xarch.createContext(xArchRef, "features");
			defaultRef = AS.xarch.create(featureContextRef, "DefaultValue");
			AS.xarch.set(defaultRef, "value", id);
			AS.xarch.set(feature,"defaultValue",defaultRef);
		}else{
			AS.xarch.set(defaultRef, "value", id);
		}
		
	}


	protected void addColor(ObjRef selectedNode) {
		
		ColorSelectorDialog csd = new ColorSelectorDialog(new Shell());
		ObjRef featureOptRef = (ObjRef) AS.xarch.get(selectedNode, "featureOptions");
		ObjRef colorRef  = (ObjRef) AS.xarch.get(featureOptRef, "featureColor");
		ObjRef colorValRef  = (ObjRef) AS.xarch.get(colorRef, "value");
		String val = (String) AS.xarch.get(colorValRef,"data");
		String colors[] = val.split(",");
		RGB r = new RGB(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
		RGB rgb = csd.open(r);
		if(rgb != null){
			String data = rgb.red+","+rgb.green+","+rgb.blue;
//			ObjRef featureOptRef = (ObjRef) AS.xarch.get(selectedNode, "featureOptions");
//			ObjRef colorRef  = (ObjRef) AS.xarch.get(featureOptRef, "featureColor");
//			ObjRef colorValRef  = (ObjRef) AS.xarch.get(colorRef, "value");
			AS.xarch.set(colorValRef, "data", data);				
			CodeFragmentHighlighter.changeColor(XadlUtils.getID(AS.xarch, selectedNode), data);
		//	assignColor(view, thingsToEdit, rgb);
			//System.out.println(rgb.toString());
		}
	}
}
