package duncanscott.org.groovy.http.xml

import duncanscott.org.groovy.http.duncanscott.org.groovy.http.util.HttpClient
import duncanscott.org.groovy.http.duncanscott.org.groovy.http.util.RequestHeader

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
