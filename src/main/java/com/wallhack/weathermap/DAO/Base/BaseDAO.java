package com.wallhack.weathermap.DAO.Base;

import com.wallhack.weathermap.utils.Conectors.PersistenceService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class BaseDAO<T> implements ICRUDContract<T> {
    protected final EntityManagerFactory emf = PersistenceService.getInstance().getEntityManagerFactory();
    private final Class<T> entityClass;

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
                }
                e.printStackTrace();
            }
        }
    }

}