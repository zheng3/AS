package edu.uci.isr.bna4.things.utility;

import java.awt.geom.Ellipse2D;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.ICoordinateMapper;
import edu.uci.isr.bna4.IThing;

public class RotaterThingPeer
	extends AbstractThingPeer{

	final int thickness = 10;

	protected RotaterThing lt;

	protected Rectangle localBoundingBox = new Rectangle(0, 0, 0, 0);

	public RotaterThingPeer(IThing t){
		super(t);
		if(!(t instanceof RotaterThing)){
			throw new IllegalArgumentException("RotaterThingPeer can only peer for RotaterThing");
		}
		this.lt = (RotaterThing)t;
	}

	@Override
	public void draw(IBNAView view, GC g){
		ICoordinateMapper cm = view.getCoordinateMapper();
		Rectangle worldBoundingBox = lt.getBoundingBox();
		localBoundingBox = BNAUtils.worldToLocal(cm, worldBoundingBox);

		if(!g.getClipping().intersects(localBoundingBox)){
			return;
		}

		Path p = new Path(g.getDevice());
		p.addArc(localBoundingBox.x, localBoundingBox.y, localBoundingBox.width, localBoundingBox.height, 0, 360);
		p.addArc(localBoundingBox.x + thickness, localBoundingBox.y + thickness, localBoundingBox.width - 2 * thickness, localBoundingBox.height - 2 * thickness, 0, 360);
		p.close();

		Color fg = getDisplay().getSystemColor(SWT.COLOR_GRAY);
		g.setForeground(fg);
		int oldAlpha = g.getAlpha();
		g.setAlpha(64);
		g.fillPath(p);
		g.setAlpha(oldAlpha);

		fg = getDisplay().getSystemColor(SWT.COLOR_BLACK);
		g.setForeground(fg);

		g.drawPath(p);
		p.dispose();

		int angle = lt.getAngle();

		fg = getDisplay().getSystemColor(SWT.COLOR_RED);
		g.setBackground(fg);
		g.setAlpha(192);
		int startAngle = 360 - (angle + 5);
		if(startAngle < 0){
			startAngle += 360;
		}
		if(startAngle > 360){
			startAngle -= 360;
		}
		g.fillArc(localBoundingBox.x, localBoundingBox.y, localBoundingBox.width, localBoundingBox.height, startAngle, 10);
		g.setAlpha(oldAlpha);

	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		if(!BNAUtils.isWithin(lt.getBoundingBox(), worldX, worldY)){
			return false;
		}

		Ellipse2D.Double outerEllipse = new Ellipse2D.Double(localBoundingBox.x, localBoundingBox.y, localBoundingBox.width, localBoundingBox.height);
		Ellipse2D.Double innerEllipse = new Ellipse2D.Double(localBoundingBox.x + thickness, localBoundingBox.y + thickness, localBoundingBox.width - 2 * thickness, localBoundingBox.height - 2 * thickness);

		int localX = view.getCoordinateMapper().worldXtoLocalX(worldX);
		int localY = view.getCoordinateMapper().worldYtoLocalY(worldY);

		boolean in = outerEllipse.contains(localX, localY) && !innerEllipse.contains(localX, localY);
		return in;
	}
}
