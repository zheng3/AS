package edu.uci.isr.bna4.facets;

import org.eclipse.swt.graphics.Point;

public interface IHasMutableInternalWorldEndpoint
	extends IHasInternalWorldEndpoint{

	public void setInternalEndpointWorldThingID(String worldThingID);

	public void setInternalEndpoint(Point internalWorldPoint);
}
