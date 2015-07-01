package edu.uci.isr.archstudio4.comp.archlight.tools.schematron;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Method;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.uci.isr.sysutils.SystemUtils;

public class SchematronUtils{
	
	public static String SCHEMATRON_METASTYLESHEET_URL = 
		"res/schematron-xml1-6.xsl";
	public static String SCHEMATRON_SKELETON_URL = 
		"res/skeleton1-6.xsl";
	

	private static DOMSource schematronMetastylesheet = null;
	private static DOMSource schematronSkeleton = null;
	
	public static String getXalanVersion(){
		try{
			Class environmentCheck = Class.forName("org.apache.xalan.xslt.EnvironmentCheck");
		}
		catch(ClassNotFoundException cnfe){
			return null;
		}
		Class versionClass = null;
		try{
			versionClass = Class.forName("org.apache.xalan.Version");
		}
		catch(ClassNotFoundException cnfe2){
			try{
				versionClass = Class.forName("org.apache.xalan.processor.Version");
			}
			catch(ClassNotFoundException cnfe3){
			}
		}
		if(versionClass == null){
			return "UnknownButPresent";
		}
		else{
			try{
				Method m = versionClass.getMethod("getVersion", new Class[0]);
				String version = (String)m.invoke(null, new Object[0]);
				return version;
			}
			catch(Exception e){
				return "UnknownButPresent";
			}
		}
	}
	
	public static DOMSource getSchematronMetastylesheet() throws SchematronInitializationException{
		if(schematronMetastylesheet != null){
			return schematronMetastylesheet;
		}
		try{
			InputStream is = SchematronUtils.class.getResourceAsStream(SCHEMATRON_METASTYLESHEET_URL);
			InputStreamReader isr = new InputStreamReader(is);
			Document doc = parseToDocument(isr);
			isr.close();
			schematronMetastylesheet = new DOMSource(doc, "schematron-xml1-6.xsl");
			return schematronMetastylesheet;
		}
		catch(Exception e){
			throw new SchematronInitializationException(e);
		}
	}

	public static DOMSource getSchematronSkeleton() throws SchematronInitializationException{
		if(schematronSkeleton != null){
			return schematronSkeleton;
		}
		try{
			InputStream is = SchematronUtils.class.getResourceAsStream(SCHEMATRON_SKELETON_URL);
			InputStreamReader isr = new InputStreamReader(is);
			Document doc = parseToDocument(isr);
			isr.close();
			schematronSkeleton = new DOMSource(doc, "skeleton1-6.xsl");
			return schematronSkeleton;
		}
		catch(Exception e){
			throw new SchematronInitializationException(e);
		}
	}
	
	public static DOMResult getEmptyDOMResult() throws SchematronInitializationException{
		return new DOMResult();
	}
	
	public static Document parseToDocument(java.io.Reader r) throws SAXException, IOException, ParserConfigurationException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setValidating(false);
		dbf.setIgnoringElementContentWhitespace(true);

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new InputSource(r));
		return doc;
	}

	/** 
	 * Gets the DOMImplementation for the preferred parser.
	 * @return DOMImplementation object for the preferred parser.
	 */
	public static DOMImplementation getDOMImplementation() throws ParserConfigurationException{
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
	
	public static TransformerFactory getTransformerFactory() throws SchematronInitializationException{
		try{
			return TransformerFactory.newInstance();
		}
		catch(Exception e){
			throw new SchematronInitializationException(e);
		}
	}
	
	public static Transformer getTransformer() throws SchematronInitializationException{
		try{
			TransformerFactory factory = getTransformerFactory();
			return factory.newTransformer();
		}
		catch(Exception e){
			throw new SchematronInitializationException(e);
		}
	}

	public static Transformer getTransformer(Source src) throws SchematronInitializationException{
		try{
			TransformerFactory factory = getTransformerFactory();
			factory.setURIResolver(new SchematronURIResolver(factory.getURIResolver(),
				getSchematronSkeleton()));
			return factory.newTransformer(src);
		}
		catch(Exception e){
			throw new SchematronInitializationException(e);
		}
	}
	

	public static String serialize(Document doc){
		try{
			StringWriter sw = new StringWriter();

			Source domSource = new javax.xml.transform.dom.DOMSource(doc);
			Result streamResult = new javax.xml.transform.stream.StreamResult(sw);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");
			t.transform(domSource, streamResult);
			return sw.toString();
		}
		catch(Exception e){
			System.err.println("This shouldn't happen.");
			e.printStackTrace();
			return null;
		}
	}
	
	static class SchematronURIResolver implements URIResolver{
		protected URIResolver baseResolver;
		protected DOMSource schematronSkeleton;
		
		public SchematronURIResolver(URIResolver baseResolver, DOMSource schematronSkeleton){
			this.schematronSkeleton = schematronSkeleton;
			this.baseResolver = baseResolver;
		}
		
		public Source resolve(String href, String base) throws TransformerException{
			if(href.equals("skeleton1-6.xsl")){
				return schematronSkeleton;
			}
			return baseResolver.resolve(href, base);
		}
	}
	

}