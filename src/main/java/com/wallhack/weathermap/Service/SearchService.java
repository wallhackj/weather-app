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
    //      https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
    private final String apiURL = "https://api.openweathermap.org/data/2.5/weather?q=";
    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public Optional<JsonNode> searchByCity(String city){
//      https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
        Request request = new Request
                .Builder()
                .url(apiURL + city + "&appid=" + apiKey)
                .build();

        return getJsonNode(request);
    }

    public Optional<JsonNode> searchByCoordinates(double lat, double lon){
//      https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
        Request request = new Request
                .Builder().url(apiURL + "lat=" + lat + "&lon=" + lon + "&appid=" + apiKey)
                .build();

        return getJsonNode(request);
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
