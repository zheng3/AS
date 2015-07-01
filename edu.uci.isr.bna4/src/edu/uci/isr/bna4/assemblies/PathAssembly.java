package edu.uci.isr.bna4.assemblies;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.facets.IHasAnchorPoint;
import edu.uci.isr.bna4.facets.IHasPathData;
import edu.uci.isr.bna4.logics.coordinating.MirrorValueLogic;
import edu.uci.isr.bna4.things.borders.PathBorderThing;
import edu.uci.isr.bna4.things.glass.PathGlassThing;
import edu.uci.isr.bna4.things.shapes.PathThing;

public class PathAssembly
	extends AbstractAssembly{

	public static final String BACKGROUND = "background";
	public static final String BORDER = "border";
	public static final String GLASS = "glass";

	public PathAssembly(IBNAModel model, IThing parentThing, Object assemblyKind){
		super(model, parentThing, assemblyKind);

		// Create sub-things
		PathThing pathThing = new PathThing();
		model.addThing(pathThing, rootThing);

		PathBorderThing pathBorderThing = new PathBorderThing();
		model.addThing(pathBorderThing, pathThing);

		PathGlassThing pathGlassThing = new PathGlassThing();
		model.addThing(pathGlassThing, rootThing);

		// Set up connections
		MirrorValueLogic.mirrorValue(pathGlassThing, IHasPathData.PATH_DATA_PROPERTY_NAME, pathThing, pathBorderThing);
		MirrorValueLogic.mirrorValue(pathGlassThing, IHasAnchorPoint.ANCHOR_POINT_PROPERTY_NAME, pathThing, pathBorderThing);

		// mark parts
		markPart(BACKGROUND, pathThing);
		markPart(BORDER, pathBorderThing);
		markPart(GLASS, pathGlassThing);
	}

	public PathThing getPathThing(){
		return getPart(BACKGROUND);
	}

	public PathBorderThing getPathBorderThing(){
		return getPart(BORDER);
	}

	public PathGlassThing getPathGlassThing(){
		return getPart(GLASS);
	}
}
