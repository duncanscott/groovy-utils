package duncanscott.org.groovy.utils.http.client;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.Method;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

public class HttpClientImpl<K extends HttpClientResponse> implements HttpClient<K>, Closeable {

    private final Constructor<K> responseConstructor;
    private final CloseableHttpClient internalClient;

    private BeforeRequestInterceptor beforeRequestInterceptor;
    private AfterRequestInterceptor<K> afterRequestInterceptor;

    protected final List<RequestHeader> defaultHeaders = new ArrayList<>();

    public HttpClientImpl(Class<K> responseClass) {
        try {
            this.responseConstructor = responseClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Response class must have a public no-arg constructor", e);
        }
        this.internalClient = HttpClients.createDefault();
    }

    @Override
    public HttpClientImpl<K> setAuthorizationHeader(String username, String password) {
        String basic = username + ":" + password;
        String basicHash = "Basic " + Base64.getEncoder().encodeToString(basic.getBytes(StandardCharsets.UTF_8));
        defaultHeaders.add(new RequestHeader(HttpHeaders.AUTHORIZATION, basicHash));
        return this;
    }

    @Override
    public void close() throws IOException {
        internalClient.close();
    }

    // ---- Builders without URL ----
    @Override
    public HttpClientRequest<K> get()   { return new HttpClientRequest<>(this, Method.GET); }
    @Override
    public HttpClientRequest<K> head()  { return new HttpClientRequest<>(this, Method.HEAD); }
    @Override
    public HttpClientRequest<K> post()  { return new HttpClientRequest<>(this, Method.POST); }
    @Override
    public HttpClientRequest<K> put()   { return new HttpClientRequest<>(this, Method.PUT); }
    @Override
    public HttpClientRequest<K> delete(){ return new HttpClientRequest<>(this, Method.DELETE); }
    @Override
    public HttpClientRequest<K> trace() { return new HttpClientRequest<>(this, Method.TRACE); }
    @Override
    public HttpClientRequest<K> options(){ return new HttpClientRequest<>(this, Method.OPTIONS); }
    @Override
    public HttpClientRequest<K> patch() { return new HttpClientRequest<>(this, Method.PATCH); }

    // ---- Builders with String URL ----
    @Override
    public HttpClientRequest<K> get(String url)    { return setUrlOrThrow(new HttpClientRequest<>(this, Method.GET), url); }
    @Override
    public HttpClientRequest<K> head(String url)   { return setUrlOrThrow(new HttpClientRequest<>(this, Method.HEAD), url); }
    @Override
    public HttpClientRequest<K> post(String url)   { return setUrlOrThrow(new HttpClientRequest<>(this, Method.POST), url); }
    @Override
    public HttpClientRequest<K> put(String url)    { return setUrlOrThrow(new HttpClientRequest<>(this, Method.PUT), url); }
    @Override
    public HttpClientRequest<K> delete(String url) { return setUrlOrThrow(new HttpClientRequest<>(this, Method.DELETE), url); }
    @Override
    public HttpClientRequest<K> trace(String url)  { return setUrlOrThrow(new HttpClientRequest<>(this, Method.TRACE), url); }
    @Override
    public HttpClientRequest<K> options(String url){ return setUrlOrThrow(new HttpClientRequest<>(this, Method.OPTIONS), url); }
    @Override
    public HttpClientRequest<K> patch(String url)  { return setUrlOrThrow(new HttpClientRequest<>(this, Method.PATCH), url); }

    private HttpClientRequest<K> setUrlOrThrow(HttpClientRequest<K> req, String url) {
        try {
            return req.setUrl(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL: " + url, e);
        }
    }

    // ---- Builders with URI ----
    @Override
    public HttpClientRequest<K> get(URI url)    { return new HttpClientRequest<>(this, Method.GET).setUrl(url); }
    @Override
    public HttpClientRequest<K> head(URI url)   { return new HttpClientRequest<>(this, Method.HEAD).setUrl(url); }
    @Override
    public HttpClientRequest<K> post(URI url)   { return new HttpClientRequest<>(this, Method.POST).setUrl(url); }
    @Override
    public HttpClientRequest<K> put(URI url)    { return new HttpClientRequest<>(this, Method.PUT).setUrl(url); }
    @Override
    public HttpClientRequest<K> delete(URI url) { return new HttpClientRequest<>(this, Method.DELETE).setUrl(url); }
    @Override
    public HttpClientRequest<K> trace(URI url)  { return new HttpClientRequest<>(this, Method.TRACE).setUrl(url); }
    @Override
    public HttpClientRequest<K> options(URI url){ return new HttpClientRequest<>(this, Method.OPTIONS).setUrl(url); }
    @Override
    public HttpClientRequest<K> patch(URI url)  { return new HttpClientRequest<>(this, Method.PATCH).setUrl(url); }

    @Override
    public HttpClientImpl<K> setInterceptor(BeforeRequestInterceptor interceptor) {
        this.beforeRequestInterceptor = interceptor;   // fixed: assign the parameter
        return this;
    }

    @Override
    public HttpClientImpl<K> setInterceptor(AfterRequestInterceptor<K> interceptor) {
        this.afterRequestInterceptor = interceptor;    // fixed: assign the parameter
        return this;
    }

    @Override
    public ContentType getDefaultRequestContentType() {
        return ContentType.TEXT_PLAIN;
    }

    @Override
    public K execute(HttpClientRequest<K> request) throws URISyntaxException {
        ClassicRequestBuilder requestBuilder = request.getRequestBuilder();
        URI uri = requestBuilder.getUri();
        if (uri == null) {
            throw new InvalidUrlException("URL cannot be null");
        }
        if (uri.getScheme() == null) {
            throw new InvalidUrlException(uri.toString(), "URL scheme cannot be null");
        }
        if (uri.getHost() == null) {
            throw new InvalidUrlException(uri.toString(), "URL host cannot be null");
        }

        if (request.getBody() != null) {
            ContentType ct = (request.getContentType() != null) ? request.getContentType() : getDefaultRequestContentType();
            requestBuilder.setEntity(new StringEntity(request.getBody(), ct));
        }

        addHeaders(requestBuilder, request.getHeaders());

        ClassicHttpRequest httpRequest = requestBuilder.build();

        K httpResponse;
        try {
            httpResponse = responseConstructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to instantiate response type", e);
        }

        TextResponse textResponse = new TextResponse();
        textResponse.requestUri = httpRequest.getUri().toString();
        textResponse.requestMethod = httpRequest.getMethod();

        if (beforeRequestInterceptor != null) {
            beforeRequestInterceptor.beforeRequest(httpRequest);
        }

        try {
            internalClient.execute(httpRequest, (ClassicHttpResponse response) -> {
                textResponse.statusCode = response.getCode();
                textResponse.reasonPhrase = response.getReasonPhrase();
                textResponse.locale = response.getLocale();

                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try {
                        InputStreamHandler handler = request.getStreamHandler();
                        if (handler != null) {
                            try (InputStream in = entity.getContent()) {
                                handler.call(in);
                            }
                        } else {
                            textResponse.text  = EntityUtils.toString(entity);
                        }
                    } finally {
                        EntityUtils.consume(entity);
                    }
                }
                return null;
            });
        } catch (IOException e) {
            throw new RuntimeException("HTTP execution failed", e);
        }

        // Assuming your response type exposes a setter:
        httpResponse.setTextResponse(textResponse);

        if (afterRequestInterceptor != null) {
            afterRequestInterceptor.afterRequest(httpRequest, httpResponse);
        }
        return httpResponse;
    }

    private void addHeaders(ClassicRequestBuilder builder, Collection<RequestHeader> headers) {
        // Apply defaults first
        for (RequestHeader header : defaultHeaders) {
            if (header != null) {
                builder.addHeader(header.getName(), header.getValue());
            }
        }

        // Apply per-request headers, potentially overriding defaults
        if (headers != null) {
            for (RequestHeader header : headers) {
                if (header != null) {
                    builder.setHeader(header.getName(), header.getValue()); // setHeader overrides
                }
            }
        }
    }
}
