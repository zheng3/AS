package edu.umkc.archstudio4.processor.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.core.resources.IFile;

import edu.umkc.archstudio4.processor.grammar.JavaBaseListener;
import edu.umkc.archstudio4.processor.grammar.JavaLexer;
import edu.umkc.archstudio4.processor.grammar.JavaParser;
import edu.umkc.archstudio4.processor.grammar.JavaParser.EnumConstantContext;

public class AnnotationEditor
{
	final static public String FEATURE_OPT_CLASS = "com.pla.chatsys.Feature";
	
	private class JavaAnnotationEditor extends JavaBaseListener
	{
	    final private Map<String, String> changes;
	    final private TokenStreamRewriter rewriter;
	    
		public JavaAnnotationEditor(final TokenStreamRewriter rewriter, final Map<String, String> changes)
		{
			this.changes = changes;
	        this.rewriter = rewriter;
		}

		@Override
		public void exitEnumConstants(JavaParser.EnumConstantsContext ctx)
		{
			final List<EnumConstantContext> eCtxList = ctx.enumConstant();
			
			if (eCtxList.size() == 1) {
				final EnumConstantContext eCtx = eCtxList.get(0);

				if (changes.containsKey(eCtx.getText())) {
					final String changedName = changes.get(eCtx.getText());
					
					if (changedName.isEmpty()) {
						rewriter.delete(eCtx.getStart(), eCtx.getStop());
					} else {
						rewriter.replace(eCtx.getStart(), eCtx.getStop(), changedName);
					}
				}
			} else {
				for (int i = 0; i < eCtxList.size(); i++) {
					final EnumConstantContext eCtx = eCtxList.get(i);
					
					if (changes.containsKey(eCtx.getText())) {
						final String changedName = changes.get(eCtx.getText());
						
						if (changedName.isEmpty()) {
							if ((i + 1) == eCtxList.size()) {
								rewriter.delete(eCtxList.get(i - 1).getStop().getTokenIndex() + 1, eCtx.getStop().getTokenIndex());
							} else {
								rewriter.delete(eCtx.getStart().getTokenIndex(), eCtxList.get(i + 1).getStart().getTokenIndex() - 1);
							}						
						} else {
							rewriter.replace(eCtx.getStart(), eCtx.getStop(), changedName);
						}
					}
				}				
			}
		}
	}
	
    public void editAnnotation(Map<String, String> changes, Set<IFile> annotationFiles)
    {
        for (IFile annotationFile : annotationFiles) {
        	if (annotationFile.exists()) {
            	editAnnotationFile(changes, annotationFile.getLocation().toFile());        		
        	}
        }
    }

    private void editAnnotationFile(Map<String, String> changes, File annotationFile)
    {
    	InputStream fileStream = null;
        try {
        	fileStream = new FileInputStream(annotationFile);
            final ANTLRInputStream antIS = new ANTLRInputStream(fileStream);

            final JavaLexer lexer = new JavaLexer(antIS);
            final CommonTokenStream tokens = new CommonTokenStream(lexer);
            final TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);

            final JavaParser parser = new JavaParser(tokens);
            final ParseTree tree = parser.compilationUnit();

            final ParseTreeWalker walker = new ParseTreeWalker();
            final JavaAnnotationEditor editor = new JavaAnnotationEditor(rewriter, changes);

            walker.walk(editor, tree);

            FileWriter writer = null;
            try {
            	writer = new FileWriter(annotationFile);
                writer.write(rewriter.getText());
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
