package edu.uci.isr.bna4;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.SplineUtils.SplineData;
import edu.uci.isr.bna4.facets.IHasMutableBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutableEndpoints;
import edu.uci.isr.bna4.facets.IHasMutableMidpoints;
import edu.uci.isr.bna4.facets.IHasStickyEndpoints;
import edu.uci.isr.bna4.facets.IRelativeMovable;
import edu.uci.isr.bna4.things.utility.GridThing;

public class GridUtils{

	private GridUtils(){
	}

	public static void rectifyToGrid(IBNAModel m){
		int gridSpacing = GridUtils.getGridSpacing(m);
		if(gridSpacing == 0){
			return;
		}

		for(IThing t: m.getAllThings()){
			if(t instanceof IHasMutableEndpoints){
				rectifyToGrid(gridSpacing, (IHasMutableEndpoints)t);
			}
			if(t instanceof IHasMutableMidpoints){
				rectifyToGrid(gridSpacing, (IHasMutableMidpoints)t);
			}
			if(t instanceof IHasMutableBoundingBox){
				rectifyToGrid(gridSpacing, (IHasMutableBoundingBox)t);
			}
			else if(t instanceof IRelativeMovable){
				rectifyToGrid(gridSpacing, (IRelativeMovable)t);
			}
		}
	}

	public static void rectifyToGrid(int gridSpacing, IRelativeMovable t){
		Point p = t.getReferencePoint();
		p = GridUtils.snapToGrid(gridSpacing, p);
		t.setReferencePoint(p);
	}

	public static void rectifyToGrid(int gridSpacing, IHasMutableBoundingBox t){
		Rectangle r = t.getBoundingBox();

		if(UserEditableUtils.isEditableForAllQualities(t, IRelativeMovable.USER_MAY_MOVE)){
			r.x = GridUtils.snapToGrid(gridSpacing, r.x);
			r.y = GridUtils.snapToGrid(gridSpacing, r.y);
		}
		if(UserEditableUtils.isEditableForAllQualities(t, IHasMutableBoundingBox.USER_MAY_RESIZE)){
			int x1 = r.x;
			int y1 = r.y;
			int x2 = r.x + r.width;
			int y2 = r.y + r.height;

			int nx1 = GridUtils.snapToGrid(gridSpacing, x1);
			int ny1 = GridUtils.snapToGrid(gridSpacing, y1);
			int nx2 = GridUtils.snapToGrid(gridSpacing, x2);
			int ny2 = GridUtils.snapToGrid(gridSpacing, y2);

			if(nx1 == nx2){
				nx2 += gridSpacing;
			}
			if(ny1 == ny2){
				ny2 += gridSpacing;
			}

			r.width = nx2 - nx1;
			r.height = ny2 - ny1;
		}

		t.setBoundingBox(r);
	}

	public static void rectifyToGrid(int gridSpacing, IHasMutableEndpoints t){
		Point ep1 = t.getEndpoint1();
		Point nep1 = GridUtils.snapToGrid(gridSpacing, new Point(ep1.x, ep1.y));

		Point ep2 = t.getEndpoint2();
		Point nep2 = GridUtils.snapToGrid(gridSpacing, new Point(ep2.x, ep2.y));

		if(nep1.x == nep2.x && nep1.y == nep2.y){
			nep2.x += gridSpacing;
			nep2.y += gridSpacing;
		}

		if(t instanceof IHasStickyEndpoints){
			if(((IHasStickyEndpoints)t).getEndpoint1StuckToThingID() == null){
				t.setEndpoint1(nep1);
			}
			if(((IHasStickyEndpoints)t).getEndpoint2StuckToThingID() == null){
				t.setEndpoint2(nep2);
			}
		}
		else{
			t.setEndpoint1(nep1);
			t.setEndpoint2(nep2);
		}
	}

	public static void rectifyToGrid(int gridSpacing, IHasMutableMidpoints t){
		SplineData sd = SplineUtils.getPoints(t);
		List<Point> newPoints = new ArrayList<Point>();

		Point lastPoint = null;
		for(Point p: sd.allPoints){
			p.x = GridUtils.snapToGrid(gridSpacing, p.x);
			p.y = GridUtils.snapToGrid(gridSpacing, p.y);
			if(lastPoint == null || !lastPoint.equals(p)){
				newPoints.add(p);
			}
			lastPoint = p;
		}

		sd.allPoints = newPoints;
		t.setMidpoints(sd.getMidpoints().toArray(new Point[0]));
	}

	public static GridThing getGridThing(IBNAModel m){
		for(IThing t: m.getAllThings()){
			if(t instanceof GridThing){
				return (GridThing)t;
			}
		}
		GridThing gt = new GridThing();
		m.addThing(gt);
		m.sendToBack(gt);
		return gt;
	}

	public static int getGridSpacing(IBNAModel m){
		GridThing gt = getGridThing(m);
		if(gt != null){
			return gt.getGridSpacing();
		}
		return 0;
	}

	public static Point snapToGrid(int gridSpacing, Point p){
		if(gridSpacing == 0){
			return p;
		}
		return new Point(snapToGrid(gridSpacing, p.x), snapToGrid(gridSpacing, p.y));
	}

	public static int snapToGrid(int gridSpacing, int coord){
		int diff = coord % gridSpacing;

		if(diff < gridSpacing / 2){
			return coord - diff;
		}
		else{
			return coord + gridSpacing - diff;
		}
	}
}
