package edu.uci.isr.archstudio4.comp.archipelago.interactions.logics.editing;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class MethodContentProvider implements ITreeContentProvider {

	private final MethodList methodList;
	protected static final Object[] EMPTY_ARRAY = new Object[0];

	public MethodContentProvider(MethodList ml) {
		this.methodList = ml;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof MethodList) {
			return ((MethodList)parentElement).getMethodLabels().toArray();
		}
		return EMPTY_ARRAY;
	}

	public Object getParent(Object element) {
		if(element instanceof MethodLabel) {
			return methodList;
		}
		return null;
	}

	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

}
