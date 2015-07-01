package edu.uci.isr.bna4.things.swt;

public class SWTComboThing
	extends AbstractSWTOptionSelectionThing{

	public SWTComboThing(){
		this(null);
	}

	public SWTComboThing(String id){
		super(id);
	}

	@Override
	protected void initProperties(){
		super.initProperties();
		setAllowsArbitraryInput(true);
	}

	public void setAllowsArbitraryInput(boolean allowsArbitraryInput){
		setProperty("allowsArbitraryInput", allowsArbitraryInput);
	}

	public boolean getAllowsArbitraryInput(){
		return getProperty("allowsArbitraryInput");
	}
}
