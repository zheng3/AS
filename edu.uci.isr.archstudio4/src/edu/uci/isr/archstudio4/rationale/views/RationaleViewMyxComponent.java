package edu.uci.isr.archstudio4.rationale.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

import edu.uci.isr.archstudio4.comp.xarchcs.explicitadt.ExplicitADTEvent;
import edu.uci.isr.archstudio4.comp.xarchcs.explicitadt.ExplicitADTListener;
import edu.uci.isr.archstudio4.comp.xarchcs.explicitadt.IExplicitADT;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetEvent;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetInterface;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetListener;
import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxRegistry;
import edu.uci.isr.myx.fw.MyxUtils;
import edu.uci.isr.xarchflat.XArchFileEvent;
import edu.uci.isr.xarchflat.XArchFileListener;
import edu.uci.isr.xarchflat.XArchFlatEvent;
import edu.uci.isr.xarchflat.XArchFlatListener;

public class RationaleViewMyxComponent extends AbstractMyxSimpleBrick implements XArchFileListener, XArchFlatListener, XArchChangeSetListener, ExplicitADTListener{

	public static final IMyxName INTERFACE_NAME_OUT_XARCHCS = MyxUtils.createName("xarchcs");

	public static final IMyxName INTERFACE_NAME_OUT_EXPLICITADT = MyxUtils.createName("explicitadt");

	XArchChangeSetInterface xArchCS = null;

	IExplicitADT explicit = null;

	MyxRegistry myxr = MyxRegistry.getSharedInstance();

	public RationaleViewMyxComponent() {
		super();
	}
	
	@Override
	public void begin(){
		xArchCS = (XArchChangeSetInterface)MyxUtils.getFirstRequiredServiceObject(this, INTERFACE_NAME_OUT_XARCHCS);
		explicit = (IExplicitADT)MyxUtils.getFirstRequiredServiceObject(this, INTERFACE_NAME_OUT_EXPLICITADT);
		myxr.register(this);
	}

	public Object getServiceObject(IMyxName interfaceName){
		if(interfaceName.equals(INTERFACE_NAME_OUT_EXPLICITADT)){
			return this;
		}
		else if(interfaceName.equals(INTERFACE_NAME_OUT_XARCHCS)){
			return this;
		}
		else {
			return null;
		}
		
	}

	public void handleXArchFileEvent(XArchFileEvent evt){
		for(Object o: myxr.getObjects(this).clone()){
			if(o instanceof XArchFileListener){
				((XArchFileListener)o).handleXArchFileEvent(evt);
			}
		}
	}

	public void handleXArchFlatEvent(XArchFlatEvent evt){
		for(Object o: myxr.getObjects(this).clone()){
			if(o instanceof XArchFlatListener){
				((XArchFlatListener)o).handleXArchFlatEvent(evt);
			}
		}
	}

	public void handleXArchChangeSetEvent(XArchChangeSetEvent evt){
		for(Object o: myxr.getObjects(this).clone()){
			if(o instanceof XArchChangeSetListener){
				((XArchChangeSetListener)o).handleXArchChangeSetEvent(evt);
			}
		}
	}

	public void handleExplicitEvent(ExplicitADTEvent evt){
		for(Object o: myxr.getObjects(this).clone()){
			if(o instanceof ExplicitADTListener){
				((ExplicitADTListener)o).handleExplicitEvent(evt);
			}
		}
	}
}
