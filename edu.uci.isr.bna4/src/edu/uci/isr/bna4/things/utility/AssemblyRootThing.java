package edu.uci.isr.bna4.things.utility;

import edu.uci.isr.bna4.AbstractThing;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.IThingPeer;
import edu.uci.isr.bna4.assemblies.IAssembly;
import edu.uci.isr.bna4.facets.IHasMutableAssemblyData;

public class AssemblyRootThing
	extends AbstractThing
	implements IThing, IHasMutableAssemblyData{

	public AssemblyRootThing(){
		super(null);
	}

	@Override
	final public Class<? extends IThingPeer> getPeerClass(){
		return NoThingPeer.class;
	}

	public Object getAssemblyKind(){
		return getProperty(ASSEMBLY_KIND_PROPERTY_NAME);
	}

	public void setAssemblyKind(Object assemblyKind){
		setProperty(ASSEMBLY_KIND_PROPERTY_NAME, assemblyKind);
	}

	public IAssembly getAssembly(){
		return getProperty(ASSEMBLY_PROPERTY_NAME);
	}

	public void setAssembly(IAssembly assembly){
		setProperty(ASSEMBLY_PROPERTY_NAME, assembly);
	}
}
