package com.wallhack.weathermap.DAO.Location;

import com.wallhack.weathermap.DAO.UsersDAO;
import com.wallhack.weathermap.Model.LocationsPOJO;
import com.wallhack.weathermap.Model.UsersPOJO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LocationsDAOTest {
    LocationsDAO locationsDAO = new LocationsDAO();
    UsersDAO usersDAO = new UsersDAO();
    Random random = new Random();

    @Test
    @Order(1)
    public void insertTest() {
        String name  = "Atlantida";
        double lat = random.nextDouble();
        double lng = random.nextDouble();
        UsersPOJO user = usersDAO.getUserByLogin("admin").orElse(null);
        assertNotNull(user);

        LocationsPOJO location = new LocationsPOJO(lat,lng,name, user);
        locationsDAO.save(location);

        LocationsPOJO testLocation = locationsDAO.getLocationByName(name).orElse(null);
        assertNotNull(testLocation);
        assertEquals(name, testLocation.getName());
    }

    @Test
    @Order(2)
    public void updateTest() {
        String name  = "Atlantida";
        LocationsPOJO location = locationsDAO.getLocationByName(name).orElse(null);
        assertNotNull(location);
        assertEquals(name, location.getName());

        var lon = 2222;
        var lat = 3333;
        location.setLongitude(lon);
        location.setLatitude(lat);
        locationsDAO.update(location);

        LocationsPOJO testLocation = locationsDAO.getLocationByName(name).orElse(null);
        assertNotNull(testLocation);
        assertEquals(name, testLocation.getName());
        assertEquals(lon, testLocation.getLongitude());
        assertEquals(lat, testLocation.getLatitude());
    }

    @Test
    @Order(3)
    public void findByCoordinates() {
        LocationsPOJO location = locationsDAO.getLocationByCoordinates(47.0056, 28.8575).orElse(null);
        assertNotNull(location);
        assertEquals(location.getName(), "Chișinău");
    }

    @Test
    @Order(4)
    public void findByIdTest(){
        LocationsPOJO location = locationsDAO.findById(1).orElse(null);
        assertNotNull(location);
        assertEquals("London", location.getName());
    }

    @Test
    @Order(5)
    public void findAllTest(){
        List<LocationsPOJO> list = locationsDAO.findAll();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    @Order(6)
    public void findByNameTest(){
        List<LocationsPOJO> locations = locationsDAO.getLocationByUserId(40);
        assertNotNull(locations);

        assertFalse(locations.isEmpty());

        for (LocationsPOJO location : locations) {
            if (location.getName().equals("London")) {
                assertEquals("London", location.getName());
            }
        }
    }

    @Test
    @Order(7)
    public void deleteByIdTest(){
        LocationsPOJO location = locationsDAO.getLocationByName("Atlantida").orElse(null);
        assertNotNull(location);

        locationsDAO.delete(location.getId());

        location = locationsDAO.getLocationByName("Atlantida").orElse(null);
        assertNull(location);
    }

}