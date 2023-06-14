package com.example.demo.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class papagoServiceImpl implements papagoService {
    @Value("${naver.papago.client-id}")
    String clientId;
    @Value("${naver.papago.client-secret}")
    String clientSecret;
    String endpoint = "https://openapi.naver.com/v1/papago/n2mt";

    @Override
    public String translate(String enText) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            String uriBase = endpoint;
            URIBuilder builder = new URIBuilder(uriBase);

            // Request parameters. All of them are optional.
            builder.setParameter("source", "en");
            builder.setParameter("target", "ko");
            builder.setParameter("text", enText);

            // Prepare the URI for the REST API method.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");
            request.setHeader("X-Naver-Client-Id", clientId);
            request.setHeader("X-Naver-Client-Secret", clientSecret);

            // Call the REST API method and get the response entity.
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonString);
                JSONObject message = (JSONObject)json.get("message");
                JSONObject result = (JSONObject)message.get("result");
                String translatedText = result.get("translatedText").toString();

                System.out.println("<papago>");
                System.out.println(enText + " -> " + translatedText);

                return translatedText;
            }
        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
        }
        return "";

    }
}
