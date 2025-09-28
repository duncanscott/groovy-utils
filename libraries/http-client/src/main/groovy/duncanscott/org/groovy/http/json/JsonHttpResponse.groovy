package duncanscott.org.groovy.http.json

import com.fasterxml.jackson.databind.ObjectMapper
import duncanscott.org.groovy.http.client.HttpResponse
import duncanscott.org.groovy.utils.ondemandcache.OnDemandCache

class JsonHttpResponse extends HttpResponse {

    private static final ObjectMapper MAPPER = new ObjectMapper()

    final OnDemandCache<Object> cachedJson = new OnDemandCache<>()

    Object getJson() {
        if (cachedJson.cachedObject) {
            return cachedJson.cachedObject
        }
        if (text != null) {
            return cachedJson.fetch(({
                MAPPER.readValue(text, Object.class)
            } as Closure<Object>))
        }
        return null
    }

}
