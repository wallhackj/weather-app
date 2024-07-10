package com.wallhack.weathermap.Model.apiDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CloudsDTO(
        @JsonProperty("all")
        int clouds
) {}
