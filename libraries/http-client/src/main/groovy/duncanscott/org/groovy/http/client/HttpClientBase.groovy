package duncanscott.org.groovy.http.client


import duncanscott.org.groovy.javautil.http.InputStreamHandler
import duncanscott.org.groovy.javautil.http.TextResponse
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ClassicHttpRequest
import org.apache.hc.core5.http.ClassicHttpResponse
import org.apache.hc.core5.http.HttpEntity
import org.apache.hc.core5.http.HttpHeaders
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.entity.StringEntity
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder
import org.apache.hc.core5.http.ContentType
import java.lang.reflect.Constructor

class HttpClientBase<K extends Response> implements HttpClient, Closeable {

    private final Constructor<K> responseConstructor
    private final CloseableHttpClient internalClient

    private BeforeRequestInterceptor beforeRequestInterceptor
    private AfterRequestInterceptor afterRequestInterceptor

    final List<RequestHeader> defaultHeaders = []
    String authorizationHeader

    HttpClientBase(Class<K> responseClass) {
        this.responseConstructor = responseClass.getConstructor()
        this.internalClient = HttpClients.createDefault()
    }

    HttpClientBase setAuthorizationHeader(String username, String password) {
        this.authorizationHeader = "Basic " + "${username}:${password}".bytes.encodeBase64().toString()
        return this
    }

    void close() throws IOException {
        internalClient.close()
    }

    @Override
    HttpClientBase<K> setInterceptor(BeforeRequestInterceptor interceptor) {
        this.beforeRequestInterceptor = beforeRequestInterceptor
        return this
    }

    @Override
    HttpClientBase<K> setInterceptor(AfterRequestInterceptor interceptor) {
        this.afterRequestInterceptor = afterRequestInterceptor
        return this
    }

    K execute(Request request) {
        ClassicRequestBuilder builder = request.requestBuilder
        if (request.body) {
            builder.setEntity(new StringEntity(request.body, request.contentType ?: ContentType.TEXT_PLAIN))
        }
        doExecute(builder, request.headers, request.streamHandler)
    }

    private K doExecute(ClassicRequestBuilder requestBuilder, Collection<RequestHeader> headers, InputStreamHandler streamHandler) {

        URI uri = requestBuilder.uri
        if (uri == null) throw new InvalidUrlException("URL cannot be null")
        if (uri.scheme == null) throw new InvalidUrlException(uri.toString(), "URL scheme cannot be null")
        if (uri.host == null) throw new InvalidUrlException(uri.toString(), "URL host cannot be null")

        addHeaders(requestBuilder, headers)

        ClassicHttpRequest request = requestBuilder.build()
        K httpResponse = responseConstructor.newInstance()
        TextResponse textResponse = new TextResponse()
        textResponse.requestUri = request.getUri().toString()
        textResponse.requestMethod = request.getMethod()

        beforeRequestInterceptor?.beforeRequest(request)

        internalClient.execute(request, { ClassicHttpResponse response ->
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

        afterRequestInterceptor?.afterRequest(request, httpResponse)
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
