package edu.uci.isr.bna4.things.shapes;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAComposite;
import edu.uci.isr.bna4.BNARenderingSettings;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.ICoordinateMapper;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ResourceUtils;

public class PolygonThingPeer
	extends AbstractThingPeer{

	protected PolygonThing t;

	public PolygonThingPeer(IThing t){
		super(t);
		this.t = (PolygonThing)t;
	}

	@Override
	public void draw(IBNAView view, GC g){
		ICoordinateMapper cm = view.getCoordinateMapper();
		Rectangle localBoundingBox = t.getBoundingBox();
		BNAUtils.convertWorldToLocal(cm, localBoundingBox);
		if(!g.getClipping().intersects(localBoundingBox)){
			return;
		}

		Point[] points = t.getPoints();
		int[] localXYArray = new int[2 * points.length];
		for(int i = 0; i < points.length; i++){
			localXYArray[2 * i] = points[i].x;
			localXYArray[2 * i + 1] = points[i].y;
		}
		BNAUtils.convertWorldToLocal(cm, localXYArray);

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
				bg = g.getDevice().getSystemColor(SWT.COLOR_WHITE);
			}
		}
		if(!isGradientFilled){
			g.setBackground(fg);
			g.fillPolygon(localXYArray);
		}
		else{
			g.setForeground(fg);
			g.setBackground(bg);
			Pattern pattern = new Pattern(g.getDevice(), localBoundingBox.x, localBoundingBox.y, localBoundingBox.x, localBoundingBox.y + localBoundingBox.height, fg, bg);
			g.setBackgroundPattern(pattern);
			g.fillPolygon(localXYArray);
			g.setForegroundPattern(null);
			pattern.dispose();
		}
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		// TODO Auto-generated method stub
		return false;
	}
}
