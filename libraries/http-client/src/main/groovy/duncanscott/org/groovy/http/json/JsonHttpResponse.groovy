package duncanscott.org.groovy.http.json

import duncanscott.org.groovy.http.client.HttpResponse
import duncanscott.org.groovy.utils.ondemandcache.OnDemandCache
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

class JsonHttpResponse extends HttpResponse {

    final OnDemandCache<JSONObject> cachedJson = new OnDemandCache<>()

    JSONObject getJson() {
        if (cachedJson.cachedObject) {
            return cachedJson.cachedObject
        }
        if (text != null) {
            return cachedJson.fetch(({
                new JSONParser().parse(text)
            } as Closure<JSONObject>))
        }
        return null
    }

}
