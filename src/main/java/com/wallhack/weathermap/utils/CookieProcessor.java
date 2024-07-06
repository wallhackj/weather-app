package com.wallhack.weathermap.utils;

import com.wallhack.weathermap.Model.CookieLocation;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URISyntaxException;

public interface CookieProcessor {
    void proceed(HttpServletResponse resp, CookieLocation locations) throws IOException, URISyntaxException, InterruptedException;
}
