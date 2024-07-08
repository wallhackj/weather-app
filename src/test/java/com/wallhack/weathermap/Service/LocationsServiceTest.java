package com.wallhack.weathermap.Service;

import com.wallhack.weathermap.Model.LocationsPOJO;
import com.wallhack.weathermap.Model.UsersPOJO;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LocationsServiceTest {
    @Mock
    private LocationsService locationsService;
    @Mock
    private UsersService usersService;
    @InjectMocks
    private LocationsServiceTest locationsServiceTest;
    private final Random random = new Random();
    private AutoCloseable autoCloseable;

    @BeforeEach
    public void init(){
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @Order(1)
    public void insertTest() {
        String name = "Atlantida";
        double lat = random.nextDouble();
        double lng = random.nextDouble();
        UsersPOJO user = new UsersPOJO();
        user.setLogin("admin");

        when(usersService.getUser("admin")).thenReturn(Optional.of(user));

        LocationsPOJO location = new LocationsPOJO(lat, lng, name, user);
        doNothing().when(locationsService).addLocation(location);

        locationsService.addLocation(location);

        when(locationsService.findLocationByName(name)).thenReturn(Optional.of(location));

        LocationsPOJO testLocation = locationsService.findLocationByName(name).orElse(null);
        assertNotNull(testLocation);
        assertEquals(name, testLocation.getName());
    }

    @Test
    @Order(2)
    public void updateTest() {
        String name = "Atlantida";
        LocationsPOJO location = new LocationsPOJO( 47.0, 28.0, name, new UsersPOJO());

        when(locationsService.findLocationByName(name)).thenReturn(Optional.of(location));

        LocationsPOJO retrievedLocation = locationsService.findLocationByName(name).orElse(null);
        assertNotNull(retrievedLocation);
        assertEquals(name, retrievedLocation.getName());

        var lon = 2222.0;
        var lat = 3333.0;
        retrievedLocation.setLongitude(lon);
        retrievedLocation.setLatitude(lat);
        doNothing().when(locationsService).updateLocation(retrievedLocation);

        locationsService.updateLocation(retrievedLocation);

        when(locationsService.findLocationByName(name)).thenReturn(Optional.of(retrievedLocation));

        LocationsPOJO testLocation = locationsService.findLocationByName(name).orElse(null);
        assertNotNull(testLocation);
        assertEquals(name, testLocation.getName());
        assertEquals(lon, testLocation.getLongitude());
        assertEquals(lat, testLocation.getLatitude());
    }

    @Test
    @Order(3)
    public void findByCoordinates() {
        LocationsPOJO location = new LocationsPOJO(47.0056, 28.8575,"Chișinău", new UsersPOJO());

        when(locationsService.findLocationByCoordinates(47.0056, 28.8575)).thenReturn(Optional.of(location));

        LocationsPOJO testLocation = locationsService.findLocationByCoordinates(47.0056, 28.8575).orElse(null);
        assertNotNull(testLocation);
        assertEquals("Chișinău", testLocation.getName());
    }

    @Test
    @Order(4)
    public void findByIdTest() {
        LocationsPOJO location = new LocationsPOJO(51.5085, -0.1257, "London", new UsersPOJO());

        when(locationsService.getLocationById(1)).thenReturn(Optional.of(location));

        LocationsPOJO testLocation = locationsService.getLocationById(1).orElse(null);
        assertNotNull(testLocation);
        assertEquals("London", testLocation.getName());
    }

    @Test
    @Order(5)
    public void findAllTest() {
        LocationsPOJO location1 = new LocationsPOJO(51.5085, -0.1257, "London", new UsersPOJO());
        LocationsPOJO location2 = new LocationsPOJO(47.0056, 28.8575,"Chișinău", new UsersPOJO());

        List<LocationsPOJO> locations = List.of(location1, location2);

        when(locationsService.getAllLocations()).thenReturn(locations);

        List<LocationsPOJO> testLocations = locationsService.getAllLocations();
        assertNotNull(testLocations);
        assertFalse(testLocations.isEmpty());
    }

    @Test
    @Order(6)
    public void findByNameTest() {
        LocationsPOJO location = new LocationsPOJO(51.5085, -0.1257,"London", new UsersPOJO());
        List<LocationsPOJO> locations = List.of(location);

        when(locationsService.getAllLocationByUserId(40)).thenReturn(locations);

        List<LocationsPOJO> testLocations = locationsService.getAllLocationByUserId(40);
        assertNotNull(testLocations);
        assertFalse(testLocations.isEmpty());

        assertTrue(testLocations.stream().anyMatch(loc -> "London".equals(loc.getName())));
    }

    @Test
    @Order(7)
    public void deleteByIdTest() {
        LocationsPOJO location = new LocationsPOJO(47.0, 28.0,"Atlantida", new UsersPOJO());

        when(locationsService.findLocationByName("Atlantida")).thenReturn(Optional.of(location));
        doNothing().when(locationsService).deleteLocation(location.getId());

        locationsService.deleteLocation(location.getId());

        when(locationsService.findLocationByName("Atlantida")).thenReturn(Optional.empty());

        LocationsPOJO testLocation = locationsService.findLocationByName("Atlantida").orElse(null);
        assertNull(testLocation);
    }
}