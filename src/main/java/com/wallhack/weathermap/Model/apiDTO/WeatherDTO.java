package com.wallhack.weathermap.Model.apiDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherDTO (
        @JsonProperty("id")
        int id,

        @JsonProperty("main")
        String main,

        @JsonProperty("description")
        String description
)
{}
