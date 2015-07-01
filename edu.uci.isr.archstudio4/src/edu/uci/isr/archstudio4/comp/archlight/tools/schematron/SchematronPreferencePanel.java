package edu.uci.isr.archstudio4.comp.archlight.tools.schematron;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import edu.uci.isr.myx.fw.MyxRegistry;

public class SchematronPreferencePanel extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{
	private SchematronPrefsMyxComponent comp = null;
	private MyxRegistry er = MyxRegistry.getSharedInstance();

	protected PathEditor testFilePathsPrefEditor;
	
	public SchematronPreferencePanel(){
		super("Schematron Preferences", GRID);
		comp = (SchematronPrefsMyxComponent)er.waitForBrick(SchematronPrefsMyxComponent.class);
		er.map(comp, this);
		
		setPreferenceStore(comp.prefs);
		setDescription("Additional test file paths:");
	}
	
	public void init(IWorkbench workbench){
	}

	protected void createFieldEditors(){
		testFilePathsPrefEditor = new PathEditor(SchematronConstants.PREF_TEST_FILE_PATHS,
			"Test File Paths", "Choose Test File Path", getFieldEditorParent());
		addField(testFilePathsPrefEditor);
	}

}
