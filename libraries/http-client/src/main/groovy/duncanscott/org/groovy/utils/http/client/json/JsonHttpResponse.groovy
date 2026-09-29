package duncanscott.org.groovy.utils.http.client.json

import tools.jackson.databind.JsonNode
import tools.jackson.databind.json.JsonMapper
import duncanscott.org.groovy.utils.http.client.base.HttpClientRequest
import duncanscott.org.groovy.utils.http.client.base.HttpClientResponse
import duncanscott.org.groovy.utils.ondemandcache.OnDemandCache

class JsonHttpResponse extends HttpClientResponse {

    private static final JsonMapper MAPPER = JsonMapper.builder().build()

    final OnDemandCache<JsonNode> cachedJson = new OnDemandCache<>()

    JsonNode getJson() {
        if (text == null) {
            return null
        }
        return cachedJson.fetch(({
            MAPPER.readTree(text)
        } as Closure<JsonNode>))
    }

    JsonHttpResponse execute(HttpClientRequest request) {
        return (JsonHttpResponse) super.execute(request)
    }

}
