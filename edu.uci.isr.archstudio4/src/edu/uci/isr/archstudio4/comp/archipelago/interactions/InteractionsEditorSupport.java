package edu.uci.isr.archstudio4.comp.archipelago.interactions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoConstants;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoMyxComponent;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoServices;
import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoUtils;
import edu.uci.isr.archstudio4.comp.archipelago.generic.logics.coordinating.MaintainXadlLinksLogic;
import edu.uci.isr.archstudio4.comp.archipelago.generic.logics.editing.XadlCopyPasteLogic;
import edu.uci.isr.archstudio4.comp.archipelago.generic.logics.editing.XadlRemoveElementLogic;
import edu.uci.isr.archstudio4.comp.archipelago.generic.logics.editing.XadlSelectionProviderLogic;
import edu.uci.isr.archstudio4.comp.archipelago.generic.logics.hints.XAdlHintRepository;
import edu.uci.isr.archstudio4.comp.archipelago.interactions.logics.editing.InteractionsNewElementLogic;
import edu.uci.isr.archstudio4.comp.archipelago.interactions.logics.editing.InteractionsSelectComponentLogic;
import edu.uci.isr.archstudio4.comp.archipelago.interactions.logics.mapping.MapXadlLifelineLogic;
import edu.uci.isr.archstudio4.comp.archipelago.interactions.logics.mapping.MapXadlMessageLogic;
import edu.uci.isr.archstudio4.comp.archipelago.interactions.logics.mapping.MapXadlNoteLogic;
import edu.uci.isr.archstudio4.comp.archipelago.util.ArchipelagoFinder;
import edu.uci.isr.archstudio4.comp.xarchcs.logics.AnnotateExplicitChangeLogic;
import edu.uci.isr.archstudio4.comp.xmapper.MapToCodeLogic;
import edu.uci.isr.bna4.BNAComposite;
import edu.uci.isr.bna4.BNARenderingSettings;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.DefaultBNAModel;
import edu.uci.isr.bna4.DefaultBNAView;
import edu.uci.isr.bna4.DefaultBNAWorld;
import edu.uci.isr.bna4.DefaultCoordinateMapper;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IBNAWorld;
import edu.uci.isr.bna4.IMutableCoordinateMapper;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.IThingLogicManager;
import edu.uci.isr.bna4.logics.background.LifeSapperLogic;
import edu.uci.isr.bna4.logics.background.RotatingOffsetLogic;
import edu.uci.isr.bna4.logics.coordinating.MaintainAnchoredAssemblyOrientationLogic;
import edu.uci.isr.bna4.logics.coordinating.MaintainGridLayoutLogic;
import edu.uci.isr.bna4.logics.coordinating.MaintainStickyPointLogic;
import edu.uci.isr.bna4.logics.coordinating.MirrorAnchorPointLogic;
import edu.uci.isr.bna4.logics.coordinating.MirrorBoundingBoxLogic;
import edu.uci.isr.bna4.logics.coordinating.MirrorEndpointLogic;
import edu.uci.isr.bna4.logics.coordinating.MirrorValueLogic;
import edu.uci.isr.bna4.logics.coordinating.MoveWithLogic;
import edu.uci.isr.bna4.logics.editing.AlignAndDistributeLogic;
import edu.uci.isr.bna4.logics.editing.BoxReshapeHandleLogic;
import edu.uci.isr.bna4.logics.editing.ClickSelectionLogic;
import edu.uci.isr.bna4.logics.editing.DragMovableLogic;
import edu.uci.isr.bna4.logics.editing.EditTextLogic;
import edu.uci.isr.bna4.logics.editing.KeyNudgerLogic;
import edu.uci.isr.bna4.logics.editing.MarqueeSelectionLogic;
import edu.uci.isr.bna4.logics.editing.RectifyToGridLogic;
import edu.uci.isr.bna4.logics.editing.RotaterLogic;
import edu.uci.isr.bna4.logics.editing.SnapToGridLogic;
import edu.uci.isr.bna4.logics.editing.SplineBreakLogic;
import edu.uci.isr.bna4.logics.editing.SplineReshapeHandleLogic;
import edu.uci.isr.bna4.logics.editing.StandardCursorLogic;
import edu.uci.isr.bna4.logics.events.DragMoveEventsLogic;
import edu.uci.isr.bna4.logics.events.WorldThingExternalEventsLogic;
import edu.uci.isr.bna4.logics.hints.SynchronizeHintsLogic;
import edu.uci.isr.bna4.logics.information.ToolTipLogic;
import edu.uci.isr.bna4.logics.navigating.FindDialogLogic;
import edu.uci.isr.bna4.logics.navigating.MousePanningLogic;
import edu.uci.isr.bna4.logics.navigating.MouseWheelZoomingLogic;
import edu.uci.isr.bna4.logics.tracking.ModelBoundsTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.ReferenceTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.SelectionTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.ThingPropertyPrefixTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.ThingPropertyTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.TypedThingTrackingLogic;
import edu.uci.isr.bna4.logics.util.ExportBitmapLogic;
import edu.uci.isr.bna4.things.utility.EnvironmentPropertiesThing;
import edu.uci.isr.bna4.things.utility.NoThing;
import edu.uci.isr.myx.fw.IMyxBrick;
import edu.uci.isr.myx.fw.MyxRegistry;
import edu.uci.isr.sysutils.UIDGenerator;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFileEvent;
import edu.uci.isr.xarchflat.XArchFileListener;

public class InteractionsEditorSupport {

	// For tree node cache
	public static final String BNA_WORLD_KEY = "bnaWorld";

	// For editor pane properties
	public static final String EDITING_BNA_COMPOSITE_KEY = "bnaComposite";
	
	public static void setupEditor(ArchipelagoServices AS, ObjRef diagramInputRef){
		ObjRef xArchRef = AS.xarch.getXArch(diagramInputRef);

		IBNAWorld bnaWorld = setupWorld(AS, xArchRef, diagramInputRef);
		if(bnaWorld == null){
			return;
		}

		final IBNAView bnaView = new DefaultBNAView(null, bnaWorld, new DefaultCoordinateMapper());

		AS.editor.clearEditor();
		Composite parentComposite = AS.editor.getParentComposite();
		FillLayout fl = new FillLayout();
		fl.type = SWT.HORIZONTAL;
		parentComposite.setLayout(fl);

		final BNAComposite bnaComposite = new BNAComposite(parentComposite, SWT.V_SCROLL | SWT.H_SCROLL | SWT.DOUBLE_BUFFERED, bnaView);
		bnaComposite.setBackground(parentComposite.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		bnaComposite.addDisposeListener(new DisposeListener(){

			public void widgetDisposed(DisposeEvent e){
				EnvironmentPropertiesThing ept = BNAUtils.getEnvironmentPropertiesThing(bnaComposite.getView().getWorld().getBNAModel());
				BNAUtils.saveCoordinateMapperData(bnaComposite.getView().getCoordinateMapper(), ept);
				bnaComposite.removeDisposeListener(this);
			}
		});

		BNARenderingSettings.setAntialiasGraphics(bnaComposite, AS.prefs.getBoolean(ArchipelagoConstants.PREF_ANTIALIAS_GRAPHICS));
		BNARenderingSettings.setAntialiasText(bnaComposite, AS.prefs.getBoolean(ArchipelagoConstants.PREF_ANTIALIAS_TEXT));
		BNARenderingSettings.setDecorativeGraphics(bnaComposite, AS.prefs.getBoolean(ArchipelagoConstants.PREF_DECORATIVE_GRAPHICS));

		ArchipelagoUtils.addZoomWidget(bnaComposite, bnaView);

		bnaComposite.pack();
		parentComposite.layout(true);

		EnvironmentPropertiesThing ept = BNAUtils.getEnvironmentPropertiesThing(bnaComposite.getView().getWorld().getBNAModel());
		BNAUtils.restoreCoordinateMapperData((IMutableCoordinateMapper)bnaComposite.getView().getCoordinateMapper(), ept);

		ArchipelagoUtils.setBNAComposite(AS.editor, bnaComposite);
		bnaComposite.setFocus();
	}

	public static IBNAWorld setupWorld(ArchipelagoServices AS, ObjRef xArchRef, ObjRef diagramInputRef){
		IBNAWorld bnaWorld = (IBNAWorld)AS.treeNodeDataCache.getData(xArchRef, diagramInputRef, BNA_WORLD_KEY);
		IBNAModel bnaModel = null;

		if(bnaWorld != null){
			bnaModel = bnaWorld.getBNAModel();			
		}
		else{
			String diagramInputID = XadlUtils.getID(AS.xarch, diagramInputRef);
			if(diagramInputID == null){
				return null;
			}
			bnaModel = new DefaultBNAModel();

			EnvironmentPropertiesThing ept = BNAUtils.getEnvironmentPropertiesThing(bnaModel);
			ept.setProperty(ArchipelagoUtils.XARCH_ID_PROPERTY_NAME, diagramInputID);

			bnaWorld = new DefaultBNAWorld("interaction", bnaModel);
			AS.treeNodeDataCache.setData(xArchRef, diagramInputRef, BNA_WORLD_KEY, bnaWorld);

			setupWorld(AS, AS.xarch.getXArch(diagramInputRef), bnaWorld, diagramInputRef);
		}

		EnvironmentPropertiesThing ept = BNAUtils.getEnvironmentPropertiesThing(bnaModel);
		String changesXArchID = ept.getProperty("ChangesID");
		if (changesXArchID != null){
			//If the env variable ChangesID is set, make sure it points to an unmapped (current) change session.
			ObjRef changesRef = AS.xarch.getByID(changesXArchID);
			String status = (String)AS.xarch.get(changesRef, "status");
			if (status.equals("mapped")){
				ept.removeProperty("ChangesID");
			}
		} 
		//Re-test since ChangesID may be removed above.
		changesXArchID = ept.getProperty("ChangesID");
		if (changesXArchID == null) {
			//If the env variable ChangesID is not set, make sure there is no unmapped change session.
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
			ObjRef[] changes = AS.xarch.getAll(archChangeRef, "changes");
			for (ObjRef chg: changes){
				String status = (String)AS.xarch.get(chg, "status");
				if (status.equals("unmapped")){
					//Save ID for future reference
					String changesID = XadlUtils.getID(AS.xarch, chg);
					ept.setProperty("ChangesID", changesID);
					break;
				}							
			}
		}		
		
		ArchipelagoUtils.applyGridPreferences(AS, bnaModel);

		return bnaWorld;
	}

	static void setupWorld(ArchipelagoServices AS, final ObjRef xArchRef, final IBNAWorld bnaWorld, ObjRef diagramInputRef){
		IThingLogicManager logicManager = bnaWorld.getThingLogicManager();

		// tracking logics
		TypedThingTrackingLogic tttl = new TypedThingTrackingLogic();
		logicManager.addThingLogic(tttl);
		ThingPropertyTrackingLogic tptl = new ThingPropertyTrackingLogic();
		logicManager.addThingLogic(tptl);
		ThingPropertyPrefixTrackingLogic tpptl = new ThingPropertyPrefixTrackingLogic(tptl);
		logicManager.addThingLogic(tpptl);
		ReferenceTrackingLogic rtl = new ReferenceTrackingLogic();
		logicManager.addThingLogic(rtl);
		SelectionTrackingLogic stl = new SelectionTrackingLogic();
		logicManager.addThingLogic(stl);
		ModelBoundsTrackingLogic mbtl = new ModelBoundsTrackingLogic(tttl);
		logicManager.addThingLogic(mbtl);

		// event logics
		DragMoveEventsLogic dml = new DragMoveEventsLogic();
		logicManager.addThingLogic(dml);
		logicManager.addThingLogic(new WorldThingExternalEventsLogic(tttl));

		// background logics
		logicManager.addThingLogic(new LifeSapperLogic(tttl));
		logicManager.addThingLogic(new RotatingOffsetLogic(tttl));
		logicManager.addThingLogic(new ToolTipLogic());
		// FIXME: this needs to be handled better (does not work with hierarchies or multiple instances)
		XadlSelectionProviderLogic xspl = new XadlSelectionProviderLogic(stl, AS.xarch);
		logicManager.addThingLogic(xspl);
		AS.workbenchSite.setSelectionProvider(xspl);

		// maintenance logics
		logicManager.addThingLogic(new MirrorAnchorPointLogic(rtl));
		logicManager.addThingLogic(new MirrorBoundingBoxLogic(rtl));
		logicManager.addThingLogic(new MirrorEndpointLogic(rtl));
		logicManager.addThingLogic(new MirrorValueLogic(rtl, tpptl));
		logicManager.addThingLogic(new MaintainAnchoredAssemblyOrientationLogic(rtl));
		logicManager.addThingLogic(new MaintainGridLayoutLogic());
		logicManager.addThingLogic(new MaintainStickyPointLogic(rtl, tpptl));
		logicManager.addThingLogic(new MaintainXadlLinksLogic(tptl, tpptl));
		logicManager.addThingLogic(new MoveWithLogic(rtl));

		// general editor logics
		logicManager.addThingLogic(new ClickSelectionLogic(tttl));
		logicManager.addThingLogic(new MouseWheelZoomingLogic());
		logicManager.addThingLogic(new MousePanningLogic());
		logicManager.addThingLogic(new SnapToGridLogic(dml));
		logicManager.addThingLogic(new KeyNudgerLogic());
		logicManager.addThingLogic(new MarqueeSelectionLogic(tttl));
		logicManager.addThingLogic(new DragMovableLogic(dml, stl));
		logicManager.addThingLogic(new BoxReshapeHandleLogic(stl, dml));
		logicManager.addThingLogic(new SplineReshapeHandleLogic(stl, dml));
		logicManager.addThingLogic(new SplineBreakLogic());
		logicManager.addThingLogic(new StandardCursorLogic());
		logicManager.addThingLogic(new RotaterLogic());

		// decorate logics
		logicManager.addThingLogic(new AnnotateExplicitChangeLogic(AS.xarchcs, AS.explicit, xArchRef));

		// editor specific: mapping
		IThing otherThingsParent = new NoThing();
		bnaWorld.getBNAModel().addThing(otherThingsParent);
		IThing splineThingsParent = new NoThing();
		bnaWorld.getBNAModel().addThing(splineThingsParent);

		//logicManager.addThingLogic(new MapXadlParticipantLogic(AS.xarchcs, diagramInputRef, "lifeline", tptl, otherThingsParent, "lifeline"));
		logicManager.addThingLogic(new MapXadlLifelineLogic(AS.xarchcs, diagramInputRef, "lifeline", tptl, otherThingsParent, "lifeline"));
		//logicManager.addThingLogic(new MapXadlInitialStateLogic(AS.xarchcs, diagramInputRef, "state", "stateType", "initial", tptl, otherThingsParent, "initialState"));
		//logicManager.addThingLogic(new MapXadlStateLogic(AS.xarchcs, diagramInputRef, "state", "stateType", "state", tptl, otherThingsParent, "state"));
		//logicManager.addThingLogic(new MapXadlFinalStateLogic(AS.xarchcs, diagramInputRef, "state", "stateType", "final", tptl, otherThingsParent, "finalState"));
		logicManager.addThingLogic(new MapXadlMessageLogic(AS.xarchcs, diagramInputRef, "message", tptl, splineThingsParent, "message"));
		logicManager.addThingLogic(new MapXadlNoteLogic(AS.xarchcs, diagramInputRef, "note", tptl, otherThingsParent, "note"));

		//Menu logics
		logicManager.addThingLogic(new XadlCopyPasteLogic(AS.xarchcs, diagramInputRef, stl,AS.copyPasteManager));
		logicManager.addThingLogic(new FindDialogLogic(new ArchipelagoFinder(AS)));
		logicManager.addThingLogic(new AlignAndDistributeLogic());
		logicManager.addThingLogic(new RectifyToGridLogic());
		logicManager.addThingLogic(new EditTextLogic());
		logicManager.addThingLogic(new InteractionsNewElementLogic(AS, xArchRef));
		logicManager.addThingLogic(new InteractionsSelectComponentLogic(AS, xArchRef));
		logicManager.addThingLogic(new XadlRemoveElementLogic(AS.xarch, xArchRef));
		logicManager.addThingLogic(new MapToCodeLogic(AS, xArchRef));		
		logicManager.addThingLogic(new ExportBitmapLogic(mbtl));

		// Note: Hints need to be applied last, after all things are synchronized
		// hint synchronization logics
		XAdlHintRepository hr = new XAdlHintRepository(AS.xarch, diagramInputRef, "edu.uci.isr.archstudio4.comp.archipelago", "4.1.0", tptl);
		logicManager.addThingLogic(new SynchronizeHintsLogic(hr));

		// map and later remove the logics when the document is closed
		final IMyxBrick brick = MyxRegistry.getSharedInstance().waitForBrick(ArchipelagoMyxComponent.class);
		final MyxRegistry myxRegistry = MyxRegistry.getSharedInstance();
		final List<Object> myxObjs = new ArrayList<Object>(Arrays.asList(logicManager.getAllThingLogics()));
		myxObjs.addAll(Arrays.asList(new Object[]{hr, new XArchFileListener(){

			public void handleXArchFileEvent(XArchFileEvent evt){
				if(xArchRef.equals(evt.getXArchRef()) && XArchFileEvent.XARCH_CLOSED_EVENT == evt.getEventType()){
					for(Object o: myxObjs){
						myxRegistry.unmap(brick, o);
					}
					bnaWorld.destroy();
				}
			}
		}}));
		for(Object o: myxObjs){
			myxRegistry.map(brick, o);
		}
	}	
}
