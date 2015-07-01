package edu.uci.isr.bna4.things.glass;

import edu.uci.isr.bna4.facets.IHasMutableInternalWorldMapping;
import edu.uci.isr.bna4.things.essence.MappingEssenceThing;

public class MappingGlassThing
	extends MappingEssenceThing
	implements IHasMutableInternalWorldMapping{

	public MappingGlassThing(){
		this(null);
	}

	public MappingGlassThing(String id){
		super(id);
	}

	public String getInternalEndpointStuckToThingID(){
		return getProperty(INTERNAL_ENDPOINT_STUCK_TO_THING_ID_PROPERTY_NAME);
	}

	public void setInternalEndpointStuckToThingID(String internalWorldThingID){
		setProperty(INTERNAL_ENDPOINT_STUCK_TO_THING_ID_PROPERTY_NAME, internalWorldThingID);
	}
}
