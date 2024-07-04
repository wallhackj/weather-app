package com.wallhack.weathermap.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallhack.weathermap.Model.APIWeatherDTO;
import com.wallhack.weathermap.Model.CookieLocation;
import com.wallhack.weathermap.Model.LocationsPOJO;
import com.wallhack.weathermap.Service.LocationsService;
import com.wallhack.weathermap.Service.SearchService;
import com.wallhack.weathermap.Service.SessionsService;
import com.wallhack.weathermap.utils.ErrorResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.wallhack.weathermap.utils.ExtraUtils.*;

@WebServlet(value = "/index")
public class MainPageServlet extends HttpServlet {
    private final ObjectMapper mapper = new ObjectMapper();
    private final LocationsService locationsService = new LocationsService();
    private final SearchService searchService = new SearchService();
    private final SessionsService sessionsService =  new SessionsService();

    public MainPageServlet() {
        this.sessionsService.deleteExpiredSessions();
        this.mapper.findAndRegisterModules();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        responseWithMethod(this::processGetMainPageServlet, req, resp);
    }

    private void processGetMainPageServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException, URISyntaxException, InterruptedException {
        prepareResponse(resp);
        sessionsService.processCookies(this::allLocationsRender, resp, req, mapper, sessionsService);
    }

    private void allLocationsRender(HttpServletResponse resp, CookieLocation cookieLocation) throws IOException, URISyntaxException, InterruptedException {
        List<LocationsPOJO> allLocationsOfUser = locationsService.getAllLocationByUserId(cookieLocation.id());

        if (allLocationsOfUser.isEmpty()) {
            resp.setStatus(400);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(400, "User dont have any location"));
        } else {
            List<APIWeatherDTO> allWeatherInfo = new ArrayList<>();

            for (LocationsPOJO location : allLocationsOfUser) {
                Optional<APIWeatherDTO> locationsPOJO = searchService
                        .searchWeatherByCoordinates(location.getLatitude(), location.getLongitude());
                locationsPOJO.ifPresent(allWeatherInfo::add);
            }

            resp.setStatus(200);
            mapper.writeValue(resp.getWriter(), allWeatherInfo);

        }
    }
}
