package duncanscott.org.groovy.http.xml

import duncanscott.org.groovy.http.client.HttpClientBase
import duncanscott.org.groovy.http.client.RequestHeader
import duncanscott.org.groovy.javautil.http.InputStreamHandler
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpHeaders

class XmlHttpClient extends HttpClientBase<XmlHttpResponse> {

    XmlHttpClient() {
        super(XmlHttpResponse)
        setHeaders()
    }

    XmlHttpClient(String username, String password) {
        this()
        setAuthorizationHeader(username, password)
    }

    private void setHeaders() {
        defaultHeaders.add(new RequestHeader(HttpHeaders.ACCEPT, 'application/xml'))
        defaultHeaders.add(new RequestHeader(HttpHeaders.CONTENT_TYPE, 'application/xml'))
    }

    // --- Body-aware methods --- //

    @Override
    XmlHttpResponse get(String url, Object body, Collection<RequestHeader> headers) {
        return (XmlHttpResponse) super.get(url, body.toString(), ContentType.APPLICATION_XML, headers)
    }

    @Override
    XmlHttpResponse get(String url, Object body, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        return (XmlHttpResponse) super.get(url, body.toString(), ContentType.APPLICATION_XML, streamHandler, headers)
    }

    @Override
    XmlHttpResponse get(String url, InputStream inputStream, Collection<RequestHeader> headers) {
        return (XmlHttpResponse) super.get(url, inputStream, headers)
    }

    @Override
    XmlHttpResponse get(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        return (XmlHttpResponse) super.get(url, inputStream, streamHandler, headers)
    }

    @Override
    XmlHttpResponse post(String url, Object body, Collection<RequestHeader> headers) {
        return (XmlHttpResponse) super.post(url, body.toString(), ContentType.APPLICATION_XML, headers)
    }

    @Override
    XmlHttpResponse post(String url, Object body, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        return (XmlHttpResponse) super.post(url, body.toString(), ContentType.APPLICATION_XML, streamHandler, headers)
    }

    @Override
    XmlHttpResponse post(String url, InputStream inputStream, Collection<RequestHeader> headers) {
        return (XmlHttpResponse) super.post(url, inputStream, headers)
    }

    @Override
    XmlHttpResponse post(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        return (XmlHttpResponse) super.post(url, inputStream, streamHandler, headers)
    }

    @Override
    XmlHttpResponse put(String url, Object body, Collection<RequestHeader> headers) {
        return (XmlHttpResponse) super.put(url, body.toString(), ContentType.APPLICATION_XML, headers)
    }

    @Override
    XmlHttpResponse put(String url, Object body, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        return (XmlHttpResponse) super.put(url, body.toString(), ContentType.APPLICATION_XML, streamHandler, headers)
    }

    @Override
    XmlHttpResponse put(String url, InputStream inputStream, Collection<RequestHeader> headers) {
        return (XmlHttpResponse) super.put(url, inputStream, headers)
    }

    @Override
    XmlHttpResponse put(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        return (XmlHttpResponse) super.put(url, inputStream, streamHandler, headers)
    }

    @Override
    XmlHttpResponse delete(String url, Object body, Collection<RequestHeader> headers) {
        return (XmlHttpResponse) super.delete(url, body.toString(), ContentType.APPLICATION_XML, headers)
    }

    @Override
    XmlHttpResponse delete(String url, Object body, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        return (XmlHttpResponse) super.delete(url, body.toString(), ContentType.APPLICATION_XML, streamHandler, headers)
    }

    @Override
    XmlHttpResponse delete(String url, InputStream inputStream, Collection<RequestHeader> headers) {
        return (XmlHttpResponse) super.delete(url, inputStream, headers)
    }

    @Override
    XmlHttpResponse delete(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers) {
        return (XmlHttpResponse) super.delete(url, inputStream, streamHandler, headers)
    }
}
