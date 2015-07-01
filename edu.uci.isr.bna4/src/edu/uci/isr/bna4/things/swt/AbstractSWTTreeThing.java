package edu.uci.isr.bna4.things.swt;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThing;
import edu.uci.isr.bna4.constants.CompletionStatus;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutableAnchorPoint;
import edu.uci.isr.bna4.facets.IHasMutableCompletionStatus;
import edu.uci.isr.bna4.facets.IHasMutableEditing;

public abstract class AbstractSWTTreeThing
	extends AbstractThing
	implements IHasMutableAnchorPoint, IHasBoundingBox,
	IHasMutableCompletionStatus, IHasMutableEditing{

	public AbstractSWTTreeThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setAnchorPoint(new Point(0, 0));
		setEditing(false);
		setCompletionStatus(CompletionStatus.INCOMPLETE);
	}

	public void setValue(Object value){
		setProperty("value", value);
	}

	public Object getValue(){
		return getProperty("value");
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
