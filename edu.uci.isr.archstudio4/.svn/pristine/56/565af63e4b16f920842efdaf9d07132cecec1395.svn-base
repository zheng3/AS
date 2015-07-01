package edu.uci.isr.archstudio4.comp.archipelago.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.util.AbstractEditDescriptionCellModifier;
import edu.uci.isr.archstudio4.comp.archipelago.util.Relationship;
import edu.uci.isr.archstudio4.comp.xmapper.XMapper;
import edu.uci.isr.archstudio4.comp.xmapper.XMapper.MonitoredTask;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.umkc.archstudio4.processor.export.AnnotationEditor;
import edu.umkc.archstudio4.processor.export.ProcessorUtils;
import edu.umkc.archstudio4.processor.export.SourceCodeRefactor;

public class FeaturesEditDescriptionCellModifier  extends AbstractEditDescriptionCellModifier
{
	final private XMapper xMapper;
	final private Pattern identifier = Pattern.compile("[a-zA-Z][ a-zA-Z0-9]*");

	public FeaturesEditDescriptionCellModifier(ArchipelagoServices services, ObjRef xArchRef)
	{
		super(services, xArchRef);
		this.xMapper = new XMapper(services, xArchRef);
	}
	
	public boolean canModify(Object element, String property)
	{
		if((element != null) && (element instanceof ObjRef)) {
			ObjRef targetRef = (ObjRef)element;
			if(AS.xarch.isInstanceOf(targetRef, "features#Feature")  || AS.xarch.isInstanceOf(targetRef, "features#Varient") ){
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void modify(Object element, String property, Object value)
	{
		final ObjRef targetRef = (ObjRef)element;

		final String newDesc = value.toString();
		final String oldDesc = XadlUtils.getDescription(AS.xarch, targetRef);

		if (oldDesc.equalsIgnoreCase(newDesc)) {
			super.modify(element, property, value);
		} else if (canRename(oldDesc, newDesc)) {
			try {
				final Relationship rel = new Relationship(AS.xarch, xArchRef, xMapper);
				final Set<IFile> relatedComponentImpFiles = rel.getRelatedComponentImpFiles(targetRef);
				
				if (!relatedComponentImpFiles.isEmpty()) {
					final Map<String, String> changes = new HashMap<String, String>();
					changes.put(ProcessorUtils.nomalizeFeatureName(oldDesc), ProcessorUtils.nomalizeFeatureName(newDesc));
					
					final boolean modified = updateFeatureRelatedCode(changes, relatedComponentImpFiles);
					
					if (!modified) {
						return;
					}
				}
			} catch (CoreException e) {
				e.printStackTrace();
				final Shell msgDialogShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				MessageDialog.openError(msgDialogShell, "Feature Rename", e.getMessage());
				return;
			}
	
			super.modify(element, property, value);
		}
	}
	
	private boolean updateFeatureRelatedCode(final Map<String, String> changes, final Set<IFile> relatedComponentImpFiles) throws CoreException
	{
		final Shell msgDialogShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		final String dialogMsg = "Feature related code exists and will be updated. Do you want to continue?";
		boolean confirmed = MessageDialog.openConfirm(msgDialogShell, "Rename Feature", dialogMsg);

		if (confirmed) {
			final List<MonitoredTask> tasks = new ArrayList<MonitoredTask>(relatedComponentImpFiles.size());
			for (final IFile srcFile : relatedComponentImpFiles) {
				final MonitoredTask processImpFile = new MonitoredTask("Processing file " + srcFile.getName()) {
					@Override
					public void run(IProgressMonitor monitor) {
						final SourceCodeRefactor refactor = new SourceCodeRefactor();
						refactor.refactorFeatures(changes, srcFile);
					}
				};
				tasks.add(processImpFile);
			}
			
			xMapper.executeMonitoredTasks(tasks);
			renameFeatureOpt(changes);						
		}

		return confirmed;
	}

	private void renameFeatureOpt(final Map<String, String> changes) throws CoreException
	{
		final String featureClassName = AnnotationEditor.FEATURE_OPT_CLASS;
		final String xArchURI = AS.xarch.getXArchURI(xArchRef);
		final String prjName = xArchURI.substring(1, xArchURI.indexOf('/', 1));
		
		final IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(prjName);
		final IJavaProject javaProj = JavaCore.create(proj);
		final IType type = javaProj.findType(featureClassName);
		final IFile featureOptFile = (IFile) type.getCompilationUnit().getCorrespondingResource();
		
		final AnnotationEditor annotationEditor = new AnnotationEditor();
		annotationEditor.editAnnotation(changes, new HashSet<IFile>(Arrays.asList(featureOptFile)));
	}
	
	private boolean canRename(String oldDesc, String newDesc)
	{
		if (newDesc.trim().isEmpty() || !identifier.matcher(newDesc).matches()) {
			MessageDialog.openError(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Feature Rename",
				"Feature name \"" + newDesc + "\" is invalid!"
			);
			return false;
		}

		final ObjRef archFeature = AS.xarch.getElement(
			AS.xarch.createContext(xArchRef, "features"), "archFeature", xArchRef
		);

		for (final ObjRef featureRef : AS.xarch.getAll(archFeature, "feature")) {
			final String existDesc = XadlUtils.getDescription(AS.xarch, featureRef);
			if (newDesc.equalsIgnoreCase(existDesc)) {
				MessageDialog.openError(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"Feature Rename",
					"Another feature with name \"" + existDesc + "\" already exists!"
				);
				return false;
			}
		}
		return true;
	}
}