package edu.uci.isr.myx.fw;

public interface IMyxBrickFactory{
	public IMyxBrick create(IMyxName name, IMyxBrickDescription brickDescription) throws MyxBrickCreationException;
}
