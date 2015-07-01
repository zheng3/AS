package edu.uci.isr.bna4.assemblies;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.IThingListener;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.logics.coordinating.MirrorBoundingBoxLogic;
import edu.uci.isr.bna4.things.borders.EllipseBorderThing;
import edu.uci.isr.bna4.things.glass.BoxGlassThing;
import edu.uci.isr.bna4.things.shapes.EllipseThing;
import edu.uci.isr.bna4.things.shapes.SplineThing;

public class ActorAssembly
	extends AbstractAssembly{

	public static final String GLASS = "glass";
	public static final String HEAD_BACKGROUND = "headBackground";
	public static final String HEAD_BORDER = "headBorder";
	public static final String BODY = "body";
	public static final String ARMS = "arms";
	public static final String LEG1 = "leg1";
	public static final String LEG2 = "leg2";

	public ActorAssembly(IBNAModel model, IThing parentThing, Object assemblyKind){
		super(model, parentThing, assemblyKind);

		// create the parts
		SplineThing bodyThing = new SplineThing();
		model.addThing(bodyThing, rootThing);

		SplineThing armsThing = new SplineThing();
		model.addThing(armsThing, rootThing);

		SplineThing leg1Thing = new SplineThing();
		model.addThing(leg1Thing, rootThing);

		SplineThing leg2Thing = new SplineThing();
		model.addThing(leg2Thing, rootThing);

		EllipseThing headThing = new EllipseThing();
		model.addThing(headThing, rootThing);
		headThing.setColor(new RGB(255, 255, 255));

		EllipseBorderThing headBorderThing = new EllipseBorderThing();
		model.addThing(headBorderThing, headThing);

		BoxGlassThing glassThing = new BoxGlassThing();
		model.addThing(glassThing, rootThing);

		// mark the parts
		markPart(HEAD_BACKGROUND, headThing);
		markPart(HEAD_BORDER, headBorderThing);
		markPart(BODY, bodyThing);
		markPart(ARMS, armsThing);
		markPart(LEG1, leg1Thing);
		markPart(LEG2, leg2Thing);
		markPart(GLASS, glassThing);

		// organize the parts
		MirrorBoundingBoxLogic.mirrorBoundingBox(headThing, null, headBorderThing);
		glassThing.setBoundingBox(new Rectangle(0, 0, 10, 30));
		glassThing.addThingListener(new IThingListener(){

			public void thingChanged(ThingEvent thingEvent){
				if(IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME.equals(thingEvent.getPropertyName())){
					updateActor();
				}
			}
		});
		updateActor();
	}

	public BoxGlassThing getGlass(){
		return getPart(GLASS);
	}

	public EllipseThing getHead(){
		return getPart(HEAD_BACKGROUND);
	}

	public EllipseBorderThing getHeadBorder(){
		return getPart(HEAD_BORDER);
	}

	public SplineThing getBody(){
		return getPart(BODY);
	}

	public SplineThing getArms(){
		return getPart(ARMS);
	}

	public SplineThing getLeg1(){
		return getPart(LEG1);
	}

	public SplineThing getLeg2(){
		return getPart(LEG2);
	}

	protected void updateActor(){
		Rectangle r = getGlass().getBoundingBox();

		int bodyX = r.x + r.width / 2;
		int neckY = r.y + r.height / 3;
		int armsY = r.y + r.height / 2;
		int baseY = r.y + 2 * r.height / 3;

		getHead().setBoundingBox(new Rectangle(r.x, r.y, r.width, neckY - r.y));
		getBody().setEndpoints(new Point(bodyX, neckY), new Point(bodyX, baseY));
		getArms().setEndpoints(new Point(r.x, armsY), new Point(r.x + r.width, armsY));
		getLeg1().setEndpoints(new Point(bodyX, baseY), new Point(r.x, r.y + r.height));
		getLeg2().setEndpoints(new Point(bodyX, baseY), new Point(r.x + r.width, r.y + r.height));
	}
}
