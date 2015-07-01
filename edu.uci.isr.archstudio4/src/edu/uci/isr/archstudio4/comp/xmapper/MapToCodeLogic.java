package edu.uci.isr.archstudio4.comp.xmapper;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.progress.IProgressService;

import edu.uci.isr.archstudio4.action.CodeSynchronizingAction;
import edu.uci.isr.archstudio4.action.CodeTracingAction;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoUtils;
import edu.uci.isr.archstudio4.comp.archipelago.types.StructureMapper;
import edu.uci.isr.archstudio4.comp.archipelago.util.Relationship;
import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAComposite;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAMenuListener;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.things.glass.BoxGlassThing;
import edu.uci.isr.bna4.things.utility.EnvironmentPropertiesThing;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;
import edu.umkc.archstudio4.processor.export.ProcessorUtils;

public class MapToCodeLogic extends AbstractThingLogic implements
		IBNAMenuListener {

	protected ArchipelagoServices AS = null;
	protected ObjRef xArchRef = null;
	protected XMapper xMpr = null;
	
	public MapToCodeLogic(ArchipelagoServices services, ObjRef xArchRef){
		this.AS = services;
		this.xArchRef = xArchRef;
		xMpr = new XMapper(services, xArchRef);
	}

	public boolean matches(IBNAView view, IThing t){
		if(t instanceof BoxGlassThing){
			IThing pt = view.getWorld().getBNAModel().getParentThing(t);
			if(pt != null){
				return StructureMapper.isBrickAssemblyRootThing(pt);
			}
		}
		return false;
	}
	
	public String getXArchID(IBNAView view, IThing t){
		if(t instanceof BoxGlassThing){
			IThing parentThing = view.getWorld().getBNAModel().getParentThing(t);
			return parentThing.getProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME);
		}
		return null;
	}
	
	public void fillMenu(IBNAView view, IMenuManager m, int localX, int localY,
			IThing t, int worldX, int worldY) {
		final EnvironmentPropertiesThing ept = BNAUtils.getEnvironmentPropertiesThing(view.getWorld().getBNAModel());
		final String changesXArchID = ept.getProperty("ChangesID");

		if (t == null) {
			fillSynchronizeFeatureRelatedCodeMenuItemCanvas(m);

			IAction menuItem = new Action("Map Changes To Code") {
				public void run() {
					   IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
					   try{
					   progressService.busyCursorWhile(new IRunnableWithProgress(){
					      public void run(IProgressMonitor monitor) {
					    	  boolean isOK = xMpr.mapChangesBySessionID(changesXArchID, monitor);
					    	  if (isOK){
					    		  //Change mapping is done. Back to normal mode.
					    		  ept.removeProperty("ChangesID");
					    	  }
					      }
					   });
					   } catch (InterruptedException e){
						   ;
					   } catch (InvocationTargetException e){
						   ;
					   }
				}
			};
			if(changesXArchID == null){
				//No changes need to be mapped to code.
				menuItem.setEnabled(false);
			}
			m.add(menuItem);
			IAction menuItemTwo = new Action("Map Changes To Code With Dialog") {
				public void run() {
			    	  boolean isOK = xMpr.mapChangesWithDialog(changesXArchID);
			    	  if (isOK){
			    		  //Change mapping is done. Back to normal mode.
			    		  ept.removeProperty("ChangesID");
			    	  }
				}
			};
			if(changesXArchID == null){
				//No changes need to be mapped to code.
				menuItemTwo.setEnabled(false);
			}
			m.add(menuItemTwo);
			m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
			m.add(new Action("Map Architecture To Code"){
				public void run(){
					xMpr.mapAllComponentsByForce();
				}
			});
			m.add(new Action("Map Architecture To Code with Dialog"){
				public void run(){
					xMpr.mapAllComponentsByForceWithDialog();
				}
			});
			m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		} else if (matches(view,t)){
			final String eltXArchID = getXArchID(view, t);
			m.add(new Action("Open Architecture-Prescribed Code") {
				public void run() {
					   try{
						   
						   String className = xMpr.getImpClassName(eltXArchID, 0);
						   if (className != null){
							   String xArchURI = AS.xarch.getXArchURI(xArchRef);
								String prjName = xArchURI.substring(1, xArchURI.indexOf('/', 1));
								//String target = "/"+prjName+"/src/"+className.replace('.', File.separatorChar)+".java";				
								//IFile targetFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(target));
								IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(prjName);
								IJavaProject jProj = JavaCore.create(proj);
								IType type = jProj.findType(className);
								IFile targetFile = (IFile) type.getCompilationUnit().getCorrespondingResource();
								if (targetFile.exists()) {
									IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
									IDE.openEditor(page, targetFile, true);
								}
						   } else {
							   IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
							   MessageDialog.openInformation(
										window.getShell(),
										"ArchStudio 4: xMapper",
										"Architeccture-prescribed code does not exist.");
						   }
					   } catch (Exception e){
						   
						   e.printStackTrace();
					   }
				}
			});
			m.add(new Action("Open User-Defined Code") {
				public void run() {
					   try{
						   String className = xMpr.getImpClassName(eltXArchID, 1);
						   if (className != null){
							   String xArchURI = AS.xarch.getXArchURI(xArchRef);
								String prjName = xArchURI.substring(1, xArchURI.indexOf('/', 1));
								//String target = "/"+prjName+"/src/"+className.replace('.', '/')+".java";				
								//IFile targetFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(target));
								IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(prjName);
								IJavaProject jProj = JavaCore.create(proj);
								IType type = jProj.findType(className);
								IFile targetFile = (IFile) type.getCompilationUnit().getCorrespondingResource();
								if (targetFile.exists()) {
									IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
									IDE.openEditor(page, targetFile, true);
								}
						   } else {
							   IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
							   MessageDialog.openInformation(
										window.getShell(),
										"ArchStudio 4: xMapper",
										"User-defined code does not exist.");
						   }
					   } catch (Exception e){
						   ;
					   }
				}
			});
			m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
			m.add(new Action("Map Component To Code"){
				public void run(){
					xMpr.mapComponentByForce(eltXArchID);
				}
			});
			m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));			

			fillViewAndSynchronizeFeatureRelatedCodeMenuItemComponent(m, eltXArchID);
		}
	}

	private void fillViewAndSynchronizeFeatureRelatedCodeMenuItemComponent(IMenuManager m, final String eltXArchID)
	{
		final Relationship rel = new Relationship(AS.xarch, xArchRef, xMpr);
		if ((XadlUtils.hasFeature(AS.xarch, xArchRef))&&(rel.hasImpFile(AS.xarch.getByID(eltXArchID)))) {
			if (getCurrentFeature() == null) {
				//Right click a component with no feature selected.
				m.add(createViewFeatureRelatedCodeAction(eltXArchID));
				m.add(createSynchronizeActionForAllFeatures(eltXArchID));
			} else {
				//Right click a component with a feature selected.
				m.add(createCodeTraceAction(eltXArchID));
				m.add(createSynchronizeActionForCurrentFeature(eltXArchID));
			}
			m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		}
	}
	
	private IAction createCodeTraceAction(final String eltXArchID)
	{
		return new Action("View Feature Related Code") {
			private final CodeTracingAction tracingAction = new CodeTracingAction(AS, xArchRef, xMpr, eltXArchID) {
				@Override
				public Map<String, ObjRef> getFeatureRefs() {
					final ObjRef featureRef = AS.xarch.getByID(getCurrentFeature());
					
					final Map<String, ObjRef> featureRefs = new HashMap<String, ObjRef>();
					featureRefs.put(ProcessorUtils.nomalizeFeatureName(XadlUtils.getDescription(AS.xarch, featureRef)), featureRef);
					
					return featureRefs;
				}
			};
			
			@Override
			public void run() {
				tracingAction.trace();
			}
		};
	}

	private IAction createSynchronizeActionForCurrentFeature(final String targetComponentId)
	{
		return new CodeSynchronizingAction("Synchronize Feature Related Code") {
			@Override
			protected XMapper getXMapper() { return xMpr; }
			
			@Override
			protected ObjRef getXArchRef() { return xArchRef; }
			
			@Override
			protected XArchFlatInterface getXArchFlatInterface() { return AS.xarch; }

			@Override
			protected List<ObjRef> getTargetFeatures() {
				return Arrays.asList(getXArchFlatInterface().getByID(getCurrentFeature()));
			}
			
			@Override
			protected List<ObjRef> getTargetComponents() {
				return Arrays.asList(getXArchFlatInterface().getByID(targetComponentId));
			}
		};
	}

	
	private IAction createViewFeatureRelatedCodeAction(final String eltXArchID)
	{
		return new Action("View All Feature Related Code") {
			private final CodeTracingAction tracingAction = new CodeTracingAction(AS, xArchRef, xMpr, eltXArchID) {
				@Override
				public Map<String, ObjRef> getFeatureRefs() {
					final ObjRef archFeature = AS.xarch.getElement(
						AS.xarch.createContext(AS.xarch.getOpenXArches()[0], "features"), "archFeature", AS.xarch.getOpenXArches()[0]
					);
					
					final Map<String, ObjRef> featureRefs = new HashMap<String, ObjRef>();
					for (ObjRef feature : AS.xarch.getAll(archFeature, "feature")) {
						featureRefs.put(ProcessorUtils.nomalizeFeatureName(XadlUtils.getDescription(AS.xarch, feature)), feature);
					}

					return featureRefs;
				}
			};

			@Override
			public void run() {
				tracingAction.trace();
			}
		};
	}
	
	private IAction createSynchronizeActionForAllFeatures(final String targetComponentId)
	{
		return new CodeSynchronizingAction("Synchronize All Feature Related Code") {
			@Override
			protected XMapper getXMapper() { return xMpr; }
			
			@Override
			protected ObjRef getXArchRef() { return xArchRef; }
			
			@Override
			protected XArchFlatInterface getXArchFlatInterface() { return AS.xarch; }

			@Override
			protected List<ObjRef> getTargetFeatures() {
				final ObjRef archFeature = getXArchFlatInterface().getElement(
					getXArchFlatInterface().createContext(getXArchRef(), "features"), "archFeature", getXArchRef()
				);

				return Arrays.asList(getXArchFlatInterface().getAll(archFeature, "feature"));
			}
			
			@Override
			protected List<ObjRef> getTargetComponents() {
				return Arrays.asList(getXArchFlatInterface().getByID(targetComponentId));
			}
		};
	}

	private void fillSynchronizeFeatureRelatedCodeMenuItemCanvas(final IMenuManager m)
	{
		final Relationship rel = new Relationship(AS.xarch, xArchRef, xMpr);
		if ((XadlUtils.hasFeature(AS.xarch, xArchRef))&&(rel.hasImpFile(getSynchronizingComponents()))) {
			if (getCurrentFeature() != null) {
				//Right click canvas with a feature selected.
				final IAction synchronizeAction = createSynchronizeActionForCurrentFeature();
				m.add(synchronizeAction);				
			} else  {
				//Right click canvas with no feature selected.
				final IAction synchronizeAction = createSynchronizeActionForAllFeatures();	
				m.add(synchronizeAction);				
			}
			m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		}
	}

	private Action createSynchronizeActionForCurrentFeature()
	{
		return new CodeSynchronizingAction("Synchronize Feature Related Code") {
			@Override
			protected XMapper getXMapper() { return xMpr; }

			@Override
			protected ObjRef getXArchRef() { return xArchRef; }

			@Override
			protected XArchFlatInterface getXArchFlatInterface() { return AS.xarch; }

			@Override
			protected List<ObjRef> getTargetFeatures() {
				return Arrays.asList(getXArchFlatInterface().getByID(getCurrentFeature()));
			}

			@Override
			protected List<ObjRef> getTargetComponents() { 
				return Arrays.asList(getSynchronizingComponents());
			}
		};
	}
	
	private IAction createSynchronizeActionForAllFeatures()
	{
		return new CodeSynchronizingAction("Synchronize All Feature Related Code") {
			@Override
			protected XMapper getXMapper() { return xMpr; }
			
			@Override
			protected ObjRef getXArchRef() { return xArchRef; }

			@Override
			protected XArchFlatInterface getXArchFlatInterface() { return AS.xarch; }

			@Override
			protected List<ObjRef> getTargetFeatures() {
				final ObjRef archFeature = getXArchFlatInterface().getElement(
					getXArchFlatInterface().createContext(getXArchRef(), "features"), "archFeature", getXArchRef()
				);

				return Arrays.asList(getXArchFlatInterface().getAll(archFeature, "feature"));
			}

			@Override
			protected List<ObjRef> getTargetComponents() { 
				return Arrays.asList(getSynchronizingComponents());
			}
		};
	}
	
	private ObjRef[] getSynchronizingComponents()
	{
		final ObjRef archStructure = AS.xarch.getElement(
				AS.xarch.createContext(xArchRef, "types"), "archStructure", xArchRef
		);
		
		return AS.xarch.getAll(archStructure, "component");
	}

	public String getCurrentFeature(){
		BNAComposite composit = ArchipelagoUtils.getBNAComposite(AS.editor);
		if(composit == null){
			return null;
		}
		if(composit.getView() == null){
			return null;
		}
		
		if(composit.getView().getWorld() == null){
			return null;
		}
		if(composit.getView().getWorld().getBNAModel() == null){
			return null;
		}
		
		IBNAModel model = composit.getView().getWorld().getBNAModel();
		return model.getSelectedFeature();
	}
}
