package duncanscott.org.groovy.http.client


import duncanscott.org.groovy.javautil.http.InputStreamHandler
import org.apache.hc.core5.http.ContentType
import  org.apache.hc.core5.http.Method
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder

class Request {

    final Method method
    private URI url
    private Collection<RequestHeader> headers
    private String body
    private InputStream inputStream
    private InputStreamHandler streamHandler
    private ContentType contentType

    Request(Method method) {
        this.method = method
    }

    ClassicRequestBuilder getRequestBuilder() {
        switch (method) {
            case Method.GET:
                return ClassicRequestBuilder.get(url)
            case Method.HEAD:
                return ClassicRequestBuilder.head(url)
            case Method.POST:
                return ClassicRequestBuilder.post(url)
            case Method.PUT:
                return ClassicRequestBuilder.put(url)
            case Method.DELETE:
                return ClassicRequestBuilder.delete(url)
            case Method.CONNECT:
                return ClassicRequestBuilder.connect(url)
            case Method.TRACE:
                return ClassicRequestBuilder.trace(url)
            case Method.OPTIONS:
                return ClassicRequestBuilder.options(url)
            case Method.PATCH:
                return ClassicRequestBuilder.patch(url)
        }
    }

    Request setUrl(URI url) {
        this.url = url
        return this
    }

    Request setUrl(String url) {
        this.url = new URI(url)
        return this
    }

    Request setHeaders(Collection<RequestHeader> headers) {
        this.headers = headers
        return this
    }

    Request addHeader(RequestHeader header) {
        if (headers == null) {
            headers = []
        }
        headers.add(header)
        return this
    }

    Request addHeaders(Collection<RequestHeader> headers) {
        if (this.headers == null && headers) {
            this.headers = []
        }
        if (headers) {
            this.headers.addAll(headers)
        }
        return this
    }

    Request setBody(String body) {
        this.body = body
        this.inputStream = null
        return this
    }

    Request setInputStream(InputStream inputStream) {
        this.inputStream = inputStream
        this.body = null
        return this
    }

    Request setStreamHandler(InputStreamHandler streamHandler) {
        this.streamHandler = streamHandler
        return this
    }

    Request setContentType(ContentType contentType) {
        this.contentType = contentType
        return this
    }

    Method getMethod() {
        return method
    }

    URI getUrl() {
        return uri
    }

    Collection<RequestHeader> getHeaders() {
        return headers
    }

    String getBody() {
        return body
    }

    InputStream getInputStream() {
        return inputStream
    }

    InputStreamHandler getStreamHandler() {
        return streamHandler
    }

    ContentType getContentType() {
        return contentType
    }

}
