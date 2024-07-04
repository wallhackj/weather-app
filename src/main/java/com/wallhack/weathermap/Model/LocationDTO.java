package com.wallhack.weathermap.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LocationDTO(
        @JsonProperty("name")
        String name,
        @JsonProperty("country")
        String country,
        @JsonProperty("lon")
        double lon,
        @JsonProperty("lat")
        double lat
) {}
