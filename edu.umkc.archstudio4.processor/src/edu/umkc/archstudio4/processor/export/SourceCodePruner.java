package edu.umkc.archstudio4.processor.export;

import edu.uci.isr.archstudio4.comp.booleaneval.SymbolTable;
import edu.umkc.archstudio4.processor.core.JavaProcessor;
import edu.umkc.archstudio4.processor.core.JavaPruner;
import edu.umkc.archstudio4.processor.core.util.CoreUtils;
import edu.umkc.archstudio4.processor.grammar.JavaLexer;
import edu.umkc.archstudio4.processor.grammar.JavaParser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class SourceCodePruner
{
	final private File srcDir;

    /**
     * Constructor
     * 
     * @param srcProjectName name of original project
     */
	public SourceCodePruner(String srcProjectName)
    {
        this.srcDir = ProcessorUtils.getProjectSrcDir(srcProjectName);
    }

	/**
	 * Original project directory getter. Used for testing constructor
	 * 
	 * @return original project directory 
	 */
    public File getSourceDir()
    {
		return srcDir;
	}
	
    /**
     * Prune unselected features in symbol table and put the result in destination project
     * 
     * @param sbTable
     * @param dstProject
     */
    public void pruneFeatures(SymbolTable sbTable, String dstProject)
    {
		final File dstDir = ProcessorUtils.getProjectSrcDir(dstProject);
		ProcessorUtils.relocate(srcDir, dstDir, ProcessorUtils.FileFilter.DEFAULT);

    	final List<String> selectedFeatures = CoreUtils.extractSelectedFeatures(sbTable);
        for (final File srcFile : getSrcFiles(dstDir, new LinkedList<File>())) {
            pruneSrcFile(srcFile, selectedFeatures);
        }
    }
    
    private List<File> getSrcFiles(File srcDir, List<File> srcFiles)
    {
        for (final File file : srcDir.listFiles()) {
            if (!file.isHidden()) {
                if (file.isDirectory()) {
                    getSrcFiles(file, srcFiles);
                } else if (file.getName().endsWith(".java")) {
                    srcFiles.add(file);
                }
            }
        }

        return srcFiles;
    }
    
    private void pruneSrcFile(File srcFile, List<String> selectedFeatures)
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
            final JavaProcessor<String> srcPruner = new JavaPruner(tokens, selectedFeatures);

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
