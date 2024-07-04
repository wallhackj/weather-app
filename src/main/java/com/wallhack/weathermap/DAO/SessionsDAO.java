package com.wallhack.weathermap.DAO;

import com.wallhack.weathermap.Model.SessionsPOJO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

public class SessionsDAO extends BaseDAO<SessionsPOJO> implements ICRUDSessions {
    public SessionsDAO() {
        super(SessionsPOJO.class);
    }

    @Override
    public Optional<SessionsPOJO> getSessionByUserId(long userId) {
        SessionsPOJO session = null;

        try (EntityManager entityManager = emf.createEntityManager()){
            TypedQuery<SessionsPOJO> query = entityManager
                    .createQuery("SELECT u FROM Locations u JOIN FETCH u.user l WHERE l.id = :userId"
                    , SessionsPOJO.class);
            query.setParameter("userId", userId);
            session = query.getSingleResult();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(session);
    }
}
