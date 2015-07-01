package edu.uci.isr.bna4.things.glass;

import edu.uci.isr.bna4.facets.IHasMutableRotatingOffset;
import edu.uci.isr.bna4.facets.IHasMutableSelected;
import edu.uci.isr.bna4.things.essence.EllipseEssenceThing;

public class EllipseGlassThing
	extends EllipseEssenceThing
	implements IHasMutableSelected, IHasMutableRotatingOffset{

	public EllipseGlassThing(){
		this(null);
	}

	public EllipseGlassThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setProperty(ROTATING_OFFSET_PROPERTY_NAME, 0);
		setSelected(false);
	}

	public void setSelected(boolean selected){
		setProperty(SELECTED_PROPERTY_NAME, selected);
	}

	public boolean isSelected(){
		return getProperty(SELECTED_PROPERTY_NAME);
	}

	public int getRotatingOffset(){
		return getProperty(ROTATING_OFFSET_PROPERTY_NAME);
	}

	public void incrementRotatingOffset(){
		if(isSelected()){
			setProperty(ROTATING_OFFSET_PROPERTY_NAME, (getRotatingOffset() + 1) % 6);
		}
	}
}