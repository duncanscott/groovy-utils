package duncanscott.org.groovy.http.client.json


import duncanscott.org.groovy.http.util.RequestHeader
import org.apache.hc.core5.http.HttpHeaders

class JsonHttpClient extends duncanscott.org.groovy.http.client.HttpClient<JsonHttpResponse> {

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
