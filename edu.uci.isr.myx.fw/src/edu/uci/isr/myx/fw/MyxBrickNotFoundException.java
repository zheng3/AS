package edu.uci.isr.myx.fw;

public class MyxBrickNotFoundException extends Exception{

	public MyxBrickNotFoundException(){
		super();
	}

	public MyxBrickNotFoundException(String message, Throwable cause){
		super(message, cause);
	}

	public MyxBrickNotFoundException(String message){
		super(message);
	}

	public MyxBrickNotFoundException(Throwable cause){
		super(cause);
	}

}
