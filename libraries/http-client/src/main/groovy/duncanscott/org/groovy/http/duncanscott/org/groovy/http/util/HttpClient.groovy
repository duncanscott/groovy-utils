package duncanscott.org.groovy.http.duncanscott.org.groovy.http.util


import org.apache.hc.client5.http.entity.EntityBuilder
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ClassicHttpRequest
import org.apache.hc.core5.http.HttpEntity
import org.apache.hc.core5.http.HttpHeaders
import org.apache.hc.core5.http.HttpHost
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
        submitRequest(ClassicRequestBuilder.get(), url)
    }

    K get(String url, String text) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.get()
        setBody(builder, text)
        submitRequest(builder, url)
    }

    K get(String url, InputStreamHandler inputStreamHandler) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.get()
        submitRequest(builder, url)
    }

    K post(String url) {
        submitRequest(ClassicRequestBuilder.post(), url)
    }

    K post(String url, String text) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.post()
        setBody(builder, text)
        submitRequest(builder, url)
    }

    K post(String url, InputStream inputStream) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.post()
        setInputStream(builder, inputStream)
        submitRequest(builder, url)
    }

    K put(String url) {
        submitRequest(ClassicRequestBuilder.put(), url)
    }

    K put(String url, InputStream inputStream) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.put()
        setInputStream(builder, inputStream)
        submitRequest(builder, url)
    }

    K put(String url, String text) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.put()
        setBody(builder, text)
        submitRequest(builder, url)
    }

    K delete(String url) {
        submitRequest(ClassicRequestBuilder.delete(), url)
    }

    K delete(String url, String text) {
        ClassicRequestBuilder builder = ClassicRequestBuilder.delete()
        setBody(builder, text)
        submitRequest(builder, url)
    }

    void beforeRequest(ClassicHttpRequest httpRequest) {

    }

    void afterRequest(ClassicHttpRequest httpRequest, K httpResponse) {

    }

    private ClassicHttpRequest prepareRequest(ClassicRequestBuilder requestBuilder, String url) {
        URI uri = new URI(url)
        String scheme = uri.scheme
        String host = uri.host
        int port = uri.port ?: -1

        HttpHost httpHost = new HttpHost(scheme, host, port) // -1 is default port for scheme
        requestBuilder.setHttpHost(httpHost)
        requestBuilder.setUri(uri)
        addHeaders(requestBuilder)
        requestBuilder.build()
    }

    private K submitRequest(ClassicRequestBuilder requestBuilder, String url) {
        ClassicHttpRequest request = prepareRequest(requestBuilder,url)
        K httpResponse = this.responseConstructor.newInstance()
        beforeRequest(request)
        httpResponse.textResponse = RequestProcessor.submitRequest(request)
        afterRequest(request, httpResponse)
        httpResponse
    }

    private K submitRequest(ClassicRequestBuilder requestBuilder, String url, InputStreamHandler inputStreamHandler) {
        ClassicHttpRequest request = prepareRequest(requestBuilder,url)
        K httpResponse = this.responseConstructor.newInstance()
        beforeRequest(request)
        httpResponse.textResponse = RequestProcessor.submitRequest(request,inputStreamHandler)
        afterRequest(request, httpResponse)
        httpResponse
    }

    private static CloseableHttpClient getHttpClient() {
        HttpClients.createDefault()
    }

    private static void setInputStream(ClassicRequestBuilder builder, InputStream inputStream) {
        EntityBuilder entityBuilder = EntityBuilder.create()
        entityBuilder.setStream(inputStream)
        HttpEntity entity = entityBuilder.build()
        builder.setEntity(entity)
    }

    private static void setBody(ClassicRequestBuilder builder, String text) {
        StringEntity entity = new StringEntity(text)
        builder.setEntity(entity)
    }

    private void addHeaders(ClassicRequestBuilder builder) {
        if (authorizationHeader) {
            builder.setHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)
        }
        defaultHeaders.each { RequestHeader header ->
            builder.addHeader(header.name, header.value)
        }
    }

}
