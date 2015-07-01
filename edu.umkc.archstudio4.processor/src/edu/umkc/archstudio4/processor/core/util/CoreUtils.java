package edu.umkc.archstudio4.processor.core.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import edu.uci.isr.archstudio4.comp.booleaneval.SymbolTable;
import edu.uci.isr.archstudio4.comp.booleaneval.SymbolTable.Entry;
import edu.umkc.archstudio4.processor.export.ProcessorUtils;
import edu.umkc.archstudio4.processor.grammar.JavaLexer;
import edu.umkc.archstudio4.processor.grammar.JavaParser;

public class CoreUtils
{
	public static final String ARCH_ANNOTATION = "Optional";
	public static final String FEATURE_OPT_PREFIX = "Feature.";


	/**
	 * This function helps determine if a given annotation is our generated feature annotation
	 * 
	 * @param
	 * 		annotationContext
	 * 
	 * @return 
	 *		True if an annotation is a feature annotation
	 *		False otherwise
	 */
	static public boolean isFeatureAnnotation(final JavaParser.AnnotationContext annotationContext)
	{
		return annotationContext != null
	            && ARCH_ANNOTATION.equals(annotationContext.annotationName().getText());
	}

	/**
	 * Check if an annotation's feature option set is intersected with the given feature option set.
	 * 
	 * @param ctx
	 * @param featureSet
	 * @return
	 */
    static public boolean isIntersected( final JavaParser.AnnotationContext ctx, final Set<String> featureSet)
    {
    	return extractFeatures(ctx).removeAll(featureSet);
    }
    
	/**
	 * Extract annotation values from annotation context as a set of string values.
	 * 
	 * @param ctx
	 * 		
	 * @return
	 */
    static public Set<String> extractFeatures(final JavaParser.AnnotationContext ctx)
    {
    	final List<String> featureNames = new LinkedList<String>();
        final JavaParser.ElementValueArrayInitializerContext arrayInitCtx = ctx.elementValue().elementValueArrayInitializer();

        if (arrayInitCtx != null) {
            for (final ParseTree child : arrayInitCtx.children) {
                if (child instanceof JavaParser.ElementValueContext) {
                    final String fOptName = child.getText().replace(FEATURE_OPT_PREFIX, "");

                    featureNames.add(fOptName);
                }
            }
        } else {
            final String fOptName = ctx.elementValue().getText().replace(FEATURE_OPT_PREFIX, "");
            
            featureNames.add(fOptName);
        }

        return new HashSet<String>(featureNames);
    }
    
    /**
     * Convert a string to an annotation context
     * 
     * @param comment
     * @return
     */
	static public JavaParser.AnnotationContext toAnnotationContext(String comment)
	{
		final String code = comment.substring("/*".length(), comment.length() - "*/".length());
		
        final ANTLRInputStream codeInputStream = new ANTLRInputStream(code);

        final JavaLexer codeLexer = new JavaLexer(codeInputStream);
        final CommonTokenStream codeStream = new CommonTokenStream(codeLexer);

        final JavaParser codeParser = new JavaParser(codeStream);
        final JavaParser.AnnotationContext annotationCtx = codeParser.annotation();
        
        return codeParser.getNumberOfSyntaxErrors() == 0 ? annotationCtx : null;
	}
	
	/**
	 * Convert a parse tree node to annotation context
	 * 
	 * @param node
	 * @return
	 */
	static public JavaParser.AnnotationContext toAnnotationContext(ParseTree node)
	{
        if (node instanceof JavaParser.ModifierContext) {
            return ((JavaParser.ModifierContext) node).classOrInterfaceModifier().annotation();
        } else if (node instanceof JavaParser.VariableModifierContext) {
			return ((JavaParser.VariableModifierContext) node).annotation();
        }
        
        return null;
	}
	
	/**
	 * 
	 * @param sbTable
	 * @return
	 */
	static public List<String> extractSelectedFeatures(final SymbolTable sbTable)
    {
    	final List<String> selectedFeature = new ArrayList<String>(sbTable.size());
    	
		for (final Object obj : sbTable.getEntryList()) {
			if (obj instanceof Entry) {
				final Entry e = (Entry) obj;
				
				if ("true".equalsIgnoreCase(e.getValue().toString())) {
					selectedFeature.add(ProcessorUtils.nomalizeFeatureName(e.getName()));
				} else if (!"false".equalsIgnoreCase(e.getValue().toString())) {
					selectedFeature.add(ProcessorUtils.nomalizeFeatureName(e.getValue().toString()));
				}
			}
		}
		
    	return selectedFeature;
    }
}
