package edu.uci.isr.archstudio4.comp.xarchcs.views.changesets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Item;

import edu.uci.isr.archstudio4.comp.xarchcs.explicitadt.IExplicitADT;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetInterface;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetUtils;
import edu.uci.isr.xarchflat.NoSuchObjectException;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatUtils;

public class ChangeSetCellModifier
    implements ICellModifier{

	TreeViewer viewer;

	XArchChangeSetInterface xarch;

	IExplicitADT explicit;

	public ChangeSetCellModifier(TreeViewer viewer, XArchChangeSetInterface xarch, IExplicitADT explicit){
		this.viewer = viewer;
		this.xarch = xarch;
		this.explicit = explicit;
	}

	public boolean canModify(Object element, String property){
		if("Apply".equals(property)){
			return true;
		}
		if("View".equals(property)){
			return true;
		}
		if("Change Set".equals(property)){
			return true;
		}
		return false;
	}

	public Object getValue(Object element, String property){
		try{
			if("Apply".equals(property)){
				return Arrays.asList(xarch.getAppliedChangeSetRefs(xarch.getXArch((ObjRef)element))).contains(element);
			}
			if("View".equals(property)){
				return Arrays.asList(explicit.getExplicitChangeSetRefs(xarch.getXArch((ObjRef)element))).contains(element);
			}
			if("Change Set".equals(property)){
				return XArchFlatUtils.getDescriptionValue(xarch, (ObjRef)element, "");
			}

		}
		catch(NoSuchObjectException e){
		}
		return null;
	}

	public void modify(Object element, String property, Object value){
		if(element instanceof Item){
			element = ((Item)element).getData();
		}

		try{
			if("Apply".equals(property)){
				final ObjRef xArchRef = xarch.getXArch((ObjRef)element);
				Set<ObjRef> appliedChangeSets = new HashSet<ObjRef>(Arrays.asList(xarch.getAppliedChangeSetRefs(xArchRef)));
				if(((Boolean)value).booleanValue()){
					appliedChangeSets.add((ObjRef)element);
				}
				else{
					appliedChangeSets.remove(element);
				}

				List<ObjRef> orderedAppliedChangeSets = XArchChangeSetUtils.getOrderedChangeSets(xarch, xArchRef);
				orderedAppliedChangeSets.retainAll(appliedChangeSets);

				xarch.setAppliedChangeSetRefs(xArchRef, orderedAppliedChangeSets.toArray(new ObjRef[orderedAppliedChangeSets.size()]), null);
			}
			if("View".equals(property)){
				final ObjRef xArchRef = xarch.getXArch((ObjRef)element);
				Set<ObjRef> explicitChangeSets = new HashSet<ObjRef>(Arrays.asList(explicit.getExplicitChangeSetRefs(xArchRef)));
				if(((Boolean)value).booleanValue()){
					explicitChangeSets.add((ObjRef)element);
				}
				else{
					explicitChangeSets.remove(element);
				}

				List<ObjRef> orderedExplicitChangeSets = XArchChangeSetUtils.getOrderedChangeSets(xarch, xArchRef);
				orderedExplicitChangeSets.retainAll(explicitChangeSets);

				explicit.setExplicitChangeSetRefs(xArchRef, orderedExplicitChangeSets.toArray(new ObjRef[orderedExplicitChangeSets.size()]));
			}
			if("Change Set".equals(property)){
				XArchFlatUtils.setDescription(xarch, (ObjRef)element, "description", (String)value);
			}
		}
		catch(NoSuchObjectException e){
		}
	}
}
