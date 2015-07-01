package edu.umkc.archstudio4.processor.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.core.resources.IFile;

import edu.umkc.archstudio4.processor.core.JavaProcessor;
import edu.umkc.archstudio4.processor.core.JavaRefactor;
import edu.umkc.archstudio4.processor.grammar.JavaLexer;
import edu.umkc.archstudio4.processor.grammar.JavaParser;

public class SourceCodeRefactor
{
    public void refactorFeatures(Map<String, String> changes, IFile srcFile)
    {
    	if (srcFile.exists()) {
        	refactorSrcFile(srcFile.getLocation().toFile(), changes);    		
    	}
    }

    private void refactorSrcFile(File srcFile, Map<String, String> changes)
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
            final JavaProcessor<String> srcRefactor = new JavaRefactor(tokens, changes);

            walker.walk(srcRefactor, tree);

            FileWriter writer = null;
            try {
            	writer = new FileWriter(srcFile);
                writer.write(srcRefactor.produce());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            	if (writer != null) {
            		writer.close();
            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
    		try {
            	if (fileStream != null) {
            		fileStream.close();
            	}
			} catch (IOException e) {}
        }
    }
}