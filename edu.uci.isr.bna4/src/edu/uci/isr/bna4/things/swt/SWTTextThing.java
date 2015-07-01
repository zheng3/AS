package edu.uci.isr.bna4.things.swt;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThing;
import edu.uci.isr.bna4.constants.CompletionStatus;
import edu.uci.isr.bna4.constants.IFontConstants;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutableAnchorPoint;
import edu.uci.isr.bna4.facets.IHasMutableColor;
import edu.uci.isr.bna4.facets.IHasMutableCompletionStatus;
import edu.uci.isr.bna4.facets.IHasMutableEditing;
import edu.uci.isr.bna4.facets.IHasMutableFontData;
import edu.uci.isr.bna4.facets.IHasMutableHorizontalAlignment;
import edu.uci.isr.bna4.facets.IHasMutableText;
import edu.uci.isr.bna4.facets.IRelativeMovable;
import edu.uci.isr.widgets.swt.constants.FontStyle;
import edu.uci.isr.widgets.swt.constants.HorizontalAlignment;

public class SWTTextThing
	extends AbstractThing
	implements IHasMutableText, IHasMutableAnchorPoint, IHasBoundingBox,
	IHasMutableColor, IHasMutableHorizontalAlignment, IHasMutableFontData,
	IHasMutableCompletionStatus, IHasMutableEditing, IRelativeMovable{

	public SWTTextThing(){
		this(null);
	}

	public SWTTextThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		Point p = new Point(50, 50);
		setAnchorPoint(p);
		//setColor(new RGB(0, 0, 0));
		setFontName(IFontConstants.DEFAULT_FONT_NAME);
		setFontSize(10);
		setFontStyle(FontStyle.NORMAL);
		setHorizontalAlignment(HorizontalAlignment.CENTER);
		setEditing(false);
		setCompletionStatus(CompletionStatus.INCOMPLETE);
	}

	public void setAnchorPoint(Point newAnchorPoint){
		setProperty(ANCHOR_POINT_PROPERTY_NAME, newAnchorPoint);
	}

	public Point getAnchorPoint(){
		Point p = (Point)getProperty(ANCHOR_POINT_PROPERTY_NAME);
		return new Point(p.x, p.y);
	}

	public Rectangle getBoundingBox(){
		Rectangle r = (Rectangle)getProperty("#boundingBox");
		if(r != null){
			return r;
		}
		Point p = getAnchorPoint();
		if(p != null){
			return new Rectangle(p.x, p.y, 0, 0);
		}
		return new Rectangle(0, 0, 0, 0);
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

	public void setEditing(boolean editing){
		setProperty(EDITING_PROPERTY_NAME, editing);
	}

	public boolean isEditing(){
		return getProperty(EDITING_PROPERTY_NAME);
	}

	public Point getReferencePoint(){
		return getAnchorPoint();
	}

	public void setReferencePoint(Point p){
		setAnchorPoint(p);
	}

	public CompletionStatus getCompletionStatus(){
		return getProperty(COMPLETION_STATUS_PROPERTY_NAME);
	}

	public void setCompletionStatus(CompletionStatus completionStatus){
		setProperty(COMPLETION_STATUS_PROPERTY_NAME, completionStatus);
	}
}
