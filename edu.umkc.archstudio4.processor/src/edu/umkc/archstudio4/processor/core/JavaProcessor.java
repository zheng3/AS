package edu.umkc.archstudio4.processor.core;


import edu.umkc.archstudio4.processor.core.modifier.Modifier;
import edu.umkc.archstudio4.processor.grammar.JavaBaseListener;
import edu.umkc.archstudio4.processor.grammar.JavaParser;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;

public abstract class JavaProcessor<T> extends JavaBaseListener
{
    final protected CommonTokenStream tokens;
    final protected TokenStreamRewriter rewriter;

    public JavaProcessor(final CommonTokenStream tokens)
    {
    	this.tokens = tokens;
        this.rewriter = new TokenStreamRewriter(tokens);
    }
    
    public abstract Modifier<T> getModifier();

    public T produce()
    {
    	return getModifier().emit();
    }
    
    @Override
    public void exitImportDeclaration(final JavaParser.ImportDeclarationContext ctx)
    {
    	getModifier().modifySpecAnnotatedCode(ctx);
    }

    @Override
    public void exitStatement(final JavaParser.StatementContext ctx)
    {
    	getModifier().modifySpecAnnotatedCode(ctx);
    }
    
	@Override
	public void exitLocalVariableDeclarationStatement(final JavaParser.LocalVariableDeclarationStatementContext ctx)
	{
    	getModifier().modifyStndAnnotatedCode((JavaParser.LocalVariableDeclarationContext) ctx.getChild(0));
	}
	
	@Override
	public void exitSwitchBlockStatementGroup(final JavaParser.SwitchBlockStatementGroupContext ctx)
	{
    	getModifier().modifySpecAnnotatedCode(ctx);		
	}
	
    @Override
    public void exitFieldDeclaration(final JavaParser.FieldDeclarationContext ctx)
    {
    	getModifier().modifyStndAnnotatedCode(ctx.getParent().getParent());
    }
    
    @Override
    public void exitMethodDeclaration(final JavaParser.MethodDeclarationContext ctx)
    {
    	getModifier().modifyStndAnnotatedCode(ctx.getParent().getParent());
    }
}
