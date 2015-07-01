/*
 * Copyright (c) 2000-2005 Regents of the University of California.
 * All rights reserved.
 *
 * This software was developed at the University of California, Irvine.
 *
 * Redistribution and use in source and binary forms are permitted
 * provided that the above copyright notice and this paragraph are
 * duplicated in all such forms and that any documentation,
 * advertising materials, and other materials related to such
 * distribution and use acknowledge that the software was developed
 * by the University of California, Irvine.  The name of the
 * University may not be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package edu.uci.isr.xarch;

/**
 * This interface is implemented by classes that are entrypoint classes for an
 * xArch-based data binding library. The primary purpose of implementations of
 * this interface is to serve as a factory objects for top-level document
 * objects (<code>IXArch</code> objects) and to provide implementations for
 * implementation-dependent utility methods that don't fit anywhere else. The
 * Apigen-generated data binding library is DOM-based, and this interface is
 * implemented by the <code>DOMBasedXArchImplementation</code> class. Other
 * data binding library implementations will have different implementations of
 * this interface.
 * 
 * @author Eric M. Dashofy <a
 *         href="mailto:edashofy@ics.uci.edu">edashofy@ics.uci.edu</a>
 */
public interface IXArchImplementation{

	/**
	 * Create a new xArch-based document.
	 * 
	 * @return Top-level data binding <code>IXArch</code> corresponding to a
	 *         newly created document.
	 */
	public IXArch createXArch();

	/**
	 * Create a new xArch-based document, allowing some creation parameters to
	 * be passed to the implementation. Allowable parameters are
	 * implementation-dependent.
	 * 
	 * @param params
	 *            Object containing parameters to use when creating the new
	 *            document.
	 * @return Top-level data binding <code>IXArch</code> corresponding to a
	 *         newly created document.
	 */
	public IXArch createXArch(Object params);

	/**
	 * Creates a context object that can be used for creating,
	 * recontextualizing, and promoting elements in that context (XML
	 * namespace).
	 * 
	 * @param xArch
	 *            The top-level element in the document in which the context
	 *            should be created.
	 * @param contextType
	 *            The name of the context to create. If the context object
	 *            desired is, say, <CODE>TypesContext</CODE> then the
	 *            <CODE>contextType</CODE> parameter will be
	 *            <CODE>"types"</CODE>.
	 * @return A new context object implementing the the given type interface.
	 *         The resulting object must be typecast to the appropriate
	 *         interface. If the context <I>object</I> desired was, say,
	 *         <CODE>TypesContext</CODE> then the result must be typcast to
	 *         the <CODE>ITypesContext</CODE> <I>interface</I>.
	 */
	public IXArchContext createContext(IXArch xArch, String contextType);

	/**
	 * Clones a document.
	 * 
	 * @param xArch
	 *            The top-level element of the document to clone.
	 * @return A new top-level element corresponding to the cloned document.
	 */
	public IXArch cloneXArch(IXArch xArch);

	/**
	 * Clones a document, allowing the user to pass some parameters to the
	 * implementation. The acceptable parameters are implementation-dependent.
	 * 
	 * @param xArch
	 *            The top-level element of the document to clone.
	 * @param params
	 *            Parameters to use when cloning.
	 * @return A new top-level element corresponding to the cloned document.
	 */
	public IXArch cloneXArch(IXArch xArch, Object params);

	/**
	 * Determines if a given IXArchElement is contained (a child of) a given
	 * IXArch.
	 * 
	 * @param xArch
	 *            <code>IXArch</code> to check for the element.
	 * @param elt
	 *            Element to check for parentage.
	 * @return <code>true</code> if the element is attached,
	 *         <code>false</code> otherwise.
	 */
	public boolean isContainedIn(IXArch xArch, IXArchElement elt);

	/**
	 * Parses a document from a given source. The types of source that are
	 * acceptable to this function are implementation-dependent (e.g., Files,
	 * Strings, Readers, InputStreams, etc.). This method should throw a
	 * <code>java.lang.IllegalArgumentException</code> if the source type is
	 * invalid, and a <code>XArchParseException</code> if the source type is
	 * valid, but the parsing failed.
	 * 
	 * @param documentSource
	 *            The source from which to parse a document into a set of data
	 *            bindings. The acceptable source types are
	 *            implementation-dependent.
	 * @return <code>IXArch</code> corresponding to the top-level element of
	 *         the parsed document.
	 * @throws IllegalArgumentException
	 *             if the type of source is invalid.
	 * @throws XArchParseException
	 *             if the source is valid but the parsing fails.
	 */
	public IXArch parse(Object documentSource) throws XArchParseException;

	/**
	 * Serializes a document into a string format. Errors are reported via an
	 * <code>XArchSerializeException</code>.
	 * 
	 * @param xArch
	 *            Object corresponding to the top-level element of the document
	 *            to be serialized.
	 * @param params
	 *            Object containing data about how to serialize the document.
	 *            Object types accepted here are implementation-dependent. If
	 *            the type of the <code>params</code> object is unrecognized,
	 *            this method should throw a
	 *            <code>java.lang.IllegalArgumentException</code>. Some
	 *            implementations may recognize a <code>null</code> parameter.
	 * @return The given document encoded as a string.
	 * @throws XArchSerializeException
	 *             if there was an error during serialization.
	 */
	public String serialize(IXArch xArch, Object params) throws XArchSerializeException;

	/**
	 * Removes any references to the document so that it can be garbage collected.
	 *
	 * @param xArch
	 *            Object corresponding to the top-level element of the document
	 *            to be forgotten.
	 */
	public void remove(IXArch xArch);
}
