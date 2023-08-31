package duncanscott.org.groovy.http.json

import duncanscott.org.groovy.http.client.HttpResponse
import duncanscott.org.groovy.utils.ondemandcache.OnDemandCache
import org.json.JSONObject

class JsonHttpResponse extends HttpResponse {

    final OnDemandCache<JSONObject> cachedJson = new OnDemandCache<>()

    JSONObject getJson() {
        if (cachedJson.cachedObject) {
            return cachedJson.cachedObject
        }
        if (text != null) {
            return cachedJson.fetch {
                new JSONObject(text)
            }
        }
        return null
    }

}
