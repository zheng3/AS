package edu.umkc.archstudio4.processor.core.modifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import edu.umkc.archstudio4.processor.core.util.CoreUtils;
import edu.umkc.archstudio4.processor.export.CodeFragment;
import edu.umkc.archstudio4.processor.grammar.JavaLexer;
import edu.umkc.archstudio4.processor.grammar.JavaParser;

public class Tracer implements Modifier<Map<CodeFragment, Set<String>>>
{
	final private CommonTokenStream tokens;
	final private Set<String> tracingFeatures;
	final private Map<CodeFragment, Set<String>> codeFragments = new HashMap<CodeFragment, Set<String>>();

	public Tracer(final CommonTokenStream tokens, final Set<String> tracingFeatures)
	{
		this.tokens = tokens;
		this.tracingFeatures = tracingFeatures;
	}
	
	@Override
	public Map<CodeFragment, Set<String>> emit()
	{
		return this.codeFragments;
	}
	
	@Override
	public boolean modifyStndAnnotatedCode(ParserRuleContext ctx)
	{
		return traceStndAnnotatedCode(ctx);
	}

	@Override
	public boolean modifySpecAnnotatedCode(ParserRuleContext ctx)
	{
		return traceSpecAnnotatedCode(ctx);
	}
	
	public boolean traceStndAnnotatedCode(ParserRuleContext ctx)
	{
		for (final ParseTree child : ctx.children) {
			final JavaParser.AnnotationContext annotationCtx = CoreUtils.toAnnotationContext(child);

			if (CoreUtils.isFeatureAnnotation(annotationCtx) && CoreUtils.isIntersected(annotationCtx, tracingFeatures)) {
				final CodeFragment codeFragment = new CodeFragment(ctx.getStart().getLine(), ctx.getStop().getLine());
				
				final Set<String> features = CoreUtils.extractFeatures(annotationCtx);
				features.retainAll(tracingFeatures);
				codeFragments.put(codeFragment, features);
				
				return true;
			}
		}
		return false;
	}
	
	public boolean traceSpecAnnotatedCode(ParserRuleContext ctx)
	{
		final List<Token> leftHiddenTokens = tokens.getHiddenTokensToLeft(ctx.getStart().getTokenIndex(), JavaLexer.COMMENT_CHANNEL);
		
		if (leftHiddenTokens != null) {
			for (Token hiddenToken : leftHiddenTokens) {
				final String htText = hiddenToken.getText();
				final JavaParser.AnnotationContext annotationCtx = CoreUtils.toAnnotationContext(htText);;
				
				if (CoreUtils.isFeatureAnnotation(annotationCtx) && CoreUtils.isIntersected(annotationCtx, tracingFeatures)) {
					final CodeFragment codeFragment = new CodeFragment(hiddenToken.getLine(), ctx.getStop().getLine());
					
					final Set<String> features = CoreUtils.extractFeatures(annotationCtx);
					features.retainAll(tracingFeatures);
					codeFragments.put(codeFragment, features);
					
					return true;
				}
			}
		}
		return false;
	}
}
