package edu.uci.isr.bna4.logics.events;

public interface IDragMoveListener{

	public void dragStarted(DragMoveEvent evt);

	public void dragMoved(DragMoveEvent evt);

	public void dragFinished(DragMoveEvent evt);
}
