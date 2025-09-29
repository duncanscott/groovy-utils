package duncanscott.org.groovy.http.xml

import duncanscott.org.groovy.utils.http.client.xml.XmlHttpClient
import duncanscott.org.groovy.utils.http.client.xml.XmlHttpResponse
import spock.lang.Specification

class XmlHttpClientIntegrationSpec extends Specification {

    void testGet() {
        setup:
        String url = 'https://gist.githubusercontent.com/franzwong/3838009/raw/e1c9a1fc2d047a5ab0fc38b9056166018614e79f/web.xml'
        //String username = System.getenv('CLARITY_API_USER')
        //String password = System.getenv('CLARITY_API_PASSWORD')

        //expect:
        //username
        //password

        when:
        XmlHttpResponse xmlHttpResponse
        try (XmlHttpClient client = new XmlHttpClient()) {
            xmlHttpResponse = client.get(url).execute()
        }

        then:
        noExceptionThrown()
        xmlHttpResponse.success
        xmlHttpResponse.text

        when:
        Node node = xmlHttpResponse.getXml()

        then:
        noExceptionThrown()
        node
        //node.name() == 'prc:process'
        node.name().toString() == '{http://java.sun.com/xml/ns/javaee}web-app'
    }
}
