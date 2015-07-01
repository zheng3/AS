package edu.uci.isr.archstudio4.comp.archipelago.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.util.AbstractRemoveContextMenuFiller;
import edu.uci.isr.archstudio4.comp.archipelago.util.Relationship;
import edu.uci.isr.archstudio4.comp.booleannotation.ParseException;
import edu.uci.isr.archstudio4.comp.booleannotation.TokenMgrError;
import edu.uci.isr.archstudio4.comp.xmapper.XMapper;
import edu.uci.isr.archstudio4.comp.xmapper.XMapper.MonitoredTask;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.umkc.archstudio4.processor.export.AnnotationEditor;
import edu.umkc.archstudio4.processor.export.ProcessorUtils;
import edu.umkc.archstudio4.processor.export.SourceCodeCommenter;

public class FeaturesRemoveContextMenuFiller extends AbstractRemoveContextMenuFiller{
	final private XMapper xMapper;
	
	public FeaturesRemoveContextMenuFiller(TreeViewer viewer, ArchipelagoServices services, ObjRef xArchRef){
		super(viewer, services, xArchRef);
		this.xMapper = new XMapper(services, xArchRef);
	}
	
	protected boolean matches(Object node) {
		if(node != null){
			if(node instanceof ObjRef){
				ObjRef targetRef = (ObjRef)node;
				if(AS.xarch.isInstanceOf(targetRef, "features#Feature")){
					return true;
				}else if(AS.xarch.isInstanceOf(targetRef, "features#Varient")){
					return true;
				}
			}
		}
		return false;
	}
	
	protected void remove(final ObjRef featureRef)
	{
		if (featureRef != null) {
			if (AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature")) {
				final Relationship rel =  new Relationship(AS.xarch, xArchRef, this.xMapper);		
				final List<ObjRef> relatedComponents = rel.getRelatedComponents(featureRef);

				if (!relatedComponents.isEmpty() && hasImplementation(relatedComponents)) {
					try {
						final List<ObjRef> remainComponents = getRemainComponents(featureRef, relatedComponents);
						final boolean removed = processFeatureRelatedCode(featureRef, remainComponents);	
						
						if (!removed) {
							return;
						}
					} catch (CoreException e) {
						e.printStackTrace();
						final Shell msgDialogShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
						MessageDialog.openError(msgDialogShell, "Remove Feature", e.getMessage());
						return;
					}
				} else {
					removeOptionalFeature(featureRef);						
				}
			} else if (AS.xarch.isInstanceOf(featureRef, "features#AlternativeFeature")){
				removeAlternativeFeature(featureRef);
			} else if (AS.xarch.isInstanceOf(featureRef, "features#Varient")){
				removeVarient(featureRef);
			}
		}
		super.remove(featureRef);
	}

	private boolean processFeatureRelatedCode(final ObjRef featureRef, final List<ObjRef> componentRefs) throws CoreException
	{
		final String dialogMsg = "Feature related code exists and will be updated. Do you want to continue?";
		final Shell msgDialogShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		final boolean confirmed = MessageDialog.openConfirm(msgDialogShell, "Remove Feature", dialogMsg);

		if (confirmed) {
			final String featureName = XadlUtils.getDescription(AS.xarch, featureRef);

			removeOptionalFeature(featureRef);

			final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			final IEditorPart editor = page.getActiveEditor();
			page.saveEditor(editor, false);

			final List<MonitoredTask> tasks = xMapper.prepareComponentCodeTasks(componentRefs);
			final Relationship rel =  new Relationship(AS.xarch, xArchRef, this.xMapper);
			for (final IFile impFile : rel.getComponentImpFiles(componentRefs)) {
				final MonitoredTask processImpFile = new MonitoredTask("Processing file " + impFile.getName()) {
					@Override
					public void run(IProgressMonitor monitor) {
						final SourceCodeCommenter commenter = new SourceCodeCommenter();
						commenter.removeUnrelatedFeatures(Arrays.asList(ProcessorUtils.nomalizeFeatureName(featureName)), impFile);
					}
				};
				tasks.add(processImpFile);					
			}

			xMapper.executeMonitoredTasks(tasks);
			removeFeatureOpt(featureName);
		}

		return confirmed;			
	}

	private boolean hasImplementation(List<ObjRef> componentRefs)
	{
		for (ObjRef componentRef : componentRefs) {
			final String componentId = XadlUtils.getID(AS.xarch, componentRef);
			
			try {
				final String archPrescribedClassName = xMapper.getImpClassName(componentId, 0);
				final String userDefineClassName = xMapper.getImpClassName(componentId, 1);
				
				final boolean hasImplementation = archPrescribedClassName != null && !archPrescribedClassName.isEmpty()
													&& userDefineClassName != null && !userDefineClassName.isEmpty();
				
				if (hasImplementation) {
					return true;
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
			
		}
		
		return false;
	}
	
	private List<ObjRef> getRemainComponents(final ObjRef featureRef, final List<ObjRef> componentRefs)
	{
		final ObjRef archFeature = AS.xarch.getElement(
			AS.xarch.createContext(xArchRef, "features"), "archFeature", xArchRef
		);

		final Set<String> remainComponentIds = new HashSet<String>();
		final String featureId = XadlUtils.getID(AS.xarch, featureRef);
		final Relationship rel =  new Relationship(AS.xarch, xArchRef, this.xMapper);

		for (ObjRef fRef : AS.xarch.getAll(archFeature, "feature")) {
			final String fId = XadlUtils.getID(AS.xarch, fRef);

			if (!fId.equals(featureId)) {
				for (final ObjRef cRef : rel.getRelatedComponents(fRef)) {
					final String cId = XadlUtils.getID(AS.xarch, cRef);
					remainComponentIds.add(cId);
				}
			}
		}

		final Set<String> componentIds = new HashSet<String>();
		for (final ObjRef componentRef : componentRefs) {
			final String componentId = XadlUtils.getID(AS.xarch, componentRef);
			componentIds.add(componentId);
		}

		remainComponentIds.retainAll(componentIds);
		
		final List<ObjRef> remainComponentRefs = new ArrayList<ObjRef>(remainComponentIds.size());
		for (final String cId : remainComponentIds) {
			remainComponentRefs.add(AS.xarch.getByID(cId));
		}
		
		return remainComponentRefs;
	}

	private void removeFeatureOpt(final String featureName) throws JavaModelException
	{
		final String featureClassName = AnnotationEditor.FEATURE_OPT_CLASS;
		final String xArchURI = AS.xarch.getXArchURI(xArchRef);
		final String prjName = xArchURI.substring(1, xArchURI.indexOf('/', 1));
		
		final IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(prjName);
		final IJavaProject javaProj = JavaCore.create(proj);
		final IType type = javaProj.findType(featureClassName);
		final IFile featureOptFile = (IFile) type.getCompilationUnit().getCorrespondingResource();
		
		final Map<String, String> changes = new HashMap<String, String>();
		changes.put(ProcessorUtils.nomalizeFeatureName(featureName), "");
		
		final AnnotationEditor annotationEditor = new AnnotationEditor();
		annotationEditor.editAnnotation(changes, new HashSet<IFile>(Arrays.asList(featureOptFile)));
	}

	private void removeVarient(ObjRef targetRef) {

		ObjRef featureElement = (ObjRef) AS.xarch.get(targetRef, "featureElements");
		if(featureElement == null){
			ObjRef parent = AS.xarch.getParent(targetRef);
			
			AS.xarch.remove(parent, "Varient", targetRef);
			return;
		}
		ObjRef[] archElementRef = AS.xarch.getAll(featureElement, "archElement");
		
		for (int i = 0; i < archElementRef.length; i++) {
			//String id =
			ObjRef archElementLink = archElementRef[i];
			
			String id = (String) AS.xarch.get(archElementLink, "href");
			id = id.substring(1); //we need to remove # infront of the element ids
			//System.out.println(id);
			
			ObjRef compRef = AS.xarch.getByID(id);
			if(compRef != null){/*
			String tagName = AS.xarch.getElementName(compRef);
			if(tagName != null){
				ObjRef parentRef = AS.xarch.getParent(compRef);
				if(parentRef != null){
					AS.xarch.remove(parentRef, tagName, compRef);
				}
			}
			*/

				
				ObjRef optionalRef = (ObjRef)AS.xarch.get(compRef, "optional");
				if (optionalRef == null){
					return;
				}
				String expr = AS.booleanNotation.booleanGuardToString(optionalRef);
				if((expr == null)||(expr.indexOf("||") == -1)){ //remove this element
					String tagName = AS.xarch.getElementName(compRef);
					if(tagName != null){
						ObjRef parentRef = AS.xarch.getParent(compRef);
						if(parentRef != null){
							AS.xarch.remove(parentRef, tagName, compRef);
						}
					}
				}else{ //edit the guard condition
					
					try {
						adjustGuardCondition(XadlUtils.getID(AS.xarch, targetRef), compRef);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TokenMgrError e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			
				
			}
		
			 
			
		}
	

		ObjRef parent = AS.xarch.getParent(targetRef);
		
		AS.xarch.remove(parent, "Varient", targetRef);
		
	
		
	}

	private void removeAlternativeFeature(ObjRef targetRef) {
		
		
		
		ObjRef featureVarientsRef = (ObjRef) AS.xarch.get(targetRef, "FeatureVarients");
		if(featureVarientsRef == null){
			ObjRef parent = AS.xarch.getParent(targetRef);
			
			AS.xarch.remove(parent, "Feature", targetRef);
			return;
		}
		ObjRef[] varientRef = AS.xarch.getAll(featureVarientsRef, "Varient"); 
		for(int j = 0; j < varientRef.length;j++){
			ObjRef featureElement = (ObjRef) AS.xarch.get(varientRef[j], "featureElements");
			if(featureElement == null){
				continue;
			}
			ObjRef[] archElementRef = AS.xarch.getAll(featureElement, "archElement");
			
			for (int i = 0; i < archElementRef.length; i++) {
				//String id =
				ObjRef archElementLink = archElementRef[i];
				
				String id = (String) AS.xarch.get(archElementLink, "href");
				id = id.substring(1); //we need to remove # infront of the element ids
				//System.out.println(id);
				
				
				
				
				
				ObjRef compRef = AS.xarch.getByID(id);
				if(compRef != null){
				/*String tagName = AS.xarch.getElementName(compRef);
				if(tagName != null){
					ObjRef parentRef = AS.xarch.getParent(compRef);
					if(parentRef != null){
						AS.xarch.remove(parentRef, tagName, compRef);
					}
				}*/
					

					
					ObjRef optionalRef = (ObjRef)AS.xarch.get(compRef, "optional");
					if (optionalRef == null){
						return;
					}
					String expr = AS.booleanNotation.booleanGuardToString(optionalRef);
					if((expr == null)||(expr.indexOf("||") == -1)){ //remove this element
						String tagName = AS.xarch.getElementName(compRef);
						if(tagName != null){
							ObjRef parentRef = AS.xarch.getParent(compRef);
							if(parentRef != null){
								AS.xarch.remove(parentRef, tagName, compRef);
							}
						}
					}else{ //edit the guard condition
						
						try {
							adjustGuardCondition(XadlUtils.getID(AS.xarch, targetRef), compRef);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (TokenMgrError e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				
				
				}
			
				 
				
			}
		}
		
		ObjRef featureElementsRef = (ObjRef) AS.xarch.get(targetRef, "featureElements");
		if(featureElementsRef !=null){
		removeAlterFeatureElements(targetRef);
		}
		ObjRef parent = AS.xarch.getParent(targetRef);
		
		AS.xarch.remove(parent, "Feature", targetRef);
		
		
		
		return;
		
	}
	
	private void removeAlterFeatureElements(ObjRef targetRef){
		ObjRef featureElementsRef = (ObjRef) AS.xarch.get(targetRef, "featureElements");
		ObjRef[] archElementRef = AS.xarch.getAll(featureElementsRef, "archElement"); 
		
		for (int i = 0; i < archElementRef.length; i++) {
			//String id =
			ObjRef archElementLink = archElementRef[i];
			
			String id = (String) AS.xarch.get(archElementLink, "href");
			id = id.substring(1); //we need to remove # infront of the element ids
			//System.out.println(id);
			
			ObjRef compRef = AS.xarch.getByID(id);
			
			
			
			
			
			if(compRef != null){
				
				ObjRef optionalRef = (ObjRef)AS.xarch.get(compRef, "optional");
				if (optionalRef == null){
					return;
				}
				String expr = AS.booleanNotation.booleanGuardToString(optionalRef);
				if((expr == null)||(expr.indexOf("||") == -1)){ //remove this element
					String tagName = AS.xarch.getElementName(compRef);
					if(tagName != null){
						ObjRef parentRef = AS.xarch.getParent(compRef);
						if(parentRef != null){
							AS.xarch.remove(parentRef, tagName, compRef);
						}
					}
				}else{ //edit the guard condition
					
					try {
						adjustGuardCondition(XadlUtils.getID(AS.xarch, targetRef), compRef);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TokenMgrError e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			
			}
		
			 
			
		}
	}

	private void removeOptionalFeature(ObjRef targetRef) {
		//ObjRef featureContextRef = AS.xarch.createContext(xArchRef, "features");
		//ObjRef featureRef = AS.xarch.getByID(selectedFeature);
		//ObjRef featureElements = AS.xarch.getElement(targetRef, "featureElements",xArchRef );
		ObjRef featureElementsRef = (ObjRef) AS.xarch.get(targetRef, "featureElements");
		ObjRef[] archElementRef = AS.xarch.getAll(featureElementsRef, "archElement"); 
		
		for (int i = 0; i < archElementRef.length; i++) {
			//String id =
			ObjRef archElementLink = archElementRef[i];
			
			String id = (String) AS.xarch.get(archElementLink, "href");
			id = id.substring(1); //we need to remove # infront of the element ids
			//System.out.println(id);
			
			ObjRef compRef = AS.xarch.getByID(id);
			
			
			
			
			
			if(compRef != null){
				
				ObjRef optionalRef = (ObjRef)AS.xarch.get(compRef, "optional");
				if (optionalRef == null){
					return;
				}
				String expr = AS.booleanNotation.booleanGuardToString(optionalRef);
				if((expr == null)||(expr.indexOf("||") == -1)){ //remove this element
					String tagName = AS.xarch.getElementName(compRef);
					if(tagName != null){
						ObjRef parentRef = AS.xarch.getParent(compRef);
						if(parentRef != null){
							AS.xarch.remove(parentRef, tagName, compRef);
						}
					}
				}else{ //edit the guard condition
					
					try {
						adjustGuardCondition(XadlUtils.getID(AS.xarch, targetRef), compRef);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TokenMgrError e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			
			}
		
			 
			
		}
		
		
		
		ObjRef parent = AS.xarch.getParent(targetRef);
		
		AS.xarch.remove(parent, "Feature", targetRef);
		
		
		
		return;
		
	}
	
	
	protected boolean adjustGuardCondition(String selectedFeature, ObjRef eltRef) throws ParseException, TokenMgrError {
		
		ObjRef featureRef = AS.xarch.getByID(selectedFeature);
		selectedFeature = selectedFeature.replaceAll("-", "");
		ObjRef optionalRef = (ObjRef)AS.xarch.get(eltRef, "optional");
		if (optionalRef == null){
			return true;
		}
		String expr = AS.booleanNotation.booleanGuardToString(optionalRef);
		if((expr == null)||(expr.indexOf("||") == -1)){
			
			AS.xarch.clear(eltRef, "optional");
			//remove link form the feature lists
			
			return true;
		}
		
		if(AS.xarch.isInstanceOf(featureRef, "features#Varient")) {
			

			ObjRef varients = AS.xarch.getParent(featureRef);
			ObjRef Altrfeature = AS.xarch.getParent(varients);
			selectedFeature = XadlUtils.getID(AS.xarch, Altrfeature);
			selectedFeature = selectedFeature.replaceAll("-", "");
			
			int index = expr.indexOf(selectedFeature);
			
			if(index!=-1){
				int  orIndex = expr.indexOf("||", index);
				int parathIndex = expr.indexOf(")", index);
				if(orIndex == -1){
					StringBuilder exprBuilder = new StringBuilder(expr);
					orIndex = expr.lastIndexOf("||", index);
					exprBuilder.replace(orIndex, parathIndex, "");
					expr = exprBuilder.toString();
				}else{
					if(orIndex < parathIndex){
						StringBuilder exprBuilder = new StringBuilder(expr);
						exprBuilder.replace(index, orIndex+2, "");
						expr = exprBuilder.toString();
					}else{
						StringBuilder exprBuilder = new StringBuilder(expr);
						orIndex = expr.lastIndexOf("||", index);
						exprBuilder.replace(orIndex, parathIndex, "");
						expr = exprBuilder.toString();
					}
				}
				
				ObjRef guardRef = AS.booleanNotation.parseBooleanGuard(expr, xArchRef);
				AS.xarch.set(optionalRef, "guard", guardRef);
			}else{
				//unexpected no selected feature is there;
				return false;
			}
			
		
			
			
		}else if(AS.xarch.isInstanceOf(featureRef, "features#OptionalFeature") ){
			
			int index = expr.indexOf(selectedFeature);
			
			if(index!=-1){
				int  orIndex = expr.indexOf("||", index);
				int parathIndex = expr.indexOf(")", index);
				if(orIndex == -1){
					StringBuilder exprBuilder = new StringBuilder(expr);
					orIndex = expr.lastIndexOf("||", index);
					exprBuilder.replace(orIndex, parathIndex, "");
					expr = exprBuilder.toString();
				}else{
					if(orIndex < parathIndex){
						StringBuilder exprBuilder = new StringBuilder(expr);
						exprBuilder.replace(index, orIndex+2, "");
						expr = exprBuilder.toString();
					}else{
						StringBuilder exprBuilder = new StringBuilder(expr);
						orIndex = expr.lastIndexOf("||", index);
						exprBuilder.replace(orIndex, parathIndex, "");
						expr = exprBuilder.toString();
					}
				}
				
				ObjRef guardRef = AS.booleanNotation.parseBooleanGuard(expr, xArchRef);
				AS.xarch.set(optionalRef, "guard", guardRef);
				
			}else{
				//unexpected no selected feature is there;
				return false;
			}
			
		}
		
		return false;
		
	}
}

