package edu.uci.isr.archstudio4.comp.xarchcs.xarchcs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import edu.uci.isr.archstudio4.comp.xarchcs.changesetadt.IChangeSetADT;
import edu.uci.isr.archstudio4.comp.xarchcs.changesetadt.IChangeSetADT.IChangeReference;
import edu.uci.isr.archstudio4.comp.xarchcs.changesetsync.IChangeSetSync;
import edu.uci.isr.archstudio4.comp.xarchcs.changesetsync.IChangeSetSync.ChangeStatus;
import edu.uci.isr.archstudio4.comp.xarchcs.changesetsync.IChangeSetSync.IChangeSetSyncMonitor;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetEvent.ChangeSetEventType;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchfakedetach.XArchDetachInterface;
import edu.uci.isr.sysutils.UIDGenerator;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.IXArchPropertyMetadata;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFileEvent;
import edu.uci.isr.xarchflat.XArchFileListener;
import edu.uci.isr.xarchflat.XArchFlatEvent;
import edu.uci.isr.xarchflat.XArchFlatImplDelegate;
import edu.uci.isr.xarchflat.XArchFlatInterface;
import edu.uci.isr.xarchflat.XArchFlatListener;
import edu.uci.isr.xarchflat.XArchFlatUtils;

public class XArchChangeSetImpl
    extends XArchFlatImplDelegate
    implements XArchChangeSetInterface, XArchFileListener, XArchFlatListener{

	private final static boolean DEBUG = false;

	static final boolean equalz(Object o1, Object o2){
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	// if this needs to be optimized later, see: http://www.cs.dartmouth.edu/~doug/diff.ps
	static final <T>Set<T> diff(T[] o1, T[] o2){
		int[][] c = new int[o1.length + 1][o2.length + 1];
		for(int i = 1; i <= o1.length; i++){
			for(int j = 1; j <= o2.length; j++){
				if(equalz(o1[i - 1], o2[j - 1])){
					c[i][j] = c[i - 1][j - 1] + 1;
				}
				else{
					c[i][j] = Math.max(c[i][j - 1], c[i - 1][j]);
				}
			}
		}
		int i = o1.length;
		int j = o2.length;
		Set<T> diff = new HashSet<T>();
		while(i >= 0 && j >= 0){
			if(i > 0 && j > 0 && equalz(o1[i - 1], o2[j - 1])){
				i--;
				j--;
				continue;
			}
			if(j > 0 && (i == 0 || c[i][j - 1] >= c[i - 1][j])){
				j--;
				diff.add(o2[j]);
				continue;
			}
			if(i > 0 && (j == 0 || c[i][j - 1] < c[i - 1][j])){
				i--;
				diff.add(o1[i]);
				continue;
			}
			break;
		}
		return diff;
	}

	static final String capFirstLetter(String s){
		if(s == null || s.length() == 0){
			return s;
		}
		char ch = s.charAt(0);
		if(Character.isUpperCase(ch)){
			return s;
		}
		return Character.toUpperCase(ch) + s.substring(1);
	}

	class ChangeSetState{

		final Object lock = new Object();
		final ObjRef xArchRef;
		final ObjRef changesetsContextRef;
		ObjRef activeChangeSetRef = null;
		ObjRef[] changeSetRefs = new ObjRef[0];
		boolean activeChangeSetApplied = false;
		ExecutorService executor = null;
		IChangeSetSyncMonitor monitor = new IChangeSetSyncMonitor(){

			boolean canceled = false;

			public void beginTask(int totalWork){
			}

			public void worked(int work){
			}

			public void done(){
			}

			public void setCanceled(boolean canceled){
				this.canceled = canceled;
			}

			public boolean isCanceled(){
				return canceled;
			}
		};

		public ChangeSetState(ObjRef xArchRef){
			this.xArchRef = xArchRef;
			this.changesetsContextRef = xarch.createContext(xArchRef, "changesets");
			finishExecution(true);
		}

		public void finishExecution(boolean cancel){
			try{
				if(executor != null){
					executor.shutdown();
					monitor.setCanceled(cancel);
					executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
				}
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
			finally{
				monitor.setCanceled(false);
				//executor = Executors.newSingleThreadExecutor();
				executor = new AbstractExecutorService(){

					boolean shutdown = false;

					public boolean isShutdown(){
						return shutdown;
					}

					public boolean isTerminated(){
						return shutdown;
					}

					public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException{
						return true;
					}

					public void shutdown(){
						shutdown = true;
					}

					public List<Runnable> shutdownNow(){
						shutdown = true;
						return Collections.emptyList();
					}

					public void execute(Runnable command){
						command.run();
					}
				};
			}
		}

		void setActiveChangeSetRef(ObjRef activeChangeSetRef){
			this.activeChangeSetRef = activeChangeSetRef;
			update();
		}

		void setChangeSetRefs(ObjRef[] changeSetRefs){
			this.changeSetRefs = changeSetRefs;
			update();
		}

		void update(){
			activeChangeSetApplied = Arrays.asList(changeSetRefs).contains(activeChangeSetRef);
		}
	}

	IChangeSetADT csadt;

	IChangeSetSync cssync;

	XArchDetachInterface xarchd;

	Map<ObjRef, ChangeSetState> changeSetStates = Collections.synchronizedMap(new HashMap<ObjRef, ChangeSetState>());

	public XArchChangeSetImpl(XArchFlatInterface xarch, IChangeSetADT csadt, IChangeSetSync cssync, XArchDetachInterface xarchd){
		super(xarch);
		this.csadt = csadt;
		this.cssync = cssync;
		this.xarchd = xarchd;
	}

	ChangeSetState createChangeSetState(ObjRef xArchRef){
		synchronized(changeSetStates){
			ChangeSetState changeSetState = changeSetStates.get(xArchRef);
			if(changeSetState == null){
				changeSetStates.put(xArchRef, changeSetState = new ChangeSetState(xArchRef));
			}
			return changeSetState;
		}
	}

	ChangeSetState getChangeSetState(ObjRef objRef){
		synchronized(changeSetStates){

			if(objRef == null || !xarch.isAttached(objRef)){
				return null;
			}

			ObjRef xArchRef = xarch.getXArch(objRef);

			ChangeSetState changeSetState = createChangeSetState(xArchRef);

			ObjRef archChangeSetsRef = xarch.getElement(changeSetState.changesetsContextRef, "ArchChangeSets", xArchRef);
			if(archChangeSetsRef == null || xarch.hasAncestor(objRef, archChangeSetsRef)){
				return null;
			}

			return changeSetState;
		}
	}

	public void handleXArchFileEvent(XArchFileEvent evt){
		switch(evt.getEventType()){
		case XArchFileEvent.XARCH_CREATED_EVENT: {
			break;
		}

		case XArchFileEvent.XARCH_OPENED_EVENT: {
			final ObjRef xArchRef = evt.getXArchRef();
			final ChangeSetState changeSetState = createChangeSetState(xArchRef);
			synchronized(changeSetState.lock){
				ObjRef changesetsContextRef = xarch.createContext(xArchRef, "changesets");
				final ObjRef archChangeSetsRef = xarch.getElement(changesetsContextRef, "ArchChangeSets", xArchRef);
				if(archChangeSetsRef != null){
					final List<ObjRef> appliedChangeSetRefs = XArchChangeSetUtils.getOrderedAppliedChangeSets(xarch, xArchRef);
					if(appliedChangeSetRefs != null && !appliedChangeSetRefs.isEmpty()){
						final ObjRef diffChangeSetRef = xarch.create(changeSetState.changesetsContextRef, "ChangeSet");
						xarch.set(diffChangeSetRef, "id", UIDGenerator.generateUID("ChangeSet"));
						XArchFlatUtils.setDescription(xarch, diffChangeSetRef, "description", new SimpleDateFormat("'External Modifications' yyyy/MM/dd HH:mm:ss.SSS Z").format(new Date()));
						appliedChangeSetRefs.add(diffChangeSetRef);
						changeSetState.executor.submit(new Runnable(){

							public void run(){
								try{
									setAppliedChangeSetRefs(changeSetState.monitor, changeSetState, xArchRef, appliedChangeSetRefs.toArray(new ObjRef[appliedChangeSetRefs.size()]), diffChangeSetRef, true);
									if(xarch.get(diffChangeSetRef, "XArchElement") == null){
										// nothing was changed, so remove diffChangeSetRef
										appliedChangeSetRefs.remove(diffChangeSetRef);
									}
									else{
										xarch.add(archChangeSetsRef, "changeSet", diffChangeSetRef);
									}
									setAppliedChangeSetRefs(changeSetState.monitor, changeSetState, xArchRef, appliedChangeSetRefs.toArray(new ObjRef[appliedChangeSetRefs.size()]), null, false);
								}
								catch(Throwable t){
									System.err.println("HandleOpenFile: " + xArchRef + " " + appliedChangeSetRefs);
									t.printStackTrace();
								}
							}
						});
					}
				}
			}
			break;
		}

		case XArchFileEvent.XARCH_CLOSED_EVENT:
			changeSetStates.remove(evt.getXArchRef());
			break;
		}
	}

	public void handleXArchFlatEvent(XArchFlatEvent evt){
	}

	List<XArchChangeSetListener> changeSetEventListeners = Collections.synchronizedList(new ArrayList<XArchChangeSetListener>());

	void fireChangeSetEvent(ChangeSetEventType eventType, ObjRef xArchRef, boolean enabled, ObjRef activeChangeSetRef, ObjRef[] appliedChangeSetRefs){
		XArchChangeSetEvent cse = new XArchChangeSetEvent(eventType, xArchRef, enabled, activeChangeSetRef, appliedChangeSetRefs.clone());
		synchronized(changeSetEventListeners){
			for(XArchChangeSetListener listener: changeSetEventListeners){
				listener.handleXArchChangeSetEvent(cse);
			}
		}
	}

	public void addXArchChangeSetListener(XArchChangeSetListener changeSetEventListener){
		changeSetEventListeners.add(changeSetEventListener);
	}

	public void removeXArchChangeSetListener(XArchChangeSetListener changeSetEventListener){
		changeSetEventListeners.remove(changeSetEventListener);
	}

	public void enableChangeSets(final ObjRef xArchRef, IChangeSetSyncMonitor monitor){

		final ChangeSetState changeSetState = createChangeSetState(xArchRef);
		synchronized(changeSetState.lock){
			ObjRef archChangeSetsRef = xarch.getElement(changeSetState.changesetsContextRef, "ArchChangeSets", xArchRef);
			if(archChangeSetsRef == null){
				synchronized(changeSetState.lock){
					archChangeSetsRef = xarch.createElement(changeSetState.changesetsContextRef, "ArchChangeSets");
					xarch.add(xArchRef, "object", archChangeSetsRef);

					// create a default change set
					final ObjRef activeChangeSetRef = xarch.create(changeSetState.changesetsContextRef, "ChangeSet");
					xarch.set(activeChangeSetRef, "id", UIDGenerator.generateUID("ChangeSet"));
					XArchFlatUtils.setDescription(xarch, activeChangeSetRef, "description", "Baseline");
					xarch.add(archChangeSetsRef, "changeSet", activeChangeSetRef);

					fireChangeSetEvent(ChangeSetEventType.UPDATED_ENABLED, changeSetState.xArchRef, getChangeSetsEnabled(changeSetState.xArchRef), changeSetState.activeChangeSetRef, changeSetState.changeSetRefs);

					setAppliedChangeSetRefs(monitor, changeSetState, xArchRef, new ObjRef[]{activeChangeSetRef}, activeChangeSetRef, true);

					if(monitor != null){
						if(monitor.isCanceled()){
							xarch.remove(xArchRef, "object", archChangeSetsRef);
							fireChangeSetEvent(ChangeSetEventType.UPDATED_ENABLED, changeSetState.xArchRef, getChangeSetsEnabled(changeSetState.xArchRef), changeSetState.activeChangeSetRef, changeSetState.changeSetRefs);
							return;
						}
					}

					setActiveChangeSetRef(xArchRef, activeChangeSetRef);
				}
			}
		}
	}

	public boolean getChangeSetsEnabled(ObjRef xArchRef){
		ChangeSetState changeSetState = getChangeSetState(xArchRef);
		return changeSetState != null;
	}

	public void setAppliedChangeSetRefs(final ObjRef xArchRef, final ObjRef[] changeSetRefs, final IChangeSetSyncMonitor monitor){
		final ChangeSetState changeSetState = getChangeSetState(xArchRef);
		if(changeSetState != null){
			synchronized(changeSetState.lock){
				if(!Arrays.equals(changeSetState.changeSetRefs, changeSetRefs)){
					changeSetState.finishExecution(true);
					changeSetState.executor.submit(new Runnable(){

						public void run(){
							try{
								setAppliedChangeSetRefs(changeSetState.monitor, changeSetState, xArchRef, changeSetRefs, null, true);
							}
							catch(Throwable t){
								System.err.println("SetAppliedChangeSetRefs: " + xArchRef + " " + Arrays.asList(changeSetRefs));
								t.printStackTrace();
							}
						}
					});
				}
			}
		}
	}

	private void setAppliedChangeSetRefs(IChangeSetSyncMonitor monitor, final ChangeSetState changeSetState, final ObjRef mXArchRef, final ObjRef[] changeSetRefs, final ObjRef activeChangeSetRef, boolean updateModel){
		ObjRef archChangeSets = xarch.getElement(changeSetState.changesetsContextRef, "archChangeSets", changeSetState.xArchRef);
		if(archChangeSets != null){
			if(DEBUG){
				System.err.println("Setting applied change sets to: ");
				for(ObjRef o: changeSetRefs){
					System.err.println(" - " + XadlUtils.getDescription(xarch, o, "Description", "?"));
				}
			}
			XArchChangeSetUtils.saveOrder(xarch, changeSetState.xArchRef, archChangeSets, "appliedChangeSets", Arrays.asList(changeSetRefs));

			ObjRef[] changeSetDiffs = diff(changeSetRefs, changeSetState.changeSetRefs).toArray(new ObjRef[0]);

			changeSetState.setChangeSetRefs(changeSetRefs);

			fireChangeSetEvent(ChangeSetEventType.UPDATING_APPLIED_CHANGE_SETS, changeSetState.xArchRef, getChangeSetsEnabled(changeSetState.xArchRef), changeSetState.activeChangeSetRef, changeSetState.changeSetRefs);

			cssync.syncElementMany(monitor, changeSetState.xArchRef, mXArchRef, changeSetRefs, changeSetDiffs, activeChangeSetRef, mXArchRef, "Object");

			fireChangeSetEvent(ChangeSetEventType.UPDATED_APPLIED_CHANGE_SETS, changeSetState.xArchRef, getChangeSetsEnabled(changeSetState.xArchRef), changeSetState.activeChangeSetRef, changeSetState.changeSetRefs);
		}
	}

	public ObjRef[] getAppliedChangeSetRefs(ObjRef xArchRef){
		ChangeSetState changeSetState = getChangeSetState(xArchRef);
		return changeSetState == null ? null : changeSetState.changeSetRefs.clone();
	}

	public void setActiveChangeSetRef(final ObjRef xArchRef, final ObjRef activeChangeSetRef){
		final ChangeSetState changeSetState = getChangeSetState(xArchRef);
		if(changeSetState != null){
			synchronized(changeSetState.lock){
				if(!equalz(changeSetState.activeChangeSetRef, activeChangeSetRef)){
					changeSetState.setActiveChangeSetRef(activeChangeSetRef);
					fireChangeSetEvent(ChangeSetEventType.UPDATED_ACTIVE_CHANGE_SET, changeSetState.xArchRef, getChangeSetsEnabled(changeSetState.xArchRef), changeSetState.activeChangeSetRef, changeSetState.changeSetRefs);
				}
			}
		}
	}

	public ObjRef getActiveChangeSetRef(ObjRef xArchRef){
		ChangeSetState changeSetState = getChangeSetState(xArchRef);
		return changeSetState == null ? null : changeSetState.activeChangeSetRef;
	}

	@Override
	public void set(final ObjRef baseObjectRef, final String typeOfThing, final String value){
		final ChangeSetState changeSetState = getChangeSetState(baseObjectRef);
		if(changeSetState == null){
			super.set(baseObjectRef, typeOfThing, value);
		}
		else if(!xarch.has(baseObjectRef, typeOfThing, value)){
			synchronized(changeSetState.lock){
				final IChangeReference preReference = csadt.getElementReference(changeSetState.xArchRef, baseObjectRef, true);
				super.set(baseObjectRef, typeOfThing, value);
				final ObjRef activeChangeSetRef = changeSetState.activeChangeSetApplied ? changeSetState.activeChangeSetRef : null;
				changeSetState.executor.submit(new Runnable(){

					public void run(){
						try{
							if(xarch.isAttached(baseObjectRef)){
								cssync.syncAttribute(null, changeSetState.xArchRef, changeSetState.xArchRef, changeSetState.changeSetRefs, null, activeChangeSetRef, preReference, baseObjectRef, capFirstLetter(typeOfThing));
							}
						}
						catch(Throwable t){
							System.err.println("Set: " + baseObjectRef + " " + typeOfThing + " " + value);
							t.printStackTrace();
						}
					}
				});
			}
		}
	}

	@Override
	public void set(final ObjRef baseObjectRef, final String typeOfThing, final ObjRef value){
		final ChangeSetState changeSetState = getChangeSetState(baseObjectRef);
		if(changeSetState == null){
			super.set(baseObjectRef, typeOfThing, value);
		}
		else{ // doesn't work with xarchd : if(!xarch.has(baseObjectRef, typeOfThing, value)){
			synchronized(changeSetState.lock){
				final IChangeReference preReference = csadt.getElementReference(changeSetState.xArchRef, baseObjectRef, true);
				xarchd.set(baseObjectRef, typeOfThing, value);
				final ObjRef activeChangeSetRef = changeSetState.activeChangeSetApplied ? changeSetState.activeChangeSetRef : null;
				changeSetState.executor.submit(new Runnable(){

					public void run(){
						try{
							cssync.syncElement(null, changeSetState.xArchRef, changeSetState.xArchRef, changeSetState.changeSetRefs, null, activeChangeSetRef, preReference, baseObjectRef, capFirstLetter(typeOfThing));
						}
						catch(Throwable t){
							System.err.println("Set: " + baseObjectRef + " " + typeOfThing + " " + value);
							t.printStackTrace();
						}
					}
				});
			}
		}
	}

	@Override
	public void add(final ObjRef baseObjectRef, final String typeOfThing, final ObjRef thingToAddRef){
		final ChangeSetState changeSetState = getChangeSetState(baseObjectRef);
		if(changeSetState == null){
			super.add(baseObjectRef, typeOfThing, thingToAddRef);
		}
		else{
			synchronized(changeSetState.lock){
				xarchd.add(baseObjectRef, typeOfThing, thingToAddRef);
				final ObjRef activeChangeSetRef = changeSetState.activeChangeSetApplied ? changeSetState.activeChangeSetRef : null;
				changeSetState.executor.submit(new Runnable(){

					public void run(){
						try{
							cssync.syncElementMany(null, changeSetState.xArchRef, changeSetState.xArchRef, changeSetState.changeSetRefs, null, activeChangeSetRef, baseObjectRef, capFirstLetter(typeOfThing), thingToAddRef);
						}
						catch(Throwable t){
							System.err.println("Add: " + baseObjectRef + " " + typeOfThing + " " + thingToAddRef);
							t.printStackTrace();
						}
					}
				});
			}
		}
	}

	@Override
	public void add(final ObjRef baseObjectRef, final String typeOfThing, final ObjRef[] thingsToAddRefs){
		final ChangeSetState changeSetState = getChangeSetState(baseObjectRef);
		if(changeSetState == null){
			super.add(baseObjectRef, typeOfThing, thingsToAddRefs);
		}
		else{
			synchronized(changeSetState.lock){
				xarchd.add(baseObjectRef, typeOfThing, thingsToAddRefs);
				final ObjRef activeChangeSetRef = changeSetState.activeChangeSetApplied ? changeSetState.activeChangeSetRef : null;
				changeSetState.executor.submit(new Runnable(){

					public void run(){
						try{
							String typeOfThing2 = capFirstLetter(typeOfThing);
							for(ObjRef thingToAddRef: thingsToAddRefs){
								cssync.syncElementMany(null, changeSetState.xArchRef, changeSetState.xArchRef, changeSetState.changeSetRefs, null, activeChangeSetRef, baseObjectRef, typeOfThing2, thingToAddRef);
							}
						}
						catch(Throwable t){
							System.err.println("Add: " + baseObjectRef + " " + typeOfThing + " " + Arrays.asList(thingsToAddRefs));
							t.printStackTrace();
						}
					}
				});
			}
		}
	}

	@Override
	public void remove(final ObjRef baseObjectRef, final String typeOfThing, final ObjRef thingToRemoveRef){
		final ChangeSetState changeSetState = getChangeSetState(baseObjectRef);
		if(changeSetState == null){
			super.remove(baseObjectRef, typeOfThing, thingToRemoveRef);
		}
		else{
			synchronized(changeSetState.lock){
				xarchd.detach(thingToRemoveRef, null);
				final ObjRef activeChangeSetRef = changeSetState.activeChangeSetApplied ? changeSetState.activeChangeSetRef : null;
				changeSetState.executor.submit(new Runnable(){

					public void run(){
						try{
							cssync.syncElementMany(null, changeSetState.xArchRef, changeSetState.xArchRef, changeSetState.changeSetRefs, null, activeChangeSetRef, baseObjectRef, capFirstLetter(typeOfThing), thingToRemoveRef);
						}
						catch(Throwable t){
							System.err.println("Remove: " + baseObjectRef + " " + typeOfThing + " " + thingToRemoveRef);
							t.printStackTrace();
						}
					}
				});
			}
		}
	}

	@Override
	public void remove(final ObjRef baseObjectRef, final String typeOfThing, final ObjRef[] thingsToRemoveRefs){
		final ChangeSetState changeSetState = getChangeSetState(baseObjectRef);
		if(changeSetState == null){
			super.remove(baseObjectRef, typeOfThing, thingsToRemoveRefs);
		}
		else{
			synchronized(changeSetState.lock){
				xarchd.detach(thingsToRemoveRefs, null);
				final ObjRef activeChangeSetRef = changeSetState.activeChangeSetApplied ? changeSetState.activeChangeSetRef : null;
				changeSetState.executor.submit(new Runnable(){

					public void run(){
						try{
							String typeOfThing2 = capFirstLetter(typeOfThing);
							for(ObjRef thingToRemoveRef: thingsToRemoveRefs){
								cssync.syncElementMany(null, changeSetState.xArchRef, changeSetState.xArchRef, changeSetState.changeSetRefs, null, activeChangeSetRef, baseObjectRef, typeOfThing2, thingToRemoveRef);
							}
						}
						catch(Throwable t){
							System.err.println("Remove: " + baseObjectRef + " " + typeOfThing + " " + Arrays.asList(thingsToRemoveRefs));
							t.printStackTrace();
						}
					}
				});
			}
		}
	}

	@Override
	public void clear(final ObjRef baseObjectRef, final String typeOfThing){
		final ChangeSetState changeSetState = getChangeSetState(baseObjectRef);
		if(changeSetState == null){
			super.clear(baseObjectRef, typeOfThing);
		}
		else if(xarch.get(baseObjectRef, typeOfThing) != null){
			synchronized(changeSetState.lock){
				final IChangeReference preReference;
				switch(xarch.getTypeMetadata(baseObjectRef).getProperty(typeOfThing).getMetadataType()){
				case IXArchPropertyMetadata.ATTRIBUTE:
					preReference = csadt.getElementReference(changeSetState.xArchRef, baseObjectRef, true);
					super.clear(baseObjectRef, typeOfThing);
					break;
				case IXArchPropertyMetadata.ELEMENT:
					preReference = csadt.getElementReference(changeSetState.xArchRef, baseObjectRef, true);
					ObjRef valueRef = (ObjRef)xarch.get(baseObjectRef, typeOfThing);
					if(valueRef != null){
						xarchd.detach(valueRef, null);
					}
					break;
				case IXArchPropertyMetadata.ELEMENT_MANY:
				default:
					throw new IllegalArgumentException(); // this shouldn't happen
				}
				final ObjRef activeChangeSetRef = changeSetState.activeChangeSetApplied ? changeSetState.activeChangeSetRef : null;
				changeSetState.executor.submit(new Runnable(){

					public void run(){
						try{
							switch(xarch.getTypeMetadata(baseObjectRef).getProperty(typeOfThing).getMetadataType()){
							case IXArchPropertyMetadata.ATTRIBUTE:
								cssync.syncAttribute(null, changeSetState.xArchRef, changeSetState.xArchRef, changeSetState.changeSetRefs, null, activeChangeSetRef, preReference, baseObjectRef, capFirstLetter(typeOfThing));
								break;
							case IXArchPropertyMetadata.ELEMENT:
								cssync.syncElement(null, changeSetState.xArchRef, changeSetState.xArchRef, changeSetState.changeSetRefs, null, activeChangeSetRef, preReference, baseObjectRef, capFirstLetter(typeOfThing));
								break;
							case IXArchPropertyMetadata.ELEMENT_MANY:
								throw new IllegalArgumentException(); // this shouldn't happen
							}
						}
						catch(Throwable t){
							System.err.println("Clear: " + baseObjectRef + " " + typeOfThing);
							t.printStackTrace();
						}
					}
				});
			}
		}
	}

	@Override
	public ObjRef promoteTo(final ObjRef contextObjectRef, final String promotionTarget, final ObjRef targetObjectRef){
		ObjRef result;
		final ChangeSetState changeSetState = getChangeSetState(targetObjectRef);
		if(changeSetState == null){
			result = super.promoteTo(contextObjectRef, promotionTarget, targetObjectRef);
		}
		else{
			synchronized(changeSetState.lock){
				final IChangeReference preReference = csadt.getElementReference(changeSetState.xArchRef, targetObjectRef, true);
				result = super.promoteTo(contextObjectRef, promotionTarget, targetObjectRef);
				final ObjRef activeChangeSetRef = changeSetState.activeChangeSetApplied ? changeSetState.activeChangeSetRef : null;
				changeSetState.executor.submit(new Runnable(){

					public void run(){
						try{
							ObjRef parentObjRef = xarch.getParent(targetObjectRef);
							String typeOfThing = xarch.getElementName(targetObjectRef);
							switch(xarch.getTypeMetadata(parentObjRef).getProperty(typeOfThing).getMetadataType()){
							case IXArchPropertyMetadata.ATTRIBUTE:
								throw new IllegalArgumentException(); // this shouldn't happen
							case IXArchPropertyMetadata.ELEMENT:
								cssync.syncElement(null, changeSetState.xArchRef, changeSetState.xArchRef, changeSetState.changeSetRefs, null, activeChangeSetRef, preReference, parentObjRef, capFirstLetter(typeOfThing));
								break;
							case IXArchPropertyMetadata.ELEMENT_MANY:
								cssync.syncElementMany(null, changeSetState.xArchRef, changeSetState.xArchRef, changeSetState.changeSetRefs, null, activeChangeSetRef, parentObjRef, capFirstLetter(typeOfThing), targetObjectRef);
								break;
							}
						}
						catch(Throwable t){
							System.err.println("PromoteTo: " + contextObjectRef + " " + promotionTarget + " " + targetObjectRef);
							t.printStackTrace();
						}
					}
				});
			}
		}

		return result;
	}

	@Override
	public String serialize(ObjRef xArchRef){
		final ChangeSetState changeSetState = getChangeSetState(xArchRef);
		if(changeSetState == null){
			return super.serialize(xArchRef);
		}
		else{
			synchronized(changeSetState.lock){
				changeSetState.finishExecution(false);
				return xarchd.serialize(xArchRef);
			}
		}
	}

	@Override
	public void writeToFile(ObjRef xArchRef, String fileName) throws IOException{
		final ChangeSetState changeSetState = getChangeSetState(xArchRef);
		if(changeSetState == null){
			super.writeToFile(xArchRef, fileName);
		}
		else{
			synchronized(changeSetState.lock){
				changeSetState.finishExecution(false);
				xarchd.writeToFile(xArchRef, fileName);
			}
		}
	}

	public ChangeStatus getChangeStatus(ObjRef objRef, ObjRef[] changeSetRefs){
		ChangeStatus changeStatus = cssync.getChangeStatus(xarch.getXArch(objRef), objRef, changeSetRefs, true);
		if(changeStatus == ChangeStatus.UNMODIFIED){
			if(!xarchd.isAttached(objRef)){
				changeStatus = ChangeStatus.DETACHED;
			}
		}
		return changeStatus;
	}

	public void diffToExternalFile(ObjRef xArchRef, ObjRef toXArchRef){
		try{
			final ChangeSetState changeSetState = getChangeSetState(xArchRef);
			if(changeSetState == null){
				throw new IllegalArgumentException("xADL file must have change sets enabled");
			}
			else{
				synchronized(changeSetState.lock){
					ObjRef archChangeSetsRef = xarch.getElement(changeSetState.changesetsContextRef, "ArchChangeSets", xArchRef);
					if(archChangeSetsRef == null){
						throw new IllegalArgumentException("xADL file must have change sets enabled");
					}

					// create a diff change set
					final ObjRef diffChangeSetRef = xarch.create(changeSetState.changesetsContextRef, "ChangeSet");
					xarch.set(diffChangeSetRef, "id", UIDGenerator.generateUID("ChangeSet"));
					XArchFlatUtils.setDescription(xarch, diffChangeSetRef, "description", "Diff to " + xarch.getXArchURI(toXArchRef));
					xarch.add(archChangeSetsRef, "changeSet", diffChangeSetRef);

					List<ObjRef> appliedChangeSets = new ArrayList<ObjRef>(Arrays.asList(changeSetState.changeSetRefs));
					appliedChangeSets.add(0, diffChangeSetRef);

					changeSetState.setActiveChangeSetRef(diffChangeSetRef);

					cssync.syncElementMany(null, changeSetState.xArchRef, toXArchRef, appliedChangeSets.toArray(new ObjRef[appliedChangeSets.size()]), null, diffChangeSetRef, toXArchRef, "Object");
					//setAppliedChangeSetRefs(null, changeSetState, toXArchRef, appliedChangeSets.toArray(new ObjRef[appliedChangeSets.size()]), diffChangeSetRef, true);
				}
			}
		}
		catch(Throwable t){
			t.printStackTrace();
		}
	}

	public void diffFromExternalFile(ObjRef sourceXArchRef, ObjRef targetXArchRef){
		throw new UnsupportedOperationException();
	}
	
	public void moveChange(ObjRef itemRef,ObjRef[] contextChangeSetRefs,ObjRef targetChangeSetRef) {
		ObjRef xArchRef = xarch.getXArch(itemRef);
		cssync.moveChanges(xArchRef,contextChangeSetRefs, targetChangeSetRef, itemRef);
	}
}
