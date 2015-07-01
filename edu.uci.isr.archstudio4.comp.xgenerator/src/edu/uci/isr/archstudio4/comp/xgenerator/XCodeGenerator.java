package edu.uci.isr.archstudio4.comp.xgenerator;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jet.JET2Platform;


public class XCodeGenerator {

	//The plugin id of this project.
	private String pluginId = "edu.uci.isr.archstudio4.comp.xgenerator";
	
	public XCodeGenerator(){
	}
	
	public void generateComp(String archFilePath, String compID, String archPrescribedClassName, String userDefineClassName, IProgressMonitor monitor) throws CoreException{
		
		IFile archFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(archFilePath));
		Map variables = new HashMap();

		String prjName = archFilePath.substring(1, archFilePath.indexOf('/', 1));
		IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(prjName);
		IJavaProject jProj = JavaCore.create(proj);
		IType type = jProj.findType(archPrescribedClassName);
		if  (type != null){
			IFile targetFile = (IFile) type.getCompilationUnit().getCorrespondingResource();
			variables.put("prjName", targetFile.getProject().getDescription().getName());
		} else {
			variables.put("prjName", prjName);
		}
		
		variables.put("compid", compID);
		variables.put("archPrescribedClass", archPrescribedClassName);
		variables.put("userDefinedClass", userDefineClassName);
		IStatus status = JET2Platform.runTransformOnResource(pluginId, archFile, variables, monitor);
		if (!status.isOK()) {
			throw new CoreException(status);
		}
	}
}
