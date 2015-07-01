package edu.uci.isr.xarchflat;

public class InvalidObjectTypeException extends RuntimeException{

	private String expectedType = null;
	private String actualType = null;
	
	public InvalidObjectTypeException(){
	}
		
	public InvalidObjectTypeException(Class expectedType, Class actualType){
		this.expectedType = expectedType != null ? expectedType.getName() : null;
		this.actualType = actualType != null ? actualType.getName() : null;
	}
	
	public InvalidObjectTypeException(String expectedType, String actualType){
		this.expectedType = expectedType;
		this.actualType = actualType;
	}
	
	public String toString(){
		if(expectedType == null){
			return "Invalid object type.";
		}
		else{
			return "Invalid object type.  Expected " + expectedType + "; encountered " + actualType;
		}
	}

}

