package edu.uci.isr.bna4.assemblies;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.logics.coordinating.MirrorBoundingBoxLogic;
import edu.uci.isr.bna4.things.borders.EllipseBorderThing;
import edu.uci.isr.bna4.things.glass.EllipseGlassThing;
import edu.uci.isr.bna4.things.shapes.EllipseThing;

public class EllipseAssembly
	extends AbstractAssembly{

	public static final String BACKGROUND = "background";
	public static final String BORDER = "border";
	public static final String GLASS = "glass";

	public EllipseAssembly(IBNAModel model, IThing parentThing, Object assemblyKind){
		super(model, parentThing, assemblyKind);

		// Create sub-things
		EllipseThing ellipseThing = new EllipseThing();
		ellipseThing.setColor(new RGB(0, 0, 0));
		model.addThing(ellipseThing, rootThing);

		EllipseBorderThing ellipseBorderThing = new EllipseBorderThing();
		ellipseBorderThing.setColor(new RGB(0, 0, 0));
		model.addThing(ellipseBorderThing, ellipseThing);

		EllipseGlassThing ellipseGlassThing = new EllipseGlassThing();
		model.addThing(ellipseGlassThing, rootThing);

		// Set up connections
		MirrorBoundingBoxLogic.mirrorBoundingBox(ellipseGlassThing, new Rectangle(0, 0, 0, 0), ellipseThing);
		MirrorBoundingBoxLogic.mirrorBoundingBox(ellipseGlassThing, new Rectangle(0, 0, 0, 0), ellipseBorderThing);

		markPart(BACKGROUND, ellipseThing);
		markPart(BORDER, ellipseBorderThing);
		markPart(GLASS, ellipseGlassThing);
	}

	public EllipseThing getEllipseThing(){
		return getPart(BACKGROUND);
	}

	public EllipseBorderThing getEllipseBorderThing(){
		return getPart(BORDER);
	}

	public EllipseGlassThing getEllipseGlassThing(){
		return getPart(GLASS);
	}
}
