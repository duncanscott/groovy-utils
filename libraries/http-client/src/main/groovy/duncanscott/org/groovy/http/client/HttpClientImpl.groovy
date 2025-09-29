package duncanscott.org.groovy.http.client

import duncanscott.org.groovy.javautil.http.TextResponse
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.*
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.entity.StringEntity
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder

import java.lang.reflect.Constructor

class HttpClientImpl<K extends HttpClientResponse> implements HttpClient, Closeable {

    private final Constructor<K> responseConstructor
    private final CloseableHttpClient internalClient

    private BeforeRequestInterceptor beforeRequestInterceptor
    private AfterRequestInterceptor afterRequestInterceptor

    final List<RequestHeader> defaultHeaders = []

    HttpClientImpl(Class<K> responseClass) {
        this.responseConstructor = responseClass.getConstructor()
        this.internalClient = HttpClients.createDefault()
    }

    HttpClientImpl setAuthorizationHeader(String username, String password) {
        String basicHash = 'Basic ' + "${username}:${password}".bytes.encodeBase64().toString()
        defaultHeaders.add(new RequestHeader(HttpHeaders.AUTHORIZATION, basicHash))
        return this
    }

    void close() throws IOException {
        internalClient.close()
    }

    @Override
    HttpClientRequest get() {
        new HttpClientRequest(this, Method.GET)
    }

    @Override
    HttpClientRequest head() {
        new HttpClientRequest(this, Method.HEAD)
    }

    @Override
    HttpClientRequest post() {
        new HttpClientRequest(this, Method.POST)
    }

    @Override
    HttpClientRequest put() {
        new HttpClientRequest(this, Method.PUT)
    }

    @Override
    HttpClientRequest delete() {
        new HttpClientRequest(this, Method.DELETE)
    }

    @Override
    HttpClientRequest trace() {
        new HttpClientRequest(this, Method.TRACE)
    }

    @Override
    HttpClientRequest options() {
        new HttpClientRequest(this, Method.OPTIONS)
    }

    @Override
    HttpClientRequest patch() {
        new HttpClientRequest(this, Method.PATCH)
    }

    @Override
    HttpClientRequest get(String url) {
        new HttpClientRequest(this, Method.GET).setUrl(url)
    }

    @Override
    HttpClientRequest head(String url) {
        new HttpClientRequest(this, Method.HEAD).setUrl(url)
    }

    @Override
    HttpClientRequest post(String url) {
        new HttpClientRequest(this, Method.POST).setUrl(url)
    }

    @Override
    HttpClientRequest put(String url) {
        new HttpClientRequest(this, Method.PUT).setUrl(url)
    }

    @Override
    HttpClientRequest delete(String url) {
        new HttpClientRequest(this, Method.DELETE).setUrl(url)
    }

    @Override
    HttpClientRequest trace(String url) {
        new HttpClientRequest(this, Method.TRACE).setUrl(url)
    }

    @Override
    HttpClientRequest options(String url) {
        new HttpClientRequest(this, Method.OPTIONS).setUrl(url)
    }

    @Override
    HttpClientRequest patch(String url) {
        new HttpClientRequest(this, Method.PATCH).setUrl(url)
    }

    @Override
    HttpClientRequest get(URI url) {
        new HttpClientRequest(this, Method.GET).setUrl(url)
    }

    @Override
    HttpClientRequest head(URI url) {
        new HttpClientRequest(this, Method.HEAD).setUrl(url)
    }

    @Override
    HttpClientRequest post(URI url) {
        new HttpClientRequest(this, Method.POST).setUrl(url)
    }

    @Override
    HttpClientRequest put(URI url) {
        new HttpClientRequest(this, Method.PUT).setUrl(url)
    }

    @Override
    HttpClientRequest delete(URI url) {
        new HttpClientRequest(this, Method.DELETE).setUrl(url)
    }

    @Override
    HttpClientRequest trace(URI url) {
        new HttpClientRequest(this, Method.TRACE).setUrl(url)
    }

    @Override
    HttpClientRequest options(URI url) {
        new HttpClientRequest(this, Method.OPTIONS).setUrl(url)
    }

    @Override
    HttpClientRequest patch(URI url) {
        new HttpClientRequest(this, Method.PATCH).setUrl(url)
    }

    @Override
    HttpClientImpl<K> setInterceptor(BeforeRequestInterceptor interceptor) {
        this.beforeRequestInterceptor = beforeRequestInterceptor
        return this
    }

    @Override
    HttpClientImpl<K> setInterceptor(AfterRequestInterceptor interceptor) {
        this.afterRequestInterceptor = afterRequestInterceptor
        return this
    }

    ContentType getDefaultRequestContentType() {
        ContentType.TEXT_PLAIN
    }

    K execute(HttpClientRequest request) {
        ClassicRequestBuilder requestBuilder = request.requestBuilder
        URI uri = requestBuilder.uri
        if (uri == null) throw new InvalidUrlException("URL cannot be null")
        if (uri.scheme == null) throw new InvalidUrlException(uri.toString(), "URL scheme cannot be null")
        if (uri.host == null) throw new InvalidUrlException(uri.toString(), "URL host cannot be null")

        if (request.body) {
            requestBuilder.setEntity(new StringEntity(request.body, request.contentType ?: defaultRequestContentType))
        }

        addHeaders(requestBuilder, request.headers)

        ClassicHttpRequest httpRequest = requestBuilder.build()
        K httpResponse = responseConstructor.newInstance()
        TextResponse textResponse = new TextResponse()
        textResponse.requestUri = httpRequest.getUri().toString()
        textResponse.requestMethod = httpRequest.getMethod()

        beforeRequestInterceptor?.beforeRequest(httpRequest)

        internalClient.execute(httpRequest, { ClassicHttpResponse response ->
            textResponse.statusCode = response.getCode()
            textResponse.reasonPhrase = response.getReasonPhrase()
            textResponse.locale = response.getLocale()

            final HttpEntity entity = response.getEntity()
            if (entity != null) {
                try {
                    if (request.streamHandler != null) {
                        entity.getContent().withStream(request.streamHandler.&call)
                    } else {
                        textResponse.text = EntityUtils.toString(entity)
                    }
                } finally {
                    EntityUtils.consume(entity)
                }
            }
            return null
        })
        httpResponse.textResponse = textResponse

        afterRequestInterceptor?.afterRequest(httpRequest, httpResponse)
        return httpResponse
    }

    private void addHeaders(ClassicRequestBuilder builder, Collection<RequestHeader> headers) {
        // Apply defaults first
        defaultHeaders?.each { RequestHeader header ->
            builder.addHeader(header.name, header.value)
        }
        // Apply per-request headers, potentially overriding defaults
        headers?.each { RequestHeader header ->
            builder.setHeader(header.name, header.value) // setHeader overrides
        }
    }
}
