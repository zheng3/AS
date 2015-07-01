package edu.uci.isr.archstudio4.comp.xarchcs.actions;

import org.eclipse.jface.action.Action;

import edu.uci.isr.archstudio4.comp.xarchcs.XArchCSActivator;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetInterface;
import edu.uci.isr.sysutils.UIDGenerator;
import edu.uci.isr.xarchflat.ObjRef;

public class AddOrRelationshipAction extends Action
	implements IHasXArchRef{

	XArchChangeSetInterface xarch;

	ObjRef xArchRef = null;

	public AddOrRelationshipAction(XArchChangeSetInterface xarch){
		super("Add Or Relationship", XArchCSActivator.getDefault().getImageRegistry().getDescriptor("res/icons/add_or_relationship_action.gif"));
		this.xarch = xarch;
		setToolTipText(getText());
		setXArchRef(null);
	}

	@Override
	public void run(){
		if(xArchRef != null){
			ObjRef changesetsContextRef = xarch.createContext(xArchRef, "changesets");
			ObjRef archChangeSetsRef = xarch.getElement(changesetsContextRef, "archChangeSets", xArchRef);
			if(archChangeSetsRef != null){
				ObjRef relationshipRef = xarch.create(changesetsContextRef, "OrRelationship");
				xarch.set(relationshipRef, "id", UIDGenerator.generateUID("OrRelationship"));

				xarch.add(archChangeSetsRef, "relationship", relationshipRef);
			}
		}
	}

	public ObjRef getXArchRef(){
		return xArchRef;
	}

	public void setXArchRef(ObjRef xArchRef){
		this.xArchRef = xArchRef;
		setEnabled(xArchRef != null);
	}
}