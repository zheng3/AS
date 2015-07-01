package edu.uci.isr.bna4.things.labels;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ResourceUtils;
import edu.uci.isr.widgets.swt.constants.FontStyle;
import edu.uci.isr.widgets.swt.constants.HorizontalAlignment;
import edu.uci.isr.widgets.swt.constants.VerticalAlignment;

public class BoxedLabelThingPeer
	extends AbstractThingPeer{

	protected BoxedLabelThing t;

	protected TextLayoutCache textLayoutCache = null;

	public BoxedLabelThingPeer(IThing t){
		super(t);
		if(!(t instanceof BoxedLabelThing)){
			throw new IllegalArgumentException("BoxedLabelThingPeer can only peer for BoxedLabelThing");
		}
		this.t = (BoxedLabelThing)t;
	}

	@Override
	public void draw(IBNAView view, GC g){
		Rectangle localBoundingBox = BNAUtils.worldToLocal(view.getCoordinateMapper(), BNAUtils.normalizeRectangle(t.getBoundingBox()));
		if(!g.getClipping().intersects(localBoundingBox)){
			return;
		}

		String text = t.getText();
		if(text == null || text.trim().length() == 0){
			return;
		}

		Color fg = ResourceUtils.getColor(getDisplay(), t.getColor());
		if(fg == null){
			fg = g.getDevice().getSystemColor(SWT.COLOR_BLACK);
		}
		g.setForeground(fg);

		String fontName = t.getFontName();
		int fontSize = t.getFontSize();
		FontStyle fontStyle = t.getFontStyle();
		boolean dontIncreaseFontSize = t.getDontIncreaseFontSize();

		VerticalAlignment verticalAlignment = t.getVerticalAlignment();
		HorizontalAlignment horizontalAlignment = t.getHorizontalAlignment();

		if(textLayoutCache == null){
			textLayoutCache = new TextLayoutCache(getDisplay());
		}
		textLayoutCache.setIncreaseFontSize(!dontIncreaseFontSize);
		textLayoutCache.setText(text);
		textLayoutCache.setFont(ResourceUtils.getFont(getDisplay(), fontName, fontSize, fontStyle));
		{
			/*
			 * The size of the local bounding box may differ slightly depending
			 * on the world origin. This causes excessive recalculation of the
			 * text layout, which is not really necessary. To overcome this, the
			 * width and height of the layout are only changed if they differ
			 * significantly from what they were before.
			 */

			Point oldSize = textLayoutCache.getSize();
			Point size = new Point(localBoundingBox.width, localBoundingBox.height);
			int dx = size.x - oldSize.x;
			int dy = size.y - oldSize.y;
			if(0 <= dx && dx <= 1 && 0 <= dy && dy <= 1){
				size = oldSize;
			}
			textLayoutCache.setSize(size);
		}
		textLayoutCache.setAlignment(horizontalAlignment.toSWT());

		TextLayout tl = textLayoutCache.getTextLayout();
		if(tl != null){
			Rectangle tlBounds = tl.getBounds();

			int x = localBoundingBox.x;
			switch(horizontalAlignment){
			case LEFT:
				break;
			case CENTER:
				x += (localBoundingBox.width - tlBounds.width) / 2;
				break;
			case RIGHT:
				x += localBoundingBox.width - tlBounds.width;
				break;
			}

			int y = localBoundingBox.y;
			switch(verticalAlignment){
			case TOP:
				break;
			case MIDDLE:
				y += (localBoundingBox.height - tlBounds.height) / 2;
				break;
			case BOTTOM:
				y += localBoundingBox.height - tlBounds.height;
				break;
			}

			tl.draw(g, x, y);
		}
		else{
			drawFakeLines(g, localBoundingBox);
		}
	}

	private void drawFakeLines(GC g, Rectangle localBoundingBox){
		g.setLineStyle(SWT.LINE_DASHDOT);
		if(localBoundingBox.width >= 3){
			if(localBoundingBox.height >= 1){
				int y = localBoundingBox.y + localBoundingBox.height / 2;
				g.drawLine(localBoundingBox.x + 1, y, localBoundingBox.x + localBoundingBox.width - 2, y);
			}
			if(localBoundingBox.height > 5){
				int y = localBoundingBox.y + localBoundingBox.height / 2 - 2;
				g.drawLine(localBoundingBox.x + 1, y, localBoundingBox.x + localBoundingBox.width - 2, y);
				y += 4;
				g.drawLine(localBoundingBox.x + 1, y, localBoundingBox.x + localBoundingBox.width - 2, y);
			}
		}
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		return BNAUtils.isWithin(t.getBoundingBox(), worldX, worldY);
	}

}
