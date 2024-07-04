package com.wallhack.weathermap.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wallhack.weathermap.utils.MillisOrLocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record APIForecastDTO (
        @JsonProperty("list")
        List<HourlyForecast> hourlyForecast
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record HourlyForecast(
            @JsonProperty("dt")
            @JsonDeserialize(using = MillisOrLocalDateTimeDeserializer.class)
            LocalDateTime date,

            @JsonProperty("main")
            MainDTO main,

            @JsonProperty("weather")
            List<WeatherDTO> weather
    ) {}
}
