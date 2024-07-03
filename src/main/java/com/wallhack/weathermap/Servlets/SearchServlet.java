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

    private void processGetSearchServlet(HttpServletRequest req, HttpServletResponse resp) throws IOException, NumberFormatException {
        prepareResponse(resp);

        Optional<JsonNode> jsonResponse;

//      /search?location=Chisinau or /search?location=3.14%2C%203.14
        var location = req.getParameter("location");
        var userID = req.getParameter("userId");

        if (isEmpty(location,userID)){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(400, "Param 'location and user' is required"));
        }

        if (Character.isDigit(location.charAt(0))) {
            String[] coordinates = location.split(",");

            double latitude = Double.parseDouble(coordinates[0]);
            double longitude = Double.parseDouble(coordinates[1]);

            Optional<LocationsPOJO> locationByCoordinates = locationsService.findLocationByCoordinates(latitude, longitude);
            if (locationByCoordinates.isPresent()) {
                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(resp.getWriter(), locationByCoordinates.get());
            }
            jsonResponse = searchService.searchByCoordinates(latitude, longitude);
        }else {
            Optional<LocationsPOJO> locationByName = locationsService.findLocationByName(location);

            if (locationByName.isPresent()) {
                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(resp.getWriter(), locationByName.get());
            }
            jsonResponse = searchService.searchByCity(location);
        }

        if (jsonResponse.isPresent()) {
        var result = jsonResponse.get();
        Optional<UsersPOJO> user = usersDAO.findById(Integer.parseInt(userID));

            if (user.isPresent()){
                resp.setStatus(HttpServletResponse.SC_OK);

                JsonNode coordNode = result.get("coord");
                String name = result.get("name") != null ? result.get("name").asText() : "";
                Double lat = coordNode != null ? coordNode.get("lat") != null ? Double.parseDouble(coordNode.get("lat").asText()) : null : null;
                Double lon = coordNode != null ? coordNode.get("lon") != null ? Double.parseDouble(coordNode.get("lon").asText()) : null : null;

                // Verifică dacă toate datele necesare sunt prezente înainte de adăugarea în locationsService
                if (lat != null && lon != null && !name.isEmpty()) {
                    mapper.writeValue(resp.getWriter(), result);
                    locationsService.addLocation(new LocationsPOJO(lat, lon, name, user.get()));
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    mapper.writeValue(resp.getWriter(), new ErrorResponse(500, "Missing or invalid data in response"));
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                mapper.writeValue(resp.getWriter(), new ErrorResponse(404, "User not found"));
            }
        }else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(500, "Internal Server Error"));
        }
    }
}
