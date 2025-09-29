package duncanscott.org.groovy.http.json

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import duncanscott.org.groovy.http.client.Response
import duncanscott.org.groovy.utils.ondemandcache.OnDemandCache

class JsonHttpResponse extends Response {

    private static final ObjectMapper MAPPER = new ObjectMapper()

    final OnDemandCache<JsonNode> cachedJson = new OnDemandCache<>()

    JsonNode getJson() {
        if (cachedJson.cachedObject) {
            return cachedJson.cachedObject
        }
        if (text != null) {
            return cachedJson.fetch(({
                MAPPER.readTree(text)
            } as Closure<JsonNode>))
        }
        return null
    }

}
