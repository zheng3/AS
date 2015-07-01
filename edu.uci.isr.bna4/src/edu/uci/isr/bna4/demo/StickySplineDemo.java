package edu.uci.isr.bna4.demo;

import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
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
import edu.uci.isr.bna4.IMutableCoordinateMapper;
import edu.uci.isr.bna4.IThingLogicManager;
import edu.uci.isr.bna4.UserEditableUtils;
import edu.uci.isr.bna4.assemblies.AssemblyUtils;
import edu.uci.isr.bna4.assemblies.IAssembly;
import edu.uci.isr.bna4.assemblies.PolygonAssembly;
import edu.uci.isr.bna4.assemblies.SplineAssembly;
import edu.uci.isr.bna4.constants.ArrowheadShape;
import edu.uci.isr.bna4.facets.IHasEndpoints;
import edu.uci.isr.bna4.facets.IHasMutableBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutableEndpoints;
import edu.uci.isr.bna4.facets.IHasMutableMidpoints;
import edu.uci.isr.bna4.facets.IHasMutableSelected;
import edu.uci.isr.bna4.facets.IRelativeMovable;
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
import edu.uci.isr.bna4.logics.information.ToolTipLogic;
import edu.uci.isr.bna4.logics.navigating.MousePanningLogic;
import edu.uci.isr.bna4.logics.navigating.MouseWheelZoomingLogic;
import edu.uci.isr.bna4.logics.tracking.ModelBoundsTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.ReferenceTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.SelectionTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.ThingPropertyPrefixTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.ThingPropertyTrackingLogic;
import edu.uci.isr.bna4.logics.tracking.TypedThingTrackingLogic;
import edu.uci.isr.bna4.things.essence.PolygonEssenceThing.IPolygonGenerator;

public class StickySplineDemo{

	public static void main(String args[]){
		IBNAModel bnaModel = new DefaultBNAModel();
		IBNAWorld bnaWorld = new DefaultBNAWorld("bna", bnaModel);
		IBNAView bnaView = new DefaultBNAView(null, bnaWorld, new DefaultCoordinateMapper());
		((IMutableCoordinateMapper)bnaView.getCoordinateMapper()).repositionAbsolute(0, 0);

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		BNAComposite bnaComposite = new BNAComposite(shell, SWT.V_SCROLL | SWT.H_SCROLL | SWT.DOUBLE_BUFFERED, bnaView);
		bnaComposite.setSize(500, 500);
		bnaComposite.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		shell.setSize(500, 500);
		shell.setText("Sticky Spline Demo");
		shell.open();

		setupWorld(bnaWorld);
		populateModel(bnaModel);

		while(!shell.isDisposed()){
			if(!display.readAndDispatch()){
				display.sleep();
			}
		}
		display.dispose();
		System.exit(0);
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

		logicManager.addThingLogic(new MaintainStickyPointLogic(rtl, tpptl));
		logicManager.addThingLogic(new ToolTipLogic());

		logicManager.addThingLogic(new MouseWheelZoomingLogic());
		logicManager.addThingLogic(new MousePanningLogic());
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

	static void populateModel(IBNAModel m){

		Random r = new Random();

		//for(StickyMode stickyMode: StickyMode.values()){
		//	BoxAssembly b = new BoxAssembly(m, null, stickyMode);
		//	b.getBoxedLabelThing().setText(stickyMode.name().replace("_", " "));
		//	b.getBoxGlassThing().setBoundingBox(10 + r.nextInt(430), 10 + r.nextInt(430), 10 + r.nextInt(40), 10 + r.nextInt(40));
		//	ToolTipLogic.setToolTip(b.getBoxGlassThing(), b.getBoxedLabelThing().getText());
		//	UserEditableUtils.addEditableQuality(b.getBoxGlassThing(), IHasMutableSelected.USER_MAY_SELECT, IRelativeMovable.USER_MAY_MOVE, IHasMutableBoundingBox.USER_MAY_RESIZE);
		//}

		for(StickyMode stickyMode: StickyMode.values()){
			PolygonAssembly p = new PolygonAssembly(m, null, stickyMode, new IPolygonGenerator(){

				public Point[] calculatePoints(Rectangle r){
					int x1 = r.x;
					int y1 = r.y;
					int w = r.width;
					int h = r.height;
					int x2 = x1 + w;
					int y2 = y1 + h;
					return new Point[]{new Point(x1 + w / 2, y1), new Point(x2, y1 + h / 2), new Point(x1 + w / 2, y2), new Point(x1, y1 + h / 2), new Point(x1 + w / 2, y1)};
				}
			});
			p.getPolygonGlassThing().setBoundingBox(10 + r.nextInt(430), 10 + r.nextInt(430), 10 + r.nextInt(40), 10 + r.nextInt(40));
			ToolTipLogic.setToolTip(p.getPolygonGlassThing(), stickyMode.name().replace("_", " "));
			UserEditableUtils.addEditableQuality(p.getPolygonGlassThing(), IHasMutableSelected.USER_MAY_SELECT, IRelativeMovable.USER_MAY_MOVE, IHasMutableBoundingBox.USER_MAY_RESIZE);
		}

		for(StickyMode fromStickyMode: StickyMode.values()){
			for(StickyMode toStickyMode: StickyMode.values()){
				IAssembly fa = AssemblyUtils.getAssemblyWithRoot(m.getAllThings(), fromStickyMode);
				IAssembly ta = AssemblyUtils.getAssemblyWithRoot(m.getAllThings(), toStickyMode);

				SplineAssembly s = new SplineAssembly(m, null, null);
				s.getEndpoint1ArrowheadThing().setArrowheadShape(ArrowheadShape.TRIANGLE);
				s.getEndpoint2ArrowheadThing().setArrowheadShape(ArrowheadShape.TRIANGLE);
				MaintainStickyPointLogic.stickPoint(fa.getPart("glass"), IHasEndpoints.ENDPOINT_1_PROPERTY_NAME, fromStickyMode, s.getSplineGlassThing());
				if(toStickyMode != fromStickyMode){
					MaintainStickyPointLogic.stickPoint(ta.getPart("glass"), IHasEndpoints.ENDPOINT_2_PROPERTY_NAME, toStickyMode, s.getSplineGlassThing());
				}
				else{
					s.getSplineGlassThing().setEndpoint2(new Point(10 + r.nextInt(430), 10 + r.nextInt(430)));
				}
				ToolTipLogic.setToolTip(s.getSplineGlassThing(), fromStickyMode.name().replace("_", " ") + " - to - " + toStickyMode.name().replace("_", " "));
				UserEditableUtils.addEditableQuality(s.getSplineGlassThing(), IHasMutableSelected.USER_MAY_SELECT, IRelativeMovable.USER_MAY_MOVE, IHasMutableEndpoints.USER_MAY_MOVE_ENDPOINT1, IHasMutableEndpoints.USER_MAY_MOVE_ENDPOINT2, IHasMutableMidpoints.USER_MAY_ADD_MIDPOINTS, IHasMutableMidpoints.USER_MAY_MOVE_MIDPOINTS, IHasMutableMidpoints.USER_MAY_REMOVE_MIDPOINTS);
				UserEditableUtils.addEditableQuality(s.getSplineGlassThing(), IHasMutableEndpoints.USER_MAY_RESTICK_ENDPOINT1, IHasMutableEndpoints.USER_MAY_RESTICK_ENDPOINT2);
			}
		}
	}
}