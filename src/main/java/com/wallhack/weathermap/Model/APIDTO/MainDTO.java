package com.wallhack.weathermap.Model.APIDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MainDTO(
        @JsonProperty("temp")
        double temp,

        @JsonProperty("feels_like")
        double feels_like,

        @JsonProperty("pressure")
        int pressure,

        @JsonProperty("humidity")
        int humidity
) {}
