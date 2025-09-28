package duncanscott.org.groovy.utils.ondemandcache

/**
 * Compute-once, cache-forever (per instance) with simple single-flight semantics.
 * Thread-safe via synchronized methods only (no atomics needed).
 *
 * NOTE: This version **caches nulls**. If you don't want that, see the comment in cacheClosureOutput().
 */
class OnDemandCache<K> {

    private K cachedObject
    private boolean locked

    OnDemandCache() {}

    synchronized void forceCache(K value) {
        cachedObject = value
        locked = true
    }

    synchronized void cache(K value) {
        if (!locked) {
            cachedObject = value
            locked = true
        }
    }

    synchronized void cacheClosureOutput(Closure<? extends K> closure) {
        if (!locked) {
            K v = closure.call()
            // If you do NOT want to cache nulls, use:
            // if (v != null) { cachedObject = v; locked = true }
            // else leave 'locked' false so another attempt can compute later.
            cachedObject = v
            locked = true
        }
    }

    /**
     * Computes (once) using the supplied closure and returns the cached value thereafter.
     * If the closure throws, nothing is cached and the exception propagates.
     */
    K fetch(Closure<? extends K> fetchClosure) {
        if (!isLocked()) {
            cacheClosureOutput(fetchClosure)   // synchronized inside
        }
        return getCachedObject()               // synchronized getter below
    }

    /** Required: expose the cached object directly. */
    synchronized K getCachedObject() {
        return cachedObject
    }

    synchronized void clear() {
        cachedObject = null
        locked = false
    }

    /** Bean-style accessor (preferred). */
    synchronized boolean isLocked() {
        return locked
    }

}
