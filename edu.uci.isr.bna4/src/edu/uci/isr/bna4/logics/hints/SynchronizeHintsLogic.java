package edu.uci.isr.bna4.logics.hints;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.regex.Pattern;

import org.eclipse.swt.events.MouseEvent;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAModelEvent;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNAModelListener;
import edu.uci.isr.bna4.IBNAMouseListener;
import edu.uci.isr.bna4.IBNASynchronousModelListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IBNAWorld;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.IThingLogicManagerListener;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.ThingLogicManagerEvent;
import edu.uci.isr.bna4.assemblies.AssemblyUtils;
import edu.uci.isr.bna4.assemblies.IAssembly;
import edu.uci.isr.bna4.facets.IHasMutableAnchorPoint;
import edu.uci.isr.bna4.facets.IHasMutableAngle;
import edu.uci.isr.bna4.facets.IHasMutableBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutableColor;
import edu.uci.isr.bna4.facets.IHasMutableEndpoints;
import edu.uci.isr.bna4.facets.IHasMutableMidpoints;
import edu.uci.isr.bna4.facets.IRelativeMovable;
import edu.uci.isr.bna4.logics.coordinating.MaintainTagsLogic;
import edu.uci.isr.bna4.logics.hints.synchronizers.BooleanHintSynchronizer;
import edu.uci.isr.bna4.logics.hints.synchronizers.PointHintSynchronizer;
import edu.uci.isr.bna4.logics.hints.synchronizers.PropertyHintSynchronizer;
import edu.uci.isr.sysutils.HashBag;
import edu.uci.isr.sysutils.ListenerList;
import edu.uci.isr.sysutils.Tuple;

public class SynchronizeHintsLogic
    extends AbstractThingLogic
    implements IBNASynchronousModelListener, IBNAModelListener, IHintRepositoryChangeListener, IBNAMouseListener, IThingLogicManagerListener{

	private static final boolean DEBUG = false;

	private static final Pattern PATH_SPLIT_PATTERN = Pattern.compile("\\/");

	private static final String HINT_INFORMATION_PROPERTY_NAME = SynchronizeHintsLogic.class.getName() + ":hintInformation";
	private static final String BEGIN_IGNORING_CHANGES = SynchronizeHintsLogic.class.getName() + ":beginIgnoringChanges";
	private static final String END_IGNORING_CHANGES = SynchronizeHintsLogic.class.getName() + ":endIgnoringChanges";

	public static class HintInformation{

		final public Object hintContext;
		final String hintPartPath;
		final String[] hintParts;

		public HintInformation(Object hintContext, String hintPartPath){
			assert hintContext != null;

			this.hintContext = hintContext;
			this.hintPartPath = hintPartPath;
			this.hintParts = PATH_SPLIT_PATTERN.split(hintPartPath);
		}
	}

	private final IHintRepository hintRepository;
	private final Map<String, HintInformation> hintInfoMap = new HashMap<String, HintInformation>();

	volatile int holdingChanges = 0;
	boolean holdingMouseDown = false;
	final Map<Tuple, Object[]> heldChanges = new HashMap<Tuple, Object[]>();

	private void bumpHoldingEvents(int i){
		synchronized(heldChanges){
			holdingChanges += i;
			if(holdingChanges <= 0){
				holdingChanges = 0;
				for(Map.Entry<Tuple, Object[]> entry: cloneclear(heldChanges).entrySet()){
					handleThingChanged((IThing)entry.getKey().getElement(0), entry.getKey().getElement(1), entry.getValue()[0], entry.getValue()[1]);
				}
			}
		}
	}

	private <K, V>Map<K, V> cloneclear(Map<K, V> map){
		Map<K, V> c = new HashMap<K, V>(map);
		map.clear();
		return c;
	}

	volatile int ignoringChanges = 0;
	// TODO: Replace this with the ability to mark the event in some way?
	final Collection<Tuple> ignoringThingProperties = new HashBag<Tuple>();

	private void bumpIgnoreChanges(int i){
		synchronized(ignoringThingProperties){
			ignoringChanges += i;
			if(ignoringChanges <= 0){
				ignoringChanges = 0;
			}
		}
	}

	public void ignoreBNAChanges(final Runnable r){
		final IBNAModel model = getBNAModel();
		Lock lock = model.getLock();
		lock.lock();
		try{
			model.fireStreamNotificationEvent(BEGIN_IGNORING_CHANGES);
			try{
				r.run();
			}
			finally{
				model.fireStreamNotificationEvent(END_IGNORING_CHANGES);
			}
		}
		finally{
			lock.unlock();
		}
	}

	public SynchronizeHintsLogic(IHintRepository hintRepository){
		this.hintRepository = hintRepository;
		addHintSynchronizer(new PropertyHintSynchronizer(IHasMutableColor.class, IHasMutableColor.COLOR_PROPERTY_NAME, IHasMutableColor.USER_MAY_EDIT_COLOR).addOldHintName("boxColor", "background"));
		addHintSynchronizer(new PropertyHintSynchronizer(IHasMutableBoundingBox.class, IHasMutableBoundingBox.BOUNDING_BOX_PROPERTY_NAME, IHasMutableBoundingBox.USER_MAY_MOVE).addOldHintName("glassBoundingBox", "glass"));
		addHintSynchronizer(new PropertyHintSynchronizer(IHasMutableAngle.class, IHasMutableAngle.ANGLE_PROPERTY_NAME, IHasMutableAngle.USER_MAY_CHANGE_ANGLE));
		addHintSynchronizer(new PropertyHintSynchronizer(IHasMutableMidpoints.class, IHasMutableMidpoints.MIDPOINTS_PROPERTY_NAME, IHasMutableMidpoints.USER_MAY_MOVE_MIDPOINTS));
		addHintSynchronizer(new BooleanHintSynchronizer(IThing.class, MaintainTagsLogic.SHOW_TAG_PROPERTY_NAME, MaintainTagsLogic.USER_MAY_SHOW_TAG));
		addHintSynchronizer(new PointHintSynchronizer(IHasMutableEndpoints.class, IHasMutableEndpoints.ENDPOINT_1_PROPERTY_NAME, IHasMutableEndpoints.USER_MAY_MOVE_ENDPOINT1).addOldStuckHintName("endpoint1FractionOffset", "glass").addOldHintName("endpoint1", "glass"));
		addHintSynchronizer(new PointHintSynchronizer(IHasMutableEndpoints.class, IHasMutableEndpoints.ENDPOINT_2_PROPERTY_NAME, IHasMutableEndpoints.USER_MAY_MOVE_ENDPOINT2).addOldStuckHintName("endpoint2FractionOffset", "glass").addOldHintName("endpoint2", "glass"));
		addHintSynchronizer(new PointHintSynchronizer(IHasMutableAnchorPoint.class, IHasMutableAnchorPoint.ANCHOR_POINT_PROPERTY_NAME, IRelativeMovable.USER_MAY_MOVE).addOldStuckHintName("glassAnchorFractionOffset", "glass").addOldHintName("glassAnchorPoint", "glass"));
	}

	final protected ListenerList<IHintSynchronizer> hintSynchronizers = new ListenerList<IHintSynchronizer>(IHintSynchronizer.class);

	public void addHintSynchronizer(IHintSynchronizer hintSynchronizer){
		if(bnaWorld != null){
			throw new IllegalStateException("Updates should only happen before logic initialization.");
		}
		hintSynchronizers.add(hintSynchronizer);
	}

	public void removeHintSynchronizer(IHintSynchronizer hintSynchronizer){
		if(bnaWorld != null){
			throw new IllegalStateException("Updates should only happen before logic initialization.");
		}
		hintSynchronizers.remove(hintSynchronizer);
	}

	protected void fireRestoreHints(IHintRepository repository, Object context, String partPath, String[] parts, IThing thing){
		if(DEBUG){
			System.out.println("Restore hints: " + context + " " + thing.getID());
		}
		for(IHintSynchronizer hintSynchronizer: hintSynchronizers.getListeners()){
			hintSynchronizer.restoreHints(repository, context, partPath, parts, thing);
		}
	}

	protected void fireRepositoryChanged(IHintRepository repository, Object context, IAssembly[] assemblies, String hintName){
		if(DEBUG){
			System.out.println("Repository changed: " + context + " " + hintName + " " + assemblies.length);
		}
		for(IHintSynchronizer hintSynchronizer: hintSynchronizers.getListeners()){
			hintSynchronizer.repositoryChanged(repository, context, assemblies, hintName);
		}
	}

	protected void fireThingChanged(IHintRepository repository, Object context, String partPath, String[] parts, IThing thing, Object propertyName, Object oldValue, Object newValue){
		if(DEBUG){
			System.out.println("Thing changed: " + context + " " + thing.getID() + " " + propertyName + " from " + oldValue + " to " + newValue);
		}
		for(IHintSynchronizer hintSynchronizer: hintSynchronizers.getListeners()){
			hintSynchronizer.thingChanged(repository, context, partPath, parts, thing, propertyName, oldValue, newValue);
		}
	}

	@Override
	public void init(){
		super.init();
		bnaWorld.getThingLogicManager().addThingLogicManagerListener(this);
		for(IHintSynchronizer hintSynchronizer: hintSynchronizers.getListeners()){
			hintSynchronizer.setBNAWorld(bnaWorld);
		}
		hintRepository.addHintRepositoryChangeListener(this);
	}

	public void handleThingLogicManagerEvent(ThingLogicManagerEvent evt){
		switch(evt.getEventType()){
		case LOGIC_ADDED:
			if(evt.getLogic() == this){
				/*
				 * Synchronize all things that are currently part of the model.
				 * Note: During the init() call we still do not get model
				 * events. So, we have to delay the restoration until here.
				 */
				IBNAModel model = getBNAModel();
				model.beginBulkChange();
				try{
					for(IThing t: bnaWorld.getBNAModel().getAllThings()){
						initializeHints(t);
					}
				}
				finally{
					model.endBulkChange();
				}
			}
		}
	}

	@Override
	public void destroy(){
		hintInfoMap.clear();
		bnaWorld.getThingLogicManager().removeThingLogicManagerListener(this);
		hintRepository.removeHintRepositoryChangeListener(this);
		for(IHintSynchronizer hintSynchronizer: hintSynchronizers.getListeners()){
			hintSynchronizer.setBNAWorld(null);
		}
		super.destroy();
	}

	private void initializeHints(IThing thing){
		if(!hintInfoMap.containsKey(thing.getID())){
			IAssembly partAssembly = AssemblyUtils.getAssemblyWithPart(thing);
			IAssembly rootAssembly = AssemblyUtils.getAssemblyWithRoot(thing);

			Object hintContext = null;
			String hintPartPath = "";

			if(rootAssembly != null){
				hintContext = hintRepository.getContextForAssembly(bnaWorld, rootAssembly);
			}
			else if(partAssembly != null){
				HintInformation parentHintInformation = hintInfoMap.get(partAssembly.getRootThing().getID());
				if(parentHintInformation != null){
					hintContext = parentHintInformation.hintContext;
					if(parentHintInformation.hintPartPath.length() > 0){
						hintPartPath = parentHintInformation.hintPartPath + "/" + AssemblyUtils.getPartName(thing);
					}
					else{
						hintPartPath = AssemblyUtils.getPartName(thing);
					}
				}
			}

			if(hintContext != null){
				if(DEBUG){
					System.out.println("Initializing hints: " + thing);
				}

				HintInformation hintInformation = new HintInformation(hintContext, hintPartPath);
				thing.setProperty(HINT_INFORMATION_PROPERTY_NAME, null); // now it has the property, so don't call initialize again
				restoreHints(thing, hintInformation);
				thing.setProperty(HINT_INFORMATION_PROPERTY_NAME, hintInformation);

				if(rootAssembly != null){
					for(IThing partThing: rootAssembly.getParts()){
						initializeHints(partThing);
					}
				}
			}
		}
	}

	private void restoreHints(IThing thing, HintInformation hintInformation){
		if(hintInformation != null){
			fireRestoreHints(hintRepository, hintInformation.hintContext, hintInformation.hintPartPath, hintInformation.hintParts, thing);
		}
	}

	private void restoreHints(IAssembly assembly, Object context, HintInformation hintInformation){
		if(hintInformation != null && context.equals(hintInformation.hintContext)){
			restoreHints(assembly.getRootThing(), hintInformation);
			for(IThing thing: assembly.getParts()){
				if(thing != null){
					IAssembly partRootAssembly = AssemblyUtils.getAssemblyWithRoot(thing);
					HintInformation partHintInformation = hintInfoMap.get(thing.getID());
					if(partRootAssembly == null){
						restoreHints(thing, partHintInformation);
					}
					else{
						restoreHints(partRootAssembly, context, partHintInformation);
					}
				}
			}
		}
	}

	public void mouseClick(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
	}

	public void mouseDoubleClick(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
	}

	public void mouseDown(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
		if(!holdingMouseDown){
			holdingMouseDown = true;
			bumpHoldingEvents(1);
		}
	}

	public void mouseUp(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
		if(holdingMouseDown){
			holdingMouseDown = false;
			bumpHoldingEvents(-1);
		}
	}

	public void bnaModelChangedSync(final BNAModelEvent evt){
		switch(evt.getEventType()){
		case STREAM_NOTIFICATION_EVENT:
			if(BEGIN_IGNORING_CHANGES.equals(evt.getStreamNotification())){
				bumpIgnoreChanges(1);
			}
			else if(END_IGNORING_CHANGES.equals(evt.getStreamNotification())){
				bumpIgnoreChanges(-1);
			}
			break;

		case BULK_CHANGE_BEGIN:
			bumpHoldingEvents(1);
			break;

		case BULK_CHANGE_END:
			bumpHoldingEvents(-1);
			break;

		case THING_ADDED: {
			IThing thing = evt.getTargetThing();
			hintInfoMap.remove(thing.getID());
			initializeHints(thing);
		}
			break;

		case THING_REMOVED: {
			IThing thing = evt.getTargetThing();
			hintInfoMap.remove(thing.getID());
		}
			break;

		case THING_CHANGED:
			if(DEBUG){
				System.out.println("BNA Event: " + evt.getThingEvent());
			}

			IThing thing = evt.getTargetThing();
			ThingEvent thingEvent = evt.getThingEvent();
			Object propertyName = thingEvent.getPropertyName();

			if(!thing.hasProperty(HINT_INFORMATION_PROPERTY_NAME)){
				initializeHints(thing);
			}
			if(!HINT_INFORMATION_PROPERTY_NAME.equals(propertyName)){
				boolean ignore = ignoringChanges > 0 || hintInfoMap.get(thing.getID()) == null;
				if(ignore || holdingChanges > 0){
					ignoringThingProperties.add(new Tuple(thing.getID(), propertyName));
				}
				if(!ignore && holdingChanges > 0){
					synchronized(heldChanges){
						Tuple key = new Tuple(thing, propertyName);
						Object[] oldNewValue = heldChanges.get(key);
						if(oldNewValue == null){
							heldChanges.put(key, oldNewValue = new Object[2]);
							oldNewValue[0] = thingEvent.getOldPropertyValue();
						}
						oldNewValue[1] = thingEvent.getNewPropertyValue();
						break;
					}
				}
			}
			else{
				hintInfoMap.put(thing.getID(), (HintInformation)thingEvent.getNewPropertyValue());
			}
		}
	}

	public void bnaModelChanged(BNAModelEvent evt){
		switch(evt.getEventType()){

		case THING_CHANGED:
			IThing thing = evt.getTargetThing();
			ThingEvent thingEvent = evt.getThingEvent();
			Object propertyName = thingEvent.getPropertyName();

			if(!ignoringThingProperties.isEmpty() && ignoringThingProperties.remove(new Tuple(thing.getID(), propertyName))){
				if(DEBUG){
					System.out.println("Ignored: " + thing.getID() + " " + propertyName + " (" + ignoringThingProperties.size() + ") " + thingEvent);
				}
				// do nothing
				break;
			}
			else{
				handleThingChanged(thing, propertyName, thingEvent.getOldPropertyValue(), thingEvent.getNewPropertyValue());
			}
		}
	}

	private void handleThingChanged(final IThing thing, final Object propertyName, final Object oldPropertyValue, final Object newPropertyValue){
		HintInformation hintInformation = hintInfoMap.get(thing.getID());
		if(hintInformation != null){
			fireThingChanged(hintRepository, hintInformation.hintContext, hintInformation.hintPartPath, hintInformation.hintParts, thing, propertyName, oldPropertyValue, newPropertyValue);
		}
	}

	public void hintRepositoryChanged(final IHintRepository repository, final Object context, final String hintName){
		ignoreBNAChanges(new Runnable(){

			public void run(){
				IBNAWorld world = getBNAWorld();
				if(world != null){
					IAssembly[] assemblies = repository.getAssembliesForContext(world, context);
					if(hintName != null){
						fireRepositoryChanged(repository, context, assemblies, hintName);
					}
					else{
						for(IAssembly assembly: assemblies){
							restoreHints(assembly, context, hintInfoMap.get(assembly.getRootThing().getID()));
						}
					}
				}
			}
		});
	}

}