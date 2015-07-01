package edu.uci.isr.bna4.things.glass;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.PathData;
import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.PathDataUtils;

public class PathGlassThingPeer
	extends AbstractThingPeer{

	protected PathGlassThing t;

	public PathGlassThingPeer(IThing t){
		super(t);
		this.t = (PathGlassThing)t;
	}

	@Override
	public void draw(IBNAView view, final GC g){
		if(t.isSelected()){
			Path path = null;
			try{
				path = new Path(g.getDevice());
				PathData pathData = t.getPathData();
				Point o = t.getAnchorPoint();
				PathDataUtils.addPathDataToPath(path, pathData, o, view.getCoordinateMapper());

				if(g.getClipping().intersects(PathDataUtils.getBounds(path))){
					int offset = t.getRotatingOffset();

					final Path fPath = path;
					BNAUtils.drawMarquee(g, offset, true, new Runnable(){

						public void run(){
							g.drawPath(fPath);
						};
					});
				}
			}
			finally{
				if(path != null){
					path.dispose();
					path = null;
				}
			}
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
