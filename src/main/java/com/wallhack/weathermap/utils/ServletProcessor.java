package com.wallhack.weathermap.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface ServletProcessor {
    void process(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
