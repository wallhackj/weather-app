package com.wallhack.weathermap.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wallhack.weathermap.utils.UnixTimestampDeserializer;

import java.time.LocalDateTime;
import java.util.List;

public class ForecastDTO {
    @JsonProperty("list")
    private List<HourlyForecast> hourlyForecast;

    private static class HourlyForecast{
        @JsonProperty("dt")
        @JsonDeserialize(using = UnixTimestampDeserializer.class)
        private LocalDateTime date;
        private MainDTO main;
        private WeatherDTO weather;
        private CloudsDTO clouds;
        private WindDTO wind;
    }
}
