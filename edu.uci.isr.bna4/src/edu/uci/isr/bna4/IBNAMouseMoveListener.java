package edu.uci.isr.bna4;

import org.eclipse.swt.events.MouseEvent;

public interface IBNAMouseMoveListener{

	//Mouse move events
	public void mouseMove(IBNAView view, MouseEvent e, IThing t, int worldX, int worldY);

}