package edu.uci.isr.xarchflat;

public interface IXArchActionMetadata{

	/**
	 * <code>CREATE</code> indicates that this describes a create action for a context.
	 */
	public static final int CREATE = 0x01 << 0;

	/**
	 * <code>CREATE_ELEMENT</code> indicates that this describes a create element action for a context.
	 */
	public static final int CREATE_ELEMENT = 0x01 << 1;

	/**
	 * <code>PROMOTE</code> indicates that this describes a promote action for a context.
	 */
	public static final int PROMOTE = 0x01 << 2;

	/**
	 * <code>RECONTEXTUALIZE</code> indicates that this describes a recontextualize action for a context.
	 */
	public static final int RECONTEXTUALIZE = 0x01 << 3;

	/**
	 * Returns the type of this action.
	 * @return the type of this action
	 * @see #CREATE
	 * @see #CREATE_ELEMENT
	 * @see #PROMOTE
	 * @see #RECONTEXTUALIZE
	 */
	public int getMetadataType();

	/**
	 * Returns metadata describing the input type used by this action, or <code>null</code> if it does not require one.
	 * @return metadata describing the input type used by this action, or <code>null</code> if it does not require one
	 */
	public String getInputType();

	/**
	 * Returns metadata describing the output type resulting from this action.
	 * @return metadata describing the output type resulting from this action
	 */
	public String getOutputType();

}