package edu.uci.isr.bna4.assemblies;

import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.logics.coordinating.MirrorBoundingBoxLogic;
import edu.uci.isr.bna4.things.borders.BoxBorderThing;
import edu.uci.isr.bna4.things.glass.EndpointGlassThing;
import edu.uci.isr.bna4.things.labels.DirectionalLabelThing;
import edu.uci.isr.bna4.things.shapes.BoxThing;

public class EndpointAssembly
	extends AbstractAssembly{

	public static final String BACKGROUND = "background";
	public static final String BORDER = "border";
	public static final String LABEL = "label";
	public static final String GLASS = "glass";

	public EndpointAssembly(IBNAModel model, IThing parentThing, Object assemblyKind){
		super(model, parentThing, assemblyKind);

		BoxThing boxThing = new BoxThing();
		model.addThing(boxThing, rootThing);

		BoxBorderThing boxBorderThing = new BoxBorderThing();
		model.addThing(boxBorderThing, rootThing);

		DirectionalLabelThing directionalLabelThing = new DirectionalLabelThing();
		model.addThing(directionalLabelThing, rootThing);

		EndpointGlassThing endpointGlassThing = new EndpointGlassThing();
		model.addThing(endpointGlassThing, rootThing);

		// Set up connections
		MirrorBoundingBoxLogic.mirrorBoundingBox(endpointGlassThing, new Rectangle(0, 0, 0, 0), boxThing, boxBorderThing, directionalLabelThing);

		markPart(BACKGROUND, boxThing);
		markPart(BORDER, boxBorderThing);
		markPart(LABEL, directionalLabelThing);
		markPart(GLASS, endpointGlassThing);
	}

	public BoxThing getBoxThing(){
		return getPart(BACKGROUND);
	}

	public BoxBorderThing getBoxBorderThing(){
		return getPart(BORDER);
	}

	public DirectionalLabelThing getDirectionalLabelThing(){
		return getPart(LABEL);
	}

	public EndpointGlassThing getEndpointGlassThing(){
		return getPart(GLASS);
	}
}
