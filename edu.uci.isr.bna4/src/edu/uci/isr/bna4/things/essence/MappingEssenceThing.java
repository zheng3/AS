package edu.uci.isr.bna4.things.essence;

import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.facets.IHasMutableInternalWorldEndpoint;

public class MappingEssenceThing
	extends AnchorPointEssenceThing
	implements IHasMutableInternalWorldEndpoint{

	public MappingEssenceThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setInternalEndpoint(new Point(0, 0));
	}

	public String getInternalEndpointWorldThingID(){
		return getProperty(INTERNAL_ENDPOINT_WORLD_THING_ID_PROPERTY_NAME);
	}

	public void setInternalEndpointWorldThingID(String worldThingID){
		setProperty(INTERNAL_ENDPOINT_WORLD_THING_ID_PROPERTY_NAME, worldThingID);
	}

	public Point getInternalEndpoint(){
		return getProperty(INTERNAL_ENDPOINT_PROPERTY_NAME);
	}

	public void setInternalEndpoint(Point internalWorldPoint){
		setProperty(INTERNAL_ENDPOINT_PROPERTY_NAME, BNAUtils.clone(internalWorldPoint));
	}
}
