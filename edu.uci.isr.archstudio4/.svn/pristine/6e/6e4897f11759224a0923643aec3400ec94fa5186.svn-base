package edu.uci.isr.archstudio4.comp.archipelago.interactions.logics.mapping;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoUtils;
import edu.uci.isr.archstudio4.comp.archipelago.generic.logics.mapping.AbstractAutomapSingleAssemblyXArchRelativePathMappingLogic;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.UserEditableUtils;
import edu.uci.isr.bna4.assemblies.BoxAssembly;
import edu.uci.isr.bna4.assemblies.GenericAssembly;
import edu.uci.isr.bna4.assemblies.SplineAssembly;
import edu.uci.isr.bna4.constants.ArrowheadShape;
import edu.uci.isr.bna4.facets.IHasMutableBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutableSelected;
import edu.uci.isr.bna4.facets.IHasMutableText;
import edu.uci.isr.bna4.facets.IHasText;
import edu.uci.isr.bna4.facets.IRelativeMovable;
import edu.uci.isr.bna4.logics.coordinating.MirrorBoundingBoxLogic;
import edu.uci.isr.bna4.logics.coordinating.MoveWithLogic;
import edu.uci.isr.bna4.logics.information.ToolTipLogic;
import edu.uci.isr.bna4.logics.tracking.ThingPropertyTrackingLogic;
import edu.uci.isr.bna4.things.glass.BoxGlassThing;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public class MapXadlLifelineLogic extends
		AbstractAutomapSingleAssemblyXArchRelativePathMappingLogic<GenericAssembly> {

	public MapXadlLifelineLogic(XArchFlatInterface xarch, ObjRef rootObjRef, String relativePath, ThingPropertyTrackingLogic tptl, IThing parentThing, Object kind){
		super(xarch, rootObjRef, relativePath, tptl, GenericAssembly.class, parentThing, kind);
		automapSimpleValue("description", "[no description]", "label", IHasText.TEXT_PROPERTY_NAME, true);
		automapSimpleValue("description", "[no description]", "glass", ToolTipLogic.TOOL_TIP_PROPERTY_NAME, false);
	}

	protected GenericAssembly addAssembly(IBNAModel model, ObjRef objRef, ObjRef[] relativeAncestorRefs){

		GenericAssembly assembly = new GenericAssembly(model, parentThing, kind);

		BoxAssembly box = new BoxAssembly(model, assembly.getRootThing(), kind);
		box.getBoxThing().setColor(new RGB(245, 245, 245));

		SplineAssembly dashline = new SplineAssembly(model, parentThing, kind);
		dashline.getSplineThing().setLineStyle(SWT.LINE_DASH);

		BoxGlassThing boxGlassThing = new BoxGlassThing();
		boxGlassThing.setMinBoundingBoxSize(new Point(125, 200));
		model.addThing(boxGlassThing, assembly.getRootThing());

		UserEditableUtils.addEditableQuality(boxGlassThing, IHasMutableSelected.USER_MAY_SELECT, IRelativeMovable.USER_MAY_MOVE, IHasMutableBoundingBox.USER_MAY_RESIZE);
		UserEditableUtils.addEditableQuality(box.getBoxedLabelThing(), IHasMutableText.USER_MAY_EDIT_TEXT);

		assembly.markPart("label", box.getBoxedLabelThing());
		assembly.markPart("dashline", dashline);
		assembly.markPart("glass", boxGlassThing);

		//MirrorBoundingBoxLogic.mirrorBoundingBox(boxGlassThing, new Rectangle(0, size * 3, 0, -size * 3), boxedLabelThing);
		MoveWithLogic.moveWith(boxGlassThing, MoveWithLogic.TRACK_BOUNDING_BOX_ONLY, box.getBoxGlassThing());
		MoveWithLogic.moveWith(boxGlassThing, MoveWithLogic.TRACK_BOUNDING_BOX_ONLY, dashline.getSplineGlassThing());

		Rectangle rect = ArchipelagoUtils.findOpenSpotForNewThing(model, 125, 50);	
		boxGlassThing.setBoundingBox(new Rectangle(rect.x, rect.y, 125, 350));
		box.getBoxGlassThing().setBoundingBox(rect);
		dashline.getSplineGlassThing().setEndpoint1(new Point(rect.x+rect.width/2, rect.y+rect.height));
		dashline.getSplineGlassThing().setEndpoint2(new Point(rect.x+rect.width/2, rect.y+rect.height + 300));

		return assembly;
	}

}
