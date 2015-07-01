// Ping Chen
// NoSuchVariableException.java

package edu.uci.isr.archstudio4.comp.booleaneval;

/**
 *
 * NoSuchVariableException is thrown when the program tries to access a variable
 * that is not in the symbol table.  Only thrown for certain functions 
 */
public class NoSuchVariableException extends Exception
{
    public NoSuchVariableException( String msg )
    {
        super( msg );
    }
}
