package edu.uci.isr.bna4.facets;

public interface IHasGradientFill extends IHasColor, IHasSecondaryColor{
	public static final String GRADIENT_FILLED_PROPERTY_NAME = "gradientFilled";
	
	public boolean isGradientFilled(); 
}
