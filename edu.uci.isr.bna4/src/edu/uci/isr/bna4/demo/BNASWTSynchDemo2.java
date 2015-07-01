package edu.uci.isr.bna4.demo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.uci.isr.bna4.BNAComposite;
import edu.uci.isr.bna4.DefaultBNAModel;
import edu.uci.isr.bna4.DefaultBNAView;
import edu.uci.isr.bna4.DefaultBNAWorld;
import edu.uci.isr.bna4.DefaultCoordinateMapper;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IBNAWorld;
import edu.uci.isr.bna4.IThingLogicManager;
import edu.uci.isr.bna4.assemblies.BoxAssembly;
import edu.uci.isr.bna4.facets.IHasSWTLayout;
import edu.uci.isr.bna4.facets.IHasSWTLayoutData;
import edu.uci.isr.bna4.logics.coordinating.MaintainAnchoredAssemblyOrientationLogic;
import edu.uci.isr.bna4.logics.coordinating.MirrorAnchorPointLogic;
import edu.uci.isr.bna4.logics.coordinating.MirrorBoundingBoxLogic;
import edu.uci.isr.bna4.logics.coordinating.MirrorEndpointLogic;
import edu.uci.isr.bna4.logics.coordinating.MirrorValueLogic;
import edu.uci.isr.bna4.logics.coordinating.MoveWithLogic;
import edu.uci.isr.bna4.logics.editing.BoxReshapeHandleLogic;
import edu.uci.isr.bna4.logics.editing.ClickSelectionLogic;
import edu.uci.isr.bna4.logics.editing.DragMovableLogic;
import edu.uci.isr.bna4.logics.editing.MarqueeSelectionLogic;
import edu.uci.isr.bna4.logics.editing.SplineBreakLogic;
import edu.uci.isr.bna4.logics.editing.SplineReshapeHandleLogic;
import edu.uci.isr.bna4.logics.editing.StandardCursorLogic;
import edu.uci.isr.bna4.logics.events.DragMoveEventsLogic;
import edu.uci.isr.bna4.logics.events.WorldThingExternalEventsLogic;
import edu.uci.isr.bna4.logics.swt.SWTBNALayoutLogic;
import edu.uci.isr.bna4.logics.swt.SWTBNASynchLogic;
import edu.uci.isr.bna4.logics.tracking.ModelBoundsTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.ReferenceTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.SelectionTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.ThingPropertyPrefixTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.ThingPropertyTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.TypedThingTrackingLogic;

public class BNASWTSynchDemo2{

	SWTBNASynchLogic swtSynchLogic;

	SWTBNALayoutLogic swtLayoutLogic;

	Composite layoutComposite;

	public static void main(String args[]){
		BNASWTSynchDemo2 demo = new BNASWTSynchDemo2();
	}

	public BNASWTSynchDemo2(){

		// Create Core {SWT} components
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		// Left Hand Side {SWT} Composite
		// layoutComposite = new Composite(shell, SWT.BORDER);

		// Right Hand Side {BNA} Composite
		// Create Core {BNA} components
		final IBNAModel m = new DefaultBNAModel();
		IBNAWorld world1 = new DefaultBNAWorld("bna", m);
		setupWorld(world1);
		IBNAView view1 = new DefaultBNAView(null, world1, new DefaultCoordinateMapper());

		final BNAComposite bnaComposite = new BNAComposite(shell, SWT.V_SCROLL | SWT.H_SCROLL | SWT.DOUBLE_BUFFERED, view1);
		bnaComposite.setSize(500, 500);
		bnaComposite.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		// Draw shell to screen
		shell.setSize(600, 600);
		shell.open();

		//// Sample Box
		//BoxAssembly[] boxes = new BoxAssembly[10];
		//for(int i=0; i<boxes.length; i++){
		//	BoxAssembly box = boxes[i] = BoxAssembly.create(m, null);
		//	box.getBoxedLabelThing().setText("I'm a BNA Box");
		//	box.getBoxThing().setGradientFilled(true);
		//	box.getBoxBorderThing().setLineStyle(SWT.LINE_DASH);
		//	box.getBoxBorderThing().setColor(new RGB(0, 0, 0));
		//	box.getBoxedLabelThing().setColor(new RGB(255, 255, 255));
		//	box.getBoxGlassThing().setBoundingBox((DefaultCoordinateMapper.DEFAULT_WORLD_WIDTH / 2)
		//				+ 20 + (i * 10), (DefaultCoordinateMapper.DEFAULT_WORLD_HEIGHT / 2)
		//				+ 20 + (i * 10), 100, 100);
		//	box.getBoxGlassThing().setSelected(true);
		//}
		//
		//composite.setLayout(new GridLayout(2, false));
		//GridData gridData = new GridData();
		//gridData.horizontalIndent = 0;
		//gridData.verticalIndent = 0;
		//composite.setLayoutData(gridData);
		//
		//// Sample Button
		//Button[] swtButtons = new Button[10];
		//for(int i=0; i<swtButtons.length; i++){
		//	Button swtButton = swtButtons[i] = new Button(composite, SWT.NONE);
		//	swtButton.setText("I'm a SWT button");
		//	swtButton.setSize(150, 100);
		//	swtButton.setLocation(10 + i * 10, 10 + i * 10);
		//	swtButton.setVisible(true);
		//	gridData = new GridData();
		//	if(i == 0)
		//		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		//	gridData.horizontalIndent = 0;
		//	gridData.verticalIndent = 0;
		//	swtButton.setLayoutData(gridData);
		//}
		//
		//// Add mappings to each BNA/SWT pair the logic should be aware of managing
		//for(int i=0; i<boxes.length && i<swtButtons.length; i++){
		//	//swtBNALogic.addSWTToBNAMapping(swtButtons[i], boxes[i].getBoxGlassThing().getID());
		//	swtBNALogic.addBNAToSWTMapping(boxes[i].getBoxClassThing().getID(), swtButtons[i]);
		//}

		GridData gd;

		// EXAMPLE CLASS

		BoxAssembly box = new BoxAssembly(m, null, null);
		box.getBoxGlassThing().setBoundingBox(DefaultCoordinateMapper.DEFAULT_WORLD_WIDTH / 2 + 20, DefaultCoordinateMapper.DEFAULT_WORLD_HEIGHT / 2 + 20, 100, 100);
		box.getBoxGlassThing().setProperty(IHasSWTLayout.SWT_LAYOUT_PROPERTY_NAME, new GridLayout(1, false));

		// class BNA
		BoxAssembly classBox = new BoxAssembly(m, box.getBoxThing(), null);
		classBox.getBoxGlassThing().setBoundingBox(DefaultCoordinateMapper.DEFAULT_WORLD_WIDTH / 2 + 20, DefaultCoordinateMapper.DEFAULT_WORLD_HEIGHT / 2 + 20, 100, 100);
		classBox.getBoxGlassThing().setProperty(IHasSWTLayout.SWT_LAYOUT_PROPERTY_NAME, new GridLayout(1, false));
		classBox.getBoxGlassThing().setProperty(IHasSWTLayoutData.SWT_LAYOUT_DATA_PROPERTY_NAME, new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		classBox.getBoxGlassThing().setProperty(IHasSWTLayoutData.LAYOUT_TARGET_ID_PROPERTY_NAME, box.getBoxGlassThing().getID());

		// logic's actions
		//		Composite classBoxSWT = new Composite(composite, SWT.NONE);
		//		classBoxSWT.setBackground(classBoxSWT.getDisplay().getSystemColor(SWT.COLOR_RED));
		//		classBoxSWT.setLayout((Layout)classBox.getBoxGlassThing().getProperty(IHasSWTLayout.SWT_LAYOUT_PROPERTY_NAME));
		//		swtBNALogic.addBNAToSWTMapping(classBox.getBoxGlassThing().getID(), classBoxSWT);

		// class title BNA
		BoxAssembly classTitle = new BoxAssembly(m, classBox.getBoxThing(), null);
		classTitle.getBoxedLabelThing().setText("class");
		classTitle.getBoxGlassThing().setProperty(IHasSWTLayoutData.SWT_LAYOUT_DATA_PROPERTY_NAME, new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		classTitle.getBoxGlassThing().setProperty(IHasSWTLayoutData.LAYOUT_TARGET_ID_PROPERTY_NAME, classBox.getBoxGlassThing().getID());

		// logic's actions
		//		Button classTitleSWT = new Button(classBoxSWT, SWT.NONE);
		//		classTitleSWT.setText(classTitle.getText());
		//		classTitleSWT.setVisible(true);
		//		classTitleSWT.setLayoutData(classTitle.getProperty(IHasSWTLayoutData.SWT_LAYOUT_DATA_PROPERTY_NAME));
		//		swtBNALogic.addSWTToBNAMapping(classTitleSWT, classTitle.getID());

		BoxAssembly[] classOperations = new BoxAssembly[3];
		for(int i = 0; i < classOperations.length; i++){
			// class operation BNA
			BoxAssembly classOperation = new BoxAssembly(m, classBox.getBoxThing(), null);
			classOperation.getBoxedLabelThing().setText("method" + i);
			classOperations[i] = classOperation;
			classOperation.getBoxGlassThing().setProperty(IHasSWTLayoutData.SWT_LAYOUT_DATA_PROPERTY_NAME, new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			classOperation.getBoxGlassThing().setProperty(IHasSWTLayoutData.LAYOUT_TARGET_ID_PROPERTY_NAME, classBox.getBoxGlassThing().getID());

			// logic's actions
			//			Button classOperationSWT = new Button(classBoxSWT, SWT.NONE);
			//			classOperationSWT.setText(classOperation.getText());
			//			classOperationSWT.setVisible(true);
			//			classOperationSWT.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
			//			swtBNALogic.addSWTToBNAMapping(classOperationSWT, classOperation.getID());
		}

		BoxAssembly[] classFields = new BoxAssembly[3];
		for(int i = 0; i < classFields.length; i++){
			// class field BNA
			BoxAssembly classField = new BoxAssembly(m, classBox.getBoxThing(), null);
			classFields[i] = classField;
			classField.getBoxedLabelThing().setText("field " + i);
			classField.getBoxGlassThing().setProperty(IHasSWTLayoutData.SWT_LAYOUT_DATA_PROPERTY_NAME, new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			classField.getBoxGlassThing().setProperty(IHasSWTLayoutData.LAYOUT_TARGET_ID_PROPERTY_NAME, classBox.getBoxGlassThing().getID());

			// logic's actions
			//			Button classFieldSWT = new Button(classBoxSWT, SWT.NONE);
			//			classFieldSWT.setText(classField.getText());
			//			classFieldSWT.setVisible(true);
			//			classFieldSWT.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1));
			//			swtBNALogic.addSWTToBNAMapping(classFieldSWT, classField.getID());
		}

		while(!shell.isDisposed()){
			if(!display.readAndDispatch()){
				display.sleep();
			}
		}
		display.dispose();
		System.exit(0);
	}

	private void setupWorld(IBNAWorld bnaWorld){

		// Add all associated logics

		IThingLogicManager logicManager = bnaWorld.getThingLogicManager();

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

		logicManager.addThingLogic(new MoveWithLogic(rtl));
		logicManager.addThingLogic(new MirrorAnchorPointLogic(rtl));
		logicManager.addThingLogic(new MirrorBoundingBoxLogic(rtl));
		logicManager.addThingLogic(new MirrorValueLogic(rtl, tpptl));
		logicManager.addThingLogic(new MirrorEndpointLogic(rtl));

		swtSynchLogic = new SWTBNASynchLogic(rtl);
		logicManager.addThingLogic(swtSynchLogic);

		swtLayoutLogic = new SWTBNALayoutLogic(swtSynchLogic);
		logicManager.addThingLogic(swtLayoutLogic);

		DragMoveEventsLogic dml = new DragMoveEventsLogic();
		logicManager.addThingLogic(dml);
		logicManager.addThingLogic(new ClickSelectionLogic(tttl));
		logicManager.addThingLogic(new MarqueeSelectionLogic(tttl));
		logicManager.addThingLogic(new DragMovableLogic(dml, stl));
		logicManager.addThingLogic(new BoxReshapeHandleLogic(stl, dml));
		logicManager.addThingLogic(new SplineReshapeHandleLogic(stl, dml));
		logicManager.addThingLogic(new SplineBreakLogic());
		logicManager.addThingLogic(new MaintainAnchoredAssemblyOrientationLogic(rtl));
		logicManager.addThingLogic(new StandardCursorLogic());

		logicManager.addThingLogic(new ModelBoundsTrackingLogic(tttl));
		logicManager.addThingLogic(new WorldThingExternalEventsLogic(tttl));

	}

}
