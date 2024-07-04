package com.wallhack.weathermap.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MainDTO(
        @JsonProperty("temp")
        double temp,
        @JsonProperty("feels_like")
        double feels_like,
        @JsonProperty("pressure")
        int pressure,
        @JsonProperty("humidity")
        int humidity
){}
