package edu.uci.isr.bna4.things.glass;

import org.eclipse.swt.SWT;

import edu.uci.isr.bna4.facets.IHasMutableOrientation;
import edu.uci.isr.bna4.facets.IHasMutableTargetThing;
import edu.uci.isr.bna4.facets.IHasStandardCursor;
import edu.uci.isr.bna4.things.essence.AnchorPointEssenceThing;
import edu.uci.isr.widgets.swt.constants.Orientation;

public class ReshapeHandleGlassThing
	extends AnchorPointEssenceThing
	implements IHasMutableOrientation, IHasMutableTargetThing,
	IHasStandardCursor{

	public ReshapeHandleGlassThing(){
		this(null);
	}

	public ReshapeHandleGlassThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setOrientation(Orientation.NONE);
	}

	public Orientation getOrientation(){
		return getProperty(ORIENTATION_PROPERTY_NAME);
	}

	public void setOrientation(Orientation orientation){
		setProperty(ORIENTATION_PROPERTY_NAME, orientation);
	}

	public String getTargetThingID(){
		return getProperty(TARGET_THING_ID_PROPERTY_NAME);
	}

	public void setTargetThingID(String targetThingID){
		setProperty(TARGET_THING_ID_PROPERTY_NAME, targetThingID);
	}

	public int getStandardCursor(){
		switch(getOrientation()){
		case NORTHWEST:
			return SWT.CURSOR_SIZENW;
		case SOUTHEAST:
			return SWT.CURSOR_SIZESE;
		case NORTHEAST:
			return SWT.CURSOR_SIZENE;
		case SOUTHWEST:
			return SWT.CURSOR_SIZESW;
		case NORTH:
			return SWT.CURSOR_SIZEN;
		case SOUTH:
			return SWT.CURSOR_SIZES;
		case EAST:
			return SWT.CURSOR_SIZEE;
		case WEST:
			return SWT.CURSOR_SIZEW;
		default:
			return SWT.CURSOR_SIZEALL;
		}
	}

}
