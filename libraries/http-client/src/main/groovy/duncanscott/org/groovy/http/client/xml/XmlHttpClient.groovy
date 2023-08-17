package duncanscott.org.groovy.http.client.xml

import duncanscott.org.groovy.http.client.HttpClient
import duncanscott.org.groovy.http.util.RequestHeader
import org.apache.hc.core5.http.HttpHeaders

class XmlHttpClient extends HttpClient<XmlHttpResponse> {

    XmlHttpClient() {
        super(XmlHttpResponse)
        setHeaders()
    }

    XmlHttpClient(String username, String password) {
        this()
        setAuthorizationHeader(username, password)
        setHeaders()
    }

    private void setHeaders() {
        defaultHeaders.add(new RequestHeader(HttpHeaders.ACCEPT, 'application/xml'))
        defaultHeaders.add(new RequestHeader(HttpHeaders.CONTENT_TYPE, 'application/xml'))
    }

}
