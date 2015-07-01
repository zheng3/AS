package edu.uci.isr.bna4;

public interface IThingLogicManager{

	public void destroy();

	public void addThingLogic(IThingLogic tl);

	public void removeThingLogic(IThingLogic tl);

	public IThingLogic[] getAllThingLogics();

	public <T>T[] getThingLogics(Class<T> implementingInterface);

	public void addThingLogicManagerListener(IThingLogicManagerListener l);

	public void removeThingLogicManagerListener(IThingLogicManagerListener l);
}
