package com.wallhack.weathermap.DAO.Base;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public abstract class BaseDAO<T> implements ICRUDContract<T> {
    private final Class<T> entityClass;
    private final EntityManagerFactory emf = PersistenceService.getInstance().getEntityManagerFactory();

    public BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void save(T entity) {
        executeInsideTransaction(entityManager -> entityManager
                .persist(entity));
    }

    @Override
    public void update(T entity) {
        executeInsideTransaction(entityManager -> {
            entityManager.merge(entity);
            entityManager.flush();
        });
    }

    @Override
    public List<T> findAll() {
        List<T> result = new ArrayList<>();

        executeInsideTransaction(entityManager -> result.addAll(entityManager
                .createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList()));

        return result;
    }

    protected void executeInsideTransaction(Consumer<EntityManager> action) {
        try (EntityManager entityManager = emf.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                action.accept(entityManager);
                transaction.commit();
            } catch (RuntimeException e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                    log.error("Transaction rollback due to: {}", e.getMessage(), e);
                }
                log.error(e.getMessage());
            }
        }
    }

}