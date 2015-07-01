package edu.uci.isr.bna4;

import org.eclipse.jface.action.IMenuManager;

public interface IBNAMenuListener{

	//Menu events
	public void fillMenu(IBNAView view, IMenuManager m, int localX, int localY, IThing t, int worldX, int worldY);

}