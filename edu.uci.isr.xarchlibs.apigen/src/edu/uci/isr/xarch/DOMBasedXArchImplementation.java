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

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.uci.isr.xarch.instance.InstanceConstants;

/**
 * This is the <code>IXArchImplementation</code> class that corresponds to the
 * DOM-based data binding library. Documents created and manipulated by this
 * implementation object will use an in-memory DOM model as their data store.
 * 
 * @author Eric M. Dashofy <a
 *         href="mailto:edashofy@ics.uci.edu">edashofy@ics.uci.edu</a>
 */
public class DOMBasedXArchImplementation
	implements IXArchImplementation{

	private static XArchContextCache contextCache = new XArchContextCache();

	/**
	 * Gets the DOMImplementation for the preferred parser.
	 * 
	 * @return DOMImplementation object for the preferred parser.
	 */
	public static synchronized DOMImplementation getDOMImplementation(){
		//return DOMImplementationImpl.getDOMImplementation();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setValidating(false);
		dbf.setIgnoringElementContentWhitespace(true);
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			return db.getDOMImplementation();
		}
		catch(ParserConfigurationException pce){
			pce.printStackTrace();
			return null;
		}
	}

	/**
	 * Creates a DOM-based XArch document.
	 * 
	 * @return Top-level element for new document.
	 */
	public synchronized IXArch createXArch(){
		DOMImplementation dom = getDOMImplementation();
		DocumentType docType = dom.createDocumentType("FooDocType", "", "");
		Document newDoc = dom.createDocument(InstanceConstants.NS_URI, "instance:xArch", docType);
		//Document newDoc = dom.createDocument(null, "xArch", docType);
		return new XArchImpl(newDoc);
	}

	/**
	 * Creates a DOM-based XArch document. Currently, the parameters passed are
	 * unused.
	 * 
	 * @param params
	 *            (unused).
	 * @return Top-level element for new document.
	 */
	public synchronized IXArch createXArch(Object params){
		return createXArch();
	}

	public synchronized void remove(IXArch xArch){
		contextCache.removeAll(xArch);
	}

	/**
	 * Creates a new DOM-based context object for a given document and context
	 * type.
	 * 
	 * @param xArch
	 *            Document for which to create the context object.
	 * @param contextType
	 *            The type of context to create. For example, if the schema is
	 *            called "types.xsd" then this should be "types".
	 * @return a context object.
	 * @throws IllegalArgumentException
	 *             if the context type is invalid.
	 */
	public synchronized IXArchContext createContext(IXArch xArch, String contextType){
		IXArchContext existingContext = contextCache.get(xArch, contextType);
		if(existingContext != null){
			return existingContext;
		}

		String className = "edu.uci.isr.xarch." + XArchUtils.uncapFirstLetter(contextType) + "." + XArchUtils.capFirstLetter(contextType) + "Context";

		try{
			Class c = Class.forName(className);
			Constructor constructor = c.getConstructor(new Class[]{edu.uci.isr.xarch.IXArch.class});
			IXArchContext context = (IXArchContext)constructor.newInstance(new Object[]{xArch});
			contextCache.put(xArch, contextType, context);
			return context;
		}

		catch(IllegalAccessException iae){
			throw new RuntimeException(iae.toString());
		}
		catch(InstantiationException ie){
			throw new RuntimeException(ie.toString());
		}
		//		catch(NoSuchMethodException nsme){
		//			throw new InvalidOperationException("[init](IXArch)");
		//		}
		catch(NoSuchMethodException nsme){
			throw new IllegalArgumentException("Invalid context name (invalid [init](IXArch)): " + contextType);
		}
		catch(ClassNotFoundException cnfe){
			throw new IllegalArgumentException("Invalid context name: " + contextType);
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException(ite.getTargetException());
		}
	}

	/**
	 * Clone a DOM-based xArch document.
	 * 
	 * @param xArch
	 *            Top-level element of the document to clone.
	 * @return Top-level element of cloned document.
	 * @throws IllegalArgumentException
	 *             if the xArch document is not DOM-based.
	 */
	public synchronized IXArch cloneXArch(IXArch xArch){
		if(!(xArch instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Node docRootElt = ((DOMBased)xArch).getDOMNode();
		Document doc = docRootElt.getOwnerDocument();
		Document clonedDoc = (Document)doc.cloneNode(true);
		//return createXArch(clonedDoc);
		return new XArchImpl(clonedDoc);
	}

	/**
	 * Clone a DOM-based xArch document. Currently, the parameters passed are
	 * unused.
	 * 
	 * @param xArch
	 *            Top-level element of the document to clone.
	 * @param params
	 *            (unused).
	 * @return Top-level element of cloned document.
	 * @throws IllegalArgumentException
	 *             if the xArch document is not DOM-based.
	 */
	public synchronized IXArch cloneXArch(IXArch xArch, Object params){
		return cloneXArch(xArch);
	}

	private synchronized static Document parseToDocument(java.io.Reader r) throws SAXException, IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setValidating(false);
		dbf.setIgnoringElementContentWhitespace(true);
		try{
			StringReplacerReader srr = new StringReplacerReader(r, XArchConstants.OLD_XSI_NS_URI, XArchConstants.XSI_NS_URI);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(srr));
			return doc;
		}
		catch(ParserConfigurationException pce){
			pce.printStackTrace();
			return null;
		}
		/*
		 * DOMParser parser = new DOMParser();
		 * parser.setIncludeIgnorableWhitespace(false); InputSource is = new
		 * InputSource(r); parser.parse(is); Document document =
		 * parser.getDocument(); return document;
		 */
	}

	/**
	 * Parse a document into data bindings. Valid sources for this
	 * implementation include a stream (<code>java.io.Reader</code>) or a
	 * DOM <code>Document</code>.
	 * 
	 * @param documentSource
	 *            The data source from which to parse.
	 * @return Top-level element of document.
	 * @throws XArchParseException
	 *             if parsing failed for some reason. In the case of a
	 *             <code>Reader</code> the chained exception will either be a
	 *             <code>SAXException</code> or <code>IOException</code>.
	 * @throws IllegalArgumentException
	 *             if the document source was not supported.
	 */
	public synchronized IXArch parse(Object documentSource) throws XArchParseException{
		if(documentSource == null){
			throw new IllegalArgumentException("Can't parse from null documentSource");
		}
		else if(documentSource instanceof java.io.Reader){
			try{
				Reader r = (Reader)documentSource;
				Document doc = parseToDocument(r);
				return new XArchImpl(doc);
			}
			catch(SAXException se){
				throw new XArchParseException(se);
			}
			catch(IOException ioe){
				throw new XArchParseException(ioe);
			}
		}
		else if(documentSource instanceof Document){
			return new XArchImpl((Document)documentSource);
		}
		else{
			throw new IllegalArgumentException("This implementation cannot parse from source type " + documentSource.getClass().toString());
		}
	}

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
	public boolean isContainedIn(IXArch xArch, IXArchElement elt){
		if(!(xArch instanceof DOMBased)){
			throw new IllegalArgumentException("xArch object must be DOM-based.");
		}
		if(!(elt instanceof DOMBased)){
			throw new IllegalArgumentException("xArch element must be DOM-based.");
		}
		Node xArchNode = ((DOMBased)xArch).getDOMNode();
		Node eltNode = ((DOMBased)elt).getDOMNode();
		synchronized(DOMUtils.getDOMLock(eltNode)){
			while(eltNode != null){
				if(eltNode == xArchNode){
					return true;
				}
				eltNode = eltNode.getParentNode();
			}
			return false;
		}
	}

	/**
	 * Returns a nice-looking representation of the given XML document.
	 * Sub-elements are properly indented, and long lines are indent-wrapped
	 * nicely, too.
	 * 
	 * @param xArch
	 *            Document to turn into a string.
	 * @return String representation of the document, prettyprinted.
	 */
	private static synchronized String getPrettyXmlRepresentation(IXArch xArch) throws DOMException{
		if(!(xArch instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Node docRootElt = ((DOMBased)xArch).getDOMNode();
		Document doc = docRootElt.getOwnerDocument();

		XArchUtils.fixNamespaces(doc);

		try{
			StringWriter sw = new StringWriter();

			Source domSource = new javax.xml.transform.dom.DOMSource(doc);
			Result streamResult = new javax.xml.transform.stream.StreamResult(sw);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			t.transform(domSource, streamResult);

			//OutputFormat of = new OutputFormat(doc, "UTF-8", true);
			//of.setOmitDocumentType(true);
			//of.setIndenting(true);
			//of.setLineWidth(120);
			//OutputFormat of = new OutputFormat(org.apache.xml.serialize.Method.XML, "UTF-8", true);
			//of.setOmitDocumentType(true);
			//of.setLineWidth(80);
			//sof.setPreserveSpace(true);
			//XMLSerializer xmlSerializer = new XMLSerializer(sw, of);
			//TextSerializer xmlSerializer = new TextSerializer();
			//xmlSerializer.setOutputFormat(of);

			//xmlSerializer.serialize(doc);
			return sw.toString();
		}
		catch(Exception e){
			System.err.println("This shouldn't happen.");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Serializes a DOM-based xArch document into XML. Params are ignored in
	 * this implementation but may be supported in future versions.
	 * 
	 * @param xArch
	 *            Top-level element of document to serialize.
	 * @param params
	 *            (ignored)
	 * @return String representation of the given document in XML format.
	 * @throws XArchSerializeException
	 *             if the serialization failed. In this implementation, the
	 *             chained exception is generally a <code>DOMException</code>.
	 */
	public synchronized String serialize(IXArch xArch, Object params) throws XArchSerializeException{
		try{
			String serDoc = getPrettyXmlRepresentation(xArch);
			return serDoc;
		}
		catch(DOMException de){
			throw new XArchSerializeException(de);
		}
	}
}
