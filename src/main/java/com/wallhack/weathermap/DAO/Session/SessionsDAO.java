package com.wallhack.weathermap.DAO.Session;

import com.wallhack.weathermap.DAO.Base.BaseDAO;
import com.wallhack.weathermap.Model.SessionsPOJO;
import jakarta.persistence.TypedQuery;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class SessionsDAO extends BaseDAO<SessionsPOJO> implements ICRUDSessions {
    public SessionsDAO() {
        super(SessionsPOJO.class);
    }

    @Override
    public Optional<SessionsPOJO> findById(UUID id) {
        AtomicReference<Optional<SessionsPOJO>> result = new AtomicReference<>();

        executeInsideTransaction(entityManager -> result.set(Optional.ofNullable(entityManager.find(SessionsPOJO.class, id))));

        return result.get();
    }

    @Override
    public void delete(UUID id) {
        executeInsideTransaction(entityManager -> {
            SessionsPOJO entity = entityManager.find(SessionsPOJO.class, id);
            if (entity != null) {
                entityManager.remove(entity);
            }
        });
    }

    @Override
    public Optional<SessionsPOJO> getSessionByUserId(long userId) {
        AtomicReference<Optional<SessionsPOJO>> result = new AtomicReference<>(Optional.empty());

        executeInsideTransaction(entityManager -> {
            TypedQuery<SessionsPOJO> query = entityManager
                    .createQuery("SELECT u FROM SessionsPOJO u JOIN FETCH u.userId l WHERE l.id = :userId"
                            , SessionsPOJO.class);
            query.setParameter("userId", userId);
            result.set(Optional.ofNullable(query.getSingleResult()));
        });

        return result.get();
    }
}
