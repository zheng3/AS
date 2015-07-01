package edu.uci.isr.archstudio4.comp.xarchcs.changesetrelationshipmanager;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatInterface;

public class ChangeSetRelationshipManagerImpl
	implements IChangeSetRelationshipManager{

	XArchFlatInterface xarch;

	public enum MergeKindEnum{
		RIGHT_MERGE, LEFT_MERGE,
	}

	Map<MergeKindEnum, IChangeSetRelationshipMergeHandler> mergeHandlerMap;

	/*
	 * This doesn't support multiple xArchRefs -- they don't all share the same
	 * current relationships
	 */
	//Set <ObjRef> currentXADLRelationships = null;
	Boolean debug = false;

	class RelationshipData{

		Set<ObjRef> relationshipRefs;
		ObjRef mergedRef;
		MergeKindEnum mergeKind;

		public RelationshipData(Set<ObjRef> relationshipRefs, ObjRef mergedRef, MergeKindEnum mergeKind){
			super();
			this.relationshipRefs = relationshipRefs;
			this.mergedRef = mergedRef;
			this.mergeKind = mergeKind;
		}
	}

	class RelationshipsDataTable{

		Map<MergeKey, RelationshipData> lookupTable;
		Set<ObjRef> simpleRelationships; // the set of all simple relationships, prior to any merging

		public RelationshipsDataTable(){
			lookupTable = new HashMap<MergeKey, RelationshipData>();
			simpleRelationships = new HashSet<ObjRef>();
		}

	}

	/** Map to keep separate relationships data table for each root XArchRef */
	private Map<ObjRef, RelationshipsDataTable> xArchToRelDataTable;

	/**
	 * Constructor
	 */
	public ChangeSetRelationshipManagerImpl(XArchFlatInterface xarch){
		this.xarch = xarch;
		xArchToRelDataTable = new HashMap<ObjRef, RelationshipsDataTable>();

		// for each merge kind, we must specify a merge handler
		mergeHandlerMap = new HashMap<MergeKindEnum, IChangeSetRelationshipMergeHandler>();
		mergeHandlerMap.put(MergeKindEnum.RIGHT_MERGE, new RightMergeHandler(xarch));
		mergeHandlerMap.put(MergeKindEnum.LEFT_MERGE, new LeftMergeHandler(xarch));
	}

	protected String getId(ObjRef changeSetLink){
		String csId = (String)xarch.get(changeSetLink, "href");
		if(csId != null && csId.charAt(0) == '#'){
			csId = csId.substring(1);
			return csId;
		}
		else{
			return null;
		}
	}

	protected void printRelationship(ObjRef relationship){
		// figure out what type of relationship this is
		if(xarch.isInstanceOf(relationship, "changesets#OrRelationship")){
			System.out.print("OR");
		}
		else if(xarch.isInstanceOf(relationship, "changesets#AndRelationship")){
			System.out.print("AND");
		}
		else{
			System.out.print("Invalid Relationship");
			return;
		}
		System.out.println(" Relationship:");

		// print out the change sets
		ObjRef[] andChangeSets = xarch.getAll(relationship, "andChangeSet");
		for(ObjRef andChangeSet: andChangeSets){
			System.out.println("And " + getId(andChangeSet));
		}
		ObjRef[] andNotChangeSets = xarch.getAll(relationship, "andNotChangeSet");
		for(ObjRef andNotChangeSet: andNotChangeSets){
			System.out.println("AndNot " + getId(andNotChangeSet));
		}
		ObjRef[] orChangeSets = xarch.getAll(relationship, "orChangeSet");
		for(ObjRef orChangeSet: orChangeSets){
			System.out.println("Or " + getId(orChangeSet));
		}
		ObjRef[] orNotChangeSets = xarch.getAll(relationship, "orNotChangeSet");
		for(ObjRef orNotChangeSet: orNotChangeSets){
			System.out.println("OrNot " + getId(orNotChangeSet));
		}
		ObjRef[] impliesChangeSets = xarch.getAll(relationship, "impliesChangeSet");
		for(ObjRef impliesChangeSet: impliesChangeSets){
			System.out.println("Implies " + getId(impliesChangeSet));
		}
		ObjRef[] impliesNotChangeSets = xarch.getAll(relationship, "impliesNotChangeSet");
		for(ObjRef impliesNotChangeSet: impliesNotChangeSets){
			System.out.println("ImpliesNot " + getId(impliesNotChangeSet));
		}
	}

	protected String getRelationshipTypeString(ObjRef relationship){
		// figure out what type of relationship this is
		if(xarch.isInstanceOf(relationship, "changesets#OrRelationship")){
			return "OR";
		}
		else if(xarch.isInstanceOf(relationship, "changesets#AndRelationship")){
			return "AND";
		}
		else{
			return null;
		}
	}

	public synchronized void addRelationship(ObjRef XArchRef, ObjRef changeSetRelationship){
		if(debug){
			System.out.println("--------------Got a relationship.-------------------");
			printRelationship(changeSetRelationship);
		}

		RelationshipsDataTable relationshipsDataTable = xArchToRelDataTable.get(XArchRef);
		// if no relationships data table exists for this xarch, then create it.
		if(relationshipsDataTable == null){
			relationshipsDataTable = new RelationshipsDataTable();
			xArchToRelDataTable.put(XArchRef, relationshipsDataTable);
		}

		// keep a set of simple relationships for this xarch
		relationshipsDataTable.simpleRelationships.add(changeSetRelationship);

		// iterate through the map of all merge handlers 
		for(Map.Entry<MergeKindEnum, IChangeSetRelationshipMergeHandler> mergeHandlerEntry: mergeHandlerMap.entrySet()){
			RelationshipData relationshipData;
			IChangeSetRelationshipMergeHandler mergeHandler = mergeHandlerEntry.getValue();
			MergeKindEnum mergeKind = mergeHandlerEntry.getKey();
			MergeKey mergeKey = mergeHandler.getMergeKey(XArchRef, changeSetRelationship);
			if(debug){
				System.out.println("Checking merge kind " + mergeHandler.keyName());
			}
			// does this relationship produce a valid key for this merge?
			if(mergeKey != null){
				// see if there are any existing entries that match this merge key
				if(relationshipsDataTable.lookupTable.containsKey(mergeKey)){

					relationshipData = relationshipsDataTable.lookupTable.get(mergeKey);
					relationshipData.relationshipRefs.add(changeSetRelationship);
					// update merged relationship
					if(debug){
						System.out.println("Merging with " + mergeHandler.keyName());
					}
					relationshipData.mergedRef = mergeHandler.doMerge(XArchRef, relationshipData.relationshipRefs);
				}
				else{

					if(debug){
						System.out.println("No matching for merge key for " + mergeHandler.keyName() + ". Adding key to lookup table.");
					}
					// no other entries have this key yet.  add it to the table
					// in this case there is only one matching relationship in the list
					Set<ObjRef> matchedRelationships = new HashSet<ObjRef>();
					matchedRelationships.add(changeSetRelationship);
					
					//TODO: fix 100 parameter to cloneElement(), should be using a constant
					//relationshipData = new RelationshipData(matchedRelationships, xarch.cloneElement(changeSetRelationship, 100), mergeKind);
					
				    //note: calling doMerge here with a single relationship allows us to avoid the clone operation, 
					//it's a simple way of creating a new relationship with getting "copy(2) embedded in the ids
					relationshipData = new RelationshipData(matchedRelationships, mergeHandler.doMerge(XArchRef, matchedRelationships), mergeKind);
					
					relationshipsDataTable.lookupTable.put(mergeKey, relationshipData);
				}

				// decide which relationships to insert into the xadl
				// eventually call this periodically??
				updateRelationships(XArchRef);

			}
		}

	}

	public synchronized void removeRelationship(ObjRef XArchRef, ObjRef changeSetRelationship){
		if(debug){
			System.out.println("--------------Remove a relationship.-------------------");
			printRelationship(changeSetRelationship);
		}

		RelationshipsDataTable relationshipsDataTable = xArchToRelDataTable.get(XArchRef);

		// maintain the set of simple relationships for this xarch
		relationshipsDataTable.simpleRelationships.remove(changeSetRelationship);

		Set<Map.Entry<MergeKey, RelationshipData>> relDataSet = relationshipsDataTable.lookupTable.entrySet();
		// iterate through the map of all merge keys looking for for the given relationship in their relationship data
		for(Map.Entry<MergeKey, RelationshipData> relDataEntry: relDataSet){
			// if found, remove the relationship from the relationships data and recalculate the merge
			RelationshipData relationshipData = relDataEntry.getValue();
			if(relationshipData.relationshipRefs.remove(changeSetRelationship)){
				IChangeSetRelationshipMergeHandler mergeHandler = mergeHandlerMap.get(relationshipData.mergeKind);
				relationshipData.mergedRef = mergeHandler.doMerge(XArchRef, relationshipData.relationshipRefs);
			}
		}
		updateRelationships(XArchRef);
	}

	private void updateRelationships(ObjRef XArchRef){

		ObjRef changesetsContextRef = xarch.createContext(XArchRef, "changesets");
		ObjRef archChangeSets = xarch.getElement(changesetsContextRef, "archChangeSets", XArchRef);
		if(archChangeSets != null){

			// populate with current xADL relationships
			Set<ObjRef> currentXADLRelationships = new HashSet<ObjRef>();
			for(ObjRef relationshipRef: xarch.getAll(archChangeSets, "relationship")){
				if("true".equals(xarch.get(relationshipRef, "generated"))){
					currentXADLRelationships.add(relationshipRef);
				}
			}

			// determine our new relationships
			RelationshipsDataTable relationshipsDataTable = xArchToRelDataTable.get(XArchRef);
			Set<ObjRef> newXADLRelationships = new HashSet<ObjRef>();

			// initialized unusedRelationships to all simple relationships
			Set<ObjRef> unusedRelationships = new HashSet<ObjRef>();
			unusedRelationships.addAll(relationshipsDataTable.simpleRelationships);

			// try to determine a minimal set of merge keys that will include all unused relationships
			while(unusedRelationships.size() > 0){
				// select the key with the most unused Relationships
				MergeKey key = selectKey(relationshipsDataTable, unusedRelationships);
				if(key == null){
					throw new RuntimeException("this shouldn't happen");
				}

				RelationshipData relationshipData = relationshipsDataTable.lookupTable.get(key);

				// mark all relationships for this key as used
				unusedRelationships.removeAll(relationshipData.relationshipRefs);

				// place the merged relationship corresponding to the key in the newXADLRelationships set
				newXADLRelationships.add(relationshipData.mergedRef);
			}

			// remove old relationships that are not sticking around after calculation
			Set<ObjRef> relationshipsToRemove = new HashSet<ObjRef>(currentXADLRelationships);
			relationshipsToRemove.removeAll(newXADLRelationships);
			xarch.remove(archChangeSets, "relationship", relationshipsToRemove.toArray(new ObjRef[relationshipsToRemove.size()]));

			// add new relationships that were not previously present
			Set<ObjRef> relationshipsToAdd = new HashSet<ObjRef>(newXADLRelationships);
			relationshipsToAdd.removeAll(currentXADLRelationships);
			xarch.add(archChangeSets, "relationship", relationshipsToAdd.toArray(new ObjRef[relationshipsToAdd.size()]));

			currentXADLRelationships = newXADLRelationships;
		}
	}

	private MergeKey selectKey(RelationshipsDataTable relationshipsDataTable, Set<ObjRef> unusedRelationships){
		// find key with the largest number of unused relationships
		int maxNumRelationships = 0;
		MergeKey key = null;

		Set<Map.Entry<MergeKey, RelationshipData>> relDataSet = relationshipsDataTable.lookupTable.entrySet();
		for(Map.Entry<MergeKey, RelationshipData> relDataEntry: relDataSet){
			Collection<ObjRef> relationshipRefs = relDataEntry.getValue().relationshipRefs;
			int numUnusedRelationships = 0;
			for(ObjRef relationshipRef: relationshipRefs){
				if(unusedRelationships.contains(relationshipRef)){
					numUnusedRelationships++;
				}
			}

			if(numUnusedRelationships > maxNumRelationships){
				maxNumRelationships = numUnusedRelationships;
				key = relDataEntry.getKey();
			}
		}
		return key;
	}

}