package edu.uci.isr.archstudio4.comp.archipelago.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;

import edu.uci.isr.archstudio4.comp.booleannotation.BooleanGuardConverter;
import edu.uci.isr.archstudio4.comp.xmapper.XMapper;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public class Relationship
{
	final private XMapper xMpr;
	final private ObjRef xArchRef;
	final private XArchFlatInterface xarch;

	public Relationship(final XArchFlatInterface xarch, final ObjRef xArchRef, final XMapper xMpr)
	{
		this.xarch = xarch;
		this.xMpr = xMpr;
		this.xArchRef = xArchRef;
	}
	
	public Set<IFile> getRelatedComponentImpFiles(final ObjRef featureRef) throws CoreException
	{
		return getComponentImpFiles(getRelatedComponents(featureRef));
	}
	
	public boolean hasImpFile(final ObjRef... componentRef)
	{
		try {
			return !getComponentImpFiles(Arrays.asList(componentRef)).isEmpty();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Set<IFile> getComponentImpFiles(final List<ObjRef> componentRefs) throws CoreException
	{
		final String xArchURI = xarch.getXArchURI(xArchRef);
		final String prjName = xArchURI.substring(1, xArchURI.indexOf('/', 1));
		
		final IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(prjName);
		final IJavaProject javaProj = JavaCore.create(proj);

		final Set<IFile> srcFiles = new HashSet<IFile>();

		for (final ObjRef compRef : componentRefs) {
			final String compId = XadlUtils.getID(xarch, compRef);
			final String className = xMpr.getImpClassName(compId, 1);
			
			if (className != null) {
				final IType type = javaProj.findType(className);
				final IFile srcFile = (IFile) type.getCompilationUnit().getCorrespondingResource();
				
				if (srcFile.exists()) {
					srcFiles.add(srcFile);				
				}				
			}
		}

		return srcFiles;
	}
	
	public boolean isRelatedOptionalFeature(final String optionalFeatureId, final String componentId)
	{
		final ObjRef componentRef = xarch.getByID(componentId);
		final List<ObjRef> relatedFeatureRefs = getRelatedOptionalFeatures(componentRef);
		
		for (final ObjRef relatedFeatureRef : relatedFeatureRefs) {
			final String relatedFeatureRefId = XadlUtils.getID(xarch, relatedFeatureRef);
			
			if (optionalFeatureId.equals(relatedFeatureRefId)) {
				return true;
			}
		}
		
		return false;
	}
	
	public List<ObjRef> getRelatedOptionalFeatures(final ObjRef componentRef)
	{
		final Set<String> relatedFeatureHist = new HashSet<String>();
		final List<ObjRef> relatedFeatureRefs = new LinkedList<ObjRef>();
		
		if (xarch.isInstanceOf(componentRef, "implementationext#OptionalComponentImpl")) {
			getRelatedOptionalFeature(componentRef, relatedFeatureRefs, relatedFeatureHist);
		}
		
		for (final ObjRef interfaceRef : xarch.getAll(componentRef, "interface")) {
			if (xarch.isInstanceOf(interfaceRef, "options#OptionalInterface")) {
				getRelatedOptionalFeature(interfaceRef, relatedFeatureRefs, relatedFeatureHist);
			}
		}			

		return relatedFeatureRefs;
	}

	private void getRelatedOptionalFeature(final ObjRef componentRef, final List<ObjRef> relatedFeatureRefs, final Set<String> relatedFeatureHist)
	{
		final ObjRef optionalRef = (ObjRef) xarch.get(componentRef, "optional");
		
		if (optionalRef != null) {
			final String guardStr = BooleanGuardConverter.booleanGuardToString(xarch, optionalRef);
			
			if (guardStr != null && guardStr.contains(" == ")) {
				final String[] guardInfo = guardStr.split(" == ");
				final String featureSymbole = guardInfo[0].trim();
				
				final String featureId = featureSymbole.substring(0, "feature".length() + 8)
										+ "-" + featureSymbole.substring("feature".length() + 8, "feature".length() + 16)
										+ "-" + featureSymbole.substring("feature".length() + 16, "feature".length() + 24)
										+ "-" + featureSymbole.substring("feature".length() + 24);
				final Boolean isRelated = Boolean.valueOf(guardInfo[1].trim().replaceAll("\"", ""));
				
				if (isRelated && !relatedFeatureHist.contains(featureId)) {
					final ObjRef featureRef = xarch.getByID(featureId);

					relatedFeatureHist.add(featureId);
					relatedFeatureRefs.add(featureRef);
				}
			}
		}
	}
	
	public List<ObjRef> getRelatedComponents(final ObjRef featureRef)
	{
		final ObjRef archStructure = xarch.getElement(
			xarch.createContext(xArchRef, "types"), "archStructure", xArchRef
		);
		final ObjRef[] components = xarch.getAll(archStructure, "component");

		final ObjRef featureElements = (ObjRef) xarch.get(featureRef, "featureElements");
		final ObjRef[] archElements = xarch.getAll(featureElements, "archElement");

		final Map<String, ObjRef> relatedComponents = new HashMap<String, ObjRef>();

		for (final ObjRef archElement : archElements) {
			final String eId = ((String) xarch.get(archElement, "href")).substring(1);

			if (eId.contains("component")) {
				final ObjRef component = xarch.getByID(eId);

				relatedComponents.put(eId, component);
			} else if (eId.contains("interface")) {
				COMP_LOOP: for (final ObjRef component : components) {
					final String cId = XadlUtils.getID(xarch, component);
					final ObjRef[] interfaces = xarch.getAll(component, "interface");

					for (final ObjRef itf : interfaces) {
						if (eId.equals(xarch.get(itf, "id")) && !relatedComponents.containsKey(cId)) {
							relatedComponents.put(cId, component);
							break COMP_LOOP;
						}
					}
				}
			}
		}

		return new ArrayList<ObjRef>(relatedComponents.values());
	}
}
