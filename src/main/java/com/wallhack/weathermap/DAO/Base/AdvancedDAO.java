package com.wallhack.weathermap.DAO.Base;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AdvancedDAO<T> extends BaseDAO<T>{
    Class<T> entityClass;

    public AdvancedDAO(Class<T> entityClass) {
        super(entityClass);
        this.entityClass = entityClass;
    }

    public void delete(long id) {
        executeInsideTransaction(entityManager -> {
            T entity = entityManager.find(entityClass, id);
            if (entity != null) {
                entityManager.remove(entity);
                entityManager.flush();
            }
        });
    }

    public Optional<T> findById(long id) {
        AtomicReference<Optional<T>> result = new AtomicReference<>(Optional.empty());

        executeInsideTransaction(entityManager -> result.set(Optional.ofNullable(entityManager
                .find(entityClass, id))));

        return result.get();
    }
}
