package edu.uci.isr.bna4.logics.editing;

import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.logics.coordinating.MaintainStickyPointLogic.StickyMode;

public interface IStickyModeSpecifier{

	public StickyMode[] getAllowableStickyModes(IThing thing, String propertyName, IThing stickToThing);

}