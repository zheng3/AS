package edu.uci.isr.bna4;

public interface IBNAWorld{

	public void destroy();

	public boolean isDestroyed();

	public String getID();

	public IBNAModel getBNAModel();

	public IThingLogicManager getThingLogicManager();

}
