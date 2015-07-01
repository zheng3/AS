package edu.umkc.archstudio4.processor.core;


import edu.umkc.archstudio4.processor.core.modifier.Modifier;
import edu.umkc.archstudio4.processor.core.modifier.Pruner;
import edu.umkc.archstudio4.processor.grammar.JavaParser;

import org.antlr.v4.runtime.CommonTokenStream;

import java.util.List;

public class JavaPruner extends JavaProcessor<String>
{
	final private Modifier<String> modifier;

	public JavaPruner(final CommonTokenStream tokens, final List<String> selectedFeatures)
	{
		super(tokens);
		this.modifier = new Pruner(tokens, this.rewriter, selectedFeatures);
	}

	@Override
	public Modifier<String> getModifier()
	{
		return this.modifier;
	}
	
	@Override
	public void exitLocalVariableDeclarationStatement(final JavaParser.LocalVariableDeclarationStatementContext ctx)
	{
		final JavaParser.LocalVariableDeclarationContext localVariableDeclarationContext = (JavaParser.LocalVariableDeclarationContext) ctx.getChild(0);
    	if (getModifier().modifyStndAnnotatedCode(localVariableDeclarationContext)) {
    		rewriter.delete(ctx.getStop());
    	}
	}
}