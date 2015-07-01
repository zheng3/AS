package edu.uci.isr.archstudio4.comp.archipelago.interactions.things;

import edu.uci.isr.archstudio4.comp.resources.IResources;
import edu.uci.isr.bna4.facets.IRelativeMovable;
import edu.uci.isr.bna4.things.swt.AbstractSWTTreeThing;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public class SWTOperationSelectorThing extends AbstractSWTTreeThing implements
		IRelativeMovable {

	public SWTOperationSelectorThing() {
		this(null);
	}

	public SWTOperationSelectorThing(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	protected void initProperties(){
		super.initProperties();
		setContentProviderFlags(0);
	}

	public void setTask(String task){
		setProperty("task", task);
	}

	public String getTask(){
		return getProperty("task");
	}

	public void setPrjName(String prjName){
		setProperty("prjName", prjName);
	}

	public String getPrjName(){
		return getProperty("prjName");
	}

	public void setRepository(XArchFlatInterface xarch){
		setProperty("$xarch", xarch);
	}

	public XArchFlatInterface getRepository(){
		return getProperty("$xarch");
	}

	public void setResources(IResources resources){
		setProperty("$resources", resources);
	}

	public IResources getResources(){
		return getProperty("$resources");
	}

	public void setContentProviderRootRef(ObjRef rootRef){
		setProperty("rootRef", rootRef);
	}

	public ObjRef getContentProviderRootRef(){
		return getProperty("rootRef");
	}

	public void setContentProviderFlags(int contentProviderFlags){
		setProperty("contentProviderFlags", contentProviderFlags);
	}

	public int getContentProviderFlags(){
		return getProperty("contentProviderFlags");
	}
}
