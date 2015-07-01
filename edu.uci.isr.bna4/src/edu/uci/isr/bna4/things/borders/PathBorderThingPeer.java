package edu.uci.isr.bna4.things.borders;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.PathData;
import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.PathDataUtils;
import edu.uci.isr.bna4.ResourceUtils;

public class PathBorderThingPeer
	extends AbstractThingPeer{

	protected PathBorderThing t;

	public PathBorderThingPeer(IThing t){
		super(t);
		this.t = (PathBorderThing)t;
	}

	@Override
	public void draw(IBNAView view, GC g){
		Color fg = ResourceUtils.getColor(getDisplay(), t.getColor());
		if(fg == null){
			fg = g.getDevice().getSystemColor(SWT.COLOR_BLACK);
		}

		g.setLineStyle(t.getLineStyle());
		g.setLineWidth(t.getLineWidth());
		g.setForeground(fg);

		Path path = null;
		try{
			path = new Path(g.getDevice());
			PathData pathData = t.getPathData();
			Point o = t.getAnchorPoint();
			PathDataUtils.addPathDataToPath(path, pathData, o, view.getCoordinateMapper());
			g.drawPath(path);
		}
		finally{
			if(path != null){
				path.dispose();
				path = null;
			}
		}

		g.setLineStyle(SWT.LINE_SOLID);
		g.setLineWidth(1);
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
			return path.contains(worldX, worldY, g, true);
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
