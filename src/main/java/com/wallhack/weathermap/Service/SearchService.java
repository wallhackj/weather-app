package com.wallhack.weathermap.Service;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.wallhack.weathermap.Model.CurrentWeatherDTO;
import com.wallhack.weathermap.Model.ForecastDTO;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static com.wallhack.weathermap.utils.API_KEY_ACCESS.apiKey;

public class SearchService {
    private final JsonMapper jsonMapper = new JsonMapper();

    public Optional<CurrentWeatherDTO> searchWeatherByCity(String city) throws URISyntaxException, IOException, InterruptedException {
//     https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
       var endpoint = "weather?" + "q=" + city + "&appid=";
       return sendRequest(endpoint, CurrentWeatherDTO.class);
    }

    public Optional<CurrentWeatherDTO> searchWeatherByCityAndRegion(String city, String region) throws URISyntaxException, IOException, InterruptedException {
//       https://api.openweathermap.org/data/2.5/weather?q=Odessa,us&appid={API key}
         var endpoint = "weather?" + "q=" + city + "," + region + "&appid=";
         return sendRequest(endpoint, CurrentWeatherDTO.class);
    }

    public Optional<CurrentWeatherDTO> searchWeatherByCoordinates(double lat, double lon) throws URISyntaxException, IOException, InterruptedException {
//      https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
        var endpoint = "weather?" + "lat=" + lat + "&lon=" + lon + "&appid=";
        return sendRequest(endpoint, CurrentWeatherDTO.class);
    }

    public Optional<ForecastDTO> forecast5Day3HoursByCity(String city) throws URISyntaxException, IOException, InterruptedException {
//      api.openweathermap.org/data/2.5/forecast?q={city name}&appid={API key}
        var endpoint = "forecast?q=" + city + "&appid=";
        return sendRequest(endpoint, ForecastDTO.class);
    }

    public Optional<ForecastDTO> forecast5Day3HoursByCityAndRegion(String city, String region) throws URISyntaxException, IOException, InterruptedException {
//      api.openweathermap.org/data/2.5/forecast?q=München,DE&appid={API key}
        var endpoint = "forecast?q=" + city + "," + region + "&appid=";
        return sendRequest(endpoint, ForecastDTO.class);
    }

    private <T> Optional<T> sendRequest(String endpoint, Class<T> entityClass) throws URISyntaxException, IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(new URI("https://api.openweathermap.org/data/2.5/" + endpoint + apiKey))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return Optional.of(jsonMapper.readValue(response.body(), entityClass));
    }
}
