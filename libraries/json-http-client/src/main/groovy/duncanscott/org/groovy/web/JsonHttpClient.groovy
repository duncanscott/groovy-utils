package duncanscott.org.groovy.web

import org.apache.http.HttpEntity
import org.apache.http.HttpHeaders
import org.apache.http.client.methods.*
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

class JsonHttpClient {

    static JsonHttpResponse get(String url) {
        HttpGet httpGet = new HttpGet(url)
        submitRequest(httpGet)
    }

    static JsonHttpResponse post(String url) {
        HttpPost httpPost = new HttpPost(url)
        submitRequest(httpPost)
    }

    static JsonHttpResponse post(String url, String json) {
        HttpPost httpPost = new HttpPost(url)
        setJson(httpPost,json)
        submitRequest(httpPost)
    }

    static JsonHttpResponse put(String url) {
        HttpPut httpPut = new HttpPut(url)
        submitRequest(httpPut)
    }

    static JsonHttpResponse put(String url, String json) {
        HttpPut httpPut = new HttpPut(url)
        setJson(httpPut, json)
        submitRequest(httpPut)
    }

    static JsonHttpResponse delete(String url) {
        HttpDelete httpDelete = new HttpDelete(url)
        submitRequest(httpDelete)
    }

    static JsonHttpResponse submitRequest(HttpRequestBase request) {
        addHeaders(request)
        JsonHttpResponse jsonResponse = new JsonHttpResponse()
        jsonResponse.uri = request.URI

        CloseableHttpResponse response = httpClient.execute(request)
        jsonResponse.statusLine = response.statusLine
        try {
            HttpEntity responseEntity = response.getEntity()
            // do something useful with the response body
            String responseText = EntityUtils.toString(responseEntity)
            jsonResponse.json = (JSONObject) new JSONParser().parse(responseText)
            // and ensure it is fully consumed
            EntityUtils.consume(responseEntity)
        } finally {
            response.close();
        }
        jsonResponse
    }

    private static CloseableHttpClient getHttpClient() {
        HttpClientBuilder.create().build()
    }
    
    private static void setJson(HttpEntityEnclosingRequestBase request, String json) {
        StringEntity entity = new StringEntity(json)
        request.setEntity(entity)
    }

    private static void addHeaders(HttpRequestBase request) {
        request.setHeader(HttpHeaders.ACCEPT, 'application/json')
        request.setHeader(HttpHeaders.CONTENT_TYPE, 'application/json')
    }

}
