package edu.uci.isr.archstudio4.comp.selectordriver;

import org.eclipse.swt.graphics.Image;

import edu.uci.isr.archstudio4.comp.archipelago.ArchipelagoMyxComponent;
import edu.uci.isr.archstudio4.comp.guardtracker.GuardTrackerImpl;
import edu.uci.isr.archstudio4.comp.pruner.IPruner;
import edu.uci.isr.archstudio4.comp.selector.ISelector;
import edu.uci.isr.archstudio4.comp.versionpruner.IVersionPruner;
import edu.uci.isr.archstudio4.editors.AbstractArchstudioEditorMyxComponent;
import edu.uci.isr.archstudio4.launcher.ILaunchData;
import edu.uci.isr.archstudio4.launcher.LaunchData;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxUtils;

public class SelectorDriverMyxComponent extends AbstractArchstudioEditorMyxComponent{
	
	public static final String EDITOR_NAME = "Selector";
	public static final String ECLIPSE_EDITOR_ID = "edu.uci.isr.archstudio4.comp.selectordriver.SelectorDriverEditor";
	
	public static final String IMAGE_SELECTOR_ICON = "selector:icon";
	public static final String URL_SELECTOR_ICON = "res/selector-icon-32.gif";

	public static final IMyxName INTERFACE_NAME_OUT_SELECTOR = MyxUtils.createName("selector");
	public static final IMyxName INTERFACE_NAME_OUT_PRUNER = MyxUtils.createName("pruner");
	public static final IMyxName INTERFACE_NAME_OUT_VERSIONPRUNER = MyxUtils.createName("versionpruner");
	
	//varun
//	public static final IMyxName INTERFACE_NAME_OUT_GUARDTRACKER = MyxUtils.createName("guardtracker");

	protected ISelector selector = null;
	protected IPruner pruner = null;
	protected IVersionPruner versionpruner = null;
	
	//varun
	protected GuardTrackerImpl guardTraker = null;

	public SelectorDriverMyxComponent(){
		super(EDITOR_NAME, ECLIPSE_EDITOR_ID, true);
	}
	
	public void begin(){
		super.begin();
		selector = (ISelector)MyxUtils.getFirstRequiredServiceObject(this, INTERFACE_NAME_OUT_SELECTOR);
		pruner = (IPruner)MyxUtils.getFirstRequiredServiceObject(this, INTERFACE_NAME_OUT_PRUNER);
		versionpruner = (IVersionPruner)MyxUtils.getFirstRequiredServiceObject(this, INTERFACE_NAME_OUT_VERSIONPRUNER);
		guardTraker = (GuardTrackerImpl)MyxUtils.getFirstRequiredServiceObject(this, ArchipelagoMyxComponent.INTERFACE_NAME_OUT_GUARDTRACKER);
		resources.createImage(IMAGE_SELECTOR_ICON, SelectorDriverMyxComponent.class.getResourceAsStream(URL_SELECTOR_ICON));
	}
	
	public GuardTrackerImpl getGuardTraker() {
		return guardTraker;
	}

	public void setGuardTraker(GuardTrackerImpl guardTraker) {
		this.guardTraker = guardTraker;
	}

	protected ISelector getSelector(){
		return selector;
	}
	
	public Image getIcon(){
		return resources.getImage(IMAGE_SELECTOR_ICON);
	}

	public IPruner getPruner(){
		return pruner;
	}

	public IVersionPruner getVersionPruner(){
		return versionpruner;
	}
	
	public ILaunchData getLaunchData(){
		return new LaunchData(ECLIPSE_EDITOR_ID, EDITOR_NAME, "Product-Line Selector Tool", getIcon(), ILaunchData.EDITOR);
	}
	
	
	
}
