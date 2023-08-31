package duncanscott.org.groovy.http.client

import duncanscott.org.groovy.http.client.HttpResponse
import duncanscott.org.groovy.http.util.InvalidUrlException
import duncanscott.org.groovy.http.util.RequestHeader
import duncanscott.org.groovy.javautil.http.InputStreamHandler
import duncanscott.org.groovy.javautil.http.RequestProcessor
import org.apache.hc.client5.http.entity.EntityBuilder
import org.apache.hc.core5.http.*
import org.apache.hc.core5.http.io.entity.StringEntity
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder

import java.lang.reflect.Constructor

// https://hc.apache.org/httpcomponents-client-5.2.x/quickstart.html

class HttpClient<K extends HttpResponse> {

    private String authorizationHeader
    private final Constructor<K> responseConstructor
    final List<RequestHeader> defaultHeaders = []

    HttpClient(Class<K> responseClass) {
        this.responseConstructor = responseClass.getConstructor()
        this.authorizationHeader = null
    }

    HttpClient setAuthorizationHeader(String username, String password) {
        this.authorizationHeader = 'Basic ' + "${username}:${password}".bytes.encodeBase64().toString()
        return this
    }

    K get(String url) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.get()
        addHeaders(builder, defaultHeaders)
        submitRequest(builder, url)
    }

    /**
     *
     * @param url
     * @param inputStreamHandler
     * @param headers
     * @return
     *
     * The defaultHeaders are not applied when this signature of get is called.  (The authorization header
     * if defined is applied.)  Specify and headers for the request in the headers method param.
     */
    K get(String url, InputStreamHandler inputStreamHandler, Collection<RequestHeader> headers = null) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.get()
        addHeaders(builder, headers)
        submitRequest(builder, url, inputStreamHandler)
    }

    K get(String url, String text) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.get()
        setBody(builder, text)
        addHeaders(builder, defaultHeaders)
        submitRequest(builder, url)
    }

    K get(String url, String text, InputStreamHandler inputStreamHandler, Collection<RequestHeader> headers = null) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.get()
        setBody(builder, text)
        addHeaders(builder, headers)
        submitRequest(builder, url, inputStreamHandler)
    }

    K post(String url) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.post()
        addHeaders(builder, defaultHeaders)
        submitRequest(builder, url)
    }

    K post(String url, InputStreamHandler inputStreamHandler, Collection<RequestHeader> headers = null) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.post()
        addHeaders(builder, headers)
        submitRequest(builder, url, inputStreamHandler)
    }

    K post(String url, String text) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.post()
        setBody(builder, text)
        addHeaders(builder, defaultHeaders)
        submitRequest(builder, url)
    }

    K post(String url, String text, InputStreamHandler inputStreamHandler, Collection<RequestHeader> headers = null) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.post()
        setBody(builder, text)
        addHeaders(builder, headers)
        submitRequest(builder, url, inputStreamHandler)
    }

    K post(String url, InputStream inputStream, Collection<RequestHeader> headers = null) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.post()
        setInputStream(builder, inputStream)
        addHeaders(builder, headers)
        submitRequest(builder, url)
    }

    K put(String url) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.put()
        addHeaders(builder, defaultHeaders)
        submitRequest(builder, url)
    }

    K put(String url, InputStreamHandler inputStreamHandler, Collection<RequestHeader> headers = null) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.put()
        addHeaders(builder, headers)
        submitRequest(builder, url, inputStreamHandler)
    }

    K put(String url, InputStream inputStream, Collection<RequestHeader> headers = null) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.put()
        setInputStream(builder, inputStream)
        addHeaders(builder, headers)
        submitRequest(builder, url)
    }

    K put(String url, String text) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.put()
        setBody(builder, text)
        addHeaders(builder, defaultHeaders)
        submitRequest(builder, url)
    }

    K put(String url, String text, InputStreamHandler inputStreamHandler, Collection<RequestHeader> headers = null) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.put()
        setBody(builder, text)
        addHeaders(builder, headers)
        submitRequest(builder, url, inputStreamHandler)
    }

    K delete(String url) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.delete()
        addHeaders(builder, defaultHeaders)
        submitRequest(builder, url)
    }

    K delete(String url, String text) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.delete()
        setBody(builder, text)
        addHeaders(builder, defaultHeaders)
        submitRequest(builder, url)
    }

    void beforeRequest(ClassicHttpRequest httpRequest) {

    }

    void afterRequest(ClassicHttpRequest httpRequest, K httpResponse) {

    }

    void prepareRequest(ClassicRequestBuilder requestBuilder, String url) {
        if (!url) throw new InvalidUrlException()
        URI uri = new URI(url)
        String scheme = uri.scheme
        String host = uri.host
        int port = uri.port ?: -1
        if (!scheme) throw new InvalidUrlException(url, "null scheme for URL [${url}]")
        if (!host) throw new InvalidUrlException(url, "null host for URL [${url}]")

        HttpHost httpHost = new HttpHost(scheme, host, port) // -1 is default port for scheme
        requestBuilder.setHttpHost(httpHost)
        requestBuilder.setUri(uri)
    }

    K submitRequest(ClassicRequestBuilder requestBuilder, String url) {
        prepareRequest(requestBuilder, url)
        ClassicHttpRequest request = requestBuilder.build()
        K httpResponse = this.responseConstructor.newInstance()
        beforeRequest(request)
        httpResponse.textResponse = RequestProcessor.submitRequest(request)
        afterRequest(request, httpResponse)
        httpResponse
    }

    K submitRequest(ClassicRequestBuilder requestBuilder, String url, InputStreamHandler inputStreamHandler) {
        prepareRequest(requestBuilder, url)
        ClassicHttpRequest request = requestBuilder.build()
        K httpResponse = this.responseConstructor.newInstance()
        beforeRequest(request)
        httpResponse.textResponse = RequestProcessor.submitRequest(request, inputStreamHandler)
        afterRequest(request, httpResponse)
        httpResponse
    }

    void setInputStream(ClassicRequestBuilder builder, InputStream inputStream) {
        HttpEntity entity = EntityBuilder.create().setStream(inputStream).setContentType(ContentType.APPLICATION_OCTET_STREAM).build()
        builder.setEntity(entity)
    }

    void setBody(ClassicRequestBuilder builder, String text) {
        StringEntity entity = new StringEntity(text)
        builder.setEntity(entity)
    }

    void addHeaders(ClassicRequestBuilder builder, Collection<RequestHeader> headers) {
        if (authorizationHeader) {
            builder.setHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)
        }
        headers?.each { RequestHeader header ->
            builder.addHeader(header.name, header.value)
        }
    }

}
