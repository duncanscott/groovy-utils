package duncanscott.org.groovy.http.json

import duncanscott.org.groovy.http.duncanscott.org.groovy.http.util.HttpResponse
import duncanscott.org.groovy.utils.ondemandcache.OnDemandCache
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

class JsonHttpResponse extends HttpResponse {

    private final OnDemandCache<JSONObject> cachedJson = new OnDemandCache<>()

    JSONObject getJson() {
        if (text != null) {
            return cachedJson.fetch {
                new JSONParser().parse(text)
            }
        }
        return null
    }

}
