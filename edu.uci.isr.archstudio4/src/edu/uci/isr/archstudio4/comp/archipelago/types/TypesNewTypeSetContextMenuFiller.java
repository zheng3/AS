package edu.uci.isr.archstudio4.comp.archipelago.types;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.IArchipelagoTreeContextMenuFiller;
import edu.uci.isr.sysutils.UIDGenerator;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;

public class TypesNewTypeSetContextMenuFiller implements IArchipelagoTreeContextMenuFiller{
	protected TreeViewer viewer = null;
	protected ArchipelagoServices AS =  null;
	protected ObjRef xArchRef = null;
	
	public TypesNewTypeSetContextMenuFiller(TreeViewer viewer, ArchipelagoServices AS, ObjRef xArchRef){
		this.viewer = viewer;
		this.AS = AS;
		this.xArchRef = xArchRef;
	}
	
	
	public void fillContextMenu(IMenuManager m, Object[] selectedNodes){
		if((selectedNodes != null) && (selectedNodes.length == 1)){
			Object selectedNode = selectedNodes[0];
			if((selectedNode != null) && (selectedNode.equals(xArchRef))){
				IAction newTypeSetAction = new Action("Create Type Set"){
					public void run(){
						createNewTypeSet();
					}
				};
				ObjRef archTypesRef = XadlUtils.getArchTypes(AS.xarch, xArchRef);
				if(archTypesRef != null){
					newTypeSetAction.setEnabled(false);
				}
				
				m.add(newTypeSetAction);
			}
		}
	}
	
	protected void createNewTypeSet(){
		ObjRef typesContextRef = AS.xarch.createContext(xArchRef, "types");
		ObjRef archTypesRef = AS.xarch.createElement(typesContextRef, "archTypes");
		AS.xarch.add(xArchRef, "object", archTypesRef);
		
		if(archTypesRef != null){
			//Create built-in connector types.
			String[] in = {"in"};
			String[] out = {"out"};
			AS.xarch.add(archTypesRef, "connectorType", createBuiltInConnectorType("edu.uci.isr.myx.conn.EventPumpConnector", "EventPump Type", in, out));
			AS.xarch.add(archTypesRef, "connectorType", createBuiltInConnectorType("edu.uci.isr.myx.conn.SynchronousProxyConnector", "Synchronous Type", in, out));
			String[] outSignatures = {"asynch", "synch"};
			AS.xarch.add(archTypesRef, "connectorType", createBuiltInConnectorType("edu.uci.isr.myx.conn.SynchAsynchPumpConnector", "EventPump Type - Sync & Async", in, outSignatures));
		}				
	}
	
	private ObjRef createBuiltInConnectorType(String imp, String desp, String[] inSignatures, String[] outSignatures){		
		ObjRef typesContextRef = AS.xarch.createContext(xArchRef, "types");
		ObjRef newTypeRef = AS.xarch.create(typesContextRef, "connectorType");
		String newID = UIDGenerator.generateUID("connectorType");
		AS.xarch.set(newTypeRef, "id", newID);
		XadlUtils.setDescription(AS.xarch, newTypeRef, desp);
		//Add the implementation information
		ObjRef varContextRef = AS.xarch.createContext(xArchRef, "variants");
		AS.xarch.promoteTo(varContextRef, "VariantConnectorType", newTypeRef);
		ObjRef impContextRef = AS.xarch.createContext(xArchRef, "implementation");
		AS.xarch.promoteTo(impContextRef, "VariantConnectorTypeImpl", newTypeRef);
		ObjRef implementationRef = AS.xarch.create(impContextRef, "implementation");
		AS.xarch.add(newTypeRef, "implementation", implementationRef);
		ObjRef javaImpContextRef = AS.xarch.createContext(xArchRef, "javaimplementation");
		AS.xarch.promoteTo(javaImpContextRef, "JavaImplementation", implementationRef);
		ObjRef classRef = AS.xarch.create(javaImpContextRef, "JavaClassFile");
		AS.xarch.set(implementationRef, "mainClass", classRef);
		ObjRef classNameRef = AS.xarch.create(javaImpContextRef, "JavaClassName");
		AS.xarch.set(classRef, "javaClassName", classNameRef);
		AS.xarch.set(classNameRef, "value", imp);
		//Add signatures whose directions are in.
		for (String sigName: inSignatures){
			ObjRef signatureRef = AS.xarch.create(typesContextRef, "signature");
			newID = UIDGenerator.generateUID("signature");
			AS.xarch.set(signatureRef, "id", newID);
			XadlUtils.setDescription(AS.xarch, signatureRef, "in");
			XadlUtils.setDirection(AS.xarch, signatureRef, "in");
			AS.xarch.add(newTypeRef, "signature", signatureRef);			
			AS.xarch.promoteTo(impContextRef, "SignatureImpl", signatureRef);
			ObjRef signatureImplementationRef = AS.xarch.create(impContextRef, "implementation");
			AS.xarch.add(signatureRef, "implementation", signatureImplementationRef);
			ObjRef lookupImpContextRef = AS.xarch.createContext(xArchRef, "lookupimplementation");
			AS.xarch.promoteTo(lookupImpContextRef, "LookupImplementation", signatureImplementationRef);
			ObjRef lookupNameRef = AS.xarch.create(lookupImpContextRef, "LookupName");
			AS.xarch.set(signatureImplementationRef, "name", lookupNameRef);
			AS.xarch.set(lookupNameRef, "value", sigName);			
		}
		//Add signatures whose directions are out.
		for (String sigName: outSignatures){
			ObjRef signatureRef = AS.xarch.create(typesContextRef, "signature");
			newID = UIDGenerator.generateUID("signature");
			AS.xarch.set(signatureRef, "id", newID);
			XadlUtils.setDescription(AS.xarch, signatureRef, "out");
			XadlUtils.setDirection(AS.xarch, signatureRef, "out");
			AS.xarch.add(newTypeRef, "signature", signatureRef);			
			AS.xarch.promoteTo(impContextRef, "SignatureImpl", signatureRef);
			ObjRef signatureImplementationRef = AS.xarch.create(impContextRef, "implementation");
			AS.xarch.add(signatureRef, "implementation", signatureImplementationRef);
			ObjRef lookupImpContextRef = AS.xarch.createContext(xArchRef, "lookupimplementation");
			AS.xarch.promoteTo(lookupImpContextRef, "LookupImplementation", signatureImplementationRef);
			ObjRef lookupNameRef = AS.xarch.create(lookupImpContextRef, "LookupName");
			AS.xarch.set(signatureImplementationRef, "name", lookupNameRef);
			AS.xarch.set(lookupNameRef, "value", sigName);			
		}		
		return newTypeRef;		
	}

}
