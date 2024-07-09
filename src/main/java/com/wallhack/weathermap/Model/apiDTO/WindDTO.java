package com.wallhack.weathermap.Model.apiDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WindDTO(
        @JsonProperty("speed")
        double speed,

        @JsonProperty("deg")
        int deg,

        @JsonProperty("gust")
        double gust
) {}
