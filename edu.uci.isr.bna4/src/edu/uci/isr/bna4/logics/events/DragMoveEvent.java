package edu.uci.isr.bna4.logics.events;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;

import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;

public class DragMoveEvent{

	private final IThing initialThing;
	private final int initialWorldX;
	private final int initialWorldY;
	private final IBNAView view;
	private final MouseEvent evt;
	private final int worldX;
	private final int worldY;
	private int adjustedWorldX;
	private int adjustedWorldY;

	public DragMoveEvent(IThing initialThing, int initialWorldX, int initialWorldY, IBNAView view, MouseEvent evt){
		this.initialThing = initialThing;
		this.initialWorldX = initialWorldX;
		this.initialWorldY = initialWorldY;
		this.view = view;
		this.evt = evt;
		this.worldX = initialWorldX;
		this.worldY = initialWorldY;
		this.adjustedWorldX = worldX;
		this.adjustedWorldY = worldY;
	}

	public DragMoveEvent(DragMoveEvent initialEvent, IBNAView view, MouseEvent evt, int worldX, int worldY){
		this.initialThing = initialEvent.initialThing;
		this.initialWorldX = initialEvent.initialWorldX;
		this.initialWorldY = initialEvent.initialWorldY;
		this.view = view;
		this.evt = evt;
		this.worldX = worldX;
		this.worldY = worldY;
		this.adjustedWorldX = worldX;
		this.adjustedWorldY = worldY;
	}

	public IThing getInitialThing(){
		return initialThing;
	}

	public int getInitialWorldX(){
		return initialWorldX;
	}

	public int getInitialWorldY(){
		return initialWorldY;
	}

	public Point getInitialWorldPoint(){
		return new Point(initialWorldX, initialWorldY);
	}

	public IBNAView getView(){
		return view;
	}

	public MouseEvent getEvt(){
		return evt;
	}

	public int getWorldX(){
		return worldX;
	}

	public int getWorldY(){
		return worldY;
	}

	public Point getWorldPoint(){
		return new Point(worldX, worldY);
	}

	public int getAdjustedWorldX(){
		return adjustedWorldX;
	}

	public void setAdjustedWorldX(int adjustedWorldX){
		this.adjustedWorldX = adjustedWorldX;
	}

	public int getAdjustedWorldY(){
		return adjustedWorldY;
	}

	public void setAdjustedWorldY(int adjustedWorldY){
		this.adjustedWorldY = adjustedWorldY;
	}

	public Point getAdjustedWorldPoint(){
		return new Point(adjustedWorldX, adjustedWorldY);
	}

	public void setAdjustedWorldPoint(Point newAdjustedWorldPoint){
		this.adjustedWorldX = newAdjustedWorldPoint.x;
		this.adjustedWorldY = newAdjustedWorldPoint.y;
	}
}
