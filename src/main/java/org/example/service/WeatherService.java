package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.constant.TextField;
import org.example.constant.UserState;
import org.example.helper.MessageSender;
import org.example.model.WeatherDetails;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.example.constant.TextField.SET_CITY_TEXT;
import static org.example.constant.UserState.AWAITING_CITY;

@Component
public class WeatherService {

    private static final String API_KEY = "7576fae14aec928b1deac8feda0bb47d";

    private final MessageSender messageSender;
    private final Map<Long, UserState> chatStates;

    public WeatherService(@Lazy SilentSender sender, DBContext db){
        messageSender = new MessageSender(sender);
        chatStates = db.getMap(TextField.CHAT_STATES);
    }

    private String buildURL(Message message){
        StringBuilder builder = new StringBuilder();
        return builder.append("https://api.openweathermap.org/data/2.5/weather?q=")
                .append(message.getText())
                .append("&appid=")
                .append(API_KEY)
                .append("&units=metric")
                .toString();

    }

    private WeatherDetails getJsonFromUrl(String url){
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("API Error: " + response.statusCode() + " - " + response.body());
            }

            return parseWeatherJson(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private WeatherDetails  parseWeatherJson(String json){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            WeatherDetails weatherDetails = new WeatherDetails();

            JsonNode rootNode = objectMapper.readTree(json);
            weatherDetails.setCityName(rootNode.path("name").asText());
            weatherDetails.setDescription(rootNode.path("weather").get(0).path("description").asText());
            weatherDetails.setTemp(rootNode.path("main").path("temp").asText());
            weatherDetails.setFeelsLike(rootNode.path("main").path("feels_like").asText());
            weatherDetails.setHumidity(rootNode.path("main").path("humidity").asText());
            weatherDetails.setPressure(rootNode.path("main").path("pressure").asText());
            weatherDetails.setVisibility(rootNode.path("visibility").asText());
            weatherDetails.setWindSpeed(rootNode.path("wind").path("speed").asText());

            return weatherDetails;
        } catch (IOException e) {
            System.out.println("Error while parsing weather data: " + e.getMessage());
            return null;
        }
    }

    public void getWeatherDetails(long chatId, Message message){
        String url = buildURL(message);

        try {
            WeatherDetails weatherDetails = getJsonFromUrl(url);
            messageSender.sendMessage(chatId, weatherDetails.toString());
            chatStates.remove(chatId);
        } catch (Exception e) {
            messageSender.sendMessage(chatId, "Sorry, something went wrong while fetching the weather data.");
            chatStates.remove(chatId);
        }
    }

    public void setCityMessage(long chatId) {
        messageSender.sendMessage(chatId, SET_CITY_TEXT);
        chatStates.put(chatId, AWAITING_CITY);
    }
}
