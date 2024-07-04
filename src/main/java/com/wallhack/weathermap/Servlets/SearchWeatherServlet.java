package com.wallhack.weathermap.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallhack.weathermap.DAO.UsersDAO;
import com.wallhack.weathermap.Model.APIWeatherDTO;
import com.wallhack.weathermap.Model.LocationsPOJO;
import com.wallhack.weathermap.Model.UsersPOJO;
import com.wallhack.weathermap.Service.LocationsService;
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
import static com.wallhack.weathermap.utils.ExtraUtils.responseWithMethod;

@WebServlet(value = "/search")
public class SearchWeatherServlet extends HttpServlet {
    private final SearchService searchService = new SearchService();
    private final ObjectMapper mapper = new ObjectMapper();
    private final LocationsService locationsService = new LocationsService();
    private final UsersDAO usersDAO = new UsersDAO();

    public SearchWeatherServlet() {
        this.mapper.findAndRegisterModules();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        responseWithMethod(this::processGetSearchServlet, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        responseWithMethod(this::processPostSearchServlet, req, resp);
    }

    private void processPostSearchServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException, NumberFormatException {
        prepareResponse(resp);

        var userID = req.getParameter("userId");
        var name = req.getParameter("name");
        var latitude = req.getParameter("lat");
        var longitude = req.getParameter("lon");

        if (isEmpty(userID, name) && isEmpty(latitude, longitude)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(400, "Bad Request"));
            return;
        }

        var id = Long.parseLong(userID);
        Optional<UsersPOJO> user = usersDAO.findById(id);
        Optional<LocationsPOJO> currentLocation = locationsService.findLocationByName(name);

        if (user.isPresent() && currentLocation.isPresent()) {
            if (currentLocation.get().getUser().getId() != id) {
                double lat = Double.parseDouble(latitude);
                double lon = Double.parseDouble(longitude);

                var newLocation = new LocationsPOJO(lat, lon, name, user.get());
                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(resp.getWriter(), newLocation);
                locationsService.addLocation(newLocation);
            }else {
                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(resp.getWriter(),currentLocation.get());
            }
        }else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                mapper.writeValue(resp.getWriter(), new ErrorResponse(404, "User not found"));
            }

    }

    private void processGetSearchServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException, NumberFormatException, URISyntaxException, InterruptedException {
        prepareResponse(resp);

        Optional<APIWeatherDTO> currentWeatherDTO;

//      /search?location=Chisinau or /search?location=3.14%2C%203.14
//      /search?location=Chisinau,MD

        var location = req.getParameter("location");

        if (isEmpty(location,"1")){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(400, "Param 'location and user' is required"));
            return;
        }

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
            mapper.writeValue(resp.getWriter(), currentWeatherDTO.get());

        }else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(500, "Internal Server Error"));
        }
    }
}
