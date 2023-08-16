package duncanscott.org.groovy.http.duncanscott.org.groovy.http.client.xml

import duncanscott.org.groovy.http.duncanscott.org.groovy.http.client.HttpResponse
import duncanscott.org.groovy.utils.ondemandcache.OnDemandCache
import groovy.xml.XmlParser

class XmlHttpResponse extends HttpResponse {

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
