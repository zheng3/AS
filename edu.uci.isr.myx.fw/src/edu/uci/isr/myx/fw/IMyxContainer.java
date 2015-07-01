package edu.uci.isr.myx.fw;

public interface IMyxContainer extends IMyxBrick{
		
	public void addInternalBrick(IMyxBrick brick);
	public void removeInternalBrick(IMyxBrick brick);
	public IMyxBrick[] getInternalBricks();
	public IMyxBrick getInternalBrick(IMyxName brickName);
}
