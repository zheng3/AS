package edu.uci.isr.bna4.assemblies;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.things.glass.StickySplineGlassThing;

@Deprecated
public class StickySplineAssembly
	extends SplineAssembly{

	@Deprecated
	public StickySplineAssembly(IBNAModel model, IThing parentThing, Object assemblyKind){
		super(model, parentThing, assemblyKind);
	}

	@Deprecated
	public StickySplineGlassThing getStickySplineGlassThing(){
		return getPart(GLASS);
	}
}
