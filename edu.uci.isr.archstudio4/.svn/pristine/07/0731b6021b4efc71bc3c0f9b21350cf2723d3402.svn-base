package edu.uci.isr.archstudio4.comp.archipelago.interactions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.text.edits.TextEdit;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.util.AbstractRemoveContextMenuFiller;
import edu.uci.isr.sysutils.UIDGenerator;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;

public class InteractionsRemoveContextMenuFiller extends
		AbstractRemoveContextMenuFiller {

	public InteractionsRemoveContextMenuFiller(TreeViewer viewer, ArchipelagoServices services, ObjRef xArchRef){
		super(viewer, services, xArchRef);
	}
	
	protected boolean matches(Object node){
		if(node != null){
			if(node instanceof ObjRef){
				ObjRef targetRef = (ObjRef)node;
				if(AS.xarch.isInstanceOf(targetRef, "interactions#Interaction")){
					return true;
				}
			}
		}
		return false;
	}

	//Get the implementation class name of an interface
	private String getImpClassName(ObjRef obj){
		ObjRef interfaceTypeRef = XadlUtils.resolveXLink(AS.xarch, obj, "type");
		if(interfaceTypeRef == null){
			return null;
		}		
		if (!AS.xarch.isInstanceOf(interfaceTypeRef, "implementation#InterfaceTypeImpl")){
				return null;				
		}
		
		ObjRef[] implementationRefs = AS.xarch.getAll(interfaceTypeRef, "implementation");
		if(implementationRefs == null || implementationRefs.length == 0){
			return null;
		}
		ObjRef javaImplementationRef = null;
		for(ObjRef element: implementationRefs){
			if(AS.xarch.isInstanceOf(element, "javaimplementation#JavaImplementation")){
				javaImplementationRef = element;
				break;
			}
		}
		if(javaImplementationRef == null){
			return null;
		}
		ObjRef classRef = (ObjRef)AS.xarch.get(javaImplementationRef, "mainClass");
		if(classRef == null){
			return null;
		}
		ObjRef classNameRef = (ObjRef)AS.xarch.get(classRef, "javaClassName");
		if(classNameRef == null){
			return null;
		}
		String className = (String)AS.xarch.get(classNameRef, "value");
		if(className == null){
			return null;
		}
		return className;		
	}
		
	protected void removeJavadoc(ObjRef targetRef){
		ObjRef intf = XadlUtils.resolveXLink(AS.xarch, targetRef, "targetIntf");
		if (intf == null){
			return;
		}
		String className = getImpClassName(intf);
		String interactionID = XadlUtils.getID(AS.xarch, targetRef);
		if (className != null){
			String xArchURI = AS.xarch.getXArchURI(xArchRef);
			String prjName = xArchURI.substring(1, xArchURI.indexOf('/', 1));
			try{
				IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(prjName);
				IJavaProject jProj = JavaCore.create(proj);
				IType t = jProj.findType(className);
				ICompilationUnit icu = t.getCompilationUnit();
				
				ASTParser parser = ASTParser.newParser(AST.JLS3);
				parser.setKind(ASTParser.K_COMPILATION_UNIT);
				parser.setSource(icu);
				CompilationUnit cu = (CompilationUnit) parser.createAST(null);
				cu.recordModifications();
				
				List cuTypes = cu.types();
				TypeDeclaration type = (TypeDeclaration)cuTypes.get(0);
				MethodDeclaration[] mthdDeclarations = type.getMethods();
				for (MethodDeclaration mthd: mthdDeclarations){
					Javadoc jc = mthd.getJavadoc();
					if (jc != null){
						List<TagElement> tags = (List<TagElement>)jc.tags();
						for (TagElement tag: tags){
							if (tag.getTagName().equals(TagElement.TAG_SEE)){
								List<ASTNode> fragments = (List<ASTNode>) tag.fragments();
								for (ASTNode node: fragments){
									if (node instanceof TextElement){
										String seeWhat = ((TextElement)node).getText().trim();
										if (seeWhat.equals(interactionID)){
											//Obtain an IDocument
											ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
											IPath path = cu.getJavaElement().getPath();
											bufferManager.connect(path, LocationKind.IFILE,null);
											ITextFileBuffer textFileBuffer = bufferManager.getTextFileBuffer(path, LocationKind.IFILE);
											IDocument document = textFileBuffer.getDocument();
											//Remove Javadoc
											mthd.setJavadoc(null);
											//Apply the change
											TextEdit text = cu.rewrite(document, null);
											text.apply(document);
											textFileBuffer.commit(null, false);
											bufferManager.disconnect(path, LocationKind.IFILE, null);
											return;
										}
									}
								}
							}
						}
					}
				}							
			} catch (Exception e){
				;
			}
		}
	}
	
	protected void remove(ObjRef targetRef){
		if(targetRef != null){
			if(AS.xarch.isInstanceOf(targetRef, "interactions#Interaction")){

				//Create the "archChange" reference if not created yet
				ObjRef changesContextRef = AS.xarch.createContext(xArchRef, "changes");
				ObjRef archChangeRef = AS.xarch.getElement(changesContextRef, "archChange", xArchRef);
				if (archChangeRef == null){
					//Create ArchChange
					archChangeRef = AS.xarch.createElement(changesContextRef, "archChange");
					String changeID = UIDGenerator.generateUID("archChange");				
					AS.xarch.set(archChangeRef, "id", changeID);
					XadlUtils.setDescription(AS.xarch, archChangeRef, "Architecture Change Model");
					AS.xarch.add(xArchRef, "object", archChangeRef);				
				}
				//Load "unmapped" change session if it exists.
				ObjRef[] changes = AS.xarch.getAll(archChangeRef, "changes");
				ObjRef changesRef = null;
				for (ObjRef chg: changes){
					String status = (String)AS.xarch.get(chg, "status");
					if (status.equals("unmapped")){
						//record changes
						changesRef = chg;
						break;
					}							
				}
				if (changesRef == null){
					//Start a new change session
					changesRef = AS.xarch.create(changesContextRef, "changes");
					AS.xarch.set(changesRef, "id", UIDGenerator.generateUID("changes"));
					AS.xarch.set(changesRef, "status", "unmapped");
					String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm";
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
					XadlUtils.setDescription(AS.xarch, changesRef, sdf.format(cal.getTime()));
					AS.xarch.add(archChangeRef, "changes", changesRef);			
				}
				//Record the action of new interaction.
				ObjRef interactionChangeRef = AS.xarch.create(changesContextRef, "interactionChange");
				AS.xarch.set(interactionChangeRef, "id", UIDGenerator.generateUID("interactionChange"));
				AS.xarch.set(interactionChangeRef, "type", "remove");
				XadlUtils.setDescription(AS.xarch, interactionChangeRef, "Remove Interaction");
				ObjRef clonedElt = AS.xarch.cloneElement(targetRef, 100);
				XadlUtils.setXLink(AS.xarch, interactionChangeRef, "interaction", clonedElt);
				AS.xarch.set(interactionChangeRef, "copyOfRemovedInteraction", clonedElt);
				AS.xarch.add(changesRef, "interactionChange", interactionChangeRef);
				
				removeJavadoc(targetRef);
				
				AS.xarch.remove(xArchRef, "Object", targetRef);
				return;
			}
		}
		super.remove(targetRef);
	}

}
