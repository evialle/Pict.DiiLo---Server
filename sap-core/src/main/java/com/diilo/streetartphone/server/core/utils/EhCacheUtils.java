/**
 * 
 */
package com.diilo.streetartphone.server.core.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Utilities concerning EhCache. Don't forget to parameterize ehCache.xml before using this cache.
 * 
 * @author Eric VIALLE
 */
public class EhCacheUtils {

	/** Name of the cache dedicated to the Happy Hours application to a Store. */
	public static final String						CREDENTIALS		= "credentials";

	/** Cache manager. */
	private static final CacheManager				cacheManager	= CacheManager.create(EhCacheUtils.class
																			.getResource("/ehcache.xml"));

	private Cache									cache;

	private static final Map<String, EhCacheUtils>	instances		= new HashMap<String, EhCacheUtils>();

	/** Private constructor. */
	private EhCacheUtils(final String cacheName) {
		this.cache = cacheManager.getCache(cacheName);
	}

	/**
	 * Get instance of the cache utils.
	 * 
	 * @param cacheName
	 *            as defined in ehcache.xml
	 * @return
	 */
	public static EhCacheUtils getInstance(final String cacheName) {

		EhCacheUtils instance = instances.get(cacheName);

		if (instance == null) {
			instance = new EhCacheUtils(cacheName);
			instances.put(cacheName, instance);
		}

		return instance;
	}

	/**
	 * Remove from cache the entity.
	 * 
	 * @param id
	 *            id of the entity
	 */
	public void deleteFromCache(final Serializable id) {
		this.cache.remove(id);
		this.cache.flush();
	}

	/**
	 * Add to cache an object.
	 * 
	 * @param id
	 *            id of the object
	 * @param obj
	 *            object to store in the cache
	 */
	public void addToCache(final Serializable id, final Object obj) {
		this.cache.put(new Element(id, obj));
		this.cache.flush();
	}

	/**
	 * @param id
	 *            of the object to retrieve
	 * @return object to retrieve
	 */
	public Object getFromCache(final Serializable id) {
		Element element = this.cache.get(id);

		if (element == null) {
			return null;
		} else {
			return element.getObjectValue();
		}
	}

	/**
	 * @return the cache
	 */
	public Cache getCache() {
		return cache;
	}

}
