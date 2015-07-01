package edu.uci.isr.bna4.things.borders;

import org.eclipse.swt.graphics.RGB;

import edu.uci.isr.bna4.facets.IHasMutableBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutableColor;
import edu.uci.isr.bna4.facets.IHasMutableLineStyle;
import edu.uci.isr.bna4.facets.IHasMutableLineWidth;
import edu.uci.isr.bna4.things.essence.EllipseEssenceThing;

public class EllipseBorderThing
	extends EllipseEssenceThing
	implements IHasMutableBoundingBox, IHasMutableColor, IHasMutableLineStyle,
	IHasMutableLineWidth{

	public static final String COUNT_PROPERTY_NAME = "count";

	public EllipseBorderThing(){
		this(null);
	}

	public EllipseBorderThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setLineStyle(LINE_STYLE_SOLID);
		setLineWidth(1);
		setCount(1);
	}

	public void setColor(RGB c){
		setProperty(COLOR_PROPERTY_NAME, c);
	}

	public RGB getColor(){
		return getProperty(COLOR_PROPERTY_NAME);
	}

	public int getLineStyle(){
		return getProperty(LINE_STYLE_PROPERTY_NAME);
	}

	public void setLineStyle(int lineStyle){
		setProperty(LINE_STYLE_PROPERTY_NAME, lineStyle);
	}

	public int getLineWidth(){
		return getProperty(LINE_WIDTH_PROPERTY_NAME);
	}

	public void setLineWidth(int lineWidth){
		setProperty(LINE_WIDTH_PROPERTY_NAME, lineWidth);
	}

	public void setCount(int count){
		setProperty(COUNT_PROPERTY_NAME, count);
	}

	public int getCount(){
		return getProperty(COUNT_PROPERTY_NAME);
	}
}
