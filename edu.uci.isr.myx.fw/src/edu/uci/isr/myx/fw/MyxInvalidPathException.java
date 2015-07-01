package edu.uci.isr.myx.fw;

public class MyxInvalidPathException extends IllegalArgumentException{

	public MyxInvalidPathException(IMyxName[] path){
		super("Invalid path: " + MyxUtils.pathToString(path));
	}


}
