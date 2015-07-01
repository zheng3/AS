package edu.uci.isr.bna4.things.utility;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.DefaultBNAView;
import edu.uci.isr.bna4.DefaultCoordinateMapper;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IBNAWorld;
import edu.uci.isr.bna4.ICoordinateMapper;
import edu.uci.isr.bna4.IMutableCoordinateMapper;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.IThingPeer;
import edu.uci.isr.bna4.logics.tracking.ModelBoundsTrackingLogic;

public class WorldThingPeer
    extends AbstractThingPeer
    implements IThingPeer{

	protected WorldThing lt;
	protected IBNAView innerView = null;

	protected boolean localBoundingBoxChanged = false;
	protected Rectangle localBoundingBox = BNAUtils.NONEXISTENT_RECTANGLE;

	public WorldThingPeer(IThing t){
		super(t);
		if(!(t instanceof WorldThing)){
			throw new IllegalArgumentException("WorldThingPeer can only peer for WorldThing");
		}
		this.lt = (WorldThing)t;
	}

	public IBNAView getInnerView(){
		return innerView;
	}

	protected void updateLocalBoundingBox(ICoordinateMapper cm){
		Rectangle lastLocalBoundingBox = localBoundingBox;
		localBoundingBox = BNAUtils.worldToLocal(cm, BNAUtils.normalizeRectangle(lt.getBoundingBox()));
		if(!lastLocalBoundingBox.equals(localBoundingBox)){
			localBoundingBoxChanged = true;
		}
	}

	protected Rectangle lastModelBounds = BNAUtils.NONEXISTENT_RECTANGLE;
	protected Rectangle clip = new Rectangle(0, 0, 0, 0);

	@Override
	public void draw(IBNAView view, GC g){
		IBNAWorld innerWorld = lt.getWorld();
		if(innerWorld == null){
			return;
		}

		if(innerView != null && !innerWorld.equals(innerView.getWorld())){
			innerView = null;
		}

		if(innerView == null){
			innerView = new DefaultBNAView(view, lt.getWorld(), new DefaultCoordinateMapper());
		}

		updateLocalBoundingBox(view.getCoordinateMapper());
		if(localBoundingBox.height < 5 || localBoundingBox.width < 5){
			return;
		}

		Rectangle modelBounds = ModelBoundsTrackingLogic.getModelBounds(innerView.getWorld());
		if(modelBounds == null){
			return;
		}
		if(modelBounds.x == Integer.MIN_VALUE || modelBounds.width == 0 || modelBounds.height == 0){
			modelBounds.x = innerView.getCoordinateMapper().getWorldCenterX();
			modelBounds.y = innerView.getCoordinateMapper().getWorldCenterY();
			modelBounds.width = 100;
			modelBounds.height = 100;
		}

		if(localBoundingBoxChanged || !BNAUtils.nulleq(modelBounds, lastModelBounds)){
			ICoordinateMapper cm = innerView.getCoordinateMapper();
			if(cm instanceof IMutableCoordinateMapper){
				IMutableCoordinateMapper mcm = (IMutableCoordinateMapper)cm;

				double sx = (double)localBoundingBox.width / (double)modelBounds.width;
				double sy = (double)localBoundingBox.height / (double)modelBounds.height;
				double s = Math.min(sx, sy);
				mcm.rescaleAbsolute(s);

				double ddx = s == sx ? 0.0d : (localBoundingBox.width / s - modelBounds.width) / 2.0d;
				double ddy = s == sy ? 0.0d : (localBoundingBox.height / s - modelBounds.height) / 2.0d;

				int dx = BNAUtils.round(ddx);
				int dy = BNAUtils.round(ddy);

				mcm.repositionAbsolute(modelBounds.x - BNAUtils.round(localBoundingBox.x / s) - dx, modelBounds.y - BNAUtils.round(localBoundingBox.y / s) - dy);
			}
		}
		localBoundingBoxChanged = false;
		lastModelBounds = modelBounds;

		clip.x = localBoundingBox.x;
		clip.y = localBoundingBox.y;
		clip.width = localBoundingBox.width + 1;
		clip.height = localBoundingBox.height + 1;
		g.setClipping(clip);
		IThing[] things = innerView.getWorld().getBNAModel().getAllThings();
		for(int i = 0; i < things.length; i++){
			if(!Boolean.TRUE.equals(things[i].getProperty(IBNAView.HIDE_THING_PROPERTY_NAME))){
				IThingPeer peer = innerView.getPeer(things[i]);
				peer.draw(innerView, g);
			}
		}
		g.setClipping((Rectangle)null);
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		IBNAWorld innerWorld = lt.getWorld();
		if(innerWorld == null){
			return false;
		}

		if(innerView == null){
			innerView = new DefaultBNAView(view, lt.getWorld(), new DefaultCoordinateMapper());
		}

		Rectangle boundingBox = lt.getBoundingBox();
		if(boundingBox != null){
			return boundingBox.contains(worldX, worldY);
		}
		return false;
	}
}
