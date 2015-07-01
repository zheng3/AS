package edu.uci.isr.bna4.things.essence;

import java.util.concurrent.locks.Lock;

import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.AbstractThing;
import edu.uci.isr.bna4.facets.IRelativeMovable;

public abstract class RelativeMovableEssenceThing
	extends AbstractThing
	implements IRelativeMovable{

	public RelativeMovableEssenceThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
	}

	abstract public Point getReferencePoint();

	public void setReferencePoint(Point p){
		if(p != null){
			Lock lock = getPropertyLock();
			lock.lock();
			try{
				Point op = getReferencePoint();
				if(op != null){
					int dx = p.x - op.x;
					int dy = p.y - op.y;
					if(dx != 0 || dy != 0){
						moveRelative(dx, dy);
					}
				}
			}
			finally{
				lock.unlock();
			}
		}
	}

	abstract protected void moveRelative(int dx, int dy);
}
