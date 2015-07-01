package edu.uci.isr.bna4;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PeerCache{

	protected Map<IThing, IThingPeer> peerMap;

	public PeerCache(){
		this.peerMap = Collections.synchronizedMap(new HashMap<IThing, IThingPeer>());
	}

	public IThingPeer createPeer(IThing th){
		try{
			Class<? extends IThingPeer> peerClass = th.getPeerClass();
			Constructor<? extends IThingPeer> constructor = peerClass.getConstructor(new Class[]{IThing.class});
			IThingPeer peer = constructor.newInstance(new Object[]{th});
			peerMap.put(th, peer);
			return peer;
		}
		catch(InvocationTargetException ite){
			throw new RuntimeException("Could not instantiate peer.", ite);
		}
		catch(IllegalAccessException iae){
			throw new RuntimeException("Could not instantiate peer.", iae);
		}
		catch(InstantiationException ie){
			throw new RuntimeException("Could not instantiate peer.", ie);
		}
		catch(NoSuchMethodException nsme){
			throw new RuntimeException("Invalid peer class.", nsme);
		}
	}

	public IThingPeer getPeer(IThing th){
		IThingPeer peer = peerMap.get(th);
		if(peer != null){
			return peer;
		}
		else{
			return createPeer(th);
		}
	}

}
