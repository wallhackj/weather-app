package com.wallhack.weathermap.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherDTO (
        @JsonProperty("id")
        int id,
        @JsonProperty("main")
        String main,
        @JsonProperty("description")
        String description
)
{}
