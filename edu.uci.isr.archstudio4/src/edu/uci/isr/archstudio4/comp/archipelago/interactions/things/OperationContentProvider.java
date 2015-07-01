package edu.uci.isr.archstudio4.comp.archipelago.interactions.things;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IWorkbenchSite;

import edu.uci.isr.archstudio4.comp.archipelago.interactions.logics.editing.MethodLabel;
import edu.uci.isr.archstudio4.comp.archipelago.interactions.logics.editing.MethodList;
import edu.uci.isr.archstudio4.util.XadlTreeUtils;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public class OperationContentProvider implements ITreeContentProvider {

	protected static final Object[] EMPTY_ARRAY = new Object[0];
	
	protected XArchFlatInterface xarch = null;
	protected ObjRef rootRef;
	protected int flags = 0;
	protected String prjName;
	
	public OperationContentProvider(XArchFlatInterface xarch, ObjRef rootRef, String prjName, int flags){
		this.xarch = xarch;
		this.rootRef = rootRef;
		this.prjName = prjName;
		this.flags = flags;
	}
	
	public boolean shouldShow(int type){
		return (type & flags) != 0;
	}
	
	private void addChildren(List<ObjRef> l, ObjRef ref, String childName, int childType){
		if(shouldShow(childType)){
			ObjRef[] childRefs = xarch.getAll(ref, childName);
			for(int i = 0; i < childRefs.length; i++){
				//if(isAllowedByFilter(childRefs[i], childType)){
					l.add(childRefs[i]);
				//}
			}
		}
	}
	
	//Get the implementation class name of an interface
	private String getImpClassName(ObjRef obj){
		ObjRef interfaceTypeRef = XadlUtils.resolveXLink(xarch, obj, "type");
		if(interfaceTypeRef == null){
			return null;
		}		
		if (!xarch.isInstanceOf(interfaceTypeRef, "implementation#InterfaceTypeImpl")){
				return null;				
		}
		
		ObjRef[] implementationRefs = xarch.getAll(interfaceTypeRef, "implementation");
		if(implementationRefs == null || implementationRefs.length == 0){
			return null;
		}
		ObjRef javaImplementationRef = null;
		for(ObjRef element: implementationRefs){
			if(xarch.isInstanceOf(element, "javaimplementation#JavaImplementation")){
				javaImplementationRef = element;
				break;
			}
		}
		if(javaImplementationRef == null){
			return null;
		}
		ObjRef classRef = (ObjRef)xarch.get(javaImplementationRef, "mainClass");
		if(classRef == null){
			return null;
		}
		ObjRef classNameRef = (ObjRef)xarch.get(classRef, "javaClassName");
		if(classNameRef == null){
			return null;
		}
		String className = (String)xarch.get(classNameRef, "value");
		if(className == null){
			return null;
		}
		return className;		
	}

	public Object[] getElements(Object inputElement){
		return getChildren(inputElement);
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof IWorkbenchSite){
			return new Object[]{rootRef};
		}
		else if(parentElement instanceof ObjRef){
			ObjRef ref = (ObjRef)parentElement;
			int type = XadlTreeUtils.getType(xarch, ref);
			List<ObjRef> childList = new ArrayList<ObjRef>();
			if(type == XadlTreeUtils.DOCUMENT){
				ObjRef typesContextRef = xarch.createContext(ref, "types");

				if(shouldShow(XadlTreeUtils.STRUCTURE)){
					ObjRef[] structureRefs = xarch.getAllElements(typesContextRef, "archStructure", ref);
					if(structureRefs != null){
						for(int i = 0; i < structureRefs.length; i++){
							//if(isAllowedByFilter(structureRefs[i], XadlTreeUtils.STRUCTURE)){
								childList.add(structureRefs[i]);
							//}
						}
					}
				}
				if(shouldShow(XadlTreeUtils.ANY_TYPE)){
					ObjRef archTypesRef = xarch.getElement(typesContextRef, "archTypes", ref);
					if(archTypesRef != null){
						childList.add(archTypesRef);
					}
				}
			}
			else if(type == XadlTreeUtils.STRUCTURE){
				addChildren(childList, ref, "component", XadlTreeUtils.COMPONENT);
				addChildren(childList, ref, "connector", XadlTreeUtils.CONNECTOR);
			}
			else if(type == XadlTreeUtils.COMPONENT){
				addChildren(childList, ref, "interface", XadlTreeUtils.COMPONENT_INTERFACE);
			}
			else if(type == XadlTreeUtils.CONNECTOR){
				addChildren(childList, ref, "interface", XadlTreeUtils.CONNECTOR_INTERFACE);
			}
			else if(type == XadlTreeUtils.COMPONENT_INTERFACE){
				String className = getImpClassName(ref);
				if (className != null){
					IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(prjName);
					IJavaProject jProj = JavaCore.create(proj);
					try {
						IType t = jProj.findType(className);
						//ICompilationUnit cu = type.getCompilationUnit();
						IMethod[] methods = t.getMethods();
						Collection<OperationLabel> operationLabels = new ArrayList<OperationLabel>();
						for (IMethod m: methods){
							operationLabels.add(new OperationLabel(m, ref, className));
						}
						return operationLabels.toArray();
					} catch (Exception e){
						;
					}					
				}
			}
			return childList.toArray();
		}
		return EMPTY_ARRAY;		
	}

	public Object getParent(Object element) {
		if(element instanceof ObjRef){
			return xarch.getParent((ObjRef)element);
		}
		else if(element instanceof OperationLabel) {
			return ((OperationLabel)element).getInterface();
		}
		return null;
	}

	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

}