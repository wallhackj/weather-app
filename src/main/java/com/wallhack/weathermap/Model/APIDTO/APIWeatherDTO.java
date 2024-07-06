package com.wallhack.weathermap.Model.APIDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wallhack.weathermap.utils.MillisOrLocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record APIWeatherDTO (
        @JsonProperty("name")
        String name,

        @JsonProperty("coord")
        APILocationDTO location,

        @JsonProperty("weather")
        List<WeatherDTO> weather,

        @JsonProperty("main")
        MainDTO main,

        @JsonProperty("wind")
        WindDTO wind,

        @JsonProperty("clouds")
        CloudsDTO clouds,

        @JsonProperty("dt")
        @JsonDeserialize(using = MillisOrLocalDateTimeDeserializer.class)
        LocalDateTime date,

        @JsonProperty("sys")
        Sys sys
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Sys(
            @JsonProperty("sunrise")
            @JsonDeserialize(using = MillisOrLocalDateTimeDeserializer.class)
            LocalDateTime sunriseTime,

            @JsonProperty("sunset")
            @JsonDeserialize(using = MillisOrLocalDateTimeDeserializer.class)
            LocalDateTime sunsetTime
        ){}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record APILocationDTO(
            @JsonProperty("lon")
            double lon,

            @JsonProperty("lat")
            double lat
    ) {}
}
