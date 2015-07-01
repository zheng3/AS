package edu.uci.isr.bna4;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

import edu.uci.isr.bna4.constants.GridDisplayType;
import edu.uci.isr.bna4.facets.IHasAnchorPoint;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.facets.IHasSelected;
import edu.uci.isr.bna4.facets.IHasWorld;
import edu.uci.isr.bna4.things.utility.EnvironmentPropertiesThing;
import edu.uci.isr.bna4.things.utility.GridThing;
import edu.uci.isr.bna4.things.utility.WorldThingPeer;
import edu.uci.isr.sysutils.UIDGenerator;
import edu.uci.isr.widgets.swt.SWTWidgetUtils;
import edu.uci.isr.widgets.swt.constants.Orientation;
public class BNAUtils{

	public static final Rectangle NONEXISTENT_RECTANGLE = new Rectangle(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);

	public static final int round(double d){
		return (int)Math.round(d);
	}

	public static final int round(float f){
		return Math.round(f);
	}

	public static Rectangle normalizeRectangle(Rectangle r){
		if(r.width >= 0 && r.height >= 0){
			return r;
		}

		Rectangle normalizedRect = new Rectangle(0, 0, 0, 0);
		normalizedRect.x = r.x;
		normalizedRect.y = r.y;

		if(r.width >= 0){
			normalizedRect.width = r.width;
		}
		else{
			normalizedRect.width = -r.width;
			normalizedRect.x = r.x + r.width;
		}

		if(r.height >= 0){
			normalizedRect.height = r.height;
		}
		else{
			normalizedRect.height = -r.height;
			normalizedRect.y = r.y + r.height;
		}
		return normalizedRect;
	}

	public static int bound(int p1, int p, int p2){
		if(p1 < p2){
			if(p < p1){
				return p1;
			}
			if(p > p2){
				return p2;
			}
		}
		else{
			if(p < p2){
				return p2;
			}
			if(p > p1){
				return p1;
			}
		}
		return p;
	}

	public static boolean isInBound(int p1, int p, int p2){
		if(p1 < p2){
			if(p < p1){
				return false;
			}
			if(p > p2){
				return false;
			}
		}
		else{
			if(p < p2){
				return false;
			}
			if(p > p1){
				return false;
			}
		}
		return true;
	}

	public static String generateUID(String prefix){
		return UIDGenerator.generateUID(prefix);
	}

	public static boolean isWithin(Rectangle outsideRect, int x, int y){
		Rectangle out = normalizeRectangle(outsideRect);
		int x1 = out.x;
		int x2 = out.x + out.width;
		int y1 = out.y;
		int y2 = out.y + out.height;

		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}

	public static boolean isWithin(Rectangle outsideRect, Rectangle insideRect){
		Rectangle in = normalizeRectangle(insideRect);

		return isWithin(outsideRect, in.x, in.y) && isWithin(outsideRect, in.x + in.width, in.y) && isWithin(outsideRect, in.x, in.y + in.height) && isWithin(outsideRect, in.x + in.width, in.y + in.height);
	}

	public static boolean nulleq(Object o1, Object o2){
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	public static void drawMarqueeRectangle(GC g, Rectangle r, int o){
		int[] oldDash = g.getLineDash();

		o = o % 6;
		g.setLineWidth(1);
		g.setLineDash(new int[]{1, 5});

		g.setForeground(g.getDevice().getSystemColor(SWT.COLOR_WHITE));
		if(r.width > o){
			g.drawLine(r.x + o, r.y, r.x + r.width, r.y);
			g.drawLine(r.x + r.width - o, r.y + r.height, r.x, r.y + r.height);
		}
		if(r.height > o){
			g.drawLine(r.x + r.width, r.y + o, r.x + r.width, r.y + r.height);
			g.drawLine(r.x, r.y + r.height - o, r.x, r.y);
		}

		o = (o + 1) % 6;
		g.setForeground(g.getDevice().getSystemColor(SWT.COLOR_GRAY));
		if(r.width > o){
			g.drawLine(r.x + o, r.y, r.x + r.width, r.y);
			g.drawLine(r.x + r.width - o, r.y + r.height, r.x, r.y + r.height);
		}
		if(r.height > o){
			g.drawLine(r.x + r.width, r.y + o, r.x + r.width, r.y + r.height);
			g.drawLine(r.x, r.y + r.height - o, r.x, r.y);
		}

		o = (o + 1) % 6;
		g.setForeground(g.getDevice().getSystemColor(SWT.COLOR_DARK_GRAY));
		if(r.width > o){
			g.drawLine(r.x + o, r.y, r.x + r.width, r.y);
			g.drawLine(r.x + r.width - o, r.y + r.height, r.x, r.y + r.height);
		}
		if(r.height > o){
			g.drawLine(r.x + r.width, r.y + o, r.x + r.width, r.y + r.height);
			g.drawLine(r.x, r.y + r.height - o, r.x, r.y);
		}

		o = (o + 1) % 6;
		g.setForeground(g.getDevice().getSystemColor(SWT.COLOR_BLACK));
		if(r.width > o){
			g.drawLine(r.x + o, r.y, r.x + r.width, r.y);
			g.drawLine(r.x + r.width - o, r.y + r.height, r.x, r.y + r.height);
		}
		if(r.height > o){
			g.drawLine(r.x + r.width, r.y + o, r.x + r.width, r.y + r.height);
			g.drawLine(r.x, r.y + r.height - o, r.x, r.y);
		}

		g.setLineDash(oldDash);
	}

	public static void drawMarquee(GC g, int[] p, int o){
		int[] oldDash = g.getLineDash();

		o = o % 6;
		g.setLineWidth(1);

		// true marquee outline paths are not possible at this point
		// because swt does not allow a dashed line to start out with 0
		// in other words, it's not possible to start a dashed line
		// with empty space

		for(int i = 5; i >= 0; i--){
			Color c = null;
			switch((6 + i - o) % 6){
			case 0:
				c = g.getDevice().getSystemColor(SWT.COLOR_WHITE);
				break;
			case 1:
				c = g.getDevice().getSystemColor(SWT.COLOR_GRAY);
				break;
			case 2:
				c = g.getDevice().getSystemColor(SWT.COLOR_DARK_GRAY);
				break;
			case 3:
				c = g.getDevice().getSystemColor(SWT.COLOR_BLACK);
				break;
			case 4:
				c = g.getDevice().getSystemColor(SWT.COLOR_BLACK);
				break;
			case 5:
				c = g.getDevice().getSystemColor(SWT.COLOR_BLACK);
				break;
			}

			if(i == 5){
				g.setLineDash(null);
			}
			else{
				g.setLineDash(new int[]{g.getLineWidth() * (i + 1), g.getLineWidth() * (5 - i)});
			}

			g.setForeground(c);
			g.drawPolyline(p);
		}

		g.setLineDash(null);
	}

	public static final Point worldToLocal(ICoordinateMapper cm, Point worldPoint){
		if(worldPoint != null){
			return new Point(cm.worldXtoLocalX(worldPoint.x), cm.worldYtoLocalY(worldPoint.y));
		}
		return null;
	}

	public static final Point[] worldToLocal(ICoordinateMapper cm, Point[] worldPoints){
		Point[] localPoints = new Point[worldPoints.length];
		for(int i = 0; i < worldPoints.length; i++){
			localPoints[i] = worldToLocal(cm, worldPoints[i]);
		}
		return localPoints;
	}

	public static final Rectangle worldToLocal(ICoordinateMapper cm, Rectangle worldRectangle){
		Rectangle localRectangle = new Rectangle(cm.worldXtoLocalX(worldRectangle.x), cm.worldYtoLocalY(worldRectangle.y), 0, 0);
		localRectangle.width = cm.worldXtoLocalX(worldRectangle.x + worldRectangle.width) - localRectangle.x;
		localRectangle.height = cm.worldYtoLocalY(worldRectangle.y + worldRectangle.height) - localRectangle.y;
		return localRectangle;
	}

	public static Point worldToLocal(ICoordinateMapper cm, Rectangle worldRectangle, int worldCornerWidth, int worldCornerHeight){
		Rectangle lc = new Rectangle(worldRectangle.x, worldRectangle.y, Math.min(worldCornerWidth, worldRectangle.width), Math.min(worldCornerHeight, worldRectangle.height));
		convertWorldToLocal(cm, lc);
		return new Point(lc.width, lc.height);
	}

	public static final Point localToWorld(ICoordinateMapper cm, Point localPoint){
		return new Point(cm.localXtoWorldX(localPoint.x), cm.localYtoWorldY(localPoint.y));
	}

	public static final Point[] localToWorld(ICoordinateMapper cm, Point[] localPoints){
		Point[] worldPoints = new Point[localPoints.length];
		for(int i = 0; i < localPoints.length; i++){
			worldPoints[i] = localToWorld(cm, localPoints[i]);
		}
		return worldPoints;
	}

	public static final Rectangle localToWorld(ICoordinateMapper cm, Rectangle localRectangle){
		Rectangle worldRectangle = new Rectangle(cm.localXtoWorldX(localRectangle.x), cm.localYtoWorldY(localRectangle.y), 0, 0);
		worldRectangle.width = cm.localXtoWorldX(localRectangle.x + localRectangle.width) - worldRectangle.x;
		worldRectangle.height = cm.localYtoWorldY(localRectangle.y + localRectangle.height) - worldRectangle.y;
		return worldRectangle;
	}

	public static final void convertWorldToLocal(ICoordinateMapper cm, Point point){
		point.x = cm.worldXtoLocalX(point.x);
		point.y = cm.worldYtoLocalY(point.y);
	}

	public static final void convertWorldToLocal(ICoordinateMapper cm, Point[] points){
		for(Point point: points){
			point.x = cm.worldXtoLocalX(point.x);
			point.y = cm.worldYtoLocalY(point.y);
		}
	}

	public static final void convertWorldToLocal(ICoordinateMapper cm, int[] xyArray){
		for(int i = 0; i + 1 < xyArray.length; i += 2){
			xyArray[i] = cm.worldXtoLocalX(xyArray[i]);
			xyArray[i + 1] = cm.worldYtoLocalY(xyArray[i + 1]);
		}
	}

	public static final void convertWorldToLocal(ICoordinateMapper cm, Rectangle r){
		int x2 = r.x + r.width;
		int y2 = r.y + r.height;

		r.x = cm.worldXtoLocalX(r.x);
		r.y = cm.worldYtoLocalY(r.y);
		r.width = cm.worldXtoLocalX(x2) - r.x;
		r.height = cm.worldYtoLocalY(y2) - r.y;
	}

	public static final void convertLocalToWorld(ICoordinateMapper cm, Point point){
		point.x = cm.localXtoWorldX(point.x);
		point.y = cm.localYtoWorldY(point.y);
	}

	public static final void convertLocalToWorld(ICoordinateMapper cm, Point[] points){
		for(Point point: points){
			point.x = cm.localXtoWorldX(point.x);
			point.y = cm.localYtoWorldY(point.y);
		}
	}

	public static final void convertLocalToWorld(ICoordinateMapper cm, Rectangle r){
		int x2 = r.x + r.width;
		int y2 = r.y + r.height;

		r.x = cm.localXtoWorldX(r.x);
		r.y = cm.localYtoWorldY(r.y);
		r.width = cm.localXtoWorldX(x2) - r.x;
		r.height = cm.localYtoWorldY(y2) - r.y;
	}

	public static boolean wasControlPressed(MouseEvent evt){
		return (evt.stateMask & SWT.CONTROL) != 0;
	}

	public static boolean wasShiftPressed(MouseEvent evt){
		return (evt.stateMask & SWT.SHIFT) != 0;
	}

	public static boolean wasClick(MouseEvent downEvt, MouseEvent upEvt){
		if(downEvt.button == upEvt.button){
			int dx = upEvt.x - downEvt.x;
			int dy = upEvt.y - downEvt.y;
			if(dx == 0 && dy == 0){
				return true;
			}
		}
		return false;
	}

	public static void setRectangle(Rectangle r, int x, int y, int width, int height){
		r.x = x;
		r.y = y;
		r.width = width;
		r.height = height;
	}

	public static Rectangle createAlignedRectangle(Point p, int width, int height, Orientation o){
		Rectangle r = new Rectangle(0, 0, 0, 0);
		alignRectangle(r, p, width, height, o);
		return r;
	}

	public static void alignRectangle(Rectangle r, Point p, int width, int height, Orientation o){
		switch(o){
		case NONE:
			setRectangle(r, p.x - width / 2, p.y - height / 2, width, height);
			break;
		case NORTHWEST:
			setRectangle(r, p.x - width, p.y - height, width, height);
			break;
		case NORTH:
			setRectangle(r, p.x - width / 2, p.y - height, width, height);
			break;
		case NORTHEAST:
			setRectangle(r, p.x, p.y - height, width, height);
			break;
		case EAST:
			setRectangle(r, p.x, p.y - height / 2, width, height);
			break;
		case SOUTHEAST:
			setRectangle(r, p.x, p.y, width, height);
			break;
		case SOUTH:
			setRectangle(r, p.x - width / 2, p.y, width, height);
			break;
		case SOUTHWEST:
			setRectangle(r, p.x - width, p.y, width, height);
			break;
		case WEST:
			setRectangle(r, p.x - width, p.y - height / 2, width, height);
			break;
		}
	}

	private static float[] deg2rad = null;

	public static float degreesToRadians(int degrees){
		while(degrees < 0){
			degrees += 360;
		}
		degrees = degrees % 360;
		if(deg2rad == null){
			deg2rad = new float[360];
			for(int i = 0; i < 360; i++){
				deg2rad[i] = i * (float)Math.PI / 180f;
			}
		}
		return deg2rad[degrees];
	}

	public static Point[] toPointArray(int[] points){
		Point[] pa = new Point[points.length / 2];
		for(int i = 0; i < points.length; i++){
			pa[i] = new Point(points[i / 2], points[i / 2 + 1]);
		}
		return pa;
	}

	public static int[] createIsocolesTriangle(Rectangle boundingBox, Orientation facing){
		int x1, x2, x3;
		int y1, y2, y3;

		switch(facing){
		case NORTHWEST:
			x1 = boundingBox.x;
			y1 = boundingBox.y + boundingBox.height;
			x2 = boundingBox.x;
			y2 = boundingBox.y;
			x3 = boundingBox.x + boundingBox.width;
			y3 = boundingBox.y;
			break;
		case NORTH:
			x1 = boundingBox.x + boundingBox.width / 2;
			y1 = boundingBox.y;
			x2 = boundingBox.x;
			y2 = boundingBox.y + boundingBox.height;
			x3 = boundingBox.x + boundingBox.width;
			y3 = boundingBox.y + boundingBox.height;
			break;
		case NORTHEAST:
			x1 = boundingBox.x;
			y1 = boundingBox.y;
			x2 = boundingBox.x + boundingBox.width;
			y2 = boundingBox.y;
			x3 = boundingBox.x + boundingBox.width;
			y3 = boundingBox.y + boundingBox.height;
			break;
		case EAST:
			x1 = boundingBox.x + boundingBox.width;
			y1 = boundingBox.y + boundingBox.height / 2;
			x2 = boundingBox.x;
			y2 = boundingBox.y;
			x3 = boundingBox.x;
			y3 = boundingBox.y + boundingBox.height;
			break;
		case SOUTHEAST:
			x1 = boundingBox.x + boundingBox.width;
			y1 = boundingBox.y;
			x2 = boundingBox.x + boundingBox.width;
			y2 = boundingBox.y + boundingBox.height;
			x3 = boundingBox.x;
			y3 = boundingBox.y + boundingBox.height;
			break;
		case SOUTH:
			x1 = boundingBox.x + boundingBox.width / 2;
			y1 = boundingBox.y + boundingBox.height;
			x2 = boundingBox.x;
			y2 = boundingBox.y;
			x3 = boundingBox.x + boundingBox.width;
			y3 = boundingBox.y;
			break;
		case SOUTHWEST:
			x1 = boundingBox.x;
			y1 = boundingBox.y;
			x2 = boundingBox.x + boundingBox.width;
			y2 = boundingBox.y + boundingBox.height;
			x3 = boundingBox.x;
			y3 = boundingBox.y + boundingBox.height;
			break;
		case WEST:
			x1 = boundingBox.x;
			y1 = boundingBox.y + boundingBox.height / 2;
			x2 = boundingBox.x + boundingBox.width;
			y2 = boundingBox.y;
			x3 = boundingBox.x + boundingBox.width;
			y3 = boundingBox.y + boundingBox.height;
			break;
		default:
			throw new IllegalArgumentException("Invalid facing");
		}

		return new int[]{x1, y1, x2, y2, x3, y3};
	}

	public static Rectangle insetRectangle(Rectangle r, Rectangle insets){
		Rectangle i = new Rectangle(r.x + insets.x, r.y + insets.y, r.width + insets.width, r.height + insets.height);
		if(i.width < 0){
			return null;
		}
		if(i.height < 0){
			return null;
		}
		if(!r.contains(i.x, i.y)){
			return null;
		}
		return i;
	}

	public static boolean isEdgePoint(Point p, Rectangle r){
		int x2 = r.x + r.width;
		int y2 = r.y + r.height;
		if(p.x == r.x && p.y >= r.y && p.y <= y2){
			// It's on the left rail
			return true;
		}
		if(p.x == x2 && p.y >= r.y && p.y <= y2){
			// It's on the right rail
			return true;
		}
		if(p.y == r.y && p.x >= r.x && p.x <= x2){
			// it's on the top rail
			return true;
		}
		if(p.y == y2 && p.x >= r.x && p.x <= x2){
			// it's on the bottom rail
			return true;
		}
		return false;
	}

	public static Orientation getOrientationOfEdgePoint(Point p, Rectangle r){
		int x2 = r.x + r.width;
		int y2 = r.y + r.height;
		if(p.x == r.x && p.y == r.y){
			return Orientation.NORTHWEST;
		}
		else if(p.x == r.x && p.y == y2){
			return Orientation.SOUTHWEST;
		}
		else if(p.x == x2 && p.y == r.y){
			return Orientation.NORTHEAST;
		}
		else if(p.x == x2 && p.y == y2){
			return Orientation.SOUTHEAST;
		}
		else if(p.x == r.x && p.y >= r.y && p.y <= y2){
			return Orientation.WEST;
		}
		if(p.x == x2 && p.y >= r.y && p.y <= y2){
			return Orientation.EAST;
		}
		if(p.y == r.y && p.x >= r.x && p.x <= x2){
			return Orientation.NORTH;
		}
		if(p.y == y2 && p.x >= r.x && p.x <= x2){
			return Orientation.SOUTH;
		}

		// it's not on a rail
		return Orientation.NONE;

	}

	public static Point findClosestEdgePoint(Point p, Rectangle r){
		Point np = new Point(p.x, p.y);

		boolean midx = false;
		boolean midy = false;

		if(p.x < r.x){
			// It's to the left of the rectangle
			np.x = r.x;
		}
		else if(p.x < r.x + r.width){
			// It's in the middle of the rectangle
			midx = true;
		}
		else{
			// It's beyond the right of the rectangle
			np.x = r.x + r.width;
		}

		if(p.y < r.y){
			np.y = r.y;
		}
		else if(p.y < r.y + r.height){
			midy = true;
		}
		else{
			np.y = r.y + r.height;
		}

		if(midx && midy){
			// It was within the rectangle
			int dl = Math.abs(p.x - r.x);
			int dr = Math.abs(p.x - (r.x + r.width));
			int dt = Math.abs(p.y - r.y);
			int db = Math.abs(p.y - (r.y + r.height));

			if(dt <= db && dt <= dl && dt <= dr){
				// it's closest to the top rail.
				np.y = r.y;
				return np;
			}
			else if(db <= dt && db <= dl && db <= dr){
				// it's closest to the bottom rail
				np.y = r.y + r.height;
				return np;
			}
			else if(dl <= dt && dl <= db && dl <= dr){
				// it's closest to the left rail
				np.x = r.x;
				return np;
			}
			else{
				np.x = r.x + r.width;
				return np;
			}
		}
		return np;
	}

	public static Point scaleAndMoveBorderPoint(Point p, Rectangle oldRect, Rectangle newRect){
		if(oldRect == null || newRect == null){
			return new Point(p.x, p.y);
		}

		int ox1 = oldRect.x;
		int ox2 = oldRect.x + oldRect.width;
		int oy1 = oldRect.y;
		int oy2 = oldRect.y + oldRect.height;
		int ow = oldRect.width;
		int oh = oldRect.height;

		int nx1 = newRect.x;
		int nx2 = newRect.x + newRect.width;
		int ny1 = newRect.y;
		int ny2 = newRect.y + newRect.height;
		int nw = newRect.width;
		int nh = newRect.height;

		int dw = nw - ow;
		int dh = nh - oh;

		double sx = (double)nw / (double)ow;
		double sy = (double)nh / (double)oh;

		int dx = nx1 - ox1;
		int dy = ny1 - oy1;

		Point p2 = new Point(p.x, p.y);

		if(p.y == oldRect.y){
			// It's on the top rail

			// Keep it on the top rail
			p2.y = newRect.y;

			// Old distance from the left rail
			int dist = p.x - oldRect.x;

			if(dw != 0){
				// Scale that distance
				dist = BNAUtils.round(dist * sx);
			}

			// Also perform translation
			p2.x = newRect.x + dist;
		}
		else if(p.y == oldRect.y + oldRect.height/* - 1 */|| p.y == oldRect.y + oldRect.height){
			// It's on the bottom rail

			// Keep it on the bottom rail
			p2.y = newRect.y + newRect.height/* - 1 */;

			// Old distance from the left rail
			int dist = p.x - oldRect.x;

			if(dw != 0){
				// Scale that distance
				dist = BNAUtils.round(dist * sx);
			}

			// Also perform translation
			p2.x = newRect.x + dist;
		}
		else if(p.x == oldRect.x){
			// It's on the left rail

			// Keep it on the left rail
			p2.x = newRect.x;

			// Old distance from the top rail
			int dist = p.y - oldRect.y;

			if(dh != 0){
				// Scale that distance
				dist = BNAUtils.round(dist * sy);
			}

			// Also perform translation
			p2.y = newRect.y + dist;
		}
		else if(p.x == oldRect.x + oldRect.width/* - 1 */|| p.x == oldRect.x + oldRect.width){
			// It's on the right rail

			// Keep it on the right rail
			p2.x = newRect.x + newRect.width/* - 1 */;

			// Old distance from the top rail
			int dist = p.y - oldRect.y;

			if(dh != 0){
				// Scale that distance
				dist = BNAUtils.round(dist * sy);
			}

			// Also perform translation
			p2.y = newRect.y + dist;
		}

		// Normalize
		if(p2.x < newRect.x){
			p2.x = newRect.x;
		}
		if(p2.x >= newRect.x + newRect.width){
			p2.x = newRect.x + newRect.width/* - 1 */;
		}
		if(p2.y < newRect.y){
			p2.y = newRect.y;
		}
		if(p2.y >= newRect.y + newRect.height){
			p2.y = newRect.y + newRect.height/* - 1 */;
		}

		return p2;
	}

	public static RGB getRGBForSystemColor(Device d, int systemColorID){
		Color c = d.getSystemColor(systemColorID);
		if(c == null){
			return null;
		}
		return c.getRGB();
	}

	public static boolean isPointOnRectangle(Point p, Rectangle r){
		return isPointOnRectangle(p.x, p.y, r.x, r.y, r.width, r.height);
	}

	public static boolean isPointOnRectangle(int x, int y, int rx, int ry, int rw, int rh){
		if(x == rx || x == rx + rw){
			if(y >= ry && y <= ry + rh){
				return true;
			}
		}
		if(y == ry || y == ry + rh){
			if(x >= rx && x <= rx + rw){
				return true;
			}
		}
		return false;
	}

	public static Point snapToNormal(Point p, Rectangle r, Orientation side){
		Point np = new Point(p.x, p.y);
		switch(side){
		case NORTH:
			np.y = r.y;
			if(p.x < r.x){
				np.x = r.x;
			}
			if(p.x > r.x + r.width){
				np.x = r.x + r.width;
			}
			break;
		case SOUTH:
			np.y = r.y + r.height;
			if(p.x < r.x){
				np.x = r.x;
			}
			if(p.x > r.x + r.width){
				np.x = r.x + r.width;
			}
			break;
		case WEST:
			np.x = r.x;
			if(p.y < r.y){
				np.y = r.y;
			}
			if(p.y > r.y + r.height){
				np.y = r.y + r.height;
			}
			break;
		case EAST:
			np.x = r.x + r.width;
			if(p.y < r.y){
				np.y = r.y;
			}
			if(p.y > r.y + r.height){
				np.y = r.y + r.height;
			}
			break;
		}
		return np;
	}

	public static class PointToRectangleDistanceData{

		public Orientation closestSide;
		public double dist;
	}

	public static PointToRectangleDistanceData getPointToRectangleDistance(Point p, Rectangle r){
		double closestDist = Double.MAX_VALUE;
		Orientation closestSide = Orientation.NONE;

		double dist;
		// Check north distance
		dist = Line2D.ptSegDist(r.x, r.y, r.x + r.width, r.y, p.x, p.y);
		if(dist < closestDist){
			closestDist = dist;
			closestSide = Orientation.NORTH;
		}
		dist = Line2D.ptSegDist(r.x, r.y, r.x, r.y + r.height, p.x, p.y);
		if(dist < closestDist){
			closestDist = dist;
			closestSide = Orientation.WEST;
		}
		dist = Line2D.ptSegDist(r.x + r.width, r.y, r.x + r.width, r.y + r.height, p.x, p.y);
		if(dist < closestDist){
			closestDist = dist;
			closestSide = Orientation.EAST;
		}
		dist = Line2D.ptSegDist(r.x, r.y + r.height, r.x + r.width, r.y + r.height, p.x, p.y);
		if(dist < closestDist){
			closestDist = dist;
			closestSide = Orientation.SOUTH;
		}
		PointToRectangleDistanceData dd = new PointToRectangleDistanceData();
		dd.closestSide = closestSide;
		dd.dist = closestDist;
		return dd;
	}

	public static EnvironmentPropertiesThing getEnvironmentPropertiesThing(IBNAModel m){
		EnvironmentPropertiesThing ept = (EnvironmentPropertiesThing)m.getThing(EnvironmentPropertiesThing.ENVIRONMENT_PROPERTIES_THING_ID);
		if(ept == null){
			m.addThing(ept = new EnvironmentPropertiesThing());
		}
		return ept;
	}

	public static Rectangle clone(Rectangle r){
		return r == null ? null : new Rectangle(r.x, r.y, r.width, r.height);
	}

	public static final Point clone(Point p){
		return p == null ? null : new Point(p.x, p.y);
	}

	public static final Point[] clone(Point[] points){
		if(points == null){
			return null;
		}
		Point[] newPoints = new Point[points.length];
		for(int i = 0; i < points.length; i++){
			newPoints[i] = clone(points[i]);
		}
		return newPoints;
	}

	/**
	 * @deprecated Use {@link SWTWidgetUtils#async(Widget,Runnable)} instead
	 */
	@Deprecated
	public static void asyncExec(final Widget w, final Runnable r){
		SWTWidgetUtils.async(w, r);
	}

	/**
	 * @deprecated Use {@link SWTWidgetUtils#async(Display,Runnable)} instead
	 */
	@Deprecated
	public static void asyncExec(final Display d, final Runnable r){
		SWTWidgetUtils.async(d, r);
	}

	/**
	 * @deprecated Use {@link SWTWidgetUtils#sync(Widget,Runnable)} instead
	 */
	@Deprecated
	public static void syncExec(final Widget w, final Runnable r){
		SWTWidgetUtils.sync(w, r);
	}

	/**
	 * @deprecated Use {@link SWTWidgetUtils#sync(Display,Runnable)} instead
	 */
	@Deprecated
	public static void syncExec(final Display d, final Runnable r){
		SWTWidgetUtils.sync(d, r);
	}

	public static Composite getParentComposite(IBNAView view){
		if(view == null){
			return null;
		}
		Composite c = view.getParentComposite();
		if(c != null){
			return c;
		}
		return getParentComposite(view.getParentView());
	}

	public static Point getCentralPoint(IThing t){
		if(t instanceof IHasAnchorPoint){
			return ((IHasAnchorPoint)t).getAnchorPoint();
		}
		if(t instanceof IHasBoundingBox){
			Rectangle r = ((IHasBoundingBox)t).getBoundingBox();
			return new Point(r.x + r.width / 2, r.y + r.height / 2);
		}
		return null;
	}

	public static IThing[] getSelectedThings(IBNAModel m){
		IThing[] allThings = m.getAllThings();
		List<IThing> selectedThings = new ArrayList<IThing>();
		for(IThing t: allThings){
			if(t instanceof IHasSelected){
				if(((IHasSelected)t).isSelected()){
					selectedThings.add(t);
				}
			}
		}
		return selectedThings.toArray(new IThing[selectedThings.size()]);
	}

	public static void setGridSpacing(IBNAModel m, int gridSpacing){
		GridThing gt = GridUtils.getGridThing(m);
		if(gt != null){
			gt.setGridSpacing(gridSpacing);
		}
	}

	public static void setGridDisplayType(IBNAModel m, GridDisplayType gdt){
		GridThing gt = GridUtils.getGridThing(m);
		if(gt != null){
			gt.setGridDisplayType(gdt);
		}
	}

	public static void saveCoordinateMapperData(ICoordinateMapper cm, EnvironmentPropertiesThing ept){
		ept.setProperty("x", cm.localXtoWorldX(0));
		ept.setProperty("y", cm.localYtoWorldY(0));
		ept.setProperty("scale", cm.getScale());
	}

	public static void restoreCoordinateMapperData(IMutableCoordinateMapper cm, EnvironmentPropertiesThing ept){
		try{
			int x = ept.getProperty("x");
			int y = ept.getProperty("y");
			double scale = ept.getProperty("scale");

			cm.repositionAbsolute(x, y);
			cm.rescaleAbsolute(scale);
		}
		catch(Exception e){
		}
	}

	public static boolean infinitelyRecurses(IBNAView view){
		IBNAWorld world = view.getWorld();
		if(world == null){
			return false;
		}

		IBNAView view2 = view.getParentView();
		while(view2 != null){
			if(world.equals(view2.getWorld())){
				return true;
			}
			view2 = view2.getParentView();
		}
		return false;
	}

	public static Point movePointWith(Rectangle oldBoundingBox, Rectangle newBoundingBox, Point oldPoint){
		if(oldBoundingBox != null && newBoundingBox != null && oldPoint != null){
			int dx;
			if(oldPoint.x <= oldBoundingBox.x){
				dx = newBoundingBox.x - oldBoundingBox.x;
			}
			else if(oldPoint.x >= oldBoundingBox.x + oldBoundingBox.width){
				dx = newBoundingBox.x + newBoundingBox.width - (oldBoundingBox.x + oldBoundingBox.width);
			}
			else{
				float fx = (oldPoint.x - (float)oldBoundingBox.x) / oldBoundingBox.width;
				int nx = Math.round(fx * newBoundingBox.width) + newBoundingBox.x;
				dx = nx - oldPoint.x;
			}

			int dy;
			if(oldPoint.y <= oldBoundingBox.y){
				dy = newBoundingBox.y - oldBoundingBox.y;
			}
			else if(oldPoint.y >= oldBoundingBox.y + oldBoundingBox.height){
				dy = newBoundingBox.y + newBoundingBox.height - (oldBoundingBox.y + oldBoundingBox.height);
			}
			else{
				float fy = (oldPoint.y - (float)oldBoundingBox.y) / oldBoundingBox.height;
				int ny = Math.round(fy * newBoundingBox.height) + newBoundingBox.y;
				dy = ny - oldPoint.y;
			}

			return new Point(oldPoint.x + dx, oldPoint.y + dy);
		}
		return oldPoint;
	}

	public static float getDistance(Point p1, Point p2){
		if(p1 != null && p2 != null){
			int dx = p2.x - p1.x;
			int dy = p2.y - p1.y;
			return (float)Math.sqrt(dx * dx + dy * dy);
		}
		return Float.POSITIVE_INFINITY;
	}

	public static Point getClosestPointOnPolygon(int[] coords, int px, int py){

		// search for closest line segment index
		int closestIndex = -1;
		double closestDist = Double.POSITIVE_INFINITY;
		for(int i = 0; i < coords.length - 3; i += 2){
			int x1 = coords[i];
			int y1 = coords[i + 1];
			int x2 = coords[i + 2];
			int y2 = coords[i + 3];

			double dist;
			if((dist = Line2D.ptSegDistSq(x1, y1, x2, y2, px, py)) < closestDist){
				closestDist = dist;
				closestIndex = i;
			}
		}

		// calculate closest point on line segment
		if(closestIndex >= 0){
			int x1 = coords[closestIndex];
			int y1 = coords[closestIndex + 1];
			int x2 = coords[closestIndex + 2];
			int y2 = coords[closestIndex + 3];
			if(x1 != x2){
				double m = (double)(y2 - y1) / (x2 - x1);
				double b = y1 - m * x1;
				int x = bound(x1, px, x2);
				int y = (int)Math.round(m * x + b);
				return new Point(x, y);
			}
			else if(y1 != y2){
				int y = bound(y1, py, y2);
				return new Point(x1, y);
			}
			return new Point(px, py);
		}

		return null;
	}

	public static Point getClosestPointOnPolygon(int[] coords, int px, int py, int rx, int ry){

		// search for closest line segment index
		if(px != rx){
			int cx = px;
			int cy = py;

			double m = (double)(py - ry) / (px - rx);
			double b = ry - m * rx;

			int closestIndex = -1;
			double closestDist = Double.POSITIVE_INFINITY;
			for(int i = 0; i < coords.length - 3; i += 2){
				int x1 = coords[i];
				int y1 = coords[i + 1];
				int x2 = coords[i + 2];
				int y2 = coords[i + 3];

				if(x1 != x2){
					double im = (double)(y2 - y1) / (x2 - x1);
					double ib = y1 - im * x1;
					int ix = (int)Math.round((ib - b) / (m - im));
					if(isInBound(x1, ix, x2)){
						double dist;
						if((dist = Line2D.ptSegDistSq(x1, y1, x2, y2, px, py)) < closestDist){
							closestDist = dist;
							closestIndex = i;
							cx = ix;
							cy = (int)Math.round(im * ix + ib);
						}
					}
				}
				else{
					// in case the line is vertical
					int iy = (int)Math.round(m * x1 + b);
					if(isInBound(y1, iy, y2)){
						double dist;
						if((dist = Line2D.ptSegDistSq(x1, y1, x2, y2, px, py)) < closestDist){
							closestDist = dist;
							closestIndex = i;
							cx = x1;
							cy = iy;
						}
					}
				}
			}

			if(closestIndex >= 0){
				return new Point(cx, cy);
			}
		}
		else{
			int cy = py;

			int closestIndex = -1;
			double closestDist = Double.POSITIVE_INFINITY;
			for(int i = 0; i < coords.length - 3; i += 2){
				int x1 = coords[i];
				int y1 = coords[i + 1];
				int x2 = coords[i + 2];
				int y2 = coords[i + 3];

				if(isInBound(x1, px, x2)){
					double dist;
					if((dist = Line2D.ptSegDistSq(x1, y1, x2, y2, px, py)) < closestDist){
						closestDist = dist;
						closestIndex = i;
						cy = bound(y1, py, y2);
					}
				}
			}

			if(closestIndex >= 0){
				return new Point(px, cy);
			}
		}

		return null;
	}

	public static final Point getClosestPointOnEllipse(Rectangle r, int px, int py){
		// normalize to a circle at (0,0) with a radius of 0.5
		double npx = (double)(px - r.x) / r.width - 0.5d;
		double npy = (double)(py - r.y) / r.height - 0.5d;

		// y = mx + b, b = 0;
		double nM = npy / npx;
		double nM2 = nM * nM;

		// x^2 + y^2 = r^2, r = 0.5
		double nx = Math.sqrt(0.25d / (1d + nM2));
		double ny = Math.sqrt(0.25d - nx * nx);

		// un-normalize results
		double x1 = ((npx < 0 ? -nx : nx) + 0.5d) * r.width + r.x;
		double y1 = ((npy < 0 ? -ny : ny) + 0.5d) * r.height + r.y;

		return new Point(round(x1), round(y1));
	}

	/*
	 * Untested!!!
	 */
	public static final Point getClosestPointOnEllipse(Rectangle r, int px, int py, int rx, int ry){
		if(px != rx){
			// normalize to a circle at (0,0) with a radius of 0.5
			double npx = (double)(px - r.x) / r.width - 0.5d;
			double npy = (double)(py - r.y) / r.height - 0.5d;
			double nrx = (double)(rx - r.x) / r.width - 0.5d;
			double nry = (double)(ry - r.y) / r.height - 0.5d;

			// y = mx + b
			double nM = (npy - nry) / (npx - nrx);
			double nB = nry - nM * nrx;
			double nM2 = nM * nM;
			double nB2 = nB * nB;

			// quadratic formula
			double QA = 1d + nM2;
			double QB = 2 * nM * nB;
			double QC = nB2 - 0.25;

			double b24ac = QB * QB - 4d * QA * QC;
			if(b24ac >= 0){
				double sqrt = Math.sqrt(b24ac);
				double nx1 = (-QB + sqrt) / 2d / QA;
				double nx2 = (-QB - sqrt) / 2d / QA;

				double nx = npx < 0 ? Math.min(nx1, nx2) : Math.max(nx1, nx2);
				double ny = Math.sqrt(0.25d - nx * nx);

				// un-normalize results
				double x1 = (nx + 0.5d) * r.width + r.x;
				double y1 = ((npy < 0 ? -ny : ny) + 0.5d) * r.height + r.y;

				return new Point(round(x1), round(y1));
			}
		}
		else{
			return getClosestPointOnEllipse(r, px, py);
		}
		return null;
	}

	private static final LineAttributes[][] marqueeAttributes = new LineAttributes[2][];
	private static final int marqueeAttributesLength = 6;
	static{
		List<LineAttributes> lineAttributes = new ArrayList<LineAttributes>();

		lineAttributes.clear();
		for(int i = 0; i < marqueeAttributesLength; i++){
			LineAttributes la = new LineAttributes(1);
			la.style = SWT.LINE_CUSTOM;
			la.dash = new float[]{marqueeAttributesLength / 2f, marqueeAttributesLength / 2f};
			la.dashOffset = i + marqueeAttributesLength / 2f;
			lineAttributes.add(la);
		}
		marqueeAttributes[0] = lineAttributes.toArray(new LineAttributes[lineAttributes.size()]);

		lineAttributes.clear();
		for(int i = 0; i < marqueeAttributesLength; i++){
			LineAttributes la = new LineAttributes(1);
			la.style = SWT.LINE_CUSTOM;
			la.dash = new float[]{marqueeAttributesLength / 2f, marqueeAttributesLength / 2f};
			la.dashOffset = i;
			lineAttributes.add(la);
		}
		marqueeAttributes[1] = lineAttributes.toArray(new LineAttributes[lineAttributes.size()]);
	}

	public static void drawMarquee(GC g, int offset, boolean reverse, Runnable r){
		offset = offset % marqueeAttributesLength;
		if(reverse){
			offset = marqueeAttributesLength - offset - 1;
		}

		LineAttributes oldLineAttributes = g.getLineAttributes();

		g.setForeground(g.getDevice().getSystemColor(SWT.COLOR_WHITE));
		g.setLineAttributes(marqueeAttributes[0][offset]);
		r.run();

		g.setForeground(g.getDevice().getSystemColor(SWT.COLOR_BLACK));
		g.setLineAttributes(marqueeAttributes[1][offset]);
		r.run();

		g.setLineAttributes(oldLineAttributes);
	}

	public static final IBNAView getInternalView(IBNAView outerView, IThing worldThing){
		if(worldThing instanceof IHasWorld){
			IThingPeer worldThingPeer = outerView.getPeer(worldThing);
			if(worldThingPeer instanceof WorldThingPeer){
				IBNAView internalView = ((WorldThingPeer)worldThingPeer).getInnerView();
				return internalView;
			}
		}
		return null;
	}

	public static final IBNAView getInternalView(IBNAView outerView, String worldThingID){
		return getInternalView(outerView, outerView.getWorld().getBNAModel().getThing(worldThingID));
	}

	public static final void expandRectangle(Rectangle r, Point toIncludePoint){
		if(toIncludePoint.x < r.x){
			r.width += r.x - toIncludePoint.x;
			r.x = toIncludePoint.x;
		}
		else if(toIncludePoint.x > r.x + r.width){
			r.width = toIncludePoint.x - r.x;
		}
		if(toIncludePoint.y < r.y){
			r.height += r.y - toIncludePoint.y;
			r.y = toIncludePoint.y;
		}
		else if(toIncludePoint.y > r.y + r.height){
			r.height = toIncludePoint.y - r.y;
		}
	}

	public static final List<IThing> toThings(String[] thingIDs, IBNAModel model){
		List<IThing> things = new ArrayList<IThing>(thingIDs.length);
		for(String thingID: thingIDs){
			IThing thing = model.getThing(thingID);
			if(thing != null){
				things.add(thing);
			}
		}
		return things;
	}

	@SuppressWarnings("unchecked")
	public static final <T extends IThing>List<T> toThings(String[] thingIDs, IBNAModel model, Class<T> thingClass){
		List<T> things = new ArrayList<T>(thingIDs.length);
		for(String thingID: thingIDs){
			IThing thing = model.getThing(thingID);
			if(thingClass.isInstance(thing)){
				things.add((T)thing);
			}
		}
		return things;
	}
}
