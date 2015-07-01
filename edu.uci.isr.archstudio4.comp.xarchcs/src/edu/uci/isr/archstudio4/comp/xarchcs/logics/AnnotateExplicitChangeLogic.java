package edu.uci.isr.archstudio4.comp.xarchcs.logics;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.archstudio4.comp.xarchcs.changesetsync.IChangeSetSync.ChangeStatus;
import edu.uci.isr.archstudio4.comp.xarchcs.explicitadt.ExplicitADTEvent;
import edu.uci.isr.archstudio4.comp.xarchcs.explicitadt.ExplicitADTListener;
import edu.uci.isr.archstudio4.comp.xarchcs.explicitadt.IExplicitADT;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetEvent;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetInterface;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetListener;
import edu.uci.isr.archstudio4.comp.xarchcs.xarchcs.XArchChangeSetEvent.ChangeSetEventType;
import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAModelEvent;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNAModelListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.PathDataConstructor;
import edu.uci.isr.bna4.PathDataUtils;
import edu.uci.isr.bna4.assemblies.AssemblyUtils;
import edu.uci.isr.bna4.assemblies.BoxAssembly;
import edu.uci.isr.bna4.assemblies.EndpointAssembly;
import edu.uci.isr.bna4.assemblies.IAssembly;
import edu.uci.isr.bna4.assemblies.MappingAssembly;
import edu.uci.isr.bna4.assemblies.PathAssembly;
import edu.uci.isr.bna4.assemblies.SplineAssembly;
import edu.uci.isr.bna4.facets.IHasAnchorPoint;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.facets.IHasEndpoints;
import edu.uci.isr.bna4.facets.IHasInternalWorldEndpoint;
import edu.uci.isr.bna4.facets.IHasLineStyle;
import edu.uci.isr.bna4.facets.IHasMidpoints;
import edu.uci.isr.bna4.facets.IHasSelected;
import edu.uci.isr.bna4.logics.coordinating.MirrorValueLogic;
import edu.uci.isr.bna4.logics.coordinating.MoveWithLogic;
import edu.uci.isr.xarchflat.ObjRef;

public class AnnotateExplicitChangeLogic
	extends AbstractThingLogic
	implements ExplicitADTListener, XArchChangeSetListener, IBNAModelListener{

	public static final String XARCH_ID_PROPERTY_NAME = "xArchID"; // TODO: use official source

	XArchChangeSetInterface xarch;

	IExplicitADT explicit;

	ObjRef xArchRef;

	public AnnotateExplicitChangeLogic(XArchChangeSetInterface xarch, IExplicitADT explicit, ObjRef xArchRef){
		this.xarch = xarch;
		this.explicit = explicit;
		this.xArchRef = xArchRef;
	}

	public void handleExplicitEvent(ExplicitADTEvent evt){
		if(evt.getXArchRef().equals(xArchRef)){
			ObjRef[] changeSetRefs = explicit.getExplicitChangeSetRefs(xArchRef);
			switch(evt.getEventType()){
			case UPDATED_EXPLICIT_CHANGE_SETS: {
				IBNAModel model = getBNAModel();
				if(model != null){
					model.beginBulkChange();
					try{
						for(IThing t: model.getAllThings()){
							update(changeSetRefs, t, null);
						}
					}
					finally{
						model.endBulkChange();
					}
				}
				break;
			}
			case UPDATED_EXPLICIT_OBJREF: {
				IBNAModel model = getBNAModel();
				if(model != null){
					model.beginBulkChange();
					try{
						for(IThing t: model.getAllThings()){
							update(changeSetRefs, t, evt.getObjRef());
						}
					}
					finally{
						model.endBulkChange();
					}
				}
				break;
			}
			}
		}
	}

	@Override
	public void init(){
		super.init();
		if(xarch.getChangeSetsEnabled(xArchRef)){
			ObjRef[] changeSetRefs = explicit.getExplicitChangeSetRefs(xArchRef);
			for(IThing thing: getBNAModel().getAllThings()){
				update(changeSetRefs, thing, null);
			}
		}
	}

	public void bnaModelChanged(BNAModelEvent evt){
		if(xarch.getChangeSetsEnabled(xArchRef)){
			ObjRef[] changeSetRefs = explicit.getExplicitChangeSetRefs(xArchRef);
			switch(evt.getEventType()){
			case THING_ADDED:
				update(changeSetRefs, evt.getTargetThing(), null);
				break;
			case THING_CHANGED:
				if(XARCH_ID_PROPERTY_NAME.equals(evt.getThingEvent().getPropertyName())){
					update(changeSetRefs, evt.getTargetThing(), null);
				}
				break;
			}
		}
	}

	public void handleXArchChangeSetEvent(XArchChangeSetEvent evt){
		if(evt.getEventType() == ChangeSetEventType.UPDATED_ENABLED){
			IBNAModel model = getBNAModel();
			if(model != null){
				model.beginBulkChange();
				try{
					ObjRef[] changeSetRefs = explicit.getExplicitChangeSetRefs(xArchRef);
					for(IThing t: model.getAllThings()){
						update(changeSetRefs, t, null);
					}
				}
				finally{
					model.endBulkChange();
				}
			}
		}

	}

	void update(ObjRef[] changeSetRefs, IThing t, ObjRef parentRef){
		String id = t.getProperty(XARCH_ID_PROPERTY_NAME);
		if(id != null){
			IAssembly assembly = AssemblyUtils.getAssemblyWithRoot(t);
			if(assembly != null){
				ObjRef objRef = xarch.getByID(xArchRef, id);
				if(objRef != null && (parentRef == null || objRef.equals(parentRef) || xarch.hasAncestor(objRef, parentRef))){
					ChangeStatus changeStatus = xarch.getChangeStatus(objRef, changeSetRefs);
					//System.err.println("" + objRef + " " + changeStatus + " " + assembly);
					update(assembly, changeStatus);
				}
			}
		}
	}

	void update(IAssembly targetAssembly, ChangeStatus changeStatus){
		IBNAModel model = getBNAModel();

		if(targetAssembly instanceof BoxAssembly){
			updateBoundingBoxAnnotation(model, targetAssembly, changeStatus, ((BoxAssembly)targetAssembly).getBoxBorderThing(), ((BoxAssembly)targetAssembly).getBoxGlassThing(), 25);
		}
		else if(targetAssembly instanceof EndpointAssembly){
			updateBoundingBoxAnnotation(model, targetAssembly, changeStatus, ((EndpointAssembly)targetAssembly).getDirectionalLabelThing(), ((EndpointAssembly)targetAssembly).getEndpointGlassThing(), 15);
		}
		else if(targetAssembly instanceof SplineAssembly){
			updateSplineAnnotation(model, targetAssembly, changeStatus, ((SplineAssembly)targetAssembly).getSplineThing(), ((SplineAssembly)targetAssembly).getSplineGlassThing(), 5);
		}
		else if(targetAssembly instanceof MappingAssembly){
			updateMappingAnnotation(model, targetAssembly, changeStatus, ((MappingAssembly)targetAssembly).getMappingThing(), ((MappingAssembly)targetAssembly).getMappingGlassThing(), 5);
		}
		else if(targetAssembly.getPart("glass") instanceof IHasBoundingBox){
			updateBoundingBoxAnnotation(model, targetAssembly, changeStatus, targetAssembly.getPart("glass"), targetAssembly.getPart("glass"), 25);
		}
		else{
			System.err.println("Cannot annotate: " + targetAssembly);
		}
	}

	void updateBoundingBoxAnnotation(IBNAModel model, IAssembly targetAssembly, ChangeStatus changeStatus, IThing parentThing, IThing glassThing, int size){
		PathAssembly changeAnnotation = AssemblyUtils.getAssemblyWithRoot(model.getChildThings(parentThing), ChangeStatus.class);
		ChangeStatus changeAnnotationStatus = changeAnnotation == null ? ChangeStatus.UNMODIFIED : (ChangeStatus)changeAnnotation.getKind();

		switch(changeStatus){
		case ADDED:
			updateAttributes(model, AssemblyUtils.getRootThingWithPart(glassThing), true, true);
			break;
		case REMOVED:
			updateAttributes(model, AssemblyUtils.getRootThingWithPart(glassThing), true, false);
			break;
		case MODIFIED:
			updateAttributes(model, AssemblyUtils.getRootThingWithPart(glassThing), true, true);
			break;
		case UNMODIFIED:
			updateAttributes(model, AssemblyUtils.getRootThingWithPart(glassThing), true, true);
			break;
		case DETACHED:
			updateAttributes(model, AssemblyUtils.getRootThingWithPart(glassThing), false, false);
			break;
		default:
			throw new IllegalArgumentException();
		}

		if(!changeStatus.equals(changeAnnotationStatus)){
			if(changeAnnotation != null){
				changeAnnotation.remove(true);
				changeAnnotation = null;
			}

			PathDataConstructor pdc;
			RGB color;

			switch(changeStatus){
			case ADDED:
				pdc = PathDataUtils.createUnitPlus(0.35f);
				pdc.translate(-0.5f, -0.5f);
				pdc.scale(size, size);
				color = new RGB(0, 255, 255);
				break;
			case REMOVED:
				pdc = PathDataUtils.createUnitX(0.3f, 0.35f);
				pdc.translate(-0.5f, -0.5f);
				pdc.scale(size, size);
				color = new RGB(255, 0, 0);
				break;
			case MODIFIED:
				pdc = new PathDataConstructor();
				pdc.moveTo(0.5f, 0);
				pdc.lineTo(1, 1);
				pdc.lineTo(0, 1);
				pdc.close();
				pdc.translate(-0.5f, -0.5f);
				pdc.scale(size, size);
				color = new RGB(255, 0, 255);
				break;
			case UNMODIFIED:
				return;
			case DETACHED:
				return;
			default:
				throw new IllegalArgumentException();
			}

			changeAnnotation = new PathAssembly(model, parentThing, changeStatus);
			changeAnnotation.getPathGlassThing().setPathData(pdc.getPathData());
			changeAnnotation.getPathThing().setColor(color);

			if(glassThing instanceof IHasBoundingBox){
				Rectangle r = ((IHasBoundingBox)glassThing).getBoundingBox();
				changeAnnotation.getPathGlassThing().setAnchorPoint(new Point(r.x, r.y));
				MoveWithLogic.moveWith(glassThing, MoveWithLogic.TRACK_BOUNDING_BOX_ONLY, changeAnnotation.getPathGlassThing());
			}
		}
	}

	void updateSplineAnnotation(IBNAModel model, IAssembly targetAssembly, ChangeStatus changeStatus, IThing parentThing, IThing glassThing, int lineWidth){
		SplineAssembly changeAnnotation = AssemblyUtils.getAssemblyWithRoot(model.getChildThings(parentThing), ChangeStatus.class);
		ChangeStatus changeAnnotationStatus = changeAnnotation == null ? ChangeStatus.UNMODIFIED : (ChangeStatus)changeAnnotation.getKind();

		switch(changeStatus){
		case ADDED:
			updateAttributes(model, AssemblyUtils.getRootThingWithPart(glassThing), true, true);
			break;
		case REMOVED:
			updateAttributes(model, AssemblyUtils.getRootThingWithPart(glassThing), true, false);
			break;
		case MODIFIED:
			updateAttributes(model, AssemblyUtils.getRootThingWithPart(glassThing), true, true);
			break;
		case UNMODIFIED:
			updateAttributes(model, AssemblyUtils.getRootThingWithPart(glassThing), true, true);
			break;
		case DETACHED:
			updateAttributes(model, AssemblyUtils.getRootThingWithPart(glassThing), false, false);
			break;
		default:
			throw new IllegalArgumentException();
		}

		if(!changeStatus.equals(changeAnnotationStatus)){
			if(changeAnnotation != null){
				changeAnnotation.remove(true);
				changeAnnotation = null;
			}

			RGB color;
			int lineStyle;

			switch(changeStatus){
			case ADDED:
				color = new RGB(0, 255, 255);
				lineStyle = IHasLineStyle.LINE_STYLE_SOLID;
				break;
			case REMOVED:
				color = new RGB(255, 0, 0);
				lineStyle = IHasLineStyle.LINE_STYLE_DOT;
				break;
			case MODIFIED:
				color = new RGB(255, 0, 255);
				lineStyle = IHasLineStyle.LINE_STYLE_SOLID;
				break;
			case UNMODIFIED:
				return;
			case DETACHED:
				return;
			default:
				throw new IllegalArgumentException();
			}

			changeAnnotation = new SplineAssembly(model, parentThing, changeStatus);
			changeAnnotation.getSplineThing().setColor(color);
			changeAnnotation.getSplineThing().setLineWidth(lineWidth);
			changeAnnotation.getSplineThing().setLineStyle(lineStyle);

			if(glassThing instanceof IHasEndpoints){
				MirrorValueLogic.mirrorValue(glassThing, IHasEndpoints.ENDPOINT_1_PROPERTY_NAME, changeAnnotation.getSplineGlassThing());
				MirrorValueLogic.mirrorValue(glassThing, IHasEndpoints.ENDPOINT_2_PROPERTY_NAME, changeAnnotation.getSplineGlassThing());
			}
			if(glassThing instanceof IHasMidpoints){
				MirrorValueLogic.mirrorValue(glassThing, IHasMidpoints.MIDPOINTS_PROPERTY_NAME, changeAnnotation.getSplineGlassThing());
			}
		}
	}

	void updateMappingAnnotation(IBNAModel model, IAssembly targetAssembly, ChangeStatus changeStatus, IThing parentThing, IThing glassThing, int lineWidth){
		MappingAssembly changeAnnotation = AssemblyUtils.getAssemblyWithRoot(model.getChildThings(parentThing), ChangeStatus.class);
		ChangeStatus changeAnnotationStatus = changeAnnotation == null ? ChangeStatus.UNMODIFIED : (ChangeStatus)changeAnnotation.getKind();

		switch(changeStatus){
		case ADDED:
			updateAttributes(model, AssemblyUtils.getRootThingWithPart(glassThing), true, true);
			break;
		case REMOVED:
			updateAttributes(model, AssemblyUtils.getRootThingWithPart(glassThing), true, false);
			break;
		case MODIFIED:
			updateAttributes(model, AssemblyUtils.getRootThingWithPart(glassThing), true, true);
			break;
		case UNMODIFIED:
			updateAttributes(model, AssemblyUtils.getRootThingWithPart(glassThing), true, true);
			break;
		case DETACHED:
			updateAttributes(model, AssemblyUtils.getRootThingWithPart(glassThing), false, false);
			break;
		default:
			throw new IllegalArgumentException();
		}

		if(!changeStatus.equals(changeAnnotationStatus)){
			if(changeAnnotation != null){
				changeAnnotation.remove(true);
				changeAnnotation = null;
			}

			RGB color;
			int lineStyle;

			switch(changeStatus){
			case ADDED:
				color = new RGB(0, 255, 255);
				lineStyle = IHasLineStyle.LINE_STYLE_SOLID;
				break;
			case REMOVED:
				color = new RGB(255, 0, 0);
				lineStyle = IHasLineStyle.LINE_STYLE_DOT;
				break;
			case MODIFIED:
				color = new RGB(255, 0, 255);
				lineStyle = IHasLineStyle.LINE_STYLE_SOLID;
				break;
			case UNMODIFIED:
				return;
			case DETACHED:
				return;
			default:
				throw new IllegalArgumentException();
			}

			changeAnnotation = new MappingAssembly(model, parentThing, changeStatus);
			changeAnnotation.getMappingThing().setColor(color);
			changeAnnotation.getMappingThing().setLineWidth(lineWidth);
			changeAnnotation.getMappingThing().setLineStyle(lineStyle);

			MirrorValueLogic.mirrorValue(glassThing, IHasAnchorPoint.ANCHOR_POINT_PROPERTY_NAME, changeAnnotation.getMappingGlassThing());
			MirrorValueLogic.mirrorValue(glassThing, IHasInternalWorldEndpoint.INTERNAL_ENDPOINT_PROPERTY_NAME, changeAnnotation.getMappingGlassThing());
			MirrorValueLogic.mirrorValue(glassThing, IHasInternalWorldEndpoint.INTERNAL_ENDPOINT_WORLD_THING_ID_PROPERTY_NAME, changeAnnotation.getMappingGlassThing());
		}
	}

	void updateAttributes(IBNAModel model, IThing thing, boolean visible, boolean editable){
		if(thing != null){
			thing.setProperty(IBNAView.HIDE_THING_PROPERTY_NAME, !visible);
			thing.setProperty(IBNAView.BACKGROUND_THING_PROPERTY_NAME, !editable);
			if(!editable){
				if(thing instanceof IHasSelected){
					thing.setProperty(IHasSelected.SELECTED_PROPERTY_NAME, false);
				}
			}

			IAssembly assembly = AssemblyUtils.getAssemblyWithRoot(thing);
			if(assembly != null){
				for(IThing t: assembly.getParts()){
					if(t.getProperty(XARCH_ID_PROPERTY_NAME) == null){
						updateAttributes(model, t, visible, editable);
					}
				}
			}
		}
	}
}
