package com.example.demo.service;

import com.example.demo.dto.urlImageDTO;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URI;

@Service
public class azureCVServiceImpl implements azureCVService {
    @Autowired
    base64Service base64Service;
    @Value("${azure.computer-vision.key}")
    String key;
    @Value("${azure.computer-vision.endpoint}")
    String endpoint;

    @Override
    public String captionImageV4(urlImageDTO urlImage) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            String uriBase = endpoint + "computervision/imageanalysis:analyze";
            URIBuilder builder = new URIBuilder(uriBase);

            // Request parameters. All of them are optional.
            builder.setParameter("api-version", "2023-02-01-preview");
            builder.setParameter("features", "tags,caption");

            // Prepare the URI for the REST API method.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", key);

            // Request body.
            JSONObject requestBodyObject = new JSONObject();
            requestBodyObject.put("url", urlImage.getUrl());
            String requestBodyString = requestBodyObject.toString();
            StringEntity requestEntity = new StringEntity(requestBodyString);
            request.setEntity(requestEntity);

            // Call the REST API method and get the response entity.
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonString);
                JSONObject captionResult = (JSONObject) json.get("captionResult");
                JSONObject tagsResult = (JSONObject)json.get("tagsResult");
                JSONArray values = (JSONArray)tagsResult.get("values");
                JSONObject firstValue = (JSONObject)values.get(0);
                String text = "";
                if(firstValue.get("name").equals("text"))
                    text = "text";
                else
                    text = captionResult.get("text").toString();

                System.out.println("<Image Caption>");
                System.out.println("text: " + text);

                return text;
            }
        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
        }
        return "";
    }
    @Override
    public String ocrImageV4(urlImageDTO urlImage) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            String uriBase = endpoint + "computervision/imageanalysis:analyze";
            URIBuilder builder = new URIBuilder(uriBase);

            // Request parameters. All of them are optional.
            builder.setParameter("api-version", "2023-02-01-preview");
            builder.setParameter("features", "read");

            // Prepare the URI for the REST API method.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", key);

            // Request body.
            StringEntity requestEntity =
                    new StringEntity("{\"url\":\"" + urlImage.getUrl() + "\"}");
            request.setEntity(requestEntity);

            // Call the REST API method and get the response entity.
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonString);
                JSONObject readResult = (JSONObject)json.get("readResult");
                String text = readResult.get("content").toString();

                System.out.println("<OCR>");
                System.out.println("text:\n" + text);

                return text;
            }
        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
        }
        return "";
    }
    @Override
    public String captionImageV4() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        File file = new File("temp.bmp");

        try {
            String uriBase = endpoint + "computervision/imageanalysis:analyze";
            URIBuilder builder = new URIBuilder(uriBase);

            // Request parameters. All of them are optional.
            builder.setParameter("api-version", "2023-02-01-preview");
            builder.setParameter("features", "tags,caption");

            // Prepare the URI for the REST API method.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString());
            request.setHeader("Ocp-Apim-Subscription-Key", key);

            // Request body.
            FileEntity requestEntity = new FileEntity(file, ContentType.APPLICATION_OCTET_STREAM);
            request.setEntity(requestEntity);

            // Call the REST API method and get the response entity.
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonString);
                JSONObject captionResult = (JSONObject)json.get("captionResult");
                JSONObject tagsResult = (JSONObject)json.get("tagsResult");
                JSONArray values = (JSONArray)tagsResult.get("values");
                JSONObject firstValue = (JSONObject)values.get(0);
                String text = "";
                if(firstValue.get("name").equals("text"))
                    text = "text";
                else
                    text = captionResult.get("text").toString();

                System.out.println("<Image Caption>");
                System.out.println("text: " + text);

                return text;
            }
        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
        }
        return "";
    }
    @Override
    public String ocrImageV4() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        File file = new File("temp.bmp");

        try {
            String uriBase = endpoint + "computervision/imageanalysis:analyze";
            URIBuilder builder = new URIBuilder(uriBase);

            // Request parameters. All of them are optional.
            builder.setParameter("api-version", "2023-02-01-preview");
            builder.setParameter("features", "read");

            // Prepare the URI for the REST API method.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.toString());
            request.setHeader("Ocp-Apim-Subscription-Key", key);

            // Request body.
            FileEntity requestEntity = new FileEntity(file, ContentType.APPLICATION_OCTET_STREAM);
            request.setEntity(requestEntity);

            // Call the REST API method and get the response entity.
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonString);
                JSONObject readResult = (JSONObject)json.get("readResult");
                String text = readResult.get("content").toString();

                System.out.println("<OCR>");
                System.out.println("text:\n" + text);

                return text;
            }
        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
        }
        return "";
    }
}
