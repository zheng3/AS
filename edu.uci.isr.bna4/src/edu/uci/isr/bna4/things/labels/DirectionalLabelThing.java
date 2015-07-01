package edu.uci.isr.bna4.things.labels;

import org.eclipse.swt.graphics.RGB;

import edu.uci.isr.bna4.facets.IHasMutableBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutableColor;
import edu.uci.isr.bna4.facets.IHasMutableFlow;
import edu.uci.isr.bna4.facets.IHasMutableOrientation;
import edu.uci.isr.bna4.things.essence.RectangleEssenceThing;
import edu.uci.isr.widgets.swt.constants.Flow;
import edu.uci.isr.widgets.swt.constants.Orientation;

public class DirectionalLabelThing
	extends RectangleEssenceThing
	implements IHasMutableBoundingBox, IHasMutableColor,
	IHasMutableOrientation, IHasMutableFlow{

	public DirectionalLabelThing(){
		this(null);
	}

	public DirectionalLabelThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setOrientation(Orientation.NONE);
		setFlow(Flow.NONE);
	}

	public void setColor(RGB c){
		setProperty(COLOR_PROPERTY_NAME, c);
	}

	public RGB getColor(){
		return getProperty(COLOR_PROPERTY_NAME);
	}

	public Orientation getOrientation(){
		return getProperty(ORIENTATION_PROPERTY_NAME);
	}

	public void setOrientation(Orientation o){
		setProperty(ORIENTATION_PROPERTY_NAME, o);
	}

	public Flow getFlow(){
		return getProperty(FLOW_PROPERTY_NAME);
	}

	public void setFlow(Flow f){
		setProperty(FLOW_PROPERTY_NAME, f);
	}
}
