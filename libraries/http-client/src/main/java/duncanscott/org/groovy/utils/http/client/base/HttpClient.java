package duncanscott.org.groovy.utils.http.client.base;

import org.apache.hc.core5.http.ContentType;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public interface HttpClient<K extends HttpClientResponse> extends Closeable {

    HttpClient<K> setAuthorizationHeader(String username, String password);

    HttpClient<K> setInterceptor(BeforeRequestInterceptor interceptor);

    HttpClient<K> setInterceptor(AfterRequestInterceptor<K> interceptor);

    ContentType getDefaultRequestContentType();

    K execute(HttpClientRequest<K> request) throws URISyntaxException;

    @Override
    void close() throws IOException;

    HttpClientRequest<K> get();

    HttpClientRequest<K> head();

    HttpClientRequest<K> post();

    HttpClientRequest<K> put();

    HttpClientRequest<K> delete();

    HttpClientRequest<K> trace();

    HttpClientRequest<K> options();

    HttpClientRequest<K> patch();

    HttpClientRequest<K> get(String url);

    HttpClientRequest<K> head(String url);

    HttpClientRequest<K> post(String url);

    HttpClientRequest<K> put(String url);

    HttpClientRequest<K> delete(String url);

    HttpClientRequest<K> trace(String url);

    HttpClientRequest<K> options(String url);

    HttpClientRequest<K> patch(String url);

    HttpClientRequest<K> get(URI url);

    HttpClientRequest<K> head(URI url);

    HttpClientRequest<K> post(URI url);

    HttpClientRequest<K> put(URI url);

    HttpClientRequest<K> delete(URI url);

    HttpClientRequest<K> trace(URI url);

    HttpClientRequest<K> options(URI url);

    HttpClientRequest<K> patch(URI url);
}
