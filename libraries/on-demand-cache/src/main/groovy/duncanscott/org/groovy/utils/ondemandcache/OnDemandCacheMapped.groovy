package duncanscott.org.groovy.utils.ondemandcache

import java.util.concurrent.ConcurrentHashMap

/**
 * Created by dscott on 3/4/14.
 */
class OnDemandCacheMapped<K, V> {

    final boolean cacheNulls

    final Map<K, OnDemandCache<V>> cacheMap = new ConcurrentHashMap<>()

    OnDemandCacheMapped(boolean cacheNulls) {
        this.cacheNulls = cacheNulls
    }

    OnDemandCacheMapped() {
        this.cacheNulls = true
    }

    V fetch(K input, Closure<V> fetchClosure) {
        V fetchedObject = null
        OnDemandCache<V> onDemandCache = cacheMap.get(input)
        if (onDemandCache == null) {
            onDemandCache = new OnDemandCache<>()
            if (cacheNulls) {
                cacheMap.putIfAbsent(input, onDemandCache)
            }
            fetchedObject = onDemandCache.fetch(fetchClosure)
            if (!cacheNulls && fetchedObject != null) {
                cacheMap.putIfAbsent(input, onDemandCache)
            }
        } else {
            fetchedObject = onDemandCache.fetch(fetchClosure)
        }
        fetchedObject
    }

    OnDemandCache<V> remove(K key) {
        cacheMap.remove(key)
    }

    void clear() {
        cacheMap.clear()
    }

    Set<K> keySet() {
        cacheMap.keySet()
    }

}
