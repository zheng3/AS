package edu.uci.isr.myx.fw;

import java.util.Properties;

public class MyxJavaClassBrickDescription extends MyxBrickDescription implements IMyxBrickDescription{

	protected String mainBrickClassName;
	
	public MyxJavaClassBrickDescription(Properties initParams, String mainBrickClassName) {
		super(initParams);
		this.mainBrickClassName = mainBrickClassName;
	}
	
	public String getMainBrickClassName(){
		return mainBrickClassName;
	}

}
