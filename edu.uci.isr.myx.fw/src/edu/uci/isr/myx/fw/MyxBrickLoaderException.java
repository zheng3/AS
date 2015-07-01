package edu.uci.isr.myx.fw;

public class MyxBrickLoaderException extends Exception implements java.io.Serializable{

	public MyxBrickLoaderException(){
		super();
	}

	public MyxBrickLoaderException(String message){
		super(message);
	}

	public MyxBrickLoaderException(Throwable cause){
		super(cause);
	}

	public MyxBrickLoaderException(String message, Throwable cause){
		super(message, cause);
	}

}
