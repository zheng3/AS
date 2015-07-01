package edu.uci.isr.bna4.things.labels;

import edu.uci.isr.bna4.facets.IHasMutableHorizontalAlignment;
import edu.uci.isr.bna4.facets.IHasMutableImage;
import edu.uci.isr.bna4.facets.IHasMutableVerticalAlignment;
import edu.uci.isr.bna4.things.essence.RectangleEssenceThing;
import edu.uci.isr.widgets.swt.constants.HorizontalAlignment;
import edu.uci.isr.widgets.swt.constants.VerticalAlignment;

public class BoxedImageThing
	extends RectangleEssenceThing
	implements IHasMutableHorizontalAlignment, IHasMutableVerticalAlignment,
	IHasMutableImage{

	public BoxedImageThing(){
		this(null);
	}

	public BoxedImageThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setImagePath(null);
		setHorizontalAlignment(HorizontalAlignment.CENTER);
		setVerticalAlignment(VerticalAlignment.MIDDLE);
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

	public String getImagePath(){
		return (String)getProperty(IMAGE_PATH_PROPERTY_NAME);
	}

	public void setImagePath(String s){
		setProperty(IMAGE_PATH_PROPERTY_NAME, s);
	}
}
