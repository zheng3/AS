package edu.uci.isr.bna4;

/**
 * Receives model events asynchronously.
 */
public interface IBNAModelListener{

	public void bnaModelChanged(BNAModelEvent evt);
}
