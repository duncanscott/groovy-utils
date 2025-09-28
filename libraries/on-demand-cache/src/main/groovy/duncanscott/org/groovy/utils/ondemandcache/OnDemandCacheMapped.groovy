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

    private V fetchCacheNulls(K input, Closure<V> fetchClosure) {
        return cacheMap.computeIfAbsent(input) { k -> new OnDemandCache<V>() }.fetch(fetchClosure)
    }

    V fetchDoNotCacheNulls(K input, Closure<V> fetchClosure) {
        V out = null
        cacheMap.compute(input) { k, existing ->
            def c = existing ?: new OnDemandCache<V>()
            def v = c.fetch(fetchClosure)
            out = v
            (v != null) ? c : existing
        }
        return out
    }

    V fetch(K input, Closure<V> fetchClosure) {
        cacheNulls ? fetchCacheNulls(input, fetchClosure) : fetchDoNotCacheNulls(input, fetchClosure)
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
