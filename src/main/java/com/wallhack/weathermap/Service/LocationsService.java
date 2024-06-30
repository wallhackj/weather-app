package com.wallhack.weathermap.Service;

import com.wallhack.weathermap.DAO.LocationsDAO;
import com.wallhack.weathermap.Model.LocationsPOJO;
import java.util.List;
import java.util.Optional;

public class LocationsService {
    private final LocationsDAO locationsDAO = new LocationsDAO();

    public void addLocation(LocationsPOJO location) {
        locationsDAO.save(location);
    }

    public List<LocationsPOJO> getLocations() {
        return locationsDAO.findAll();
    }

    public List<LocationsPOJO> getLocationByUserId(long id) {
        return locationsDAO.getLocationByUserId(id);
    }

    public Optional<LocationsPOJO> getLocationById(long id) {
        return locationsDAO.findById(id);
    }

    public Optional<LocationsPOJO> findLocationByName(String name){
        return locationsDAO.getLocationByName(name);
    }

    public Optional<LocationsPOJO> findLocationByCoordinates(double lat, double lng) {
        return locationsDAO.getLocationByCoordinates(lat, lng);
    }

    public void updateLocation(LocationsPOJO location) {
        locationsDAO.save(location);
    }

    public void deleteLocation(long id) {
        locationsDAO.delete(id);
    }
}
