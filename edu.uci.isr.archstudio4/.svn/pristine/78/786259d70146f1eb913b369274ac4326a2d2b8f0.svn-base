package edu.uci.isr.archstudio4.comp.archipelago.interactions.things;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

import edu.uci.isr.archstudio4.comp.archipelago.things.SWTXadlSelectorThing;
import edu.uci.isr.archstudio4.comp.resources.IResources;
import edu.uci.isr.archstudio4.util.XadlTreeContentProvider;
import edu.uci.isr.archstudio4.util.XadlTreeLabelProvider;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.things.swt.AbstractSWTTreeThingPeer;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public class SWTOperationSelectorThingPeer extends AbstractSWTTreeThingPeer {

	protected SWTOperationSelectorThing lt;

	public SWTOperationSelectorThingPeer(IThing t) {
		super(t);
		if(!(t instanceof SWTOperationSelectorThing)){
			throw new IllegalArgumentException("SWTOperationSelectorThingPeer can only peer for SWTOperationSelectorThing");
		}
		this.lt = (SWTOperationSelectorThing)t;
	}

	@Override
	protected Object getInput() {
		return lt.getContentProviderRootRef();
	}

	@Override
	protected ITreeContentProvider getContentProvider() {
		XArchFlatInterface xarch = lt.getRepository();
		if(xarch != null){
			ObjRef rootRef = lt.getContentProviderRootRef();
			if(rootRef != null){
				String prjName = lt.getPrjName();
				int flags = lt.getContentProviderFlags();
				OperationContentProvider contentProvider = new OperationContentProvider(xarch, rootRef, prjName, flags);
				return contentProvider;
			}
		}
		return null;
	}

	@Override
	protected ILabelProvider getLabelProvider() {
		XArchFlatInterface xarch = lt.getRepository();
		if(xarch != null){
			IResources resources = lt.getResources();
			if(resources != null){
				return new OperationLabelProvider(xarch, resources);
			}
		}
		return null;
	}

}
