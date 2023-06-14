package com.example.demo.service;

import com.example.demo.dto.textDTO;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class azureLanguageServiceImpl implements azureLanguageService {
    @Value("${azure.language.key}")
    String key;
    @Value("${azure.language.endpoint}")
    String endpoint;

    @Override
    public String summarize(String text) {
        try {
            String uriBase = endpoint + "language/analyze-text/jobs";
            URIBuilder builder = new URIBuilder(uriBase);

            // Request parameters. All of them are optional.
            builder.setParameter("api-version", "2022-10-01-preview");

            // Prepare the URI for the REST API method.
            URI uri = builder.build();

            // Create a RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Set request headers
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            requestHeaders.add("Ocp-Apim-Subscription-Key", key);

            // Set request body if needed
            JSONObject requestBody = new JSONObject();
            requestBody.put("displayName", "Extractive Summarize Task");

            JSONObject analysisInput = new JSONObject();
            JSONArray documents = new JSONArray();
            JSONObject document = new JSONObject();
            document.put("id", 1);
            document.put("language", "ko");
            document.put("text", text);
            documents.put(document);
            analysisInput.put("documents", documents);
            requestBody.put("analysisInput", analysisInput);

            JSONArray tasks = new JSONArray();
            JSONObject task = new JSONObject();
            task.put("kind", "ExtractiveSummarization");
            task.put("taskName", "Extractive Summarization Task 1");
            JSONObject parameters = new JSONObject();
            parameters.put("sentenceCount", "1");
            task.put("parameters", parameters);
            tasks.put(task);
            requestBody.put("tasks", tasks);

            // Create the HttpEntity with headers and body
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), requestHeaders);

            // Make the HTTP request
            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

            // Process the response
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                // Handle response body
                System.out.println("<azureLanguage success>");
                String responseBody = responseEntity.getBody();
                System.out.println(responseBody);
                return "";
            } else {
                // Handle error response
                System.out.println("<azureLanguage failed> :");
                String responseBody = responseEntity.getBody();
                System.out.println(responseBody);
            }
        } catch(Exception e) {
            // Display Error
            System.out.println(e.getMessage());
        }
        return "";
    }
    @Override
    public String keyPhraseExtract(List<textDTO> textList) {
        try {
            String uriBase = endpoint + "language/:analyze-text";
            URIBuilder builder = new URIBuilder(uriBase);

            // Request parameters. All of them are optional.
            builder.setParameter("api-version", "2022-10-01-preview");

            // Prepare the URI for the REST API method.
            URI uri = builder.build();

            // Create a RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Set request headers
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            requestHeaders.add("Ocp-Apim-Subscription-Key", key);

            // Set request body if needed
            JSONObject requestBody = new JSONObject();
            requestBody.put("kind", "KeyPhraseExtraction");

            JSONObject parameters = new JSONObject();
            parameters.put("modelVersion", "latest");
            requestBody.put("parameters", parameters);

            JSONObject analysisInput = new JSONObject();
            JSONArray documents = new JSONArray();
            for(int i = 0; i < textList.size(); i++) {
                JSONObject document = new JSONObject();
                document.put("id", i + 1);
                document.put("language", textList.get(i).getLanguage());
                document.put("text", textList.get(i).getText());
                documents.put(document);
            }
            analysisInput.put("documents", documents);
            requestBody.put("analysisInput", analysisInput);
            System.out.println(requestBody.toString());

            // Create the HttpEntity with headers and body
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), requestHeaders);

            // Make the HTTP request
            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

            // Process the response
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                // Handle response body
                String responseBody = responseEntity.getBody();
                System.out.println(responseBody);
                return "";
            } else {
                // Handle error response
                String responseBody = responseEntity.getBody();
                System.out.println(responseBody);
            }
        } catch(Exception e) {
            // Display Error
            System.out.println(e.getMessage());
        }
        return "";
    }
    @Override
    public List<textDTO> languageDetect(String[] texts) {
        List<textDTO> textList = null;
        try {
            String uriBase = endpoint + "language/:analyze-text";
            URIBuilder builder = new URIBuilder(uriBase);

            // Request parameters. All of them are optional.
            builder.setParameter("api-version", "2022-10-01-preview");

            // Prepare the URI for the REST API method.
            URI uri = builder.build();

            // Create a RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Set request headers
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            requestHeaders.add("Ocp-Apim-Subscription-Key", key);

            // Set request body if needed
            JSONObject requestBody = new JSONObject();
            requestBody.put("kind", "LanguageDetection");

            JSONObject parameters = new JSONObject();
            parameters.put("modelVersion", "latest");
            requestBody.put("parameters", parameters);

            JSONObject analysisInput = new JSONObject();
            JSONArray documents = new JSONArray();
            for (int i = 0; i < texts.length; i++) {
                JSONObject document = new JSONObject();
                document.put("id", i + 1);
                document.put("text", texts[i]);
                documents.put(document);
            }
            analysisInput.put("documents", documents);
            requestBody.put("analysisInput", analysisInput);
            System.out.println(requestBody.toString());

            // Create the HttpEntity with headers and body
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), requestHeaders);

            // Make the HTTP request
            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

            // Process the response
            textList = new ArrayList<textDTO>();
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                // Handle response body
                String responseBody = responseEntity.getBody();
                System.out.println(responseBody);
                JSONObject response = new JSONObject(responseBody);
                JSONObject results = response.getJSONObject("results");
                documents = results.getJSONArray("documents");
                for(int i = 0; i < documents.length(); i++) {
                    JSONObject document = documents.getJSONObject(i);
                    Integer id = Integer.parseInt(document.getString("id"));
                    String language = document.getJSONObject("detectedLanguage").getString("name");
                    textDTO text = new textDTO();
                    text.setText(texts[id - 1]);
                    text.setLanguage(language);
                    if(text.getLanguage().equals("Korean")) text.setLanguage("ko");
                    if(text.getLanguage().equals("English")) text.setLanguage("en");
                    textList.add(text);
                }
            } else {
                // Handle error response
                String responseBody = responseEntity.getBody();
                System.out.println(responseBody);
            }
        } catch (Exception e) {
            // Display Error
            System.out.println(e.getMessage());
        }
        return textList;
    }
}
