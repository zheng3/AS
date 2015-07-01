package edu.uci.isr.bna4.assemblies;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.facets.IHasPoints;
import edu.uci.isr.bna4.logics.coordinating.MirrorValueLogic;
import edu.uci.isr.bna4.things.borders.PolygonBorderThing;
import edu.uci.isr.bna4.things.essence.PolygonEssenceThing.IPolygonGenerator;
import edu.uci.isr.bna4.things.glass.PolygonGlassThing;
import edu.uci.isr.bna4.things.shapes.PolygonThing;

public class PolygonAssembly
	extends AbstractAssembly{

	public static final String BACKGROUND = "background";
	public static final String BORDER = "border";
	public static final String GLASS = "glass";

	public PolygonAssembly(IBNAModel model, IThing parentThing, Object assemblyKind, IPolygonGenerator polygonGenerator){
		super(model, parentThing, assemblyKind);

		// Create sub-things
		PolygonThing polygonThing = new PolygonThing();
		model.addThing(polygonThing, rootThing);

		PolygonBorderThing polygonBorderThing = new PolygonBorderThing();
		model.addThing(polygonBorderThing, polygonThing);

		PolygonGlassThing polygonGlassThing = new PolygonGlassThing(polygonGenerator);
		model.addThing(polygonGlassThing, rootThing);

		// Set up connections
		MirrorValueLogic.mirrorValue(polygonGlassThing, IHasPoints.POINTS_PROPERTY_NAME, polygonThing, polygonBorderThing);

		markPart(BACKGROUND, polygonThing);
		markPart(BORDER, polygonBorderThing);
		markPart(GLASS, polygonGlassThing);
	}

	public PolygonThing getPolygonThing(){
		return getPart(BACKGROUND);
	}

	public PolygonBorderThing getPolygonBorderThing(){
		return getPart(BORDER);
	}

	public PolygonGlassThing getPolygonGlassThing(){
		return getPart(GLASS);
	}
}
