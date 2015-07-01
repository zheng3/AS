package edu.uci.isr.bna4.assemblies;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.UserEditableUtils;
import edu.uci.isr.bna4.facets.IRelativeMovable;
import edu.uci.isr.widgets.swt.constants.Orientation;

public class BoxReshapeHandlesAssembly
	extends AbstractAssembly{

	public BoxReshapeHandlesAssembly(IBNAModel model, IThing parentThing, Object assemblyKind){
		super(model, parentThing, assemblyKind);

		// Create sub-things
		for(Orientation o: EnumSet.allOf(Orientation.class)){
			if(!o.equals(Orientation.NONE)){
				ReshapeHandleAssembly rh = new ReshapeHandleAssembly(model, rootThing, null);
				rh.getReshapeHandleGlassThing().setOrientation(o);
				rh.getReshapeHandleThing().setOrientation(o);
				UserEditableUtils.addEditableQuality(rh.getReshapeHandleGlassThing(), IRelativeMovable.USER_MAY_MOVE);
				markPart(o.name(), rh.getRootThing());
			}
		}
	}

	public ReshapeHandleAssembly getReshapeHandleAssembly(Orientation o){
		return AssemblyUtils.getAssemblyWithRoot(getPart(o.name()));
	}

	public ReshapeHandleAssembly[] getAllReshapeHandleAssemblies(){
		EnumSet<Orientation> orientations = EnumSet.allOf(Orientation.class);
		List<ReshapeHandleAssembly> reshapeHandleAssemblies = new ArrayList<ReshapeHandleAssembly>();
		for(Orientation o: orientations){
			ReshapeHandleAssembly reshapeHandleAssembly = getReshapeHandleAssembly(o);
			if(reshapeHandleAssembly != null){
				reshapeHandleAssemblies.add(reshapeHandleAssembly);
			}
		}
		return reshapeHandleAssemblies.toArray(new ReshapeHandleAssembly[reshapeHandleAssemblies.size()]);
	}
}
