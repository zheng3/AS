package edu.uci.isr.archstudio4.comp.archipelago.interactions;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TreeViewer;

import edu.uci.isr.archstudio4.comp.archipelago.AbstractArchipelagoTreePlugin;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.IArchipelagoTreeContextMenuFiller;
import edu.uci.isr.xarchflat.ObjRef;

public class InteractionsTreePlugin extends AbstractArchipelagoTreePlugin {
	
	public InteractionsTreePlugin(TreeViewer viewer, ArchipelagoServices AS, ObjRef xArchRef){
		this.contentProvider = new InteractionsTreeContentProvider(AS, xArchRef);
		this.labelProvider = new InteractionsTreeLabelProvider(AS);
		this.doubleClickHandler = new InteractionsDoubleClickHandler(AS);
		this.flatListener = new InteractionsXArchEventHandler(AS);
		this.fileManagerListener = new InteractionsFileManagerListener(AS, xArchRef);

		this.contextMenuFillers = new IArchipelagoTreeContextMenuFiller[]{
			new InteractionsNewInteractionContextMenuFiller(viewer, AS, xArchRef),
			new InteractionsEditDescriptionContextMenuFiller(viewer, AS, xArchRef),
			new InteractionsRemoveContextMenuFiller(viewer, AS, xArchRef)
		};
		this.cellModifiers = new ICellModifier[]{
			new InteractionsEditDescriptionCellModifier(AS, xArchRef)
		};	

		initDefaultPreferences(viewer, AS.prefs);
	}
	
	protected void initDefaultPreferences(TreeViewer viewer, IPreferenceStore prefs){
	}

}
