package edu.umkc.archstudio4.processor.core.modifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import edu.umkc.archstudio4.processor.core.Consumer;
import edu.umkc.archstudio4.processor.core.util.CoreUtils;
import edu.umkc.archstudio4.processor.export.CodeFragment;
import edu.umkc.archstudio4.processor.export.ProcessorUtils;
import edu.umkc.archstudio4.processor.grammar.JavaLexer;
import edu.umkc.archstudio4.processor.grammar.JavaParser;
import edu.umkc.archstudio4.processor.grammar.JavaParser.AnnotationContext;

public class Commenter implements Modifier<String>
{
    final private CommonTokenStream tokens;
    final private Set<String> featureOptSet;
	final private TokenStreamRewriter rewriter;
    final private Predicate<AnnotationContext> predicate;

    final private List<Integer> pushingLines = new LinkedList<Integer>();
    final private List<CodeFragment> commentOutFragments = new LinkedList<CodeFragment>();

    final private String LINE_COMMENT_PREFIX = "//";
    final private String TODO_MSG = " TODO: Please confirm this can be safely removed: ";

    private interface Predicate<T>
    {
    	public boolean test(T t);
    }

	public Commenter(final CommonTokenStream tokens, final TokenStreamRewriter rewriter, final Set<String> featureNames, final boolean inclusive)
	{
		this.tokens = tokens;
		this.rewriter = rewriter;
		this.featureOptSet = featureNames;
		
		if (inclusive) {
			this.predicate = new Predicate<AnnotationContext>() {
				@Override
				public boolean test(final AnnotationContext annotationCtx) {
					return CoreUtils.isIntersected(annotationCtx, featureOptSet);
				}
			};
		} else {
			this.predicate = new Predicate<AnnotationContext>() {
				@Override
				public boolean test(final AnnotationContext annotationCtx) {
					return !CoreUtils.isIntersected(annotationCtx, featureOptSet);
				}
			};
		}
	}

	@Override
	public String emit()
	{
		if (commentOutFragments.isEmpty()) {
			return this.rewriter.getText();
		}

		Collections.sort(pushingLines);
		final String[] lines = this.rewriter.getText().split("\\n");
		
		for (final CodeFragment fragment : commentOutFragments) {
			final int init = fragment.getStart() + getLineOffset(fragment.getStart());
			final int term = fragment.getStop() + getLineOffset(fragment.getStop());
			lines[init - 1] = LINE_COMMENT_PREFIX + TODO_MSG + "\n" 
							+ LINE_COMMENT_PREFIX + lines[init - 1];

			for (int i = init + 1; i <= term; i++) {
				lines[i - 1] = LINE_COMMENT_PREFIX + lines[i - 1];
			}
		}

		return ProcessorUtils.joinStr("\n", lines);
	}
	
	private int getLineOffset(final int lineNumber)
	{
		int offset = 0;
		for (final Integer lNum : pushingLines) {
			if (lNum < lineNumber) {
				offset += 1;
			}
		}
		return offset;
	}
	
	@Override
	public boolean modifyStndAnnotatedCode(final ParserRuleContext ctx)
	{
		return commentStndAnnotatedCode(ctx);
    }
	
	private boolean commentStndAnnotatedCode(final ParserRuleContext ctx)
	{
        for (final ParseTree child : ctx.children) {
            final AnnotationContext annotationCtx = CoreUtils.toAnnotationContext(child);

            if (rewriteStndAnnotation(annotationCtx)) {
            	final CodeFragment fragment = new CodeFragment(ctx.getStart().getLine(), ctx.getStop().getLine());
            	commentOutFragments.add(fragment);
                return true;
            }
        }
        return false;
	}
	
    private boolean rewriteStndAnnotation(final AnnotationContext ctx)
    {
        if (CoreUtils.isFeatureAnnotation(ctx) && predicate.test(ctx)) {
        	final Set<String> remainFeatures = CoreUtils.extractFeatures(ctx);

        	if (remainFeatures.size() == 1) {
        		return true;
        	} else if (remainFeatures.removeAll(featureOptSet)) {
        		if (remainFeatures.isEmpty()) {
                	return true;
        		} else {
                	rewriter.insertBefore(ctx.getStart(), LINE_COMMENT_PREFIX + TODO_MSG + ctx.getText() + "\n");
                	pushingLines.add(ctx.getStart().getLine()); // above insertBefore operation will result 1 additional line offset

                	final JavaParser.ElementValueArrayInitializerContext arrayInitializerCtx = ctx.elementValue().elementValueArrayInitializer();

                	final String[] featureOpts = prepend(CoreUtils.FEATURE_OPT_PREFIX, remainFeatures);

                	if (remainFeatures.size() == 1) {
                		rewriter.replace(arrayInitializerCtx.getStart(), arrayInitializerCtx.getStop(), featureOpts[0]);
                	} else {
                    	final String featureOptsCSV = ProcessorUtils.joinStr(",", featureOpts);
                    	rewriter.replace(arrayInitializerCtx.getStart(), arrayInitializerCtx.getStop(), "{" + featureOptsCSV + "}");                		
                	}                	
        		}
        	}
        }

        return false;
    }

	@Override
	public boolean modifySpecAnnotatedCode(final ParserRuleContext ctx)
	{
		return commentSpecAnnotatedCode(ctx, new Consumer<Token>() {
            @Override
            public void accept(final Token token) {
            	final CodeFragment fragment = new CodeFragment(token.getLine(), ctx.getStop().getLine());
            	commentOutFragments.add(fragment);
            }
        });
	}

	private boolean commentSpecAnnotatedCode(final ParserRuleContext ctx, final Consumer<Token> consumer)
	{
        final List<Token> leftHiddenTokens = tokens.getHiddenTokensToLeft(ctx.getStart().getTokenIndex(),JavaLexer.COMMENT_CHANNEL);

        if (leftHiddenTokens != null) {
            for (final Token hiddenToken : leftHiddenTokens) {
                if (rewriteSpecAnnotation(hiddenToken)) {
                	consumer.accept(hiddenToken);
                	return true;
                }
            }
        }
        return false;
	}

    private boolean rewriteSpecAnnotation(final Token token)
    {
        final String tokenText = token.getText().trim();
		final String annotation = tokenText.substring("/*".length(), tokenText.length() - "*/".length());

        final ANTLRInputStream codeInputStream = new ANTLRInputStream(annotation);

        final JavaLexer codeLexer = new JavaLexer(codeInputStream);
        final CommonTokenStream codeStream = new CommonTokenStream(codeLexer);
        final TokenStreamRewriter codeRewriter = new TokenStreamRewriter(codeStream);

        final JavaParser codeParser = new JavaParser(codeStream);
        final JavaParser.AnnotationContext annotationCtx = codeParser.annotation();
        
        if (codeParser.getNumberOfSyntaxErrors() == 0) {
        	if (CoreUtils.isFeatureAnnotation(annotationCtx) && predicate.test(annotationCtx)) {
	        	final Set<String> remainFeatures = CoreUtils.extractFeatures(annotationCtx);
	        	
	        	if (remainFeatures.size() == 1) {
	        		return true;
	        	} else if (remainFeatures.removeAll(featureOptSet)) {
	        		if (remainFeatures.isEmpty()) {
	                	return true;
	        		} else {
	        			rewriter.insertBefore(token, LINE_COMMENT_PREFIX + TODO_MSG + tokenText + "\n");
	                	pushingLines.add(token.getLine()); // above insertBefore operation will result 1 additional line offset

	                	final JavaParser.ElementValueArrayInitializerContext arrayInitializerCtx = annotationCtx.elementValue().elementValueArrayInitializer();
	                	
	                	final String[] featureOpts = prepend(CoreUtils.FEATURE_OPT_PREFIX, remainFeatures);
	                	
	                	if (remainFeatures.size() == 1) {
	                		codeRewriter.replace(arrayInitializerCtx.getStart(), arrayInitializerCtx.getStop(), featureOpts[0]);
	                	} else {
	                    	final String featureOptsCSV = ProcessorUtils.joinStr(",", featureOpts);
	                    	codeRewriter.replace(arrayInitializerCtx.getStart(), arrayInitializerCtx.getStop(), "{" + featureOptsCSV + "}");                		
	                	}
	                	
	                	rewriter.replace(token, "/*" + codeRewriter.getText() + "*/");
	        		}
	        	}
        	}
        }
        
        return false;
    }
    
    private String[] prepend(final String prefix, final Set<String> strs)
    {
    	final List<String> prefixStrs = new ArrayList<String>(strs.size());
    	
    	for (final String str : strs) {
    		prefixStrs.add(prefix + str);
    	}
    	
    	return prefixStrs.toArray(new String[prefixStrs.size()]);
    }
}
