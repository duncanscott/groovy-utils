package duncanscott.org.groovy.javautil.http;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.io.InputStream;

public class RequestProcessor {

    public static TextResponse submitRequest(ClassicHttpRequest httpRequest) throws IOException {
        TextResponse textResponse = new TextResponse();
        textResponse.requestUri = httpRequest.getRequestUri();
        textResponse.requestMethod = httpRequest.getMethod();
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            httpclient.execute(httpRequest, response -> {
                textResponse.statusCode = response.getCode();
                textResponse.reasonPhrase = response.getReasonPhrase();
                textResponse.locale = response.getLocale();

                final HttpEntity entity = response.getEntity();
                try {
                    textResponse.text = EntityUtils.toString(entity);
                } finally {
                    EntityUtils.consume(entity);
                }
                return null;
            });
        }
        return textResponse;
    }

    public static TextResponse submitRequest(ClassicHttpRequest httpRequest, InputStreamHandler inputStreamHandler) throws IOException {
        TextResponse textResponse = new TextResponse();
        textResponse.requestUri = httpRequest.getRequestUri();
        textResponse.requestMethod = httpRequest.getMethod();
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            httpclient.execute(httpRequest, response -> {
                textResponse.statusCode = response.getCode();
                textResponse.reasonPhrase = response.getReasonPhrase();
                textResponse.locale = response.getLocale();
                final HttpEntity entity = response.getEntity();
                try {
                    try (InputStream inputStream = entity.getContent()) {
                        inputStreamHandler.call(inputStream);
                    }
                } finally {
                    EntityUtils.consume(entity);
                }
                return null;
            });
        }
        return textResponse;
    }

}
