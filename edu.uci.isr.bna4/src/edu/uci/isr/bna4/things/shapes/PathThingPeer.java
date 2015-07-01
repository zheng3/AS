package edu.uci.isr.bna4.things.shapes;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.PathData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.PathDataUtils;
import edu.uci.isr.bna4.ResourceUtils;

public class PathThingPeer extends AbstractThingPeer{

	protected PathThing t;

	public PathThingPeer(IThing t){
		super(t);
		this.t = (PathThing)t;
	}

	@Override
	public void draw(IBNAView view, GC g){
		Path path = null;
		Region region = null;
		try{
			Color fg = ResourceUtils.getColor(getDisplay(), t.getColor());
			if(fg == null){
				fg = g.getDevice().getSystemColor(SWT.COLOR_GRAY);
			}

			path = new Path(g.getDevice());
			PathData pathData = t.getPathData();
			Point o = t.getAnchorPoint();
			PathDataUtils.addPathDataToPath(path, pathData, o, view.getCoordinateMapper());
			
			boolean isGradientFilled = t.isGradientFilled();
			if(!isGradientFilled){
				g.setBackground(fg);
				g.fillPath(path);
			}
			else{
				Color bg = ResourceUtils.getColor(getDisplay(), t.getSecondaryColor());
				if(bg == null){
					bg = g.getDevice().getSystemColor(SWT.COLOR_DARK_GRAY);
				}

				g.setForeground(fg);
				g.setBackground(bg);

				region = new Region();
				g.getClipping(region);
				g.setClipping(path);

				Rectangle b = region.getBounds();
				g.fillGradientRectangle(b.x - 1, b.y - 1, b.width + 2, b.height + 2, true);

				g.setClipping(region);
			}
		}
		finally{
			if(path != null){
				path.dispose();
				path = null;
			}

			if(region != null)
				region.dispose();
			region = null;
		}
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		GC g = null;
		Path path = null;
		try{
			g = new GC(getDisplay());
			path = new Path(g.getDevice());
			PathData pathData = t.getPathData();
			Point o = t.getAnchorPoint();
			PathDataUtils.addPathDataToPath(path, pathData, o, view.getCoordinateMapper());
			return path.contains(worldX, worldY, g, false);
		}
		finally{
			if(path != null){
				path.dispose();
				path = null;
			}
			if(g != null){
				g.dispose();
				g = null;
			}
		}
	}
}
