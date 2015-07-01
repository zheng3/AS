package edu.uci.isr.archstudio4.comp.versionpruner;

/**
 * This exception is thrown whenever a provided URL is invalid.  This could be the case when, for example,
 * a provided URL does not point to an open xArch document.  
 *
 * @author Christopher Van der Westhuizen
 */

public class InvalidURIException extends Exception implements java.io.Serializable
{
	/**
	 * Constructs a new exception with null as its detail message.
	 */
	public InvalidURIException() {}
	
	/**
	 * Constructs a new exception with the specified detail message.
     *
     * @param msg the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public InvalidURIException(String msg)
	{
		super(msg);
	}
}