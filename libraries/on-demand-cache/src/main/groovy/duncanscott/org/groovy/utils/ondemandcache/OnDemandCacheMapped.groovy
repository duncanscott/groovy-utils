package duncanscott.org.groovy.utils.ondemandcache

import groovy.transform.CompileStatic

import java.util.concurrent.ConcurrentHashMap

/**
 * A thread-safe, on-demand cache that maps keys to lazily-computed values.
 *
 * @param <K> The type of the keys.
 * @param <V> The type of the values.
 */
@CompileStatic
class OnDemandCacheMapped<K, V> {

    final boolean cacheNulls
    final Map<K, OnDemandCache<V>> cacheMap = new ConcurrentHashMap<>()

    /**
     * @param cacheNulls If true, a null result from the fetch closure will be cached.
     *                   If false, the closure will be re-executed on the next fetch call.
     */
    OnDemandCacheMapped(boolean cacheNulls) {
        this.cacheNulls = cacheNulls
    }

    /**
     * Default constructor. Configured to cache nulls.
     */
    OnDemandCacheMapped() {
        this(true)
    }

    private V fetchCacheNulls(K input, Closure<V> fetchClosure) {
        // This is clean and correct. computeIfAbsent is atomic.
        // The underlying OnDemandCache handles caching nulls correctly.
        return cacheMap.computeIfAbsent(input) { K k -> new OnDemandCache<V>() }.fetch(fetchClosure)
    }

    private V fetchDoNotCacheNulls(K input, Closure<V> fetchClosure) {
        // Get or create the cache instance atomically.
        OnDemandCache<V> cache = cacheMap.computeIfAbsent(input) { K k -> new OnDemandCache<V>() }
        V value = cache.fetch(fetchClosure)

        if (value == null) {
            // The result was null, so we must not "cache" it.
            // We achieve this by removing the OnDemandCache instance for this key.
            // This atomic remove operation prevents a race condition where another thread might have
            // already removed and replaced it between our fetch and remove calls.
            cacheMap.remove(input, cache)
        }
        return value
    }

    /**
     * Fetches a value from the cache for the given input key.
     * If the value is not already cached, the fetchClosure is executed to produce it.
     *
     * @param input The key for the value to fetch.
     * @param fetchClosure The closure to execute to create the value if it's not in the cache.
     * @return The cached or newly created value.
     */
    V fetch(K input, Closure<V> fetchClosure) {
        cacheNulls ? fetchCacheNulls(input, fetchClosure) : fetchDoNotCacheNulls(input, fetchClosure)
    }

    /**
     * Removes the cache entry for the specified key.
     * @param key The key to remove.
     * @return The cache instance that was removed, or null.
     */
    OnDemandCache<V> remove(K key) {
        cacheMap.remove(key)
    }

    /**
     * Clears all entries from the cache.
     */
    void clear() {
        cacheMap.clear()
    }

    /**
     * @return A set of all keys currently in the cache.
     */
    Set<K> keySet() {
        cacheMap.keySet()
    }
}
