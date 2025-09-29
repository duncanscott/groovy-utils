package duncanscott.org.groovy.http.xml

import duncanscott.org.groovy.http.client.HttpClient
import duncanscott.org.groovy.http.util.RequestHeader
import duncanscott.org.groovy.javautil.http.InputStreamHandler
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpHeaders

class XmlHttpClient extends HttpClient<XmlHttpResponse> {

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

    XmlHttpResponse get(String url, String xmlBody, Collection<RequestHeader> headers = null) {
        return (XmlHttpResponse) super.get(url, xmlBody, ContentType.APPLICATION_XML, headers)
    }

    XmlHttpResponse get(String url, String xmlBody, InputStreamHandler streamHandler, Collection<RequestHeader> headers = null) {
        return (XmlHttpResponse) super.get(url, xmlBody, ContentType.APPLICATION_XML, streamHandler, headers)
    }

    XmlHttpResponse get(String url, InputStream inputStream, Collection<RequestHeader> headers = null) {
        return (XmlHttpResponse) super.get(url, inputStream, headers)
    }

    XmlHttpResponse get(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers = null) {
        return (XmlHttpResponse) super.get(url, inputStream, streamHandler, headers)
    }

    XmlHttpResponse post(String url, String xmlBody, Collection<RequestHeader> headers = null) {
        return (XmlHttpResponse) super.post(url, xmlBody, ContentType.APPLICATION_XML, headers)
    }

    XmlHttpResponse post(String url, String xmlBody, InputStreamHandler streamHandler, Collection<RequestHeader> headers = null) {
        return (XmlHttpResponse) super.post(url, xmlBody, ContentType.APPLICATION_XML, streamHandler, headers)
    }

    XmlHttpResponse post(String url, InputStream inputStream, Collection<RequestHeader> headers = null) {
        return (XmlHttpResponse) super.post(url, inputStream, headers)
    }

    XmlHttpResponse post(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers = null) {
        return (XmlHttpResponse) super.post(url, inputStream, streamHandler, headers)
    }

    XmlHttpResponse put(String url, String xmlBody, Collection<RequestHeader> headers = null) {
        return (XmlHttpResponse) super.put(url, xmlBody, ContentType.APPLICATION_XML, headers)
    }

    XmlHttpResponse put(String url, String xmlBody, InputStreamHandler streamHandler, Collection<RequestHeader> headers = null) {
        return (XmlHttpResponse) super.put(url, xmlBody, ContentType.APPLICATION_XML, streamHandler, headers)
    }

    XmlHttpResponse put(String url, InputStream inputStream, Collection<RequestHeader> headers = null) {
        return (XmlHttpResponse) super.put(url, inputStream, headers)
    }

    XmlHttpResponse put(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers = null) {
        return (XmlHttpResponse) super.put(url, inputStream, streamHandler, headers)
    }

    XmlHttpResponse delete(String url, String xmlBody, Collection<RequestHeader> headers = null) {
        return (XmlHttpResponse) super.delete(url, xmlBody, ContentType.APPLICATION_XML, headers)
    }

    XmlHttpResponse delete(String url, String xmlBody, InputStreamHandler streamHandler, Collection<RequestHeader> headers = null) {
        return (XmlHttpResponse) super.delete(url, xmlBody, ContentType.APPLICATION_XML, streamHandler, headers)
    }

    XmlHttpResponse delete(String url, InputStream inputStream, Collection<RequestHeader> headers = null) {
        return (XmlHttpResponse) super.delete(url, inputStream, headers)
    }

    XmlHttpResponse delete(String url, InputStream inputStream, InputStreamHandler streamHandler, Collection<RequestHeader> headers = null) {
        return (XmlHttpResponse) super.delete(url, inputStream, streamHandler, headers)
    }
}
