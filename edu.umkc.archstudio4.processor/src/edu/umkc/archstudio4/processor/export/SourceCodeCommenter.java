package edu.umkc.archstudio4.processor.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.core.resources.IFile;

import edu.umkc.archstudio4.processor.core.JavaCommenter;
import edu.umkc.archstudio4.processor.core.JavaProcessor;
import edu.umkc.archstudio4.processor.grammar.JavaLexer;
import edu.umkc.archstudio4.processor.grammar.JavaParser;

public class SourceCodeCommenter
{
    public void removeUnrelatedFeatures(final List<String> unrelatedFeatures, final IFile srcFile)
    {
    	if (srcFile.exists()) {
            commentSrcFile(srcFile.getLocation().toFile(), unrelatedFeatures, true);    		
    	}
    }
    
    public void retainRelatedFeatures(final List<String> relatedFeatures, final IFile srcFile)
    {
    	if (srcFile.exists()) {
            commentSrcFile(srcFile.getLocation().toFile(), relatedFeatures, false);    		
    	}    	
    }
    
    private void commentSrcFile(final File srcFile, final List<String> featureNames, final boolean inclusive)
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
            final JavaProcessor<String> srcPruner = new JavaCommenter(tokens, featureNames, inclusive);

            walker.walk(srcPruner, tree);

            FileWriter writer = null;
            try {
            	writer = new FileWriter(srcFile);
                writer.write(srcPruner.produce());
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
