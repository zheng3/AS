package edu.uci.isr.xarchflat;

public interface IXArchInstanceMetadata{

	/**
	 * Returns the current context in which the element was last created in, 
	 * recontextualized, or promoted to, or <code>null</code> if none exists.
	 * @return Returns the current context of the element, or <code>null</code> if none exists.
	 */
	public String getCurrentContext();

}