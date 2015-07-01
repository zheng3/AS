package edu.uci.isr.archstudio4.comp.xmapper;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.xgenerator.XCodeGenerator;
import edu.uci.isr.archstudio4.comp.xmessenger.XMessenger;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;

public class XMapper {
	protected ArchipelagoServices AS = null;
	protected ObjRef xArchRef = null;
	protected XCodeGenerator codeGen = null;
	protected XMessenger msger = null;
	
	public XMapper(ArchipelagoServices services, ObjRef xArchRef){
		this.AS = services;
		this.xArchRef = xArchRef;
		codeGen = new XCodeGenerator();
		msger = new XMessenger();
	}
	
	public boolean mapChangesWithDialog (String sessionID){

		ObjRef changesObj = AS.xarch.getByID(sessionID);
		if (changesObj==null){
			return false;
		}
		
		//Load component changes. TODO: link changes, behavior changes.
		Vector compAdded = new Vector<String>();
		Vector compRemoved = new Vector<String>();
		Map compUpdated = new HashMap<String, Vector>();
		loadCompChanges(changesObj,compAdded, compRemoved, compUpdated);

		//deal with interaction changes
		Vector interactionAdded = new Vector<String>();
		Vector interactionRemoved = new Vector<String>();
		Vector interactionUpdated = new Vector<String>();
		loadInteractionChanges(changesObj, interactionAdded, interactionRemoved, interactionUpdated);
		TreatInteractionChangesAsUpdateComponent(interactionAdded, interactionRemoved, interactionUpdated,
					compAdded, compRemoved, compUpdated);		

		//deal with state chart changes
		Vector statechartAdded = new Vector<String>();
		Vector statechartRemoved = new Vector<String>();
		Vector statechartUpdated = new Vector<String>();
		loadStatechartChanges(changesObj, statechartAdded, statechartRemoved, statechartUpdated);
		TreatStatechartChangesAsUpdateComponent(statechartAdded, statechartRemoved, statechartUpdated,
				compAdded, compRemoved, compUpdated);
		
		
		//Translate changes to generation and notification tasks, and initialize the configuration dialog with the information
		//from the architecture description file.
		final String[] toGenerate = new String[compAdded.size()+compUpdated.keySet().size()];
		final String[] toNotify = new String[compUpdated.keySet().size()+compRemoved.size()];
		int i = 0;
		final xMapperDialog dialog = new xMapperDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		for (Enumeration e = compAdded.elements() ; e.hasMoreElements() ;) {
			String compId = (String)(e.nextElement());
			String compDesp = XadlUtils.getDescription(AS.xarch, AS.xarch.getByID(compId));
			toGenerate[i++] = compId;
			dialog.setCompDescription(compId, compDesp);
			dialog.setChangeType(compId, "Added");
			try{
				String archPrescribedClassName = getImpClassName(compId, 0);
				String userDefineClassName = getImpClassName(compId, 1);
				if (archPrescribedClassName != null && archPrescribedClassName.length()>0){
					dialog.setArchPrescribedClassName(compId, archPrescribedClassName);
				}
				if (userDefineClassName != null && userDefineClassName.length()>0){
					dialog.setUserDefinedClassName(compId, userDefineClassName);
				}
			} catch (CoreException ce){
				return false;
			}
		}
		int j = 0;
		for (Iterator e = compUpdated.keySet().iterator(); e.hasNext();) {
			String compId = (String)(e.next());
			String compDesp = XadlUtils.getDescription(AS.xarch, AS.xarch.getByID(compId));
			toGenerate[i++] = compId;
			toNotify[j++] = compId;
			dialog.setCompDescription(compId, compDesp);
			dialog.setMessages(compId, (Vector)compUpdated.get(compId));
			dialog.setChangeType(compId, "Updated");
			try{
				String archPrescribedClassName = getImpClassName(compId, 0);
				String userDefineClassName = getImpClassName(compId, 1);
				if (archPrescribedClassName != null && archPrescribedClassName.length()>0){
					dialog.setArchPrescribedClassName(compId, archPrescribedClassName);
				}
				if (userDefineClassName != null && userDefineClassName.length()>0){
					dialog.setUserDefinedClassName(compId, userDefineClassName);
				}
			} catch (CoreException ce){
				return false;
			}
		}
		for (Enumeration e = compRemoved.elements() ; e.hasMoreElements() ;) {
			String compId = (String)(e.nextElement());
			String compDesp = XadlUtils.getDescription(AS.xarch, AS.xarch.getByID(compId));
			toNotify[j++] = compId;
			dialog.setCompDescription(compId, compDesp);
			Vector v = new Vector<String>();
			v.add("Component "+compDesp+" was removed.");
			dialog.setMessages(compId, v);			
			dialog.setChangeType(compId, "Removed");
			try{
				String archPrescribedClassName = getImpClassName(compId, 0);
				String userDefineClassName = getImpClassName(compId, 1);
				if (archPrescribedClassName != null && archPrescribedClassName.length()>0){
					dialog.setArchPrescribedClassName(compId, archPrescribedClassName);
				}
				if (userDefineClassName != null && userDefineClassName.length()>0){
					dialog.setUserDefinedClassName(compId, userDefineClassName);
				}
			} catch (CoreException ce){
				return false;
			}			
		}
		
		// Open the dialog, and collect user input.
		int addedCount = compAdded.size();
		int updatedCount = compUpdated.keySet().size();
		int removedCount = compRemoved.size();
		String[] compChanged = new String[addedCount+updatedCount+removedCount];
		System.arraycopy(compAdded.toArray(), 0, compChanged, 0, addedCount);
		System.arraycopy(compUpdated.keySet().toArray(), 0, compChanged, addedCount, updatedCount);
		System.arraycopy(compRemoved.toArray(), 0, compChanged, addedCount+updatedCount, removedCount);
		if (compChanged.length>0){
			if (dialog.open(compChanged) == 0){
				return false;
			}			
		}

		//Generate code for changed components
		//IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		try{
			ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
			pmd.run(true, true, new IRunnableWithProgress(){
		      public void run(IProgressMonitor monitor) {
		    	  SubMonitor progress = SubMonitor.convert(monitor, 100);
		    	  try{
		  			  String xArchURI = AS.xarch.getXArchURI(xArchRef);
		    		  progress.setWorkRemaining(toGenerate.length);
		    		  for (int i=0;i<toGenerate.length;i++){
		  				  String compId = toGenerate[i];
						  final String compDesp = dialog.getCompDescription(compId);
		    			  try{
		    				  progress.setTaskName("Generating code for Component "+compDesp+" ...");
		    				  codeGen.generateComp(xArchURI, compId, dialog.getArchPrescribedClassName(compId), dialog.getUserDefinedClassName(compId), progress.newChild(1));
		    				  //Record implementation names into the architecture file
		    				  setImpClassName(compId, dialog.getArchPrescribedClassName(compId),0);
		    				  setImpClassName(compId, dialog.getUserDefinedClassName(compId),1);
		    			  } catch (final CoreException ce){
		    				  Display.getDefault().asyncExec(new Runnable(){
								public void run(){
									ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "Cannot map Component "+compDesp+" to code",ce.getStatus());					
								}
							});
		    			  }
		    		  }
		    	  } finally {
		              if (monitor != null) {
		                  monitor.done();
		                }		    		  
		    	  }
		      }
		   });
		} catch (InterruptedException e){
			return false;
		} catch (InvocationTargetException e){
			return false;
		}
		
		//Send notifications
		String xArchURI = AS.xarch.getXArchURI(xArchRef);
		for (int k=0;k<toNotify.length;k++){
			String compId = toNotify[k];
			String userDefineClassName = dialog.getUserDefinedClassName(compId);
			Vector v = dialog.getMessages(compId);
			String[] msgs = (String []) v.toArray(new String[1]);
			if (userDefineClassName != null){
				String prjName = xArchURI.substring(1, xArchURI.indexOf('/', 1));
				//String target = "/"+prjName+"/src/"+userDefineClassName.replace('.', '/')+".java";
				IFile target = getIFileFromFQClassName(prjName, userDefineClassName);
				try{
					msger.notify(target, msgs);										
				} catch (CoreException ce){
					return false;
				}
			}			
		}
		
		AS.xarch.set(changesObj, "status", "mapped");
		return true;
	}
	
	public boolean mapChangesBySessionID (String sessionID, IProgressMonitor monitor){
		SubMonitor progress = SubMonitor.convert(monitor, 100);
		ObjRef changesObj = AS.xarch.getByID(sessionID);
		if (changesObj==null){
			return false;
		}
		//Internal representation of component changes, must satisfy: compAdded and compUpdated and compRemoved = null.
		Vector compAdded = new Vector<String>();
		Vector compRemoved = new Vector<String>();
		Map compUpdated = new HashMap<String, Vector>();

		Vector interactionAdded = new Vector<String>();
		Vector interactionRemoved = new Vector<String>();
		Vector interactionUpdated = new Vector<String>();

		Vector statechartAdded = new Vector<String>();
		Vector statechartRemoved = new Vector<String>();
		Vector statechartUpdated = new Vector<String>();

		try{
			//Load component changes. TODO: link changes, behavior changes.
			loadCompChanges(changesObj,compAdded, compRemoved, compUpdated);
			loadInteractionChanges(changesObj, interactionAdded, interactionRemoved, interactionUpdated);
			TreatInteractionChangesAsUpdateComponent(interactionAdded, interactionRemoved, interactionUpdated,
					compAdded, compRemoved, compUpdated);
			loadStatechartChanges(changesObj, statechartAdded, statechartRemoved, statechartUpdated);
			TreatStatechartChangesAsUpdateComponent(statechartAdded, statechartRemoved, statechartUpdated,
					compAdded, compRemoved, compUpdated);
			progress.worked(20);
			progress.setWorkRemaining(compAdded.size()+compUpdated.size());
			//Map changes to code
			String xArchURI = AS.xarch.getXArchURI(xArchRef);
			for (Enumeration e = compAdded.elements() ; e.hasMoreElements() ;) {
				String compId = (String)(e.nextElement());
				final String compDesp = XadlUtils.getDescription(AS.xarch, AS.xarch.getByID(compId));
				try{
					//Get implementation class names: both cannot be null
					String archPrescribedClassName = getImpClassName(compId, 0);
					String userDefineClassName = getImpClassName(compId, 1);
					if (archPrescribedClassName == null || archPrescribedClassName.length()==0){
						archPrescribedClassName = "comp."+compDesp.replace(" ", "")+"."+compDesp.replace(" ", "")+"Arch";
					}
					if (userDefineClassName == null || userDefineClassName.length()==0){
						userDefineClassName = "comp."+compDesp.replace(" ", "")+"."+compDesp.replace(" ", "")+"Imp";
					}
					//Generate code
					progress.setTaskName("Generating code for Component "+compDesp+" ...");
					codeGen.generateComp(xArchURI, compId, archPrescribedClassName, userDefineClassName, progress.newChild(1));
					//Record implementation names into the architecture file
					setImpClassName(compId, archPrescribedClassName,0);
					setImpClassName(compId, userDefineClassName,1);
				} catch (final CoreException ce){
					//System.out.println(ce.getMessage());
					Display.getDefault().asyncExec(new Runnable(){
						public void run(){
							ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "Cannot map Component "+compDesp+" to code",ce.getStatus());					
						}
					});
					return false;
				}
		     }
			for (Enumeration e = compRemoved.elements() ; e.hasMoreElements() ;) {
				//Pending on the extension of xADL
				String compId = (String)(e.nextElement());
				final String compDesp = XadlUtils.getDescription(AS.xarch, AS.xarch.getByID(compId));
				try{
					//Get implementation class names
					String userDefineClassName = getImpClassName(compId, 1);
					//Send notifications to user-defined class
					String[] msgs = new String[]{"Component "+compDesp+" is removed."};
					if (userDefineClassName != null){
						progress.setTaskName("Sending notifications to "+userDefineClassName+" ...");
						String prjName = xArchURI.substring(1, xArchURI.indexOf('/', 1));
						//String target = "/"+prjName+"/src/"+userDefineClassName.replace('.', '/')+".java";
						IFile target = getIFileFromFQClassName(prjName, userDefineClassName);
						msger.notify(target, msgs);					
					}
				}catch (final CoreException ce){
					//System.out.println(ce.getMessage());
					Display.getDefault().asyncExec(new Runnable(){
						public void run(){
							ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "Cannot map Component "+compDesp+" to code",ce.getStatus());					
						}
					});
					return false;
				}
		     }
			for (Iterator e = compUpdated.keySet().iterator(); e.hasNext();) {
				String compId = (String)(e.next());
				final String compDesp = XadlUtils.getDescription(AS.xarch, AS.xarch.getByID(compId));
				try{
					//Get implementation class names
					String archPrescribedClassName = getImpClassName(compId, 0);
					String userDefineClassName = getImpClassName(compId, 1);
					if (archPrescribedClassName == null || archPrescribedClassName.length()==0){
						archPrescribedClassName = "comp."+compDesp.replace(" ", "")+"."+compDesp.replace(" ", "")+"Arch";
					}
					if (userDefineClassName == null || userDefineClassName.length()==0){
						userDefineClassName = "comp."+compDesp.replace(" ", "")+"."+compDesp.replace(" ", "")+"Imp";
					}
					//Generate code
					progress.setTaskName("Generating code for Component "+compDesp+" ...");
					codeGen.generateComp(xArchURI, compId, archPrescribedClassName, userDefineClassName, progress.newChild(1));
					//Record implementation names into the architecture file
					setImpClassName(compId, archPrescribedClassName,0);
					setImpClassName(compId, userDefineClassName,1);
					//Send notifications to user-defined class
					Vector v = (Vector)compUpdated.get(compId);
					String[] msgs = (String []) v.toArray(new String[1]);
					if (userDefineClassName != null){
						progress.setTaskName("Sending notifications to "+userDefineClassName+" ...");
						String prjName = xArchURI.substring(1, xArchURI.indexOf('/', 1));
						//String target = "/"+prjName+"/src/"+userDefineClassName.replace('.', '/')+".java";
						IFile target = getIFileFromFQClassName(prjName, userDefineClassName);
						msger.notify(target, msgs);					
					}
				}catch (final CoreException ce){
					//System.out.println(ce.getMessage());
					Display.getDefault().asyncExec(new Runnable(){
						public void run(){
							ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "Cannot map Component "+compDesp+" to code",ce.getStatus());					
						}
					});
					return false;
				}
		     }
			AS.xarch.set(changesObj, "status", "mapped");
			return true;			
		} finally {
              if (monitor != null) {
                monitor.done();
              }
        } 
	}
	
	public void mapChangesOfAllSessions (IProgressMonitor monitor){

		SubMonitor progress = SubMonitor.convert(monitor, 100);
		
		try{
			//Load and analyze changes, convert them into internal representation.
			ObjRef changesContextRef = AS.xarch.createContext(xArchRef, "changes");		
			ObjRef archChange = AS.xarch.getElement(changesContextRef, "archChange", xArchRef);
			ObjRef[] changes = AS.xarch.getAll(archChange, "changes");
			progress.setWorkRemaining(changes.length);
			for (ObjRef chg: changes){
				String status = (String)AS.xarch.get(chg, "status");
				//Only map "unmapped"
				if (status.equals("unmapped")){
					mapChangesBySessionID(XadlUtils.getID(AS.xarch, chg), progress.newChild(1));
				}			
			}
		} finally {
              if (monitor != null) {
                monitor.done();
              }
        } 
	}
	
	/*
	 Generate code for all components regardless of whether they are changed or not.
	 This is primarily to deal with existing software systems.
	*/
	public void mapAllComponentsByForce(){
		ObjRef typesContextRef = AS.xarch.createContext(xArchRef, "types");		
		ObjRef archStructure = AS.xarch.getElement(typesContextRef, "archStructure", xArchRef);
		final ObjRef[] components = AS.xarch.getAll(archStructure, "component");
		IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		try{
			progressService.busyCursorWhile(new IRunnableWithProgress(){
				public void run(IProgressMonitor monitor) {
					SubMonitor progress = SubMonitor.convert(monitor, 100);
					try{
					progress.setWorkRemaining(components.length);
					String xArchURI = AS.xarch.getXArchURI(xArchRef);
					for (ObjRef component: components){
						String compId = XadlUtils.getID(AS.xarch, component);
						final String compDesp = XadlUtils.getDescription(AS.xarch, component);
						try{
							//Get implementation class names: both cannot be null
							String archPrescribedClassName = getImpClassName(compId, 0);
							String userDefineClassName = getImpClassName(compId, 1);
							if (archPrescribedClassName == null || archPrescribedClassName.length()==0){
								archPrescribedClassName = "comp."+compDesp.replace(" ", "")+"."+compDesp.replace(" ", "")+"Arch";
							}
							if (userDefineClassName == null || userDefineClassName.length()==0){
								userDefineClassName = "comp."+compDesp.replace(" ", "")+"."+compDesp.replace(" ", "")+"Imp";
							}
							//Generate code
							progress.setTaskName("Generating code for Component "+compDesp+" ...");
							codeGen.generateComp(xArchURI, compId, archPrescribedClassName, userDefineClassName, progress.newChild(1));
							//Record implementation names into the architecture file
							setImpClassName(compId, archPrescribedClassName,0);
							setImpClassName(compId, userDefineClassName,1);
						} catch (final CoreException ce){
							//System.out.println(ce.getMessage());
							Display.getDefault().asyncExec(new Runnable(){
								public void run(){
									ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "Cannot map Component "+compDesp+" to code",ce.getStatus());					
								}
							});
						}
					}//end of for loop
					} finally {
						if (monitor != null) {
							monitor.done();
			            }
					}
		    	}
		    });
		} catch (InterruptedException e){
			   ;
		} catch (InvocationTargetException e){
			   ;
		}
	}
	
	public static abstract class MonitoredTask
	{
		private String description;
		
		public MonitoredTask(String description)
		{
			this.description = description;
		}
		
		public String getDescription()
		{
			return this.description;
		}
		
		public abstract void run(IProgressMonitor monitor);
	}
	
	public List<MonitoredTask> prepareComponentCodeTasks(final List<ObjRef> componentRefs)
	{
		final List<MonitoredTask> tasks = new LinkedList<MonitoredTask>();
		final String xArchURI = AS.xarch.getXArchURI(xArchRef);
		
		for (ObjRef componentRef: componentRefs) {
			final String compId = XadlUtils.getID(AS.xarch, componentRef);
			final String compDesp = XadlUtils.getDescription(AS.xarch, componentRef);

			final MonitoredTask task = new MonitoredTask("Processing code for Component "+compDesp+" ...") {
				@Override
				public void run(IProgressMonitor monitor) {
					try {
						String archPrescribedClassName = getImpClassName(compId, 0);
						String userDefineClassName = getImpClassName(compId, 1);
						
						if (archPrescribedClassName == null || archPrescribedClassName.length() == 0) {
							archPrescribedClassName = "comp."+compDesp.replace(" ", "")+"."+compDesp.replace(" ", "")+"Arch";
						}
						if (userDefineClassName == null || userDefineClassName.length() == 0) {
							userDefineClassName = "comp."+compDesp.replace(" ", "")+"."+compDesp.replace(" ", "")+"Imp";
						}
						
						codeGen.generateComp(xArchURI, compId, archPrescribedClassName, userDefineClassName, monitor);
					} catch (final CoreException ce) {
						Display.getDefault().asyncExec(new Runnable(){
							public void run(){
								ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "Cannot map Component "+compDesp+" to code", ce.getStatus());					
							}
						});
					}	
				}
			};
			
			tasks.add(task);
		}
		
		return tasks;
	}

	public void executeMonitoredTasks(final List<MonitoredTask> tasks)
	{
		final IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		
		try {
			progressService.busyCursorWhile(new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					final SubMonitor progress = SubMonitor.convert(monitor, 100);
					
					if (monitor != null) {
						progress.setWorkRemaining(tasks.size());

						for (MonitoredTask task: tasks) {
							progress.setTaskName(task.getDescription());
							task.run(progress.newChild(1));
						}
						
						monitor.done();
		            }
				}
			});
		} catch (InvocationTargetException e) {
			;
		} catch (InterruptedException e) {
			;
		}
	}

	public void mapComponentByForce(String componentId){
		final String compId = componentId;
		IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		try{
			progressService.busyCursorWhile(new IRunnableWithProgress(){
				public void run(IProgressMonitor monitor) {
					String xArchURI = AS.xarch.getXArchURI(xArchRef);
					ObjRef component = AS.xarch.getByID(compId);
					final String compDesp = XadlUtils.getDescription(AS.xarch, component);
					try{	
						//Get implementation class names: both cannot be null
						String archPrescribedClassName = getImpClassName(compId, 0);
						String userDefineClassName = getImpClassName(compId, 1);
						if (archPrescribedClassName == null || archPrescribedClassName.length()==0){
							archPrescribedClassName = "comp."+compDesp.replace(" ", "")+"."+compDesp.replace(" ", "")+"Arch";
						}
						if (userDefineClassName == null || userDefineClassName.length()==0){
							userDefineClassName = "comp."+compDesp.replace(" ", "")+"."+compDesp.replace(" ", "")+"Imp";
						}
						//Generate code
						monitor.setTaskName("Generating code for Component "+compDesp+" ...");
						codeGen.generateComp(xArchURI, compId, archPrescribedClassName, userDefineClassName, monitor);
						//Record implementation names into the architecture file
						setImpClassName(compId, archPrescribedClassName,0);
						setImpClassName(compId, userDefineClassName,1);
					} catch (final CoreException ce){
						//System.out.println(ce.getMessage());
						Display.getDefault().asyncExec(new Runnable(){
							public void run(){
								ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "Cannot map Component "+compDesp+" to code",ce.getStatus());					
							}
						});
					}
		    	}
		    });
		} catch (InterruptedException e){
			   ;
		} catch (InvocationTargetException e){
			   ;
		}	
	}
	
	protected void loadStatechartChanges(ObjRef chg, Vector added, Vector removed, Vector updated){
		ObjRef[] statechartChanges = AS.xarch.getAll(chg, "statechartChange");
		for (ObjRef intn: statechartChanges){
			String chgType = (String) AS.xarch.get(intn, "type");
			ObjRef statechart = XadlUtils.resolveXLink(AS.xarch, intn, "statechart");			
			if (statechart==null){
				//The target state chart is removed. Use its copy for the information.
				statechart = getCopy((ObjRef)AS.xarch.get(intn, "statechart"));
			}
			String statechartID = XadlUtils.getID(AS.xarch, statechart);
			if (chgType.equalsIgnoreCase("add")){
				added.add(statechartID);
			} else if (chgType.equalsIgnoreCase("update")){
				if ((!added.contains(statechartID)) && (!updated.contains(statechartID))){
					updated.add(statechartID);
				}
			} else if (chgType.equalsIgnoreCase("remove")){
				if (added.contains(statechartID)){
					added.remove(statechartID);
				} else if (updated.contains(statechartID)){
					updated.remove(statechartID);
					removed.add(statechartID);
				} else {
					removed.add(statechartID);
				}
			}
		}
	}

	protected void TreatStatechartChangesAsUpdateComponent(Vector<String> added, Vector<String> removed, Vector<String> updated, Vector addedComp, Vector removedComp, Map updatedComp){
		for (String statechartID: added){
			ObjRef scRef = AS.xarch.getByID(statechartID);
			ObjRef linkedComp = (ObjRef)AS.xarch.get(scRef, "linkedComp");
			if (linkedComp == null){
				//this state chart is not associated with any component.
				break;
			}
			ObjRef component = XadlUtils.resolveXLink(AS.xarch, scRef, "linkedComp");
			if (component==null){
				//The target component is removed. Use its copy for component information.
				component = getCopy(linkedComp);
			}
			String compID = XadlUtils.getID(AS.xarch, component);
			if ((!addedComp.contains(compID))&&(!removedComp.contains(compID))){
				String compDesp = XadlUtils.getDescription(AS.xarch, component);
				String msg = "A state chart was created for Component "+compDesp;
				if (updatedComp.containsKey(compID)){
					((Vector)updatedComp.get(compID)).add(msg);
				} else {
					Vector v = new Vector<String>();
					v.add(msg);
					updatedComp.put(compID, v);
				}
			}
		}
		for (String statechartID: removed){
			ObjRef scRef = AS.xarch.getByID(statechartID);
			ObjRef linkedComp = (ObjRef)AS.xarch.get(scRef, "linkedComp");
			if (linkedComp == null){
				//this state chart is not associated with any component.
				break;
			}
			ObjRef component = XadlUtils.resolveXLink(AS.xarch, scRef, "linkedComp");
			if (component==null){
				//The target component is removed. Use its copy for component information.
				component = getCopy(linkedComp);
			}
			String compID = XadlUtils.getID(AS.xarch, component);
			if ((!addedComp.contains(compID))&&(!removedComp.contains(compID))){
				String compDesp = XadlUtils.getDescription(AS.xarch, component);
				String msg = "The state chart of Component "+compDesp+" was removed";
				if (updatedComp.containsKey(compID)){
					((Vector)updatedComp.get(compID)).add(msg);
				} else {
					Vector v = new Vector<String>();
					v.add(msg);
					updatedComp.put(compID, v);
				}
			}
		}
		for (String statechartID: updated){
			ObjRef scRef = AS.xarch.getByID(statechartID);
			ObjRef linkedComp = (ObjRef)AS.xarch.get(scRef, "linkedComp");
			if (linkedComp == null){
				//this state chart is not associated with any component.
				break;
			}
			ObjRef component = XadlUtils.resolveXLink(AS.xarch, scRef, "linkedComp");
			if (component==null){
				//The target component is removed. Use its copy for component information.
				component = getCopy(linkedComp);
			}
			String compID = XadlUtils.getID(AS.xarch, component);
			if ((!addedComp.contains(compID))&&(!removedComp.contains(compID))){
				String compDesp = XadlUtils.getDescription(AS.xarch, component);
				String msg = "The state chart of Component "+compDesp+" was updated";
				if (updatedComp.containsKey(compID)){
					((Vector)updatedComp.get(compID)).add(msg);
				} else {
					Vector v = new Vector<String>();
					v.add(msg);
					updatedComp.put(compID, v);
				}
			}
		}
	}
	
	protected void loadInteractionChanges(ObjRef chg, Vector added, Vector removed, Vector updated){
		ObjRef[] interactionChanges = AS.xarch.getAll(chg, "interactionChange");
		for (ObjRef intn: interactionChanges){
			String chgType = (String) AS.xarch.get(intn, "type");
			ObjRef interaction = XadlUtils.resolveXLink(AS.xarch, intn, "interaction");			
			if (interaction==null){
				//The target interaction is removed. Use its copy for the information.
				interaction = getCopy((ObjRef)AS.xarch.get(intn, "interaction"));
			}
			String interactionID = XadlUtils.getID(AS.xarch, interaction);
			if (chgType.equalsIgnoreCase("add")){
				added.add(interactionID);
			} else if (chgType.equalsIgnoreCase("update")){
				if ((!added.contains(interactionID)) && (!updated.contains(interactionID))){
					updated.add(interactionID);
				}
			} else if (chgType.equalsIgnoreCase("remove")){
				if (added.contains(interactionID)){
					added.remove(interactionID);
				} else if (updated.contains(interactionID)){
					updated.remove(interactionID);
					removed.add(interactionID);
				} else {
					removed.add(interactionID);
				}
			}
		}
	}
	
	protected void TreatInteractionChangesAsUpdateComponent(Vector<String> added, Vector<String> removed, Vector<String> updated, Vector addedComp, Vector removedComp, Map updatedComp){
		for (String interactionID: added){
			ObjRef intnRef = AS.xarch.getByID(interactionID);
			ObjRef linkedComp = (ObjRef)AS.xarch.get(intnRef, "targetComp");
			if (linkedComp == null){
				//this state chart is not associated with any component.
				break;
			}
			ObjRef component = XadlUtils.resolveXLink(AS.xarch, intnRef, "targetComp");
			if (component==null){
				//The target component is removed. Use its copy for component information.
				component = getCopy((ObjRef)AS.xarch.get(intnRef, "targetComp"));
			}
			String compID = XadlUtils.getID(AS.xarch, component);
			String compDesp = XadlUtils.getDescription(AS.xarch, component);
			String opDesp = XadlUtils.getDescription(AS.xarch, (AS.xarch.getAll(intnRef, "note"))[0]);
			String msg = "A new interaction diagram was created for Component "+compDesp+"'s operation: "+opDesp;
			if ((!addedComp.contains(compID))&&(!removedComp.contains(compID))){
				if (updatedComp.containsKey(compID)){
					((Vector)updatedComp.get(compID)).add(msg);
				} else {
					Vector v = new Vector<String>();
					v.add(msg);
					updatedComp.put(compID, v);
				}
			}
		}
		for (String interactionID: removed){
			ObjRef intnRef = AS.xarch.getByID(interactionID);
			ObjRef linkedComp = (ObjRef)AS.xarch.get(intnRef, "targetComp");
			if (linkedComp == null){
				//this state chart is not associated with any component.
				break;
			}
			ObjRef component = XadlUtils.resolveXLink(AS.xarch, intnRef, "targetComp");
			if (component==null){
				//The target component is removed. Use its copy for component information.
				component = getCopy((ObjRef)AS.xarch.get(intnRef, "targetComp"));
			}
			String compID = XadlUtils.getID(AS.xarch, component);
			String compDesp = XadlUtils.getDescription(AS.xarch, component);
			String opDesp = XadlUtils.getDescription(AS.xarch, (AS.xarch.getAll(intnRef, "note"))[0]);
			String msg = "The interaction diagram was removed from Component "+compDesp+"'s operation: "+opDesp;
			if ((!addedComp.contains(compID))&&(!removedComp.contains(compID))){
				if (updatedComp.containsKey(compID)){
					((Vector)updatedComp.get(compID)).add(msg);
				} else {
					Vector v = new Vector<String>();
					v.add(msg);
					updatedComp.put(compID, v);
				}
			}
		}
		for (String interactionID: updated){
			ObjRef intnRef = AS.xarch.getByID(interactionID);
			ObjRef linkedComp = (ObjRef)AS.xarch.get(intnRef, "targetComp");
			if (linkedComp == null){
				//this state chart is not associated with any component.
				break;
			}
			ObjRef component = XadlUtils.resolveXLink(AS.xarch, intnRef, "targetComp");
			if (component==null){
				//The target component is removed. Use its copy for component information.
				component = getCopy((ObjRef)AS.xarch.get(intnRef, "targetComp"));
			}
			String compID = XadlUtils.getID(AS.xarch, component);
			String compDesp = XadlUtils.getDescription(AS.xarch, component);
			String opDesp = XadlUtils.getDescription(AS.xarch, (AS.xarch.getAll(intnRef, "note"))[0]);
			String msg = "The interaction diagram was updated on Component "+compDesp+"'s operation: "+opDesp;
			if ((!addedComp.contains(compID))&&(!removedComp.contains(compID))){
				if (updatedComp.containsKey(compID)){
					((Vector)updatedComp.get(compID)).add(msg);
				} else {
					Vector v = new Vector<String>();
					v.add(msg);
					updatedComp.put(compID, v);
				}
			}
		}
	}
	
	protected void loadCompChanges(ObjRef chg, Vector added, Vector removed, Map updated){
		ObjRef[] compChanges = AS.xarch.getAll(chg, "componentChange");
		for(ObjRef compChg: compChanges){
			String chgType = (String) AS.xarch.get(compChg, "type");
			ObjRef component = XadlUtils.resolveXLink(AS.xarch, compChg, "component");
			if (component==null){
				//The target component is removed. Use its copy for component information.
				component = getCopy((ObjRef)AS.xarch.get(compChg, "component"));
			}
			String compID = XadlUtils.getID(AS.xarch, component);
			String compDesp = XadlUtils.getDescription(AS.xarch, component);
			
			if (chgType.equalsIgnoreCase("add")){
				added.add(compID);
			} else if (chgType.equalsIgnoreCase("update")){
				if (!added.contains(compID)){
					if (updated.containsKey(compID)){
						((Vector)updated.get(compID)).add(generateNotification(compChg, compDesp));
					} else {
						Vector v = new Vector<String>();
						v.add(generateNotification(compChg, compDesp));
						updated.put(compID, v);
					}
				}
			} else if (chgType.equalsIgnoreCase("remove")){
				if (added.contains(compID)){
					added.remove(compID);
				} else if (updated.containsKey(compID)){
					((Vector)updated.get(compID)).clear();
					updated.remove(compID);
					removed.add(compID);
				} else {
					removed.add(compID);
				}
			}
		}	
	}
	
	protected String generateNotification(ObjRef compChange, String compDesp){
		ObjRef intfChange = (ObjRef) AS.xarch.get(compChange, "interfaceChange");
		if (intfChange != null){
			ObjRef intfElt = XadlUtils.resolveXLink(AS.xarch, intfChange, "interface");
			if (intfElt == null){
				//The target interface is removed. Use its copy for component information.
				intfElt = getCopy((ObjRef)AS.xarch.get(intfChange, "interface"));
			}
			try {
				String intfEltName = XadlUtils.getDescription(AS.xarch, intfElt);
				String intrEltImp = getImpClassName(XadlUtils.getID(AS.xarch, intfElt), 0);
				String changeType = (String) AS.xarch.get(intfChange, "type");
				String msg;
				if ((intfEltName == null)||(intfEltName.length()==0)){
					msg = "A new interface";
				} else {
					msg = "Interface "+ intfEltName;
				}
				if (changeType.equalsIgnoreCase("add")){
					msg = msg.concat(" was created on Component "+compDesp+".");
				} else if (changeType.equalsIgnoreCase("update")){
					msg = msg.concat(" was updated on Component "+compDesp+".");					
				} else if (changeType.equalsIgnoreCase("remove")){
					msg = msg.concat(" was removed from Component "+compDesp+".");					
				}
				if (intrEltImp!=null){
					msg = msg.concat(" (Interface type: "+intrEltImp+" )");
				}
				return msg;
			} catch (CoreException ce){
				return null;
			}
		}
		return XadlUtils.getDescription(AS.xarch, compChange);
	}
	
	protected ObjRef getCopy(ObjRef ref){
		String linkTypeString = (String)AS.xarch.get(ref, "type");
		if(linkTypeString == null || linkTypeString.equals("simple")){
			String hrefString = (String)AS.xarch.get(ref, "href");
			if(hrefString == null){
				return null;
			}
			else{
		        int i = hrefString.indexOf('#');
		        if (i == -1 || i == (hrefString.length()-1)) {
		            return null;
		        }
		        String newHref = hrefString.substring(0,i+1)+"Copy of "+hrefString.substring(i+1);
				ObjRef xArchRef = AS.xarch.getXArch(ref);
				ObjRef targetRef = AS.xarch.resolveHref(xArchRef, newHref);
				return targetRef;
			}
		}
		return null;
	}
	
	public void setImpClassName(String compID, String className, int classType) throws CoreException{
		//classType - 0:architecture prescribed; 1:user-defined.
		ObjRef brickRef =  AS.xarch.getByID(compID);
		if (brickRef == null){
			throw new CoreException(new Status(IStatus.ERROR, "edu.uci.isr.archstudio4", "Brick missing"));
		}
		ObjRef impHostRef = XadlUtils.resolveXLink(AS.xarch, brickRef, "type");
		if(impHostRef == null){//No type is defined for this component, implementation is defined in component instance.
			
			impHostRef  = brickRef;
			//Check if this is an optional component
			if (AS.xarch.isInstanceOf(impHostRef, "options#OptionalComponent")){
				ObjRef varContextRef = AS.xarch.createContext(xArchRef, "implementationext");
				AS.xarch.promoteTo(varContextRef, "OptionalComponentImpl", impHostRef);				
			} else {
				//Component must be promoted to ComponentImpl to have an implementation
				if (!AS.xarch.isInstanceOf(impHostRef, "implementationext#ComponentImpl")){
					//Promotion has to be done step by step.
					ObjRef varContextRef = AS.xarch.createContext(xArchRef, "implementationext");
					AS.xarch.promoteTo(varContextRef, "ComponentImpl", impHostRef);
				}				
			}
			
		} else {//Implementation is defined in component type.
			//Component type must be promoted to VariantComponentTypeImpl to have an implementation
			if (!AS.xarch.isInstanceOf(impHostRef, "implementation#VariantComponentTypeImpl")){
				//Promotion has to be done step by step.
				ObjRef varContextRef = AS.xarch.createContext(xArchRef, "variants");
				AS.xarch.promoteTo(varContextRef, "VariantComponentType", impHostRef);
				ObjRef impContextRef = AS.xarch.createContext(xArchRef, "implementation");
				AS.xarch.promoteTo(impContextRef, "VariantComponentTypeImpl", impHostRef);
			}
		}
		
		ObjRef[] implementationRefs = AS.xarch.getAll(impHostRef, "implementation");
		if(implementationRefs == null || implementationRefs.length == 0){
			ObjRef impContextRef = AS.xarch.createContext(xArchRef, "implementation");
			ObjRef implementationRef = AS.xarch.create(impContextRef, "implementation");
			AS.xarch.add(impHostRef, "implementation", implementationRef);
			ObjRef javaImpContextRef = AS.xarch.createContext(xArchRef, "javaimplementation");
			AS.xarch.promoteTo(javaImpContextRef, "JavaImplementation", implementationRef);
			implementationRefs = AS.xarch.getAll(impHostRef, "implementation");
		}
		ObjRef javaImplementationRef = null;
		for(ObjRef element: implementationRefs){
			if(AS.xarch.isInstanceOf(element, "javaimplementation#JavaImplementation")){
				javaImplementationRef = element;
				break;
			}
		}
		if(javaImplementationRef == null){
			ObjRef impContextRef = AS.xarch.createContext(xArchRef, "implementation");
			javaImplementationRef = AS.xarch.create(impContextRef, "implementation");
			AS.xarch.add(impHostRef, "implementation", javaImplementationRef);
			ObjRef javaImpContextRef = AS.xarch.createContext(xArchRef, "javaimplementation");
			AS.xarch.promoteTo(javaImpContextRef, "JavaImplementation", javaImplementationRef);
		}
		ObjRef classRef;
		if (classType == 0){
			//mainClass: architecture-prescribed
			classRef = (ObjRef)AS.xarch.get(javaImplementationRef, "mainClass");
			if(classRef == null){
				ObjRef javaImpContextRef = AS.xarch.createContext(xArchRef, "javaimplementation");
				classRef = AS.xarch.create(javaImpContextRef, "JavaClassFile");
				AS.xarch.set(javaImplementationRef, "mainClass", classRef);
			}
		} else {
			//auxClass: user-defined
			ObjRef[] auxClassRefs = AS.xarch.getAll(javaImplementationRef, "auxClass");
			if(auxClassRefs == null || auxClassRefs.length ==0){
				ObjRef javaImpContextRef = AS.xarch.createContext(xArchRef, "javaimplementation");
				classRef = AS.xarch.create(javaImpContextRef, "JavaClassFile");
				AS.xarch.add(javaImplementationRef, "auxClass", classRef);
			} else {
				classRef = auxClassRefs[0];				
			}
		}
		ObjRef classNameRef = (ObjRef)AS.xarch.get(classRef, "javaClassName");
		if(classNameRef == null){
			ObjRef javaImpContextRef = AS.xarch.createContext(xArchRef, "javaimplementation");
			classNameRef = AS.xarch.create(javaImpContextRef, "JavaClassName");
			AS.xarch.set(classRef, "javaClassName", classNameRef);
		}
		AS.xarch.set(classNameRef, "value", className);
		return;
	}
	
	public IFile getComponentImpFile(String compID, int classType) throws CoreException
	{
		final String componentImpClassName = getImpClassName(compID, classType);
		
		final String xArchURI = AS.xarch.getXArchURI(xArchRef);
		final String prjName = xArchURI.substring(1, xArchURI.indexOf('/', 1));
		
		final IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(prjName);
		final IJavaProject javaProj = JavaCore.create(proj);
		final IType type = javaProj.findType(componentImpClassName);
		final IFile componentImpFile = (IFile) type.getCompilationUnit().getCorrespondingResource();

		return componentImpFile;
	}

	public String getImpClassName(String compID, int classType) throws CoreException{
		//Get the notification target: compType/implementation/javaimplementation/auxClass/javaClassName.
		ObjRef brickRef =  AS.xarch.getByID(compID);
		if (brickRef == null){
			throw new CoreException(new Status(IStatus.ERROR, "edu.uci.isr.archstudio4", "Brick missing"));
		}
		ObjRef impHostRef = XadlUtils.resolveXLink(AS.xarch, brickRef, "type");
		if(impHostRef == null){//No type is defined for this component, get implementation information from the instance.
			impHostRef = brickRef;
		}
		
		/* to be removed.
		boolean flag = false;
		if (AS.xarch.isInstanceOf(impHostRef, "options#OptionalComponent")){
			flag = true;
			ObjRef varContextRef = AS.xarch.createContext(xArchRef, "implementationext");
			AS.xarch.promoteTo(varContextRef, "ComponentImpl", impHostRef);
		}
		*/
		
		if (!AS.xarch.isInstanceOf(impHostRef, "implementation#VariantComponentTypeImpl")){
			if (!AS.xarch.isInstanceOf(impHostRef, "implementationext#ComponentImpl")){
				if (!AS.xarch.isInstanceOf(impHostRef, "implementation#InterfaceTypeImpl")){
					if(!AS.xarch.isInstanceOf(impHostRef, "implementationext#OptionalComponentImpl")){
					//Playing a trick here: this is to get the implementation of an interface.
					return null;				
					}
				}				
			}
		}
		ObjRef[] implementationRefs = AS.xarch.getAll(impHostRef, "implementation");
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
		ObjRef classRef;
		if (classType == 0){
			//mainClass
			classRef = (ObjRef)AS.xarch.get(javaImplementationRef, "mainClass");
			if(classRef == null){
				return null;
			}
		} else {
			//auxClass
			ObjRef[] auxClassRefs = AS.xarch.getAll(javaImplementationRef, "auxClass");
			if(auxClassRefs == null || auxClassRefs.length ==0){
				return null;
			}
			classRef = auxClassRefs[0];
		}
		ObjRef classNameRef = (ObjRef)AS.xarch.get(classRef, "javaClassName");
		if(classNameRef == null){
			return null;
		}
		String className = (String)AS.xarch.get(classNameRef, "value");
		if(className == null){
			return null;
		}
		/* to be removed.
		if(flag == true){
			
		}
		*/
		return className;
	}
	
	protected IFile getIFileFromFQClassName(String prjName, String fqClassName){
		IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(prjName);
		IJavaProject jProj = JavaCore.create(proj);
		try {
			IType type = jProj.findType(fqClassName);
			IFile targetFile = (IFile) type.getCompilationUnit().getCorrespondingResource();
			return targetFile;
		} catch (Exception e){
			return null;			
		}
	}

	public void mapAllComponentsByForceWithDialog() {
		ObjRef typesContextRef = AS.xarch.createContext(xArchRef, "types");		
		ObjRef archStructure = AS.xarch.getElement(typesContextRef, "archStructure", xArchRef);
		final ObjRef[] components = AS.xarch.getAll(archStructure, "component");
		final String[] compIds = new String[components.length];

		int i = 0;
		final xMapperDialog dialog = new xMapperDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		for (ObjRef comp: components) {
			String compId = XadlUtils.getID(AS.xarch, comp);
			String compDesp = XadlUtils.getDescription(AS.xarch, comp);
			compIds[i++] = compId;
			dialog.setCompDescription(compId, compDesp);
			try{
				String archPrescribedClassName = getImpClassName(compId, 0);
				String userDefineClassName = getImpClassName(compId, 1);
				if (archPrescribedClassName != null && archPrescribedClassName.length()>0){
					dialog.setArchPrescribedClassName(compId, archPrescribedClassName);
				}
				if (userDefineClassName != null && userDefineClassName.length()>0){
					dialog.setUserDefinedClassName(compId, userDefineClassName);
				}
			} catch (CoreException ce){
				return;
			}
		}
		
		if (components.length>0){
			if (dialog.open(compIds) == 0){
				return;
			}			
		}

		//Generate code for changed components
		//IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		try{
			ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
			pmd.run(true, true, new IRunnableWithProgress(){
		      public void run(IProgressMonitor monitor) {
		    	  SubMonitor progress = SubMonitor.convert(monitor, 100);
		    	  try{
		  			  String xArchURI = AS.xarch.getXArchURI(xArchRef);
		    		  progress.setWorkRemaining(compIds.length);
		    		  for (int i=0;i<compIds.length;i++){
		  				  String compId = compIds[i];
						  final String compDesp = dialog.getCompDescription(compId);
		    			  try{
		    				  progress.setTaskName("Generating code for Component "+compDesp+" ...");
		    				  codeGen.generateComp(xArchURI, compId, dialog.getArchPrescribedClassName(compId), dialog.getUserDefinedClassName(compId), progress.newChild(1));
		    				  //Record implementation names into the architecture file
		    				  setImpClassName(compId, dialog.getArchPrescribedClassName(compId),0);
		    				  setImpClassName(compId, dialog.getUserDefinedClassName(compId),1);
		    			  } catch (final CoreException ce){
		    				  Display.getDefault().asyncExec(new Runnable(){
								public void run(){
									ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "Cannot map Component "+compDesp+" to code",ce.getStatus());					
								}
							});
		    			  }
		    		  }
		    	  } finally {
		              if (monitor != null) {
		                  monitor.done();
		                }		    		  
		    	  }
		      }
		   });
		} catch (InterruptedException e){
			;
		} catch (InvocationTargetException e){
			;
		}
	}
}