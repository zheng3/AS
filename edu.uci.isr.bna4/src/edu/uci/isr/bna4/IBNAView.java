package edu.uci.isr.bna4;

import org.eclipse.swt.widgets.Composite;

public interface IBNAView{

	/**
	 * Setting this property value on a IThing to true makes the view ignore
	 * that thing in terms of mouse events. This allows the things to be treated
	 * like background items on the canvas.
	 */
	public static final String BACKGROUND_THING_PROPERTY_NAME = "backgroundThing";

	/**
	 * Setting this property value on a IThing to true makes the view not paint
	 * the things peer.
	 */
	public static final String HIDE_THING_PROPERTY_NAME = "hideThing";

	public void setParentComposite(Composite parent);

	public Composite getParentComposite();

	public IBNAView getParentView();

	public IBNAWorld getWorld();

	public ICoordinateMapper getCoordinateMapper();

	public IThing getThingAt(int lx, int ly);

	public IThingPeer createPeer(IThing th);

	public IThingPeer getPeer(IThing th);
}
