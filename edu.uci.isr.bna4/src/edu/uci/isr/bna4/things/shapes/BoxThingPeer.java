package edu.uci.isr.bna4.things.shapes;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAComposite;
import edu.uci.isr.bna4.BNARenderingSettings;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ResourceUtils;

public class BoxThingPeer
	extends AbstractThingPeer{

	protected BoxThing t;

	public BoxThingPeer(IThing t){
		super(t);
		this.t = (BoxThing)t;
	}

	@Override
	public void draw(IBNAView view, GC g){
		Rectangle r = t.getBoundingBox();
		Rectangle lr = BNAUtils.worldToLocal(view.getCoordinateMapper(), r);
		if(g.getClipping().intersects(lr)){

			boolean isGradientFilled = t.isGradientFilled();
			if(isGradientFilled){
				BNAComposite c = (BNAComposite)BNAUtils.getParentComposite(view);
				if(c != null){
					if(!BNARenderingSettings.getDecorativeGraphics(c)){
						isGradientFilled = false;
					}
				}
			}

			Point lc = BNAUtils.worldToLocal(view.getCoordinateMapper(), r, t.getCornerWidth(), t.getCornerHeight());
			if(lc.x > 0 && lc.y > 0){
				Pattern pattern = null;
				try{
					if(isGradientFilled){
						g.setForeground(ResourceUtils.getColor(getDisplay(), t.getColor()));
						g.setBackground(ResourceUtils.getColor(getDisplay(), t.getSecondaryColor()));
						pattern = new Pattern(g.getDevice(), lr.x, lr.y, lr.x, lr.y + lr.height, g.getForeground(), g.getBackground());
						g.setBackgroundPattern(pattern);
					}
					else{
						if(t.featureSelected){
							g.setBackground(ResourceUtils.getColor(getDisplay(), t.featureColor));	
						}else{
						g.setBackground(ResourceUtils.getColor(getDisplay(), t.getColor()));
						}
					}
					g.fillRoundRectangle(lr.x, lr.y, lr.width, lr.height, lc.x, lc.y);
				}
				finally{
					if(pattern != null){
						pattern.dispose();
						pattern = null;
						g.setBackgroundPattern(null);
					}
				}
			}
			else{
				if(isGradientFilled){
					g.setForeground(ResourceUtils.getColor(getDisplay(), t.getColor()));
					g.setBackground(ResourceUtils.getColor(getDisplay(), t.getSecondaryColor()));
					g.fillGradientRectangle(lr.x, lr.y, lr.width, lr.height, true);
				}
				else{
					if(t.featureSelected){
						g.setBackground(ResourceUtils.getColor(getDisplay(), t.featureColor));	
					}else{
					g.setBackground(ResourceUtils.getColor(getDisplay(), t.getColor()));
					}
					g.fillRectangle(lr.x, lr.y, lr.width, lr.height);
				}
			}
		}
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		return false;
	}
}
