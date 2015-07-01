package edu.uci.isr.bna4.assemblies;

import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.logics.coordinating.MirrorBoundingBoxLogic;
import edu.uci.isr.bna4.things.borders.BoxBorderThing;
import edu.uci.isr.bna4.things.glass.BoxGlassThing;
import edu.uci.isr.bna4.things.labels.BoxedLabelThing;
import edu.uci.isr.bna4.things.layouts.LayoutRootThing;
import edu.uci.isr.bna4.things.shapes.BoxThing;
import edu.uci.isr.bna4.things.utility.WorldThing;

public class ClassBoxAssembly
	extends AbstractAssembly{

	public static final String BACKGROUND = "background";
	public static final String BORDER = "border";
	public static final String LABEL = "label";
	public static final String GLASS = "glass";
	public static final String VIEW = "view";
	public static final String LAYOUT = "layout";

	public ClassBoxAssembly(IBNAModel model, IThing parentThing, Object assemblyKind){
		super(model, parentThing, assemblyKind);

		// Create sub-things
		BoxThing boxThing = new BoxThing();
		model.addThing(boxThing, rootThing);

		BoxBorderThing boxBorderThing = new BoxBorderThing();
		model.addThing(boxBorderThing, boxThing);

		BoxedLabelThing boxedLabelThing = new BoxedLabelThing();
		model.addThing(boxedLabelThing, boxThing);

		BoxGlassThing boxGlassThing = new BoxGlassThing();
		model.addThing(boxGlassThing, rootThing);

		LayoutRootThing layoutThing = new LayoutRootThing();
		model.addThing(layoutThing, boxGlassThing);

		// View thing goes even on top of the glass thing
		WorldThing viewThing = new WorldThing();
		model.addThing(viewThing, rootThing);

		// Set up connections
		// TODO Add connection between layoutThing and boxThing???
		MirrorBoundingBoxLogic.mirrorBoundingBox(boxGlassThing, new Rectangle(0, 0, 0, 0), boxThing, boxBorderThing);
		MirrorBoundingBoxLogic.mirrorBoundingBox(boxGlassThing, new Rectangle(5, 5, -10, -10), boxedLabelThing, viewThing);

		markPart(BACKGROUND, boxThing);
		markPart(BORDER, boxBorderThing);
		markPart(LABEL, boxedLabelThing);
		markPart(GLASS, boxGlassThing);
		markPart(VIEW, viewThing);
		markPart(LAYOUT, layoutThing);
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

	public WorldThing getViewThing(){
		return getPart(VIEW);
	}

	public LayoutRootThing getLayoutThing(){
		return getPart(LAYOUT);
	}
}
