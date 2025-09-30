package duncanscott.org.groovy.utils.http.client.text

import duncanscott.org.groovy.utils.http.client.base.AbstractHttpClient
import duncanscott.org.groovy.utils.http.client.base.HttpClientResponse
import duncanscott.org.groovy.utils.http.client.base.RequestHeader
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpHeaders

class TextHttpClient extends AbstractHttpClient<HttpClientResponse> {

    TextHttpClient() {
        setHeaders()
    }

    private void setHeaders() {
        defaultHeaders.add(new RequestHeader(HttpHeaders.ACCEPT, ContentType.TEXT_PLAIN.mimeType))
        defaultHeaders.add(new RequestHeader(HttpHeaders.CONTENT_TYPE, ContentType.TEXT_PLAIN.mimeType))
    }

    ContentType getDefaultRequestContentType() {
        return ContentType.TEXT_PLAIN
    }

    protected HttpClientResponse newResponseInstance() {
        return new HttpClientResponse()
    }

}
