package com.wallhack.weathermap.utils;

import jakarta.persistence.EntityManager;

@FunctionalInterface
public interface EntityManagerConsumer {
    void accept(EntityManager entity);
}
