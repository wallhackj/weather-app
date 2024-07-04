package com.wallhack.weathermap.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallhack.weathermap.Model.APIForecastDTO;
import com.wallhack.weathermap.Service.SearchService;
import com.wallhack.weathermap.utils.ErrorResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static com.wallhack.weathermap.utils.ExtraUtils.*;

@WebServlet(value = "/forecast")
public class ForecastServlet extends HttpServlet {
    private final SearchService searchService = new SearchService();
    private final ObjectMapper mapper = new ObjectMapper();

    public ForecastServlet() {
        this.mapper.findAndRegisterModules();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        responseWithMethod(this::processGetForecastServlet, req, resp);
    }

    private void processGetForecastServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException, URISyntaxException, InterruptedException {
        prepareResponse(resp);

        var location = req.getParameter("location");
        Optional<APIForecastDTO> fiveDaysForecast;

        if (isEmpty(location, "1")){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(400, "Param 'location and user' is required"));
            return;
        }

        if (location.contains(",")){
            String[] parts = location.split(",", 2);
            String city = parts[0].trim();
            String region = parts[1].trim();

            fiveDaysForecast = searchService.forecast5Day3HoursByCityAndRegion(city, region);
        }else fiveDaysForecast = searchService.forecast5Day3HoursByCity(location);

        if (fiveDaysForecast.isPresent() && fiveDaysForecast.get().hourlyForecast() != null){
            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), fiveDaysForecast.get());
        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(404, "Not Found Forecast by City"));
        }

    }

}
