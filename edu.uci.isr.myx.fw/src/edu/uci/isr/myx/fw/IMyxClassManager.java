package edu.uci.isr.myx.fw;

public interface IMyxClassManager{
	public Class classForName(String className) throws ClassNotFoundException;
}
