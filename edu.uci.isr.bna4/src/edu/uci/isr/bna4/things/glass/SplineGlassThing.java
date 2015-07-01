package edu.uci.isr.bna4.things.glass;

import java.util.concurrent.locks.Lock;

import edu.uci.isr.bna4.facets.IHasMutableRotatingOffset;
import edu.uci.isr.bna4.facets.IHasMutableSelected;
import edu.uci.isr.bna4.things.essence.SplineEssenceThing;

public class SplineGlassThing
	extends SplineEssenceThing
	implements IHasMutableSelected, IHasMutableRotatingOffset{

	public SplineGlassThing(){
		this(null);
	}

	public SplineGlassThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setProperty(ROTATING_OFFSET_PROPERTY_NAME, 0);
		setSelected(false);
	}

	public boolean isSelected(){
		return getProperty(SELECTED_PROPERTY_NAME);
	}

	public void setSelected(boolean selected){
		setProperty(SELECTED_PROPERTY_NAME, selected);
	}

	public int getRotatingOffset(){
		return getProperty(ROTATING_OFFSET_PROPERTY_NAME);
	}

	public void incrementRotatingOffset(){
		if(isSelected()){
			Lock lock = getPropertyLock();
			lock.lock();
			try{
				setProperty(ROTATING_OFFSET_PROPERTY_NAME, (getRotatingOffset() + 1) % 6);
			}
			finally{
				lock.unlock();
			}
		}
	}
}
