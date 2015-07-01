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

import edu.uci.isr.xarch.*;
import edu.uci.isr.xarch.instance.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;

//import org.apache.xml.serialize.*;
//import org.apache.xerces.*;
//import org.apache.xerces.dom.*;
//import org.apache.xerces.parsers.*;

/**
 * Utility functions for xArch API implementations.
 * This class is the only one that is XML-parser-dependent.
 *
 * @author Eric M. Dashofy (edashofy@ics.uci.edu)
 */
public class XArchUtils{

	private static IXArchImplementation xArchImplementation = new DOMBasedXArchImplementation();
	private static XArchContextCache contextCache = new XArchContextCache();
	
	/**
	 * Returns the default (i.e. DOM-based) implementation of the xArch
	 * data bindings.  This is the entrypoint to all other library APIs.
	 * @return Default DOM-based xArch data binding implementation.
	 */
	public static IXArchImplementation getDefaultXArchImplementation(){
		return xArchImplementation;
	}
	
	/** 
	 * DEPRECATED: Gets the DOMImplementation for the preferred parser.
	 * 
	 * @deprecated No public replacement.
	 * 
	 * @return DOMImplementation object for the preferred parser.
	 */
	public synchronized static DOMImplementation getDOMImplementation(){
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
	 * Fixes the namespace prefixes in a document
	 * before it is serialized.  This is here because
	 * the Xerces serializers, as of 1.2.1, are badly broken
	 * and do not do this themselves.
	 * @param doc Document on which to fix namespaces.
	 */
	protected static void fixNamespaces(Document doc){
		synchronized(DOMUtils.getDOMLock(doc)){
			//This is in here because Xerces serializers (as of 1.2.1)
			//are nastily broken and don't do this themselves.
			
			Hashtable prefixMap = new Hashtable();
			prefixMap.put(XArchConstants.XMLNS_NS_URI, "xmlns");
			prefixMap.put(XArchConstants.XSI_NS_URI, "xsi");
			prefixMap.put(InstanceConstants.NS_URI, "instance");
			
			Element docElt = doc.getDocumentElement();
			
			NamedNodeMap attributes = docElt.getAttributes();
			if(attributes != null){
				for(int i = 0; i < attributes.getLength(); i++){
					Node n = attributes.item(i);
					if(n != null){
						String prefix = n.getPrefix();
						if(prefix != null){
							if(prefix.equals("xmlns")){
								String internalPrefix = n.getLocalName();
								String internalURI = n.getNodeValue();
								prefixMap.put(internalURI, internalPrefix);
							}
						}
					}
				}
			}
			
			//System.out.println("Prefix map is: " + prefixMap);
			
			docElt.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi",
				XArchConstants.XSI_NS_URI);
			
			fixNode(doc, prefixMap);
			//docElt.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns",
			//	InstanceConstants.NS_URI);
			docElt.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:instance",
				InstanceConstants.NS_URI);
		}
	}

	private static Set noPrefixNS = new HashSet();
	
	static {
		try{
			InputStream is = XArchUtils.class.getClassLoader().getResourceAsStream("edu/uci/isr/xarch/noprefix.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while(true){
				String line = br.readLine();
				if(line == null){
					br.close();
					is.close();
				}
				else{
					line = line.trim();
					if(!line.equals("")){
						noPrefixNS.add(line);
					}
				}
			}
		}
		catch(Exception e){
			// Just ignore any error
			// e.printStackTrace();
		}
	}

	/**
	 * Whether a node with a namespace should be prefixed when serialized out
	 * 
	 * @param namespace the namespace of the node to check
	 * @return true if the namespace should be prefixed to the node for 
	 * serialization out, false otherwise
	 */
	protected static boolean noPrefix(String namespace) {
		if (noPrefixNS.contains(namespace)) 
			return true;
		else
			return false;
	}
	
	/**
	 * Fixes a specific node.  See <code>fixNamespaces</code>
	 * @param n Node to fix.
	 * @param prefixMap Prefix<->Namespace URI mappings.
 	 */
	protected static void fixNode(Node n, Hashtable prefixMap){
		synchronized(DOMUtils.getDOMLock(n)){
			//Preorder traversal
			if(n == null){
				return;
			}
			
			String	nodeNS = n.getNamespaceURI();
			if (nodeNS != null ) {
				if ( noPrefix(nodeNS) ) {
					return;
				}
			}
				
			//Traverse attributes
			NamedNodeMap nnm = n.getAttributes();
			if(nnm != null){
				for(int i = 0; i < nnm.getLength(); i++){
					Attr nnmItem = (Attr)nnm.item(i);
					
					if(nnmItem != null){
						Document doc = nnmItem.getOwnerDocument();
						if(doc == null){
							Element parentElt = nnmItem.getOwnerElement();
							if(parentElt != null){
								doc = parentElt.getOwnerDocument();
							}
						}
						Element docElt = doc.getDocumentElement();
						if(docElt != nnmItem.getOwnerElement()){
							String namespaceURI = nnmItem.getNamespaceURI();
							if((namespaceURI != null) && (namespaceURI.equals(XArchConstants.XSI_NS_URI))){
								if(nnmItem.getLocalName().equals("type")){
									fixXsiTypeAttribute(nnmItem, prefixMap);
								}
							}
							fixAttribute(nnmItem, prefixMap);
						}
					}
				}
			}
			
			if(n.hasChildNodes()){
				NodeList nl = n.getChildNodes();
				for(int i = 0; i < nl.getLength(); i++){
					fixNode(nl.item(i), prefixMap);
				}
			}
			
			if(n.getNodeType() == Node.ELEMENT_NODE){
				fixElement((Element)n, prefixMap);
			}
		}
	}
	
	
	/**
	 * Fixes a specific element.  See <code>fixNamespaces</code>
	 * @param elt Element to fix.
	 * @param prefixMap Prefix<->Namespace URI mappings.
 	 */
	protected static void fixElement(Element elt, Hashtable prefixMap){
		synchronized(DOMUtils.getDOMLock(elt)){
			if(elt == null){
				return;
			}
			
			String nsURI = elt.getNamespaceURI();
			if(nsURI != null){
				//Get the element's current prefix.
				String nsPrefix = elt.getPrefix();
				if(nsPrefix != null){
					//Whoops, it already has a prefix.
					//Is this the first time we've seen this namespace URI?
					String alreadyPrefix = (String)prefixMap.get(nsURI);
					if(alreadyPrefix == null){
						//It is.  Well, this is as good a prefix as any.
						//Store it in the prefix map and add it to the
						//document.
						prefixMap.put(nsURI, nsPrefix);
						Document doc = elt.getOwnerDocument();
						Element docElt = doc.getDocumentElement();
						docElt.setAttributeNS(XArchConstants.XMLNS_NS_URI, "xmlns:" + nsPrefix,
							nsURI);
					}
					else{
						//Okay, so we have a conflict--two prefixes for
						//the same namespace.  Let's change this prefix
						//so it matches the one we've already established.
						//That one's already in the document, so
						//we don't have to change the document.
						elt.setPrefix(alreadyPrefix);
					}
				}
				else{
					//The element doesn't have a prefix.
					//Have we established one for it yet?
					String alreadyPrefix = (String)prefixMap.get(nsURI);
					if(alreadyPrefix == null){
						//We haven't.  Let's create one and add it to the 
						//document.
						
						//Generate new prefix
						String newPrefix = getReasonablePrefix(nsURI, prefixMap);
						
						prefixMap.put(nsURI, newPrefix);
						//Add the prefix mapping to the document's root node (no namespace-nesting here)
						Document doc = elt.getOwnerDocument();
						Element docElt = doc.getDocumentElement();
						docElt.setAttributeNS(XArchConstants.XMLNS_NS_URI, "xmlns:" + newPrefix,
							nsURI);
						elt.setPrefix(newPrefix);
					}
					else{
						//We have.  Use it.  No need to modify the document.
						elt.setPrefix(alreadyPrefix);
					}
				}
			}
		}
	}
	
	/**
	 * Generate a reasonable namespace prefix based on a namespace
	 * URI and a prefix map (to prevent collisions).
	 * @param namespaceURI Namespace URI to use to generate prefix
	 * @param prefixMap Prefix map.
	 * @return reasonable namespace prefix not already in <code>prefixMap</code>
	 */
	protected static String getReasonablePrefix(String namespaceURI, Hashtable prefixMap){
		String packageTitle = namespaceURI;
		if(namespaceURI.equals(XArchConstants.XSD_NS_URI)){
			return "xsd";
		}
		if(namespaceURI.equals(XArchConstants.XSI_NS_URI)){
			return "xsi";
		}

		int lastSlashIndex = namespaceURI.lastIndexOf("/");
		if(lastSlashIndex > -1){
			packageTitle = namespaceURI.substring(lastSlashIndex + 1);
		}
		if(packageTitle.toLowerCase().endsWith(".xsd")){
			packageTitle = packageTitle.substring(0, packageTitle.length() - 4);
		}
		if(packageTitle.equals("")){
			packageTitle = "ns";
		}
		
		//Check for conflicts
		String basePackageTitle = packageTitle;
		int i = 1;
		while(conflicts(packageTitle, prefixMap)){
			packageTitle = basePackageTitle + i;
		}
			
		return packageTitle;
	}
	
	private static boolean conflicts(String prefixName, Hashtable prefixMap){
		for(Enumeration en = prefixMap.elements(); en.hasMoreElements(); ){
			String prefixToCheck = (String)en.nextElement();
			if(prefixToCheck.equals(prefixName)){
				return true;
			}
		}
		return false;
	}
			

	/**
	 * Fixes a specific attribute.  See <code>fixNamespaces</code>
	 * @param attr Attribute to fix.
	 * @param prefixMap Prefix<->Namespace URI mappings.
 	 */
	protected static void fixAttribute(Attr attr, Hashtable prefixMap){
		if(attr == null){
			return;
		}
		
		synchronized(DOMUtils.getDOMLock(attr)){
			String nsURI = attr.getNamespaceURI();
			if(nsURI != null){
				//Get the element's current prefix.
				String nsPrefix = attr.getPrefix();
				if(nsPrefix != null){
					//Whoops, it already has a prefix.
					//Is this the first time we've seen this namespace URI?
					String alreadyPrefix = (String)prefixMap.get(nsURI);
					if(alreadyPrefix == null){
						//It is.  Well, this is as good a prefix as any.
						//Store it in the prefix map and add it to the
						//document.
						prefixMap.put(nsURI, nsPrefix);
						Document doc = attr.getOwnerDocument();
						Element docElt = doc.getDocumentElement();
						docElt.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + nsPrefix,
							nsURI);
					}
					else{
						//Okay, so we have a conflict--two prefixes for
						//the same namespace.  Let's change this prefix
						//so it matches the one we've already established.
						//That one's already in the document, so
						//we don't have to change the document.
						attr.setPrefix(alreadyPrefix);
					}
				}
				else{
					//The element doesn't have a prefix.
					//Have we established one for it yet?
					String alreadyPrefix = (String)prefixMap.get(nsURI);
					if(alreadyPrefix == null){
						//We haven't.  Let's create one and add it to the 
						//document.
						
						//Generate new prefix
						String newPrefix = getReasonablePrefix(nsURI, prefixMap);
						
						prefixMap.put(nsURI, newPrefix);
						//Add the prefix mapping to the document's root node (no namespace-nesting here)
						Document doc = attr.getOwnerDocument();
						Element docElt = doc.getDocumentElement();
						docElt.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + newPrefix,
							nsURI);
						attr.setPrefix(newPrefix);
					}
					else{
						//We have.  Use it.  No need to modify the document.
						attr.setPrefix(alreadyPrefix);
					}
				}
			}
	
			/*
			String nsURI = attr.getNamespaceURI();
			if((nsURI != null) && (attr.getPrefix() == null)){
				String nsPrefix = (String)prefixMap.get(nsURI);
				if(nsPrefix != null){
					attr.setPrefix(nsPrefix);
				}
				else{
					//Generate new prefix
					//String newPrefix = "ns" + prefixMap.size();
					String newPrefix = getReasonablePrefix(nsURI, prefixMap);
					//Put it in the table of URI->prefix mappings
					prefixMap.put(nsURI, newPrefix);
					//Add the prefix mapping to the document's root node (no namespace-nesting here)
					Document doc = attr.getOwnerDocument();
					Element docElt = doc.getDocumentElement();
					docElt.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + newPrefix,
						nsURI);
					//Set the prefix on the node.
					attr.setPrefix(newPrefix);
				}
			}	
			*/
		}
		
	}
	
	/**
	 * Gets the owner document from a DOM node.  This works
	 * even if the node is an Attr, which has no owner document apparently,
	 * by getting the Attr's owner element's owner document.
	 * @param n Node for which to get the owner document.
	 * @return Owner document for this node.
	 */
	protected static Document getOwnerDocument(Node n){
		synchronized(DOMUtils.getDOMLock(n)){
			Document doc = n.getOwnerDocument();
			if(doc == null){
				if(n.getNodeType() == Node.ATTRIBUTE_NODE){
					Element elt = ((Attr)n).getOwnerElement();
					if(elt != null){
						return elt.getOwnerDocument();
					}
				}
			}
			return doc;
		}
	}
	
	/**
	 * Fixes a specific xsi:type attribute.  See <code>fixNamespaces</code>
	 * @param attr xsi:type attribute to fix.
	 * @param prefixMap Prefix<->Namespace URI mappings.
 	 */
	protected static void fixXsiTypeAttribute(Attr attr, Hashtable prefixMap){
		synchronized(DOMUtils.getDOMLock(attr)){
			String origVal = attr.getNodeValue();
			
			if(!origVal.startsWith("unfixed$")){
				return;
			}
			else{
				origVal = origVal.substring(origVal.indexOf("$") + 1);
			}
			int colonIndex = origVal.lastIndexOf(":");
			if(colonIndex != -1){
				String internalNSURI = origVal.substring(0, colonIndex);
				String internalTypeName = origVal.substring(colonIndex + 1);
				
				String intPrefix = (String)prefixMap.get(internalNSURI);
				if(intPrefix != null){
					attr.setNodeValue(intPrefix + ":" + internalTypeName);
				}
				else{
					//Generate new prefix
					//String newPrefix = "ns" + prefixMap.size();
					String newPrefix = getReasonablePrefix(internalNSURI, prefixMap);
					//Put it in the table of URI->prefix mappings
					prefixMap.put(internalNSURI, newPrefix);
					//Set the prefix on the node.
					attr.setNodeValue(newPrefix + ":" + internalTypeName);
					
					//Add the prefix mapping to the document's root node (no namespace-nesting here)
					Document doc = getOwnerDocument(attr);
					Element docElt = doc.getDocumentElement();
					docElt.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + newPrefix,
						internalNSURI);
				}
			}
		}		
	}
	
	/**
	 * DEPRECATED: Returns a nice-looking representation of the given XML document.
	 * Sub-elements are properly indented, and long lines are indent-wrapped
	 * nicely, too.
	 * 
	 * @deprecated Use <code>serialize</code> from an implementation of
	 * <code>IXArchImplementation</code> instead.
	 * 
	 * @param xArch Document to turn into a string.
	 * @return String representation of the document, prettyprinted.
	 */
	public static String getPrettyXmlRepresentation(IXArch xArch) throws DOMException{
		if(!(xArch instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Node docRootElt = ((DOMBased)xArch).getDOMNode();
		Document doc = docRootElt.getOwnerDocument();
		
		fixNamespaces(doc);
		
		try{
			StringWriter sw = new StringWriter();

			Source domSource = new javax.xml.transform.dom.DOMSource(doc);
			Result streamResult = new javax.xml.transform.stream.StreamResult(sw);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");
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
	 * DEPRECATED: Parse a stream into a DOM document.
	 * 
	 * @deprecated No public replacement.
	 * 
	 * @param r Reader to parse from.
	 * @return new DOM document corresponding to the parsed stream
	 * @throws SAXException on parse failure
	 * @throws IOException on I/O failure.
	 */
	public static Document parseToDocument(Reader r) throws SAXException, IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setValidating(false);
		dbf.setIgnoringElementContentWhitespace(true);
		try{
			StringReplacerReader srr = 
				new StringReplacerReader(r, XArchConstants.OLD_XSI_NS_URI, XArchConstants.XSI_NS_URI);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(srr));
			return doc;
		}
		catch(ParserConfigurationException pce){
			pce.printStackTrace();
			return null;
		}
		/*
		DOMParser parser = new DOMParser();
		parser.setIncludeIgnorableWhitespace(false);
		InputSource is = new InputSource(r);
		parser.parse(is);
		Document document = parser.getDocument();		
		return document;
		*/
	}
	
	/** 
	 * DEPRECATED: Parse an xArch document from a reader, returning an IXArch object.
	 * 
	 * @deprecated Use <code>parse(Reader)</code> from an implementation of
	 * <code>IXArchImplementation</code> instead.
	 * 
	 * @param r Reader from which to parse xArch document.
	 * @return IXArch object representing the document.
	 * @exception SAXException if there was a problem parsing the document.
	 * @exception IOException if there was an I/O error while reading the document.
	 */
	public static IXArch parse(Reader r) throws SAXException, IOException{
		try{
			return xArchImplementation.parse(r);
		}
		catch(XArchParseException xpe){
			Throwable t = xpe.getCause();
			if(t instanceof SAXException){
				throw (SAXException)t;
			}
			else if(t instanceof IOException){
				throw (IOException)t;
			}
			else{
				//This shouldn't happen.
				throw new RuntimeException(t);
			}
		}
		
		//Document doc = parseToDocument(r);
		//return createXArch(doc);
	}

	/**
	 * DEPRECATED: Create an IXArch object based on a new DOM Document.
	 * 
	 * @deprecated Use <code>createXArch</code> from an implementation of
	 * <code>IXArchImplementation</code> instead.
	 * 
	 * @return New IXArch object.
	 */
	public static IXArch createXArch(){
		return xArchImplementation.createXArch();
	}

	/**
	 * DEPRECATED: Clones an xArch document, given its IXArch (root) element.
	 * Changes to one document will not affect the other.
	 * 
	 * @deprecated Use <code>cloneXArch</code> from an implementation of
	 * <code>IXArchImplementation</code> instead.
	 * 
	 * @param xArchToClone The root xArch element of the document to clone.
	 * @return Root element of cloned document.
	 */
	public static IXArch cloneXArch(IXArch xArchToClone){
		return xArchImplementation.cloneXArch(xArchToClone);
	}
	
	/**
	 * DEPRECATED: Create an IXArch object based on a given DOM Document.
	 * 
	 * @deprecated Use <code>parse(Document)</code> from a
	 * DOM-based implementation of
	 * <code>IXArchImplementation</code> instead.
	 * 
	 * @return New IXArch object.
	 */
	public static IXArch createXArch(Document doc){
		try{
			return xArchImplementation.parse(doc);
		}
		catch(XArchParseException xpe){
			throw new RuntimeException(xpe);
		}
	}

	/**
	 * DEPRECATED: Determines if a given IXArchElement is contained (a child of)
	 * a given IXArch.
	 * 
	 * @deprecated Use <code>isContainedIn</code> from an implementation of
	 * <code>IXArchImplementation</code> instead.
	 * 
	 * @param xArch <code>IXArch</code> to check for the element.
	 * @param elt Element to check for parentage.
	 * @return <code>true</code> if the element is attached,
	 * <code>false</code> otherwise.
	 */
	public static boolean isContainedIn(IXArch xArch, IXArchElement elt){
		return xArchImplementation.isContainedIn(xArch, elt);
	}

	/**
	 * Guesses the package title where a given schema URI would have
	 * been generated by apigen.  Used internally; not for external
	 * use.
	 * @param schemaURI Schema URI (Namespace URI) from which to guess
	 * package title.
	 * @return Probable package title.
	 */
	public static String getPackageTitle(String schemaURI){
		String packageTitle = schemaURI;
		int lastSlashIndex = schemaURI.lastIndexOf("/");
		if(lastSlashIndex > -1){
			packageTitle = schemaURI.substring(lastSlashIndex + 1);
		}
		if(packageTitle.toLowerCase().endsWith(".xsd")){
			packageTitle = packageTitle.substring(0, packageTitle.length() - 4);
		}
		return packageTitle;
	}

	/**
	 * Gets the full package name given a package title.
	 * Used internally; not for external use.
	 * @param packageTitle Package Title from getPackageTitle()
	 * @return Package name.
	 */
	public static String getPackageName(String packageTitle){
		return "edu.uci.isr.xarch." + packageTitle;
	}

	/**
	 * Gets the interface name for a given <code>xsi:type</code>.
	 * Not for public use.
	 * @param packageName Package name for the interface.
	 * @param xsiTypeName xsi:type name.
	 * @return Interface name.
	 */
	public static String getInterfaceName(String packageName, String xsiTypeName){
		return packageName + ".I" + xsiTypeName;
	}

	/**
	 * Gets the implementation name for a given <code>xsi:type</code>.
	 * Not for public use.
	 * @param packageName Package name for the interface.
	 * @param xsiTypeName xsi:type name.
	 * @return Implementation name.
	 */
	public static String getImplName(String packageName, String xsiTypeName){
		return packageName + "." + xsiTypeName + "Impl";
	}

	/**
	 * Gets the implementation name for a given interface name.
	 * Not for public use.
	 * @param interfaceName Interface class name.
	 * @return Implementation name.
	 */
	public static String getImplNameFromInterfaceName(String interfaceName){
		int lastDot = interfaceName.lastIndexOf(".");
		String packagePrefix = interfaceName.substring(0, lastDot + 1);
		String interfaceClassName = interfaceName.substring(lastDot + 1);
		String baseTypeName = interfaceClassName.substring(1);
		String implName = baseTypeName + "Impl";
		return packagePrefix + implName;
	}
	
	/**
	 * Gets a list of subpackages that represent generated APIs for schemas.
	 * Package names are in the form "edu.uci.isr.xarch.packagename"
	 * @return List of package names.
	 */
	public static String[] getPackageNames(){
		Vector v = new Vector();
		try{
			InputStream is = XArchUtils.class.getClassLoader().getResourceAsStream("edu/uci/isr/xarch/packagelist.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while(true){
				String line = br.readLine();
				if(line == null){
					br.close();
					is.close();
					String[] retArr = new String[v.size()];
					v.copyInto(retArr);
					return retArr;
				}
				else{
					line = line.trim();
					if(!line.equals("")){
						v.addElement(line);
					}
				}
			}
		}
		catch(IOException e){
			//Shouldn't happen.
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * Get the <CODE>xsi:type</CODE> for a given element.  This
	 * function is superior to the one in DOMUtils because it
	 * will actually guess the xsi:type using a tree-walking/reflection
	 * based algorithm when possible.
	 * Not for public use.
	 * @param elt Element whose type you want to get.
	 * @return Element's <CODE>xsi:type</CODE> or <CODE>null</CODE> if
	 * it can't be determined.
	 */
	public static QName getXSIType(Element elt){
		synchronized(DOMUtils.getDOMLock(elt)){
			//First, let's try to get the type directly.
			QName directType = DOMUtils.getXSIType(elt);
			if(directType != null){
				//Yay!
				return directType;
			}
	
			//Okay, this could be a very weird case where we've recontextualized an
			//element over into a different document, and it has an xsi:type with a 
			//namespace prefix that was valid in the old document, but isn't valid (yet)
			//in the new document.  So, let's try to guess the namespace prefix and
			//add it to the document if we guess right.
			String xsiTypeString = DOMUtils.getAttributeValue(elt, XArchConstants.XSI_NS_URI, "type");
			if(xsiTypeString != null){
				int colonIndex = xsiTypeString.indexOf(":");
				if(colonIndex != -1){
					String internalPrefix = xsiTypeString.substring(0, colonIndex);
					String internalTypeName = xsiTypeString.substring(colonIndex + 1);
				
					//Okay, we have an internal prefix and type name.  Let's do some
					//reflection.
					String constantsClassName = 
						"edu.uci.isr.xarch." + internalPrefix + "." + capFirstLetter(internalPrefix) + "Constants";
					//Okay, we have a possible constants class name.
					try{
						Class constantsClass = Class.forName(constantsClassName);
						Field nsURIField = constantsClass.getField("NS_URI");
						//Static field, so we dont' have to pass an object
						String nsURI = (String)nsURIField.get(null);
						if(nsURI != null){
							//Oh, wow, we resolved it.  Neat.
							//We should add that prefix to the document element so we don't go through this again.
							Document doc = getOwnerDocument(elt);
							Element docElt = doc.getDocumentElement();
							docElt.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + internalPrefix,
								nsURI);
							return new QName(nsURI, internalTypeName);
						}
					}
					catch(Exception e){
						//Lots of bad stuff can happen here, but that means we just fall back again...
					}
				}
			}
	
			//Dammit.  We have to crawl up the XML tree until
			//we find something with an xsi:type.
			
			//This is one of those fancy recursive calls.  First,
			//let's see who our element's parent is.
			
			Node parentNode = elt.getParentNode();
			//Base case: are we the xArch element?
			if((parentNode == null) || (parentNode.getNodeType() != Node.ELEMENT_NODE)){
				//Okay, we're potentially the xArch node, but we could have just been
				//a disconnected subtree.
				String thisNodeTagName = elt.getTagName();
				thisNodeTagName = DOMUtils.stripPrefix(thisNodeTagName);
				if(thisNodeTagName.equals("xArch")){
					//It's the xArch node.
					return new QName(XArchImpl.XSD_TYPE_NSURI, XArchImpl.XSD_TYPE_NAME);
				}
				else{
					//Hmph.  I don't know what the hell node this is.
					return null;
				}
			}
			//Okay, we're not the root node, and we have a parent who's an element.
			QName parentXSIType = getXSIType((Element)parentNode);
			if(parentXSIType == null){
				//We couldn't find an ancestor whose xsi:type is determinable.  That's a failure.
				return null;
			}
			//Aha!  We know our parent's xsi:type.  Good.  Now we have to figure out
			//what OUR xsi:type is from that.
	
			try {
				//Two cases here.  Either our parent is xArch, in which case we need
				//to go through some special hoops to get our xsi:type, or it's not.
				//First, let's handle the our-parent-is-xArch case.
				if(parentXSIType.equals(new QName(XArchImpl.XSD_TYPE_NSURI, XArchImpl.XSD_TYPE_NAME))){
					//Okay, our parent was xArch.  That means we're a top-level element.
					//Since our tag name was apparently derived from our type name in the first
					//place, we can reverse the process to get our type name.
					//The type namespace prefix is going to be the same as the tag's namespace
					//prefix in this case.  
					String nsuri = elt.getNamespaceURI();
					//The type name is going to be the capped tag name (sans prefix)
					String name = capFirstLetter(DOMUtils.stripPrefix(elt.getTagName()));
					return new QName(nsuri, name);
				}
				
				//Okay, our parent is not xArch.
				//Presumably, given our tag name (say, 'component'), there will
				//be a setComponent or an addComponent method in the parent class.
				
				String packageTitle = XArchUtils.getPackageTitle
					(parentXSIType.getNamespaceURI());
				String packageName = XArchUtils.getPackageName(packageTitle);
				String implName = XArchUtils.getImplName
					(packageName, parentXSIType.getName());
				//System.out.println("wrapping class <" + implName + ">\n");
				Class c = Class.forName(implName);
				//Rawk!  We got our parent's implementation class!  Now we can use
				//reflection to figure out what our implementation class
				//is!
				
				String ourTagName = elt.getTagName();
				ourTagName = DOMUtils.stripPrefix(ourTagName);
				String setMethodName = "set" + capFirstLetter(ourTagName);
				String addMethodName = "add" + capFirstLetter(ourTagName);
				
				Class interfaceClass = null;
				java.lang.reflect.Method[] methods = c.getMethods();
				for(int i = 0; i < methods.length; i++){
					java.lang.reflect.Method m = methods[i];
					String methodName = m.getName();
					if(methodName.equals(setMethodName) || (methodName.equals(addMethodName))){
						if(c.getName().startsWith("edu.uci.isr.xarch.")){
							int mods = m.getModifiers();
							if(Modifier.isPublic(mods)){
								if(!Modifier.isStatic(mods)){
									Class[] params = m.getParameterTypes();
									if(params.length == 1){
										interfaceClass = params[0];
										break;
									}
								}
							}
						}
					}
				}
				//Couldn't find the method.  This is a failure condition.
				if(interfaceClass == null){
					return null;
				}
				//Okay, we have the interface class.  We need the implementation, though.
				String implClassName = getImplNameFromInterfaceName(interfaceClass.getName());
				Class implClass = Class.forName(implClassName);
				
				Field nsuriField = implClass.getField("XSD_TYPE_NSURI");
				Field nameField = implClass.getField("XSD_TYPE_NAME");
				String nsuri = (String)nsuriField.get(null);
				String name = (String)nameField.get(null);
				
				//Got it!  Phew!
				return new QName(nsuri, name);
			}
			catch(Exception e){
				//Lots of bad things could happen, but this
				//is OK, because this is best-effort anyway.
				return null;
			}
		}
	}

	public static String capFirstLetter(String s){
		if(s == null) return null;
		if(s.equals("")) return "";
		StringBuffer sb = new StringBuffer();
		sb.append(Character.toUpperCase(s.charAt(0)));
		if(s.length() > 0){
			sb.append(s.substring(1));
		}
		return sb.toString();
	}

	public static String uncapFirstLetter(String s){
		if(s == null) return null;
		if(s.equals("")) return "";
		StringBuffer sb = new StringBuffer();
		sb.append(Character.toLowerCase(s.charAt(0)));
		if(s.length() > 0){
			sb.append(s.substring(1));
		}
		return sb.toString();
	}

}