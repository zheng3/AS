package edu.uci.isr.bna4.things.shapes;

import org.eclipse.swt.graphics.RGB;

import edu.uci.isr.bna4.facets.IHasMutableColor;
import edu.uci.isr.bna4.facets.IHasMutableGradientFill;
import edu.uci.isr.bna4.facets.IHasMutableSecondaryColor;
import edu.uci.isr.bna4.things.essence.RoundedRectangleEssenceThing;

public class BoxThing
	extends RoundedRectangleEssenceThing
	implements IHasMutableColor, IHasMutableSecondaryColor,
	IHasMutableGradientFill{
	
	

	public BoxThing(){
		this(null);
	}

	public BoxThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setColor(new RGB(192, 192, 192));
		setSecondaryColor(new RGB(128, 128, 128));
		setGradientFilled(false);
	}

	public void setColor(RGB c){
		setProperty(COLOR_PROPERTY_NAME, c);
	}

	public RGB getColor(){
		return getProperty(COLOR_PROPERTY_NAME);
	}

	public void setSecondaryColor(RGB c){
		setProperty(SECONDARY_COLOR_PROPERTY_NAME, c);
	}

	public RGB getSecondaryColor(){
		return getProperty(SECONDARY_COLOR_PROPERTY_NAME);
	}

	public boolean isGradientFilled(){
		return getProperty(GRADIENT_FILLED_PROPERTY_NAME);
	}

	public void setGradientFilled(boolean newHasGradientFill){
		setProperty(GRADIENT_FILLED_PROPERTY_NAME, newHasGradientFill);
	}
}
