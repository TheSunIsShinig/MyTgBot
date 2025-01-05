package org.example.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class ParameterID {

    private static final String BASE_URL = "https://developers.ria.com/auto/categories/1/";
    private static final String API = "UxvwGMUaYLpv1l528BOgtcw76XHEAP84ufycn33f";

    @Getter @Setter private String name;
    @Getter @Setter private String value;

    public String getBrandID(String brandName){
        List<ParameterID> brandID = getJsonFromUrl(BASE_URL+"marks?api_key="+API);

        for (ParameterID brand : brandID) {
            if (brand.getName().equalsIgnoreCase(brandName)) {
                return brand.getValue();
            }
        }
        return null;
    }

    public String getModelID(String brandName, String modelName){
        String brandID = getBrandID(brandName);
        List<ParameterID> modelID = getJsonFromUrl(BASE_URL+"marks/"+brandID+"/models?api_key="+API);

        for (ParameterID model : modelID) {
            if (model.getName().equalsIgnoreCase(modelName)) {
                return model.getValue();
            }
        }
        return null;
    }

    private List<ParameterID> getJsonFromUrl(String apiUrl){

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("API Error: " + response.statusCode() + " - " + response.body());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), new TypeReference<List<ParameterID>>() {});
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
