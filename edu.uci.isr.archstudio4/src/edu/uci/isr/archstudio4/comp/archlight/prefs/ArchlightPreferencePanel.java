package edu.uci.isr.archstudio4.comp.archlight.prefs;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import edu.uci.isr.myx.fw.MyxRegistry;
import edu.uci.isr.archstudio4.comp.archlight.ArchlightConstants;
import edu.uci.isr.archstudio4.comp.archlight.DoubleClickAction;
import edu.uci.isr.archstudio4.util.EclipseUtils;

public class ArchlightPreferencePanel extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{
	private ArchlightPrefsMyxComponent comp = null;
	private MyxRegistry er = MyxRegistry.getSharedInstance();
	
	protected RadioGroupFieldEditor doubleClickActionEditor;
	
	public ArchlightPreferencePanel(){
		super("Archlight General Preferences", GRID);
		comp = (ArchlightPrefsMyxComponent)er.waitForBrick(ArchlightPrefsMyxComponent.class);
		er.map(comp, this);
		
		setPreferenceStore(comp.prefs);
		setDescription("This panel lets you set general preferences for Archlight.");
	}
	
	public void init(IWorkbench workbench){
	}

	protected void createFieldEditors(){
		doubleClickActionEditor = new RadioGroupFieldEditor(ArchlightConstants.PREF_DOUBLE_CLICK_ACTION, "Double Click Action", 1,
			EclipseUtils.getFieldEditorPreferenceData(DoubleClickAction.class), 
			getFieldEditorParent(), true);
		addField(doubleClickActionEditor);

	}

}
