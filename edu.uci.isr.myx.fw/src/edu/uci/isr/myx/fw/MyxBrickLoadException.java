package edu.uci.isr.myx.fw;

public class MyxBrickLoadException extends Exception{

	protected Throwable[] causes;
	
	public MyxBrickLoadException(String message, Throwable[] causes){
		super(message);
		this.causes = causes;
		if((causes != null) && (causes.length > 0)){
			this.initCause(causes[causes.length - 1]);
		}
	}
	
	public Throwable[] getCauses(){
		return causes;
	}

}
