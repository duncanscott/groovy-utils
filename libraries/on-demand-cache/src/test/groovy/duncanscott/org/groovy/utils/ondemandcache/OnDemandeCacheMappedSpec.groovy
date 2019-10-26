package duncanscott.org.groovy.utils.ondemandcache

import spock.lang.Specification

class OnDemandeCacheMappedSpec extends Specification {

    def "do not cache null"() {
        setup:
        boolean cacheNulls = false
        OnDemandCacheMapped<String,String> onDemandCacheMapped = new OnDemandCacheMapped<>(cacheNulls)

        when:
        onDemandCacheMapped.fetch('hello') {null}

        then:
        !onDemandCacheMapped.keySet()

        when:
        onDemandCacheMapped.fetch('hello') {'world'}

        then:
        onDemandCacheMapped.keySet().contains('hello')
    }

    def "do cache null"() {
        setup:
        boolean cacheNulls = true
        OnDemandCacheMapped<String,String> onDemandCacheMapped = new OnDemandCacheMapped<>(cacheNulls)

        when:
        onDemandCacheMapped.fetch('hello') {null}

        then:
        onDemandCacheMapped.keySet().contains('hello')

        when:
        String who = onDemandCacheMapped.fetch('hello') {'world'}

        then:
        who == null

    }

}
