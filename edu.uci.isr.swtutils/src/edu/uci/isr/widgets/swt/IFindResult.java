package edu.uci.isr.widgets.swt;

import org.eclipse.jface.resource.ImageDescriptor;

public interface IFindResult extends Comparable{
	public String getString();
	public ImageDescriptor getImageDescriptor();
	public Object getData();
}
