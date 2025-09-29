package duncanscott.org.groovy.http.xml

import duncanscott.org.groovy.http.client.HttpClientResponse
import duncanscott.org.groovy.utils.ondemandcache.OnDemandCache
import groovy.xml.XmlParser

class XmlHttpResponse extends HttpClientResponse {

    final OnDemandCache<Node> cachedXml = new OnDemandCache<>()

    Node getXml() {
        if (cachedXml.cachedObject) {
            return cachedXml.cachedObject
        }
        if (text != null) {
            return cachedXml.fetch {
                new XmlParser().parseText(text)
            }
        }
        return null
    }

}
