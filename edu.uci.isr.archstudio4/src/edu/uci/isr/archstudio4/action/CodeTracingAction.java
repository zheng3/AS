package edu.uci.isr.archstudio4.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.xmapper.XMapper;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.umkc.archstudio4.processor.export.CodeFragment;
import edu.umkc.archstudio4.processor.export.CodeFragmentHighlighter;
import edu.umkc.archstudio4.processor.export.SourceCodeTracer;

public abstract class CodeTracingAction
{
	private final XMapper xMpr;
	private final ObjRef xArchRef;
	private final ArchipelagoServices AS;

	private final String eltXArchID;
	
	public abstract Map<String, ObjRef> getFeatureRefs();

	public CodeTracingAction(ArchipelagoServices AS, ObjRef xArchRef, XMapper xMpr, String eltXArchID)
	{
		this.AS = AS;
		this.xMpr = xMpr;
		this.xArchRef = xArchRef;
		this.eltXArchID = eltXArchID;
	}

	final public void trace()
	{
		try {
			final IFile targetFile = getTargetFile();

			if (targetFile.exists()) {
				final Map<String, ObjRef> featureRefs = getFeatureRefs();

				final SourceCodeTracer tracer = new SourceCodeTracer();
				final Map<CodeFragment, Set<String>> fragmentToFeatureNameMap = tracer.trace(featureRefs.keySet(), targetFile.getLocation().toFile());
				
				if (fragmentToFeatureNameMap.isEmpty()) {
					MessageDialog.openInformation(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						"Trace feature related code",
						"No feature related code found!"
					);
				} else {
					final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					final ITextEditor editor = (ITextEditor) IDE.openEditor(page, targetFile, true);

					final Map<String, String> featureIdToColorMap = resolveFeatureColors(featureRefs.values());
					final CodeFragmentHighlighter cfHighlighter = new CodeFragmentHighlighter(featureIdToColorMap, editor);

					final Map<CodeFragment, Set<String>> fragmentToFeatureIdMap = resolveFeatureIds(featureRefs, fragmentToFeatureNameMap);
					final int firstOccur = cfHighlighter.highlight(targetFile, fragmentToFeatureIdMap);

					if (firstOccur >= 0) {
						editor.selectAndReveal(firstOccur, 0);
					}
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private IFile getTargetFile() throws CoreException
	{
		final String xArchURI = AS.xarch.getXArchURI(xArchRef);
		final String prjName = xArchURI.substring(1, xArchURI.indexOf('/', 1));
		
		final IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(prjName);
		final IJavaProject javaProj = JavaCore.create(proj);
		
		final String className = xMpr.getImpClassName(eltXArchID, 1);
		final IType type = javaProj.findType(className);
		final IFile targetFile = (IFile) type.getCompilationUnit().getCorrespondingResource();
		
		return targetFile;
	}
	
	private Map<CodeFragment, Set<String>> resolveFeatureIds(final Map<String, ObjRef> featureRefs, final Map<CodeFragment, Set<String>> fragments)
	{
		final Map<CodeFragment, Set<String>> fragmentFeatureIdMap = new HashMap<CodeFragment, Set<String>>();
		
		for (final CodeFragment fragment : fragments.keySet()) {
			final Set<String> featureIds = new HashSet<String>();
			
			for (final String featureName : fragments.get(fragment)) {
				featureIds.add(XadlUtils.getID(AS.xarch, featureRefs.get(featureName)));
			}
			
			fragmentFeatureIdMap.put(fragment, featureIds);
		}
		
		return fragmentFeatureIdMap;
	}

	private Map<String, String> resolveFeatureColors(final Collection<ObjRef> featureRefs)
	{
		final Map<String, String> featureColors = new HashMap<String, String>();
		for (final ObjRef featureRef : featureRefs) {
			final String featureId = XadlUtils.getID(AS.xarch, featureRef);
			featureColors.put(featureId, getFeatureColor(featureRef));
		}
		
		return featureColors;
	}
	
	private String getFeatureColor(final ObjRef featureRef)
	{
		return getFeatureColor(featureRef, "featureOptions > featureColor > value > data");
	}

	private String getFeatureColor(final ObjRef root, final String path)
	{
		final char forward = '>';
		
		if (path.indexOf(forward) == -1) {
			return (String) AS.xarch.get(root, path);
		}

		final String head = path.substring(0, path.indexOf(forward)).trim();
		final String tail = path.substring(path.indexOf(forward) + 1).trim();

		return getFeatureColor((ObjRef) AS.xarch.get(root, head), tail);
	}
}