package edu.uci.isr.bna4.logics.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hslf.model.Picture;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextBox;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchActionConstants;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAComposite;
import edu.uci.isr.bna4.BNARenderingSettings;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.DefaultBNAView;
import edu.uci.isr.bna4.DefaultCoordinateMapper;
import edu.uci.isr.bna4.IBNAMenuListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.IThingPeer;
import edu.uci.isr.bna4.logics.tracking.ModelBoundsTrackingLogic;

public class ExportPPTLogic
	extends AbstractThingLogic
	implements IBNAMenuListener{

	protected ModelBoundsTrackingLogic mbtl = null;

	public ExportPPTLogic(ModelBoundsTrackingLogic mbtl){
		this.mbtl = mbtl;
	}

	public void fillMenu(IBNAView view, IMenuManager m, int localX, int localY, IThing t, int worldX, int worldY){
		if(t == null){
			final IBNAView fview = view;
			IAction saveAsPTTAction = new Action("Save as PTT..."){

				@Override
				public void run(){

					System.out.println("PPT Save action: Start");

					saveAsPPT(fview, "ppt", "Microsoft PowerPoint 97-2003 Format (*.ppt)", "Title", SWT.IMAGE_JPEG);
					System.out.println("PPT Save action: End");
				}
			};
			m.add(saveAsPTTAction);

			m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		}
	}

	protected void saveAsPPT(IBNAView view, String extension, String bitmapName, String slideTitle, int swtImageType){

		Shell s = BNAUtils.getParentComposite(view).getShell();

		//Begin file save prep
		FileDialog fileDialog = new FileDialog(s, SWT.SAVE);
		fileDialog.setText("Save");

		String[] filterExt = {"*." + extension};
		String[] filterNames = {bitmapName};
		fileDialog.setFilterExtensions(filterExt);
		fileDialog.setFilterNames(filterNames);
		String selected = fileDialog.open();

		if(selected == null){
			return;
		}

		// Debug
		System.out.println("selected = " + selected);

		if(!selected.toLowerCase().trim().endsWith("." + extension)){
			selected += "." + extension;
		}

		File f = new File(selected);
		if(f.exists()){
			boolean confirm = MessageDialog.openConfirm(s, "Confirm Overwrite", "Overwrite existing file?");
			if(!confirm){
				saveAsPPT(view, extension, bitmapName, slideTitle, swtImageType);
				return;
			}
		}

		//End file save prep

		//Creating a Jpeg of the active view
		Image image = createImage(view);

		// store jpeg as a file for import into PPT slide
		// REASON: could not figure out a non-file based hack to store the image data
		int indexOfFileExtension = selected.lastIndexOf("." + extension);
		String jpegFilenameOfSelected = selected.substring(0, indexOfFileExtension) + ".jpg";

		File fjpeg = new File(jpegFilenameOfSelected);
		int counter = 1;
		while(fjpeg.exists()){
			String baseName = selected.substring(0, indexOfFileExtension);
			jpegFilenameOfSelected = baseName + "(" + counter + ").jpg";
			fjpeg = new File(jpegFilenameOfSelected);
		}
		java.io.FileOutputStream out;
		try{
			out = new java.io.FileOutputStream(jpegFilenameOfSelected);
			ImageLoader loader = new ImageLoader();
			loader.data = new ImageData[]{image.getImageData()};
			loader.save(out, swtImageType);
			out.close();

			SlideShow ppt = new SlideShow();
			Slide slide = ppt.createSlide();
			TextBox title = slide.addTitle();
			title.setText(slideTitle);

			int idx = ppt.addPicture(new File(jpegFilenameOfSelected), Picture.JPEG);
			Picture pict = new Picture(idx);
			slide.addShape(pict);
			pict.setAnchor(new java.awt.Rectangle(100, 100, 600, 400));
			java.io.FileOutputStream pptOut = new java.io.FileOutputStream(selected);
			ppt.write(pptOut);
			pptOut.close();

		}
		catch(FileNotFoundException efnf){
			efnf.printStackTrace();
		}
		catch(IOException eio){
			eio.printStackTrace();
		}
		finally{
			if(image != null){
				image.dispose();
			}
		}

	}

	protected void saveAsBitmap(IBNAView view, String extension, String bitmapName, int swtImageType){
		Shell s = BNAUtils.getParentComposite(view).getShell();
		FileDialog fd = new FileDialog(s, SWT.SAVE);
		fd.setText("Save");
		String[] filterExt = {"*." + extension};
		String[] filterNames = {bitmapName};
		fd.setFilterExtensions(filterExt);
		fd.setFilterNames(filterNames);
		String selected = fd.open();

		if(selected == null){
			return;
		}
		if(!selected.toLowerCase().trim().endsWith("." + extension)){
			selected += "." + extension;
		}

		File f = new File(selected);
		if(f.exists()){
			boolean confirm = MessageDialog.openConfirm(s, "Confirm Overwrite", "Overwrite existing file?");
			if(!confirm){
				saveAsBitmap(view, extension, bitmapName, swtImageType);
				return;
			}
		}

		Image image = createImage(view);
		try{
			ImageLoader loader = new ImageLoader();
			loader.data = new ImageData[]{image.getImageData()};
			loader.save(selected, swtImageType);
		}
		finally{
			if(image != null){
				image.dispose();
			}
		}
	}

	protected Image createImage(IBNAView view){

		Rectangle bounds = mbtl.getModelBounds();

		BNAComposite bnaComposite = (BNAComposite)BNAUtils.getParentComposite(view);

		DefaultCoordinateMapper cm = new DefaultCoordinateMapper();
		cm.repositionAbsolute(bounds.x, bounds.y);
		cm.rescaleAbsolute(1.0);

		DefaultBNAView newView = new DefaultBNAView(view, view.getWorld(), cm);

		Image image = new Image(BNAUtils.getParentComposite(view).getDisplay(), bounds.width + 10, bounds.height + 10);
		GC gc = new GC(image);

		gc.setAntialias(BNARenderingSettings.getAntialiasGraphics(bnaComposite) ? SWT.ON : SWT.OFF);
		gc.setTextAntialias(BNARenderingSettings.getAntialiasText(bnaComposite) ? SWT.ON : SWT.OFF);

		IThing[] things = newView.getWorld().getBNAModel().getAllThings();
		for(int i = 0; i < things.length; i++){
			if(!Boolean.TRUE.equals(things[i].getProperty(IBNAView.HIDE_THING_PROPERTY_NAME))){
				IThingPeer peer = newView.getPeer(things[i]);
				peer.draw(newView, gc);
			}
		}
		gc.dispose();
		return image;
	}
}
