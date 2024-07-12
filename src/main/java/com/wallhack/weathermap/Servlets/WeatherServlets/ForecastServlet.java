package com.wallhack.weathermap.Servlets.WeatherServlets;

import com.wallhack.weathermap.Model.apiDTO.APIForecastDTO;
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

@WebServlet(value = "/forecasts")
public class ForecastServlet extends HttpServlet {
    private final SearchService searchService = new SearchService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doReq(this::processGetForecastServlet, req, resp);
    }

    private void processGetForecastServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException, URISyntaxException, InterruptedException {
        prepareResponse(resp);

        var location = req.getParameter("location");
        if (isEmpty(location, "1")){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            MAPPER.writeValue(resp.getWriter(), new ErrorResponse(400, "Param 'location and user' is required"));
            return;
        }

        Optional<APIForecastDTO> fiveDaysForecast;
        if (location.contains(",")){
            String[] parts = location.split(",", 2);
            String city = parts[0].trim();
            String region = parts[1].trim();
            fiveDaysForecast = searchService.forecast5Day3HoursByCityAndRegion(city, region);
        }else fiveDaysForecast = searchService.forecast5Day3HoursByCity(location);

        if (fiveDaysForecast.isPresent() && fiveDaysForecast.get().hourlyForecast() != null){
            resp.setStatus(HttpServletResponse.SC_OK);
            MAPPER.writeValue(resp.getWriter(), fiveDaysForecast.get());
        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            MAPPER.writeValue(resp.getWriter(), new ErrorResponse(404, "Not Found Forecast by City"));
        }

    }

}
