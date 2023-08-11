package duncanscott.org.groovy.http.duncanscott.org.groovy.http.util

import org.apache.hc.client5.http.classic.methods.*
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.HttpHeaders
import org.apache.hc.core5.http.io.entity.StringEntity

import java.lang.reflect.Constructor

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
        HttpGet httpGet = new HttpGet(url)
        submitRequest(httpGet)
    }

    K post(String url) {
        HttpPost httpPost = new HttpPost(url)
        submitRequest(httpPost)
    }

    K post(String url, String text) {
        HttpPost httpPost = new HttpPost(url)
        setBody(httpPost, text)
        submitRequest(httpPost)
    }

    K put(String url) {
        HttpPut httpPut = new HttpPut(url)
        submitRequest(httpPut)
    }

    K put(String url, String text) {
        HttpPut httpPut = new HttpPut(url)
        setBody(httpPut, text)
        submitRequest(httpPut)
    }

    K delete(String url) {
        HttpDelete httpDelete = new HttpDelete(url)
        submitRequest(httpDelete)
    }

    private K submitRequest(HttpUriRequestBase request) {
        addHeaders(request)
        K httpResponse = this.responseConstructor.newInstance()
        httpResponse.textResponse = RequestProcessor.submitRequest(request)
        httpResponse
    }

    private static CloseableHttpClient getHttpClient() {
        HttpClients.createDefault()
    }

    private static void setBody(HttpUriRequestBase request, String text) {
        StringEntity entity = new StringEntity(text)
        request.setEntity(entity)
    }

    private void addHeaders(HttpUriRequestBase request) {
        if (authorizationHeader) {
            request.setHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)
        }
        defaultHeaders.each { RequestHeader header ->
            request.setHeader(header.name, header.value)
        }
    }

}
