package edu.uci.isr.bna4.things.essence;

import java.util.concurrent.locks.Lock;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.facets.IHasMutableBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutableReferencePointFractionOffset;

public abstract class RectangleEssenceThing
    extends RelativeMovableEssenceThing
    implements IHasMutableBoundingBox, IHasMutableReferencePointFractionOffset{

	public RectangleEssenceThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setMinBoundingBoxSize(new Point(5, 5));
		setReferencePointFractionOffset(new float[]{0, 0});
		setBoundingBox(new Rectangle(0, 0, 10, 10));
	}

	public Point getMinBoundingBoxSize(){
		return BNAUtils.clone((Point)getProperty(MINIMUM_BOUNDING_BOX_SIZE_PROPERTY_NAME));
	}

	public int getMinimumHeight(){
		return getMinBoundingBoxSize().y;
	}

	public int getMinimumWidth(){
		return getMinBoundingBoxSize().x;
	}

	public void setMinBoundingBoxSize(Point minSize){
		Lock lock = getPropertyLock();
		lock.lock();
		try{
			setProperty(MINIMUM_BOUNDING_BOX_SIZE_PROPERTY_NAME, BNAUtils.clone(minSize));
			Rectangle r = getBoundingBox();
			if(r != null){
				setBoundingBox(r);
			}
		}
		finally{
			lock.unlock();
		}
	}

	public Rectangle getBoundingBox(){
		return BNAUtils.clone((Rectangle)getProperty(BOUNDING_BOX_PROPERTY_NAME));
	}

	public void setBoundingBox(Rectangle r){
		Rectangle nr = BNAUtils.normalizeRectangle(r);
		Lock lock = getPropertyLock();
		lock.lock();
		try{
			Point minSize = getMinBoundingBoxSize();
			if(minSize != null){
				if(nr.width < minSize.x){
					nr.width = minSize.x;
				}
				if(nr.height < minSize.y){
					nr.height = minSize.y;
				}
			}

			setProperty(BOUNDING_BOX_PROPERTY_NAME, nr);
		}
		finally{
			lock.unlock();
		}
	}

	public void setBoundingBox(int x, int y, int width, int height){
		setBoundingBox(new Rectangle(x, y, width, height));
	}

	@Override
	public Point getReferencePoint(){
		Rectangle r = getBoundingBox();
		float[] fo = getReferencePointFractionOffset();
		return new Point(r.x + Math.round(fo[0] * r.width), r.y + Math.round(fo[1] * r.height));
	}

	@Override
	protected void moveRelative(int dx, int dy){
		if(dx != 0 || dy != 0){
			Lock lock = getPropertyLock();
			lock.lock();
			try{
				Rectangle r = getBoundingBox();
				setBoundingBox(new Rectangle(r.x + dx, r.y + dy, r.width, r.height));
			}
			finally{
				lock.unlock();
			}
		}
	}

	public void setReferencePointFractionOffset(float[] offset){
		setProperty(IHasMutableReferencePointFractionOffset.REFERENCE_POINT_FRACTION_OFFSET_PROPERTY_NAME, offset.clone());
	}

	public float[] getReferencePointFractionOffset(){
		return getProperty(IHasMutableReferencePointFractionOffset.REFERENCE_POINT_FRACTION_OFFSET_PROPERTY_NAME);
	}
}
