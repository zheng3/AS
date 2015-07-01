package edu.uci.isr.archstudio4.comp.archipelago.interactions.logics.mapping;

import org.eclipse.swt.graphics.Point;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoUtils;
import edu.uci.isr.archstudio4.comp.archipelago.generic.logics.mapping.AbstractAutomapSingleAssemblyXArchRelativePathMappingLogic;
import edu.uci.isr.archstudio4.comp.archipelago.generic.logics.mapping.MapXadlDirectionalSplineLogic;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.UserEditableUtils;
import edu.uci.isr.bna4.assemblies.LabeledSplineAssembly;
import edu.uci.isr.bna4.assemblies.SplineAssembly;
import edu.uci.isr.bna4.constants.ArrowheadShape;
import edu.uci.isr.bna4.facets.IHasEndpoints;
import edu.uci.isr.bna4.facets.IHasMutableEndpoints;
import edu.uci.isr.bna4.facets.IHasMutableMidpoints;
import edu.uci.isr.bna4.facets.IHasMutableSelected;
import edu.uci.isr.bna4.facets.IHasMutableText;
import edu.uci.isr.bna4.facets.IHasText;
import edu.uci.isr.bna4.facets.IRelativeMovable;
import edu.uci.isr.bna4.logics.information.ToolTipLogic;
import edu.uci.isr.bna4.logics.tracking.ThingPropertyTrackingLogic;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public class MapXadlMessageLogic
	extends
	AbstractAutomapSingleAssemblyXArchRelativePathMappingLogic<LabeledSplineAssembly>{

	public MapXadlMessageLogic(XArchFlatInterface xarch, ObjRef rootObjRef, String relativePath, ThingPropertyTrackingLogic tptl, IThing parentThing, Object kind){
		super(xarch, rootObjRef, relativePath, tptl, LabeledSplineAssembly.class, parentThing, kind);
		automapSimpleValue("description", "[no description]", "label", IHasText.TEXT_PROPERTY_NAME, true);
		automapSimpleValue("description", "[no description]", "glass", ToolTipLogic.TOOL_TIP_PROPERTY_NAME, true);
		automapXLinkToStuckPoint("from", "glass", IHasEndpoints.ENDPOINT_1_PROPERTY_NAME, true);
		automapXLinkToStuckPoint("to", "glass", IHasEndpoints.ENDPOINT_2_PROPERTY_NAME, true);
	}

	@Override
	protected LabeledSplineAssembly addAssembly(IBNAModel model, ObjRef objRef, ObjRef[] relativeAncestorRefs){

		LabeledSplineAssembly assembly = new LabeledSplineAssembly(model, parentThing, kind);
		Point p = ArchipelagoUtils.findOpenSpotForNewThing(model);
		assembly.getSplineGlassThing().setEndpoint1(new Point(p.x - 50, p.y));
		assembly.getSplineGlassThing().setEndpoint2(new Point(p.x + 50, p.y));
		assembly.getEndpoint2ArrowheadThing().setArrowheadShape(ArrowheadShape.WEDGE);

		UserEditableUtils.addEditableQuality(assembly.getSplineGlassThing(), IHasMutableSelected.USER_MAY_SELECT, IRelativeMovable.USER_MAY_MOVE, IHasMutableMidpoints.USER_MAY_ADD_MIDPOINTS, IHasMutableMidpoints.USER_MAY_MOVE_MIDPOINTS, IHasMutableMidpoints.USER_MAY_REMOVE_MIDPOINTS, IHasMutableEndpoints.USER_MAY_MOVE_ENDPOINT1, IHasMutableEndpoints.USER_MAY_MOVE_ENDPOINT2, IHasMutableEndpoints.USER_MAY_RESTICK_ENDPOINT1, IHasMutableEndpoints.USER_MAY_RESTICK_ENDPOINT2);
		UserEditableUtils.addEditableQuality(assembly.getSplineGlassThing(), ToolTipLogic.USER_MAY_EDIT_TOOL_TIP);
		UserEditableUtils.addEditableQuality(assembly.getBoxedLabelThing(), IHasMutableText.USER_MAY_EDIT_TEXT);

		return assembly;
	}
}
