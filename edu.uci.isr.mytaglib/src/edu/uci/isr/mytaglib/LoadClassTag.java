package edu.uci.isr.mytaglib;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.taglib.AbstractEmptyTag;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagInfo;
import org.eclipse.jet.internal.xpath.inspectors.jdt.ASTNodeDocumentRoot;

public class LoadClassTag extends AbstractEmptyTag {

	public LoadClassTag() {
		// TODO Auto-generated constructor stub
	}

	public void doAction(TagInfo td, JET2Context context, JET2Writer out)
			throws JET2TagException {
		
		IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(getAttribute( "project"));
		IJavaProject jProj = JavaCore.create(proj);
		try {
			IType type = jProj.findType(getAttribute("fqn"));
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			if(type.isBinary()) {
				parser.setSource( type.getClassFile() );
			} else {
				parser.setSource( type.getCompilationUnit() );
			}
			//CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			//context.setVariable(getAttribute("var"), cu);
			final ASTNode astNode = parser.createAST(null);
			try {
				final String contents = type.getCompilationUnit().getBuffer().getContents();
				context.setVariable(getAttribute("var"), new ASTNodeDocumentRoot(astNode, contents));
			} catch (JavaModelException e) {
				// ignore, ... just fall through;
			}
			
		} catch (JavaModelException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		
	}
}
