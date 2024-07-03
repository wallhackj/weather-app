package com.wallhack.weathermap.Servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallhack.weathermap.DAO.UsersDAO;
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
import java.util.Optional;

import static com.wallhack.weathermap.utils.ExtraUtils.*;

@WebServlet(value = "/search")
public class SearchServlet extends HttpServlet {
    private final SearchService searchService = new SearchService();
    private final ObjectMapper mapper = new ObjectMapper();
    private final LocationsService locationsService = new LocationsService();
    private final UsersDAO usersDAO = new UsersDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        headerSetter(resp);

        try {
            processGetSearchServlet(req, resp);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        headerSetter(resp);

        try {
            processPostSearchServlet(req, resp);
        }catch (Exception e){
            e.printStackTrace();
        }
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
        }

        Optional<LocationsPOJO> currentLocation = locationsService.findLocationByName(name);

        if (currentLocation.isEmpty()) {
            Optional<UsersPOJO> user = usersDAO.findById(Integer.parseInt(userID));

            if (user.isPresent()) {
                double lat = Double.parseDouble(latitude);
                double lon = Double.parseDouble(longitude);

                var newLocation = new LocationsPOJO(lat, lon, name, user.get());
                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(resp.getWriter(), newLocation);
                locationsService.addLocation(newLocation);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                mapper.writeValue(resp.getWriter(), new ErrorResponse(404, "User not found"));
            }
        }else {
            resp.setStatus(200);
            mapper.writeValue(resp.getWriter(),currentLocation.get());
        }
    }

    private void processGetSearchServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException, NumberFormatException {
        prepareResponse(resp);

        Optional<JsonNode> jsonResponse;

//      /search?location=Chisinau or /search?location=3.14%2C%203.14
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

            jsonResponse = searchService.searchByCoordinates(latitude, longitude);
        }else {
            jsonResponse = searchService.searchByCity(location);
        }

        if (jsonResponse.isPresent()) {
        resp.setStatus(HttpServletResponse.SC_OK);
        mapper.writeValue(resp.getWriter(), jsonResponse.get());

        }else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(500, "Internal Server Error"));
        }
    }
}
