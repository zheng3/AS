package edu.uci.isr.bna4.things.essence;

import java.util.concurrent.locks.Lock;

import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.facets.IHasMutableAnchorPoint;

public abstract class AnchorPointEssenceThing
	extends RelativeMovableEssenceThing
	implements IHasMutableAnchorPoint{

	public AnchorPointEssenceThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setAnchorPoint(new Point(0, 0));
	}

	public Point getAnchorPoint(){
		return BNAUtils.clone((Point)getProperty(ANCHOR_POINT_PROPERTY_NAME));
	}

	public void setAnchorPoint(Point p){
		setProperty(ANCHOR_POINT_PROPERTY_NAME, p);
	}

	@Override
	public Point getReferencePoint(){
		return getAnchorPoint();
	}

	@Override
	public void setReferencePoint(Point p){
		setAnchorPoint(p);
	}

	@Override
	protected void moveRelative(int dx, int dy){
		if(dx != 0 || dy != 0){
			Lock lock = getPropertyLock();
			lock.lock();
			try{
				Point p = getAnchorPoint();
				p.x += dx;
				p.y += dy;
				setAnchorPoint(p);
			}
			finally{
				lock.unlock();
			}
		}
	}
}
