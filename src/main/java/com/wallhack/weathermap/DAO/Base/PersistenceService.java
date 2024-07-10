package com.wallhack.weathermap.DAO.Base;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PersistenceService {
    private static PersistenceService instance;
    private final EntityManagerFactory emf;

    private PersistenceService() {
        this.emf = Persistence.createEntityManagerFactory("WeatherAppPU");
    }

    public static PersistenceService getInstance() {
        if (instance == null) {
            instance = new PersistenceService();
        }
        return instance;
    }
    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}
