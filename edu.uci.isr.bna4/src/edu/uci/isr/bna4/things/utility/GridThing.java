package edu.uci.isr.bna4.things.utility;

import org.eclipse.swt.graphics.RGB;

import edu.uci.isr.bna4.AbstractThing;
import edu.uci.isr.bna4.constants.GridDisplayType;
import edu.uci.isr.bna4.facets.IHasMutableColor;

public class GridThing
	extends AbstractThing
	implements IHasMutableColor{

	public static final String GRID_SPACING_PROPERTY_NAME = "gridSpacing";
	public static final String GRID_DISPLAY_TYPE_PROPERTY_NAME = "gridDisplayType";

	public GridThing(){
		this(null);
	}

	public GridThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setGridSpacing(0);
		setGridDisplayType(GridDisplayType.NONE);
	}

	public void setGridSpacing(int gridSpacing){
		setProperty(GRID_SPACING_PROPERTY_NAME, gridSpacing);
	}

	public int getGridSpacing(){
		return getProperty(GRID_SPACING_PROPERTY_NAME);
	}

	public void setGridDisplayType(GridDisplayType gridDisplayType){
		setProperty(GRID_DISPLAY_TYPE_PROPERTY_NAME, gridDisplayType);
	}

	public GridDisplayType getGridDisplayType(){
		return getProperty(GRID_DISPLAY_TYPE_PROPERTY_NAME);
	}

	public void setColor(RGB c){
		setProperty(COLOR_PROPERTY_NAME, c);
	}

	public RGB getColor(){
		return getProperty(COLOR_PROPERTY_NAME);
	}

}
