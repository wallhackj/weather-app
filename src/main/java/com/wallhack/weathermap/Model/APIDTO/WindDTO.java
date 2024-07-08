package com.wallhack.weathermap.Model.APIDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WindDTO(
        @JsonProperty("speed")
        double speed,

        @JsonProperty("deg")
        int deg,

        @JsonProperty("gust")
        double gust
) {}
