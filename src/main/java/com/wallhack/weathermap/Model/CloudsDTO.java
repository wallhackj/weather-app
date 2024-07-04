package com.wallhack.weathermap.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CloudsDTO(
        @JsonProperty("all")
        int clouds
) {}
