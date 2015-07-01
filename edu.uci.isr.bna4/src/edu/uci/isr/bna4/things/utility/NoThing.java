package edu.uci.isr.bna4.things.utility;

import edu.uci.isr.bna4.AbstractThing;
import edu.uci.isr.bna4.IThing;

public class NoThing
	extends AbstractThing
	implements IThing{

	public NoThing(){
		this(null);
	}

	public NoThing(String id){
		super(id);
	}

}
