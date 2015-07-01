package edu.uci.isr.bna4.things.shapes;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAComposite;
import edu.uci.isr.bna4.BNARenderingSettings;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.ICoordinateMapper;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ResourceUtils;

public class EllipseThingPeer
	extends AbstractThingPeer{

	protected EllipseThing t;

	protected Rectangle localBoundingBox = new Rectangle(0, 0, 0, 0);

	public EllipseThingPeer(IThing t){
		super(t);
		if(!(t instanceof EllipseThing)){
			throw new IllegalArgumentException("EllipseThingPeer can only peer for EllipseThing");
		}
		this.t = (EllipseThing)t;
	}

	protected void updateLocalBoundingBox(ICoordinateMapper cm){
		localBoundingBox = BNAUtils.worldToLocal(cm, BNAUtils.normalizeRectangle(t.getBoundingBox()));
	}

	@Override
	public void draw(IBNAView view, GC g){
		updateLocalBoundingBox(view.getCoordinateMapper());

		if(!g.getClipping().intersects(localBoundingBox)){
			return;
		}
		Color fg = ResourceUtils.getColor(getDisplay(), t.getColor());
		if(fg == null){
			fg = g.getDevice().getSystemColor(SWT.COLOR_GRAY);
		}

		boolean isGradientFilled = t.isGradientFilled();

		if(isGradientFilled){
			BNAComposite c = (BNAComposite)BNAUtils.getParentComposite(view);
			if(c != null){
				if(!BNARenderingSettings.getDecorativeGraphics(c)){
					isGradientFilled = false;
				}
			}
		}

		Color bg = null;
		if(isGradientFilled){
			bg = ResourceUtils.getColor(getDisplay(), t.getSecondaryColor());
			if(bg == null){
				bg = g.getDevice().getSystemColor(SWT.COLOR_DARK_GRAY);
			}
		}

		if(!isGradientFilled){
			g.setBackground(fg);
			g.fillOval(localBoundingBox.x, localBoundingBox.y, localBoundingBox.width, localBoundingBox.height);
		}
		else{
			g.setForeground(fg);
			g.setBackground(bg);
			Pattern pattern = new Pattern(g.getDevice(), localBoundingBox.x, localBoundingBox.y, localBoundingBox.x, localBoundingBox.y + localBoundingBox.height, fg, bg);
			g.setBackgroundPattern(pattern);
			g.fillOval(localBoundingBox.x, localBoundingBox.y, localBoundingBox.width, localBoundingBox.height);
			g.setForegroundPattern(null);
			pattern.dispose();
		}
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		return BNAUtils.isWithin(t.getBoundingBox(), worldX, worldY);
	}
}
