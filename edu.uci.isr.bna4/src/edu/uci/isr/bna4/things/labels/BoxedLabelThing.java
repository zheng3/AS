package edu.uci.isr.bna4.things.labels;

import org.eclipse.swt.graphics.RGB;

import edu.uci.isr.bna4.constants.IFontConstants;
import edu.uci.isr.bna4.facets.IHasMutableColor;
import edu.uci.isr.bna4.facets.IHasMutableFontData;
import edu.uci.isr.bna4.facets.IHasMutableHorizontalAlignment;
import edu.uci.isr.bna4.facets.IHasMutableText;
import edu.uci.isr.bna4.facets.IHasMutableVerticalAlignment;
import edu.uci.isr.bna4.facets.IRelativeMovable;
import edu.uci.isr.bna4.things.essence.RectangleEssenceThing;
import edu.uci.isr.widgets.swt.constants.FontStyle;
import edu.uci.isr.widgets.swt.constants.HorizontalAlignment;
import edu.uci.isr.widgets.swt.constants.VerticalAlignment;

public class BoxedLabelThing
	extends RectangleEssenceThing
	implements IHasMutableText, IHasMutableColor,
	IHasMutableHorizontalAlignment, IHasMutableVerticalAlignment,
	IHasMutableFontData, IRelativeMovable{

	public static final String DONT_INCREASE_FONT_SIZE_PROPERTY_NAME = "dontIncreaseFontSize";

	public BoxedLabelThing(){
		this(null);
	}

	public BoxedLabelThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setText("");
		setFontName(IFontConstants.DEFAULT_FONT_NAME);
		setFontSize(10);
		setFontStyle(FontStyle.NORMAL);
		setHorizontalAlignment(HorizontalAlignment.CENTER);
		setVerticalAlignment(VerticalAlignment.MIDDLE);
		setDontIncreaseFontSize(true);
	}

	public void setColor(RGB c){
		setProperty(COLOR_PROPERTY_NAME, c);
	}

	public RGB getColor(){
		return getProperty(COLOR_PROPERTY_NAME);
	}

	public String getText(){
		return (String)getProperty(TEXT_PROPERTY_NAME);
	}

	public void setText(String text){
		setProperty(TEXT_PROPERTY_NAME, text);
	}

	public HorizontalAlignment getHorizontalAlignment(){
		return (HorizontalAlignment)getProperty(HORIZONTAL_ALIGNMENT_PROPERTY_NAME);
	}

	public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment){
		setProperty(HORIZONTAL_ALIGNMENT_PROPERTY_NAME, horizontalAlignment);
	}

	public VerticalAlignment getVerticalAlignment(){
		return (VerticalAlignment)getProperty(VERTICAL_ALIGNMENT_PROPERTY_NAME);
	}

	public void setVerticalAlignment(VerticalAlignment verticalAlignment){
		setProperty(VERTICAL_ALIGNMENT_PROPERTY_NAME, verticalAlignment);
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
