package edu.uci.isr.bna4.assemblies;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.facets.IHasRoundedCorners;
import edu.uci.isr.bna4.logics.coordinating.MirrorBoundingBoxLogic;
import edu.uci.isr.bna4.logics.coordinating.MirrorValueLogic;
import edu.uci.isr.bna4.things.borders.BoxBorderThing;
import edu.uci.isr.bna4.things.glass.BoxGlassThing;
import edu.uci.isr.bna4.things.labels.BoxedLabelThing;
import edu.uci.isr.bna4.things.shapes.BoxThing;
import edu.uci.isr.bna4.things.utility.NoThing;
import edu.uci.isr.bna4.things.utility.WorldThing;

public class BoxAssembly
	extends AbstractAssembly{

	public static final String BACKGROUND = "background";
	public static final String BORDER = "border";
	public static final String LABEL = "label";
	public static final String GLASS = "glass";
	public static final String MAPPING = "mapping";
	public static final String ENDPOINT = "endpoint";
	public static final String WORLD = "world";

	public BoxAssembly(IBNAModel model, IThing parentThing, Object assemblyKind){
		super(model, parentThing, assemblyKind);

		// Create sub-things
		BoxThing boxThing = new BoxThing();
		boxThing.setMinBoundingBoxSize(new Point(0, 0));
		model.addThing(boxThing, rootThing);

		BoxBorderThing boxBorderThing = new BoxBorderThing();
		boxBorderThing.setMinBoundingBoxSize(new Point(0, 0));
		model.addThing(boxBorderThing, boxThing);

		BoxedLabelThing boxedLabelThing = new BoxedLabelThing();
		boxedLabelThing.setMinBoundingBoxSize(new Point(0, 0));
		model.addThing(boxedLabelThing, boxThing);

		BoxGlassThing boxGlassThing = new BoxGlassThing();
		model.addThing(boxGlassThing, rootThing);

		// World thing goes even on top of the glass thing
		WorldThing worldThing = new WorldThing();
		worldThing.setMinBoundingBoxSize(new Point(0, 0));
		model.addThing(worldThing, rootThing);

		NoThing mappingRootThing = new NoThing();
		mappingRootThing.setProperty("rootThingKind", "mapping");
		model.addThing(mappingRootThing, rootThing);

		NoThing endpointRootThing = new NoThing();
		endpointRootThing.setProperty("rootThingKind", "endpoint");
		model.addThing(endpointRootThing, rootThing);

		// Set up connections
		MirrorValueLogic.mirrorValue(boxGlassThing, IHasRoundedCorners.CORNER_WIDTH_PROPERTY_NAME, boxThing, boxBorderThing);
		MirrorValueLogic.mirrorValue(boxGlassThing, IHasRoundedCorners.CORNER_HEIGHT_PROPERTY_NAME, boxThing, boxBorderThing);
		MirrorBoundingBoxLogic.mirrorBoundingBox(boxGlassThing, new Rectangle(0, 0, 0, 0), boxThing, boxBorderThing);
		MirrorBoundingBoxLogic.mirrorBoundingBox(boxGlassThing, new Rectangle(5, 5, -10, -10), boxedLabelThing);
		MirrorBoundingBoxLogic.mirrorBoundingBox(boxGlassThing, new Rectangle(15, 15, -30, -30), worldThing);

		markPart(BACKGROUND, boxThing);
		markPart(BORDER, boxBorderThing);
		markPart(LABEL, boxedLabelThing);
		markPart(GLASS, boxGlassThing);
		markPart(MAPPING, mappingRootThing);
		markPart(ENDPOINT, endpointRootThing);
		markPart(WORLD, worldThing);
	}

	public BoxThing getBoxThing(){
		return getPart(BACKGROUND);
	}

	public BoxBorderThing getBoxBorderThing(){
		return getPart(BORDER);
	}

	public BoxedLabelThing getBoxedLabelThing(){
		return getPart(LABEL);
	}

	public BoxGlassThing getBoxGlassThing(){
		return getPart(GLASS);
	}

	public NoThing getMappingRootThing(){
		return getPart(MAPPING);
	}

	public NoThing getEndpointRootThing(){
		return getPart(ENDPOINT);
	}

	public WorldThing getWorldThing(){
		return getPart(WORLD);
	}
}
