package edu.uci.isr.myx.fw;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class MyxBasicRuntime implements IMyxRuntime, IMyxClassManager{
	protected IMyxContainer mainContainer = new MyxContainer();

	protected List<BrickLoaderEntry> brickLoaders = 
		Collections.synchronizedList(new ArrayList<BrickLoaderEntry>());
	protected Map<IMyxBrickDescription,IMyxBrickFactory> brickDescriptionToFactoryMap = 
		Collections.synchronizedMap(new HashMap<IMyxBrickDescription,IMyxBrickFactory>());
	protected List<IMyxWeld> weldList = 
		Collections.synchronizedList(new ArrayList<IMyxWeld>());
	
	protected MyxInterfaceRepository interfaceRepository = new MyxInterfaceRepository();
	
	public MyxBasicRuntime(){
		try{
			addAppClassLoader(this.getClass().getClassLoader());
			addBrickLoader(
				MyxUtils.createName(MyxJavaClassBrickLoader.class.getName()),
				MyxJavaClassBrickLoader.class.getName(),
				null);
		}
		catch(MyxBrickLoaderException mble){
			throw new RuntimeException("This shouldn't happen.");
		}
	}
	
	protected static class BrickLoaderEntry{
		public IMyxName loaderName;
		public IMyxBrickLoader loader;
	}

	public void addBrickLoader(IMyxName loaderName, String className, Properties initParams) throws MyxBrickLoaderException{
		try{
			//Class brickLoaderClass = Class.forName(className);
			Class brickLoaderClass = classForName(className);
			IMyxBrickLoader loader = null;
			if(initParams == null){
				Constructor constructor = brickLoaderClass.getConstructor(new Class[0]);
				Object o = constructor.newInstance(new Object[0]);
				loader = (IMyxBrickLoader)o;
			}
			else{
				Constructor constructor = brickLoaderClass.getConstructor(new Class[]{Properties.class});
				Object o = constructor.newInstance(new Object[]{initParams});
				loader = (IMyxBrickLoader)o;
			}
			loader.setRuntime(this);
			loader.setClassManager(this);
			
			BrickLoaderEntry ble = new BrickLoaderEntry();
			ble.loaderName = loaderName;
			ble.loader = loader;
			brickLoaders.add(ble);
		}
		catch(ClassNotFoundException cnfe){
			throw new MyxBrickLoaderException("Can't load brick loader class", cnfe);
		}
		catch(NoSuchMethodException nsme){
			throw new MyxBrickLoaderException("Can't find brick loader constructor", nsme);
		}
		catch(IllegalAccessException iae){
			throw new MyxBrickLoaderException("Illegal access when creating brick loader", iae);
		}
		catch(InstantiationException ie){
			throw new MyxBrickLoaderException("Instantiation exception when creating brick loader", ie);
		}
		catch(InvocationTargetException ite){
			throw new MyxBrickLoaderException("Constructor invocation failed when creating brick loader", ite);
		}
	}
	
	public void removeBrickLoader(IMyxName loaderName){
		synchronized(brickLoaders){
			BrickLoaderEntry doomed = null;
			for(Iterator it = brickLoaders.iterator(); it.hasNext(); ){
				BrickLoaderEntry ble = (BrickLoaderEntry)it.next();
				if(loaderName.equals(ble.loaderName)){
					doomed = ble;
					break;
				}
			}
			if(doomed != null){
				brickLoaders.remove(doomed);
			}
		}
	}
	
	public IMyxName[] getBrickLoaderNames(){
		synchronized(brickLoaders){
			int i = 0;
			IMyxName[] names = new IMyxName[brickLoaders.size()];
			for(Iterator it = brickLoaders.iterator(); it.hasNext(); ){
				BrickLoaderEntry ble = (BrickLoaderEntry)it.next();
				names[i++] = ble.loaderName;
			}
			return names;
		}
	}
	
	/* Non-interface method */
	public IMyxBrickLoader getBrickLoader(IMyxName name){
		synchronized(brickLoaders){
			for(Iterator it = brickLoaders.iterator(); it.hasNext(); ){
				BrickLoaderEntry ble = (BrickLoaderEntry)it.next();
				if(ble.loaderName.equals(name)){
					return ble.loader;
				}
			}
			return null;
		}
	}
	
	/* Non-interface method */
	public IMyxBrickLoader[] getAllBrickLoaders(){
		synchronized(brickLoaders){
			int i = 0;
			IMyxBrickLoader[] bls = new IMyxBrickLoader[brickLoaders.size()];
			for(Iterator it = brickLoaders.iterator(); it.hasNext(); ){
				BrickLoaderEntry ble = (BrickLoaderEntry)it.next();
				bls[i++] = ble.loader;
			}
			return bls;
		}
	}
	
	protected IMyxBrick createBrick(IMyxName brickName, IMyxBrickDescription brickDescription) 
	throws MyxBrickLoadException, MyxBrickCreationException{
		synchronized(brickDescriptionToFactoryMap){
			IMyxBrickFactory factory = (IMyxBrickFactory)brickDescriptionToFactoryMap.get(brickDescription);
			if(factory == null){
				List<Exception> exceptionList = new ArrayList<Exception>();
				IMyxBrickLoader[] bls = getAllBrickLoaders();
				for(int i = 0; i < bls.length; i++){
					try{
						factory = bls[i].load(brickDescription);
						if(factory != null){
							break;
						}
					}
					catch(MyxBrickNotFoundException bnfe){
						exceptionList.add(bnfe);
					}
					catch(MyxBrickLoadFailedException blfe){ 
						exceptionList.add(blfe);
					}
					catch(MyxUnsupportedBrickDescriptionException ubde){
						exceptionList.add(ubde);
					}
				}
				if(factory == null){
					//we couldn't load the brick and create a factory
					Exception[] causes = exceptionList.toArray(new Exception[0]);
					throw new MyxBrickLoadException("Can't load brick " + brickName + " with any loader", causes);
				}
			}
			//Now we have a factory
			IMyxBrick brick = factory.create(brickName, brickDescription);
			
			return brick;
		}
	}
	
	public void addBrick(IMyxName[] path, IMyxName brickName, IMyxBrickDescription brickDescription) throws MyxBrickLoadException, MyxBrickCreationException {
		IMyxContainer container = MyxUtils.resolvePath(mainContainer, path);
		if(container == null){
			throw new MyxInvalidPathException(path);
		}
		IMyxBrick b = createBrick(brickName, brickDescription);

		//Add the classloaders of all bricks to the app.
		addAppClassLoader(b.getClass().getClassLoader());
		
		//Let's set up the brick items
		//First, the required service provider
		IMyxRequiredServiceProvider requiredServiceProvider = new MyxBasicRequiredServiceProvider();
		
		//Next, the interface manager
		IMyxInterfaceManager interfaceManager = new MyxBasicInterfaceManager(this, path, brickName);
		
		IMyxBrickItems brickItems = new MyxBasicBrickItems(brickName, requiredServiceProvider, 
			interfaceManager, this, brickDescription);
		b.setMyxBrickItems(brickItems);
		
		container.addInternalBrick(b);
	}
	
	public void removeBrick(IMyxName[] path, IMyxName brickName){
		IMyxContainer container = MyxUtils.resolvePath(mainContainer, path);
		if(container == null){
			throw new MyxInvalidPathException(path);
		}
		synchronized(container){
			IMyxBrick b = container.getInternalBrick(brickName);
			if(b == null){
				throw new IllegalArgumentException("No brick found with name: " + brickName + " at " + MyxUtils.pathToString(path));
			}
			container.removeInternalBrick(b);
		}
	}
	
	public IMyxName[] getAllBrickNames(IMyxName[] path){
		IMyxContainer container = MyxUtils.resolvePath(mainContainer, path);
		if(container == null){
			throw new MyxInvalidPathException(path);
		}
		synchronized(container){
			IMyxBrick[] internalBricks = container.getInternalBricks();
			IMyxName[] names = new IMyxName[internalBricks.length];
			for(int i = 0; i < internalBricks.length; i++){
				names[i] = internalBricks[i].getMyxBrickItems().getBrickName();
			}
			return names;
		}
	}
	
	public IMyxBrickDescription getBrickDescription(IMyxName[] path, IMyxName brickName) {
		IMyxContainer container = MyxUtils.resolvePath(mainContainer, path);
		if(container == null){
			throw new MyxInvalidPathException(path);
		}
		IMyxBrick b = container.getInternalBrick(brickName);
		if(b == null){
			throw new IllegalArgumentException("No brick found with name: " + brickName + " at " + MyxUtils.pathToString(path));
		}
		
		return b.getMyxBrickItems().getBrickDescription();
	}

	public void addInterface(IMyxName[] path, IMyxName brickName, IMyxName interfaceName, IMyxInterfaceDescription interfaceDescription, EMyxInterfaceDirection interfaceDirection) {
		IMyxContainer container = MyxUtils.resolvePath(mainContainer, path);
		if(container == null){
			throw new MyxInvalidPathException(path);
		}
		IMyxBrick b = container.getInternalBrick(brickName);
		if(b == null){
			throw new IllegalArgumentException("No brick found with name: " + brickName + " at " + MyxUtils.pathToString(path));
		}
		
		//Don't use this method to add container interfaces, which are mapped.
		if(b instanceof IMyxContainer){
			throw new IllegalArgumentException("Use addContainerInterface(...) to add interfaces to container " + brickName + " at " + MyxUtils.pathToString(path));
		}
		
		interfaceRepository.addInterface(b, interfaceName, interfaceDescription, interfaceDirection);
	}
	
	public void addContainerInterface(IMyxName[] path, IMyxName brickName, IMyxName interfaceName, IMyxInterfaceDescription interfaceDescription, EMyxInterfaceDirection interfaceDirection, IMyxName internalBrickName, IMyxName internalBrickInterfaceName) {
		IMyxContainer outercontainer = MyxUtils.resolvePath(mainContainer, path);
		if(outercontainer == null){
			throw new MyxInvalidPathException(path);
		}
		IMyxBrick b = outercontainer.getInternalBrick(brickName);
		if(b == null){
			throw new IllegalArgumentException("No brick found with name: " + brickName + " at " + MyxUtils.pathToString(path));
		}
		if(!(b instanceof IMyxContainer)){
			throw new IllegalArgumentException("Brick " + brickName + " at " + MyxUtils.pathToString(path) + " should have been a container, but wasn't.");
		}
		IMyxContainer container = (IMyxContainer)b;
		
		IMyxBrick internalBrick = container.getInternalBrick(internalBrickName);
		if(internalBrick == null){
			throw new IllegalArgumentException("No brick found with name: " + internalBrickName + " at " + MyxUtils.pathToString(path) + "/" + brickName);
		}
		IMyxName[] internalInterfaceNames = interfaceRepository.getAllInterfaceNames(internalBrick);
		if(!hasName(internalInterfaceNames, internalBrickInterfaceName)){
			throw new IllegalArgumentException("No interface named: " + internalBrickInterfaceName + " on brick: " + internalBrickName + " at " + MyxUtils.pathToString(path) + "/" + brickName);
		}
		
		interfaceRepository.addInterface(b, interfaceName, interfaceDescription, interfaceDirection, internalBrickName, internalBrickInterfaceName);
	}
	
	public void removeInterface(IMyxName[] path, IMyxName brickName, IMyxName interfaceName){
		IMyxContainer container = MyxUtils.resolvePath(mainContainer, path);
		if(container == null){
			throw new MyxInvalidPathException(path);
		}
		IMyxBrick b = container.getInternalBrick(brickName);
		if(b == null){
			throw new IllegalArgumentException("No brick found with name: " + brickName + " at " + MyxUtils.pathToString(path));
		}

		//TODO: Disallow the removal of interfaces that are involved in welds.
		interfaceRepository.removeInterface(b, interfaceName);
	}
	
	public IMyxName[] getAllInterfaceNames(IMyxName[] path, IMyxName brickName){
		IMyxContainer container = MyxUtils.resolvePath(mainContainer, path);
		if(container == null){
			throw new MyxInvalidPathException(path);
		}
		IMyxBrick b = container.getInternalBrick(brickName);
		if(b == null){
			throw new IllegalArgumentException("No brick found with name: " + brickName + " at " + MyxUtils.pathToString(path));
		}

		return interfaceRepository.getAllInterfaceNames(b);
	}
	
	public IMyxInterfaceDescription getInterfaceDescription(IMyxName[] path, IMyxName brickName, IMyxName interfaceName) {
		IMyxContainer container = MyxUtils.resolvePath(mainContainer, path);
		if(container == null){
			throw new MyxInvalidPathException(path);
		}
		IMyxBrick b = container.getInternalBrick(brickName);
		if(b == null){
			throw new IllegalArgumentException("No brick found with name: " + brickName + " at " + MyxUtils.pathToString(path));
		}

		return interfaceRepository.getInterfaceDescription(b, interfaceName);
	}

	public EMyxInterfaceDirection getInterfaceDirection(IMyxName[] path, IMyxName brickName, IMyxName interfaceName) {
		IMyxContainer container = MyxUtils.resolvePath(mainContainer, path);
		if(container == null){
			throw new MyxInvalidPathException(path);
		}
		IMyxBrick b = container.getInternalBrick(brickName);
		if(b == null){
			throw new IllegalArgumentException("No brick found with name: " + brickName + " at " + MyxUtils.pathToString(path));
		}

		return interfaceRepository.getInterfaceDirection(b, interfaceName);
	}

	public void init(IMyxName[] path, IMyxName brickName){
		callLifecycleMethod(path, brickName, 0);
	}
	
	public void begin(IMyxName[] path, IMyxName brickName){
		callLifecycleMethod(path, brickName, 1);
	}
	
	public void end(IMyxName[] path, IMyxName brickName){
		callLifecycleMethod(path, brickName, 2);
	}

	public void destroy(IMyxName[] path, IMyxName brickName){
		callLifecycleMethod(path, brickName, 3);
	}
	
	private void callLifecycleMethod(IMyxName[] path, IMyxName brickName, int method){
		IMyxContainer container = MyxUtils.resolvePath(mainContainer, path);
		if(container == null){
			throw new MyxInvalidPathException(path);
		}
		IMyxBrick b = null;
		if(brickName == null){
			b = container;
		}
		else{
			b = container.getInternalBrick(brickName);
			if(b == null){
				throw new IllegalArgumentException("No brick found with name: " + brickName + " at " + MyxUtils.pathToString(path));
			}
		}
		
		IMyxLifecycleProcessor[] lps = b.getLifecycleProcessors();
		for(int i = 0; i < lps.length; i++){
			switch(method){
			case 0:
				lps[i].init();
				break;
			case 1:
				lps[i].begin();
				break;
			case 2:
				lps[i].end();
				break;
			case 3:
				lps[i].destroy();
				break;
			}
		}
		
	}
	
	private static boolean hasName(IMyxName[] nameArray, IMyxName nameToMatch){
		for(int i = 0; i < nameArray.length; i++){
			if(nameArray[i].equals(nameToMatch)){
				return true;
			}
		}
		return false;
	}
	
	private static class MyxWeldData{
		public IMyxBrick[] requiredBrick;
		public IMyxName[] requiredInterface;
		public IMyxBrick[] providedBrick;
		public IMyxName[] providedInterface;
	}
	
	private MyxWeldData parseAndValidateWeld(IMyxWeld weld){
		synchronized(weldList){
			IMyxContainer container1 = MyxUtils.resolvePath(mainContainer, weld.getRequiredPath());
			if(container1 == null){
				throw new MyxInvalidPathException(weld.getRequiredPath());
			}
			IMyxBrick b1 = container1.getInternalBrick(weld.getRequiredBrickName());
			if(b1 == null){
				throw new IllegalArgumentException("No brick found with name: " + weld.getRequiredBrickName() + " at " + MyxUtils.pathToString(weld.getRequiredPath()));
			}
			IMyxName i1 = weld.getRequiredInterfaceName();
			IMyxName[] names = interfaceRepository.getAllInterfaceNames(b1); 
			if(!hasName(names, i1)){
				throw new IllegalArgumentException("No interface found with name: " + i1 + " on brick " + weld.getRequiredBrickName());
			}
			
			IMyxContainer container2 = MyxUtils.resolvePath(mainContainer, weld.getProvidedPath());
			if(container2 == null){
				throw new MyxInvalidPathException(weld.getProvidedPath());
			}
			IMyxBrick b2 = container2.getInternalBrick(weld.getProvidedBrickName());
			if(b2 == null){
				throw new IllegalArgumentException("No brick found with name: " + weld.getProvidedBrickName() + " at " + MyxUtils.pathToString(weld.getRequiredPath()));
			}
			IMyxName i2 = weld.getProvidedInterfaceName();
			IMyxName[] names2 = interfaceRepository.getAllInterfaceNames(b2); 
			if(!hasName(names2, i2)){
				throw new IllegalArgumentException("No interface found with name: " + i2 + " on brick " + weld.getProvidedBrickName());
			}
			
			//Okay, we've got all the data set up.  Now we have to actually
			//hook up the interfaces.  If either the provided or required
			//side is a container, that means we have to follow mappings down
			//until we get to the real provider/requirer brick.
			
			//1 == required; 2 == provided
			IMyxBrick requiredBrick = b1;
			IMyxBrick[] rb = new IMyxBrick[]{requiredBrick};
			IMyxName requiredInterface = i1;
			IMyxName[] ri = new IMyxName[]{requiredInterface};			
			while(requiredBrick instanceof IMyxContainer){
				IMyxName[] internalBrickName = interfaceRepository.getInternalBrickName(requiredBrick, requiredInterface);
				ri = interfaceRepository.getInternalInterfaceName(requiredBrick, requiredInterface);
				
				rb = new IMyxBrick[internalBrickName.length];
				for (int i=0;i<internalBrickName.length;i++){
					rb[i] = ((IMyxContainer)requiredBrick).getInternalBrick(internalBrickName[i]);
				}
				requiredBrick = rb[0];
				/*
				IMyxBrick internalBrick = ((IMyxContainer)requiredBrick).getInternalBrick(internalBrickName);
				if(internalBrick == null){
					throw new IllegalArgumentException("No brick found with name: " + internalBrickName + " in container " + MyxUtils.getName(requiredBrick));
				}
				IMyxName[] internalInterfaceNames = interfaceRepository.getAllInterfaceNames(internalBrick);
				if(!hasName(internalInterfaceNames, internalBrickInterfaceName)){
					throw new IllegalArgumentException("No interface found with name: " + internalBrickInterfaceName + " on brick " + internalBrickName);
				}
				
				//Okay, we've identified the internal brick and validated the internal interface.
				//If it's a container too, we'll go through the loop again to get to the real brick.
				requiredBrick = internalBrick;
				*/
				
			}
			
			IMyxBrick providedBrick = b2;
			IMyxBrick[] pb = new IMyxBrick[]{providedBrick};
			IMyxName providedInterface = i2;
			IMyxName[] pi = new IMyxName[]{providedInterface};			
			while(providedBrick instanceof IMyxContainer){
				IMyxName[] internalBrickName = interfaceRepository.getInternalBrickName(providedBrick, providedInterface);
				pi = interfaceRepository.getInternalInterfaceName(providedBrick, providedInterface);

				pb = new IMyxBrick[internalBrickName.length];
				for (int i=0;i<internalBrickName.length;i++){
					pb[i] = ((IMyxContainer)providedBrick).getInternalBrick(internalBrickName[i]);
				}
				providedBrick = pb[0];
				
				//IMyxBrick internalBrick = ((IMyxContainer)providedBrick).getInternalBrick(internalBrickName);
				/*
				if(internalBrick == null){
					throw new IllegalArgumentException("No brick found with name: " + internalBrickName + " in container " + MyxUtils.getName(providedBrick));
				}
				IMyxName[] internalInterfaceNames = interfaceRepository.getAllInterfaceNames(internalBrick);
				if(!hasName(internalInterfaceNames, internalBrickInterfaceName)){
					throw new IllegalArgumentException("No interface found with name: " + internalBrickInterfaceName + " on brick " + internalBrickName);
				}
				//Okay, we've identified the internal brick and validated the internal interface.
				//If it's a container too, we'll go through the loop again to get to the real brick.
				providedBrick = internalBrick;
				*/
			}
			
			//Wrap up the data
			MyxWeldData mwd = new MyxWeldData();
			mwd.requiredBrick = rb; //requiredBrick;
			mwd.requiredInterface = ri;
			mwd.providedBrick = pb;
			mwd.providedInterface = pi;
			return mwd;
		}
	}
	
	public IMyxWeld createWeld(IMyxName[] requiredPath, IMyxName requiredBrickName, IMyxName requiredInterfaceName, IMyxName[] providedPath, IMyxName providedBrickName, IMyxName providedInterfaceName){
		return new MyxBasicWeld(requiredPath, requiredBrickName, requiredInterfaceName, providedPath, providedBrickName, providedInterfaceName);
	}

	public void addWeld(IMyxWeld weld){
		synchronized(weldList){
			MyxWeldData mwd = parseAndValidateWeld(weld);
			
			/*
			IMyxProvidedServiceProvider providedServiceProvider = mwd.providedBrick.getProvidedServiceProvider();
			Object providedServiceObject = 
				providedServiceProvider.getServiceObject(mwd.providedInterface);
			if(providedServiceObject == null){
				throw new IllegalArgumentException("No provided service found for interface: " + mwd.providedInterface + " on brick " + mwd.providedBrick.getMyxBrickItems().getBrickName());
			}
			*/			
			
			for (int i=0;i<mwd.requiredBrick.length;i++){				
				IMyxBrickItems requiredBrickItems = mwd.requiredBrick[i].getMyxBrickItems();
				if(requiredBrickItems == null){
					throw new RuntimeException("Brick " + MyxUtils.getName(mwd.requiredBrick[i]) + " has no brick items.");
				}
				IMyxRequiredServiceProvider sp = requiredBrickItems.getRequiredServiceProvider();
				if(sp == null){
					throw new RuntimeException("Brick " + MyxUtils.getName(mwd.requiredBrick[i]) + " has a null required service provider.");
				}
				if(!(sp instanceof MyxBasicRequiredServiceProvider)){
					throw new RuntimeException("Brick " + MyxUtils.getName(mwd.requiredBrick[i]) + " has an invalid required service provider.");
				}
				MyxBasicRequiredServiceProvider bsp = (MyxBasicRequiredServiceProvider)sp;

				for (int j=0;j<mwd.providedBrick.length;j++){
					IMyxProvidedServiceProvider providedServiceProvider = mwd.providedBrick[j].getProvidedServiceProvider();
					Object providedServiceObject = 
						providedServiceProvider.getServiceObject(mwd.providedInterface[j]);
					bsp.addService(mwd.requiredInterface[i], providedServiceObject);
					//Notify the brick of the connection if it's dynamic
					if(mwd.requiredBrick[i] instanceof IMyxDynamicBrick){
						((IMyxDynamicBrick)mwd.requiredBrick[i]).interfaceConnected(mwd.requiredInterface[i], providedServiceObject);
					}
				}
			}
			weldList.add(weld);
		}
	}
	
	public void removeWeld(IMyxWeld weld){
		synchronized(weldList){
			MyxWeldData mwd = parseAndValidateWeld(weld);
			
			/*
			IMyxProvidedServiceProvider providedServiceProvider = mwd.providedBrick.getProvidedServiceProvider();
			Object providedServiceObject = 
				providedServiceProvider.getServiceObject(mwd.providedInterface);
			if(providedServiceObject == null){
				throw new IllegalArgumentException("No provided service found for interface: " + mwd.providedInterface + " on brick " + MyxUtils.getName(mwd.providedBrick));
			}
			*/
			
			for (int i=0;i<mwd.requiredBrick.length;i++){
				IMyxBrickItems requiredBrickItems = mwd.requiredBrick[i].getMyxBrickItems();
				if(requiredBrickItems == null){
					throw new RuntimeException("Brick " + MyxUtils.getName(mwd.requiredBrick[i]) + " has no brick items.");
				}
				IMyxRequiredServiceProvider sp = requiredBrickItems.getRequiredServiceProvider();
				if(sp == null){
					throw new RuntimeException("Brick " + MyxUtils.getName(mwd.requiredBrick[i]) + " has a null required service provider.");
				}
				if(!(sp instanceof MyxBasicRequiredServiceProvider)){
					throw new RuntimeException("Brick " + MyxUtils.getName(mwd.requiredBrick[i]) + " has an invalid required service provider.");
				}				
				MyxBasicRequiredServiceProvider bsp = (MyxBasicRequiredServiceProvider)sp;
				for (int j=0;j<mwd.providedBrick.length;j++){
					IMyxProvidedServiceProvider providedServiceProvider = mwd.providedBrick[j].getProvidedServiceProvider();
					Object providedServiceObject = 
						providedServiceProvider.getServiceObject(mwd.providedInterface[j]);
					bsp.removeService(mwd.requiredInterface[i], providedServiceObject);
					//Notify the brick of the disconnection if it's dynamic
					if(mwd.requiredBrick[i] instanceof IMyxDynamicBrick){
						((IMyxDynamicBrick)mwd.requiredBrick[i]).interfaceDisconnected(mwd.requiredInterface[i], providedServiceObject);
					}
				}				
			}
			weldList.remove(weld);

		}
	}
	
	public IMyxWeld[] getAllWelds(){
		synchronized(weldList){
			return (IMyxWeld[])weldList.toArray(new IMyxWeld[weldList.size()]);
		}
	}
	
	protected List<ClassLoader> appClassLoaders = new ArrayList<ClassLoader>();
	
	protected void addAppClassLoader(ClassLoader cl){
		if(cl != null){
			synchronized(appClassLoaders){
				for(Iterator it = appClassLoaders.iterator(); it.hasNext(); ){
					ClassLoader cl2 = (ClassLoader)it.next();
					if(cl2.equals(cl)){
						return;
					}
				}
				appClassLoaders.add(cl);
			}
		}
	}
	
	protected ClassLoader[] getAppClassLoaders(){
		synchronized(appClassLoaders){
			HashSet<ClassLoader> s = new HashSet<ClassLoader>();
			ClassLoader[] appClassLoaderArray = (ClassLoader[])appClassLoaders.toArray(new ClassLoader[appClassLoaders.size()]);
			for(int i = 0; i < appClassLoaderArray.length; i++){
				s.add(appClassLoaderArray[i]);
			}
			ClassLoader[] externalClassLoaders = MyxClassLoaders.getClassLoaders();
			for(int i = 0; i < externalClassLoaders.length; i++){
				s.add(externalClassLoaders[i]);
			}
			return (ClassLoader[])s.toArray(new ClassLoader[s.size()]);
		}
	}
	
	public Class classForName(String className) throws ClassNotFoundException{
		ClassLoader[] cls = getAppClassLoaders();
		for(int i = -3; i < cls.length; i++){
			ClassLoader cl;
			switch(i){
			case -3: cl = ClassLoader.getSystemClassLoader(); break;
			case -2: cl = this.getClass().getClassLoader(); break;
			case -1: cl = Thread.currentThread().getContextClassLoader(); break;
			default: cl = cls[i];
			}
			try{
				Class c = Class.forName(className, true, cl);
				if(c != null){
					return c;
				}
			}
			catch(Throwable cnfe){
				//System.err.println(i);
				//cnfe.printStackTrace();
			}
		}
		throw new ClassNotFoundException(className);
	}
	
	public void addClassLoader(ClassLoader cl){
		appClassLoaders.add(cl);
	}
	
	public void removeClassLoader(ClassLoader cl){
		appClassLoaders.remove(cl);
	}
}
