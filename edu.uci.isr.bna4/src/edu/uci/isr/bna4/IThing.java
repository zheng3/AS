package edu.uci.isr.bna4;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * Describes a BNA Thing that has a unique identifier and zero or more
 * properties.
 */
public interface IThing{

	/**
	 * Gets the class that maintains and draws the graphical representation of
	 * this Thing.
	 * 
	 * @return The Thing's Peer's class.
	 */
	public Class<? extends IThingPeer> getPeerClass();

	/**
	 * Gets the unique ID of this Thing.
	 * 
	 * @return Thing ID.
	 */
	public String getID();

	/**
	 * Gets the property {@link Lock} for this Thing.
	 * 
	 * @return Property lock object for this Thing.
	 */
	public Lock getPropertyLock();

	public void setPropertyLock(Lock lock);

	/**
	 * Adds a listener to this Thing that will be notified when the Thing
	 * changes.
	 * 
	 * @param tl
	 *            Thing listener to add.
	 */
	public void addThingListener(IThingListener tl);

	/**
	 * Removes a listener to this Thing that will no longer be notified when the
	 * Thing changes.
	 * 
	 * @param tl
	 *            Thing listener to remove.
	 */
	public void removeThingListener(IThingListener tl);

	/**
	 * Adds a property to this thing. Autoboxing takes care of the case where
	 * the property is a simple type such as an int or float.
	 * 
	 * @param key
	 *            Key of the property.
	 * @param value
	 *            Value of the property.
	 */
	public <T>T setProperty(Object key, Object value);

	/**
	 * Gets a property from this thing.
	 * 
	 * @param key
	 *            Key of the property.
	 * @return Value of the property, or <code>null</code> if there is no such
	 *         property.
	 */
	public <T>T getProperty(Object key);

	public boolean hasProperty(Object key);

	/**
	 * Removes a property from this thing. Does nothing if the property does not
	 * exist.
	 * 
	 * @param key
	 *            Key of the property to remove.
	 */
	public <T>T removeProperty(Object key);

	/**
	 * Gets a Map representation of the key/values of this thing.
	 * 
	 * @return Map representation of the key/values pair properties.
	 */
	public Map<Object, Object> getPropertyMap();

	/**
	 * Gets all property keys in this thing as an array of Objects.
	 * 
	 * @return All property names in an array.
	 */
	public Collection<Object> getAllPropertyNames();
}
