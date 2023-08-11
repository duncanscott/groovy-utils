package duncanscott.org.groovy.http.json

import duncanscott.org.groovy.http.duncanscott.org.groovy.http.util.HttpClient
import duncanscott.org.groovy.http.duncanscott.org.groovy.http.util.RequestHeader
import org.apache.hc.core5.http.HttpHeaders

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

}
