package com.wallhack.weathermap.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Optional;

import static com.wallhack.weathermap.utils.API_KEY_ACCESS.apiKey;

public class SearchService {
    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public Optional<JsonNode> searchWeatherByCity(String city){
//      https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
        return getJsonNode(getRequest("weather?" + "q=" + city + "&appid="));
    }

    public Optional<JsonNode> searchWeatherByCoordinates(double lat, double lon){
//      https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
        return getJsonNode(getRequest("weather?" + "lat=" + lat + "&lon=" + lon + "&appid="));
    }

    public Optional<JsonNode> hourlyForecast4DaysByCoordinates(double lat, double lon){
//      https://pro.openweathermap.org/data/2.5/forecast/hourly?lat={lat}&lon={lon}&appid={API key}
        return getJsonNode(getRequest("forecast/hourly?" + "lat=" + lat + "&lon=" + lon + "&appid="));
    }

    public Optional<JsonNode> forecast5Day3HoursByCity(String city){
//      api.openweathermap.org/data/2.5/forecast?q={city name}&appid={API key}
        return getJsonNode(getRequest("forecast?q=" + city + "&appid="));
    }

    private Request getRequest(String url){
        String apiURL = "https://api.openweathermap.org/data/2.5/";
        return new Request
                .Builder()
                .url(apiURL + url + apiKey)
                .build();
    }

    private Optional<JsonNode> getJsonNode(Request request) {
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            if (response.body() != null) {
                return Optional.of(mapper.readTree(response.body().string()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


}
