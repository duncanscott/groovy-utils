package duncanscott.org.groovy.http.json

import com.fasterxml.jackson.databind.ObjectMapper
import duncanscott.org.groovy.http.client.HttpClient
import duncanscott.org.groovy.http.util.RequestHeader
import duncanscott.org.groovy.javautil.http.InputStreamHandler
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpHeaders

class JsonHttpClient extends HttpClient<JsonHttpResponse> {

    private static final ObjectMapper MAPPER = new ObjectMapper()

    JsonHttpClient() {
        super(JsonHttpResponse)
        setHeaders()
    }

    JsonHttpClient(String username, String password) {
        this()
        setAuthorizationHeader(username, password)
    }

    private void setHeaders() {
        defaultHeaders.add(new RequestHeader(HttpHeaders.ACCEPT, 'application/json'))
        defaultHeaders.add(new RequestHeader(HttpHeaders.CONTENT_TYPE, 'application/json'))
    }

    // --- Body-aware methods --- //

    JsonHttpResponse get(String url, Object body, Collection<RequestHeader> headers = null) {
        String jsonBody = MAPPER.writeValueAsString(body)
        return (JsonHttpResponse) super.get(url, jsonBody, ContentType.APPLICATION_JSON, headers)
    }

    JsonHttpResponse get(String url, Object body, InputStreamHandler streamHandler, Collection<RequestHeader> headers = null) {
        String jsonBody = MAPPER.writeValueAsString(body)
        return (JsonHttpResponse) super.get(url, jsonBody, ContentType.APPLICATION_JSON, streamHandler, headers)
    }

    JsonHttpResponse get(String url, InputStream inputStream, Collection<RequestHeader> headers = null) {
        return (JsonHttpResponse) super.get(url, inputStream, headers)
    }

    JsonHttpResponse get(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers = null) {
        return (JsonHttpResponse) super.get(url, inputStream, streamHandler, headers)
    }

    JsonHttpResponse post(String url, Object body, Collection<RequestHeader> headers = null) {
        String jsonBody = MAPPER.writeValueAsString(body)
        return (JsonHttpResponse) super.post(url, jsonBody, ContentType.APPLICATION_JSON, headers)
    }

    JsonHttpResponse post(String url, Object body, InputStreamHandler streamHandler, Collection<RequestHeader> headers = null) {
        String jsonBody = MAPPER.writeValueAsString(body)
        return (JsonHttpResponse) super.post(url, jsonBody, ContentType.APPLICATION_JSON, streamHandler, headers)
    }

    JsonHttpResponse post(String url, InputStream inputStream, Collection<RequestHeader> headers = null) {
        return (JsonHttpResponse) super.post(url, inputStream, headers)
    }

    JsonHttpResponse post(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers = null) {
        return (JsonHttpResponse) super.post(url, inputStream, streamHandler, headers)
    }

    JsonHttpResponse put(String url, Object body, Collection<RequestHeader> headers = null) {
        String jsonBody = MAPPER.writeValueAsString(body)
        return (JsonHttpResponse) super.put(url, jsonBody, ContentType.APPLICATION_JSON, headers)
    }

    JsonHttpResponse put(String url, Object body, InputStreamHandler streamHandler, Collection<RequestHeader> headers = null) {
        String jsonBody = MAPPER.writeValueAsString(body)
        return (JsonHttpResponse) super.put(url, jsonBody, ContentType.APPLICATION_JSON, streamHandler, headers)
    }

    JsonHttpResponse put(String url, InputStream inputStream, Collection<RequestHeader> headers = null) {
        return (JsonHttpResponse) super.put(url, inputStream, headers)
    }

    JsonHttpResponse put(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers = null) {
        return (JsonHttpResponse) super.put(url, inputStream, streamHandler, headers)
    }

    JsonHttpResponse delete(String url, Object body, Collection<RequestHeader> headers = null) {
        String jsonBody = MAPPER.writeValueAsString(body)
        return (JsonHttpResponse) super.delete(url, jsonBody, ContentType.APPLICATION_JSON, headers)
    }

    JsonHttpResponse delete(String url, Object body, InputStreamHandler streamHandler, Collection<RequestHeader> headers = null) {
        String jsonBody = MAPPER.writeValueAsString(body)
        return (JsonHttpResponse) super.delete(url, jsonBody, ContentType.APPLICATION_JSON, streamHandler, headers)
    }

    JsonHttpResponse delete(String url, InputStream inputStream, Collection<RequestHeader> headers = null) {
        return (JsonHttpResponse) super.delete(url, inputStream, headers)
    }

    JsonHttpResponse delete(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers = null) {
        return (JsonHttpResponse) super.delete(url, inputStream, streamHandler, headers)
    }
}
