package edu.uci.isr.bna4.assemblies;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.facets.IHasAnchorPoint;
import edu.uci.isr.bna4.facets.IHasInternalWorldEndpoint;
import edu.uci.isr.bna4.logics.coordinating.MirrorValueLogic;
import edu.uci.isr.bna4.things.glass.MappingGlassThing;
import edu.uci.isr.bna4.things.shapes.MappingThing;

public class MappingAssembly
	extends AbstractAssembly{

	public static final String MAPPING = "mapping";
	public static final String GLASS = "glass";

	public MappingAssembly(IBNAModel model, IThing parentThing, Object assemblyKind){
		super(model, parentThing, assemblyKind);

		// Create sub-things
		MappingThing mappingThing = new MappingThing();
		model.addThing(mappingThing, rootThing);

		MappingGlassThing mappingGlassThing = new MappingGlassThing();
		model.addThing(mappingGlassThing, rootThing);

		// Set up connections
		MirrorValueLogic.mirrorValue(mappingGlassThing, IHasAnchorPoint.ANCHOR_POINT_PROPERTY_NAME, mappingThing);
		MirrorValueLogic.mirrorValue(mappingGlassThing, IHasInternalWorldEndpoint.INTERNAL_ENDPOINT_PROPERTY_NAME, mappingThing);
		MirrorValueLogic.mirrorValue(mappingGlassThing, IHasInternalWorldEndpoint.INTERNAL_ENDPOINT_WORLD_THING_ID_PROPERTY_NAME, mappingThing);

		// mark parts
		markPart(MAPPING, mappingThing);
		markPart(GLASS, mappingGlassThing);
	}

	public MappingThing getMappingThing(){
		return getPart(MAPPING);
	}

	public MappingGlassThing getMappingGlassThing(){
		return getPart(GLASS);
	}
}
