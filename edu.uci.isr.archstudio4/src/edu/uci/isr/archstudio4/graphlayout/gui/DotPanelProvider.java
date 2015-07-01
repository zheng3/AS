package edu.uci.isr.archstudio4.graphlayout.gui;

public class DotPanelProvider implements IGraphLayoutParameterPanelProvider{
	protected IGraphLayoutParameterPanel[] panels = new IGraphLayoutParameterPanel[]{
		new SizeAndScaleParameterPanel(),
		new GeneralOptionsParameterPanel(),
		new OrientInterfacesParameterPanel(),
		new DotSpacingParameterPanel()
	};
	
	public String getEngineID(){
		return "dot";
	}
	
	public IGraphLayoutParameterPanel[] getPanels(){
		return panels;
	}
}
