package edu.uci.isr.bna4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;

public class UserEditableUtils{

	private static final String USER_EDITABLE_QUALITIES_PROPERTY_NAME = "userEditableQualities";

	public static void addEditableQuality(IThing thing, String... qualities){
		Lock lock = thing.getPropertyLock();
		lock.lock();
		try{
			Set<String> oldEditableQualities = thing.getProperty(USER_EDITABLE_QUALITIES_PROPERTY_NAME);
			if(oldEditableQualities == null){
				oldEditableQualities = new HashSet<String>();
			}
			Set<String> newEditableQualities = new HashSet<String>(oldEditableQualities);
			newEditableQualities.addAll(Arrays.asList(qualities));
			thing.setProperty(USER_EDITABLE_QUALITIES_PROPERTY_NAME, newEditableQualities);
		}
		finally{
			lock.unlock();
		}
	}

	public static void removeEditableQuality(IThing thing, String... qualities){
		Lock lock = thing.getPropertyLock();
		lock.lock();
		try{
			Set<String> oldEditableQualities = thing.getProperty(USER_EDITABLE_QUALITIES_PROPERTY_NAME);
			if(oldEditableQualities == null){
				oldEditableQualities = new HashSet<String>();
			}
			Set<String> newEditableQualities = new HashSet<String>(oldEditableQualities);
			newEditableQualities.removeAll(Arrays.asList(qualities));
			thing.setProperty(USER_EDITABLE_QUALITIES_PROPERTY_NAME, newEditableQualities);
		}
		finally{
			lock.unlock();
		}
	}

	public static boolean isEditable(IThing thing){
		if(Boolean.TRUE.equals(thing.getProperty(IBNAView.BACKGROUND_THING_PROPERTY_NAME))){
			return false;
		}
		if(Boolean.TRUE.equals(thing.getProperty(IBNAView.HIDE_THING_PROPERTY_NAME))){
			return false;
		}
		return true;
	}

	public static boolean isEditableForAllQualities(IThing thing, String... editableQualities){
		return isEditable(thing) && hasAllEditableQualities(thing, editableQualities);
	}

	public static boolean hasAllEditableQualities(IThing thing, String... editableQualities){
		Set<String> thingEditableQualities = thing.getProperty(USER_EDITABLE_QUALITIES_PROPERTY_NAME);
		if(thingEditableQualities != null){
			for(String editableQuality: editableQualities){
				if(!thingEditableQualities.contains(editableQuality)){
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public static boolean isEditableForAnyQualities(IThing thing, String... editableQualities){
		return isEditable(thing) && hasAnyEditableQualities(thing, editableQualities);
	}

	public static boolean hasAnyEditableQualities(IThing thing, String... editableQualities){
		Set<String> thingEditableQualities = thing.getProperty(USER_EDITABLE_QUALITIES_PROPERTY_NAME);
		if(thingEditableQualities != null){
			for(String editableQuality: editableQualities){
				if(thingEditableQualities.contains(editableQuality)){
					return true;
				}
			}
		}
		return false;
	}
}
