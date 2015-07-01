package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IBNAWorld;

public interface IHasMutableWorld
	extends IHasWorld{

	public void setWorld(IBNAWorld newWorld);
}
