package edu.uci.isr.bna4.logics.editing;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchActionConstants;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAAlignUtils;
import edu.uci.isr.bna4.BNADistributeUtils;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAMenuListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.facets.IHasMutableAnchorPoint;
import edu.uci.isr.bna4.facets.IHasMutableBoundingBox;
import edu.uci.isr.widgets.swt.constants.HorizontalAlignment;
import edu.uci.isr.widgets.swt.constants.VerticalAlignment;

public class AlignAndDistributeLogic extends AbstractThingLogic implements IBNAMenuListener{
	
	protected ImageRegistry imageRegistry = null;

	protected static final String ALIGN_LEFT = "alignLeft";
	protected static final String ALIGN_CENTER = "alignCenter";
	protected static final String ALIGN_RIGHT = "alignRight";
	protected static final String ALIGN_TOP = "alignTop";
	protected static final String ALIGN_MIDDLE = "alignMiddle";
	protected static final String ALIGN_BOTTOM = "alignBottom";
	
	protected static final String DISTRIBUTE_HORIZONTAL_LOOSE = "distributeHorizontalLoose";
	protected static final String DISTRIBUTE_HORIZONTAL_TIGHT = "distributeHorizontalTight";
	protected static final String DISTRIBUTE_VERTICAL_LOOSE = "distributeVerticalLoose";
	protected static final String DISTRIBUTE_VERTICAL_TIGHT = "distributeVerticalTight";
	
	public AlignAndDistributeLogic(){
	}
	
	protected void loadImages(IBNAView view){
		Composite comp = BNAUtils.getParentComposite(view);
		imageRegistry = new ImageRegistry(comp.getDisplay());
		
		imageRegistry.put(ALIGN_LEFT, new Image(comp.getDisplay(), 
			AlignAndDistributeLogic.class.getClassLoader().getResourceAsStream("edu/uci/isr/bna4/res/align-left.gif")));
		imageRegistry.put(ALIGN_CENTER, new Image(comp.getDisplay(), 
			AlignAndDistributeLogic.class.getClassLoader().getResourceAsStream("edu/uci/isr/bna4/res/align-center.gif")));
		imageRegistry.put(ALIGN_RIGHT, new Image(comp.getDisplay(), 
			AlignAndDistributeLogic.class.getClassLoader().getResourceAsStream("edu/uci/isr/bna4/res/align-right.gif")));
		imageRegistry.put(ALIGN_TOP, new Image(comp.getDisplay(), 
			AlignAndDistributeLogic.class.getClassLoader().getResourceAsStream("edu/uci/isr/bna4/res/align-top.gif")));
		imageRegistry.put(ALIGN_MIDDLE, new Image(comp.getDisplay(), 
			AlignAndDistributeLogic.class.getClassLoader().getResourceAsStream("edu/uci/isr/bna4/res/align-middle.gif")));
		imageRegistry.put(ALIGN_BOTTOM, new Image(comp.getDisplay(), 
			AlignAndDistributeLogic.class.getClassLoader().getResourceAsStream("edu/uci/isr/bna4/res/align-bottom.gif")));

		imageRegistry.put(DISTRIBUTE_HORIZONTAL_LOOSE, new Image(comp.getDisplay(), 
			AlignAndDistributeLogic.class.getClassLoader().getResourceAsStream("edu/uci/isr/bna4/res/distribute-horizontal-loose.gif")));
		imageRegistry.put(DISTRIBUTE_HORIZONTAL_TIGHT, new Image(comp.getDisplay(), 
			AlignAndDistributeLogic.class.getClassLoader().getResourceAsStream("edu/uci/isr/bna4/res/distribute-horizontal-tight.gif")));
		imageRegistry.put(DISTRIBUTE_VERTICAL_LOOSE, new Image(comp.getDisplay(), 
			AlignAndDistributeLogic.class.getClassLoader().getResourceAsStream("edu/uci/isr/bna4/res/distribute-vertical-loose.gif")));
		imageRegistry.put(DISTRIBUTE_VERTICAL_TIGHT, new Image(comp.getDisplay(), 
			AlignAndDistributeLogic.class.getClassLoader().getResourceAsStream("edu/uci/isr/bna4/res/distribute-vertical-tight.gif")));
	}
	
	public void destroy(){
		if(imageRegistry != null){
			imageRegistry.dispose();
		}
	}
	
	public void fillMenu(IBNAView view, IMenuManager m, int localX, int localY, IThing t, int worldX, int worldY){
		if(imageRegistry == null){
			loadImages(view);
		}
		
		IThing[] selectedThings = BNAUtils.getSelectedThings(view.getWorld().getBNAModel());
		if(selectedThings.length < 2) return;

		List<IThing> thingsToEditList = new ArrayList<IThing>();
		//Make sure at least two things have either an anchor point or bounding box
		for(IThing st : selectedThings){
			if((st instanceof IHasMutableBoundingBox) || (st instanceof IHasMutableAnchorPoint)){
				thingsToEditList.add(st);
			}
		}
		if(thingsToEditList.size() < 2) return;
		final IThing[] thingsToEdit = thingsToEditList.toArray(new IThing[thingsToEditList.size()]);
		
		IMenuManager alignMenu = new MenuManager("Align");
		
		IAction alignTop = new Action("Align &Top", imageRegistry.getDescriptor(ALIGN_TOP)){
			public void run(){
				BNAAlignUtils.align(thingsToEdit, VerticalAlignment.TOP);
			}
		};
		alignMenu.add(alignTop);
		
		IAction alignMiddle = new Action("Align &Middle", imageRegistry.getDescriptor(ALIGN_MIDDLE)){
			public void run(){
				BNAAlignUtils.align(thingsToEdit, VerticalAlignment.MIDDLE);
			}
		};
		alignMenu.add(alignMiddle);
		
		IAction alignBottom = new Action("Align &Bottom", imageRegistry.getDescriptor(ALIGN_BOTTOM)){
			public void run(){
				BNAAlignUtils.align(thingsToEdit, VerticalAlignment.BOTTOM);
			}
		};
		alignMenu.add(alignBottom);
		
		IAction alignLeft = new Action("Align &Left", imageRegistry.getDescriptor(ALIGN_LEFT)){
			public void run(){
				BNAAlignUtils.align(thingsToEdit, HorizontalAlignment.LEFT);
			}
		};
		alignMenu.add(alignLeft);

		IAction alignCenter = new Action("Align &Center", imageRegistry.getDescriptor(ALIGN_CENTER)){
			public void run(){
				BNAAlignUtils.align(thingsToEdit, HorizontalAlignment.CENTER);
			}
		};
		alignMenu.add(alignCenter);
			
		IAction alignRight = new Action("Align &Right", imageRegistry.getDescriptor(ALIGN_RIGHT)){
			public void run(){
				BNAAlignUtils.align(thingsToEdit, HorizontalAlignment.RIGHT);
			}
		};
		alignMenu.add(alignRight);

		IMenuManager distributeMenu = new MenuManager("Distribute");
		
		IAction distributeHorizontalLoose = new Action("Distribute Horizontal Loose", imageRegistry.getDescriptor(DISTRIBUTE_HORIZONTAL_LOOSE)){
			public void run(){
				BNADistributeUtils.distributeHorizontalLoose(thingsToEdit);
			}
		};
		distributeMenu.add(distributeHorizontalLoose);

		IAction distributeHorizontalTight = new Action("Distribute Horizontal Tight", imageRegistry.getDescriptor(DISTRIBUTE_HORIZONTAL_TIGHT)){
			public void run(){
				BNADistributeUtils.distributeHorizontalTight(thingsToEdit);
			}
		};
		distributeMenu.add(distributeHorizontalTight);

		IAction distributeVerticalLoose = new Action("Distribute Vertical Loose", imageRegistry.getDescriptor(DISTRIBUTE_VERTICAL_LOOSE)){
			public void run(){
				BNADistributeUtils.distributeVerticalLoose(thingsToEdit);
			}
		};
		distributeMenu.add(distributeVerticalLoose);

		IAction distributeVerticalTight = new Action("Distribute Vertical Tight", imageRegistry.getDescriptor(DISTRIBUTE_VERTICAL_TIGHT)){
			public void run(){
				BNADistributeUtils.distributeVerticalTight(thingsToEdit);
			}
		};
		distributeMenu.add(distributeVerticalTight);

		m.add(alignMenu);
		m.add(distributeMenu);
		m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	
}
