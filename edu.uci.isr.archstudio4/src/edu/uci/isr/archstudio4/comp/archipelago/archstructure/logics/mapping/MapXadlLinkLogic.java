package edu.uci.isr.archstudio4.comp.archipelago.archstructure.logics.mapping;

import edu.uci.isr.archstudio4.comp.archipelago.generic.logics.mapping.MapXadlNondirectionalSplineLogic;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.logics.tracking.ThingPropertyTrackingLogic;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public class MapXadlLinkLogic
	extends MapXadlNondirectionalSplineLogic{

	public MapXadlLinkLogic(XArchFlatInterface xarch, ObjRef rootObjRef, String relativePath, ThingPropertyTrackingLogic tptl, IThing parentThing, Object kind){
		super(xarch, rootObjRef, relativePath, tptl, parentThing, kind);
	}
}
