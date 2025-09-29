package duncanscott.org.groovy.http.xml

import duncanscott.org.groovy.http.client.HttpClientImpl
import duncanscott.org.groovy.http.client.RequestHeader
import org.apache.hc.core5.http.HttpHeaders

class XmlHttpClient extends HttpClientImpl<XmlHttpResponse> {

    XmlHttpClient() {
        super(XmlHttpResponse)
        setHeaders()
    }

    private void setHeaders() {
        defaultHeaders.add(new RequestHeader(HttpHeaders.ACCEPT, 'application/xml'))
        defaultHeaders.add(new RequestHeader(HttpHeaders.CONTENT_TYPE, 'application/xml'))
    }

}
