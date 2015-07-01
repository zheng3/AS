package edu.uci.isr.bna4.things.utility;

import edu.uci.isr.bna4.IBNAWorld;
import edu.uci.isr.bna4.facets.IHasMutableWorld;
import edu.uci.isr.bna4.things.essence.RectangleEssenceThing;

public class WorldThing
	extends RectangleEssenceThing
	implements IHasMutableWorld{

	public WorldThing(){
		this(null);
	}

	public WorldThing(String id){
		super(id);
	}

	public IBNAWorld getWorld(){
		return getProperty(WORLD_PROPERTY_NAME);
	}

	public void setWorld(IBNAWorld world){
		setProperty(WORLD_PROPERTY_NAME, world);
	}

	public void clearWorld(){
		removeProperty(WORLD_PROPERTY_NAME);
	}
}
