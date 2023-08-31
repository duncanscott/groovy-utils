package duncanscott.org.groovy.http.json

import duncanscott.org.groovy.http.client.HttpClient
import duncanscott.org.groovy.http.util.RequestHeader
import duncanscott.org.groovy.javautil.http.InputStreamHandler
import org.apache.hc.core5.http.HttpHeaders
import org.apache.hc.core5.http.io.entity.StringEntity
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder

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

    static String toUtf8(String text) {
        ByteBuffer buffer = StandardCharsets.UTF_8.encode(text);
        return StandardCharsets.UTF_8.decode(buffer).toString();
    }

    void setBody(ClassicRequestBuilder builder, String text) {
        StringEntity entity = new StringEntity(toUtf8(text))
        builder.setEntity(entity)
    }
}
