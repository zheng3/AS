package edu.uci.isr.bna4.logics.hints;

import edu.uci.isr.bna4.IBNAWorld;
import edu.uci.isr.bna4.assemblies.IAssembly;

public interface IHintRepository{

	public Object getContextForAssembly(IBNAWorld world, IAssembly assembly);

	public IAssembly[] getAssembliesForContext(IBNAWorld world, Object context);

	public String[] getStoredHintNames(Object context);

	public void storeHint(Object context, String hintName, Object hintValue);

	public Object getHint(Object context, String hintName);

	public void addHintRepositoryChangeListener(IHintRepositoryChangeListener l);

	public void removeHintRepositoryChangeListener(IHintRepositoryChangeListener l);
}
