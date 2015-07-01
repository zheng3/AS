package edu.uci.isr.bna4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ThingTree{

	final static IThing[] EMPTY_ARRAY = new IThing[0];

	class Node{

		final IThing t;

		Node parent = null;
		List<Node> children = new ArrayList<Node>();

		public Node(IThing t){
			this.t = t;
		}
	}

	final Node root;
	final Map<IThing, Node> lookup;

	public ThingTree(){
		this.lookup = new HashMap<IThing, Node>();
		this.root = getNode(null, true);
	}

	protected Node getNode(IThing t, boolean create){
		Node node = lookup.get(t);
		if(node == null && create){
			lookup.put(t, node = new Node(t));
		}
		return node;
	}

	protected Node getPrevious(Node node){
		Node parentNode = node.parent;
		if(parentNode != null){
			List<Node> siblings = parentNode.children;
			int index = siblings.indexOf(node);
			if(index == 0){
				return parentNode;
			}
			if(index > 0){
				Node p = siblings.get(index - 1);
				List<Node> l;
				int s;
				while((s = (l = p.children).size()) > 0){
					p = l.get(s - 1);
				}
				return p;
			}
		}
		return null;
	}

	public void add(IThing t){
		add(t, null);
	}

	public void add(IThing t, IThing parent){
		Node parentNode = getNode(parent, false);
		if(parentNode != null){
			Node childNode = getNode(t, true);
			if(childNode.parent != null && childNode.parent != parentNode){
				remove(t);
			}
			parentNode.children.add(childNode);
			childNode.parent = parentNode;
			if(allThings != null){
				int insertIndex;
				Node previousNode = getPrevious(childNode);
				if(previousNode != null && previousNode.t != null){
					insertIndex = allThings.indexOf(previousNode.t) + 1;
				}
				else{
					insertIndex = 0;
				}
				allThings.add(insertIndex, t);
				allThingsArray = null;
			}
		}
	}

	public void remove(IThing t){
		Node childNode = lookup.remove(t);
		if(childNode != null){
			Node parentNode = childNode.parent;
			if(parentNode != null){
				List<Node> siblings = parentNode.children;
				int index = siblings.indexOf(childNode);
				if(index >= 0){
					siblings.remove(index);
					siblings.addAll(index, childNode.children);
					for(Node grandchild: childNode.children){
						grandchild.parent = parentNode;
					}
					if(allThings != null){
						allThings.remove(childNode.t);
						allThingsArray = null;
					}
				}
			}
		}
	}

	public int size(){
		return lookup.size() - 1;
	}

	public boolean contains(IThing t){
		if(t != null){
			Node childNode = getNode(t, false);
			if(childNode != null){
				return childNode.parent != null;
			}
		}
		return false;
	}

	public IThing getParent(IThing t){
		if(t != null){
			Node childNode = getNode(t, false);
			if(childNode != null){
				return childNode.parent.t;
			}
		}
		return null;
	}

	public IThing[] getChildren(IThing t){
		Node parentNode = getNode(t, false);
		if(parentNode != null){
			IThing[] childThings = new IThing[parentNode.children.size()];
			for(int i = 0; i < childThings.length; i++){
				childThings[i] = parentNode.children.get(i).t;
			}
			return childThings;
		}
		return EMPTY_ARRAY;
	}

	public IThing[] getAllChildren(IThing t){
		Node parentNode = getNode(t, false);
		if(parentNode != null){
			List<IThing> allChildren = new ArrayList<IThing>();
			appendChildren(allChildren, parentNode);
			return allChildren.toArray(new IThing[allChildren.size()]);
		}
		return EMPTY_ARRAY;
	}

	public IThing[] getAncestors(IThing t){
		List<IThing> ancestors = new ArrayList<IThing>();
		if(t != null){
			Node node = getNode(t, false);
			while(node != null){
				ancestors.add(node.t);
				node = node.parent;
			}
		}
		return ancestors.toArray(new IThing[ancestors.size()]);
	}

	protected void move(Node parentNode, Node childNode, int toIndex){
		List<Node> siblings = parentNode.children;
		int index = siblings.indexOf(childNode);
		if(toIndex < 0){
			toIndex = siblings.size() + toIndex;
		}
		if(index >= 0 && index != toIndex){
			siblings.remove(index);
			if(index < toIndex){
				toIndex--;
			}
			siblings.add(toIndex, childNode);
			allThings = null;
			allThingsArray = null;
		}
	}

	public void moveAfter(IThing t, IThing referenceThing){
		Node childNode = getNode(t, false);
		Node referenceNode = getNode(referenceThing, false);
		if(childNode != null && referenceNode != null){
			Node parentNode = childNode.parent;
			if(parentNode != null && parentNode == referenceNode.parent){
				int referenceIndex = parentNode.children.indexOf(referenceNode);
				if(referenceIndex >= 0){
					move(parentNode, childNode, referenceIndex + 1);
				}
			}
		}
	}

	public void bringToFront(IThing t){
		Node childNode = getNode(t, false);
		if(childNode != null){
			Node parentNode = childNode.parent;
			if(parentNode != null){
				move(parentNode, childNode, -1);
			}
		}
	}

	public void sendToBack(IThing t){
		Node childNode = getNode(t, false);
		if(childNode != null){
			Node parentNode = childNode.parent;
			if(parentNode != null){
				move(parentNode, childNode, 0);
			}
		}
	}

	List<IThing> allThings = null;
	IThing[] allThingsArray = null;

	public IThing[] getAllThings(){
		if(allThingsArray == null){
			if(allThings == null){
				allThings = new ArrayList<IThing>(lookup.size());
				appendChildren(allThings, root);
			}
			this.allThingsArray = allThings.toArray(new IThing[allThings.size()]);
		}
		return allThingsArray;
	}

	protected void appendChildren(List<IThing> list, Node node){
		for(Node n: node.children){
			list.add(n.t);
			appendChildren(list, n);
		}
	}
}
