package com.wallhack.weathermap.DAO;

import com.wallhack.weathermap.Model.LocationsPOJO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationsDAO extends BaseDAO<LocationsPOJO> implements ICRUDLocations {

    public LocationsDAO() {
        super(LocationsPOJO.class);
    }

    @Override
    public List<LocationsPOJO> getLocationByUserId(long userId) {
        List<LocationsPOJO> locations = new ArrayList<>();

        try (EntityManager entityManager = emf.createEntityManager()){
            TypedQuery<LocationsPOJO> query = entityManager.createQuery("SELECT u FROM LocationsPOJO u WHERE u.userId = :userId"
                    , LocationsPOJO.class);
            query.setParameter("userId", userId);
            locations = query.getResultList();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return locations;
    }

    @Override
    public Optional<LocationsPOJO> getLocationByName(String name) {
        LocationsPOJO location = null;

        try (EntityManager entityManager = emf.createEntityManager()){
            TypedQuery<LocationsPOJO> query = entityManager.createQuery("SELECT u FROM LocationsPOJO u WHERE u.name = :name"
                    , LocationsPOJO.class);
            query.setParameter("name", name);
            location = query.getSingleResult();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(location);
    }

    @Override
    public Optional<LocationsPOJO> getLocationByCoordinates(double latitude, double longitude) {
        LocationsPOJO location = null;

        try (EntityManager entityManager = emf.createEntityManager()){
            TypedQuery<LocationsPOJO> query = entityManager
                    .createQuery("SELECT u FROM LocationsPOJO u WHERE u.longitude = :longitude " +
                                    "AND u.latitude = :latitude"
                    , LocationsPOJO.class);
            query.setParameter("longitude", longitude);
            query.setParameter("latitude", latitude);
            location = query.getSingleResult();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(location);
    }
}
