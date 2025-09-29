package duncanscott.org.groovy.utils.http.client.xml

import duncanscott.org.groovy.utils.http.client.base.HttpClientResponse
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
