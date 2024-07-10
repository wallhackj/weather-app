package com.wallhack.weathermap.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallhack.weathermap.Servlets.ServletProcessor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ExtraUtils {
    private static final Logger LOGGER = LogManager.getLogger(ExtraUtils.class);
    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static boolean isEmpty(String str, String str2) {
        return str == null || str2 == null || str.isEmpty() || str2.isEmpty();
    }

    public static void prepareResponse(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

    public static void politicCORS(HttpServletResponse resp){
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    public static void doReq(ServletProcessor processor, HttpServletRequest req, HttpServletResponse resp) {
        politicCORS(resp);

        try {
            processor.process(req, resp);
        }catch (Exception e){
            handleResponseError(resp, e, 500);
        }
    }

    public static void handleResponseError(HttpServletResponse resp, Exception e, int statusCode) {
        LOGGER.error("Error processing request", e);
        resp.setStatus(statusCode);
        try {
            MAPPER.writeValue(resp.getWriter(), new ErrorResponse(statusCode,"Internal Server Error"));
        } catch (IOException ex) {
            LOGGER.error("Error writing error response", ex);
        }
    }
}
