package edu.uci.isr.archstudio4.comp.pruner;

/**
 *  This exception should be thrown whenever an element is expected but does not exist.  
 *
 *	@author Christopher Van der Westhuizen
 */
public class MissingElementException extends Exception implements java.io.Serializable
{
	/**
	 * Constructs a new exception with null as its detail message.
	 */
	MissingElementException() {}
	
	/**
	 * Constructs a new exception with the specified detail message.
	 *
	 * @param msg the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	MissingElementException(String msg)
	{
		super(msg);
	}
}