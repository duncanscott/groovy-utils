package duncanscott.org.groovy.http.xml

import duncanscott.org.groovy.http.duncanscott.org.groovy.http.util.HttpResponse
import duncanscott.org.groovy.utils.ondemandcache.OnDemandCache
import groovy.xml.XmlParser

class XmlHttpResponse extends HttpResponse {

    private final OnDemandCache<Node> cachedXml = new OnDemandCache<>()

    Node getXml() {
        if (body != null) {
            return cachedXml.fetch {
                new XmlParser().parse(this.text)
            }
        }
        return null
    }

}
