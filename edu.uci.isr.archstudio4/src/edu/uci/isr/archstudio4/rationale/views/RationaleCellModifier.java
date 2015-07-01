package edu.uci.isr.archstudio4.rationale.views;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Item;

import edu.uci.isr.archstudio4.rationale.RationaleViewManager;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public class RationaleCellModifier implements ICellModifier{

	RationaleViewManager rationaleViewManager;

	TableViewer rationaleTableViewer;

	XArchFlatInterface xArch;

	private static boolean equalz(Object o1, Object o2) {
		return o1 == null ? o2 == null : o1.equals(o2);
	}	

	public RationaleCellModifier(TableViewer rationaleTableViewer,RationaleViewManager rationaleViewManager,XArchFlatInterface xArch) {
		this.rationaleViewManager = rationaleViewManager;
		this.xArch = xArch;
		this.rationaleTableViewer = rationaleTableViewer;
	}


	public boolean canModify(Object element, String property) {
		if(element instanceof ObjRef) {
			if(xArch.isInstanceOf((ObjRef)element, "changesets#RelationshipRationale")) {
				return false;
			}
			else {
				return true;
			}
		}
		else {
			return false;
		}
	}

	public Object getValue(Object element, String property) {
		int columnIndex = indexOf(rationaleTableViewer.getColumnProperties(), property);

		switch(columnIndex) {
		case 0:
			return null;
		case 1:
			if(element instanceof ObjRef) {
				ObjRef objRef = (ObjRef)element;
				ObjRef descriptionRef = (ObjRef)xArch.get(objRef,"Description");
				return (String)xArch.get(descriptionRef,"value");
			}
			break;
		}
		return null;
	}

	public void modify(Object element, String property, Object value) {
		int columnIndex = indexOf(rationaleTableViewer.getColumnProperties(), property);

		if (element instanceof Item)
			element = ((Item) element).getData();
		switch (columnIndex) {
		case 0:
			return;
		case 1:
			if (element instanceof ObjRef) {
				ObjRef rationaleRef = (ObjRef) element;
				ObjRef descriptionRef = (ObjRef)xArch.get(rationaleRef,"Description");
				if(value == null) {
					value = new String("");
				}
				xArch.set(descriptionRef, "Value",(String)value);
				rationaleTableViewer.refresh(rationaleRef);
			}
			break;
		}
	}

	public int indexOf(Object[] values, Object value) {
		for (int i = 0; i < values.length; i++) {
			if (equalz(values[i], value))
				return i;
		}
		return -1;
	}
}
