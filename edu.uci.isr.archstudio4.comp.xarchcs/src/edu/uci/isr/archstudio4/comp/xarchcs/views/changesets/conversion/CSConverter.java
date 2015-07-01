package edu.uci.isr.archstudio4.comp.xarchcs.views.changesets.conversion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetInterface;
import edu.uci.isr.sysutils.UIDGenerator;
import edu.uci.isr.xarchflat.IXArchPropertyMetadata;
import edu.uci.isr.xarchflat.IXArchTypeMetadata;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatUtils;
import edu.uci.isr.xarchflat.XArchMetadataUtils;

public class CSConverter{

	private static ObjRef newChangeSet(XArchChangeSetInterface xarch, ObjRef xArchRef, String description){

		ObjRef changesetsContextRef = xarch.createContext(xArchRef, "changesets");
		ObjRef archChangeSetsRef = xarch.getElement(changesetsContextRef, "ArchChangeSets", xArchRef);
		ObjRef newChangeSetRef = xarch.create(changesetsContextRef, "ChangeSet");
		xarch.set(newChangeSetRef, "id", UIDGenerator.generateUID("ChangeSet"));
		XArchFlatUtils.setDescription(xarch, newChangeSetRef, "description", description);
		xarch.add(archChangeSetsRef, "changeSet", newChangeSetRef);

		List<ObjRef> appliedChangeSetRefs = new ArrayList<ObjRef>(Arrays.asList(xarch.getAppliedChangeSetRefs(xArchRef)));
		appliedChangeSetRefs.add(newChangeSetRef);
		xarch.setAppliedChangeSetRefs(xArchRef, appliedChangeSetRefs.toArray(new ObjRef[appliedChangeSetRefs.size()]), null);

		return newChangeSetRef;
	}

	private static ObjRef cloneTypeAndAttributes(XArchChangeSetInterface xarch, ObjRef destXArchRef, ObjRef srcRef){
		IXArchTypeMetadata srcType = xarch.getTypeMetadata(srcRef);
		ObjRef destRef;
		if(xarch.getXArch(srcRef).equals(xarch.getParent(srcRef))){
			destRef = xarch.createElement(xarch.createContext(destXArchRef, XArchMetadataUtils.getTypeContext(srcType.getType())), XArchMetadataUtils.getTypeName(srcType.getType()));
		}
		else{
			destRef = xarch.create(xarch.createContext(destXArchRef, XArchMetadataUtils.getTypeContext(srcType.getType())), XArchMetadataUtils.getTypeName(srcType.getType()));
		}

		for(IXArchPropertyMetadata property: srcType.getProperties()){
			// copy the attributes first so that the element can be resolved within a change set
			switch(property.getMetadataType()){

			case IXArchPropertyMetadata.ATTRIBUTE:
				String name = property.getName();
				String value = (String)xarch.get(srcRef, name);
				if(value != null){
					xarch.set(destRef, name, value);
				}
				break;

			case IXArchPropertyMetadata.ELEMENT:
				continue;

			case IXArchPropertyMetadata.ELEMENT_MANY:
				continue;
			}
		}

		return destRef;
	}

	private static ObjRef selectChangeSetForGuard(XArchChangeSetInterface xarch, ObjRef destXArchRef, Map<String, ObjRef> guardsToChangeSetRefs, ObjRef currentCSRef, ObjRef srcRef){
		IXArchTypeMetadata srcType = xarch.getTypeMetadata(srcRef);
		IXArchPropertyMetadata optionalProperty = srcType.getProperty("optional");
		if(optionalProperty != null && xarch.isAssignable("options#Optional", optionalProperty.getType())){
			ObjRef optionalRef = (ObjRef)xarch.get(srcRef, "optional");
			if(optionalRef != null){
				String guardString = ComparableBooleanGuardConverter.booleanGuardToString(xarch, optionalRef);
				if(guardString != null){
					ObjRef newCSRef = guardsToChangeSetRefs.get(guardString);
					if(newCSRef == null){
						guardsToChangeSetRefs.put(guardString, newCSRef = newChangeSet(xarch, destXArchRef, guardString));
					}
					return newCSRef;
				}
			}
		}

		return currentCSRef;
	}

	private static void copyToCS(XArchChangeSetInterface xarch, ObjRef plaXArchRef, ObjRef csXArchRef, Map<String, ObjRef> guardsToChangeSetRefs, Set<ObjRef> variantTypeRefs, ObjRef toCSRef, ObjRef srcRef, ObjRef destRef){

		IXArchTypeMetadata srcType = xarch.getTypeMetadata(srcRef);

		for(IXArchPropertyMetadata property: srcType.getProperties()){
			switch(property.getMetadataType()){
			case IXArchPropertyMetadata.ATTRIBUTE:
				// these were already copied by the parent in order to ensure that the element was resolvable before copying its children
				continue;

			case IXArchPropertyMetadata.ELEMENT: {
				ObjRef childSrcRef = (ObjRef)xarch.get(srcRef, property.getName());
				if(childSrcRef != null){
					if(xarch.isInstanceOf(childSrcRef, "options#Optional")){
						continue;
					}
					ObjRef childToCSRef = selectChangeSetForGuard(xarch, csXArchRef, guardsToChangeSetRefs, toCSRef, childSrcRef);
					xarch.setActiveChangeSetRef(csXArchRef, childToCSRef);
					ObjRef childDestRef = cloneTypeAndAttributes(xarch, csXArchRef, childSrcRef);
					xarch.set(destRef, property.getName(), childDestRef);
					copyToCS(xarch, plaXArchRef, csXArchRef, guardsToChangeSetRefs, variantTypeRefs, childToCSRef, childSrcRef, childDestRef);
				}
			}
				break;

			case IXArchPropertyMetadata.ELEMENT_MANY: {
				for(ObjRef childSrcRef: xarch.getAll(srcRef, property.getName())){
					if(xarch.isInstanceOf(childSrcRef, "variants#VariantComponentType")){
						variantTypeRefs.add(childSrcRef);
					}
					if(xarch.isInstanceOf(childSrcRef, "variants#VariantConnectorType")){
						variantTypeRefs.add(childSrcRef);
					}
					if(xarch.isInstanceOf(childSrcRef, "variants#Variant")){
						continue;
					}
					ObjRef childToCSRef = selectChangeSetForGuard(xarch, csXArchRef, guardsToChangeSetRefs, toCSRef, childSrcRef);
					xarch.setActiveChangeSetRef(csXArchRef, childToCSRef);
					ObjRef childDestRef = cloneTypeAndAttributes(xarch, csXArchRef, childSrcRef);
					xarch.add(destRef, property.getName(), childDestRef);
					copyToCS(xarch, plaXArchRef, csXArchRef, guardsToChangeSetRefs, variantTypeRefs, childToCSRef, childSrcRef, childDestRef);
				}
			}
				break;
			}
		}
	}

	private static void variantsToInstancesInCS(XArchChangeSetInterface xarch, ObjRef plaXArchRef, ObjRef csXArchRef, Map<String, ObjRef> guardsToChangeSetRefs, Set<ObjRef> variantTypeRefs, ObjRef baselineChangeSetRef, ObjRef plaXArchRef2, ObjRef csXArchRef2){
		for(ObjRef variantTypeRef: variantTypeRefs){
			for(ObjRef variantInstanceRef: xarch.getReferences(plaXArchRef, (String)xarch.get(variantTypeRef, "id"))){
				if(xarch.getXArchPath(variantInstanceRef).toTagsOnlyString().startsWith("xArch/archStructure")){

				}
			}
		}
	}

	public static void convertPLAtoCS(XArchChangeSetInterface xarch, ObjRef plaXArchRef, ObjRef csXArchRef){
		xarch.enableChangeSets(csXArchRef, null);
		ObjRef changesetsContextRef = xarch.createContext(csXArchRef, "changesets");
		ObjRef archChangeSetsRef = xarch.getElement(changesetsContextRef, "ArchChangeSets", csXArchRef);
		ObjRef baselineChangeSetRef = xarch.getAll(archChangeSetsRef, "ChangeSet")[0];

		Map<String, ObjRef> guardsToChangeSetRefs = new HashMap<String, ObjRef>();
		Set<ObjRef> variantTypeRefs = new HashSet<ObjRef>();
		guardsToChangeSetRefs.put(null, baselineChangeSetRef);

		copyToCS(xarch, plaXArchRef, csXArchRef, guardsToChangeSetRefs, variantTypeRefs, baselineChangeSetRef, plaXArchRef, csXArchRef);
		variantsToInstancesInCS(xarch, plaXArchRef, csXArchRef, guardsToChangeSetRefs, variantTypeRefs, baselineChangeSetRef, plaXArchRef, csXArchRef);

		xarch.set(archChangeSetsRef, "changeSetOrder", (String)xarch.get(archChangeSetsRef, "appliedChangeSets"));
	}
}
