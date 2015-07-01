package edu.umkc.archstudio4.processor.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.SimpleMarkerAnnotation;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class CodeFragmentHighlighter 
{
	static final int CAPACITY = 8;

	static final String EDITOR_PREFERENCE = "org.eclipse.ui.editors";
	static final String MARKER_ID = "edu.umkc.archstudio4.markers.feature_marker";

	static final String ANNOTATION_COLOR_ATTRIBUTE_PREFIX = "aColor_";
	static final String ANNOTATION_ID_PREFIX = "edu.umkc.archstudio4.markers.feature_annotation_";

	static final private Map<String, String> FEATURE_TO_ANNOTATION_MAP = new HashMap<String, String>();
	
	
	final private IDocument doc;
	final private IDocumentProvider docProvider;
	final private IAnnotationModel annotationModel;
	final private Map<String, String> featureColors;

	public CodeFragmentHighlighter(final Map<String, String> featureColors, final ITextEditor editor)
	{
		this.featureColors = featureColors;
		this.docProvider = editor.getDocumentProvider();
		this.doc = docProvider.getDocument(editor.getEditorInput());
		this.annotationModel = docProvider.getAnnotationModel(editor.getEditorInput());
	}

	public int highlight(final IFile targetFile, final Map<CodeFragment, Set<String>> annotateCode)
	{
	    clearAllAnnotations(annotationModel);

		for (final Set<String> featureIds : annotateCode.values()) {
			if (!bindAnnotations(featureIds)) {
				return -1;
			}
		}
	    
	    final List<CodeFragment> orderedFragments = new ArrayList<CodeFragment>(annotateCode.keySet());
	    Collections.sort(orderedFragments);

	    try {
			annotationModel.connect(doc);
		    for (final CodeFragment fragment : orderedFragments) {
				final List<String> featureIds = new ArrayList<String>(annotateCode.get(fragment));
				
				if (featureIds.size() == 1) {
					final int start = doc.getLineOffset(fragment.getStart() - 1);
					final int length = doc.getLineOffset(fragment.getStop() - 1) + doc.getLineLength(fragment.getStop() - 1)  - start;
					
					final String featureId = featureIds.get(0);
					final IMarker marker = targetFile.createMarker(MARKER_ID);
					final SimpleMarkerAnnotation ma = new SimpleMarkerAnnotation(FEATURE_TO_ANNOTATION_MAP.get(featureId), marker);

				    annotationModel.addAnnotation(ma, new Position(start, length));
				} else {
					for (int line = fragment.getStart() - 1; line <= fragment.getStop(); line++) {
						final int start = doc.getLineOffset(line);
						final int length = doc.getLineLength(line);
						final int step = length / featureIds.size();

						for (int i = 0; i < featureIds.size(); i++) {
							final String featureId = featureIds.get(i);

							final IMarker marker = targetFile.createMarker(MARKER_ID);
							final SimpleMarkerAnnotation ma = new SimpleMarkerAnnotation(FEATURE_TO_ANNOTATION_MAP.get(featureId), marker);
							
						    annotationModel.addAnnotation(ma, new Position(start + (i * step), step));
						}
					}					
				}
			}
		    annotationModel.disconnect(doc);
		    
		    return doc.getLineOffset(orderedFragments.get(0).getStart() - 1);
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		return -1;
	}
	
	private void clearAllAnnotations(final IAnnotationModel annotationModel)
	{
		@SuppressWarnings("unchecked")
		final Iterator<Annotation> iterator = annotationModel.getAnnotationIterator();

		while(iterator.hasNext()) {
			final Annotation a = iterator.next();
			
			if (a.getType().startsWith(ANNOTATION_ID_PREFIX)) {
				annotationModel.removeAnnotation(a);
			}
		}
	}
	
	private boolean bindAnnotations(final Set<String> featureIds)
	{
		for (final String featureId : featureIds) {
			if (!FEATURE_TO_ANNOTATION_MAP.containsKey(featureId)) {
				if (FEATURE_TO_ANNOTATION_MAP.size() == CAPACITY) {
					final Shell msgDialogShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					MessageDialog.openError(msgDialogShell, "Highlighter", "Currently support at most " + CAPACITY + " features!");
					
					return false;
				} else {
					final String availableAnnotation = ANNOTATION_ID_PREFIX + FEATURE_TO_ANNOTATION_MAP.size();
					final String featureColor = featureColors.get(featureId);
					
					setAnnotationColor(ANNOTATION_COLOR_ATTRIBUTE_PREFIX + availableAnnotation.substring(ANNOTATION_ID_PREFIX.length()), featureColor);
					FEATURE_TO_ANNOTATION_MAP.put(featureId, availableAnnotation);					
				}
			}
		}
		
		return true;
	}

	static final public boolean changeColor(final String featureId, final String newRGBColor)
	{
		if (FEATURE_TO_ANNOTATION_MAP.containsKey(featureId)) {
			final String annotationId = FEATURE_TO_ANNOTATION_MAP.get(featureId);
			setAnnotationColor(ANNOTATION_COLOR_ATTRIBUTE_PREFIX + annotationId.substring(ANNOTATION_ID_PREFIX.length()), newRGBColor);
			
			return true;
		}
		
		return false;
	}
	
	static private void setAnnotationColor(String prfId, String rgb)
	{
        try {
	        final Preferences preferences = InstanceScope.INSTANCE.getNode(EDITOR_PREFERENCE);
	        preferences.put(prfId, rgb);

	        preferences.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
}
