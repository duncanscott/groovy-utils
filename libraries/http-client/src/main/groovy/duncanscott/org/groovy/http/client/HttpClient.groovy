package duncanscott.org.groovy.http.client

import org.apache.hc.core5.http.ContentType

interface HttpClient<K extends HttpClientResponse> extends Closeable {

    HttpClient<K> setAuthorizationHeader(String username, String password)

    HttpClient<K> setInterceptor(BeforeRequestInterceptor interceptor)

    HttpClient<K> setInterceptor(AfterRequestInterceptor interceptor)

    ContentType getDefaultRequestContentType()

    K execute(HttpClientRequest request)

    void close()

   HttpClientRequest get()
   HttpClientRequest head()
   HttpClientRequest post()
   HttpClientRequest put()
   HttpClientRequest delete()
   HttpClientRequest trace()
   HttpClientRequest options()
   HttpClientRequest patch()

   HttpClientRequest get(String url)
   HttpClientRequest head(String url)
   HttpClientRequest post(String url)
   HttpClientRequest put(String url)
   HttpClientRequest delete(String url)
   HttpClientRequest trace(String url)
   HttpClientRequest options(String url)
   HttpClientRequest patch(String url)

   HttpClientRequest get(URI url)
   HttpClientRequest head(URI url)
   HttpClientRequest post(URI url)
   HttpClientRequest put(URI url)
   HttpClientRequest delete(URI url)
   HttpClientRequest trace(URI url)
   HttpClientRequest options(URI url)
   HttpClientRequest patch(URI url)
}