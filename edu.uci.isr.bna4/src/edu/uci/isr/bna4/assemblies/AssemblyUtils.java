package edu.uci.isr.bna4.assemblies;

import java.util.ArrayList;
import java.util.List;

import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.facets.IHasAssemblyData;

public class AssemblyUtils{

	@SuppressWarnings("unchecked")
	public static <T extends IAssembly>T getAssemblyWithRoot(IThing rootThing){
		if(rootThing instanceof IHasAssemblyData){
			return (T)((IHasAssemblyData)rootThing).getAssembly();
		}
		return null;
	}

	public static IAssembly[] getAssembliesWithRoots(IThing[] rootThings){
		List<IAssembly> assemblies = new ArrayList<IAssembly>();
		for(IThing thing: rootThings){
			IAssembly assembly = getAssemblyWithRoot(thing);
			if(assembly != null){
				assemblies.add(assembly);
			}
		}
		return assemblies.toArray(new IAssembly[assemblies.size()]);
	}

	@SuppressWarnings("unchecked")
	public static <T extends IAssembly>T getAssemblyWithRoot(IThing[] rootThings, Object kind){
		for(IThing rootThing: rootThings){
			IAssembly assembly = getAssemblyWithRoot(rootThing);
			if(assembly != null && assembly.isKind(kind)){
				return (T)assembly;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends IAssembly>T getAssemblyWithRoot(IThing[] rootThings, Class<?> kindClass){
		for(IThing rootThing: rootThings){
			IAssembly assembly = getAssemblyWithRoot(rootThing);
			if(assembly != null && kindClass.isInstance(assembly.getKind())){
				return (T)assembly;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends IAssembly>T getAssemblyWithPart(IThing partThing){
		if(partThing != null){
			AssembledPartData apd = partThing.getProperty(AssembledPartData.ASSEMBLED_PART_DATA_PROPERTY_NAME);
			if(apd != null){
				return (T)apd.getAssembly();
			}
		}
		return null;
	}

	public static IAssembly[] getAssembliesWithParts(IThing[] partThings){
		List<IAssembly> assemblies = new ArrayList<IAssembly>();
		for(IThing thing: partThings){
			IAssembly assembly = getAssemblyWithPart(thing);
			if(assembly != null){
				assemblies.add(assembly);
			}
		}
		return assemblies.toArray(new IAssembly[assemblies.size()]);
	}

	public static IThing getRootThingWithPart(IThing partThing){
		if(partThing != null){
			AssembledPartData apd = partThing.getProperty(AssembledPartData.ASSEMBLED_PART_DATA_PROPERTY_NAME);
			if(apd != null){
				IAssembly assembly = apd.getAssembly();
				if(assembly != null){
					return assembly.getRootThing();
				}
			}
		}
		return null;
	}

	public static String getPartName(IThing partThing){
		if(partThing != null){
			AssembledPartData apd = partThing.getProperty(AssembledPartData.ASSEMBLED_PART_DATA_PROPERTY_NAME);
			if(apd != null){
				return apd.getPartName();
			}
		}
		return null;
	}

	public final static IThing getThing(IAssembly assembly, String[] parts){
		for(int i = 0; i < parts.length; i++){
			if(assembly == null){
				break;
			}
			IThing thing = assembly.getPart(parts[i]);
			if(i == parts.length - 1){
				return thing;
			}
			assembly = AssemblyUtils.getAssemblyWithRoot(thing);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public final static <T>T getPart(IAssembly assembly, String partName){
		if(assembly != null){
			return (T)assembly.getPart(partName);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public final static <T>T getPart(IAssembly assembly, String partName, Class<? extends T> partClass){
		if(assembly != null){
			IThing partThing = assembly.getPart(partName);
			if(partClass.isInstance(partThing)){
				return (T)partThing;
			}
		}
		return null;
	}
}
