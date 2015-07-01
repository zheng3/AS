package edu.umkc.archstudio4.processor.core.modifier;

import org.antlr.v4.runtime.ParserRuleContext;

public interface Modifier<T>
{
	public T emit();
	
	public boolean modifyStndAnnotatedCode(final ParserRuleContext ctx);
	
	public boolean modifySpecAnnotatedCode(final ParserRuleContext ctx);
}
