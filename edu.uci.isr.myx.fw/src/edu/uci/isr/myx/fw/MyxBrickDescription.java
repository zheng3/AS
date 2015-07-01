package edu.uci.isr.myx.fw;

import java.util.Properties;

public class MyxBrickDescription implements IMyxBrickDescription {

	protected java.util.Properties initParams;

	public MyxBrickDescription(Properties initParams) {
		this.initParams = initParams != null ? initParams : new java.util.Properties();
	}

	public java.util.Properties getInitParams() {
		return initParams;
	}
}
