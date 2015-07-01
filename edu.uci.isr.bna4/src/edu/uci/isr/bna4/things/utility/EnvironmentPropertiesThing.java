package edu.uci.isr.bna4.things.utility;

import edu.uci.isr.bna4.AbstractThing;
import edu.uci.isr.bna4.IThingPeer;

public class EnvironmentPropertiesThing
	extends AbstractThing{

	public static final String ENVIRONMENT_PROPERTIES_THING_ID = EnvironmentPropertiesThing.class.getName();

	public EnvironmentPropertiesThing(){
		super(ENVIRONMENT_PROPERTIES_THING_ID);
	}

	@Override
	public Class<? extends IThingPeer> getPeerClass(){
		return NoThingPeer.class;
	}
}
