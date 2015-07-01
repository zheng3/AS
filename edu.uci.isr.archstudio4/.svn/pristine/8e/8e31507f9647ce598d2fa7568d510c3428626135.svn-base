package edu.uci.isr.archstudio4.comp.archipelago.interactions.things;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.Signature;

import edu.uci.isr.xarchflat.ObjRef;

public class OperationLabel {
	
	private String label;
	private ObjRef intf;
	private String source;
	
	private String className;
	private String mthdName;
	
	OperationLabel(IMethod m, ObjRef parent, String classN){
		StringBuffer sb = new StringBuffer();
		sb.append(m.getElementName());
		sb.append("(");
		String[] paramTypes = m.getParameterTypes();
		if (paramTypes != null){
			int n = paramTypes.length;
			if (n>0){
				sb.append(Signature.toString(paramTypes[0]));
				for (int i=1;i<n;i++){
					sb.append(", ");
					sb.append(Signature.toString(paramTypes[i]));
				}
			}
		}
		sb.append("): ");
		try {
			sb.append(Signature.toString(m.getReturnType()));
			source = m.getSource();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		label = sb.toString();
		intf = parent;
		
		className = classN;
		mthdName = m.getElementName();
	}
	
	public String getClassName(){
		return className;
	}
	
	public String getMthdName(){
		return mthdName;
	}
	
	public ObjRef getInterface(){
		return intf;
	}
	
	public String getSource(){
		return source;
	}
	
	public String toLabelString(){
		return this.label;
	}
}
