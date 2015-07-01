package edu.umkc.archstudio4.processor.core;

import java.util.Map;

import org.antlr.v4.runtime.CommonTokenStream;

import edu.umkc.archstudio4.processor.core.modifier.Modifier;
import edu.umkc.archstudio4.processor.core.modifier.Refactor;

public class JavaRefactor extends JavaProcessor<String>
{
	final private Modifier<String> modifier;

	public JavaRefactor(final CommonTokenStream tokens, final Map<String, String> changes)
	{
		super(tokens);
		this.modifier = new Refactor(tokens, this.rewriter, changes);
	}

	@Override
	public Modifier<String> getModifier()
	{
		return this.modifier;
	}
}
