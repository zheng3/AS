package edu.uci.isr.archstudio4.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import edu.uci.isr.archstudio4.comp.archipelago.util.Relationship;
import edu.uci.isr.archstudio4.comp.xmapper.XMapper;
import edu.uci.isr.archstudio4.comp.xmapper.XMapper.MonitoredTask;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;
import edu.umkc.archstudio4.processor.export.ProcessorUtils;
import edu.umkc.archstudio4.processor.export.SourceCodeCommenter;

public abstract class CodeSynchronizingAction extends Action
{
	protected abstract XMapper getXMapper();
	protected abstract ObjRef getXArchRef();
	protected abstract XArchFlatInterface getXArchFlatInterface();

	protected abstract List<ObjRef> getTargetFeatures();
	protected abstract List<ObjRef> getTargetComponents();

	protected CodeSynchronizingAction(final String text)
	{
		super(text);
	}
	
	@Override
	final public void run()
	{
		saveEditor();
		synchronizeCode();
	}
	
	private void saveEditor()
	{
		final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		final IEditorPart editor = page.getActiveEditor();

		page.saveEditor(editor, false);
	}

	private void synchronizeCode()
	{
		final List<MonitoredTask> synchonizationTasks = prepareSynchronizationTasks();
		getXMapper().executeMonitoredTasks(synchonizationTasks);
	}
	
	private List<MonitoredTask> prepareSynchronizationTasks()
	{
		final List<ObjRef> targetFeatureRefs = getTargetFeatures();

		if (targetFeatureRefs.size() == 1) {
			return prepareSynchronizationByRemovingTasks(targetFeatureRefs.get(0));
		} else {
			return prepareSynchronizationByRetainingTasks();
		}
	}

	private List<MonitoredTask> prepareSynchronizationByRemovingTasks(final ObjRef targetFeatureRef)
	{
		final String featureId = XadlUtils.getID(getXArchFlatInterface(), targetFeatureRef);
		final Relationship rel = new Relationship(getXArchFlatInterface(), getXArchRef(), getXMapper());
		
		final List<ObjRef> targetComponentRefs = getTargetComponents();
		final List<MonitoredTask> synchronizationTasks = getXMapper().prepareComponentCodeTasks(targetComponentRefs);

		for (final ObjRef componentRef : targetComponentRefs) {
			final String componentId = XadlUtils.getID(getXArchFlatInterface(), componentRef);
			final boolean isRelated = rel.isRelatedOptionalFeature(featureId, componentId);
			
			if (!isRelated) {
				try {
					final IFile componentImpFile = getXMapper().getComponentImpFile(componentId, 1);

					synchronizationTasks.add(new MonitoredTask("Processing " + componentImpFile.getName()) {
						@Override
						public void run(IProgressMonitor monitor) {
							final SourceCodeCommenter commenter = new SourceCodeCommenter();
							final String featureName = XadlUtils.getDescription(getXArchFlatInterface(), targetFeatureRef);

							commenter.removeUnrelatedFeatures(Arrays.asList(ProcessorUtils.nomalizeFeatureName(featureName)), componentImpFile);
						}
					});
				} catch (CoreException e) {
					final Shell msgDialogShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					MessageDialog.openError(msgDialogShell, "Code Synchronization Error", e.getMessage());
				}
			}
		}
		return synchronizationTasks;
	}
	
	private List<MonitoredTask> prepareSynchronizationByRetainingTasks()
	{
		final Relationship rel = new Relationship(getXArchFlatInterface(), getXArchRef(), getXMapper());

		final List<ObjRef> targetComponentRefs = getTargetComponents();
		final List<MonitoredTask> synchronizationTasks = getXMapper().prepareComponentCodeTasks(targetComponentRefs);

		for (final ObjRef componentRef : targetComponentRefs) {
			final List<ObjRef> relatedFeatureRefs = rel.getRelatedOptionalFeatures(componentRef);
			final List<String> relatedFeatureNames = new ArrayList<String>(relatedFeatureRefs.size());

			for (ObjRef relatedFeatureRef : relatedFeatureRefs) {
				final String relatedFeatureName = XadlUtils.getDescription(getXArchFlatInterface(), relatedFeatureRef);
				relatedFeatureNames.add(ProcessorUtils.nomalizeFeatureName(relatedFeatureName));
			}

			try {
				final String componentId = XadlUtils.getID(getXArchFlatInterface(), componentRef);
				final IFile componentImpFile = getXMapper().getComponentImpFile(componentId, 1);
				
				synchronizationTasks.add(new MonitoredTask("Processing " + componentImpFile.getName()) {									
					@Override
					public void run(IProgressMonitor monitor) {
						final SourceCodeCommenter commenter = new SourceCodeCommenter();
						
						commenter.retainRelatedFeatures(relatedFeatureNames, componentImpFile);						
					}
				});
			} catch (CoreException e) {
				final Shell msgDialogShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				MessageDialog.openError(msgDialogShell, "Code Synchronization Error", e.getMessage());
			}
		}
		return synchronizationTasks;
	}
}
