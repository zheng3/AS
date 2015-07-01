package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;

public interface IHasSWTLayoutData extends IThing {

	public static final String SWT_LAYOUT_DATA_PROPERTY_NAME = "swtLayoutData";
	public static final String LAYOUT_TARGET_ID_PROPERTY_NAME = "&targetLayoutThing";

	public Object getSWTLayoutData();

	public void setSWTLayoutData(Object layoutData);
	
	public String getTargetLayoutThingID();
	
	public void setTargetLayoutThingID(String id);
}
