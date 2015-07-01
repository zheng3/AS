package edu.uci.isr.bna4;

public interface IMutableCoordinateMapper extends ICoordinateMapper{

	public void repositionRelative(int dx, int dy);
	public void repositionAbsolute(int x, int y);
	public void rescaleAbsolute(double newScale);
	public void rescaleRelative(double ds);

}