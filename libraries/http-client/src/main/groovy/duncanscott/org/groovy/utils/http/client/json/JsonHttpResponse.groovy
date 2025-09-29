package duncanscott.org.groovy.utils.http.client.json

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import duncanscott.org.groovy.utils.http.client.base.HttpClientRequest
import duncanscott.org.groovy.utils.http.client.base.HttpClientResponse
import duncanscott.org.groovy.utils.ondemandcache.OnDemandCache

class JsonHttpResponse extends HttpClientResponse {

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

   JsonHttpResponse execute(HttpClientRequest request) {
        return (JsonHttpResponse) super.execute(request)
    }

}
