package edu.uci.isr.bna4.things.essence;

import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.facets.IHasMutableAnchorPoint;
import edu.uci.isr.bna4.facets.IHasMutableEllipse;

public abstract class EllipseEssenceThing
	extends RectangleEssenceThing
	implements IHasMutableEllipse, IHasMutableAnchorPoint{

	public EllipseEssenceThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setReferencePointFractionOffset(new float[]{0.5f, 0.5f});
	}

	public Point getAnchorPoint(){
		return getReferencePoint();
	}

	public void setAnchorPoint(Point newAnchorPoint){
		setReferencePoint(newAnchorPoint);
	}
}
