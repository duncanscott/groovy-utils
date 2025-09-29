package duncanscott.org.groovy.utils.http.client.xml

import duncanscott.org.groovy.utils.http.client.HttpClientImpl
import duncanscott.org.groovy.utils.http.client.HttpClientRequest
import duncanscott.org.groovy.utils.http.client.RequestHeader
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

    XmlHttpResponse execute(HttpClientRequest request) {
        return (XmlHttpResponse) super.execute(request)
    }
}
