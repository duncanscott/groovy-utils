package duncanscott.org.groovy.utils.http.client.xml

import duncanscott.org.groovy.utils.http.client.base.AbstractHttpClient
import duncanscott.org.groovy.utils.http.client.base.RequestHeader
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpHeaders

class XmlHttpClient extends AbstractHttpClient<XmlHttpResponse> {

    XmlHttpClient() {
        setHeaders()
    }

    private void setHeaders() {
        defaultHeaders.add(new RequestHeader(HttpHeaders.ACCEPT, 'application/xml'))
        defaultHeaders.add(new RequestHeader(HttpHeaders.CONTENT_TYPE, 'application/xml'))
    }

    ContentType getDefaultRequestContentType() {
        return ContentType.APPLICATION_XML
    }

    protected XmlHttpResponse newResponseInstance() {
        return new XmlHttpResponse()
    }

}
