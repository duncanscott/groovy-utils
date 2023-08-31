package duncanscott.org.groovy.http.json

import duncanscott.org.groovy.http.client.HttpClient
import duncanscott.org.groovy.http.util.RequestHeader
import duncanscott.org.groovy.javautil.http.InputStreamHandler
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpHeaders
import org.apache.hc.core5.http.io.entity.StringEntity
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder
import org.apache.hc.core5.util.Args

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

import java.nio.ByteBuffer

class JsonHttpClient extends HttpClient<JsonHttpResponse> {

    JsonHttpClient() {
        super(JsonHttpResponse)
        setHeaders()
    }

    JsonHttpClient(String username, String password) {
        this()
        setAuthorizationHeader(username, password)
        setHeaders()
    }

    private void setHeaders() {
        defaultHeaders.add(new RequestHeader(HttpHeaders.ACCEPT, 'application/json'))
        defaultHeaders.add(new RequestHeader(HttpHeaders.CONTENT_TYPE, 'application/json'))
    }

    void setBody(ClassicRequestBuilder builder, String text) {
        StringEntity entity = new StringEntity(text, ContentType.APPLICATION_JSON, 'UTF-8', false)
        builder.setEntity(entity)
    }

}
