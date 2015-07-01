package edu.uci.isr.archstudio4.comp.archipelago.prefs;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoConstants;
import edu.uci.isr.archstudio4.util.EclipseUtils;
import edu.uci.isr.bna4.constants.GridDisplayType;
import edu.uci.isr.myx.fw.MyxRegistry;

public class ArchipelagoPreferencePanel extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{
	private ArchipelagoPrefsMyxComponent comp = null;
	private MyxRegistry er = MyxRegistry.getSharedInstance();
	
	protected BooleanFieldEditor antialiasGraphicsEditor;
	protected BooleanFieldEditor antialiasTextEditor;
	protected BooleanFieldEditor decorativeGraphicsEditor;
	
	protected IntegerFieldEditor gridSpacingEditor;
	protected RadioGroupFieldEditor gridDisplayTypeEditor;
	
	public ArchipelagoPreferencePanel(){
		super("Archipelago General Preferences", GRID);
		comp = (ArchipelagoPrefsMyxComponent)er.waitForBrick(ArchipelagoPrefsMyxComponent.class);
		er.map(comp, this);
		
		setPreferenceStore(comp.prefs);
		setDescription("This panel lets you set general preferences for Archipelago; feature-specific preferences are in subpanels.");
	}
	
	public void init(IWorkbench workbench){
	}

	protected void createFieldEditors(){
		antialiasGraphicsEditor = new BooleanFieldEditor(ArchipelagoConstants.PREF_ANTIALIAS_GRAPHICS, "Antialias Graphics", getFieldEditorParent());
		addField(antialiasGraphicsEditor);

		antialiasTextEditor = new BooleanFieldEditor(ArchipelagoConstants.PREF_ANTIALIAS_TEXT, "Antialias Text", getFieldEditorParent());
		addField(antialiasTextEditor);
		
		decorativeGraphicsEditor = new BooleanFieldEditor(ArchipelagoConstants.PREF_DECORATIVE_GRAPHICS, "Decorative Graphics", getFieldEditorParent());
		addField(decorativeGraphicsEditor);
		
		gridSpacingEditor = new IntegerFieldEditor(ArchipelagoConstants.PREF_GRID_SPACING, "Grid Spacing", getFieldEditorParent());
		gridSpacingEditor.setEmptyStringAllowed(false);
		gridSpacingEditor.setValidRange(0, 255);
		addField(gridSpacingEditor);
		
		gridDisplayTypeEditor = new RadioGroupFieldEditor(ArchipelagoConstants.PREF_GRID_DISPLAY_TYPE, "Grid Display", 1,
			EclipseUtils.getFieldEditorPreferenceData(GridDisplayType.class), 
			getFieldEditorParent(), true);	
		addField(gridDisplayTypeEditor);

	}

}
