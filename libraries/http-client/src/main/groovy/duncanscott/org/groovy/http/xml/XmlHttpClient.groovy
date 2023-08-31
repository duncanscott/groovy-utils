package duncanscott.org.groovy.http.xml

import duncanscott.org.groovy.http.client.HttpClient
import duncanscott.org.groovy.http.util.RequestHeader
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpHeaders
import org.apache.hc.core5.http.io.entity.StringEntity
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder

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

    void setBody(ClassicRequestBuilder builder, String text) {
        StringEntity entity = new StringEntity(text, ContentType.APPLICATION_XML, 'UTF-8', false)
        builder.setEntity(entity)
    }
}
