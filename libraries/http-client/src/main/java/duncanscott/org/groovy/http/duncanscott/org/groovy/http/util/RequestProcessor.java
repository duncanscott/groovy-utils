package duncanscott.org.groovy.http.duncanscott.org.groovy.http.util;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

public class RequestProcessor {

    public static TextResponse submitRequest(ClassicHttpRequest httpRequest) throws IOException {
        TextResponse textResponse = new TextResponse();
        textResponse.requestUri = httpRequest.getRequestUri();
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            httpclient.execute(httpRequest, response -> {
                textResponse.statusCode = response.getCode();
                textResponse.reasonPhrase = response.getReasonPhrase();
                textResponse.locale = response.getLocale();

                final HttpEntity entity1 = response.getEntity();
                textResponse.text = EntityUtils.toString(entity1);
                EntityUtils.consume(entity1);
                return null;
            });
        }
        return textResponse;
    }


}
