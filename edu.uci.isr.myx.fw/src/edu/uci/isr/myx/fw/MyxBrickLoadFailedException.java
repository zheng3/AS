package edu.uci.isr.myx.fw;

public class MyxBrickLoadFailedException extends Exception{

	public MyxBrickLoadFailedException(){
		super();
	}

	public MyxBrickLoadFailedException(String message){
		super(message);
	}

	public MyxBrickLoadFailedException(Throwable cause){
		super(cause);
	}

	public MyxBrickLoadFailedException(String message, Throwable cause){
		super(message, cause);
	}

}
