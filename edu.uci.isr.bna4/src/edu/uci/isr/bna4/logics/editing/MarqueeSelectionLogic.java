package edu.uci.isr.bna4.logics.editing;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IBNAMouseListener;
import edu.uci.isr.bna4.IBNAMouseMoveListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.UserEditableUtils;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutableSelected;
import edu.uci.isr.bna4.logics.tracking.TypedThingTrackingLogic;
import edu.uci.isr.bna4.things.borders.MarqueeBoxBorderThing;

public class MarqueeSelectionLogic
	extends AbstractThingLogic
	implements IBNAMouseListener, IBNAMouseMoveListener{

	protected final TypedThingTrackingLogic tttl;

	protected MarqueeBoxBorderThing marqueeSelection = null;

	protected int initDownX = -1;
	protected int initDownY = -1;

	public MarqueeSelectionLogic(TypedThingTrackingLogic tttl){
		this.tttl = tttl;
	}

	@Override
	public void destroy(){
		if(marqueeSelection != null){
			IBNAModel m = getBNAModel();
			if(m != null){
				m.removeThing(marqueeSelection);
			}
		}
		super.destroy();
	}

	public void mouseDown(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
		if(marqueeSelection != null){
			getBNAModel().removeThing(marqueeSelection);
			marqueeSelection = null;
		}
		if(evt.button == 1){
			if(t == null){
				initDownX = worldX;
				initDownY = worldY;

				marqueeSelection = new MarqueeBoxBorderThing();
				marqueeSelection.setBoundingBox(new Rectangle(worldX, worldY, 1, 1));

				getBNAModel().addThing(marqueeSelection);
			}
		}
	}

	public void mouseMove(IBNAView view, MouseEvent e, IThing t, int worldX, int worldY){
		if(marqueeSelection != null){
			int x1 = Math.min(initDownX, worldX);
			int x2 = Math.max(initDownX, worldX);
			int y1 = Math.min(initDownY, worldY);
			int y2 = Math.max(initDownY, worldY);

			marqueeSelection.setBoundingBox(new Rectangle(x1, y1, x2 - x1, y2 - y1));
		}
	}

	public void mouseUp(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
		try{
			if(evt.button == 1){
				if(marqueeSelection != null){
					Rectangle selectionRectangle = marqueeSelection.getBoundingBox();
					selectionRectangle = BNAUtils.normalizeRectangle(selectionRectangle);

					//getBNAModel().removeThing(marqueeSelection);
					IHasMutableSelected[] allSelectableThings = tttl.getThings(IHasMutableSelected.class);
					if(!BNAUtils.wasControlPressed(evt)){
						for(IHasMutableSelected possiblySelectedThing: allSelectableThings){
							possiblySelectedThing.setSelected(false);
						}
					}

					IBNAModel model = getBNAModel();
					model.beginBulkChange();
					try{
						for(IHasMutableSelected st: allSelectableThings){
							if(st instanceof IHasMutableSelected){
								if(st instanceof IHasBoundingBox && UserEditableUtils.isEditableForAllQualities(st, IHasMutableSelected.USER_MAY_SELECT)){
									Rectangle r = ((IHasBoundingBox)st).getBoundingBox();
									if(BNAUtils.isWithin(selectionRectangle, r)){
										if(!BNAUtils.wasControlPressed(evt)){
											st.setSelected(true);
										}
										else{
											st.setSelected(!st.isSelected());
										}
									}
								}
							}
						}
					}
					finally{
						model.endBulkChange();
					}
				}
			}
		}
		finally{
			if(marqueeSelection != null){
				getBNAModel().removeThing(marqueeSelection);
				marqueeSelection = null;
			}
		}
	}

	public void mouseClick(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
	}

	public void mouseDoubleClick(IBNAView view, MouseEvent evt, IThing t, int worldX, int worldY){
	}
}
