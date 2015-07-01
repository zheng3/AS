package edu.umkc.archstudio4.processor.core.modifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import edu.umkc.archstudio4.processor.core.Consumer;
import edu.umkc.archstudio4.processor.core.util.CoreUtils;
import edu.umkc.archstudio4.processor.grammar.JavaLexer;
import edu.umkc.archstudio4.processor.grammar.JavaParser;

public class Pruner implements Modifier<String>
{
    final private CommonTokenStream tokens;
	final private TokenStreamRewriter rewriter;
    final private Set<String> selectedFeatureOptSet;

    public Pruner(final CommonTokenStream tokens, final TokenStreamRewriter rewriter, final List<String> selectedFeatures)
    {
        this.tokens = tokens;
        this.rewriter = rewriter;
        this.selectedFeatureOptSet = new HashSet<String>(selectedFeatures);
    }
    
	@Override
	public String emit()
	{
		return this.rewriter.getText();
	}

	@Override
	public boolean modifyStndAnnotatedCode(final ParserRuleContext ctx)
	{
		return pruneStndAnnotatedCode(ctx);
	}
	
    private boolean pruneStndAnnotatedCode(final ParserRuleContext ctx)
    {
        for (final ParseTree child : ctx.children) {
            final JavaParser.AnnotationContext annotationCtx = CoreUtils.toAnnotationContext(child);
            
            if (CoreUtils.isFeatureAnnotation(annotationCtx)) {
            	if (!CoreUtils.isIntersected(annotationCtx, selectedFeatureOptSet)) {
	                final List<Token> leftHiddenTokens = ((CommonTokenStream) tokens).getHiddenTokensToLeft(
	            		annotationCtx.getStart().getTokenIndex(),
	                    JavaLexer.COMMENT_CHANNEL
	        		);
	                
	                if (leftHiddenTokens != null) {
	                	for (Token ht : leftHiddenTokens) {
	                		rewriter.delete(ht);
	                	}
	                }
	
	                rewriter.delete(ctx.getStart(), ctx.getStop());
	            } else {
	                rewriter.delete(annotationCtx.getStart(), annotationCtx.getStop());
	            }
                return true;
            }
        }
        
        return false;
    }
    
	@Override
	public boolean modifySpecAnnotatedCode(final ParserRuleContext ctx)
	{
		return pruneSpecAnnotatedCode(ctx, new Consumer<Token>() {
            @Override
            public void accept(Token token) {
                rewriter.delete(token, ctx.getStop());
            }
        });
	}

    private boolean pruneSpecAnnotatedCode(final ParserRuleContext prCtx, final Consumer<Token> action)
    {
        final List<Token> leftHiddenTokens = ((CommonTokenStream) tokens).getHiddenTokensToLeft(
            prCtx.getStart().getTokenIndex(),
            JavaLexer.COMMENT_CHANNEL
        );

        if (leftHiddenTokens != null) {
            for (final Token hiddenToken : leftHiddenTokens) {
                final String htText = hiddenToken.getText();
                final JavaParser.AnnotationContext annotationCtx = CoreUtils.toAnnotationContext(htText);

                if (CoreUtils.isFeatureAnnotation(annotationCtx)) { 
                    if (!CoreUtils.isIntersected(annotationCtx, selectedFeatureOptSet)) {
                        action.accept(hiddenToken);
                	} else {
                        rewriter.delete(hiddenToken);
                	}
                    return true;
                }
            }
        }
        
        return false;
    }
}
