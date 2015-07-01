package edu.uci.isr.bna4.assemblies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.facets.IHasAssemblyData;
import edu.uci.isr.bna4.facets.IHasMutableAssemblyData;
import edu.uci.isr.bna4.things.utility.AssemblyRootThing;

public abstract class AbstractAssembly
	implements IAssembly{

	private static boolean equalz(Object o1, Object o2){
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	protected IBNAModel model;
	protected IHasMutableAssemblyData rootThing;
	private Map<String, IThing> parts;

	protected AbstractAssembly(IBNAModel model, IThing parentThing, Object assemblyKind){
		this.model = model;
		this.rootThing = new AssemblyRootThing();
		this.parts = new HashMap<String, IThing>();

		rootThing.setAssembly(this);
		rootThing.setAssemblyKind(assemblyKind);

		model.addThing(rootThing, parentThing);
	}

	public IHasAssemblyData getRootThing(){
		return rootThing;
	}

	public boolean isKind(Object kind){
		return equalz(kind, rootThing.getAssemblyKind());
	}

	public Object getKind(){
		return rootThing.getAssemblyKind();
	}

	@SuppressWarnings("unchecked")
	public <T extends IThing>T getPart(String name){
		return (T)parts.get(name);
	}

	@SuppressWarnings("unused")
	public <T extends IAssembly>T getAssemblyPart(String name){
		return AssemblyUtils.getAssemblyWithRoot(getPart(name));
	}

	public IThing[] getParts(){
		List<IThing> p = new ArrayList<IThing>(parts.size());
		for(IThing thing: parts.values()){
			if(thing != null){
				p.add(thing);
			}
		}
		return p.toArray(new IThing[p.size()]);
	}

	public void markPart(String name, IThing thing){
		IThing oldThing = parts.put(name, thing);
		if(!equalz(oldThing == null ? null : oldThing.getID(), thing == null ? null : thing.getID())){
			if(oldThing != null){
				oldThing.removeProperty(AssembledPartData.ASSEMBLED_PART_DATA_PROPERTY_NAME);
			}
			if(thing != null){
				thing.setProperty(AssembledPartData.ASSEMBLED_PART_DATA_PROPERTY_NAME, new AssembledPartData(this, name));
			}
		}
	}

	public void markPart(String name, IAssembly assembly){
		markPart(name, assembly.getRootThing());
	}

	public void unmarkPart(String name){
		markPart(name, (IThing)null);
	}

	@Override
	public int hashCode(){
		return rootThing.getID().hashCode();
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof IAssembly){
			if(rootThing.getID().equals(((IAssembly)o).getRootThing().getID())){
				return true;
			}
		}
		return false;
	}

	public void remove(boolean andChildren){
		for(IThing part: parts.values()){
			remove(part, andChildren);
		}
		remove(rootThing, andChildren);
	}

	private void remove(IThing t, boolean andChildren){
		if(andChildren){
			model.removeThingAndChildren(t);
		}
		else{
			model.removeThing(t);
		}
	}

	public void setPropertyOnAllParts(IBNAModel model, String propertyName, Object value, boolean andChildren){
		for(IThing partThing: getParts()){
			setProperty(model, partThing, propertyName, value, andChildren);
		}
	}

	private static void setProperty(IBNAModel model, IThing t, String propertyName, Object value, boolean andChildren){
		t.setProperty(propertyName, value);
		if(andChildren){
			for(IThing ct: model.getChildThings(t)){
				setProperty(model, ct, propertyName, value, andChildren);
			}
		}
	}
}
