package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.assemblies.IAssembly;

public interface IHasMutableAssemblyData
	extends IHasAssemblyData{

	public void setAssembly(IAssembly assembly);

	public void setAssemblyKind(Object kind);
}
