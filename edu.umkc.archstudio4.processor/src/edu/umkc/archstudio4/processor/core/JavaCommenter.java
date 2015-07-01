package edu.umkc.archstudio4.processor.core;

import java.util.HashSet;
import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;

import edu.umkc.archstudio4.processor.core.modifier.Commenter;
import edu.umkc.archstudio4.processor.core.modifier.Modifier;

public class JavaCommenter extends JavaProcessor<String>
{
	final private Modifier<String> modifier;
	
	public JavaCommenter(final CommonTokenStream tokens, final List<String> selectedFeatures, final boolean inclusive)
	{
		super(tokens);
		this.modifier =  new Commenter(this.tokens, this.rewriter, new HashSet<String>(selectedFeatures), inclusive);
	}

	@Override
	public Modifier<String> getModifier()
	{
		return this.modifier;
	}
}