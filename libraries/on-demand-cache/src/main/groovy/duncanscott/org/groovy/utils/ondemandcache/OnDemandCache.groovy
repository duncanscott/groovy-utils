package duncanscott.org.groovy.utils.ondemandcache

import groovy.transform.CompileStatic

@CompileStatic
class OnDemandCache<K> {

    private static final Object NULL_SENTINEL = new Object()
    private volatile Object cachedObject

    void forceCache(K objectToCache) {
        synchronized (this) {
            cachedObject = (objectToCache == null) ? NULL_SENTINEL : objectToCache
        }
    }

    void cache(K objectToCache) {
        if (cachedObject == null) {
            synchronized (this) {
                if (cachedObject == null) {
                    cachedObject = (objectToCache == null) ? NULL_SENTINEL : objectToCache
                }
            }
        }
    }

    void cacheClosureOutput(Closure<K> closure) {
        if (cachedObject == null) {
            synchronized (this) {
                if (cachedObject == null) {
                    K obj = closure.call()
                    cachedObject = (obj == null) ? NULL_SENTINEL : obj
                }
            }
        }
    }

    K fetch(Closure<K> fetchClosure) {
        Object value = cachedObject
        if (value == null) {
            cacheClosureOutput(fetchClosure)
            value = cachedObject
        }
        return (value?.is(NULL_SENTINEL)) ? null : (K) value
    }

    K getCachedObject() {
        Object value = cachedObject
        return (value?.is(NULL_SENTINEL)) ? null : (K) value
    }

    void clear() {
        synchronized (this) {
            cachedObject = null
        }
    }

    boolean getLocked() {
        cachedObject != null
    }
}
