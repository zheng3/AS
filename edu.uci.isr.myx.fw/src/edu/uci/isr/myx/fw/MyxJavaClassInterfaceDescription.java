package edu.uci.isr.myx.fw;

public class MyxJavaClassInterfaceDescription implements IMyxInterfaceDescription {

	protected String[] serviceObjectInterfaceNames;

	public MyxJavaClassInterfaceDescription(String[] serviceObjectInterfaceNames) {
		this.serviceObjectInterfaceNames = serviceObjectInterfaceNames.clone();
	}

	public String[] getServiceObjectInterfaceNames() {
		return serviceObjectInterfaceNames.clone();
	}
}
