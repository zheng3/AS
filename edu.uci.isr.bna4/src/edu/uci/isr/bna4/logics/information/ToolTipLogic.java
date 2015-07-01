package edu.uci.isr.bna4.logics.information;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAMouseMoveListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;

public class ToolTipLogic
	extends AbstractThingLogic
	implements IBNAMouseMoveListener{

	public static final String USER_MAY_EDIT_TOOL_TIP = "userMayEditToolTip";

	public static final String TOOL_TIP_PROPERTY_NAME = "toolTip";

	public static final void setToolTip(IThing thing, String toolTip){
		thing.setProperty(TOOL_TIP_PROPERTY_NAME, toolTip);
	}

	public static final String getToolTip(IThing thing){
		return thing == null ? null : (String)thing.getProperty(TOOL_TIP_PROPERTY_NAME);
	}

	protected IThing lastThing = null;

	public void mouseMove(IBNAView view, MouseEvent e, IThing t, int worldX, int worldY){
		if(t != lastThing){
			lastThing = t;
			Composite c = BNAUtils.getParentComposite(view);
			if(c != null){
				String toolTip = ToolTipLogic.getToolTip(t);
				if(!BNAUtils.nulleq(toolTip, c.getToolTipText())){
					c.setToolTipText(toolTip);
				}
			}
		}
	}
}
