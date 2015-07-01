package edu.uci.isr.bna4.assemblies;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;

public final class GenericAssembly
	extends AbstractAssembly{

	public GenericAssembly(IBNAModel model, IThing parentThing, Object assemblyKind){
		super(model, parentThing, assemblyKind);
	}
}
