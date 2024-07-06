package com.wallhack.weathermap.Model.APIDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CloudsDTO(
        @JsonProperty("all")
        int clouds
) {}
