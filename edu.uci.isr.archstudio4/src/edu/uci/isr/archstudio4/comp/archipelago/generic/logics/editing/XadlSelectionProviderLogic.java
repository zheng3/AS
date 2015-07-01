package edu.uci.isr.archstudio4.comp.archipelago.generic.logics.editing;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoUtils;
import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAModelEvent;
import edu.uci.isr.bna4.IBNASynchronousModelListener;
import edu.uci.isr.bna4.assemblies.AssemblyUtils;
import edu.uci.isr.bna4.assemblies.IAssembly;
import edu.uci.isr.bna4.logics.tracking.ISelectionTrackingListener;
import edu.uci.isr.bna4.logics.tracking.SelectionTrackingLogic;
import edu.uci.isr.sysutils.HashBag;
import edu.uci.isr.sysutils.ListenerList;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public class XadlSelectionProviderLogic
	extends AbstractThingLogic
	implements ISelectionProvider, IPostSelectionProvider, ISelectionChangedListener, ISelectionTrackingListener, IBNASynchronousModelListener{

	protected final SelectionTrackingLogic stl;
	XArchFlatInterface xarch;

	public XadlSelectionProviderLogic(SelectionTrackingLogic stl, XArchFlatInterface xarch){
		this.stl = stl;
		this.xarch = xarch;
	}

	@Override
	public void init(){
		super.init();
		stl.addSelectionTrackingListener(this);
	}

	@Override
	public void destroy(){
		stl.removeSelectionTrackingListener(this);
		super.destroy();
	}

	Collection<ObjRef> selection = Collections.synchronizedCollection(new HashBag<ObjRef>());

	public ISelection getSelection(){
		return new StructuredSelection(selection.toArray(new ObjRef[selection.size()]));
	}

	ListenerList<ISelectionChangedListener> selectionChangedListeners = new ListenerList<ISelectionChangedListener>(ISelectionChangedListener.class);

	public void addSelectionChangedListener(ISelectionChangedListener listener){
		selectionChangedListeners.add(listener);
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener){
		selectionChangedListeners.remove(listener);
	}

	protected void fireSelectionChangedEvent(){
		SelectionChangedEvent evt = new SelectionChangedEvent(this, getSelection());
		for(ISelectionChangedListener l: selectionChangedListeners.getListeners()){
			l.selectionChanged(evt);
		}
	}

	ListenerList<ISelectionChangedListener> postSelectionChangedListeners = new ListenerList<ISelectionChangedListener>(ISelectionChangedListener.class);

	public void addPostSelectionChangedListener(ISelectionChangedListener listener){
		postSelectionChangedListeners.add(listener);
	}

	public void removePostSelectionChangedListener(ISelectionChangedListener listener){
		postSelectionChangedListeners.remove(listener);
	}

	protected void firePostSelectionChangedEvent(){
		SelectionChangedEvent evt = new SelectionChangedEvent(this, getSelection());
		postSelectionChangeEventNeeded = false;
		for(ISelectionChangedListener l: postSelectionChangedListeners.getListeners()){
			l.selectionChanged(evt);
		}
	}

	public void setSelection(ISelection selection){
	}

	public void selectionChanged(SelectionChangedEvent event){
	}

	int inBulkChange = 0;
	boolean postSelectionChangeEventNeeded = false;

	public void selectionChanged(edu.uci.isr.bna4.logics.tracking.SelectionChangedEvent evt){
		IAssembly assembly = AssemblyUtils.getAssemblyWithPart(evt.getTargetThing());
		if(assembly != null){
			String id = assembly.getRootThing().getProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME);
			if(id != null){
				ObjRef objRef = xarch.getByID(id);
				if(objRef != null){
					switch(evt.getEventType()){
					case THING_SELECTED:
						if(selection.add(objRef)){
							fireSelectionChangedEvent();
							if(inBulkChange == 0){
								firePostSelectionChangedEvent();
							}
							else{
								postSelectionChangeEventNeeded = true;
							}
						}
						break;
					case THING_DESELECTED:
						if(selection.remove(objRef)){
							fireSelectionChangedEvent();
							if(inBulkChange == 0){
								firePostSelectionChangedEvent();
							}
							else{
								postSelectionChangeEventNeeded = true;
							}
						}
						break;
					}
				}
			}
		}
	}

	public void bnaModelChangedSync(BNAModelEvent evt){
		switch(evt.getEventType()){
		case BULK_CHANGE_BEGIN:
			inBulkChange++;
			break;

		case BULK_CHANGE_END:
			if(--inBulkChange <= 0){
				inBulkChange = 0;
				if(postSelectionChangeEventNeeded){
					firePostSelectionChangedEvent();
				}
			}
		}
	}
}
