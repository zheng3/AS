package edu.uci.isr.archstudio4.comp.archipelago.interactions.logics.editing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

public class MethodList {
	private Collection<MethodLabel> methodLabels;
	
	public MethodList(IMethod[] methodList) {
		this.methodLabels = new ArrayList<MethodLabel>();
		for(IMethod method : methodList) {
			add(method);
		}
	}
	
	public boolean add(IMethod m){
		return this.methodLabels.add(new MethodLabel(m));
	}
	
	public Collection<MethodLabel> getMethodLabels(){
		return methodLabels;
	}
}
