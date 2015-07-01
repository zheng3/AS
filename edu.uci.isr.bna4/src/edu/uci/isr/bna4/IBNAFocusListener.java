package edu.uci.isr.bna4;

import org.eclipse.swt.events.FocusEvent;

public interface IBNAFocusListener{

	//Focus Events
	public void focusGained(IBNAView view, FocusEvent e);

	public void focusLost(IBNAView view, FocusEvent e);

}