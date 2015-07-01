package edu.uci.isr.archstudio4.comp.xarchcs.views.changesets;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Image;

import edu.uci.isr.archstudio4.comp.xarchcs.XArchCSActivator;
import edu.uci.isr.archstudio4.comp.xarchcs.explicitadt.ExplicitADTEvent;
import edu.uci.isr.archstudio4.comp.xarchcs.explicitadt.ExplicitADTListener;
import edu.uci.isr.archstudio4.comp.xarchcs.explicitadt.IExplicitADT;
import edu.uci.isr.archstudio4.comp.xarchcs.explicitadt.ExplicitADTEvent.ExplicitEventType;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetEvent;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetInterface;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetListener;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetEvent.ChangeSetEventType;
import edu.uci.isr.widgets.swt.SWTWidgetUtils;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.NoSuchObjectException;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFileEvent;
import edu.uci.isr.xarchflat.XArchFileListener;
import edu.uci.isr.xarchflat.XArchFlatEvent;
import edu.uci.isr.xarchflat.XArchFlatListener;

public class ChangeSetLabelProvider
	extends LabelProvider
	implements ITableLabelProvider, XArchFileListener, XArchFlatListener, XArchChangeSetListener, ExplicitADTListener{

	private static <T>List<T> nullArraysAsList(T... a){
		if(a == null){
			return Collections.emptyList();
		}
		return Arrays.asList(a);
	}

	public static final String APPLY_PROPERTY = "Apply";
	public static final String EXPLICIT_PROPERTY = "View";
	public static final String CHANGE_SET_PROPERTY = "Change Set";

	XArchChangeSetInterface xarch;
	ColumnViewer viewer;
	IExplicitADT explicit;
	Set<String> properties = new HashSet<String>(Arrays.asList(new String[]{APPLY_PROPERTY, EXPLICIT_PROPERTY, CHANGE_SET_PROPERTY}));
	Map<ObjRef, Set<ObjRef>> appliedChangeSetRefs = Collections.synchronizedMap(new HashMap<ObjRef, Set<ObjRef>>());
	Map<ObjRef, Set<ObjRef>> explicitChangeSetRefs = Collections.synchronizedMap(new HashMap<ObjRef, Set<ObjRef>>());

	public ChangeSetLabelProvider(ColumnViewer viewer, XArchChangeSetInterface xarch, IExplicitADT explicit){
		this.viewer = viewer;
		this.xarch = xarch;
		this.explicit = explicit;

		for(ObjRef xArchRef: xarch.getOpenXArches()){
			appliedChangeSetRefs.put(xArchRef, new HashSet<ObjRef>(nullArraysAsList(xarch.getAppliedChangeSetRefs(xArchRef))));
			explicitChangeSetRefs.put(xArchRef, new HashSet<ObjRef>(nullArraysAsList(explicit.getExplicitChangeSetRefs(xArchRef))));
		}
	}

	@Override
	public boolean isLabelProperty(Object element, String property){
		return properties.contains(property);
	}

	public Image getColumnImage(Object element, int columnIndex){
		try{
			Object columnProperty = SWTWidgetUtils.getColumnProperty(viewer, columnIndex);
			ImageRegistry ir = XArchCSActivator.getDefault().getImageRegistry();

			if(APPLY_PROPERTY.equals(columnProperty)){
				ObjRef changeSetRef = (ObjRef)element;
				Set<ObjRef> s = appliedChangeSetRefs.get(xarch.getXArch(changeSetRef));
				if(s != null){
					if(s.contains(changeSetRef)){
						return ir.get("res/icons/applied.gif");
					}
					else{
						return ir.get("res/icons/unapplied.gif");
					}
				}
			}
			else if(EXPLICIT_PROPERTY.equals(columnProperty)){
				ObjRef changeSetRef = (ObjRef)element;
				Set<ObjRef> s = explicitChangeSetRefs.get(xarch.getXArch(changeSetRef));
				if(s != null){
					if(s.contains(changeSetRef)){
						return ir.get("res/icons/explicit.gif");
					}
					else{
						return ir.get("res/icons/implicit.gif");
					}
				}
			}
			else if(CHANGE_SET_PROPERTY.equals(columnProperty)){
				return null;
			}
		}
		catch(NoSuchObjectException e){
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex){
		try{
			Object columnProperty = SWTWidgetUtils.getColumnProperty(viewer, columnIndex);

			if(APPLY_PROPERTY.equals(columnProperty)){
				return null;
			}
			else if(EXPLICIT_PROPERTY.equals(columnProperty)){
				return null;
			}
			else if(CHANGE_SET_PROPERTY.equals(columnProperty)){
				ObjRef changeSetRef = (ObjRef)element;
				String value = XadlUtils.getDescription(xarch, changeSetRef);
				if(value != null){
					return value;
				}
				return changeSetRef.toString();
			}
		}
		catch(NoSuchObjectException e){
		}
		return null;
	}

	public void handleXArchFlatEvent(final XArchFlatEvent evt){
		if(evt.getIsAttached() && !evt.getIsExtra()){
			if(evt.getSourceTargetPath().toTagsOnlyString().startsWith("xArch/archChangeSets/changeSet/description")){
				SWTWidgetUtils.async(viewer, new Runnable(){

					public void run(){
						fireLabelProviderChanged(new LabelProviderChangedEvent(ChangeSetLabelProvider.this, evt.getSourceAncestors()[evt.getSourceAncestors().length - 3]));
					}
				});
			}
		}
	}

	boolean updateApplied = false;

	public void handleXArchFileEvent(XArchFileEvent evt){
		switch(evt.getEventType()){
		case XArchFileEvent.XARCH_CREATED_EVENT:
		case XArchFileEvent.XARCH_OPENED_EVENT:
			ObjRef xArchRef = evt.getXArchRef();
			appliedChangeSetRefs.put(xArchRef, new HashSet<ObjRef>(nullArraysAsList(xarch.getAppliedChangeSetRefs(xArchRef))));
			explicitChangeSetRefs.put(xArchRef, new HashSet<ObjRef>(nullArraysAsList(explicit.getExplicitChangeSetRefs(xArchRef))));
		}
	}

	public void handleXArchChangeSetEvent(XArchChangeSetEvent evt){
		if(evt.getEventType() == ChangeSetEventType.UPDATED_APPLIED_CHANGE_SETS){
			appliedChangeSetRefs.put(evt.getXArchRef(), new HashSet<ObjRef>(nullArraysAsList(evt.getAppliedChangeSets())));
			updateApplied = true;
			SWTWidgetUtils.async(viewer, new Runnable(){

				public void run(){
					if(updateApplied){
						updateApplied = false;
						fireLabelProviderChanged(new LabelProviderChangedEvent(ChangeSetLabelProvider.this));
					}
				}
			});
		}
	}

	boolean updateExplicit = false;

	public void handleExplicitEvent(ExplicitADTEvent evt){
		if(evt.getEventType() == ExplicitEventType.UPDATED_EXPLICIT_CHANGE_SETS){
			explicitChangeSetRefs.put(evt.getXArchRef(), new HashSet<ObjRef>(nullArraysAsList(evt.getChangeSetRefs())));
			updateExplicit = true;
			SWTWidgetUtils.async(viewer, new Runnable(){

				public void run(){
					if(updateExplicit){
						updateExplicit = false;
						fireLabelProviderChanged(new LabelProviderChangedEvent(ChangeSetLabelProvider.this));
					}
				}
			});
		}
	}
}
