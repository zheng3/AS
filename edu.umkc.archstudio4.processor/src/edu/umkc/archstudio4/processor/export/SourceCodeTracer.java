package edu.umkc.archstudio4.processor.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import edu.umkc.archstudio4.processor.core.JavaProcessor;
import edu.umkc.archstudio4.processor.core.JavaTracer;
import edu.umkc.archstudio4.processor.grammar.JavaLexer;
import edu.umkc.archstudio4.processor.grammar.JavaParser;

public class SourceCodeTracer
{
	public Map<CodeFragment, Set<String>> trace(Set<String> tracingFeatureNames, File srcFile)
	{
		InputStream fileStream = null;
		
		try {
			fileStream = new FileInputStream(srcFile);
			
			final ANTLRInputStream antIS = new ANTLRInputStream(fileStream);
			final JavaLexer lexer = new JavaLexer(antIS);
			
			final CommonTokenStream tokens = new CommonTokenStream(lexer);
			final JavaParser parser = new JavaParser(tokens);
			
			final ParseTree tree = parser.compilationUnit();
			final ParseTreeWalker walker = new ParseTreeWalker();
			
			final JavaProcessor<Map<CodeFragment, Set<String>>> tracer = new JavaTracer(tokens, tracingFeatureNames);
			
			walker.walk(tracer, tree);
			return tracer.produce();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileStream != null) {
					fileStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return Collections.emptyMap();
	}
}