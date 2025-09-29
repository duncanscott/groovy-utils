package duncanscott.org.groovy.http.json

import com.fasterxml.jackson.databind.ObjectMapper
import duncanscott.org.groovy.http.client.HttpClientBase
import duncanscott.org.groovy.http.client.RequestHeader
import duncanscott.org.groovy.javautil.http.InputStreamHandler
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpHeaders

class JsonHttpClient extends HttpClientBase<JsonHttpResponse> {

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

    @Override
    JsonHttpResponse get(String url, Object body, Collection<RequestHeader> headers) {
        String jsonBody = MAPPER.writeValueAsString(body)
        return (JsonHttpResponse) super.get(url, jsonBody, ContentType.APPLICATION_JSON, headers)
    }

    @Override
    JsonHttpResponse get(String url, Object body, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        String jsonBody = MAPPER.writeValueAsString(body)
        return (JsonHttpResponse) super.get(url, jsonBody, ContentType.APPLICATION_JSON, streamHandler, headers)
    }

    @Override
    JsonHttpResponse get(String url, InputStream inputStream, Collection<RequestHeader> headers) {
        return (JsonHttpResponse) super.get(url, inputStream, headers)
    }

    @Override
    JsonHttpResponse get(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        return (JsonHttpResponse) super.get(url, inputStream, streamHandler, headers)
    }

    @Override
    JsonHttpResponse post(String url, Object body, Collection<RequestHeader> headers) {
        String jsonBody = MAPPER.writeValueAsString(body)
        return (JsonHttpResponse) super.post(url, jsonBody, ContentType.APPLICATION_JSON, headers)
    }

    @Override
    JsonHttpResponse post(String url, Object body, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        String jsonBody = MAPPER.writeValueAsString(body)
        return (JsonHttpResponse) super.post(url, jsonBody, ContentType.APPLICATION_JSON, streamHandler, headers)
    }

    @Override
    JsonHttpResponse post(String url, InputStream inputStream, Collection<RequestHeader> headers) {
        return (JsonHttpResponse) super.post(url, inputStream, headers)
    }

    @Override
    JsonHttpResponse post(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        return (JsonHttpResponse) super.post(url, inputStream, streamHandler, headers)
    }

    @Override
    JsonHttpResponse put(String url, Object body, Collection<RequestHeader> headers) {
        String jsonBody = MAPPER.writeValueAsString(body)
        return (JsonHttpResponse) super.put(url, jsonBody, ContentType.APPLICATION_JSON, headers)
    }

    @Override
    JsonHttpResponse put(String url, Object body, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        String jsonBody = MAPPER.writeValueAsString(body)
        return (JsonHttpResponse) super.put(url, jsonBody, ContentType.APPLICATION_JSON, streamHandler, headers)
    }

    @Override
    JsonHttpResponse put(String url, InputStream inputStream, Collection<RequestHeader> headers) {
        return (JsonHttpResponse) super.put(url, inputStream, headers)
    }

    @Override
    JsonHttpResponse put(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        return (JsonHttpResponse) super.put(url, inputStream, streamHandler, headers)
    }

    @Override
    JsonHttpResponse delete(String url, Object body, Collection<RequestHeader> headers) {
        String jsonBody = MAPPER.writeValueAsString(body)
        return (JsonHttpResponse) super.delete(url, jsonBody, ContentType.APPLICATION_JSON, headers)
    }

    @Override
    JsonHttpResponse delete(String url, Object body, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        String jsonBody = MAPPER.writeValueAsString(body)
        return (JsonHttpResponse) super.delete(url, jsonBody, ContentType.APPLICATION_JSON, streamHandler, headers)
    }

    @Override
    JsonHttpResponse delete(String url, InputStream inputStream, Collection<RequestHeader> headers) {
        return (JsonHttpResponse) super.delete(url, inputStream, headers)
    }

    @Override
    JsonHttpResponse delete(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        return (JsonHttpResponse) super.delete(url, inputStream, streamHandler, headers)
    }
}
