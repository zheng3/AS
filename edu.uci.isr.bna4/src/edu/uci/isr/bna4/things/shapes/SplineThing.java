package edu.uci.isr.bna4.things.shapes;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

import edu.uci.isr.bna4.facets.IHasMutableColor;
import edu.uci.isr.bna4.facets.IHasMutableLineStyle;
import edu.uci.isr.bna4.facets.IHasMutableLineWidth;
import edu.uci.isr.bna4.facets.IRelativeMovable;
import edu.uci.isr.bna4.things.essence.SplineEssenceThing;

public class SplineThing
	extends SplineEssenceThing
	implements IHasMutableColor, IHasMutableLineWidth, IHasMutableLineStyle,
	IRelativeMovable{

	public SplineThing(){
		this(null);
	}

	public SplineThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setLineStyle(SWT.LINE_SOLID);
		setColor(new RGB(0, 0, 0));
		setLineWidth(1);
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
}
