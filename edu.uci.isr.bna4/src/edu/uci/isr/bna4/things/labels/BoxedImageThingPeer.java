package edu.uci.isr.bna4.things.labels;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import edu.uci.isr.bna4.AbstractThingPeer;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.ICoordinateMapper;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.IThingListener;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.WeakThingListener;
import edu.uci.isr.bna4.facets.IHasImage;
import edu.uci.isr.widgets.swt.constants.HorizontalAlignment;
import edu.uci.isr.widgets.swt.constants.VerticalAlignment;

public class BoxedImageThingPeer
	extends AbstractThingPeer{

	protected BoxedImageThing t;
	protected Rectangle localBoundingBox = new Rectangle(0, 0, 0, 0);
	protected ImageCache imageCache;
	private CacheMaintainer cacheMaintainer;

	private class ImageCache{

		public Image originalImage = null;
		public Image lastImage = null;
		public int lastHeight = -1;
		public int lastWidth = -1;

		public ImageCache(Display display){
			display.disposeExec(new Runnable(){

				public void run(){
					try{
						ImageCache.this.dispose();
					}
					catch(Throwable t){
						t.printStackTrace();
					}

				}
			});
		}

		private void resetCache(Display display){
			dispose();
			String imagePath = t.getImagePath();
			try{
				if(imagePath != null){
					originalImage = new Image(display, imagePath);
				}
			}
			catch(Exception e){
				System.err.println("Could not create Image from path: " + imagePath);
				e.printStackTrace();
				dispose();
			}
		}

		public synchronized void resetImageSize(){
			lastHeight = -1;
			lastWidth = -1;
			disposeLastImage();
		}

		public synchronized void dispose(){
			disposeLastImage();
			disposeOriginalImage();
			resetImageSize();
		}

		private synchronized void disposeOriginalImage(){
			if(originalImage != null){
				if(!originalImage.isDisposed()){
					originalImage.dispose();
				}
				originalImage = null;
			}
		}

		private synchronized void disposeLastImage(){
			if(lastImage != null){
				if(!lastImage.isDisposed()){
					lastImage.dispose();
				}
				lastImage = null;
			}
		}

		public synchronized boolean hasImageSizeFor(int width, int height){
			return originalImage != null && lastImage != null && width == lastWidth && height == lastHeight;
		}
	}

	public BoxedImageThingPeer(IThing thing){
		super(thing);
		if(!(thing instanceof BoxedImageThing)){
			throw new IllegalArgumentException("BoxedImageThingPeer can only peer for BoxedImageThing");
		}
		t = (BoxedImageThing)thing;
		cacheMaintainer = new CacheMaintainer();
		t.addThingListener(new WeakThingListener(t, cacheMaintainer));
	}

	protected void updateLocalBoundingBox(ICoordinateMapper cm){
		localBoundingBox = BNAUtils.worldToLocal(cm, BNAUtils.normalizeRectangle(t.getBoundingBox()));
	}

	private class CacheMaintainer
		implements IThingListener{

		public void thingChanged(ThingEvent thingEvent){
			Object propertyName = thingEvent.getPropertyName();
			if(propertyName == null){
				return;
			}
			if(propertyName.equals(IHasImage.IMAGE_PATH_PROPERTY_NAME)){
				if(imageCache != null){
					imageCache.resetCache(getDisplay());
				}
			}
		}
	}

	@Override
	public void draw(IBNAView view, GC g){
		updateLocalBoundingBox(view.getCoordinateMapper());
		if(!g.getClipping().intersects(localBoundingBox)){
			return;
		}

		if(imageCache == null){
			imageCache = new ImageCache(getDisplay());
		}

		VerticalAlignment verticalAlignment = t.getVerticalAlignment();
		HorizontalAlignment horizontalAlignment = t.getHorizontalAlignment();

		if(imageCache != null){
			if(!imageCache.hasImageSizeFor(localBoundingBox.width, localBoundingBox.height)){
				if(imageCache.originalImage == null || imageCache.originalImage.isDisposed()){
					imageCache.resetCache(getDisplay());
				}
				resize(g);
			}
			draw(g, verticalAlignment, horizontalAlignment);
		}
	}

	private void draw(GC g, VerticalAlignment verticalAlignment, HorizontalAlignment horizontalAlignment){
		if(localBoundingBox.height > 1 && localBoundingBox.width > 1){
			if(imageCache != null){
				int x = 0, y = 0;
				int imageWidth = imageCache.lastWidth;
				int imageHeight = imageCache.lastHeight;
				switch(horizontalAlignment){
				case CENTER:
					x = localBoundingBox.x + localBoundingBox.width / 2 - imageWidth / 2;
					break;
				case LEFT:
					x = localBoundingBox.x;
					break;
				case RIGHT:
					x = localBoundingBox.x + localBoundingBox.width - imageWidth;
					break;
				}

				switch(verticalAlignment){
				case MIDDLE:
					y = localBoundingBox.y + localBoundingBox.height / 2 - imageHeight / 2;
					break;
				case TOP:
					y = localBoundingBox.y;
					break;
				case BOTTOM:
					y = localBoundingBox.y + localBoundingBox.height - imageHeight;
					break;
				}
				if(imageCache.lastImage != null){
					g.drawImage(imageCache.lastImage, x, y);
				}
			}
		}
	}

	private void resize(GC g){
		Image originalImage = null;
		if(imageCache != null){
			imageCache.resetImageSize();
			originalImage = imageCache.originalImage;
			if(originalImage != null && !originalImage.isDisposed()){
				int height = originalImage.getBounds().height;
				int width = originalImage.getBounds().width;
				if(height > localBoundingBox.height || width > localBoundingBox.width){
					float ratio = new Float(height) / new Float(width);
					if(height > localBoundingBox.height){
						height = localBoundingBox.height;
						width = (int)(localBoundingBox.height / ratio);
					}
					if(width > localBoundingBox.width){
						width = localBoundingBox.width;
						height = (int)(localBoundingBox.width * ratio);
					}

					if(width == 0 || height == 0){
						width = 1;
						height = 1;
					}
					imageCache.lastImage = new Image(g.getDevice(), originalImage.getImageData().scaledTo(width, height));
				}
				else{
					imageCache.lastImage = new Image(g.getDevice(), originalImage.getImageData());
				}
				imageCache.lastHeight = height;
				imageCache.lastWidth = width;
			}
		}
	}

	@Override
	public boolean isInThing(IBNAView view, int worldX, int worldY){
		return BNAUtils.isWithin(t.getBoundingBox(), worldX, worldY);
	}

}
