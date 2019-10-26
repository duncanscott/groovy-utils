package duncanscott.org.groovy.utils.ondemandcache

import spock.lang.Specification

class OnDemandeCacheSpec extends Specification {

    def "cache null"() {
        setup:
        OnDemandCache<String> onDemandCache = new OnDemandCache<>()

        expect:
        !onDemandCache.locked

        when:
        onDemandCache.fetch {null}

        then:
        onDemandCache.locked
        onDemandCache.cachedObject == null

    }

}
