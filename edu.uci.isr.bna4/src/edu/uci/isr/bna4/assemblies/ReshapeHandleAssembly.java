package edu.uci.isr.bna4.assemblies;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.facets.IHasAnchorPoint;
import edu.uci.isr.bna4.logics.coordinating.MirrorValueLogic;
import edu.uci.isr.bna4.things.glass.ReshapeHandleGlassThing;
import edu.uci.isr.bna4.things.shapes.ReshapeHandleThing;

public class ReshapeHandleAssembly
	extends AbstractAssembly{

	public static final String HANDLE = "handle";
	public static final String GLASS = "glass";

	public ReshapeHandleAssembly(IBNAModel model, IThing parentThing, Object assemblyKind){
		super(model, parentThing, assemblyKind);

		// Create sub-things
		ReshapeHandleThing reshapeHandleThing = new ReshapeHandleThing();
		model.addThing(reshapeHandleThing, rootThing);

		ReshapeHandleGlassThing reshapeHandleGlassThing = new ReshapeHandleGlassThing();
		model.addThing(reshapeHandleGlassThing, rootThing);

		// Set up connections
		MirrorValueLogic.mirrorValue(reshapeHandleGlassThing, IHasAnchorPoint.ANCHOR_POINT_PROPERTY_NAME, reshapeHandleThing);

		// mark parts
		markPart(HANDLE, reshapeHandleThing);
		markPart(GLASS, reshapeHandleGlassThing);
	}

	public ReshapeHandleThing getReshapeHandleThing(){
		return getPart(HANDLE);
	}

	public ReshapeHandleGlassThing getReshapeHandleGlassThing(){
		return getPart(GLASS);
	}
}
