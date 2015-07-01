package edu.uci.isr.bna4.things.shapes;

import org.eclipse.swt.graphics.RGB;

import edu.uci.isr.bna4.facets.IHasMutableColor;
import edu.uci.isr.bna4.facets.IHasMutableGradientFill;
import edu.uci.isr.bna4.facets.IHasMutableSecondaryColor;
import edu.uci.isr.bna4.things.essence.EllipseEssenceThing;

public class EllipseThing
	extends EllipseEssenceThing
	implements IHasMutableColor, IHasMutableSecondaryColor,
	IHasMutableGradientFill{

	public EllipseThing(){
		this(null);
	}

	public EllipseThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
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
