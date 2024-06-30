package com.wallhack.weathermap.DAO;

import com.wallhack.weathermap.Model.LocationsPOJO;

import java.util.List;
import java.util.Optional;

public interface ICRUDLocations {
    List<LocationsPOJO> getLocationByUserId(long userId);
    Optional<LocationsPOJO> getLocationByName(String name);
    Optional<LocationsPOJO> getLocationByCoordinates(double latitude, double longitude);
}
