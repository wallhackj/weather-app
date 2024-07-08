package com.wallhack.weathermap.DAO.Location;

import com.wallhack.weathermap.DAO.Base.AdvancedDAO;
import com.wallhack.weathermap.Model.LocationsPOJO;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class LocationsDAO extends AdvancedDAO<LocationsPOJO> implements ICRUDLocations {

    public LocationsDAO() {
        super(LocationsPOJO.class);
    }

    @Override
    public List<LocationsPOJO> getLocationByUserId(long userId) {
        List<LocationsPOJO> locations = new ArrayList<>();

        executeInsideTransaction(entityManager -> {
            TypedQuery<LocationsPOJO> query = entityManager
                    .createQuery("SELECT u FROM LocationsPOJO u JOIN FETCH u.user l WHERE l.id = :userId"
                            , LocationsPOJO.class);
            query.setParameter("userId", userId);
            locations.addAll(query.getResultList());
        });

        return locations;
    }

    @Override
    public Optional<LocationsPOJO> getLocationByName(String name) {
        AtomicReference<Optional<LocationsPOJO>> result = new AtomicReference<>(Optional.empty());

        executeInsideTransaction(entityManager -> {
            TypedQuery<LocationsPOJO> query = entityManager.createQuery("SELECT u FROM LocationsPOJO u WHERE u.name = :name"
                    , LocationsPOJO.class);
            query.setParameter("name", name);
            result.set(Optional.ofNullable(query.getSingleResult()));
        });

        return result.get();
    }

    @Override
    public Optional<LocationsPOJO> getLocationByCoordinates(double latitude, double longitude) {
        AtomicReference<Optional<LocationsPOJO>> result = new AtomicReference<>(Optional.empty());

        executeInsideTransaction(entityManager -> {
            TypedQuery<LocationsPOJO> query = entityManager
                    .createQuery("SELECT u FROM LocationsPOJO u WHERE u.longitude = :longitude " +
                                    "AND u.latitude = :latitude"
                            , LocationsPOJO.class);
            query.setParameter("longitude", longitude);
            query.setParameter("latitude", latitude);
            result.set(Optional.ofNullable(query.getSingleResult()));
        });

        return result.get();
    }
}
