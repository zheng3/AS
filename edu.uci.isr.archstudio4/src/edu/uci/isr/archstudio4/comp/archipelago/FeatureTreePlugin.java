package edu.uci.isr.archstudio4.comp.archipelago;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TreeViewer;

import edu.uci.isr.archstudio4.comp.archipelago.features.FeatureAddColorContextMenuFiller;
import edu.uci.isr.archstudio4.comp.archipelago.features.FeatureAddVarientContextMenuFiller;
import edu.uci.isr.archstudio4.comp.archipelago.features.FeatureDoubleClickHandler;
import edu.uci.isr.archstudio4.comp.archipelago.features.FeatureNewFeatureContextMenuFiller;
import edu.uci.isr.archstudio4.comp.archipelago.features.FeatureTreeContentProvider;
import edu.uci.isr.archstudio4.comp.archipelago.features.FeaturesEditDescriptionCellModifier;
import edu.uci.isr.archstudio4.comp.archipelago.features.FeaturesEditDescriptionContextMenuFiller;
import edu.uci.isr.archstudio4.comp.archipelago.features.FeaturesRemoveContextMenuFiller;
import edu.uci.isr.archstudio4.comp.archipelago.statecharts.StatechartsEditDescriptionCellModifier;
import edu.uci.isr.archstudio4.comp.archipelago.types.StructureDoubleClickHandler;
import edu.uci.isr.archstudio4.comp.archipelago.types.StructureEditDescriptionContextMenuFiller;
import edu.uci.isr.archstudio4.comp.archipelago.types.StructureNewStructureContextMenuFiller;
import edu.uci.isr.archstudio4.comp.archipelago.types.StructureRemoveContextMenuFiller;
import edu.uci.isr.bna4.constants.GridDisplayType;
import edu.uci.isr.xarchflat.ObjRef;

public class FeatureTreePlugin extends AbstractArchipelagoTreePlugin{
	public FeatureTreePlugin(TreeViewer viewer, ArchipelagoServices AS, ObjRef xArchRef){
		this.contentProvider = new FeatureTreeContentProvider(AS, xArchRef);
		this.labelProvider = new FeatureLabelProvider(AS);
		this.doubleClickHandler = new FeatureDoubleClickHandler(AS,xArchRef);
		
		this.contextMenuFillers = new IArchipelagoTreeContextMenuFiller[]{
				new FeatureNewFeatureContextMenuFiller(viewer, AS, xArchRef),
				new FeaturesEditDescriptionContextMenuFiller(viewer, AS, xArchRef),
				new FeaturesRemoveContextMenuFiller(viewer, AS, xArchRef),
				new FeatureAddVarientContextMenuFiller(viewer,AS,xArchRef),
				new FeatureAddColorContextMenuFiller(viewer,AS,xArchRef)//"MarkAsDefault" is also implemented in this
			};
		this.cellModifiers = new ICellModifier[]{
				new FeaturesEditDescriptionCellModifier(AS, xArchRef)
			};
		
		final TreeViewer fviewer = viewer;
		this.editorFocuser = new IArchipelagoEditorFocuser(){
			public void focusEditor(String editorName, ObjRef[] refs){
				fviewer.setSelection(null);
			}
		};
		initDefaultPreferences(AS.prefs);
	}
	
	protected void initDefaultPreferences(IPreferenceStore prefs){
		prefs.setDefault(ArchipelagoConstants.PREF_ANTIALIAS_GRAPHICS, false);
		prefs.setDefault(ArchipelagoConstants.PREF_ANTIALIAS_TEXT, false);
		prefs.setDefault(ArchipelagoConstants.PREF_DECORATIVE_GRAPHICS, false);
		prefs.setDefault(ArchipelagoConstants.PREF_GRID_SPACING, 25);
		prefs.setDefault(ArchipelagoConstants.PREF_GRID_DISPLAY_TYPE, GridDisplayType.NONE.name());
	}

}
