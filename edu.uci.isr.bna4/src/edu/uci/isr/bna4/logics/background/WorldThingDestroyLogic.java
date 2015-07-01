package edu.uci.isr.bna4.logics.background;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNAWorld;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.facets.IHasWorld;

/* Note! This class cannot rely on a TypedThingSetTrackingLogic, because
 * it needs the typed thing set during the destroy operation and it can't
 * be guaranteed to exist at that time (the TTSTL is probably already
 * destroyed before we get here) */
public class WorldThingDestroyLogic extends AbstractThingLogic{
	protected boolean removeDestroyedViews = false;
	
	public WorldThingDestroyLogic(boolean removeDestroyedViews){
		this.removeDestroyedViews = removeDestroyedViews;
	}
	
	public boolean getRemoveDestroyedViews(){
		return removeDestroyedViews;
	}
	
	public void destroy(){
		IBNAModel model = getBNAModel();
		if(model != null){
			IThing[] allThings = model.getAllThings();
			for(IThing t : allThings){
				if(t instanceof IHasWorld){
					IBNAWorld internalWorld = ((IHasWorld)t).getWorld();
					if(internalWorld != null){
						internalWorld.destroy();
						if(removeDestroyedViews){
							((IHasWorld)t).removeProperty(IHasWorld.WORLD_PROPERTY_NAME);
						}
					}
				}
			}
		}
		super.destroy();
	}
}
