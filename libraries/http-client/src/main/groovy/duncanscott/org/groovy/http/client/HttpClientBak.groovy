package duncanscott.org.groovy.http.client


import duncanscott.org.groovy.javautil.http.InputStreamHandler
import duncanscott.org.groovy.javautil.http.TextResponse
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ClassicHttpRequest
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpEntity
import org.apache.hc.core5.http.HttpHeaders
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.entity.InputStreamEntity
import org.apache.hc.core5.http.io.entity.StringEntity
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder

import java.lang.reflect.Constructor

class HttpClientBak<K extends Response> implements Closeable {

    private final Constructor<K> responseConstructor
    private final CloseableHttpClient internalClient

    final List<RequestHeader> defaultHeaders = []
    String authorizationHeader

    HttpClientBak(Class<K> responseClass) {
        this.responseConstructor = responseClass.getConstructor()
        this.internalClient = HttpClients.createDefault()
    }

    HttpClientBak setAuthorizationHeader(String username, String password) {
        this.authorizationHeader = "Basic " + "${username}:${password}".bytes.encodeBase64().toString()
        return this
    }

    void close() throws IOException {
        internalClient.close()
    }

    // --- Public, Common Methods for Subclasses to Inherit --- //
    public K get(String url, Collection<RequestHeader> headers) {
        return execute(ClassicRequestBuilder.get(url), headers)
    }

    public K get(String url, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        return execute(ClassicRequestBuilder.get(url), headers, streamHandler)
    }

    public K post(String url, Collection<RequestHeader> headers) {
        return execute(ClassicRequestBuilder.post(url), headers)
    }

    public K post(String url, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        return execute(ClassicRequestBuilder.post(url), headers, streamHandler)
    }

    public K put(String url, Collection<RequestHeader> headers) {
        return execute(ClassicRequestBuilder.put(url), headers)
    }

    public K put(String url, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        return execute(ClassicRequestBuilder.put(url), headers, streamHandler)
    }

    public K delete(String url, Collection<RequestHeader> headers) {
        return execute(ClassicRequestBuilder.delete(url), headers)
    }

    public K delete(String url, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        return execute(ClassicRequestBuilder.delete(url), headers, streamHandler)
    }

    // --- Protected Template Methods for Subclasses --- //
    protected K get(String url, String text, ContentType contentType, Collection<RequestHeader> headers) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.get(url)
        builder.setEntity(new StringEntity(text, contentType))
        return execute(builder, headers)
    }

    protected K get(String url, String text, ContentType contentType, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.get(url)
        builder.setEntity(new StringEntity(text, contentType))
        return execute(builder, headers, streamHandler)
    }

    protected K get(String url, InputStream inputStream, Collection<RequestHeader> headers) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.get(url)
        builder.setEntity(new InputStreamEntity(inputStream, ContentType.APPLICATION_OCTET_STREAM))
        return execute(builder, headers)
    }

    protected K get(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.get(url)
        builder.setEntity(new InputStreamEntity(inputStream, ContentType.APPLICATION_OCTET_STREAM))
        return execute(builder, headers, streamHandler)
    }

    protected K post(String url, String text, ContentType contentType, Collection<RequestHeader> headers) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.post(url)
        builder.setEntity(new StringEntity(text, contentType))
        return execute(builder, headers)
    }

    protected K post(String url, String text, ContentType contentType, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.post(url)
        builder.setEntity(new StringEntity(text, contentType))
        return execute(builder, headers, streamHandler)
    }

    protected K post(String url, InputStream inputStream, Collection<RequestHeader> headers) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.post(url)
        builder.setEntity(new InputStreamEntity(inputStream, ContentType.APPLICATION_OCTET_STREAM))
        return execute(builder, headers)
    }

    protected K post(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.post(url)
        builder.setEntity(new InputStreamEntity(inputStream, ContentType.APPLICATION_OCTET_STREAM))
        return execute(builder, headers, streamHandler)
    }

    protected K put(String url, String text, ContentType contentType, Collection<RequestHeader> headers) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.put(url)
        builder.setEntity(new StringEntity(text, contentType))
        return execute(builder, headers)
    }

    protected K put(String url, String text, ContentType contentType, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.put(url)
        builder.setEntity(new StringEntity(text, contentType))
        return execute(builder, headers, streamHandler)
    }

    protected K put(String url, InputStream inputStream, Collection<RequestHeader> headers) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.put(url)
        builder.setEntity(new InputStreamEntity(inputStream, ContentType.APPLICATION_OCTET_STREAM))
        return execute(builder, headers)
    }

    protected K put(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.put(url)
        builder.setEntity(new InputStreamEntity(inputStream, ContentType.APPLICATION_OCTET_STREAM))
        return execute(builder, headers, streamHandler)
    }

    protected K delete(String url, String text, ContentType contentType, Collection<RequestHeader> headers) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.delete(url)
        builder.setEntity(new StringEntity(text, contentType))
        return execute(builder, headers)
    }

    protected K delete(String url, String text, ContentType contentType, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.delete(url)
        builder.setEntity(new StringEntity(text, contentType))
        return execute(builder, headers, streamHandler)
    }

    protected K delete(String url, InputStream inputStream, Collection<RequestHeader> headers) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.delete(url)
        builder.setEntity(new InputStreamEntity(inputStream, ContentType.APPLICATION_OCTET_STREAM))
        return execute(builder, headers)
    }

    protected K delete(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.delete(url)
        builder.setEntity(new InputStreamEntity(inputStream, ContentType.APPLICATION_OCTET_STREAM))
        return execute(builder, headers, streamHandler)
    }

    // --- Hooks for subclasses --- //
    void beforeRequest(ClassicHttpRequest httpRequest) { }

    void afterRequest(ClassicHttpRequest httpRequest, K httpResponse) { }

    // --- Private --- //
    private K execute(ClassicRequestBuilder requestBuilder, Collection<RequestHeader> headers, InputStreamHandler streamHandler = null) {
        // 1. Prepare URI
        URI uri = requestBuilder.getUri()
        if (uri == null) throw new InvalidUrlException("URL cannot be null")
        if (uri.scheme == null) throw new InvalidUrlException(uri.toString(), "URL scheme cannot be null")
        if (uri.host == null) throw new InvalidUrlException(uri.toString(), "URL host cannot be null")

        // 2. Add all headers (default and per-request)
        addHeaders(requestBuilder, headers)

        // 3. Build and execute
        ClassicHttpRequest request = requestBuilder.build()
        K httpResponse = responseConstructor.newInstance()
        TextResponse textResponse = new TextResponse()
        textResponse.requestUri = request.getUri().toString()
        textResponse.requestMethod = request.getMethod()

        beforeRequest(request)

        internalClient.execute(request, {
            response ->
                textResponse.statusCode = response.getCode()
                textResponse.reasonPhrase = response.getReasonPhrase()
                textResponse.locale = response.getLocale()

                final HttpEntity entity = response.getEntity()
                if (entity != null) {
                    try {
                        if (streamHandler != null) {
                            entity.getContent().withStream(streamHandler.&call)
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
        afterRequest(request, httpResponse)
        return httpResponse
    }

    private void addHeaders(ClassicRequestBuilder builder, Collection<RequestHeader> headers) {
        // Apply defaults first
        if (authorizationHeader) {
            builder.setHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)
        }
        defaultHeaders?.each { RequestHeader header ->
            builder.addHeader(header.name, header.value)
        }
        // Apply per-request headers, potentially overriding defaults
        headers?.each { RequestHeader header ->
            builder.setHeader(header.name, header.value) // setHeader overrides
        }
    }
}
