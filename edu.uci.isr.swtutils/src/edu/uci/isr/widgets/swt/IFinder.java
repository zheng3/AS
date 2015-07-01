package edu.uci.isr.widgets.swt;

public interface IFinder<C>{
	public IFindResult[] find(C context, String search);
	public void selected(IFindResult selectedResult);
}
