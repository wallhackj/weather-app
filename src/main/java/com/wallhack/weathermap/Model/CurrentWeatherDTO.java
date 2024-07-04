package com.wallhack.weathermap.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wallhack.weathermap.utils.UnixTimestampDeserializer;

import java.time.LocalDateTime;
import java.util.List;

public class CurrentWeatherDTO {
    private LocationDTO location;
    private List<WeatherDTO> weather;
    private MainDTO main;
    private WindDTO wind;
    private CloudsDTO clouds;
    private Sys sys;

    private static class Sys{
        @JsonProperty("sunrise")
        @JsonDeserialize(using = UnixTimestampDeserializer.class)
        private LocalDateTime sunriseTime;

        @JsonProperty("sunset")
        @JsonDeserialize(using = UnixTimestampDeserializer.class)
        private LocalDateTime sunsetTime;
    }
}
