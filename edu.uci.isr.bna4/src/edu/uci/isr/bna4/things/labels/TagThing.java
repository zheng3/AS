package edu.uci.isr.bna4.things.labels;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.constants.IFontConstants;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutableAnchorPoint;
import edu.uci.isr.bna4.facets.IHasMutableAngle;
import edu.uci.isr.bna4.facets.IHasMutableColor;
import edu.uci.isr.bna4.facets.IHasMutableFontData;
import edu.uci.isr.bna4.facets.IHasMutableIndicatorPoint;
import edu.uci.isr.bna4.facets.IHasMutableText;
import edu.uci.isr.bna4.facets.IRelativeMovable;
import edu.uci.isr.bna4.things.essence.AnchorPointEssenceThing;
import edu.uci.isr.widgets.swt.constants.FontStyle;

public class TagThing
	extends AnchorPointEssenceThing
	implements IHasBoundingBox, IHasMutableColor, IHasMutableAnchorPoint, IHasMutableText, IHasMutableAngle, IHasMutableIndicatorPoint, IHasMutableFontData, IRelativeMovable{

	public static final String DONT_INCREASE_FONT_SIZE_PROPERTY_NAME = "dontIncreaseFontSize";

	public TagThing(){
		this(null);
	}

	public TagThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setText("");
		setAngle(0);
		setFontName(IFontConstants.DEFAULT_FONT_NAME);
		setFontSize(10);
		setFontStyle(FontStyle.NORMAL);
		setDontIncreaseFontSize(true);
	}

	public Rectangle getBoundingBox(){
		Rectangle r = getProperty("#" + BOUNDING_BOX_PROPERTY_NAME);
		if(r != null){
			return new Rectangle(r.x, r.y, r.width, r.height);
		}

		Point p = getProperty(ANCHOR_POINT_PROPERTY_NAME);
		return new Rectangle(p.x, p.y, 1, 1);
	}

	public RGB getColor(){
		return getProperty(COLOR_PROPERTY_NAME);
	}

	public void setColor(RGB newColor){
		setProperty(COLOR_PROPERTY_NAME, newColor);
	}

	public String getText(){
		return getProperty(TEXT_PROPERTY_NAME);
	}

	public void setText(String text){
		setProperty(TEXT_PROPERTY_NAME, text);
	}

	public int getAngle(){
		return getProperty(ANGLE_PROPERTY_NAME);
	}

	public void setAngle(int angle){
		setProperty(ANGLE_PROPERTY_NAME, angle);
	}

	public Point getIndicatorPoint(){
		return getProperty(INDICATOR_POINT_PROPERTY_NAME);
	}

	public void setIndicatorPoint(Point p){
		setProperty(INDICATOR_POINT_PROPERTY_NAME, p);
	}

	public String getFontName(){
		return (String)getProperty(FONT_NAME_PROPERTY_NAME);
	}

	public void setFontName(String fontName){
		setProperty(FONT_NAME_PROPERTY_NAME, fontName);
	}

	public int getFontSize(){
		return getProperty(FONT_SIZE_PROPERTY_NAME);
	}

	public void setFontSize(int fontSize){
		setProperty(FONT_SIZE_PROPERTY_NAME, fontSize);
	}

	public FontStyle getFontStyle(){
		return (FontStyle)getProperty(FONT_STYLE_PROPERTY_NAME);
	}

	public void setFontStyle(FontStyle fontStyle){
		setProperty(FONT_STYLE_PROPERTY_NAME, fontStyle);
	}

	public void setDontIncreaseFontSize(boolean dontIncrease){
		setProperty(DONT_INCREASE_FONT_SIZE_PROPERTY_NAME, dontIncrease);
	}

	public boolean getDontIncreaseFontSize(){
		return getProperty(DONT_INCREASE_FONT_SIZE_PROPERTY_NAME);
	}
}
