package edu.uci.isr.xarchflat.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.xml.sax.SAXException;

import edu.uci.isr.xarchflat.IXArchInstanceMetadata;
import edu.uci.isr.xarchflat.IXArchTypeMetadata;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;
import edu.uci.isr.xarchflat.XArchPath;

/*
 * This class needs to disappear and the xarch impl needs to handle multiple
 * threads.
 */

@Deprecated
@SuppressWarnings("deprecation")
public class XArchFlatInterfaceSynchronizer
	implements XArchFlatInterface{

	private XArchFlatInterface xarch;

	public XArchFlatInterfaceSynchronizer(XArchFlatInterface xarch){
		this.xarch = xarch;
	}

	public synchronized void add(ObjRef baseObjectRef, String typeOfThing, ObjRef thingToAddRef){
		xarch.add(baseObjectRef, typeOfThing, thingToAddRef);
	}

	public synchronized void add(ObjRef baseObjectRef, String typeOfThing, ObjRef[] thingsToAddRefs){
		xarch.add(baseObjectRef, typeOfThing, thingsToAddRefs);
	}

	public synchronized void clear(ObjRef baseObjectRef, String typeOfThing){
		xarch.clear(baseObjectRef, typeOfThing);
	}

	public synchronized ObjRef cloneElement(ObjRef targetObjectRef, int depth){
		return xarch.cloneElement(targetObjectRef, depth);
	}

	public synchronized ObjRef cloneXArch(ObjRef xArchRef, String newURI){
		return xarch.cloneXArch(xArchRef, newURI);
	}

	public void close(ObjRef xArchRef){
		xarch.close(xArchRef);
	}

	public void close(String uriString){
		xarch.close(uriString);
	}

	public synchronized ObjRef create(ObjRef contextObjectRef, String typeOfThing){
		return xarch.create(contextObjectRef, typeOfThing);
	}

	public synchronized ObjRef createContext(ObjRef xArchObject, String contextType){
		return xarch.createContext(xArchObject, contextType);
	}

	public synchronized ObjRef createElement(ObjRef contextObjectRef, String typeOfThing){
		return xarch.createElement(contextObjectRef, typeOfThing);
	}

	public ObjRef createXArch(String uri){
		return xarch.createXArch(uri);
	}

	public synchronized void dump(ObjRef ref){
		xarch.dump(ref);
	}

	public synchronized boolean equals(ObjRef ref1, ObjRef ref2){
		return xarch.equals(ref1, ref2);
	}

	public synchronized ObjRef get(ObjRef baseObjectRef, String typeOfThing, String id){
		return xarch.get(baseObjectRef, typeOfThing, id);
	}

	public synchronized ObjRef[] get(ObjRef baseObjectRef, String typeOfThing, String[] ids){
		return xarch.get(baseObjectRef, typeOfThing, ids);
	}

	public synchronized Object get(ObjRef baseObjectRef, String typeOfThing){
		return xarch.get(baseObjectRef, typeOfThing);
	}

	public synchronized ObjRef[] getAll(ObjRef baseObjectRef, String typeOfThing){
		return xarch.getAll(baseObjectRef, typeOfThing);
	}

	public synchronized ObjRef[] getAllAncestors(ObjRef targetObjectRef){
		return xarch.getAllAncestors(targetObjectRef);
	}

	public synchronized ObjRef[] getAllElements(ObjRef contextObjectRef, String typeOfThing, ObjRef xArchObjectRef){
		return xarch.getAllElements(contextObjectRef, typeOfThing, xArchObjectRef);
	}

	public synchronized ObjRef getByID(ObjRef xArchRef, String id){
		return xarch.getByID(xArchRef, id);
	}

	public synchronized ObjRef getByID(String id){
		return xarch.getByID(id);
	}

	public synchronized String[] getContextTypes(){
		return xarch.getContextTypes();
	}

	public synchronized ObjRef getElement(ObjRef contextObjectRef, String typeOfThing, ObjRef xArchObjectRef){
		return xarch.getElement(contextObjectRef, typeOfThing, xArchObjectRef);
	}

	public synchronized String getElementName(ObjRef xArchRef){
		return xarch.getElementName(xArchRef);
	}

	public synchronized IXArchInstanceMetadata getInstanceMetadata(ObjRef baseObjRef){
		return xarch.getInstanceMetadata(baseObjRef);
	}

	public synchronized ObjRef getOpenXArch(String uri){
		return xarch.getOpenXArch(uri);
	}

	public synchronized ObjRef[] getOpenXArches(){
		return xarch.getOpenXArches();
	}

	public synchronized String[] getOpenXArchURIs(){
		return xarch.getOpenXArchURIs();
	}

	public synchronized String[] getOpenXArchURLs(){
		return xarch.getOpenXArchURLs();
	}

	public synchronized ObjRef getParent(ObjRef targetObjectRef){
		return xarch.getParent(targetObjectRef);
	}

	public synchronized ObjRef[] getReferences(ObjRef xArchRef, String id){
		return xarch.getReferences(xArchRef, id);
	}

	public synchronized String getType(ObjRef baseObjectRef){
		return xarch.getType(baseObjectRef);
	}

	public synchronized IXArchTypeMetadata getTypeMetadata(ObjRef baseObjectRef){
		return xarch.getTypeMetadata(baseObjectRef);
	}

	public synchronized IXArchTypeMetadata getTypeMetadata(String type){
		return xarch.getTypeMetadata(type);
	}

	public synchronized ObjRef getXArch(ObjRef baseObjectRef){
		return xarch.getXArch(baseObjectRef);
	}

	public synchronized XArchPath getXArchPath(ObjRef ref){
		return xarch.getXArchPath(ref);
	}

	public synchronized String getXArchURI(ObjRef xArchRef){
		return xarch.getXArchURI(xArchRef);
	}

	public synchronized String getXArchURL(ObjRef xArchRef){
		return xarch.getXArchURL(xArchRef);
	}

	public synchronized boolean has(ObjRef baseObjectRef, String typeOfThing, ObjRef valueToCheck){
		return xarch.has(baseObjectRef, typeOfThing, valueToCheck);
	}

	public synchronized boolean[] has(ObjRef baseObjectRef, String typeOfThing, ObjRef[] thingsToCheck){
		return xarch.has(baseObjectRef, typeOfThing, thingsToCheck);
	}

	public synchronized boolean has(ObjRef baseObjectRef, String typeOfThing, String valueToCheck){
		return xarch.has(baseObjectRef, typeOfThing, valueToCheck);
	}

	public synchronized boolean hasAll(ObjRef baseObjectRef, String typeOfThing, ObjRef[] valuesToCheck){
		return xarch.hasAll(baseObjectRef, typeOfThing, valuesToCheck);
	}

	public synchronized boolean hasAncestor(ObjRef childRef, ObjRef ancestorRef){
		return xarch.hasAncestor(childRef, ancestorRef);
	}

	public synchronized boolean isAssignable(String toType, String fromType){
		return xarch.isAssignable(toType, fromType);
	}

	public synchronized boolean isAttached(ObjRef childRef){
		return xarch.isAttached(childRef);
	}

	public synchronized boolean isEqual(ObjRef baseObjectRef, ObjRef thingToCheck){
		return xarch.isEqual(baseObjectRef, thingToCheck);
	}

	public synchronized boolean isEquivalent(ObjRef baseObjectRef, ObjRef thingToCheck){
		return xarch.isEquivalent(baseObjectRef, thingToCheck);
	}

	public synchronized boolean isInstanceOf(ObjRef baseObjectRef, String type){
		return xarch.isInstanceOf(baseObjectRef, type);
	}

	public synchronized boolean isValidObjRef(ObjRef ref){
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

	public synchronized ObjRef promoteTo(ObjRef contextObjectRef, String promotionTarget, ObjRef targetObjectRef){
		return xarch.promoteTo(contextObjectRef, promotionTarget, targetObjectRef);
	}

	public synchronized ObjRef recontextualize(ObjRef contextObjectRef, String typeOfThing, ObjRef targetObjectRef){
		return xarch.recontextualize(contextObjectRef, typeOfThing, targetObjectRef);
	}

	public synchronized void remove(ObjRef baseObjectRef, String typeOfThing, ObjRef thingToRemove){
		xarch.remove(baseObjectRef, typeOfThing, thingToRemove);
	}

	public synchronized void remove(ObjRef baseObjectRef, String typeOfThing, ObjRef[] thingsToRemove){
		xarch.remove(baseObjectRef, typeOfThing, thingsToRemove);
	}

	public void renameXArch(String oldURI, String newURI){
		xarch.renameXArch(oldURI, newURI);
	}

	public synchronized ObjRef resolveHref(ObjRef xArchRef, String href){
		return xarch.resolveHref(xArchRef, href);
	}

	public synchronized ObjRef resolveXArchPath(ObjRef xArchRef, XArchPath xArchPath){
		return xarch.resolveXArchPath(xArchRef, xArchPath);
	}

	public synchronized String serialize(ObjRef xArchRef){
		return xarch.serialize(xArchRef);
	}

	public synchronized void set(ObjRef baseObjectRef, String typeOfThing, ObjRef value){
		xarch.set(baseObjectRef, typeOfThing, value);
	}

	public synchronized void set(ObjRef baseObjectRef, String typeOfThing, String value){
		xarch.set(baseObjectRef, typeOfThing, value);
	}

	public synchronized void writeToFile(ObjRef xArchRef, String fileName) throws IOException{
		xarch.writeToFile(xArchRef, fileName);
	}
}
