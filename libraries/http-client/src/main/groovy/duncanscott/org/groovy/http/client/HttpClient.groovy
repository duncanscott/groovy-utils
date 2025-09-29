package duncanscott.org.groovy.http.client

interface HttpClient<K extends Response> extends Closeable {

    HttpClient<K> setAuthorizationHeader(String username, String password)
    HttpClient<K> setInterceptor(BeforeRequestInterceptor interceptor)
    HttpClient<K> setInterceptor(AfterRequestInterceptor interceptor)

    K execute(Request request)

    void close()

}