package edu.uci.isr.archstudio4.rationale.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import edu.uci.isr.archstudio4.rationale.RationaleViewManager;

public class RationaleContentProvider implements IStructuredContentProvider{

	protected RationaleViewManager rationaleViewManager;
	
	public RationaleContentProvider(RationaleViewManager rationaleViewManager) {
		this.rationaleViewManager = rationaleViewManager;
	}
	
	public Object[] getElements(Object arg0) {
		return rationaleViewManager.getRationales();
	}

	public void dispose() {
		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		viewer.refresh();
	}
}
