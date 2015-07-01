package edu.uci.isr.bna4.facets;

import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.assemblies.IAssembly;

public interface IHasAssemblyData
	extends IThing{

	public static final String ASSEMBLY_PROPERTY_NAME = "assembly";
	public static final String ASSEMBLY_KIND_PROPERTY_NAME = "assemblyKind";

	public IAssembly getAssembly();

	public Object getAssemblyKind();
}
