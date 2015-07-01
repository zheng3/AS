package edu.uci.isr.myx.fw;

import java.util.*;

class MyxInterfaceRepository{

	protected Map<IMyxBrick,List<MyxInterface>> brickToInterfaceListMap = 
		Collections.synchronizedMap(new HashMap<IMyxBrick,List<MyxInterface>>());
	
	public MyxInterfaceRepository(){
	}
	
	public void addInterface(IMyxBrick b, IMyxName interfaceName, IMyxInterfaceDescription interfaceDescription, EMyxInterfaceDirection interfaceDirection){
		synchronized(brickToInterfaceListMap){
			List<MyxInterface> l = brickToInterfaceListMap.get(b);
			if(l == null){
				brickToInterfaceListMap.put(b, l = new ArrayList<MyxInterface>());
			}
			MyxInterface mi = new MyxInterface(interfaceName, interfaceDescription, interfaceDirection);
			l.add(mi);
		}
	}
	
	public void addInterface(IMyxBrick b, IMyxName interfaceName, IMyxInterfaceDescription interfaceDescription, EMyxInterfaceDirection interfaceDirection, IMyxName internalBrickName, IMyxName internalInterfaceName){
		synchronized(brickToInterfaceListMap){
			List<MyxInterface> l = brickToInterfaceListMap.get(b);
			if(l == null){
				brickToInterfaceListMap.put(b, l = new ArrayList<MyxInterface>());
			}
			MyxInterface mi = new MyxMappedInterface(interfaceName, interfaceDescription, interfaceDirection, internalBrickName, internalInterfaceName);
			l.add(mi);
		}
	}
	
	public void removeInterface(IMyxBrick b, IMyxName interfaceName){
		synchronized(brickToInterfaceListMap){
			List<MyxInterface> l = brickToInterfaceListMap.get(b);
			if(l == null){
				return;
			}
			MyxInterface doomedInterface = null;
			for(Iterator<MyxInterface> it = l.iterator(); it.hasNext(); ){
				MyxInterface mi = it.next();
				IMyxName name = mi.getInterfaceName();
				if(name.equals(interfaceName)){
					doomedInterface = mi;
					break;
				}
			}
			l.remove(doomedInterface);
			if(l.size() == 0){
				brickToInterfaceListMap.remove(b);
			}
		}
	}
	
	public IMyxName[] getAllInterfaceNames(IMyxBrick b){
		synchronized(brickToInterfaceListMap){
			List<MyxInterface> l = brickToInterfaceListMap.get(b);
			if(l == null){
				return new IMyxName[0];
			}
			IMyxName[] names = new IMyxName[l.size()];
			int i = 0;
			for(Iterator<MyxInterface> it = l.iterator(); it.hasNext(); ){
				names[i++] = it.next().getInterfaceName();
			}
			return names;
		}
	}
	
	protected MyxInterface getMyxInterface(IMyxBrick b, IMyxName interfaceName){
		List<MyxInterface> l = brickToInterfaceListMap.get(b);
		if(l != null){
			for (Iterator<MyxInterface> it = l.iterator(); it.hasNext();) {
				MyxInterface mi = it.next();
				IMyxName name = mi.getInterfaceName();
				if (name.equals(interfaceName)) {
					return mi;
				}
			}
		}
		return null;
	}
	
	public IMyxInterfaceDescription getInterfaceDescription(IMyxBrick b, IMyxName interfaceName){
		synchronized(brickToInterfaceListMap){
			MyxInterface mi = getMyxInterface(b, interfaceName);
			if(mi != null){
				return mi.getInterfaceDescription();
			}
			return null;
		}
	}

	public EMyxInterfaceDirection getInterfaceDirection(IMyxBrick b, IMyxName interfaceName){
		synchronized(brickToInterfaceListMap){
			MyxInterface mi = getMyxInterface(b, interfaceName);
			if(mi != null){
				return mi.getInterfaceDirection();
			}
			return null;
		}
	}
	
/*	
	public IMyxName getInternalBrickName(IMyxBrick b, IMyxName interfaceName){
		synchronized(brickToInterfaceListMap){
			MyxInterface mi = getMyxInterface(b, interfaceName);
			if(mi != null){
				if (mi instanceof MyxMappedInterface) {
					MyxMappedInterface mmi = (MyxMappedInterface) mi;
					return mmi.getInternalBrickName();
				}
			}
			return null;
		}
	}

	public IMyxName getInternalInterfaceName(IMyxBrick b, IMyxName interfaceName){
		synchronized(brickToInterfaceListMap){
			MyxInterface mi = getMyxInterface(b, interfaceName);
			if(mi != null){
				if (mi instanceof MyxMappedInterface) {
					MyxMappedInterface mmi = (MyxMappedInterface) mi;
					return mmi.getInternalInterfaceName();
				}
			}
			return null;
		}		
	}
*/
	
	public IMyxName[] getInternalBrickName(IMyxBrick b, IMyxName interfaceName){
		synchronized(brickToInterfaceListMap){
			List<MyxInterface> l = brickToInterfaceListMap.get(b);
			if(l != null){
				int i = 0;
				for (Iterator<MyxInterface> it = l.iterator(); it.hasNext();) {
					MyxInterface mi = it.next();
					IMyxName name = mi.getInterfaceName();
					if (name.equals(interfaceName)) {
						if (mi instanceof MyxMappedInterface) {
							i++;
						}
					}
				}
				IMyxName[] intf = new IMyxName[i];
				i = 0;
				for (Iterator<MyxInterface> it = l.iterator(); it.hasNext();) {
					MyxInterface mi = it.next();
					IMyxName name = mi.getInterfaceName();
					if (name.equals(interfaceName)) {
						if (mi instanceof MyxMappedInterface) {
							MyxMappedInterface mmi = (MyxMappedInterface) mi;
							intf[i++] = mmi.getInternalBrickName();
						}
					}
				}
				return intf;
			}
		}
		return null;
	}

	public IMyxName[] getInternalInterfaceName(IMyxBrick b, IMyxName interfaceName){
		synchronized(brickToInterfaceListMap){
			List<MyxInterface> l = brickToInterfaceListMap.get(b);
			if(l != null){
				int i = 0;
				for (Iterator<MyxInterface> it = l.iterator(); it.hasNext();) {
					MyxInterface mi = it.next();
					IMyxName name = mi.getInterfaceName();
					if (name.equals(interfaceName)) {
						if (mi instanceof MyxMappedInterface) {
							i++;
						}
					}
				}
				IMyxName[] intf = new IMyxName[i];
				i = 0;
				for (Iterator<MyxInterface> it = l.iterator(); it.hasNext();) {
					MyxInterface mi = it.next();
					IMyxName name = mi.getInterfaceName();
					if (name.equals(interfaceName)) {
						if (mi instanceof MyxMappedInterface) {
							MyxMappedInterface mmi = (MyxMappedInterface) mi;
							intf[i++] = mmi.getInternalInterfaceName();
						}
					}
				}
				return intf;
			}
		}
		return null;
	}
	
	private static class MyxInterface{
		protected IMyxName interfaceName;
		protected IMyxInterfaceDescription interfaceDescription;
		protected EMyxInterfaceDirection interfaceDirection;
		
		public MyxInterface(IMyxName interfaceName, IMyxInterfaceDescription interfaceDescription, EMyxInterfaceDirection interfaceDirection){
			this.interfaceName = interfaceName;
			this.interfaceDescription = interfaceDescription;
			this.interfaceDirection = interfaceDirection;
		}

		public IMyxName getInterfaceName(){
			return interfaceName;
		}

		public void setInterfaceName(IMyxName interfaceName){
			this.interfaceName = interfaceName;
		}

		public EMyxInterfaceDirection getInterfaceDirection() {
			return interfaceDirection;
		}

		public void setInterfaceDirection(EMyxInterfaceDirection interfaceDirection) {
			this.interfaceDirection = interfaceDirection;
		}

		public IMyxInterfaceDescription getInterfaceDescription() {
			return interfaceDescription;
		}

		public void setInterfaceDescription(IMyxInterfaceDescription interfaceDescription) {
			this.interfaceDescription = interfaceDescription;
		}
	}
	
	private static class MyxMappedInterface extends MyxInterface{
		protected IMyxName internalBrickName;
		protected IMyxName internalInterfaceName;
		
		public MyxMappedInterface(IMyxName interfaceName, IMyxInterfaceDescription interfaceDescription, EMyxInterfaceDirection interfaceDirection, IMyxName internalBrickName, IMyxName internalInterfaceName){
			super(interfaceName, interfaceDescription, interfaceDirection);
			this.internalBrickName = internalBrickName;
			this.internalInterfaceName = internalInterfaceName;
		}

		public IMyxName getInternalBrickName(){
			return internalBrickName;
		}

		public void setInternalBrickName(IMyxName internalBrickName){
			this.internalBrickName = internalBrickName;
		}

		public IMyxName getInternalInterfaceName(){
			return internalInterfaceName;
		}

		public void setInternalInterfaceName(IMyxName internalInterfaceName){
			this.internalInterfaceName = internalInterfaceName;
		}
	}
}
