package edu.umkc.archstudio4.processor.core;

import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.CommonTokenStream;

import edu.umkc.archstudio4.processor.core.modifier.Modifier;
import edu.umkc.archstudio4.processor.core.modifier.Tracer;
import edu.umkc.archstudio4.processor.export.CodeFragment;

public class JavaTracer extends JavaProcessor<Map<CodeFragment, Set<String>>>
{
	final private Modifier<Map<CodeFragment, Set<String>>> modifier;
	
	public JavaTracer(final CommonTokenStream tokens, final Set<String> tracingFeatures)
	{
		super(tokens);
		this.modifier = new Tracer(tokens, tracingFeatures);
	}

	@Override
	public Modifier<Map<CodeFragment, Set<String>>> getModifier()
	{
		return this.modifier;
	}

}