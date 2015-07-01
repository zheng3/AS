package edu.uci.isr.archstudio4.comp.archipelago.interactions.logics.editing;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.Signature;

public class MethodLabel {

	private String methodName;
	private String[] paramTypes;
	private String returnType;
	
	MethodLabel(IMethod method) {
		try {
			this.methodName = method.getElementName();
			this.paramTypes = method.getParameterTypes();
			this.returnType = Signature.toString(method.getReturnType());
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	String getMethodName() {
		return this.methodName;
	}
	
	String toLabelString(){
		String out = methodName+"(";
		if (paramTypes != null){
			int n = paramTypes.length;
			if (n>0){
				out += Signature.toString(paramTypes[0]);
				for (int i=1;i<n;i++){
					out +=", "+Signature.toString(paramTypes[i]);
				}
			}
		}
		out += "): "+returnType;
		return out;
	}
}
