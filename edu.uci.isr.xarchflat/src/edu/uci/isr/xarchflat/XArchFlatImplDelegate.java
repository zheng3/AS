package edu.uci.isr.xarchflat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.xml.sax.SAXException;

public class XArchFlatImplDelegate
	implements XArchFlatInterface{

	protected final XArchFlatInterface xarch;

	public XArchFlatImplDelegate(XArchFlatInterface xarch){
		this.xarch = xarch;
	}

	public void add(ObjRef baseObjectRef, String typeOfThing, ObjRef thingToAddRef){
		xarch.add(baseObjectRef, typeOfThing, thingToAddRef);
	}

	public void add(ObjRef baseObjectRef, String typeOfThing, ObjRef[] thingsToAddRefs){
		xarch.add(baseObjectRef, typeOfThing, thingsToAddRefs);
	}

	public void clear(ObjRef baseObjectRef, String typeOfThing){
		xarch.clear(baseObjectRef, typeOfThing);
	}

	public ObjRef cloneElement(ObjRef targetObjectRef, int depth){
		return xarch.cloneElement(targetObjectRef, depth);
	}

	public ObjRef cloneXArch(ObjRef xArchRef, String newURI){
		return xarch.cloneXArch(xArchRef, newURI);
	}

	public void close(ObjRef xArchRef){
		xarch.close(xArchRef);
	}

	public void close(String uriString){
		xarch.close(uriString);
	}

	public ObjRef create(ObjRef contextObjectRef, String typeOfThing){
		return xarch.create(contextObjectRef, typeOfThing);
	}

	public ObjRef createContext(ObjRef archObject, String contextType){
		return xarch.createContext(archObject, contextType);
	}

	public ObjRef createElement(ObjRef contextObjectRef, String typeOfThing){
		return xarch.createElement(contextObjectRef, typeOfThing);
	}

	public ObjRef createXArch(String uri){
		return xarch.createXArch(uri);
	}

	public void dump(ObjRef ref){
		xarch.dump(ref);
	}

	public boolean equals(ObjRef ref1, ObjRef ref2){
		return xarch.equals(ref1, ref2);
	}

	public ObjRef get(ObjRef baseObjectRef, String typeOfThing, String id){
		return xarch.get(baseObjectRef, typeOfThing, id);
	}

	public ObjRef[] get(ObjRef baseObjectRef, String typeOfThing, String[] ids){
		return xarch.get(baseObjectRef, typeOfThing, ids);
	}

	public Object get(ObjRef baseObjectRef, String typeOfThing){
		return xarch.get(baseObjectRef, typeOfThing);
	}

	public ObjRef[] getAll(ObjRef baseObjectRef, String typeOfThing){
		return xarch.getAll(baseObjectRef, typeOfThing);
	}

	public ObjRef[] getAllAncestors(ObjRef targetObjectRef){
		return xarch.getAllAncestors(targetObjectRef);
	}

	public ObjRef[] getAllElements(ObjRef contextObjectRef, String typeOfThing, ObjRef archObjectRef){
		return xarch.getAllElements(contextObjectRef, typeOfThing, archObjectRef);
	}

	public ObjRef getByID(ObjRef xArchRef, String id){
		return xarch.getByID(xArchRef, id);
	}

	public ObjRef getByID(String id){
		return xarch.getByID(id);
	}

	public String[] getContextTypes(){
		return xarch.getContextTypes();
	}

	public ObjRef getElement(ObjRef contextObjectRef, String typeOfThing, ObjRef archObjectRef){
		return xarch.getElement(contextObjectRef, typeOfThing, archObjectRef);
	}

	public String getElementName(ObjRef xArchRef){
		return xarch.getElementName(xArchRef);
	}

	public IXArchInstanceMetadata getInstanceMetadata(ObjRef baseObjRef){
		return xarch.getInstanceMetadata(baseObjRef);
	}

	public ObjRef getOpenXArch(String uri){
		return xarch.getOpenXArch(uri);
	}

	public ObjRef[] getOpenXArches(){
		return xarch.getOpenXArches();
	}

	public String[] getOpenXArchURIs(){
		return xarch.getOpenXArchURIs();
	}

	@SuppressWarnings("deprecation")
	public String[] getOpenXArchURLs(){
		return xarch.getOpenXArchURLs();
	}

	public ObjRef getParent(ObjRef targetObjectRef){
		return xarch.getParent(targetObjectRef);
	}

	public ObjRef[] getReferences(ObjRef xArchRef, String id){
		return xarch.getReferences(xArchRef, id);
	}

	@SuppressWarnings("deprecation")
	public String getType(ObjRef baseObjectRef){
		return xarch.getType(baseObjectRef);
	}

	public IXArchTypeMetadata getTypeMetadata(ObjRef baseObjectRef){
		return xarch.getTypeMetadata(baseObjectRef);
	}

	public IXArchTypeMetadata getTypeMetadata(String type){
		return xarch.getTypeMetadata(type);
	}

	public ObjRef getXArch(ObjRef baseObjectRef){
		return xarch.getXArch(baseObjectRef);
	}

	public XArchPath getXArchPath(ObjRef ref){
		return xarch.getXArchPath(ref);
	}

	public String getXArchURI(ObjRef xArchRef){
		return xarch.getXArchURI(xArchRef);
	}

	@SuppressWarnings("deprecation")
	public String getXArchURL(ObjRef xArchRef){
		return xarch.getXArchURL(xArchRef);
	}

	public boolean has(ObjRef baseObjectRef, String typeOfThing, ObjRef valueToCheck){
		return xarch.has(baseObjectRef, typeOfThing, valueToCheck);
	}

	public boolean[] has(ObjRef baseObjectRef, String typeOfThing, ObjRef[] thingsToCheck){
		return xarch.has(baseObjectRef, typeOfThing, thingsToCheck);
	}

	public boolean has(ObjRef baseObjectRef, String typeOfThing, String valueToCheck){
		return xarch.has(baseObjectRef, typeOfThing, valueToCheck);
	}

	public boolean hasAll(ObjRef baseObjectRef, String typeOfThing, ObjRef[] valuesToCheck){
		return xarch.hasAll(baseObjectRef, typeOfThing, valuesToCheck);
	}

	public boolean hasAncestor(ObjRef childRef, ObjRef ancestorRef){
		return xarch.hasAncestor(childRef, ancestorRef);
	}

	public boolean isAssignable(String toType, String fromType){
		return xarch.isAssignable(toType, fromType);
	}

	public boolean isAttached(ObjRef childRef){
		return xarch.isAttached(childRef);
	}

	public boolean isEqual(ObjRef baseObjectRef, ObjRef thingToCheck){
		return xarch.isEqual(baseObjectRef, thingToCheck);
	}

	public boolean isEquivalent(ObjRef baseObjectRef, ObjRef thingToCheck){
		return xarch.isEquivalent(baseObjectRef, thingToCheck);
	}

	public boolean isInstanceOf(ObjRef baseObjectRef, String type){
		return xarch.isInstanceOf(baseObjectRef, type);
	}

	public boolean isValidObjRef(ObjRef ref){
		return xarch.isValidObjRef(ref);
	}

	public ObjRef parseFromFile(String fileName) throws FileNotFoundException, IOException, SAXException{
		return xarch.parseFromFile(fileName);
	}

	public ObjRef parseFromString(String uri, String xml) throws IOException, SAXException{
		return xarch.parseFromString(uri, xml);
	}

	public ObjRef parseFromURL(String urlString) throws MalformedURLException, IOException, SAXException{
		return xarch.parseFromURL(urlString);
	}

	public ObjRef promoteTo(ObjRef contextObjectRef, String promotionTarget, ObjRef targetObjectRef){
		return xarch.promoteTo(contextObjectRef, promotionTarget, targetObjectRef);
	}

	public ObjRef recontextualize(ObjRef contextObjectRef, String typeOfThing, ObjRef targetObjectRef){
		return xarch.recontextualize(contextObjectRef, typeOfThing, targetObjectRef);
	}

	public void remove(ObjRef baseObjectRef, String typeOfThing, ObjRef thingToRemove){
		xarch.remove(baseObjectRef, typeOfThing, thingToRemove);
	}

	public void remove(ObjRef baseObjectRef, String typeOfThing, ObjRef[] thingsToRemove){
		xarch.remove(baseObjectRef, typeOfThing, thingsToRemove);
	}

	public void renameXArch(String oldURI, String newURI){
		xarch.renameXArch(oldURI, newURI);
	}

	public ObjRef resolveHref(ObjRef xArchRef, String href){
		return xarch.resolveHref(xArchRef, href);
	}

	public ObjRef resolveXArchPath(ObjRef xArchRef, XArchPath xArchPath){
		return XArchPath.resolve(this, xArchRef, xArchPath);
	}

	public String serialize(ObjRef xArchRef){
		return xarch.serialize(xArchRef);
	}

	public void set(ObjRef baseObjectRef, String typeOfThing, ObjRef value){
		xarch.set(baseObjectRef, typeOfThing, value);
	}

	public void set(ObjRef baseObjectRef, String typeOfThing, String value){
		xarch.set(baseObjectRef, typeOfThing, value);
	}

	public void writeToFile(ObjRef xArchRef, String fileName) throws IOException{
		xarch.writeToFile(xArchRef, fileName);
	}
}
