package com.wallhack.weathermap.Servlets.WeatherServlets;

import com.wallhack.weathermap.Model.apiDTO.APIWeatherDTO;
import com.wallhack.weathermap.Model.cookieDTO.CookieLocation;
import com.wallhack.weathermap.Model.LocationsPOJO;
import com.wallhack.weathermap.Model.UsersPOJO;
import com.wallhack.weathermap.Service.LocationsService;
import com.wallhack.weathermap.Service.SearchService;
import com.wallhack.weathermap.Service.SessionsService;
import com.wallhack.weathermap.Service.UsersService;
import com.wallhack.weathermap.utils.ErrorResponse;
import jakarta.persistence.NoResultException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static com.wallhack.weathermap.utils.ExtraUtils.*;

@WebServlet(value = "/search_bar")
public class SearchWeatherServlet extends HttpServlet {
    private final SearchService searchService = new SearchService();
    private final LocationsService locationsService = new LocationsService();
    private final UsersService usersService = new UsersService();
    private final SessionsService sessionsService =  new SessionsService();
    private CookieLocation localCookieLocation;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
       doReq(this::processGetSearchServlet, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doReq(this::processPostSearchServlet, req, resp);
    }

    private void processPostSearchServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        prepareResponse(resp);

        var name = req.getParameter("name");
        var latitude = req.getParameter("lat");
        var longitude = req.getParameter("lon");

        if (isEmpty("1", name) && isEmpty(latitude, longitude)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            MAPPER.writeValue(resp.getWriter(), new ErrorResponse(400, "Bad Request"));
            return;
        }

        try {
            var lat = Double.parseDouble(latitude);
            var lon = Double.parseDouble(longitude);

            if (searchService.searchWeatherByCoordinates(lat, lon).isPresent()){
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                MAPPER.writeValue(resp.getWriter(), new ErrorResponse(400, "Location already exists"));
            }
            localCookieLocation = new CookieLocation(1, name, lat, lon);
            sessionsService.processCookies(this::addLocationToDB, resp, req, sessionsService);
        } catch (NoResultException e) {
            resp.setStatus(403);
            MAPPER.writeValue(resp.getWriter(), new ErrorResponse(403, "Session expired"));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            MAPPER.writeValue(resp.getWriter(), new ErrorResponse(400, "Wrong parameters of latitude or longitude"));
        }catch (IOException | URISyntaxException | InterruptedException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            MAPPER.writeValue(resp.getWriter(), new ErrorResponse(500, "Internal Server Error"));
        }
    }

    private void addLocationToDB(HttpServletResponse resp, CookieLocation cookieLocation) throws IOException {
        Optional<UsersPOJO> user = usersService.findUserById(cookieLocation.id());
        Optional<LocationsPOJO> currentLocation = locationsService.findLocationByName(localCookieLocation.name());

        if (user.isPresent() && currentLocation.isPresent()) {
            if (currentLocation.get().getUser().getId() != cookieLocation.id()) {
                var newLocation = new LocationsPOJO(localCookieLocation.lat(), localCookieLocation.lon(), localCookieLocation.name(), user.get());
                resp.setStatus(HttpServletResponse.SC_OK);
                MAPPER.writeValue(resp.getWriter(), newLocation);
                locationsService.addLocation(newLocation);

                } else {
                resp.setStatus(HttpServletResponse.SC_OK);
                MAPPER.writeValue(resp.getWriter(), currentLocation.get());
            }
        }
    }

    private void processGetSearchServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException, NumberFormatException, URISyntaxException, InterruptedException {
        prepareResponse(resp);

//      /search?location=Chisinau or /search?location=3.14%2C%203.14
//      /search?location=Chisinau,MD

        var location = req.getParameter("location");

        if (isEmpty(location,"1")){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            MAPPER.writeValue(resp.getWriter(), new ErrorResponse(400, "Param 'location' is required"));
            return;
        }

        Optional<APIWeatherDTO> currentWeatherDTO;

        if (Character.isDigit(location.charAt(0))) {
            String[] coordinates = location.split(",");

            double latitude = Double.parseDouble(coordinates[0]);
            double longitude = Double.parseDouble(coordinates[1]);

            currentWeatherDTO = searchService.searchWeatherByCoordinates(latitude, longitude);
        }else {
            if (location.contains(",")){
                String[] parts = location.split(",", 2);
                String city = parts[0].trim();
                String region = parts[1].trim();

                currentWeatherDTO = searchService.searchWeatherByCityAndRegion(city, region);

            }else currentWeatherDTO = searchService.searchWeatherByCity(location);
        }

        if (currentWeatherDTO.isPresent()) {
            resp.setStatus(HttpServletResponse.SC_OK);
            MAPPER.writeValue(resp.getWriter(), currentWeatherDTO.get());

        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            MAPPER.writeValue(resp.getWriter(), new ErrorResponse(404, "Internal Server Error"));
        }
    }
}
