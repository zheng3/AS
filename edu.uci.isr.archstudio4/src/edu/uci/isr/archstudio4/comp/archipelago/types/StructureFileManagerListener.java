package edu.uci.isr.archstudio4.comp.archipelago.types;

import org.eclipse.core.runtime.IProgressMonitor;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.fileman.IFileManagerListener;
import edu.uci.isr.xarchflat.ObjRef;

public class StructureFileManagerListener
    implements IFileManagerListener{

	protected ArchipelagoServices AS = null;
	protected ObjRef xArchRef = null;

	public StructureFileManagerListener(ArchipelagoServices services, ObjRef xArchRef){
		this.AS = services;
		this.xArchRef = xArchRef;
	}

	public void fileDirtyStateChanged(ObjRef xArchRef, boolean dirty){
	}

	public void fileSaving(final ObjRef xArchRef, final IProgressMonitor monitor){
		if(xArchRef.equals(this.xArchRef)){
			monitor.subTask("Writing Hints");
			//StructureEditorSupport.writeHints(AS, xArchRef, monitor);
		}
	}
}
