package com.wallhack.weathermap.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import java.io.IOException;

public class ExtraUtils {

    public static boolean isEmpty(String str, String str2) {
        return str == null || str2 == null || str.isEmpty() || str2.isEmpty();
    }

    public static void prepareResponse(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

    public static void handleResponseError(HttpServletResponse resp, Logger logger, ObjectMapper mapper, Exception e, int statusCode, String errorMessage) {
        logger.error("Error processing request", e);
        resp.setStatus(statusCode);
        try {
            mapper.writeValue(resp.getWriter(), new ErrorResponse(statusCode, errorMessage));
        } catch (IOException ex) {
            logger.error("Error writing error response", ex);
        }
    }
}
