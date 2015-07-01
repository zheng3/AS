package edu.uci.isr.archstudio4.comp.xarchcs.xarchcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.uci.isr.archstudio4.comp.xarchcs.changesetsync.IChangeSetSync.IChangeSetSyncMonitor;
import edu.uci.isr.xadlutils.XadlUtils;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public class XArchChangeSetUtils{

	private static List<ObjRef> loadOrder(XArchFlatInterface xarch, ObjRef xArchRef, ObjRef archChangeSetsRef, String listName){
		List<ObjRef> orderList = new ArrayList<ObjRef>();
		Set<ObjRef> orderSet = new HashSet<ObjRef>();
		String changeSetOrder = (String)xarch.get(archChangeSetsRef, listName);
		if(changeSetOrder != null){
			for(String id: changeSetOrder.split("\\,")){
				id = id.trim();
				if(id.length() > 0){
					ObjRef changeSetRef = xarch.getByID(xArchRef, id);
					if(changeSetRef != null && orderSet.add(changeSetRef)){
						orderList.add(changeSetRef);
					}
				}
			}
		}
		return orderList;
	}

	public static void saveOrder(XArchFlatInterface xarch, ObjRef xArchRef, ObjRef archChangeSetsRef, String listName, List<ObjRef> changeSetRefs){
		changeSetRefs = new ArrayList<ObjRef>(changeSetRefs);
		StringBuffer sb = new StringBuffer();
		for(Object changeSetRef: changeSetRefs){
			if(changeSetRef instanceof ObjRef){
				if(sb.length() > 0){
					sb.append(", ");
				}
				sb.append(XadlUtils.getID(xarch, (ObjRef)changeSetRef));
			}
		}
		xarch.set(archChangeSetsRef, listName, sb.toString());
	}

	private static List<ObjRef> getOrderedChangeSets(XArchFlatInterface xarch, ObjRef xArchRef, ObjRef changeSetContextRef, ObjRef archChangeSetsRef, List<ObjRef> appliedList){
		List<ObjRef> orderList = loadOrder(xarch, xArchRef, archChangeSetsRef, "changeSetOrder");
		final Map<ObjRef, Integer> orderMap = new HashMap<ObjRef, Integer>();
		for(ObjRef changeSetRef: orderList){
			if(changeSetRef != null && !orderMap.containsKey(changeSetRef)){
				// place ordered change sets in natural order
				orderMap.put(changeSetRef, orderMap.size());
			}
		}
		for(ObjRef changeSetRef: appliedList){
			if(changeSetRef != null && !orderMap.containsKey(changeSetRef)){
				// place unordered applied change sets at the end
				orderMap.put(changeSetRef, orderMap.size());
			}
		}
		for(ObjRef changeSetRef: xarch.getAll(archChangeSetsRef, "changeSet")){
			if(changeSetRef != null && !orderMap.containsKey(changeSetRef)){
				// place unidentified change sets at the beginning
				orderMap.put(changeSetRef, -orderMap.size());
			}
		}
		List<ObjRef> newOrderList = toList(orderMap);
		if(!orderList.equals(newOrderList)){
			saveOrder(xarch, xArchRef, archChangeSetsRef, "changeSetOrder", newOrderList);
		}
		return newOrderList;
	}

	private static List<ObjRef> toList(final Map<ObjRef, Integer> orderMap){
		List<ObjRef> orderedList = new ArrayList<ObjRef>(orderMap.keySet());
		Collections.sort(orderedList, new Comparator<ObjRef>(){

			public int compare(ObjRef o1, ObjRef o2){
				return orderMap.get(o1).compareTo(orderMap.get(o2));
			}

			@Override
			public boolean equals(Object obj){
				return false;
			}
		});
		return orderedList;
	}

	public static List<ObjRef> getOrderedChangeSets(XArchFlatInterface xarch, ObjRef xArchRef){
		ObjRef changeSetContextRef = xarch.createContext(xArchRef, "changesets");
		ObjRef archChangeSetsRef = xarch.getElement(changeSetContextRef, "archChangeSets", xArchRef);
		List<ObjRef> orderedChangeSetRefs = null;
		if(archChangeSetsRef != null){
			List<ObjRef> appliedList = loadOrder(xarch, xArchRef, archChangeSetsRef, "appliedChangeSets");
			orderedChangeSetRefs = getOrderedChangeSets(xarch, xArchRef, changeSetContextRef, archChangeSetsRef, appliedList);
		}
		return orderedChangeSetRefs;
	}

	public static List<ObjRef> getOrderedAppliedChangeSets(XArchFlatInterface xarch, ObjRef xArchRef){
		ObjRef changeSetContextRef = xarch.createContext(xArchRef, "changesets");
		ObjRef archChangeSetsRef = xarch.getElement(changeSetContextRef, "archChangeSets", xArchRef);
		List<ObjRef> orderedChangeSetRefs = null;
		if(archChangeSetsRef != null){
			List<ObjRef> appliedList = loadOrder(xarch, xArchRef, archChangeSetsRef, "appliedChangeSets");
			orderedChangeSetRefs = getOrderedChangeSets(xarch, xArchRef, changeSetContextRef, archChangeSetsRef, appliedList);
			Set<ObjRef> appliedSet = new HashSet<ObjRef>(appliedList);
			orderedChangeSetRefs.retainAll(appliedSet);
		}
		return orderedChangeSetRefs;
	}

	public static void move(XArchChangeSetInterface xarch, ObjRef xArchRef, ObjRef[] moveChangeSetRefs, int newIndex, IChangeSetSyncMonitor monitor){
		ObjRef changeSetContextRef = xarch.createContext(xArchRef, "changesets");
		ObjRef archChangeSetsRef = xarch.getElement(changeSetContextRef, "archChangeSets", xArchRef);
		if(archChangeSetsRef != null){
			List<ObjRef> appliedList = loadOrder(xarch, xArchRef, archChangeSetsRef, "appliedChangeSets");
			List<ObjRef> orderedList = getOrderedChangeSets(xarch, xArchRef, changeSetContextRef, archChangeSetsRef, appliedList);
			if(newIndex < 0)
				newIndex = orderedList.size() + newIndex + 1;

			Set<ObjRef> movedChangeSetRefsSet = new HashSet<ObjRef>(Arrays.asList(moveChangeSetRefs));
			List<ObjRef> movedOrderedList = new ArrayList<ObjRef>(orderedList);
			movedOrderedList.retainAll(movedChangeSetRefsSet);
			{
				List<ObjRef> subList = orderedList.subList(0, newIndex);
				subList.removeAll(movedChangeSetRefsSet);
				newIndex = subList.size();
			}
			{
				List<ObjRef> subList = orderedList.subList(newIndex, orderedList.size());
				subList.removeAll(movedChangeSetRefsSet);
			}
			orderedList.addAll(newIndex, movedOrderedList);

			saveOrder(xarch, xArchRef, archChangeSetsRef, "changeSetOrder", orderedList);
			orderedList.retainAll(new HashSet<ObjRef>(appliedList));
			// saveOrder(xarch, xArchRef, archChangeSetsRef, "appliedChangeSets", orderedList);
			xarch.setAppliedChangeSetRefs(xArchRef, orderedList.toArray(new ObjRef[orderedList.size()]), monitor);
		}
	}
}
