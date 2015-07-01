package edu.uci.isr.xarchflat;

import java.util.ArrayList;
import java.util.List;

import edu.uci.isr.sysutils.SystemUtils;

/**
 * An XArchPath is similar to an XML XPath but it works for xArch-based XML
 * documents. An XArchPath can be converted to and from a string easily with the
 * functions in this class. It is useful for quickly assessing an element's
 * position within an XML document without having to call costly operations to
 * walk around the XML tree.
 * <p>
 * An XArchPath is composed of segments, starting at the XML tree root and
 * proceeding down the tree to the specified element. Each segment has one of
 * the following formats:
 * <p>
 * <i>tagName</i> (for elements where that is the only tag)
 * <p>
 * <i>tagName</i>:<i>tagIndex</i> Where tagIndex is the the index of that tag
 * name within the list of all tags with the same name. So, if there are five
 * tags called ComponentInstance in the same place, the third one is
 * <code>componentInstance:2</code> (remember, indices are zero-based).
 * <p>
 * <i>tagName</i>:id=<i>tagID</i> Where tagID is the ID of the element, if the
 * element has an ID attribute.
 * <p>
 * So, a sample XArchPath might be:
 * <p>
 * <code>xArch/ArchStructure:id=hello there/Component:5/Description</code>
 * <p>
 * Any slashes in the ID or tag name are escaped with a backslash; backslashes
 * are also escaped as a double-backslash.
 */
public class XArchPath
    implements java.io.Serializable{

	private static final String escape(String s){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < s.length(); i++){
			char ch = s.charAt(i);
			if(ch == ':'){
				sb.append("\\:");
			}
			else if(ch == '/'){
				sb.append("\\/");
			}
			else if(ch == '\\'){
				sb.append("\\\\");
			}
			else{
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	@Deprecated
	public static final String capFirstLetter(String s){
		return SystemUtils.capFirst(s);
	}

	private static final void addSegment(int i, String s, String tagName, String tagAttribute, List<String> tagNames, List<Integer> tagIndexes, List<String> tagIDs){
		if(tagName.length() == 0){
			throw new IllegalArgumentException("Illegal XArchPath Specification; Zero Length Name at [" + i + "] in " + s);
		}

		tagNames.add(tagName);
		if(tagAttribute == null){
			tagIndexes.add(new Integer(-1));
			tagIDs.add(null);
		}
		else{
			if(tagAttribute.length() == 0){
				throw new IllegalArgumentException("Illegal XArchPath Specification; Zero Length Annotation at [" + i + "] in " + s);
			}

			if(tagAttribute.startsWith("id=")){
				tagAttribute = tagAttribute.substring(3);
				tagIndexes.add(new Integer(-1));
				tagIDs.add(tagAttribute);
			}
			else{
				try{
					int index = Integer.parseInt(tagAttribute);
					tagIndexes.add(new Integer(index));
					tagIDs.add(null);
				}
				catch(NumberFormatException nfe){
					throw new IllegalArgumentException("Illegal XArchPath Specification; Illegal Tagged Value at [" + i + "] in " + s);
				}
			}
		}
	}

	private static final XArchPath parse(String s){
		List<String> tagNames = new ArrayList<String>();
		List<Integer> tagIndexes = new ArrayList<Integer>();
		List<String> tagIDs = new ArrayList<String>();

		//If mode = 0; we're parsing a tag name
		//If mode = 1; we're parsing a tag attribute
		int mode = 0;

		StringBuffer tokenBuf = new StringBuffer();

		String tagName = null;
		String tagAttribute = null;

		try{
			for(int i = 0; i < s.length(); i++){
				char ch = s.charAt(i);
				if(ch == '\\'){
					i++;
					ch = s.charAt(i);
					tokenBuf.append(ch);
				}
				else{
					//It wasn't an escaped character, so it might
					//have been the delimiter
					if(ch == '/'){
						if(mode == 0){ //No tagged attribute
							tagName = tokenBuf.toString();
							tokenBuf.setLength(0);
							tagAttribute = null;
						}
						else if(mode == 1){ //we were parsing a tagged attribute
							tagAttribute = tokenBuf.toString();
							tokenBuf.setLength(0);
						}
						//This the end of the segment
						mode = 0; //mode = 0 now
						addSegment(i, s, tagName, tagAttribute, tagNames, tagIndexes, tagIDs);
						tagName = null;
						tagAttribute = null;
					}
					else if(ch == ':'){
						if(mode == 0){
							tagName = tokenBuf.toString();
							tokenBuf.setLength(0);
							//Now parsing a tagged attribute
							mode = 1;
						}
						else if(mode == 1){
							throw new IllegalArgumentException("Illegal XArchPath Specification; Illegal Colon at [" + i + "] in " + s);
						}
					}
					else{
						tokenBuf.append(ch);
					}
				}
			}
			if(tokenBuf.length() > 0){
				if(mode == 0){
					tagName = tokenBuf.toString();
					tagAttribute = null;
					addSegment(s.length(), s, tagName, tagAttribute, tagNames, tagIndexes, tagIDs);
				}
				if(mode == 1){
					tagAttribute = tokenBuf.toString();
					addSegment(s.length(), s, tagName, tagAttribute, tagNames, tagIndexes, tagIDs);
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			throw new IllegalArgumentException("Illegal XArchPath Specification; Unexpected end of path in " + s);
		}

		int[] tagIndexes2 = new int[tagIndexes.size()];
		for(int i = 0; i < tagIndexes2.length; i++){
			tagIndexes2[i] = tagIndexes.get(i);
		}

		return new XArchPath(tagNames.toArray(new String[tagNames.size()]), tagIndexes2, tagIDs.toArray(new String[tagIDs.size()]));
	}

	public static final ObjRef resolve(XArchFlatQueryInterface xArch, ObjRef xArchRef, XArchPath p){
		String pathTagsString = p.toTagsOnlyString();
		if(!pathTagsString.startsWith("xArch/")){
			return null;
		}

		ObjRef curNode = xArchRef;
		outerLoop: for(int i = 1; i < p.getLength(); i++){
			String tagName = p.getTagName(i);
			int tagIndex = p.getTagIndex(i);
			String tagID = p.getTagID(i);

			if(i == 1){
				// It's the top-level element
				int localIndex = 0;
				ObjRef[] objects = xArch.getAll(curNode, "Object");
				for(int j = 0; j < objects.length; j++){
					String objectTagName = capFirstLetter(xArch.getElementName(objects[j]));
					if(capFirstLetter(tagName).equals(objectTagName)){
						if(tagID != null){
							try{
								String objectID = (String)xArch.get(objects[j], "Id");
								if(objectID != null){
									if(objectID.equals(tagID)){
										curNode = objects[j];
										continue outerLoop;
									}
								}
							}
							catch(Exception e){
							}
						}
						else{
							// Go by index
							if(tagIndex == -1 || tagIndex == 0){
								// Looking for the first one
								curNode = objects[j];
								continue outerLoop;
							}
							else{
								if(tagIndex == localIndex){
									curNode = objects[j];
									continue outerLoop;
								}
								localIndex++;
							}
						}
					}
				}
				return null;
			}
			else{
				// It's not the very first one
				if(tagID != null){
					try{
						ObjRef ref = (ObjRef)xArch.get(curNode, tagName);
						if(ref != null){
							try{
								String objectID = (String)xArch.get(ref, "Id");
								if(objectID != null){
									if(objectID.equals(tagID)){
										curNode = ref;
										continue;
									}
								}
							}
							catch(Exception e){
							}
						}
					}
					catch(Exception e){
					}
					try{
						ObjRef[] refs = xArch.getAll(curNode, tagName);
						for(int j = 0; j < refs.length; j++){
							try{
								String objectID = (String)xArch.get(refs[j], "Id");
								if(objectID != null){
									if(objectID.equals(tagID)){
										curNode = refs[j];
										continue outerLoop;
									}
								}
							}
							catch(Exception e){
							}
						}
					}
					catch(Exception e){
					}
					// We fell off the tree;
					return null;
				}
				else{
					// Find by index
					if(tagIndex < 1){
						try{
							ObjRef ref = (ObjRef)xArch.get(curNode, tagName);
							if(ref != null){
								curNode = ref;
								continue;
							}
						}
						catch(Exception e){
						}
						try{
							ObjRef[] refs = xArch.getAll(curNode, tagName);
							if(refs != null && refs.length > 0){
								curNode = refs[0];
								continue;
							}
						}
						catch(Exception e){
						}
						// Fell off the tree
						return null;
					}
					else{
						try{
							ObjRef[] refs = xArch.getAll(curNode, tagName);
							if(refs != null && refs.length > 0){
								curNode = refs[tagIndex];
								continue;
							}
						}
						catch(Exception e){
						}
						// Fell off the tree;
						return null;
					}
				}
			}
		}
		return curNode;
	}

	private final String[] tagNames;
	private final int tagNameOffset;

	private final int[] tagIndexes;
	private final int tagIndexOffset;

	private final String[] tagIDs;
	private final int tagIdOffset;

	private final int pathLength;

	private volatile String stringString = null;
	private volatile String tagsOnlyString = null;
	private volatile String dumpString = null;
	private volatile String[] externalTagNames = null;

	XArchPath(String[] tagNames, int tagNameOffset, int[] tagIndexes, int tagIndexOffset, String[] tagIDs, int tagIdOffset, int pathLength){
		this.tagNames = tagNames;
		this.tagNameOffset = tagNameOffset;
		this.tagIndexes = tagIndexes;
		this.tagIndexOffset = tagIndexOffset;
		this.tagIDs = tagIDs;
		this.tagIdOffset = tagIdOffset;
		this.pathLength = pathLength;
		if(tagNameOffset < 0 || tagNameOffset + pathLength > tagNames.length){
			throw new IndexOutOfBoundsException();
		}
		if(tagIndexOffset < 0 || tagIndexOffset + pathLength > tagIndexes.length){
			throw new IndexOutOfBoundsException();
		}
		if(tagIdOffset < 0 || tagIdOffset + pathLength > tagIDs.length){
			throw new IndexOutOfBoundsException();
		}
	}

	public XArchPath(String[] tagNames, int[] tagIndexes, String[] tagIDs){
		this(tagNames, 0, tagIndexes, 0, tagIDs, 0, tagNames.length);
		if(tagIndexes.length != pathLength || tagIDs.length != pathLength){
			throw new IllegalArgumentException();
		}
	}

	XArchPath(XArchPath xArchPath){
		this(xArchPath.tagNames, xArchPath.tagNameOffset, xArchPath.tagIndexes, xArchPath.tagIndexOffset, xArchPath.tagIDs, xArchPath.tagIdOffset, xArchPath.pathLength);
	}

	/**
	 * Parse a stringified XArchPath into an XArchPath object.
	 * 
	 * @param s
	 *            Stringified XArchPath
	 * @exception IllegalArgumentException
	 *                if the string is invalid.
	 */
	public XArchPath(String s){
		this(parse(s));
	}

	/**
	 * Get the length, in number of elements, of this XArchPath.
	 * 
	 * @return XArchPath's length.
	 */
	public int getLength(){
		return pathLength;
	}

	/**
	 * Get the name of the tag at the given segment.
	 * 
	 * @param index
	 *            Segment number
	 * @return name of tag at that segment
	 */
	public String getTagName(int index){
		if(index < 0 || index >= pathLength){
			throw new IndexOutOfBoundsException();
		}
		return tagNames[tagNameOffset + index];
	}

	/**
	 * Get the index of the tag at the given segment.
	 * 
	 * @param index
	 *            Segment number
	 * @return index of tag at that segment, or -1 if the index is not
	 *         applicable
	 */
	public int getTagIndex(int index){
		if(index < 0 || index >= pathLength){
			throw new IndexOutOfBoundsException();
		}
		return tagIndexes[tagIndexOffset + index];
	}

	/**
	 * Get the ID of the tag at the given segment.
	 * 
	 * @param index
	 *            Segment number
	 * @return ID of tag at that segment, or null if the index has no ID
	 */
	public String getTagID(int index){
		if(index < 0 || index >= pathLength){
			throw new IndexOutOfBoundsException();
		}
		return tagIDs[tagIdOffset + index];
	}

	public XArchPath subpath(int beginIndex){
		return subpath(beginIndex, pathLength);
	}

	public XArchPath subpath(int beginIndex, int endIndex){
		if(beginIndex == 0 && endIndex == pathLength){
			return this;
		}
		return new XArchPath(tagNames, tagNameOffset + beginIndex, tagIndexes, tagIndexOffset + beginIndex, tagIDs, tagIdOffset + beginIndex, endIndex - beginIndex);
	}

	/**
	 * Converts this XArchPath into a string.
	 * 
	 * @return String representation of this XArchPath
	 */
	@Override
	public String toString(){
		if(stringString == null){
			int iN = tagNameOffset;
			int iX = tagIndexOffset;
			int iD = tagIdOffset;
			int c = pathLength;

			StringBuffer sb = new StringBuffer();
			while(c-- > 0){
				String tagName = tagNames[iN++];
				int tagIndex = tagIndexes[iX++];
				String tagID = tagIDs[iD++];

				sb.append(escape(tagName));
				if(tagID != null){
					sb.append(":id=" + escape(tagID));
				}
				else if(tagIndex != -1){
					sb.append(":" + tagIndex);
				}
				if(c > 0){
					sb.append("/");
				}
			}
			stringString = sb.toString();
		}
		return stringString;
	}

	/**
	 * Converts this XArchPath into a string, stripping all information except
	 * tag names.
	 * 
	 * @return String representation of this XArchPath with tag names only.
	 */
	public String toTagsOnlyString(){
		if(tagsOnlyString == null){
			switch(pathLength){
			case 0:
				tagsOnlyString = "";
				break;
			case 1:
				tagsOnlyString = tagNames[tagNameOffset];
				break;
			default:
				int iN = tagNameOffset;
				int c = pathLength;

				StringBuffer sb = new StringBuffer();
				while(c-- > 0){
					String tagName = tagNames[iN++];

					sb.append(escape(tagName));
					if(c > 0){
						sb.append("/");
					}
				}
				tagsOnlyString = sb.toString();
			}
		}
		return tagsOnlyString;
	}

	/**
	 * Converts this XArchPath into a debugging string.
	 * 
	 * @return String representation of this XArchPath, useful for debugging.
	 */
	public String toDumpString(){
		if(dumpString == null){
			int iN = tagNameOffset;
			int iX = tagIndexOffset;
			int iD = tagIdOffset;
			int c = pathLength;

			StringBuffer sb = new StringBuffer();
			while(c-- > 0){
				String tagName = tagNames[iN++];
				int tagIndex = tagIndexes[iX++];
				String tagID = tagIDs[iD++];

				sb.append(tagName);
				sb.append(",");
				if(tagID != null){
					sb.append("id=" + tagID);
					sb.append(",");
				}
				else if(tagIndex != -1){
					sb.append("[");
					sb.append(tagIndex);
					sb.append("]");
				}
				sb.append(System.getProperty("line.separator"));
			}
			dumpString = sb.toString();
		}
		return dumpString;
	}

	public String[] getTagNames(){
		if(externalTagNames == null){
			String[] externalTagNamesCopy = new String[pathLength];
			System.arraycopy(tagNames, tagNameOffset, externalTagNamesCopy, 0, pathLength);
			externalTagNames = externalTagNamesCopy;
		}
		return externalTagNames;
	}
}
