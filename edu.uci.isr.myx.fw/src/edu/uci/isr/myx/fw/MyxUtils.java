package edu.uci.isr.myx.fw;

import java.util.Properties;

public class MyxUtils{

	protected static final IMyxImplementation DEFAULT_MYX_IMPLEMENTATION = new MyxBasicImplementation();

	protected static final IMyxBrickDescription CONTAINER_BRICK_DESCRIPTION = new MyxJavaClassBrickDescription(null, 
		MyxContainer.class.getName());

	private MyxUtils(){
	}

	public static IMyxImplementation getDefaultImplementation(){
		return DEFAULT_MYX_IMPLEMENTATION;
	}

	public static IMyxName createName(String name){
		MyxBasicName bn = new MyxBasicName(name);
		return bn;
	}

	public static IMyxBrickDescription getContainerBrickDescription(){
		return CONTAINER_BRICK_DESCRIPTION;
	}

	public static IMyxName getName(IMyxBrick brick){
		if(brick == null){
			return new MyxBasicName("[null-brick]");
		}
		IMyxBrickItems bi = brick.getMyxBrickItems();
		if(bi == null){
			return new MyxBasicName("[null-brick-items]");
		}
		return bi.getBrickName();
	}

	public static boolean nulleq(Object o1, Object o2){
		if(o1 == o2)
			return true;
		if((o1 == null) && (o2 != null))
			return false;
		if((o1 != null) && (o2 == null))
			return false;
		return o1.equals(o2);
	}

	public static int hc(Object o){
		if(o == null)
			return 0;
		return o.hashCode();
	}

	public static boolean classeq(Object o1, Object o2){
		return o1.getClass().equals(o2.getClass());
	}

	public static String pathToString(IMyxName[] path){
		if(path == null){
			return "/";
		}

		if(path.length == 0){
			return "/";
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < path.length; i++){
			sb.append("/");
			sb.append(path[i]);
		}
		return sb.toString();
	}

	public static IMyxContainer resolvePath(IMyxContainer rootContainer,
		IMyxName[] path){
		if(path == null)
			return rootContainer;

		IMyxContainer currentContainer = rootContainer;
		for(int i = 0; i < path.length; i++){
			IMyxBrick internalBrick = currentContainer.getInternalBrick(path[i]);
			if((internalBrick == null) || (!(internalBrick instanceof IMyxContainer))){
				return null;
			}
			currentContainer = (IMyxContainer)internalBrick;
		}
		return currentContainer;
	}

	public static Class classForName(String name, ClassLoader[] clArray)
		throws ClassNotFoundException{
		ClassNotFoundException lastCnfe = null;
		for(int i = 0; i < clArray.length; i++){
			try{
				Class c = Class.forName(name, true, clArray[i]);
				return c;
			}
			catch(ClassNotFoundException cnfe){
				lastCnfe = cnfe;
			}
		}
		if(lastCnfe != null){
			throw lastCnfe;
		}
		else{
			throw new ClassNotFoundException(name);
		}
	}

	public static Object getFirstRequiredServiceObject(IMyxBrick b,
		IMyxName interfaceName){
		IMyxBrickItems brickItems = b.getMyxBrickItems();
		if(brickItems != null){
			IMyxRequiredServiceProvider rsp = brickItems.getRequiredServiceProvider();
			Object[] svcs = rsp.getServiceObjects(interfaceName);
			if(svcs.length > 0){
				return svcs[0];
			}
		}
		return null;
	}

	public static Object[] getRequiredServiceObjects(IMyxBrick b,
		IMyxName interfaceName){
		IMyxBrickItems brickItems = b.getMyxBrickItems();
		if(brickItems != null){
			IMyxRequiredServiceProvider rsp = brickItems.getRequiredServiceProvider();
			Object[] svcs = rsp.getServiceObjects(interfaceName);
			return svcs;
		}
		return null;
	}

	public static Properties getInitProperties(IMyxBrick b) {
		IMyxBrickItems brickItems = b.getMyxBrickItems();
		if(brickItems != null){
			IMyxBrickDescription d = brickItems.getBrickDescription();
			if(d instanceof MyxBrickDescription)
				return ((MyxBrickDescription)d).getInitParams();
		}
		return new java.util.Properties();
	}
}
