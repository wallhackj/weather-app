package com.wallhack.weathermap.Servlets.WeatherServlets;


import com.wallhack.weathermap.Model.apiDTO.APIWeatherDTO;
import com.wallhack.weathermap.Model.cookieDTO.CookieLocation;
import com.wallhack.weathermap.Model.LocationsPOJO;
import com.wallhack.weathermap.Service.LocationsService;
import com.wallhack.weathermap.Service.SearchService;
import com.wallhack.weathermap.Service.SessionsService;
import com.wallhack.weathermap.utils.ErrorResponse;
import jakarta.persistence.NoResultException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.wallhack.weathermap.utils.ExtraUtils.*;

@WebServlet(value = "/main_page")
public class MainPageServlet extends HttpServlet {
    private final LocationsService locationsService = new LocationsService();
    private final SearchService searchService = new SearchService();
    private final SessionsService sessionsService = new SessionsService();

    public MainPageServlet() {
        MAPPER.findAndRegisterModules();
        sessionsService.deleteExpiredSessions();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doReq(this::processGetMainPageServlet, req, resp);
    }

    private void processGetMainPageServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException, URISyntaxException, InterruptedException {
        prepareResponse(resp);
        try {
            sessionsService.processCookies(this::allLocationsRender, resp, req, sessionsService);
        } catch (NoResultException e) {
            resp.setStatus(403);
            MAPPER.writeValue(resp.getWriter(), new ErrorResponse(403, "Session expired"));
        }
    }

    private void allLocationsRender(HttpServletResponse resp, CookieLocation cookieLocation) throws IOException, URISyntaxException, InterruptedException {
        List<LocationsPOJO> allLocationsOfUser = locationsService.getAllLocationByUserId(cookieLocation.id());

        if (allLocationsOfUser.isEmpty()) {
            resp.setStatus(400);
            MAPPER.writeValue(resp.getWriter(), new ErrorResponse(400, "User dont have any location"));
        } else {
            List<APIWeatherDTO> allWeatherInfo = new ArrayList<>();
            addAllLocationToArray(allLocationsOfUser, allWeatherInfo);
            resp.setStatus(200);
            MAPPER.writeValue(resp.getWriter(), allWeatherInfo);
        }
    }

    private void addAllLocationToArray(List<LocationsPOJO> allLocationsOfUser, List<APIWeatherDTO> allWeatherInfo) throws URISyntaxException, IOException, InterruptedException {
        for (LocationsPOJO location : allLocationsOfUser) {
            Optional<APIWeatherDTO> locationsPOJO = searchService
                    .searchWeatherByCoordinates(location.getLatitude(), location.getLongitude());
            locationsPOJO.ifPresent(allWeatherInfo::add);
        }
    }
}
