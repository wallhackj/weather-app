package com.wallhack.weathermap.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallhack.weathermap.Service.SearchService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(value = "/forecast")
public class ForecastServlet extends HttpServlet {
    private final SearchService searchService = new SearchService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processGetForecastServlet(req, resp);
    }

    private void processGetForecastServlet(HttpServletRequest req, HttpServletResponse resp) {

    }

}
