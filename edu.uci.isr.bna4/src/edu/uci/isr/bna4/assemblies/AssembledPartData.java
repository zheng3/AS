package edu.uci.isr.bna4.assemblies;

public class AssembledPartData{

	public static final String ASSEMBLED_PART_DATA_PROPERTY_NAME = "assembledPartData";

	IAssembly assembly;
	String partName;

	public AssembledPartData(IAssembly assembly, String partName){
		this.assembly = assembly;
		this.partName = partName;
	}

	public IAssembly getAssembly(){
		return assembly;
	}

	public String getPartName(){
		return partName;
	}

	@Override
	public int hashCode(){
		return partName.hashCode() ^ assembly.hashCode();
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof AssembledPartData){
			AssembledPartData apd = (AssembledPartData)o;
			if(partName.equals(apd.partName)){
				if(assembly.equals(apd.assembly)){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString(){
		return assembly.getClass().getName() + "/" + partName;
	}
}