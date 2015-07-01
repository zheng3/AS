package edu.uci.isr.archstudio4.comp.xmapper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CodeGenParameters implements Serializable {
	
	protected Map<String,String> archPrescribedClassName;
	protected Map<String,String> userDefinedClassName;
	
	public CodeGenParameters (){
		archPrescribedClassName = new HashMap<String,String>();
		userDefinedClassName = new HashMap<String,String>();
	}
	
	public void setArchPrescribedClassName(String compId, String apcn){
		archPrescribedClassName.put(compId, apcn);
	}
	
	public String getArchPrescribedClassName(String compId){
		return archPrescribedClassName.get(compId);
	}

	public void setUserDefinedClassName(String compId, String udcn){
		userDefinedClassName.put(compId, udcn);
	}
	
	public String getUserDefinedClassName(String compId){
		return userDefinedClassName.get(compId);
	}
}
