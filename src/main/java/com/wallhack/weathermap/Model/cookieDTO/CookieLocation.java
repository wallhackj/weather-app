package com.wallhack.weathermap.Model.cookieDTO;

public record CookieLocation(
        long id,
        String name,
        double lat,
        double lon
) {}
