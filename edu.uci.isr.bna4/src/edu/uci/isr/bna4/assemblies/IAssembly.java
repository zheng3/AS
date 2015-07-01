package edu.uci.isr.bna4.assemblies;

import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.facets.IHasAssemblyData;

public interface IAssembly{

	public IHasAssemblyData getRootThing();

	public boolean isKind(Object kind);

	public Object getKind();

	public <T extends IThing>T getPart(String name);

	public IThing[] getParts();

	public void markPart(String name, IThing thing);

	public void markPart(String name, IAssembly assembly);

	public void unmarkPart(String name);

	public void remove(boolean andChildren);
}
