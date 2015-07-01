package edu.uci.isr.bna4.demo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
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
import edu.uci.isr.bna4.assemblies.EndpointAssembly;
import edu.uci.isr.bna4.assemblies.StickySplineAssembly;
import edu.uci.isr.bna4.constants.ArrowheadShape;
import edu.uci.isr.bna4.facets.IHasAnchorPoint;
import edu.uci.isr.bna4.logics.background.RotatingOffsetLogic;
import edu.uci.isr.bna4.logics.coordinating.MaintainAnchoredAssemblyOrientationLogic;
import edu.uci.isr.bna4.logics.coordinating.MaintainStickyPointLogic;
import edu.uci.isr.bna4.logics.coordinating.MirrorAnchorPointLogic;
import edu.uci.isr.bna4.logics.coordinating.MirrorBoundingBoxLogic;
import edu.uci.isr.bna4.logics.coordinating.MirrorEndpointLogic;
import edu.uci.isr.bna4.logics.coordinating.MirrorValueLogic;
import edu.uci.isr.bna4.logics.coordinating.MoveWithLogic;
import edu.uci.isr.bna4.logics.coordinating.MaintainStickyPointLogic.StickyMode;
import edu.uci.isr.bna4.logics.editing.BoxReshapeHandleLogic;
import edu.uci.isr.bna4.logics.editing.ClickSelectionLogic;
import edu.uci.isr.bna4.logics.editing.DragMovableLogic;
import edu.uci.isr.bna4.logics.editing.MarqueeSelectionLogic;
import edu.uci.isr.bna4.logics.editing.SplineBreakLogic;
import edu.uci.isr.bna4.logics.editing.SplineReshapeHandleLogic;
import edu.uci.isr.bna4.logics.editing.StandardCursorLogic;
import edu.uci.isr.bna4.logics.events.DragMoveEventsLogic;
import edu.uci.isr.bna4.logics.events.WorldThingExternalEventsLogic;
import edu.uci.isr.bna4.logics.navigating.MousePanningLogic;
import edu.uci.isr.bna4.logics.navigating.MouseWheelZoomingLogic;
import edu.uci.isr.bna4.logics.tracking.ModelBoundsTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.ReferenceTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.SelectionTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.ThingPropertyPrefixTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.ThingPropertyTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.TypedThingTrackingLogic;
import edu.uci.isr.widgets.swt.constants.Flow;
import edu.uci.isr.widgets.swt.constants.Orientation;

public class BasicBNADemo{

	public static void main(String args[]){
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		final IBNAModel m = new DefaultBNAModel();

		IBNAWorld bnaWorld1 = new DefaultBNAWorld("bna", m);
		setupTopWorld(bnaWorld1);
		populateModel(m);

		IBNAView bnaView1 = new DefaultBNAView(null, bnaWorld1, new DefaultCoordinateMapper());

		IBNAModel m2 = new DefaultBNAModel();
		IBNAWorld bnaWorld2 = new DefaultBNAWorld("subworld", m2);
		setupWorld(bnaWorld2);

		populateModel(m2);
		/*
		 * IBNAView bnaView2 = new DefaultBNAView(bnaView1, bnaWorld2, new
		 * DefaultCoordinateMapper()); ViewThing wt2 = new ViewThing();
		 * wt2.setBoundingBox((DefaultCoordinateMapper.DEFAULT_WORLD_WIDTH / 2)
		 * + 20, (DefaultCoordinateMapper.DEFAULT_WORLD_HEIGHT / 2) + 20, 200,
		 * 200); wt2.setView(bnaView2); m.addThing(wt2); IBNAView bnaView3 = new
		 * DefaultBNAView(bnaView1, bnaWorld2, new DefaultCoordinateMapper());
		 * ViewThing wt3 = new ViewThing();
		 * wt3.setBoundingBox((DefaultCoordinateMapper.DEFAULT_WORLD_WIDTH / 2)
		 * + 200, (DefaultCoordinateMapper.DEFAULT_WORLD_HEIGHT / 2) + 20, 200,
		 * 200); wt3.setView(bnaView3); m.addThing(wt3);
		 */

		populateWithViews(m, bnaView1, bnaWorld2);

		final BNAComposite bnaComposite = new BNAComposite(shell, SWT.V_SCROLL | SWT.H_SCROLL | SWT.DOUBLE_BUFFERED, bnaView1);
		bnaComposite.setSize(500, 500);
		bnaComposite.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		shell.setSize(400, 400);
		shell.open();

		while(!shell.isDisposed()){
			if(!display.readAndDispatch()){
				display.sleep();
			}
		}
		display.dispose();
		System.exit(0);
	}

	static void setupTopWorld(IBNAWorld bnaWorld){
		IThingLogicManager logicManager = bnaWorld.getThingLogicManager();
		logicManager.addThingLogic(new MouseWheelZoomingLogic());
		logicManager.addThingLogic(new MousePanningLogic());
		setupWorld(bnaWorld);
	}

	static void setupWorld(IBNAWorld bnaWorld){
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
		logicManager.addThingLogic(new MirrorValueLogic(rtl, tpptl));
		logicManager.addThingLogic(new MirrorBoundingBoxLogic(rtl));
		logicManager.addThingLogic(new MirrorEndpointLogic(rtl));
		DragMoveEventsLogic dml = new DragMoveEventsLogic();
		logicManager.addThingLogic(dml);
		logicManager.addThingLogic(new RotatingOffsetLogic(tttl));
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

	static void populateModel(/* BNAComposite c, */IBNAModel m){
		final BoxAssembly[] boxes = new BoxAssembly[50];

		for(int i = 0; i < boxes.length; i++){
			BoxAssembly box = new BoxAssembly(m, null, null);
			box.getBoxThing().setGradientFilled(true);
			box.getBoxBorderThing().setLineStyle(SWT.LINE_DASH);
			box.getBoxBorderThing().setColor(new RGB(0, 0, 0));
			box.getBoxedLabelThing().setText("Now is the time for all good men to come to the aid of their country");
			box.getBoxedLabelThing().setColor(new RGB(255, 0, 0));
			box.getBoxGlassThing().setBoundingBox(DefaultCoordinateMapper.DEFAULT_WORLD_WIDTH / 2 + 20 + i * 10, DefaultCoordinateMapper.DEFAULT_WORLD_HEIGHT / 2 + 20 + i * 10, 100, 100);
			box.getBoxGlassThing().setSelected(i % 2 == 0);
			boxes[i] = box;
		}

		EndpointAssembly endpoint = new EndpointAssembly(m, null, null);
		endpoint.getBoxThing().setColor(new RGB(255, 255, 255));
		endpoint.getBoxBorderThing().setColor(new RGB(0, 0, 0));
		endpoint.getDirectionalLabelThing().setColor(new RGB(0, 0, 0));
		endpoint.getDirectionalLabelThing().setOrientation(Orientation.NORTHWEST);
		endpoint.getDirectionalLabelThing().setFlow(Flow.INOUT);
		endpoint.getEndpointGlassThing().setAnchorPoint(new Point(DefaultCoordinateMapper.DEFAULT_WORLD_WIDTH / 2 + 20, DefaultCoordinateMapper.DEFAULT_WORLD_HEIGHT / 2 + 20));
		MaintainStickyPointLogic.stickPoint(boxes[0].getBoxGlassThing(), IHasAnchorPoint.ANCHOR_POINT_PROPERTY_NAME, StickyMode.EDGE, endpoint.getEndpointGlassThing());

		StickySplineAssembly spline = new StickySplineAssembly(m, null, null);
		spline.getSplineGlassThing().setEndpoint1(new Point(DefaultCoordinateMapper.DEFAULT_WORLD_WIDTH / 2 + 20, DefaultCoordinateMapper.DEFAULT_WORLD_HEIGHT / 2 + 20));
		Point[] midpoints = new Point[]{new Point(DefaultCoordinateMapper.DEFAULT_WORLD_WIDTH / 2 + 30, DefaultCoordinateMapper.DEFAULT_WORLD_HEIGHT / 2 + 30), new Point(DefaultCoordinateMapper.DEFAULT_WORLD_WIDTH / 2 + 50, DefaultCoordinateMapper.DEFAULT_WORLD_HEIGHT / 2 + 50)};
		spline.getSplineGlassThing().setMidpoints(midpoints);
		spline.getSplineGlassThing().setEndpoint2(new Point(DefaultCoordinateMapper.DEFAULT_WORLD_WIDTH / 2 + 200, DefaultCoordinateMapper.DEFAULT_WORLD_HEIGHT / 2 + 100));
		spline.getEndpoint1ArrowheadThing().setArrowheadShape(ArrowheadShape.TRIANGLE);
		spline.getEndpoint1ArrowheadThing().setArrowheadSize(10);
		spline.getEndpoint1ArrowheadThing().setSecondaryColor(new RGB(128, 128, 128));
		spline.getEndpoint1ArrowheadThing().setArrowheadFilled(true);

		spline.getEndpoint2ArrowheadThing().setArrowheadShape(ArrowheadShape.TRIANGLE);
		spline.getEndpoint2ArrowheadThing().setArrowheadSize(10);
		spline.getEndpoint2ArrowheadThing().setSecondaryColor(new RGB(128, 128, 128));
		spline.getEndpoint2ArrowheadThing().setArrowheadFilled(true);
		/*
		 * final PathAssembly[] paths = new PathAssembly[50]; for (int i = 0; i
		 * < paths.length; i++) { PathAssembly path = PathAssembly.create(m,
		 * null); path.getPathThing().setGradientFilled(true);
		 * path.getPathGlassThing().setPathData(newExamplePathData(100, 100));
		 * path.getPathGlassThing().moveRelative((DefaultCoordinateMapper.
		 * DEFAULT_WORLD_WIDTH / 2) + 220 + (i 10),
		 * (DefaultCoordinateMapper.DEFAULT_WORLD_HEIGHT / 2) + 20 + (i 10));
		 * path.getPathGlassThing().setSelected((i % 2) == 0); paths[i] = path;
		 * }
		 */
		/*
		 * MarqueeBorderThing mbt = new MarqueeBorderThing();
		 * mbt.setBoundingBox((DefaultCoordinateMapper.DEFAULT_WORLD_WIDTH / 2),
		 * (DefaultCoordinateMapper.DEFAULT_WORLD_HEIGHT / 2), 100, 100);
		 * m.addThing(mbt);
		 */
		// final ScrolledComposite sc1 = new ScrolledComposite(shell, SWT.H_SCROLL |
		// SWT.V_SCROLL | SWT.BORDER);
	}

	static void populateWithViews(IBNAModel m, IBNAView parentView, IBNAWorld internalWorld){
		BoxAssembly vbox1 = new BoxAssembly(m, null, null);
		vbox1.getBoxThing().setGradientFilled(true);
		vbox1.getBoxBorderThing().setLineStyle(SWT.LINE_DASH);
		vbox1.getBoxBorderThing().setColor(new RGB(0, 0, 0));
		vbox1.getBoxedLabelThing().setText("Viewsion 1");
		vbox1.getBoxedLabelThing().setColor(new RGB(255, 0, 0));
		vbox1.getBoxGlassThing().setBoundingBox(DefaultCoordinateMapper.DEFAULT_WORLD_WIDTH / 2 + 20 + 200, DefaultCoordinateMapper.DEFAULT_WORLD_HEIGHT / 2 + 20 + 100, 200, 200);
		vbox1.getBoxGlassThing().setSelected(false);
		vbox1.getWorldThing().setWorld(internalWorld);
		//vbox1.getViewThing().setView(new DefaultBNAView(parentView, internalWorld, new DefaultCoordinateMapper()));

		BoxAssembly vbox2 = new BoxAssembly(m, null, null);
		vbox2.getBoxThing().setGradientFilled(true);
		vbox2.getBoxBorderThing().setLineStyle(SWT.LINE_DASH);
		vbox2.getBoxBorderThing().setColor(new RGB(0, 0, 0));
		vbox2.getBoxedLabelThing().setText("Viewsion 2");
		vbox2.getBoxedLabelThing().setColor(new RGB(255, 0, 0));
		vbox2.getBoxGlassThing().setBoundingBox(DefaultCoordinateMapper.DEFAULT_WORLD_WIDTH / 2 + 20 + 400, DefaultCoordinateMapper.DEFAULT_WORLD_HEIGHT / 2 + 20 + 100, 200, 200);
		vbox2.getBoxGlassThing().setSelected(false);
		vbox2.getWorldThing().setWorld(internalWorld);
		//vbox2.getViewThing().setView(new DefaultBNAView(parentView, internalWorld, new DefaultCoordinateMapper()));

	}
}
