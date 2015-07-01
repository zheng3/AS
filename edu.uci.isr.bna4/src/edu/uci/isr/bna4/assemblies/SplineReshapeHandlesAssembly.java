package edu.uci.isr.bna4.assemblies;

import java.util.ArrayList;
import java.util.List;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.UserEditableUtils;
import edu.uci.isr.bna4.facets.IRelativeMovable;
import edu.uci.isr.widgets.swt.constants.Orientation;

public class SplineReshapeHandlesAssembly
	extends AbstractAssembly{

	public SplineReshapeHandlesAssembly(IBNAModel model, IThing parentThing, Object assemblyKind, int handleCount){
		super(model, parentThing, assemblyKind);

		// Create sub-things
		for(int i = 0; i < handleCount; i++){
			ReshapeHandleAssembly rh = new ReshapeHandleAssembly(model, rootThing, null);
			rh.getReshapeHandleGlassThing().setOrientation(Orientation.NONE);
			rh.getReshapeHandleThing().setOrientation(Orientation.NONE);
			UserEditableUtils.addEditableQuality(rh.getReshapeHandleGlassThing(), IRelativeMovable.USER_MAY_MOVE);
			markPart("" + i, rh.getRootThing());
		}
	}

	public ReshapeHandleAssembly[] getAllReshapeHandleAssemblies(){
		List<ReshapeHandleAssembly> reshapeHandleAssemblies = new ArrayList<ReshapeHandleAssembly>();
		int handleCount = 0;
		IThing rt;
		while((rt = getPart("" + handleCount++)) != null){
			reshapeHandleAssemblies.add((ReshapeHandleAssembly)AssemblyUtils.getAssemblyWithRoot(rt));
		}
		return reshapeHandleAssemblies.toArray(new ReshapeHandleAssembly[reshapeHandleAssemblies.size()]);
	}
}
