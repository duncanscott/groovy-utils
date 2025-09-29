package duncanscott.org.groovy.http.client;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Method;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

public class HttpClientRequest<K> {

    private final HttpClient<K> httpClient;
    private final Method method;

    private URI url;
    private Collection<RequestHeader> headers;
    private String body;
    private InputStream inputStream;
    private InputStreamHandler streamHandler;
    private ContentType contentType;

    public HttpClientRequest(HttpClient<K> httpClient, Method method) {
        this.httpClient = httpClient;
        this.method = method;
    }

    public K execute() {
        return httpClient.execute(this);
    }

    public ClassicRequestBuilder getRequestBuilder() {
        switch (method) {
            case GET:
                return ClassicRequestBuilder.get(url);
            case HEAD:
                return ClassicRequestBuilder.head(url);
            case POST:
                return ClassicRequestBuilder.post(url);
            case PUT:
                return ClassicRequestBuilder.put(url);
            case DELETE:
                return ClassicRequestBuilder.delete(url);
            case CONNECT:
                throw new UnsupportedOperationException("CONNECT not supported");
            case TRACE:
                return ClassicRequestBuilder.trace(url);
            case OPTIONS:
                return ClassicRequestBuilder.options(url);
            case PATCH:
                return ClassicRequestBuilder.patch(url);
            default:
                throw new IllegalStateException("Unhandled method: " + method);
        }
    }

    public HttpClientRequest<K> setUrl(URI url) {
        this.url = url;
        return this;
    }

    public HttpClientRequest<K> setUrl(String url) throws URISyntaxException {
        this.url = new URI(url);
        return this;
    }

    public HttpClientRequest<K> setHeaders(Collection<RequestHeader> headers) {
        this.headers = headers;
        return this;
    }

    public HttpClientRequest<K> addHeader(RequestHeader header) {
        if (this.headers == null) {
            this.headers = new ArrayList<>();
        }
        this.headers.add(header);
        return this;
    }

    public HttpClientRequest<K> addHeaders(Collection<RequestHeader> headers) {
        if (headers == null || headers.isEmpty()) {
            return this;
        }
        if (this.headers == null) {
            this.headers = new ArrayList<>();
        }
        this.headers.addAll(headers);
        return this;
    }

    public HttpClientRequest<K> setBody(String body) {
        this.body = body;
        this.inputStream = null;
        return this;
    }

    public HttpClientRequest<K> setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        this.body = null;
        return this;
    }

    public HttpClientRequest<K> setStreamHandler(InputStreamHandler streamHandler) {
        this.streamHandler = streamHandler;
        return this;
    }

    public HttpClientRequest<K> setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public Method getMethod() {
        return method;
    }

    public URI getUrl() {
        return url; // fixed: was "uri" in Groovy snippet
    }

    public Collection<RequestHeader> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public InputStreamHandler getStreamHandler() {
        return streamHandler;
    }

    public ContentType getContentType() {
        return contentType;
    }
}
