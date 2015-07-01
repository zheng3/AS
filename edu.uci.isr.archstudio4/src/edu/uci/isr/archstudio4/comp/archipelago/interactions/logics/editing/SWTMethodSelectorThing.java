package edu.uci.isr.archstudio4.comp.archipelago.interactions.logics.editing;

import edu.uci.isr.archstudio4.comp.resources.IResources;
import edu.uci.isr.bna4.facets.IRelativeMovable;
import edu.uci.isr.bna4.things.swt.AbstractSWTTreeThing;

public class SWTMethodSelectorThing extends AbstractSWTTreeThing implements
		IRelativeMovable {
	final static String METHOD_LIST_PROPERTY_NAME = "methodList";	

	public SWTMethodSelectorThing() {
		this(null);
	}

	public SWTMethodSelectorThing(String id) {
		super(id);
	}

	public void setMethodList(MethodList methodList) {
		setProperty(SWTMethodSelectorThing.METHOD_LIST_PROPERTY_NAME, methodList);
	}

	public MethodList getMethodList() {
		return getProperty(SWTMethodSelectorThing.METHOD_LIST_PROPERTY_NAME);
	}
	
	public void setResources(IResources resources) {
		setProperty("$resources", resources);
	}

	public IResources getResources() {
		return getProperty("$resources");
	}

}
