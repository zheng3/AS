package edu.uci.isr.bna4.logics.hints;

import edu.uci.isr.bna4.IBNAWorld;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.assemblies.IAssembly;

public interface IHintSynchronizer{

	public void setBNAWorld(IBNAWorld world);

	public void restoreHints(IHintRepository repository, Object context, String partPath, String[] parts, IThing thing);

	public void thingChanged(IHintRepository repository, Object context, String partPath, String[] parts, IThing thing, Object propertyName, Object oldValue, Object newValue);

	public void repositoryChanged(IHintRepository repository, Object context, IAssembly[] assemblies, String hintName);
}
