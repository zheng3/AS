package edu.uci.isr.bna4.logics.coordinating;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.UserEditableUtils;
import edu.uci.isr.bna4.assemblies.AssemblyUtils;
import edu.uci.isr.bna4.assemblies.IAssembly;
import edu.uci.isr.bna4.facets.IHasAnchorPoint;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.facets.IHasIndicatorPoint;
import edu.uci.isr.bna4.facets.IHasMutableAngle;
import edu.uci.isr.bna4.facets.IHasMutableTargetThing;
import edu.uci.isr.bna4.facets.IHasText;
import edu.uci.isr.bna4.facets.IRelativeMovable;
import edu.uci.isr.bna4.logics.coordinating.MaintainStickyPointLogic.StickyMode;
import edu.uci.isr.bna4.logics.tracking.ReferenceTrackingLogic;
import edu.uci.isr.bna4.things.labels.TagThing;

public class MaintainTagsLogic
	extends AbstractMaintainThingsWithReferenceNameLogic<IThing, TagThing>{

	public static final String USER_MAY_SHOW_TAG = "userMayShowTag";

	public static final String TAG_PART_NAME = "tag";
	public static final String SHOW_TAG_PROPERTY_NAME = "showTag";

	public MaintainTagsLogic(ReferenceTrackingLogic rtl){
		super(IThing.class, new String[]{SHOW_TAG_PROPERTY_NAME, IHasText.TEXT_PROPERTY_NAME}, TagThing.class, new String[]{}, rtl, IHasMutableTargetThing.TARGET_THING_ID_PROPERTY_NAME);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void maintainAll(){
		IBNAModel model = getBNAModel();
		for(IThing sourceThing: model.getAllThings()){
			if(isSourceThing(model, sourceThing, null)){
				updateFromSource(null, sourceThing, null);
			}
		}
	}

	@Override
	protected void updateFromSource(IBNAModel sourceModel, IThing sourceThing, ThingEvent sourceThingEvent){
		IBNAModel targetModel = getBNAModel();
		if((sourceThingEvent == null || sourceThingEvent.getPropertyName().equals(SHOW_TAG_PROPERTY_NAME)) && Boolean.TRUE.equals(sourceThing.getProperty(SHOW_TAG_PROPERTY_NAME))){
			TagThing t = new TagThing();
			int delta = 20;
			Point initialPoint = t.getAnchorPoint();
			if(sourceThing instanceof IHasAnchorPoint){
				initialPoint = BNAUtils.clone(((IHasAnchorPoint)sourceThing).getAnchorPoint());
				initialPoint.x += delta;
				initialPoint.y -= delta;
			}
			else if(sourceThing instanceof IHasBoundingBox){
				Rectangle r = ((IHasBoundingBox)sourceThing).getBoundingBox();
				initialPoint = new Point(r.x + r.width / 2, r.y + r.height / 2);
				initialPoint.x += delta;
				initialPoint.y -= delta;
			}
			t.setAnchorPoint(initialPoint);
			targetModel.addThing(t, sourceThing);

			MoveWithLogic.moveWith(sourceThing, MoveWithLogic.TRACK_ANCHOR_POINT_FIRST, t);
			MaintainStickyPointLogic.stickPoint(sourceThing, IHasIndicatorPoint.INDICATOR_POINT_PROPERTY_NAME, StickyMode.CENTER, t);
			MirrorValueLogic.mirrorValue(sourceThing, IHasText.TEXT_PROPERTY_NAME, t);
			UserEditableUtils.addEditableQuality(t, IRelativeMovable.USER_MAY_MOVE, IHasMutableAngle.USER_MAY_CHANGE_ANGLE);

			IAssembly assembly = AssemblyUtils.getAssemblyWithPart(sourceThing);
			if(assembly != null){
				assembly.markPart(TAG_PART_NAME, t);
			}
		}
		if(sourceThingEvent != null && sourceThingEvent.getPropertyName().equals(SHOW_TAG_PROPERTY_NAME) && !Boolean.TRUE.equals(sourceThing.getProperty(SHOW_TAG_PROPERTY_NAME))){
			IAssembly assembly = AssemblyUtils.getAssemblyWithPart(sourceThing);
			if(assembly != null){
				IThing t = assembly.getPart(TAG_PART_NAME);
				if(t != null){
					assembly.unmarkPart(TAG_PART_NAME);
					targetModel.removeThing(t);
				}
			}
		}
		super.updateFromSource(sourceModel, sourceThing, sourceThingEvent);
	}

	@Override
	protected void maintain(IBNAModel sourceModel, IThing sourceThing, TagThing targetThing, ThingEvent thingEvent){
	}
}
