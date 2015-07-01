package edu.uci.isr.bna4.things.glass;

import edu.uci.isr.bna4.facets.IHasMutableRotatingOffset;
import edu.uci.isr.bna4.facets.IHasMutableSelected;
import edu.uci.isr.bna4.things.essence.PolygonEssenceThing;

public class PolygonGlassThing
	extends PolygonEssenceThing
	implements IHasMutableSelected, IHasMutableRotatingOffset{

	public PolygonGlassThing(){
		this(null, null);
	}

	public PolygonGlassThing(IPolygonGenerator polygonGenerator){
		this(null, polygonGenerator);
	}

	public PolygonGlassThing(String id, IPolygonGenerator polygonGenerator){
		super(id, polygonGenerator);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setSelected(false);
		setProperty(ROTATING_OFFSET_PROPERTY_NAME, 0);
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
		setProperty(ROTATING_OFFSET_PROPERTY_NAME, (getRotatingOffset() + 1) % 6);
	}
}
