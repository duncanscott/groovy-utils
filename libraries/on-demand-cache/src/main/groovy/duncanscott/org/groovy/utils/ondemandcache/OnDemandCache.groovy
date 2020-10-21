package duncanscott.org.groovy.utils.ondemandcache

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

/**
 * Created by dscott on 2/11/14.
 */
public class OnDemandCache<K> {

    private final AtomicReference<K> cachedObject = new AtomicReference<>()
    private final AtomicBoolean locked = new AtomicBoolean(false)

    OnDemandCache() {}

    synchronized void forceCache(K objectToCache) {
        cachedObject.set(objectToCache)
        locked.set(true)
    }

    synchronized void cache(K objectToCache) {
        if (!locked.get()) {
            cachedObject.set(objectToCache)
            locked.set(true)
        }
    }

    synchronized void cacheClosureOutput(Closure<K> closure) {
        if (!locked.get()) {
            K obj = closure.call()
            cache(obj)
        }
    }

    K fetch(Closure fetchClosure) {
        if (!locked.get()) {
            cacheClosureOutput(fetchClosure)
        }
        cachedObject.get()
    }

    K getCachedObject() {
        cachedObject.get()
    }

    synchronized void clear() {
        cachedObject.set(null)
        locked.set(false)
    }

    boolean getLocked() {
        locked.get()
    }
}

