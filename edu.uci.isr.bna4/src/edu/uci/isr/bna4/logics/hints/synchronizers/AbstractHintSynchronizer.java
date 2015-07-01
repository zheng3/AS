package edu.uci.isr.bna4.logics.hints.synchronizers;

import java.util.List;
import java.util.regex.Pattern;

import edu.uci.isr.bna4.IBNAWorld;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.assemblies.AssemblyUtils;
import edu.uci.isr.bna4.assemblies.IAssembly;
import edu.uci.isr.bna4.logics.hints.IHintRepository;
import edu.uci.isr.bna4.logics.hints.IHintSynchronizer;

public abstract class AbstractHintSynchronizer
	implements IHintSynchronizer{

	protected static final Pattern pathSplitPattern = Pattern.compile("\\/");

	protected IBNAWorld world;

	public AbstractHintSynchronizer(){
	}

	public void setBNAWorld(IBNAWorld world){
		this.world = world;
	}

	protected final IThing getThing(IAssembly assembly, String[] parts){
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

	protected final Object getFirstHint(IHintRepository repository, Object context, List<String> hintNames){
		for(String hintName: hintNames){
			Object hintValue = repository.getHint(context, hintName);
			if(hintValue != null){
				return hintValue;
			}
		}
		return null;
	}

	protected void restoreHint(IHintRepository repository, Object context, String partPath, String[] parts, IThing thing, Object propertyName, String hintName, Object hintValue){
		if(hintValue != null){
			thing.setProperty(propertyName, hintValue);
		}
	}

	protected void storeHint(IHintRepository repository, Object context, String hintName, IThing thing, Object propertyName, Object value){
		if(value != null){
			repository.storeHint(context, hintName, value);
		}
	}
}
