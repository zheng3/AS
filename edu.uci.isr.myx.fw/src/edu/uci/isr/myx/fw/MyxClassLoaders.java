package edu.uci.isr.myx.fw;

import java.util.*;

public class MyxClassLoaders{

	private static Set<ClassLoader> classLoaderSet = 
		Collections.synchronizedSet(new HashSet<ClassLoader>());
	private static ClassLoader[] classLoaderArray = new ClassLoader[0];
	
	private MyxClassLoaders(){}

	public static synchronized void addClassLoader(ClassLoader cl){
		classLoaderSet.add(cl);
		classLoaderArray = classLoaderSet.toArray(new ClassLoader[classLoaderSet.size()]);
	}

	public static synchronized void removeClassLoader(ClassLoader cl){
		classLoaderSet.remove(cl);
		classLoaderArray = classLoaderSet.toArray(new ClassLoader[classLoaderSet.size()]);
	}
	
	public static synchronized ClassLoader[] getClassLoaders(){
		return classLoaderArray;
	}
}
