package edu.uci.isr.bna4.things.layouts;

import org.eclipse.swt.widgets.Layout;

import edu.uci.isr.bna4.facets.IHasSWTLayout;
import edu.uci.isr.bna4.things.essence.RectangleEssenceThing;

public class LayoutRootThing
	extends RectangleEssenceThing
	implements IHasSWTLayout{

	public LayoutRootThing(){
		this(null);
	}

	public LayoutRootThing(String id){
		super(id);
	}

	public Layout getSWTLayout(){
		return getProperty(IHasSWTLayout.SWT_LAYOUT_PROPERTY_NAME);
	}

	public void setSWTLayout(Layout layout){
		setProperty(IHasSWTLayout.SWT_LAYOUT_PROPERTY_NAME, layout);
		// TODO Refactor below layout_target_id assignment outside of this method ???
		// setProperty(IHasSWTLayoutData.LAYOUT_TARGET_ID_PROPERTY_NAME, classBox.getBoxGlassThing().getID());
	}
}
