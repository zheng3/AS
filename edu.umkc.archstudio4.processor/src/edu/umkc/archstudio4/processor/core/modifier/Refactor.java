package edu.umkc.archstudio4.processor.core.modifier;

import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import edu.umkc.archstudio4.processor.core.util.CoreUtils;
import edu.umkc.archstudio4.processor.grammar.JavaLexer;
import edu.umkc.archstudio4.processor.grammar.JavaParser;

public class Refactor implements Modifier<String>
{
    final private CommonTokenStream tokens;
    final private Map<String, String> changes;
    final private TokenStreamRewriter rewriter;

	public Refactor(final CommonTokenStream tokens, final TokenStreamRewriter rewriter, final Map<String, String> changes)
	{
		this.tokens = tokens;
        this.changes = changes;
		this.rewriter = rewriter;
	}

	@Override
	public String emit()
	{
		return this.rewriter.getText();
	}
	
	@Override
	public boolean modifyStndAnnotatedCode(ParserRuleContext ctx)
	{
		return refactorStndAnnotatedCode(ctx);
	}

	@Override
	public boolean modifySpecAnnotatedCode(ParserRuleContext ctx)
	{
		return refactorSpecAnnotatedCode(ctx);
	}

    private boolean refactorStndAnnotatedCode(final ParserRuleContext ctx)
    {
        for (ParseTree child : ctx.children) {
            final JavaParser.AnnotationContext annotationCtx = CoreUtils.toAnnotationContext(child);

            if (CoreUtils.isFeatureAnnotation(annotationCtx) && CoreUtils.isIntersected(annotationCtx, changes.keySet())) {
            	renameAnnotation(rewriter, annotationCtx);
                return true;
            }
        }
        return false;
    }
    
    private boolean refactorSpecAnnotatedCode(final ParserRuleContext prCtx)
    {
        final List<Token> leftHiddenTokens = tokens.getHiddenTokensToLeft(prCtx.getStart().getTokenIndex(), JavaLexer.COMMENT_CHANNEL);

        if (leftHiddenTokens != null) {
            for (Token hiddenToken : leftHiddenTokens) {
                final String htText = hiddenToken.getText();
                final ANTLRInputStream htInputStream = new ANTLRInputStream(htText.substring("/*".length(), htText.length() - "*/".length()));

                final JavaLexer htLexer = new JavaLexer(htInputStream);
                final CommonTokenStream htStream = new CommonTokenStream(htLexer);

                final JavaParser htParser = new JavaParser(htStream);
                final JavaParser.AnnotationContext annotationCtx = htParser.annotation();
                
                if ((htParser.getNumberOfSyntaxErrors() == 0) && CoreUtils.isFeatureAnnotation(annotationCtx) && CoreUtils.isIntersected(annotationCtx, changes.keySet())) {
                	final String renamedAnnotation = renameAnnotation(new TokenStreamRewriter(htStream), annotationCtx);
                    rewriter.replace(hiddenToken, "/*" + renamedAnnotation + "*/");
                    return true;
                }
            }
        }
        return false;
    }
    
    private String renameAnnotation(TokenStreamRewriter annotationRewriter, JavaParser.AnnotationContext annotationCtx)
    {
        final JavaParser.ElementValueArrayInitializerContext arrayInitializerCtx = annotationCtx.elementValue().elementValueArrayInitializer();
        
        if (arrayInitializerCtx != null) {
        	for (ParseTree child : arrayInitializerCtx.children) {
                if (child instanceof JavaParser.ElementValueContext) {
                    final String fOptName = child.getText().replace(CoreUtils.FEATURE_OPT_PREFIX, "");

                    if (changes.containsKey(fOptName)) {
                        annotationRewriter.replace(
                    		((JavaParser.ElementValueContext) child).getStart(),
                    		((JavaParser.ElementValueContext) child).getStop(),
                    		CoreUtils.FEATURE_OPT_PREFIX + changes.get(fOptName)
                		);
                    }
                }
            }
        } else {
            final String fOptName = annotationCtx.elementValue().getText().replace(CoreUtils.FEATURE_OPT_PREFIX, "");

            annotationRewriter.replace(
        		annotationCtx.elementValue().getStart(),
        		annotationCtx.elementValue().getStop(),
        		CoreUtils.FEATURE_OPT_PREFIX + changes.get(fOptName)
    		);
        }
        
    	return annotationRewriter.getText();
    }
}
